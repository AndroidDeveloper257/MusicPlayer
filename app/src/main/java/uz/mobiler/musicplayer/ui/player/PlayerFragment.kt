package uz.mobiler.musicplayer.ui.player

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.databinding.FragmentPlayerBinding
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.utils.ConstValues.NAVIGATION_OPTION
import uz.mobiler.musicplayer.utils.ConstValues.PLAYLIST_ID
import uz.mobiler.musicplayer.utils.ConstValues.SEARCH_QUERY
import uz.mobiler.musicplayer.utils.ConstValues.SONG_EXTRA

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            songTitleTv.isSelected = true
            artistAlbumNameTv.isSelected = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(
                    SONG_EXTRA,
                    Song::class.java
                )?.let {
                    playerViewModel.setCurrentSong(
                        it
                    )
                }
                playerViewModel.initializeSongList(
                    navigationOption = arguments?.getString(NAVIGATION_OPTION).toString(),
                    searchQuery = arguments?.getString(SEARCH_QUERY).toString(),
                    playlistId = arguments?.getLong(PLAYLIST_ID)
                )
            }

            playerViewModel.currentSong.observe(viewLifecycleOwner) {
                songTitleTv.text = it.title
                artistAlbumNameTv.text = it.artist
                Glide.with(requireContext())
                    .load(it.albumArtUri)
                    .placeholder(R.drawable.ic_music)
                    .into(albumArt)
                durationTv.text = playerViewModel.convertTime(it.duration)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}