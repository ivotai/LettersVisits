package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.graphics.Color
import android.os.Environment.getExternalStorageDirectory
import android.view.View
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.files.fileChooser
import com.afollestad.materialdialogs.list.listItems
import com.baidu.ocr.sdk.model.IDCardResult
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.mutable
import com.drake.brv.utils.setup
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.google.android.flexbox.FlexboxLayoutManager
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.initialPassword
import com.unicorn.lettersVisits.data.model.Department
import com.unicorn.lettersVisits.data.model.Material
import com.unicorn.lettersVisits.data.model.Petition
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.event.PetitionerPutEvent
import com.unicorn.lettersVisits.data.model.event.PetitionerSelectEvent
import com.unicorn.lettersVisits.data.model.event.StartOrcEvent
import com.unicorn.lettersVisits.data.model.role.PetitionType
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.databinding.ActAddPetitionBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialUploadBinding
import com.unicorn.lettersVisits.view.PetitionerSelectView
import io.objectbox.kotlin.boxFor
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import splitties.resources.color
import java.io.File
import java.util.*


@RuntimePermissions
class PetitionDetailAct : BaiduOrcAct<ActAddPetitionBinding>() {


    private var mEditable = false

    override fun initViews() {
        binding.apply {
            titleBar.setTitle("信访申请")

            btnConfirm.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(R.color.main_color), Color.WHITE, 0.3f
            )

            // 信访材料
            rv.layoutManager = FlexboxLayoutManager(this@PetitionDetailAct)
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
                onFastClick(R.id.root) {
                    if (!mEditable) return@onFastClick
                    when (getModel<Any>()) {
                        is String -> {
                            showFileDialogWithPermissionCheck()
                        }
                        is Material -> {
                            val position = modelPosition
                            binding.apply {
                                rv.mutable.removeAt(position) // 先删除数据
                                rv.bindingAdapter.notifyItemRemoved(position) // 然后刷新列表
                            }
                        }
                    }
                }
            }.models = listOf("上传信访材料")
        }
    }

    override fun initIntents() {
        super.initIntents()

        binding.apply {
            // 如果是当事人
            if (Global.isPetitioner) {
                mPetition.petitioner.target = Global.currentUser
            }

            // 如果是Staff
            if (Global.isStaff) {
                mPetition.department.target = Global.currentUser!!.department.target
            }

            // 恢复数据
            val id = intent.getLongExtra("id", -1L)
            val isCreating = id == -1L
            if (isCreating) {
                mEditable = true
            } else {
                mPetition = Global.boxStore.boxFor<Petition>().get(id)
                // 如果是创建者，则启用编辑
                if (mPetition.creator.target == Global.currentUser) {
                    mEditable = true
                }
            }

            // 根据 mEditable 设置界面
            etContent.isEnabled = mEditable
            btnConfirm.visibility = if (mEditable) View.VISIBLE else View.GONE

            // 根据 isCreating 设置界面
            val show = isCreating && Global.isStaff
            btnReply.visibility = if (show) View.VISIBLE else View.GONE
            btnTransfer.visibility = if (show) View.VISIBLE else View.GONE

            // 展示数据
            mPetition.apply {
                etContent.setText(this.content)
                tvPetitioner.text = this.petitioner.target?.name
                tvDepartment.text = this.department.target?.name
                tvPetitionType.text = this.petitionType.petitionTypeName
            }
        }

        binding.apply {
            btnConfirm.setOnClickListener {
                if (!mEditable) return@setOnClickListener
                // 非空验证
                val content = etContent.text.toString().trim()
                if (content.isEmpty()) {
                    ToastUtils.showShort("请输入申请内容")
                    return@setOnClickListener
                }
                if (mPetition.petitioner.target == null) {
                    ToastUtils.showShort("请选择当事人")
                    return@setOnClickListener
                }

                // 保存数据
                mPetition.apply {
                    this.content = content
                    this.creator.target = Global.currentUser
                    this.createTime = Date()
                }
                Global.boxStore.boxFor<Petition>().put(mPetition)

                // 通知列表刷新
                sendEvent(PetitionerPutEvent())
                finish()
            }

            tvPetitioner.setOnClickListener {
                if (!mEditable) return@setOnClickListener
                if (Global.isStaff) {
                    dialogHolder = MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                        title(text = "请选择当事人")
                        customView(view = PetitionerSelectView(this@PetitionDetailAct))
                    }
                }
            }

            tvDepartment.setOnClickListener {
                if (!mEditable) return@setOnClickListener
                val departments = Global.boxStore.boxFor<Department>().all
                dialogHolder = MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                    title(text = "请选择部门")
                    listItems(
                        items = departments.map { it.name!! },
                    ) { _, index, _ ->
                        setDepartment(departments[index])
                    }
                }
            }

            tvPetitionType.setOnClickListener {
                if (!mEditable) return@setOnClickListener
                dialogHolder = MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                    title(text = "请选择信访类型")
                    listItems(
                        items = PetitionType.values().map { it.petitionTypeName },
                    ) { _, index, text ->
                        mPetition.petitionType = PetitionType.values()[index]
                        tvPetitionType.text = text
                    }
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
        MaterialDialog(this).show {
            fileChooser(
                context = context,
                initialDirectory = File(getExternalStorageDirectory(), "Download"),
                emptyTextRes = R.string.empty_text,
            ) { _, file ->
                binding.rv.bindingAdapter.apply {
                    val index = modelCount - 1
                    addModels(
                        listOf(Material(file = file)), index = index, animation = true
                    )
                }
            }
        }
    }

    private var dialogHolder: MaterialDialog? = null

    override fun initEvents() {
        receiveEvent<StartOrcEvent> {
            dialogHolder?.dismiss()
            startOrcWithPermissionCheck()
        }
        receiveEvent<PetitionerSelectEvent> {
            dialogHolder?.dismiss()
            setPetitioner(it.petitioner)
        }
    }

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
        setPetitioner(user)
    }

    private var mPetition = Petition()

    private fun setPetitioner(it: User) {
        mPetition.petitioner.target = it
        binding.tvPetitioner.text = it.username
    }

    private fun setDepartment(it: Department) {
        mPetition.department.target = it
        binding.tvDepartment.text = it.name
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

}