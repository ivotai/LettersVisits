package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.os.Environment.getExternalStorageDirectory
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.files.fileChooser
import com.baidu.ocr.sdk.model.IDCardResult
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.mutable
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.google.android.flexbox.FlexboxLayoutManager
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.initialPassword
import com.unicorn.lettersVisits.app.module.SimpleComponent
import com.unicorn.lettersVisits.data.model.Material
import com.unicorn.lettersVisits.data.model.Petition
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.event.StartOrcEvent
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.databinding.ActAddApplyBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialUploadBinding
import com.unicorn.lettersVisits.view.PetitionerSelectView
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.util.*


@RuntimePermissions
class AddPetitionAct : BaiduOrcAct<ActAddApplyBinding>() {
    override fun onOrcResult(result: IDCardResult) {
        val user = User(
            username = result.name.words,
            password = initialPassword,
            role = Role.PETITIONER,
            address = result.address.words,
            birthday = result.birthday.words,
            ethnic = result.birthday.words,
            gender = result.gender.words,
            idNumber = result.idNumber.words,
            name = result.name.words
        )
        sendEvent(user)
    }

    override fun initViews() {
        binding.apply {
            rv.layoutManager = FlexboxLayoutManager(this@AddPetitionAct)
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
        super.initIntents()
        binding.apply {
            tvCancel.setOnClickListener {
                finish()
            }

            tvAddApply.setOnClickListener {
                if (mPetitioner == null) {
                    ToastUtils.showShort("请选择当事人")
                    return@setOnClickListener
                }

                val petition =
                    Petition(content = etContent.text.toString(), createTime = Date()).apply {
                        petitioner.target = mPetitioner
                    }
                SimpleComponent().boxStore.boxFor(Petition::class.java).put(petition)
                sendEvent(petition)
                finish()
            }

            tvPetitioner.setOnClickListener {
                dialogHolder = MaterialDialog(this@AddPetitionAct, BottomSheet()).show {
                    customView(view = PetitionerSelectView(this@AddPetitionAct))
                }
            }
        }
    }

    // Android 10适配要点，作用域存储
    // https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650249029&idx=1&sn=6ab18477950e5f4e1a14dc47ecc4f763&chksm=8863662abf14ef3c1500d64c106ab2e5a6c95e716ff6e57ba379e2aabca7b6046060ccb78af2&scene=21#wechat_redirect
    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    fun showFileDialog() {
        MaterialDialog(this, BottomSheet(LayoutMode.MATCH_PARENT)).show {
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

    private var dialogHolder: MaterialDialog? = null


    override fun initEvents() {
        receiveEvent<StartOrcEvent> {
            dialogHolder?.dismiss()
            startOrcWithPermissionCheck()
        }
        receiveEvent<User> {
            dialogHolder?.dismiss()
            mPetitioner = it
            binding.tvPetitioner.text = it.name
        }
    }

    private var mPetitioner: User? = null

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

}