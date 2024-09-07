package uz.mobiler.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.mobiler.musicplayer.databinding.FragmentPlayerBinding
import uz.mobiler.musicplayer.utils.ConstValues.NAVIGATION_ARGUMENT_KEY
import uz.mobiler.musicplayer.utils.NavigationOptions

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater)
        binding.apply {

            val argument = arguments?.getInt(NAVIGATION_ARGUMENT_KEY) ?: 0
            when (argument) {
                NavigationOptions.HOME.value -> {
                    tv.text = "Came from home\nSongs will come from storage"
                }

                NavigationOptions.SEARCH.value -> {
                    tv.text = "Came from search\nSongs will come from storage by search key"
                }

                NavigationOptions.FAVORITE.value -> {
                    tv.text = "Came from favorite\nFavorite songs come from room"
                }

                NavigationOptions.PLAYLIST.value -> {
                    tv.text = "Came from playlist\nPlaylist songs come from room by playlist id"
                    arguments?.getInt("playlist_id")
                    arguments?.getInt("position")
                }

                NavigationOptions.RECENT.value -> {
                    tv.text = "Came from recent\nRecent songs"
                }

                else -> {}
            }


            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}