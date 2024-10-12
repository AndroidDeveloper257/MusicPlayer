package uz.mobiler.musicplayer.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.databinding.FragmentFavoriteBinding
import uz.mobiler.musicplayer.utils.ConstValues.NAVIGATION_ARGUMENT_KEY
import uz.mobiler.musicplayer.utils.NavigationOptions

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.apply {

            tv.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(NAVIGATION_ARGUMENT_KEY, NavigationOptions.FAVORITE.value)

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