package ru.kanogor.attractions.presentation.mapsFragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.kanogor.attractions.R

private const val ARG_TITLE = "argTitleDialog"
private const val ARG_INFO = "argInfoDialog"

@AndroidEntryPoint
class MarkerDialog : DialogFragment() {

    private var title: String? = null
    private var info: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            title = it.getString(ARG_TITLE)
            info = it.getString(ARG_INFO)
        }
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMessage(info)
                .setIcon(R.drawable.ic_icon_foreground)
                .setPositiveButton("Back") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw java.lang.IllegalStateException("Activity cannot be null")
    }
}