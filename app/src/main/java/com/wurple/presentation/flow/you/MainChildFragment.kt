package com.wurple.presentation.flow.you

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ScrollingView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.wurple.domain.navigation.Screen
import com.wurple.presentation.extension.isScrolled
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.main.MainFragment

abstract class MainChildFragment<VM : BaseViewModel>(
    @LayoutRes layoutResId: Int
) : BaseViewModelFragment<VM>(layoutResId) {
    abstract fun getScrollingView(): ScrollingView

    override fun onResume() {
        super.onResume()
        setAppBarLayoutLiftable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (getScrollingView()) {
            is RecyclerView -> {
                (getScrollingView() as? RecyclerView)
                    ?.setOnScrollChangeListener { _, _, _, _, _ -> setAppBarLayoutLiftable() }
            }
            is NestedScrollView -> {
                (getScrollingView() as? NestedScrollView)
                    ?.setOnScrollChangeListener { _, _, _, _, _ -> setAppBarLayoutLiftable() }
            }
        }
    }

    fun scrollToTop() {
        when (getScrollingView()) {
            is RecyclerView -> (getScrollingView() as? RecyclerView)?.smoothScrollToPosition(0)
            is NestedScrollView -> (getScrollingView() as? NestedScrollView)?.smoothScrollTo(0, 0)
        }
    }

    protected fun getMainFragment(): MainFragment? {
        val fragmentManager = activity?.supportFragmentManager
        return fragmentManager?.findFragmentByTag(Screen.Main().tag) as? MainFragment
    }

    private fun setAppBarLayoutLiftable() {
        when (getScrollingView()) {
            is RecyclerView -> {
                getMainFragment()?.setAppBarLayoutLiftable(
                    (getScrollingView() as? RecyclerView)?.isScrolled()?.not() ?: false
                )
            }
            is NestedScrollView -> {
                getMainFragment()?.setAppBarLayoutLiftable(
                    (getScrollingView() as? NestedScrollView)?.isScrolled()?.not() ?: false
                )
            }
        }
    }
}