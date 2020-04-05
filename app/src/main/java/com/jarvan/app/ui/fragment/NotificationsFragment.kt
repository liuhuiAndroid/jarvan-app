package com.jarvan.app.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.jarvan.app.databinding.FragmentNotificationsBinding
import com.jarvan.app.viewmodels.NotificationsViewModel

private const val videoUrl = "https://video.acadsoc.com.cn/WinfromUploads/video/2019-07-16/2019071614514172.mp4"

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    private lateinit var fragmentNotificationsBinding: FragmentNotificationsBinding

    private lateinit var player: SimpleExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        fragmentNotificationsBinding =
            FragmentNotificationsBinding.inflate(inflater, container, false)
        return fragmentNotificationsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlayer()
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        fragmentNotificationsBinding.mPlayerView.player = player

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            "Jarvan"
        )
        // This is the MediaSource representing the media to be played.
        val videoUri =
            Uri.parse(videoUrl)
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(videoUri)
        // Prepare the player with the source.
        player.prepare(videoSource)
        // Start
        player.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player.let {
            player.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.let {
            player.stop()
            player.release()
        }
    }

}
