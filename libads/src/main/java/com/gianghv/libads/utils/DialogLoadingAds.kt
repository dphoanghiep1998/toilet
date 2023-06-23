package com.gianghv.libads.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.gianghv.libads.R

class DialogLoadingAds constructor(private val context: Context) {
    private var dialog: Dialog? = null

    init {
        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_loading_ads)
        val window = dialog?.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        window.attributes = wlp
        dialog?.window!!.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    fun showDialogLoading(): Dialog? {
        if (dialog != null && dialog?.isShowing == false) {
            dialog?.show()
        }
        return dialog
    }

    fun dismissDialog() {
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}