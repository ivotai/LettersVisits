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
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.baidu.ocr.sdk.model.IDCardResult
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.*
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.google.android.flexbox.FlexboxLayoutManager
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.initialPassword
import com.unicorn.lettersVisits.data.model.*
import com.unicorn.lettersVisits.data.model.event.ExcelDialogEvent
import com.unicorn.lettersVisits.data.model.event.PetitionerPutEvent
import com.unicorn.lettersVisits.data.model.event.PetitionerSelectEvent
import com.unicorn.lettersVisits.data.model.event.StartOrcEvent
import com.unicorn.lettersVisits.data.model.role.PetitionType
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.databinding.ActAddPetitionBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialBinding
import com.unicorn.lettersVisits.databinding.ItemMaterialUploadBinding
import com.unicorn.lettersVisits.databinding.ItemPetitionFieldBinding
import com.unicorn.lettersVisits.view.PetitionerSelectView
import io.objectbox.kotlin.boxFor
import io.objectbox.query.QueryBuilder
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import splitties.resources.color
import java.io.File
import java.text.Collator
import java.util.*


@RuntimePermissions
class PetitionDetailAct : BaiduOrcAct<ActAddPetitionBinding>() {

    private var mEditable = false

    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun startOrcWrapper() {
        startOrc()
    }

    override fun initViews() {
        binding.apply {
            titleBar.setTitle("信访申请")

            btnSubmit.helper.backgroundColorPressed = ColorUtils.blendARGB(
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

            // petition field
            rv2.linear().divider {
                setColorRes(splitties.material.colors.R.color.grey_200)
                setDivider(width = 1, dp = false)
            }.setup {

                addType<PetitionField>(R.layout.item_petition_field)
                onBind {
                    val binding = getBinding<ItemPetitionFieldBinding>()
                    val item = getModel<PetitionField>()
                    binding.apply {
                        l.text = item.label
                        tv.hint = item.hint
                        tv.text = item.text
                    }
                }
                onFastClick(R.id.tv) {
                    if (!mEditable) return@onFastClick
                    when (getModel<PetitionField>().label) {
                        "当事人" -> {
                            if (Global.isStaff) {
                                dialogHolder =
                                    MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                                        title(text = "请选择当事人")
                                        customView(view = PetitionerSelectView(this@PetitionDetailAct))
                                    }
                            }
                        }
                        "信访单位" -> {
                            val departments = Global.boxStore.boxFor<Department>().all
                            MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                                title(text = "请选择信访单位")
                                listItems(
                                    items = departments.map { it.name },
                                ) { _, index, _ ->
                                    setDepartment(departments[index])
                                }
                            }
                        }
                        "信访类型" -> {
//                            val petitionTypes = PetitionType.values()
//                            MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
//                                title(text = "请选择信访类型")
//                                listItems(
//                                    items = petitionTypes.map { it.petitionTypeName },
//                                ) { _, index, _ ->
//                                    setPetitionType(petitionTypes[index])
//                                }
//                            }
                            sendEvent(ExcelDialogEvent(queryIndex = 1, queryValue = "涉诉法院信息"))
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }.models = listOf(
                PetitionField(label = "当事人", hint = "请选择"),
                PetitionField(label = "信访单位", hint = "请选择"),
                PetitionField(label = "信访类型", hint = "请选择"),
                PetitionField(label = "信访答复", hint = "待答复"),
            )
        }
    }

    override fun initIntents() {
        super.initIntents()

        binding.apply {
            // 赋予一些默认值
            if (Global.isPetitioner) {
                setPetitioner(Global.currentUser)
            }
            if (Global.isStaff) {
                setDepartment(Global.currentUser?.department?.target)
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
            btnSubmit.visibility = if (mEditable) View.VISIBLE else View.GONE

            // 根据 temp 设置界面
            val temp = !isCreating && Global.isStaff
            btnTransfer.visibility = if (temp) View.VISIBLE else View.GONE
            btnReply.visibility = if (temp) View.VISIBLE else View.GONE

            // 展示数据
            mPetition.apply {
                etContent.setText(this.content)
                setPetitioner(this.petitioner.target)
                setDepartment(this.department.target)
                setPetitionType(this.petitionType)
            }
        }

        binding.apply {
            btnSubmit.setOnClickListener {
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
                if (mPetition.department.target == null) {
                    ToastUtils.showShort("请选择信访单位")
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

            btnReply.setOnClickListener {
                MaterialDialog(this@PetitionDetailAct).show {
                    title(text = "请输入答复内容")
                    input()
                    positiveButton { dialog ->
                        setReply(dialog.getInputField().text.toString())
                        Global.boxStore.boxFor<Petition>().put(mPetition)
                    }
                }
            }

            btnTransfer.setOnClickListener {
                ToastUtils.showShort("转办没做")
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
            startOrcWrapperWithPermissionCheck()
        }
        receiveEvent<PetitionerSelectEvent> {
            dialogHolder?.dismiss()
            setPetitioner(it.petitioner)
        }

        receiveEvent<ExcelDialogEvent> { event ->
            val excelDataBox = Global.boxStore.boxFor<ExcelData>()
            val propertyQuery = excelDataBox.query().equal(
                ExcelData_.__ALL_PROPERTIES[event.queryIndex],
                event.queryValue,
                QueryBuilder.StringOrder.CASE_SENSITIVE
            ).build().property(ExcelData_.__ALL_PROPERTIES[event.queryIndex + 1])
            val results = propertyQuery.distinct().findStrings()
            if (results.size == 1) {
                ToastUtils.showShort("选择结果: ${event.queryValue}")
                return@receiveEvent
            }
            results.sortWith(Collator.getInstance(Locale.CHINA))
            MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                title(text = "请选择")
                listItems(items = results.toList()) { _, _, text ->
                    sendEvent(ExcelDialogEvent(event.queryIndex + 1, text.toString()))
                }
            }
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

    private fun setPetitioner(it: User?) {
        if (it == null) return
        binding.rv2.bindingAdapter.apply {
            mPetition.petitioner.target = it
            val petitionField = models!![0] as PetitionField
            petitionField.text = it.username
            notifyItemChanged(0)
        }
    }

    private fun setDepartment(it: Department?) {
        if (it == null) return
        binding.rv2.bindingAdapter.apply {
            mPetition.department.target = it
            val petitionField = models!![1] as PetitionField
            petitionField.text = it.name
            notifyItemChanged(1)
        }
    }

    private fun setPetitionType(it: PetitionType) {
        binding.rv2.bindingAdapter.apply {
            mPetition.petitionType = it
            val petitionField = models!![2] as PetitionField
            petitionField.text = it.petitionTypeName
            notifyItemChanged(2)
        }
    }

    private fun setReply(it: String) {
        binding.rv2.bindingAdapter.apply {
            mPetition.reply = it
            val petitionField = models!![3] as PetitionField
            petitionField.text = it
            notifyItemChanged(3)
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