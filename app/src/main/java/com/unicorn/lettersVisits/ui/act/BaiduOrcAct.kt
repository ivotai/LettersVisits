package com.unicorn.lettersVisits.ui.act

import android.Manifest
import android.content.Intent
import androidx.viewbinding.ViewBinding
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
import com.unicorn.lettersVisits.app.baidu.FileUtil
import com.unicorn.lettersVisits.app.initialPassword
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.ui.base.BaseAct
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File

@RuntimePermissions
abstract class BaiduOrcAct<VB : ViewBinding>: BaseAct<VB>() {

    abstract fun onResult(result: IDCardResult)

    private var ocrPrepared = false

    override fun initIntents() {
        initAccessToken()
    }
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
        if (ocrPrepared.not()) {
            ToastUtils.showShort("OCR未准备好")
            return
        }

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
                    onResult(result)
                }
            }

            override fun onError(error: OCRError) {
                // do nothing
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

}