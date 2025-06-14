package com.quickaccess

import android.app.Application
import com.quickaccess.app.viewmodel.SharedViewModel

class MyApplication : Application() {
    val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel()
    }
}