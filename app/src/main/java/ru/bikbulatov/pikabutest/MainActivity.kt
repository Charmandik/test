package ru.bikbulatov.pikabutest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import ru.bikbulatov.pikabutest.ui.PostsFeedFragment
import ru.bikbulatov.pikabutest.ui.SavedPostsFragment
import ru.bikbulatov.pikabutest.ui.adapters.ViewPagerAdapter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureViewPager()
        configureBottomNav()
    }

    private fun configureViewPager() {
        vpMain?.offscreenPageLimit = 2
        val adapter: ViewPagerAdapter? = ViewPagerAdapter(supportFragmentManager)
        adapter?.addFrag(PostsFeedFragment())
        adapter?.addFrag(SavedPostsFragment())
        vpMain?.adapter = adapter
        vpMain?.adapter?.notifyDataSetChanged()
    }

    private fun configureBottomNav() {
        bnvMain?.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_posts -> vpMain?.currentItem = 0
                R.id.navigation_saved -> vpMain?.currentItem = 1
            }
            true
        }
    }
}