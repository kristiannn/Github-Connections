package com.example.github_connections.modules.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.github_connections.R

//DialogFragment > AlertDialog because there are issues with the custom rounded corners if it's not a fragment with its own window property.
class DialogController(private val message: String) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_error_message, container, false)

        val dialog = dialog!!
        val button = view.findViewById<Button>(R.id.dialog_button)
        val textView = view.findViewById<TextView>(R.id.dialog_textView)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        textView.text = message

        button.setOnClickListener {
            dialog.dismiss()
        }

        return view
    }
}