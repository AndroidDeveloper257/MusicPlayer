package uz.mobiler.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.mobiler.musicplayer.databinding.FragmentPlaylistSongsBinding

class PlaylistSongsFragment : Fragment() {

    private var _binding: FragmentPlaylistSongsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistSongsBinding.inflate(layoutInflater)
        binding.apply {

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}