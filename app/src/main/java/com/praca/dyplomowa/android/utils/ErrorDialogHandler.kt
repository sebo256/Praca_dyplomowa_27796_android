package com.praca.dyplomowa.android.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.praca.dyplomowa.android.R

class ErrorDialogHandler(context: Context) {
    init {
        val materialDialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_error_text_title)
            .setMessage(R.string.dialog_error_text_message)
            .setPositiveButton(R.string.dialog_error_positive_title) { dialog, which ->

            }
            .setOnDismissListener {  }
        materialDialog.show()
    }
}