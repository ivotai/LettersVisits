package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Environment.getExternalStorageDirectory
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.files.FileFilter
import com.afollestad.materialdialogs.files.fileChooser
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.mutable
import com.drake.brv.utils.setup
import com.drake.channel.sendEvent
import com.google.android.flexbox.FlexboxLayoutManager
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.baidu.FileUtil
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
                    when (val item = getModel<Any>()) {
                        is String -> {
//                            filter = when (item) {
//                                "上传 Word 格式信访材料" -> {
//                                    { it.isDirectory || it.extension == "docx" || it.extension == "doc" }
//                                }
//                                "上传 PDF 格式信访材料" -> {
//                                    { it.isDirectory || it.extension == "pdf" }
//                                }
//                                "上传任意格式信访材料" -> {
//                                    { true }
//                                }
//                                else -> throw Exception("未知的文件类型")
//                            }
                            showFileDialogWithPermissionCheck()
                        }
                        is Material -> {
                            val position = modelPosition
                            binding.rv.mutable.removeAt(position) // 先删除数据
                            binding.rv.bindingAdapter.notifyItemRemoved(position)
                        }
                    }
                }
                onClick(R.id.iv) {
                    when (val item = getModel<Any>()) {
                        is String -> {
                            //
                        }
                        is Material -> {

                        }
                    }
                }
            }.models = listOf("上传信访材料")
        }
    }

    override fun initIntents() {
        binding.apply {
            tvCancel.setOnClickListener {
                startOrcWithPermissionCheck()
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
        initAccessToken()
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
                val index = binding.rv.bindingAdapter.modelCount - 1
                binding.rv.bindingAdapter.addModels(
                    listOf(Material(file = file)), index = index, animation = true
                )
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

    private fun initAccessToken() {
        OCR.getInstance(applicationContext).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                val token = accessToken.accessToken
                ToastUtils.showShort("成了")
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
                ToastUtils.showShort("失败了")
            }
        }, applicationContext)
    }

    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun startOrc() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(
            CameraActivity.KEY_OUTPUT_FILE_PATH,
            FileUtil.getSaveFile(application).absolutePath
        )
        intent.putExtra(
            CameraActivity.KEY_NATIVE_ENABLE,
            true
        )
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(
            CameraActivity.KEY_NATIVE_MANUAL,
            true
        )
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
        startActivityForResult(intent, 2333)
    }



}    private val startActivityLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            //
        } else if (it.resultCode == Activity.RESULT_CANCELED) {
            //
        }
    }