package uz.mobiler.musicplayer.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.MainActivity
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import uz.mobiler.musicplayer.utils.ConstValues.NAVIGATION_OPTION
import uz.mobiler.musicplayer.utils.ConstValues.SONG_EXTRA
import uz.mobiler.musicplayer.utils.ConstValues.TAG
import uz.mobiler.musicplayer.utils.NavigationOptions
import java.io.FileNotFoundException
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service() {

    companion object {
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_STOP = "action_stop"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "music_channel"
    }

    @Inject
    lateinit var songRepository: SongRepository

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var notificationManager: NotificationManagerCompat

    private val binder = MusicServiceBinder()
    private var currentSongIndex: Int = 0 // Track the current song index
    private var currentSong: Song? = null // Track the current song
    private var songList: List<Song> = listOf() // List of songs to play

    private var isFirstTime = true

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        initializeMediaSession()
        initializePlayer()
    }

    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(this, "MusicService")
        mediaSession.isActive = true
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(exoPlayer)
    }

    private fun initializePlayer() {
        exoPlayer.addListener(playerListener)
    }

    private fun initializeNotification() {
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification channel for music playback controls"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val isPlaying = exoPlayer.isPlaying

        Log.d(
            TAG,
            "Creating notification for song: ${currentSong?.title}, Artist: ${currentSong?.artist}"
        )

        val albumArtBitmap = currentSong?.albumArtUri?.let { uri ->
            try {
                BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(uri)))
            } catch (e: FileNotFoundException) {
                // Handle the missing album art (e.g., set a default image)
                null // Or set a default bitmap here
            }
        }

        val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        val playPauseAction = NotificationCompat.Action.Builder(
            playPauseIcon,
            if (isPlaying) "Pause" else "Play",
            getActionPendingIntent(if (isPlaying) ACTION_PAUSE else ACTION_PLAY)
        ).build()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(currentSong?.title ?: "Unknown Title") // Fallback if title is null
            setContentText(currentSong?.artist ?: "Unknown Artist") // Fallback if artist is null
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentIntent(createContentIntent())
            setOngoing(isPlaying)
            addAction(R.drawable.ic_previous, "Previous", getActionPendingIntent(ACTION_PREVIOUS))
            addAction(playPauseAction)
            addAction(R.drawable.ic_next, "Next", getActionPendingIntent(ACTION_NEXT))

            albumArtBitmap?.let { bitmap ->
                setLargeIcon(bitmap) // Set album art if available
            }

            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(1)
            )
        }

        return notificationBuilder.build()
    }

    private fun updateNotification() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Log.d(TAG, "updateNotification: notification updating")
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    private fun createContentIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getActionPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        when (intent?.action) {
            ACTION_PLAY -> {
                if (currentSong == null) {
                    currentSong = intent.getParcelableExtra(SONG_EXTRA)
                    initializeSongList(intent.getStringExtra(NAVIGATION_OPTION))
                    initializeNotification()
                }
                Log.d(TAG, "onStartCommand: currentSongIndex $currentSongIndex")
                Log.d(
                    TAG,
                    "onStartCommand: songList.indexOf(currentSong) ${songList.indexOf(currentSong)}"
                )
                Log.d(TAG, "onStartCommand: ${currentSongIndex != songList.indexOf(currentSong)}")
                if (isFirstTime) {
                    isFirstTime = false
                    playSong(currentSong!!)
                } else {
                    if (!exoPlayer.isPlaying) {
                        exoPlayer.play()
                    }
                }
            }

            ACTION_PAUSE -> {
                exoPlayer.pause()
            }

            ACTION_NEXT -> {
                playNextSong()
            }

            ACTION_PREVIOUS -> {
                playPreviousSong()
            }

            ACTION_STOP -> {
                stopSelf()
            }
            else -> {
                val song: Song? = intent?.getParcelableExtra("song")
                song?.let { playSong(it) }
            }
        }
        return START_STICKY
    }

    private fun initializeSongList(
        navigationOption: String?,
        playlistId: Long? = null,
        searchQuery: String? = null
    ) {
        when (navigationOption.toString()) {
            NavigationOptions.HOME.navigationName -> {
                songList = songRepository.getAllSongs()
            }

            NavigationOptions.FAVORITE.navigationName -> {
                songList = songRepository.getFavoriteSongs()
            }

            NavigationOptions.RECENT.navigationName -> {
                songList = songRepository.getRecentSongs()
            }

            NavigationOptions.PLAYLIST.navigationName -> {
                songList = songRepository.getPlaylistSongs(playlistId ?: 0)
            }

            NavigationOptions.SEARCH.navigationName -> {
                songList = songRepository.search(searchQuery.toString())
            }

            else -> {
                songList = emptyList()
            }
        }
    }

    private fun playSong(song: Song) {
        currentSongIndex = songList.indexOf(song)
        currentSong = song
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(song.filePath)))
        exoPlayer.prepare()
        exoPlayer.play()
        updateNotification()
    }

    private fun playNextSong() {
        playSong(songList[(currentSongIndex + 1) % songList.size])
    }

    private fun playPreviousSong() {
        playSong(songList[(currentSongIndex - 1 + songList.size) % songList.size])
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    updateNotification()
                }
                Player.STATE_ENDED -> {
                    playNextSong()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateNotification()
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
        mediaSession.release()
    }
}