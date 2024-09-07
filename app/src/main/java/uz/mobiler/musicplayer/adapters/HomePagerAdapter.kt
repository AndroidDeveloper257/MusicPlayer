package uz.mobiler.musicplayer.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.mobiler.musicplayer.ui.AllSongsFragment
import uz.mobiler.musicplayer.ui.FavoriteFragment
import uz.mobiler.musicplayer.ui.PlaylistsFragment
import uz.mobiler.musicplayer.ui.RecentFragment
import uz.mobiler.musicplayer.ui.SearchFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllSongsFragment()
            1 -> FavoriteFragment()
            2 -> RecentFragment()
            3 -> PlaylistsFragment()
            4 -> SearchFragment()
            else -> Fragment()
        }
    }
}