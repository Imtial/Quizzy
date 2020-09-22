package com.example.quizzy

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.quizzy.database.QuizDatabase
import com.example.quizzy.domain.PRIVATE
import com.example.quizzy.domain.PUBLIC
import com.example.quizzy.network.Status
import com.example.quizzy.repository.QuizRepository
import com.example.quizzy.repository.UserRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class QuizGameActivity: AppCompatActivity() {
    private lateinit var onButtonClickListener: OnButtonClickListener
    private lateinit var onAccessChangeListener: OnAccessChangeListener
    private lateinit var onSearchListener: OnSearchListener
    private lateinit var topTextView: TextView
    private lateinit var backButton: FloatingActionButton
    private lateinit var completeButton: FloatingActionButton
    private lateinit var nextButton: FloatingActionButton
    private lateinit var spaceOne: Space
    private lateinit var spaceTwo: Space
    private lateinit var navDrawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_game)

        val toolbar = findViewById<Toolbar>(R.id.quiz_toolbar)
        setSupportActionBar(toolbar)

        navDrawer = findViewById(R.id.nav_drawer)
        backButton = findViewById(R.id.button_back)
        spaceOne = findViewById(R.id.space_one)
        completeButton = findViewById(R.id.button_complete)
        spaceTwo = findViewById(R.id.space_two)
        nextButton = findViewById(R.id.button_next)
        topTextView = findViewById(R.id.quiz_game_top)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.game_fragment)
        navigationView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, navDrawer)

        val userRepository = UserRepository(QuizDatabase.getDatabase(applicationContext))

        userRepository.logOutStatus.observe(this, {
            it?.let {
                when(it) {
                    Status.SUCCESS -> {
                        Toast.makeText(applicationContext, "Log out successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    Status.FAILURE -> {
                        Toast.makeText(applicationContext, "Log out failed. Try again later.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { view, isChecked ->
            if (isChecked) onAccessChangeListener.accessChanged(PRIVATE)
            else onAccessChangeListener.accessChanged(PUBLIC)
        }

        val accessMenuItem = navigationView.menu.findItem(R.id.toggle_access)
        val switch = accessMenuItem.actionView.findViewById<SwitchMaterial>(R.id.switch_access)
        switch.setOnCheckedChangeListener(onCheckedChangeListener)

        navigationView.setNavigationItemSelectedListener {menuItem ->
            when(menuItem.itemId) {
                R.id.logout -> userRepository.logOut()
            }
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem,navController);
            //This is for closing the drawer after acting on it
            navDrawer.closeDrawer(GravityCompat.START);
            true
        }

        nextButton.setOnClickListener {
            onButtonClickListener.nextButtonClicked()
        }
        completeButton.setOnClickListener {
            onButtonClickListener.completeButtonClicked()
        }
        backButton.setOnClickListener {
            onButtonClickListener.backButtonClicked()
        }
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }

    fun setOnAccessChangeListener(listener: OnAccessChangeListener) {
        onAccessChangeListener = listener
    }

    fun setOnSearchListener(listener: OnSearchListener) {
        onSearchListener = listener
    }

    fun setQuestionNumberOnTopBar (text: String) {
        val spannableString = SpannableString(String.format("Question #%s", text))
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#AA00FF")), 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        topTextView.text = spannableString
    }

    fun setTextOnTopBar (text: String) {
        topTextView.text = text
    }

    fun showTopTextView () {
        topTextView.visibility = View.VISIBLE
    }

    fun hideTopTextView() {
        topTextView.visibility = View.GONE
    }

    fun showButton(vararg buttonResIds: Int) {
        for (resId in buttonResIds) {
            when(resId) {
                R.id.button_back -> backButton.visibility = View.VISIBLE
                R.id.button_complete -> completeButton.visibility = View.VISIBLE
                R.id.button_next -> nextButton.visibility= View.VISIBLE
            }
        }
        spaceOne.visibility = View.VISIBLE
        spaceTwo.visibility = View.VISIBLE
    }
    fun hideButton(vararg buttonResIds: Int) {
        for (resId in buttonResIds) {
            when(resId) {
                R.id.button_back -> backButton.visibility = View.GONE
                R.id.button_complete -> completeButton.visibility = View.GONE
                R.id.button_next -> {
                    nextButton.visibility = View.GONE
                    spaceTwo.visibility = View.GONE
                }
            }
        }
    }

    private lateinit var searchView: SearchView
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search)
        searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("SEARCH", "onQueryTextSubmit: $query")
                onSearchListener.search(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                onSearchListener.onSearchViewCollapsed()
                return true
            }
        })

        return true
    }

    override fun onBackPressed() {
        if(!searchView.isIconified) searchView.onActionViewCollapsed()
        else super.onBackPressed()
    }

}

interface OnButtonClickListener {
    fun nextButtonClicked()
    fun completeButtonClicked()
    fun backButtonClicked()
}

interface OnAccessChangeListener {
    fun accessChanged(access: String)
}

interface OnSearchListener {
    fun search(query: String?)
    fun onSearchViewCollapsed()
}