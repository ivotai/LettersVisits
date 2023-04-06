package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.baidu.ocr.sdk.model.IDCardResult
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.*
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.app.initialPassword
import com.unicorn.lettersVisits.data.model.ExcelData
import com.unicorn.lettersVisits.data.model.ExcelData_
import com.unicorn.lettersVisits.data.model.User
import com.unicorn.lettersVisits.data.model.event.ExcelDialogEvent
import com.unicorn.lettersVisits.data.model.event.PetitionerPutEvent
import com.unicorn.lettersVisits.data.model.event.StartOrcEvent
import com.unicorn.lettersVisits.data.model.petition.InputType
import com.unicorn.lettersVisits.data.model.petition.Petition
import com.unicorn.lettersVisits.data.model.petition.PetitionField
import com.unicorn.lettersVisits.data.model.role.Role
import com.unicorn.lettersVisits.databinding.ActAddPetitionBinding
import com.unicorn.lettersVisits.databinding.ItemPetitionFieldBinding
import io.objectbox.kotlin.boxFor
import io.objectbox.query.QueryBuilder
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import splitties.resources.color
import java.text.Collator
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties


@RuntimePermissions
class PetitionDetailAct : BaiduOrcAct<ActAddPetitionBinding>() {

    private var isEditable = false

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
//            rv.layoutManager = FlexboxLayoutManager(this@PetitionDetailAct)
//            rv.setup {
//                addType<String>(R.layout.item_material_upload)
//                addType<Material>(R.layout.item_material)
//                onBind {
//                    when (val item = getModel<Any>()) {
//                        is String -> {
//                            val binding = getBinding<ItemMaterialUploadBinding>()
//                            binding.tv.text = item
//                        }
//                        is Material -> {
//                            val binding = getBinding<ItemMaterialBinding>()
//                            binding.tv.text = item.file.name
//                        }
//                    }
//                }
//                onFastClick(R.id.root) {
//                    if (!mEditable) return@onFastClick
//                    when (getModel<Any>()) {
//                        is String -> {
//                            showFileDialogWithPermissionCheck()
//                        }
//                        is Material -> {
//                            val position = modelPosition
//                            binding.apply {
//                                rv.mutable.removeAt(position) // 先删除数据
//                                rv.bindingAdapter.notifyItemRemoved(position) // 然后刷新列表
//                            }
//                        }
//                    }
//                }
//            }.models = listOf("上传信访材料")

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
                        tv.hint = item.inputType.hint
                        tv.text = item.value
                    }
                }
                onFastClick(R.id.tv) {
                    if (!isEditable) return@onFastClick
                    val item = getModel<PetitionField>()
                    when (item.inputType) {
                        InputType.TEXT -> {
                            MaterialDialog(this@PetitionDetailAct).show {
                                title(text = "请输入${item.label}")
                                input()
                                positiveButton { dialog ->
                                    val value = dialog.getInputField().text.toString()

                                    onPetitionFieldValueChange(
                                        position = modelPosition, value = value
                                    )

                                }
                            }
                        }
                        InputType.SELECT -> {
                            sendEvent(item.excelDialogEvent)
                        }

                        else -> {
                            // do nothing
                        }
                    }

                }
            }.models = listOf(PetitionField(label = "来访人姓名", inputType = InputType.TEXT),
                PetitionField(label = "年龄", inputType = InputType.TEXT),
                PetitionField(label = "性别", inputType = InputType.SELECT).apply {
                    excelDialogEvent = ExcelDialogEvent(
                        queryValue = label, queryIndex = 2, petitionField = this
                    )
                },
                PetitionField(label = "类别", inputType = InputType.SELECT).apply {
                    excelDialogEvent = ExcelDialogEvent(
                        queryValue = label, queryIndex = 2, petitionField = this
                    )
                },
                PetitionField(label = "民族", inputType = InputType.SELECT).apply {
                    excelDialogEvent = ExcelDialogEvent(
                        queryValue = label, queryIndex = 2, petitionField = this
                    )
                },
                PetitionField(label = "职业", inputType = InputType.SELECT).apply {
                    excelDialogEvent = ExcelDialogEvent(
                        queryValue = label, queryIndex = 2, petitionField = this
                    )
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initIntents() {
        super.initIntents()

        binding.apply {
            // 赋予一些默认值
//            if (Global.isPetitioner) {
//                setPetitioner(Global.currentUser)
//            }
//            if (Global.isStaff) {
//                setDepartment(Global.currentUser?.department?.target)
//            }

            // 恢复数据
            val id = intent.getLongExtra("id", -1L)
            val isCreating = id == -1L
            if (isCreating) {
                isEditable = true
            } else {
                mPetition = Global.boxStore.boxFor<Petition>().get(id)
                // 如果是创建者，则启用编辑
                if (mPetition.creator.target == Global.currentUser) {
                    isEditable = true
                }
            }

//            // 根据 mEditable 设置界面
//            etContent.isEnabled = isEditable
//            btnSubmit.visibility = if (isEditable) View.VISIBLE else View.GONE
//
//            // 根据 temp 设置界面
//            val temp = !isCreating && Global.isStaff
//            btnTransfer.visibility = if (temp) View.VISIBLE else View.GONE
//            btnReply.visibility = if (temp) View.VISIBLE else View.GONE

            // 展示数据
            rv2.models!!.forEachIndexed { index, any ->
                if (any is PetitionField) {
                    val kProperty1 = Petition::class.memberProperties.elementAt(index)
//                    kProperty1 as KMutableProperty1<Petition, String>
                    kProperty1.get(mPetition)
                    any.value =  kProperty1.get(mPetition) as String
                }
            }
            rv2.bindingAdapter.notifyDataSetChanged()

        }

        binding.apply {
            btnSubmit.setOnClickListener {
                if (!isEditable) return@setOnClickListener

                // 保存数据
                mPetition.apply {
                    this.creator.target = Global.currentUser
                    this.createTime = Date()
                }
                Global.boxStore.boxFor<Petition>().put(mPetition)

                // 通知列表刷新
                sendEvent(PetitionerPutEvent())
                finish()
            }

//            btnReply.setOnClickListener {
//                MaterialDialog(this@PetitionDetailAct).show {
//                    title(text = "请输入答复内容")
//                    input()
//                    positiveButton { dialog ->
//                        setReply(dialog.getInputField().text.toString())
//                        Global.boxStore.boxFor<Petition>().put(mPetition)
//                    }
//                }
//            }

            btnTransfer.setOnClickListener {
                ToastUtils.showShort("转办没做")
            }
        }
    }

    // Android 10适配要点，作用域存储
// https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650249029&idx=1&sn=6ab18477950e5f4e1a14dc47ecc4f763&chksm=8863662abf14ef3c1500d64c106ab2e5a6c95e716ff6e57ba379e2aabca7b6046060ccb78af2&scene=21#wechat_redirect
//    @NeedsPermission(
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//    )
//    fun showFileDialog() {
//        MaterialDialog(this).show {
//            fileChooser(
//                context = context,
//                initialDirectory = File(getExternalStorageDirectory(), "Download"),
//                emptyTextRes = R.string.empty_text,
//            ) { _, file ->
//                binding.rv.bindingAdapter.apply {
//                    val index = modelCount - 1
//                    addModels(
//                        listOf(Material(file = file)), index = index, animation = true
//                    )
//                }
//            }
//        }
//    }

    private var dialogHolder: MaterialDialog? = null

    override fun initEvents() {
        receiveEvent<StartOrcEvent> {
            dialogHolder?.dismiss()
            startOrcWrapperWithPermissionCheck()
        }

        receiveEvent<ExcelDialogEvent> { event ->
            val excelDataBox = Global.boxStore.boxFor<ExcelData>()
            val propertyQuery = excelDataBox.query().equal(
                ExcelData_.__ALL_PROPERTIES[event.queryIndex],
                event.queryValue,
                QueryBuilder.StringOrder.CASE_SENSITIVE
            ).build().property(ExcelData_.__ALL_PROPERTIES[event.queryIndex + 1])
            val results = propertyQuery.distinct().findStrings().filter { it.isNotEmpty() }
            if (results.isEmpty()) {
                ToastUtils.showShort("选择结果: ${event.queryValue}")
                val position = binding.rv2.models!!.indexOf(event.petitionField)
                onPetitionFieldValueChange(position, event.queryValue)
                return@receiveEvent
            }
            results.sortedWith(Collator.getInstance(Locale.CHINA))
            MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                title(text = "请选择")
                listItems(items = results.toList()) { _, _, text ->
                    sendEvent(
                        ExcelDialogEvent(
                            queryValue = text.toString(),
                            queryIndex = event.queryIndex + 1,
                            petitionField = event.petitionField
                        )
                    )
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
            ethnic = result.ethnic.words,
            gender = result.gender.words,
            idNumber = result.idNumber.words,
            name = result.name.words
        )
    }

    private var mPetition = Petition()

    private fun onPetitionFieldValueChange(position: Int, value: String) {
        binding.rv2.bindingAdapter.apply {
            // 展示
            val petitionField = models!![position] as PetitionField
            petitionField.value = value
            notifyItemChanged(position)

            // 反射修改 mPetition
            val kProperty1 = Petition::class.memberProperties.elementAt(position)
            kProperty1 as KMutableProperty1<Petition, String>
            kProperty1.set(mPetition, value)
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