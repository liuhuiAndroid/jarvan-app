#### Java IO

#### NIO

#### Okio

- Okio：A modern I/O library for Android, Kotlin, and Java.

  适用于 Android，Kotlin 和 Java 的现代 I / O 库。

- 官网：https://github.com/square/okio

- Okio 基础使用部分：https://www.jianshu.com/p/3e0935bf2d45

- Okio 源码分析部分：https://www.jianshu.com/p/dccb6e1bd536

- Okio源码解析：https://juejin.im/post/5e0ed3835188253a5c7d1536

- 特点：

  - 它也是基于插管的，而且是单向的，输入源叫 Source，输出目标叫 Sink
  - 支持 Buffer（缓冲）
    - 像 NIO 一样，可以对 Buffer 进行操作
    - 但不强制使用 Buffer

- 核心类

  - ByteString：不可变的字节序列，像是 String 的兄弟
  - Buffer：可变的字节序列，类似 ArrayList
  - Source：类似于 Java 的 Inputstream
  - Sink：类似于 Java 的 Outputstream

- 用法：

  - 按行读取文本

    ```java
    public void readLines(File file) throws IOException {
        // 1. 构建 Source，类似于 Java 的 InputStream
        try (Source fileSource = Okio.source(file);
             // 2. 构建 BufferedSource，类似于 Java 的 BufferedInputStream
             BufferedSource bufferedSource = Okio.buffer(fileSource)) {
            while (true) {
                // 3. 按 UTF8 格式逐行读取字符
                String line = bufferedSource.readUtf8Line();
                if (line == null) break;
                if (line.contains("square")) {
                    System.out.println(line);
                }
            }
        }
    }
    ```

  - 写入文本

    ```java
    public void writeEnv(File file) throws IOException {
        // 1. 构建 Sink，类似于 Java 的 OutputStream
        try (Sink fileSink = Okio.sink(file);
             // 2. 构建 BufferedSink，类似于 Java 的 BufferedOutputStream
             BufferedSink bufferedSink = Okio.buffer(fileSink)) {
            // 3. 写入文本
            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                bufferedSink.writeUtf8(entry.getKey());
                bufferedSink.writeUtf8("=");
                bufferedSink.writeUtf8(entry.getValue());
                bufferedSink.writeUtf8("\n");
            }
        }
    }
    ```

- 源码解析

  - Sink：类似于 Java 的 Outputstream

    ```kotlin
    interface Sink : Closeable, Flushable {
      fun write(source: Buffer, byteCount: Long)
    
      override fun flush()
    
      fun timeout(): Timeout
    
      override fun close()
    }
    ```

  - Okio.sink(file) 方法基于 OutputStream 获取一个 Sink

    ```java
      private static Sink sink(final OutputStream out, final Timeout timeout) {
        if (out == null) throw new IllegalArgumentException("out == null");
        if (timeout == null) throw new IllegalArgumentException("timeout == null");
    	// 构造 Sink 的匿名内部类并返回
        return new Sink() {
          // 主要实现了 write 方法
          @Override public void write(Buffer source, long byteCount) throws IOException {
            // 状态检验
            checkOffsetAndCount(source.size, 0, byteCount);
            while (byteCount > 0) {
              timeout.throwIfReached();
              // 从 Buffer 中获取了一个 Segment
              Segment head = source.head;
              int toCopy = (int) Math.min(byteCount, head.limit - head.pos);
              // 从 Segment 中取出数据，写入 Sink 所对应的 OutputStream
              out.write(head.data, head.pos, toCopy);
    
              head.pos += toCopy;
              byteCount -= toCopy;
              source.size -= toCopy;
    
              if (head.pos == head.limit) {
                source.head = head.pop();
                // 对当前 Segment 进行回收
                SegmentPool.recycle(head);
              }
            }
          }
    
          @Override public void flush() throws IOException {
            out.flush();
          }
    
          @Override public void close() throws IOException {
            out.close();
          }
    
          @Override public Timeout timeout() {
            return timeout;
          }
    
          @Override public String toString() {
            return "sink(" + out + ")";
          }
        };
      }
    ```

  - Source：类似于 Java 的 Inputstream

    ```kotlin
    interface Source : Closeable {
      fun read(sink: Buffer, byteCount: Long): Long
    
      fun timeout(): Timeout
    
      override fun close()
    }
    ```

  - 通过 source 方法根据  InputStream 创建一个 Source

    ```java
      private static Source source(final InputStream in, final Timeout timeout) {
        if (in == null) throw new IllegalArgumentException("in == null");
        if (timeout == null) throw new IllegalArgumentException("timeout == null");
    	// 构建并实现了 Source 的一个匿名内部类并返回
        return new Source() {
          @Override public long read(Buffer sink, long byteCount) throws IOException {
            // 状态检测
            if (byteCount < 0) throw new IllegalArgumentException("byteCount < 0: " + byteCount);
            if (byteCount == 0) return 0;
            try {
              timeout.throwIfReached();
              // 获取到一个可以写入的 Segment
              Segment tail = sink.writableSegment(1);
              int maxToCopy = (int) Math.min(byteCount, Segment.SIZE - tail.limit);
              // 从 InputStream 中读取数据向 Segment 中写入
              int bytesRead = in.read(tail.data, tail.limit, maxToCopy);
              if (bytesRead == -1) return -1;
              tail.limit += bytesRead;
              sink.size += bytesRead;
              return bytesRead;
            } catch (AssertionError e) {
              if (isAndroidGetsocknameError(e)) throw new IOException(e);
              throw e;
            }
          }
    
          @Override public void close() throws IOException {
            in.close();
          }
    
          @Override public Timeout timeout() {
            return timeout;
          }
    
          @Override public String toString() {
            return "source(" + in + ")";
          }
        };
      }
    ```

