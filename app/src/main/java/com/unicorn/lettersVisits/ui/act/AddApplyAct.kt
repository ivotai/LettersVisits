package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.os.Environment.getExternalStorageDirectory
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.FileFilter
import com.afollestad.materialdialogs.files.fileChooser
import com.blankj.utilcode.util.ToastUtils
import com.drake.channel.sendEvent
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.ActAddApplyBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class AddApplyAct : BaseAct<ActAddApplyBinding>() {

    override fun initIntents() {
        binding.apply {
            tvCancel.setOnClickListener {
                showFileDialogWithPermissionCheck()
            }

            tvAddApply.setOnClickListener {
                val apply = Apply(content = etContent.text.toString()).apply {
                    applicant.target = Global.currentUser
                }
                SimpleComponent().boxStore.boxFor(Apply::class.java).put(apply)
                sendEvent(apply)
                finish()
            }
        }

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showFileDialog() {
        val myFilter: FileFilter = { true }
        MaterialDialog(this).show {
            fileChooser(
                context = context, initialDirectory = getExternalStorageDirectory(), filter = myFilter
            ) { dialog, file ->

                // File selected
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

}