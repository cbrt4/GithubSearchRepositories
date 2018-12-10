package com.alex.githubsearchrepositories.util

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


class FloatingButtonHideOnScrollBehavior(context: Context, attrs: AttributeSet) :
        FloatingActionButton.Behavior(context, attrs) {

    var isShown = true

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

        if (target is RecyclerView) {
            if (dyConsumed > 0 && isShown) {
                val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
                val bottomMargin = layoutParams.bottomMargin
                child.animate().translationY((child.height + bottomMargin).toFloat()).setInterpolator(LinearInterpolator()).start()
                isShown = false
            } else if (dyConsumed < 0 && !isShown || dyUnconsumed < 0 && !isShown) {
                child.animate().translationY(0F).setInterpolator(LinearInterpolator()).start()
                isShown = true
            }
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}