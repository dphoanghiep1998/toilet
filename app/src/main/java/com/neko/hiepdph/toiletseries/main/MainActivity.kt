package com.neko.hiepdph.toiletseries.main

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.neko.hiepdph.toiletseries.R
import com.neko.hiepdph.toiletseries.common.AppSharePreference
import com.neko.hiepdph.toiletseries.common.changeStatusBarColor
import com.neko.hiepdph.toiletseries.common.createContext
import com.neko.hiepdph.toiletseries.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor()
        hideNavigationBar()
        checkInit()
        initData()
    }

    private fun checkInit() {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)

        val controller = navHostFragment.navController

        if (!AppSharePreference.INSTANCE.getPassSetting(false)) {
            controller.navigate(R.id.fragmentOnBoard)
        } else {
            controller.navigate(R.id.fragmentVideo)
        }

    }

    private fun initData() {
        if (!AppSharePreference.INSTANCE.getPassSetting(false)) {
            AppSharePreference.INSTANCE.saveListUnlockPos(
                mutableListOf(
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9,
                    10,
                    11,
                    12,
                    13,
                    14,
                    15,
                    16,
                    17,
                    18,
                    19,
                    20,
                    21,
                    22,
                    23,
                    24,
                    25,
                    26,
                    27,
                    28,
                    29,
                    30,
                    31,
                    32,
                    33,
                    34,
                    35,
                    36,
                    37,
                    38,
                    39,
                )
            )
        }
    }


    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(
        newBase.createContext(
            Locale(
                AppSharePreference.INSTANCE.getSavedLanguage(
                    AppSharePreference.INSTANCE.getSavedLanguage(Locale.getDefault().language)
                )
            )
        )
    )

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun hideNavigationBar() {
        val decorView: View = window.decorView
        val uiOptions: Int =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}