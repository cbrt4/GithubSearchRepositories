package com.alex.githubsearchrepositories.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FloatingButtonHideOnScrollBehavior(context: Context, attrs: AttributeSet) :
    FloatingActionButton.Behavior(context, attrs) {

    var isShown = true

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )

        if (target is RecyclerView) {
            if (dyConsumed > 0 && isShown) {
                val layoutParams =
                    child.layoutParams as CoordinatorLayout.LayoutParams
                val bottomMargin = layoutParams.bottomMargin
                child.animate().translationY((child.height + bottomMargin).toFloat())
                    .setInterpolator(LinearInterpolator()).start()
                isShown = false
            } else if (dyConsumed < 0 && !isShown || dyUnconsumed < 0 && !isShown) {
                child.animate().translationY(0F).setInterpolator(LinearInterpolator()).start()
                isShown = true
            }
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}