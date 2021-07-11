package com.dewival.testdewival.ui.view

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    var errorDialog: AlertDialog? = null
    fun showError (message: String) {
        if (errorDialog == null) {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Error")
                .setMessage(message)
                .setPositiveButton(
                    "Ok"
                ) { alertDialog, id ->
                    errorDialog = null
                    alertDialog.cancel()
                }.setOnDismissListener {
                    errorDialog = null
                }
            errorDialog = alertDialog.create()
            errorDialog!!.show()
        }
    }
}