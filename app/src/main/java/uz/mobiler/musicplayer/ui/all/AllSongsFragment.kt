package uz.mobiler.musicplayer.ui.all

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.adapters.SongAdapter
import uz.mobiler.musicplayer.databinding.FragmentAllSongsBinding
import uz.mobiler.musicplayer.models.Song

@AndroidEntryPoint
class AllSongsFragment : Fragment() {

    private var _binding: FragmentAllSongsBinding? = null
    private val binding get() = _binding!!

    private val allSongsViewModel: AllSongsViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var songAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSongsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            allSongsViewModel.allSongs.observe(viewLifecycleOwner) { songList ->
                songAdapter = SongAdapter(
                    songList = songList,
                    { song ->
                        playSong(song)
                    },
                    { song ->
                        shareSong(song)
                    }, { song ->
                        moreOptions(song)
                    }
                )
                allSongsRv.adapter = songAdapter
            }

            requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        allSongsViewModel.getAllSongs()
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                requireActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showRationaleDialog()
                        } else {
                            showSettingsDialog()
                        }
                    }
                }
            checkPermissions()
        }
    }

    private fun playSong(song: Song) {

    }

    private fun shareSong(song: Song) {

    }

    private fun moreOptions(song: Song) {

    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.permission_rationale_title))
            .setMessage(resources.getString(R.string.permission_rationale_body))
            .setPositiveButton(resources.getString(R.string.permission_rationale_positive_button)) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton(
                resources.getString(R.string.permission_rationale_negative_button),
                null
            )
            .create()
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.permission_settings_title))
            .setMessage(resources.getString(R.string.permission_settings_body))
            .setPositiveButton(resources.getString(R.string.permission_settings_positive_button)) { _, _ ->
                // Open app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(
                resources.getString(R.string.permission_settings_negative_button),
                null
            )
            .create()
            .show()
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                allSongsViewModel.getAllSongs()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showRationaleDialog()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}