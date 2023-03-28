package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.os.Environment.getExternalStorageDirectory
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.files.FileFilter
import com.afollestad.materialdialogs.files.fileChooser
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.google.android.flexbox.FlexboxLayoutManager
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Apply
import com.unicorn.lettersVisits.data.model.Material
import com.unicorn.lettersVisits.databinding.ActAddApplyBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialUploadBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.util.*


@RuntimePermissions
class AddApplyAct : BaseAct<ActAddApplyBinding>() {

    private var filter: FileFilter = { true }

    override fun initViews() {
        binding.apply {
            rv.layoutManager = FlexboxLayoutManager(this@AddApplyAct)
            rv.setup {
                addType<String>(R.layout.item_material_upload)
                addType<Material>(R.layout.item_material)
                onBind {
                    when (val item = getModel<Any>()) {
                        is String -> {
                            val binding = getBinding<ItemMaterialUploadBinding>()
                            binding.tv.text = item
                        }
                        is Material -> {
                            val binding = getBinding<ItemMaterialBinding>()
                            binding.tv.text = item.file.name
                        }
                    }
                }
                onClick(R.id.root) {
                    val item = getModel<Any>()
                    if (item is String) {
                        filter = when (item) {
                            "上传 Word 格式信访材料" -> {
                                { it.isDirectory || it.extension == "docx" || it.extension == "doc" }
                            }
                            "上传 PDF 格式信访材料" -> {
                                { it.isDirectory || it.extension == "pdf" }
                            }
                            "上传任意格式信访材料" -> {
                                { true }
                            }
                            else -> throw Exception("未知的文件类型")
                        }
                    }
                    showFileDialogWithPermissionCheck()
                }
            }.models = listOf("上传 Word 格式信访材料", "上传 PDF 格式信访材料", "上传任意格式信访材料")
        }
    }

    override fun initIntents() {
        binding.apply {
            tvCancel.setOnClickListener {
                finish()
            }

            tvAddApply.setOnClickListener {
                val apply = Apply(content = etContent.text.toString(), createTime = Date()).apply {
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
        MaterialDialog(this, BottomSheet()).show {
            fileChooser(
                context = context,
                initialDirectory = File(getExternalStorageDirectory(), "Download"),
                filter = filter
            ) { _, file ->
                val index = binding.rv.bindingAdapter.headers.size
                binding.rv.bindingAdapter.addHeader(Material(file), index = index, animation = true)
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