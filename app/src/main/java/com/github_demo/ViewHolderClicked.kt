package com.github_demo

import android.view.View

interface ViewHolderClicked<T> {
    fun onItemViewClicked(view: View, item: T)
}