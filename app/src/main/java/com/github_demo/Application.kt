package com.github_demo

import android.app.Application
import com.github_demo.dagger.AppComponent
import com.github_demo.dagger.AppContextModule
import com.github_demo.dagger.DaggerAppComponent

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init(){
        appComponent = DaggerAppComponent.builder().appContextModule(AppContextModule(applicationContext)).build()
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}