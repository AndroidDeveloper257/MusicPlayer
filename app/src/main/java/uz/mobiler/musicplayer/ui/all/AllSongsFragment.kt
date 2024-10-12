package uz.mobiler.musicplayer.ui.all

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.databinding.FragmentAllSongsBinding
import uz.mobiler.musicplayer.utils.ConstValues.TAG

@AndroidEntryPoint
class AllSongsFragment : Fragment() {

    private var _binding: FragmentAllSongsBinding? = null
    private val binding get() = _binding!!

    private val allSongsViewModel: AllSongsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSongsBinding.inflate(layoutInflater)
        binding.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_MEDIA_AUDIO
                )
            } else {
                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

            allSongsViewModel.allSongs.observe(viewLifecycleOwner) { songList ->
                Log.d(TAG, "onCreateView: ${songList.size} songs loaded")
                songList?.forEach { song ->
                    Log.d(TAG, "onCreateView: $song")
                }
            }

            return root
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "granted", Toast.LENGTH_SHORT).show()
                allSongsViewModel.getAllSongs()
            } else {
                Toast.makeText(requireContext(), "denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}