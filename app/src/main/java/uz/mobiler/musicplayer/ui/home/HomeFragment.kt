package uz.mobiler.musicplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import uz.mobiler.musicplayer.R
import uz.mobiler.musicplayer.adapters.HomePagerAdapter
import uz.mobiler.musicplayer.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.apply {
            homeViewPager.adapter = HomePagerAdapter(this@HomeFragment)

            homeViewPager.isUserInputEnabled = false

            bottomNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        homeViewPager.setCurrentItem(0, true)
                    }

                    R.id.search -> {
                        homeViewPager.setCurrentItem(1, true)
                    }

                    R.id.favorite -> {
                        homeViewPager.setCurrentItem(2, true)
                    }

                    R.id.recent -> {
                        homeViewPager.setCurrentItem(3, true)
                    }

                    R.id.playlist -> {
                        homeViewPager.setCurrentItem(4, true)
                    }


                    else -> {}
                }
                true
            }

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}