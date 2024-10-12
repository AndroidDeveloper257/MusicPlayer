package uz.mobiler.musicplayer.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.mobiler.musicplayer.ui.all.AllSongsFragment
import uz.mobiler.musicplayer.ui.favorite.FavoriteFragment
import uz.mobiler.musicplayer.ui.playlists.PlaylistsFragment
import uz.mobiler.musicplayer.ui.recent.RecentFragment
import uz.mobiler.musicplayer.ui.search.SearchFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllSongsFragment()
            1 -> SearchFragment()
            2 -> FavoriteFragment()
            3 -> RecentFragment()
            4 -> PlaylistsFragment()
            else -> Fragment()
        }
    }
}