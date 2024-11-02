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
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.adapters.SongAdapter
import uz.mobiler.musicplayer.databinding.FragmentAllSongsBinding
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.service.MusicService
import uz.mobiler.musicplayer.utils.ConstValues.NAVIGATION_OPTION
import uz.mobiler.musicplayer.utils.ConstValues.SONG_EXTRA
import uz.mobiler.musicplayer.utils.NavigationOptions

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
                if (songList.isEmpty()) {
                    emptyTv.visibility = View.VISIBLE
                } else {
                    emptyTv.visibility = View.GONE
                }
            }

            requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        allSongsViewModel.getAllSongs()
                        permissionExplanationBtn.visibility = View.GONE
                        permissionExplanationTv.visibility = View.GONE
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
        val serviceIntent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(SONG_EXTRA, song)
            putExtra(NAVIGATION_OPTION, NavigationOptions.HOME.navigationName)
        }
        val bundle = Bundle().apply {
            putParcelable(SONG_EXTRA, song)
            putString(NAVIGATION_OPTION, NavigationOptions.HOME.navigationName)
        }
        requireContext().startService(serviceIntent)
        findNavController().navigate(
            resId = R.id.action_homeFragment_to_playerFragment,
            args = bundle
        )
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
        binding.apply {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    permissionExplanationBtn.visibility = View.GONE
                    permissionExplanationTv.visibility = View.GONE
                    emptyTv.visibility = View.GONE
                    allSongsViewModel.getAllSongs()
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    showRationaleDialog()
                }

                else -> {
                    permissionExplanationTv.visibility = View.VISIBLE
                    permissionExplanationBtn.visibility = View.VISIBLE
                    permissionExplanationBtn.setOnClickListener {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}