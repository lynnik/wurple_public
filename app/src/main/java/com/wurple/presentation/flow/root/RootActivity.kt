package com.wurple.presentation.flow.root

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.wurple.R
import com.wurple.presentation.flow.base.activity.BaseViewModelActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RootActivity : BaseViewModelActivity<RootViewModel>() {
    override val viewModel: RootViewModel by baseViewModel()
    private var splashScreen: SplashScreen? = null

    override fun observeViewModelState() {
        super.observeViewModelState()
        with(viewModel) {
            authCheckedLiveData.observe { splashScreen?.setKeepOnScreenCondition { false } }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashscreen()
        setContentView(R.layout.activity_root)
        makeFullscreen()
        checkAuthOrSignIn(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkAuthOrSignIn(intent)
    }

    private fun checkAuthOrSignIn(intent: Intent?) {
        lifecycleScope.launch {
            // may be as auth email link or as a firebase dynamic link
            val link = intent?.data?.toString()
            val deepLink = try {
                val pendingDynamicLinkData = Firebase.dynamicLinks.getDynamicLink(intent).await()
                pendingDynamicLinkData?.link?.toString()
            } catch (throwable: Throwable) {
                null
            }
            viewModel.checkAuthOrSignIn(link, deepLink)
        }
    }

    private fun setupSplashscreen() {
        splashScreen = installSplashScreen()
        splashScreen?.setKeepOnScreenCondition { true }
    }
}