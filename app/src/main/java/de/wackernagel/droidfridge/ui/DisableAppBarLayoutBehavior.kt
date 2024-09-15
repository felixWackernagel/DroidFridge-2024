package de.wackernagel.droidfridge.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


class DisableAppBarLayoutBehavior : AppBarLayout.Behavior {
    private var mEnabled = false

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super( context, attrs)

    fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return mEnabled && super.onStartNestedScroll( parent, child, directTargetChild, target, nestedScrollAxes, type )
    }

    fun isEnabled(): Boolean {
        return mEnabled
    }
}
