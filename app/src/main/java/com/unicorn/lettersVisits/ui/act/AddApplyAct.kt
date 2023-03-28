package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.os.Environment.getExternalStorageDirectory
import androidx.databinding.DataBindingUtil.getBinding
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.files.FileFilter
import com.afollestad.materialdialogs.files.fileChooser
import com.blankj.utilcode.util.DeviceUtils.getModel
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.google.android.flexbox.FlexboxLayoutManager
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.databinding.ActAddApplyBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import io.objectbox.model.ModelProperty.addType
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class AddApplyAct : BaseAct<ActAddApplyBinding>() {

    override fun initViews() {
        binding.apply {
            rv.layoutManager = FlexboxLayoutManager(this@AddApplyAct)
            rv
                .setup {
                addType<String>(R.layout.item_material)
                onBind {
                    val binding = getBinding<ItemMaterialBinding>()
                    val item = getModel<String>()
                    binding.tv.text = item
                }
            }.models = listOf("Word 格式材料", "Excel 格式材料", "PDF 格式材料", "任意格式材料")
        }
    }

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

    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun showFileDialog() {
        val myFilter: FileFilter = { it.isDirectory || it.extension == "docx" }
        MaterialDialog(this, BottomSheet()).show {
            fileChooser(
                context = context,
                initialDirectory = getExternalStorageDirectory(),
                filter = myFilter
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