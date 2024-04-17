package com.mobile.core.ui.base

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager

sealed class ViewPagerItem<T : Fragment>(val fragment: T) {
    data class OnlyPage<T : Fragment>(val page: T) : ViewPagerItem<T>(page)
    data class PageWithTitle<T : Fragment>(val page: T, val title: String) : ViewPagerItem<T>(page)
}

open class LazyPagerAdapter<T : BindingFragmentLazyPager<*>>(
    fm: FragmentManager,
    val pages: List<ViewPagerItem<T>>,
    lifecycleOwner: LifecycleOwner
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var viewPager: ViewPager? = null
    private val onPageChange = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            pages.getOrNull(position)?.fragment?.updateUiIfNeed(isDefaultTab = false)
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    viewPager?.removeOnPageChangeListener(onPageChange)
                    viewPager = null
                }
            }
        })
    }

    fun attachWithViewPager(viewPager: ViewPager, currentIndex: Int = 0) {
        this.viewPager = viewPager
        pages.map { it.fragment }.onEach { it.invokeLazyPager() }
        viewPager.addOnPageChangeListener(onPageChange)
        viewPager.adapter = this
        viewPager.offscreenPageLimit = count
        if (currentIndex in 0 until count) {
            viewPager.setCurrentItem(currentIndex, false)
            pages.getOrNull(currentIndex)?.fragment?.updateUiIfNeed(true)
        }
    }

    final override fun getItem(position: Int): Fragment {
        return this.pages[position].fragment
    }

    fun getItemAt(position: Int): ViewPagerItem<T> {
        return pages[position]
    }

    fun indexOf(condition: (f: T) -> Boolean): Int? {
        return pages.indexOfFirst { condition(it.fragment) }.takeUnless { it == -1 }
    }

    override fun getCount(): Int {
        return this.pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val page = pages[position]
        return if (page is ViewPagerItem.PageWithTitle) {
            val context = page.fragment.context
            if (context != null) {
                page.title
            } else null
        } else null
    }

    override fun saveState(): Parcelable? {
        return null
    }
}
