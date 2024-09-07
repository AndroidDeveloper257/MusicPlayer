package uz.mobiler.musicplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.databinding.FragmentAllSongsBinding
import uz.mobiler.musicplayer.utils.ConstValues.NAVIGATION_ARGUMENT_KEY
import uz.mobiler.musicplayer.utils.NavigationOptions

class AllSongsFragment : Fragment() {

    private var _binding: FragmentAllSongsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSongsBinding.inflate(layoutInflater)
        binding.apply {

            tv.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(NAVIGATION_ARGUMENT_KEY, NavigationOptions.HOME.value)
                bundle.putInt("position", 0)

                findNavController().navigate(
                    resId = R.id.action_homeFragment_to_playerFragment,
                    args = bundle
                )
            }

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}