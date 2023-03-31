package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.content.Intent
import android.os.Environment.getExternalStorageDirectory
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.files.fileChooser
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.sdk.model.IDCardParams
import com.baidu.ocr.sdk.model.IDCardResult
import com.baidu.ocr.ui.camera.CameraActivity
import com.baidu.ocr.ui.camera.CameraNativeHelper
import com.baidu.ocr.ui.camera.CameraView
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
import com.unicorn.lettersVisits.data.model.Applicant
import com.unicorn.lettersVisits.data.model.Petition
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
                    when (getModel<Any>()) {
                        is String -> {
                            showFileDialogWithPermissionCheck()
                        }
                        is Material -> {
                            val position = modelPosition
                            binding.rv.mutable.removeAt(position) // 先删除数据
                            binding.rv.bindingAdapter.notifyItemRemoved(position)
                        }
                    }
                }
            }.models = listOf("上传信访材料")
        }
    }

    override fun initIntents() {
        binding.apply {
            tvCancel.setOnClickListener {
                // just for test
                startOrcWithPermissionCheck()
            }

            tvAddApply.setOnClickListener {
                val petition = Petition(content = etContent.text.toString(), createTime = Date()).apply {
                    petitioner.target = Global.currentUser
                }
                SimpleComponent().boxStore.boxFor(Petition::class.java).put(petition)
                sendEvent(petition)
                finish()
            }
        }

        // 初始化AccessToken
        initAccessToken()
    }

    // Android 10适配要点，作用域存储
    // https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650249029&idx=1&sn=6ab18477950e5f4e1a14dc47ecc4f763&chksm=8863662abf14ef3c1500d64c106ab2e5a6c95e716ff6e57ba379e2aabca7b6046060ccb78af2&scene=21#wechat_redirect
    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    fun showFileDialog() {
        MaterialDialog(this, BottomSheet()).show {
            fileChooser(
                context = context,
                initialDirectory = File(getExternalStorageDirectory(), "Download"),
            ) { _, file ->
                val index = binding.rv.bindingAdapter.modelCount - 1
                binding.rv.bindingAdapter.addModels(
                    listOf(Material(file = file)), index = index, animation = true
                )
            }
        }
    }

    private var ocrPrepared = false
    private fun initAccessToken() {
        OCR.getInstance(applicationContext).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                ocrPrepared = true
                initCameraNativeHelper()
            }

            override fun onError(error: OCRError) {
                // do nothing
            }
        }, applicationContext)
    }

    private fun initCameraNativeHelper() {
        //  初始化本地质量控制模型,释放代码在onDestroy中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(
            this, OCR.getInstance(this).license
        ) { errorCode, _ ->
            ocrPrepared = false
            val msg: String = when (errorCode) {
                CameraView.NATIVE_SOLOAD_FAIL -> "加载so失败，请确保apk中存在ui部分的so"
                CameraView.NATIVE_AUTH_FAIL -> "授权本地质量控制token获取失败"
                CameraView.NATIVE_INIT_FAIL -> "本地质量控制"
                else -> errorCode.toString()
            }
            ToastUtils.showShort(msg)
        }
    }

    override fun onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release()
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun startOrc() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(
            CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(application).absolutePath
        )
        intent.putExtra(
            CameraActivity.KEY_NATIVE_ENABLE, true
        )
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(
            CameraActivity.KEY_NATIVE_MANUAL, true
        )
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
        startActivityForResult(intent, 2333)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2333 && resultCode == RESULT_OK) {
            if (data != null) {
                val filePath = FileUtil.getSaveFile(applicationContext).absolutePath
                recIDCard(filePath)
            }
        }
    }

    private fun recIDCard(filePath: String) {
        val param = IDCardParams()
        param.imageFile = File(filePath)
        // 设置身份证正反面
        param.idCardSide = IDCardParams.ID_CARD_SIDE_FRONT
        // 设置方向检测
        param.isDetectDirection = true
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.imageQuality = 20
        param.setDetectRisk(true)
        OCR.getInstance(this).recognizeIDCard(param, object : OnResultListener<IDCardResult?> {
            override fun onResult(result: IDCardResult?) {
                if (result != null) {
                    val applicant = Applicant(
                        address = result.address.words,
                        birthday = result.birthday.words,
                        ethnic = result.birthday.words,
                        gender = result.gender.words,
                        idNumber = result.idNumber.words,
                        name = result.name.words
                    )
                    onApplicant(applicant)
                }
            }

            override fun onError(error: OCRError) {
                // do nothing
            }
        })
    }

    fun onApplicant(applicant: Applicant) {
        // todo
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

}