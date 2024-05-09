package ru.practicum.android.diploma.ui.root

import android.os.Binder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private val viewModel by viewModel<RootViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

   /* binding.bottomNavigationView.menu.findItem(R.id.mediaLibraryFragment).isChecked = true
    viewModel.updateTheme()*/

    val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
    /*val navController = navHostFragment.navController
    val bottomNavigationView = binding.bottomNavigationView
    bottomNavigationView.setupWithNavController(navController)*/

}

