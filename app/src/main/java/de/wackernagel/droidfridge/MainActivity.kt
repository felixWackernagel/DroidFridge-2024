package de.wackernagel.droidfridge

import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.wackernagel.droidfridge.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuProvider {

    private lateinit var navController: NavController
    private val navConfiguration = AppBarConfiguration(
        setOf(
            R.id.homeFragment,
            R.id.shopsListFragment
        )
    )
    private var previousDestinationId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView( this, R.layout.activity_main )
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.collapsingToolbar.setupWithNavController( binding.toolbar, navController, navConfiguration )
        binding.bottomNavigation.setupWithNavController( navController )

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            // update bottom navigation
            showOrHideBottomNavigation( binding.bottomNavigation, arguments )

            // update toolbar
            showCloseIconOnToolbar( binding.toolbar, destination.id )
            collapseOrExpandCollapsingToolbarLayout( destination.id, previousDestinationId, listOf( R.id.shopFragment), binding.appBar, binding.collapsingToolbar )
            invalidateMenu()

            previousDestinationId = destination.id
        }

        disableExpandableAndCollapsableAppBarByDragAndDrop( binding.appBar )

        addMenuProvider( this )
    }

    private fun disableExpandableAndCollapsableAppBarByDragAndDrop(appBar: AppBarLayout) {
        val params = appBar.layoutParams as CoordinatorLayout.LayoutParams
        val behaviour = params.behavior as AppBarLayout.Behavior
        behaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }

    /**
     * Show a X icon instead of a arrow icon on the toolbar.
     * Do this only for some special destinations.
     * It is not required to undo this action because it is done automatically.
     */
    private fun showCloseIconOnToolbar(toolbar: Toolbar, destinationId: Int) {
        val showCloseIconFor = listOf( R.id.updateShopFragment )
        if( showCloseIconFor.contains( destinationId ) ) toolbar.setNavigationIcon( R.drawable.ic_close_24 )
    }

    /**
     * Show or hide BottomNavigationView based on additional navigation arguments.
     * Is no argument defined then BottomNavigationView is shown.
     */
    private fun showOrHideBottomNavigation( bottomNavigation: BottomNavigationView, arguments: Bundle? ) {
        val showBottomNavigation = arguments?.getBoolean("showBottomNavigation", true ) ?: true
        val position = if (showBottomNavigation) 0f else TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 56f, resources.displayMetrics )
        bottomNavigation.animate().translationY( position ).setDuration( 200L ).start()
    }

    /**
     * Expand or collapse CollapsingToolbarLayout based on the current fragment.
     * The content-scrim, expand and collapse process is animated.
     */
    private fun collapseOrExpandCollapsingToolbarLayout(destinationId: Int, previousDestinationId: Int, expandForDestinations: List<Int>, appBar: AppBarLayout, collapsingToolbar: CollapsingToolbarLayout ) {
        val expandToolbar = expandForDestinations.contains( destinationId )
        val collapseToolbar = expandForDestinations.contains( previousDestinationId )

        collapsingToolbar.scrimAnimationDuration = if( expandToolbar ) 600 else 0

        appBar.setExpanded( expandToolbar, expandToolbar || collapseToolbar )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        val fragmentId = navController.currentDestination?.id
        val hideAboutMenuFor = listOf( R.id.aboutFragment, R.id.updateShopFragment )
        menu.findItem( R.id.aboutFragment )?.isVisible = !hideAboutMenuFor.contains( fragmentId )
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // add all menu-item ids which are no navigation destinations to avoid unnecessary log warnings
        return when( menuItem.itemId ) {
            R.id.editShop -> false
            R.id.deleteShop -> false
            else -> NavigationUI.onNavDestinationSelected( menuItem, navController )
        }
    }
}