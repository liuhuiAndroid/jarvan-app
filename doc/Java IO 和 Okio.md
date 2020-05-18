#### Java IO

#### NIO

#### Okio

- Okio：A modern I/O library for Android, Kotlin, and Java.

  适用于 Android，Kotlin 和 Java 的现代 I / O 库。

- 官网：https://github.com/square/okio

- Okio 基础使用部分：https://www.jianshu.com/p/3e0935bf2d45

- Okio 源码分析部分：https://www.jianshu.com/p/dccb6e1bd536

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