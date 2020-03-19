package ru.bey_sviatoslav.android.vkcuptask1

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton

class RenameDeleteDialog : Dialog, View.OnClickListener {

    private val activity : Activity
    private lateinit var dialog : Dialog
    private lateinit var buttonRename : Button
    private lateinit var buttonDelete : Button

    constructor(context: Context, activity: Activity) : super(context) {
        this.activity = activity
    }

    constructor(context: Context, themeResId: Int, activity: Activity) : super(
        context,
        themeResId
    ) {
        this.activity = activity
    }

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?,
        activity: Activity
    ) : super(context, cancelable, cancelListener) {
        this.activity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.rename_delete_dialog)
        buttonRename = findViewById(R.id.button_rename)
        //buttonRename.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        buttonDelete = findViewById(R.id.button_delete)
        //buttonDelete.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        buttonRename.setOnClickListener(this)
        buttonDelete.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.button_rename -> {
                dismiss()
            }
            R.id.button_delete -> {
                dismiss()
            }
            else -> {
                dismiss()
            }
        }


    }
}