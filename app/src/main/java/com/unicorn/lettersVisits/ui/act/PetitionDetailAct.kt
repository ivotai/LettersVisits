package com.unicorn.lettersVisits.ui.act


import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.View
import androidx.core.graphics.ColorUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.baidu.ocr.sdk.model.IDCardResult
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.*
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.unicorn.lettersVisits.R
import com.unicorn.lettersVisits.app.Global
import com.unicorn.lettersVisits.data.model.ExcelData
import com.unicorn.lettersVisits.data.model.ExcelData_
import com.unicorn.lettersVisits.data.model.event.ExcelDialogEvent
import com.unicorn.lettersVisits.data.model.event.PetitionerPutEvent
import com.unicorn.lettersVisits.data.model.event.StartOrcEvent
import com.unicorn.lettersVisits.data.model.petition.*
import com.unicorn.lettersVisits.data.model.support.SupportDivider
import com.unicorn.lettersVisits.databinding.ActAddPetitionBinding
import com.unicorn.lettersVisits.databinding.GroupPetitionFieldBinding
import com.unicorn.lettersVisits.databinding.ItemPetitionFieldBinding
import io.objectbox.kotlin.boxFor
import io.objectbox.query.QueryBuilder
import me.saket.cascade.CascadePopupMenu
import me.saket.cascade.add
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import splitties.resources.color
import java.text.Collator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties


@RuntimePermissions
class PetitionDetailAct : BaiduOrcAct<ActAddPetitionBinding>() {


    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    override fun initViews() {
        binding.apply {
            titleBar.setTitle("信访申请")

            // 设置按钮点击效果
            btnSubmit.helper.backgroundColorPressed = ColorUtils.blendARGB(
                color(R.color.main_color), Color.WHITE, 0.3f
            )

            fun getData(): List<Any> {
                val data = listOf(
                    PetitionGroup("来访人信息"),
                    PetitionField(
                        label = "姓名", inputType = InputType.TEXT, allowEmpty = false
                    ),
                    PetitionField(label = "年龄", inputType = InputType.TEXT, allowEmpty = false),
                    PetitionField(
                        label = "性别", inputType = InputType.SELECT, allowEmpty = false
                    ).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = label, queryIndex = 2, petitionField = this
                        )
                    },
                    PetitionField(
                        label = "民族", inputType = InputType.SELECT, allowEmpty = false
                    ).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = label, queryIndex = 2, petitionField = this
                        )
                    },
                    PetitionField(
                        label = "证件类型", inputType = InputType.SELECT, allowEmpty = false
                    ).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = label, queryIndex = 2, petitionField = this
                        )
                    },
                    PetitionField(
                        label = "证件号",
                        inputType = InputType.TEXT,
                        petitionFieldType = PetitionFieldType.BOTTOM,
                        allowEmpty = false
                    ),

                    //
                    SupportDivider(),
                    PetitionGroup("其他信息"),
                    PetitionField(
                        label = "类别",
                        inputType = InputType.SELECT,
                    ).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = label, queryIndex = 2, petitionField = this
                        )
                    },
                    PetitionField(label = "职业", inputType = InputType.SELECT).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = label, queryIndex = 2, petitionField = this
                        )
                    },
                    PetitionField(label = "邮政编码", inputType = InputType.TEXT),
                    PetitionField(label = "固定电话", inputType = InputType.TEXT),
                    PetitionField(label = "移动电话", inputType = InputType.TEXT),
                    PetitionField(label = "来访次数", inputType = InputType.TEXT),
                    PetitionField(label = "律师证号", inputType = InputType.TEXT),
                    PetitionField(label = "组织机构代码", inputType = InputType.TEXT),
                    PetitionField(label = "涉诉法院", inputType = InputType.SELECT).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = "最高人民法院  （备注：关联各审级的涉诉法院信息）",
                            queryIndex = 2,
                            petitionField = this
                        )
                    },
                    PetitionField(
                        label = "来访案件类型",
                        inputType = InputType.SELECT,
                        petitionFieldType = PetitionFieldType.BOTTOM
                    ).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = label, queryIndex = 2, petitionField = this
                        )
                    },
                    SupportDivider(),
                    PetitionGroup("越级转办"),
                    PetitionField(
                        label = "通知法院",
                        inputType = InputType.SELECT,
                    ).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = "最高人民法院  （备注：关联各审级的涉诉法院信息）",
                            queryIndex = 2,
                            petitionField = this
                        )
                    },
                    PetitionField(label = "通知人", inputType = InputType.TEXT),
                    PetitionField(label = "通知时间", inputType = InputType.DATETIME),
                    PetitionField(label = "领走法院", inputType = InputType.SELECT).apply {
                        excelDialogEvent = ExcelDialogEvent(
                            queryValue = "最高人民法院  （备注：关联各审级的涉诉法院信息）",
                            queryIndex = 2,
                            petitionField = this
                        )
                    },
                    PetitionField(label = "领走人", inputType = InputType.TEXT),
                    PetitionField(
                        label = "领走时间",
                        inputType = InputType.DATETIME,
                        petitionFieldType = PetitionFieldType.BOTTOM
                    ),
                )
                return data
            }

            rv.linear().divider {
                setColorRes(splitties.material.colors.R.color.grey_200)
                setDivider(width = 1, dp = false)
            }.setup {
                addType<PetitionField>(R.layout.item_petition_field)
                addType<SupportDivider>(R.layout.divider_support)
                addType<PetitionGroup>(R.layout.group_petition_field)
                onBind {
                    when (val item = getModel<Any>()) {
                        is PetitionField -> {
                            val binding = getBinding<ItemPetitionFieldBinding>()

                            // 设置圆角
                            val helper = binding.root.helper
                            val dp16 = SizeUtils.dp2px(16f).toFloat()
                            when (item.petitionFieldType) {
                                PetitionFieldType.TOP -> {
                                    helper.cornerRadiusTopLeft = dp16
                                    helper.cornerRadiusTopRight = dp16
                                    helper.cornerRadiusBottomLeft = 0f
                                    helper.cornerRadiusBottomRight = 0f
                                }
                                PetitionFieldType.BOTTOM -> {
                                    helper.cornerRadiusTopLeft = 0f
                                    helper.cornerRadiusTopRight = 0f
                                    helper.cornerRadiusBottomLeft = dp16
                                    helper.cornerRadiusBottomRight = dp16
                                }
                                PetitionFieldType.MIDDLE -> {
                                    helper.cornerRadiusTopLeft = 0f
                                    helper.cornerRadiusTopRight = 0f
                                    helper.cornerRadiusBottomLeft = 0f
                                    helper.cornerRadiusBottomRight = 0f
                                }
                            }

                            // 展示数据
                            binding.apply {
                                l.text = item.label
                                tv.hint = item.inputType.hint
                                tv.text = item.value
                            }
                        }
                        is PetitionGroup -> {
                            val binding = getBinding<GroupPetitionFieldBinding>()
                            binding.tv.text = item.name
                        }
                        is SupportDivider -> {
                            // do nothing
                        }
                    }
                }
                onFastClick(R.id.tv) {
                    if (!isEditable) return@onFastClick
                    val item = getModel<PetitionField>()
                    when (item.inputType) {
                        InputType.TEXT -> {
                            MaterialDialog(this@PetitionDetailAct).show {
                                title(text = "请输入${item.label}")
                                input { _, text ->
                                    onPetitionFieldValueChange(item, text.toString())
                                }
                                positiveButton(text = "确认")
                            }
                        }
                        InputType.SELECT -> {
                            sendEvent(item.excelDialogEvent)
                        }
                        InputType.DATETIME -> {
                            MaterialDialog(this@PetitionDetailAct).show {
                                dateTimePicker() { _, dateTime ->
                                    // 创建日期格式化器，指定格式
                                    SimpleDateFormat.getDateTimeInstance()
                                    val formatter = SimpleDateFormat("yyyy年MM月dd日\nHH点mm分")
                                    // 格式化日期
                                    val dateTimeString = formatter.format(dateTime.time)
                                    // 返回格式化后的日期字符串
                                    onPetitionFieldValueChange(item, dateTimeString)
                                }
                            }
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }.models = getData()
            prepareData()

            // 可否编辑
            val id = intent.getLongExtra("id", -1L)
            isCreating = id == -1L
            if (isCreating) {
                isEditable = true
            } else {
                petition = Global.boxStore.boxFor<Petition>().get(id)
                // 如果是创建者，也启用编辑
                if (petition.creator.target == Global.currentUser) {
                    isEditable = true
                }
            }
            btnSubmit.visibility = if (isEditable) View.VISIBLE else View.GONE

            // 展示数据
            rv.models?.filterIsInstance<PetitionField>()?.forEachIndexed { index, model ->
                val kProperty1 = Petition::class.memberProperties.elementAt(index)
                model.value = kProperty1.get(petition) as String
            }
            rv.bindingAdapter.notifyDataSetChanged()
        }
    }

    override fun initIntents() {
        super.initIntents()

        binding.apply {
            btnSubmit.setOnClickListener {
                if (!isEditable) return@setOnClickListener

                // 非空校验
                rv.models?.filterIsInstance<PetitionField>()?.forEach {
                    if (!it.allowEmpty && it.value.isEmpty()) {
                        ToastUtils.showShort("${it.label}不能为空")
                        binding.rv.smoothScrollToPosition(it.modelPosition)
                        return@setOnClickListener
                    }
                }

                // 设置创建信息
                if (isCreating) {
                    petition.also {
                        it.creator.target = Global.currentUser
                        it.createTime = Date()
                    }
                }
                Global.boxStore.boxFor<Petition>().put(petition)

                // 通知列表刷新
                sendEvent(PetitionerPutEvent())
                finish()
            }

            titleBar.setOnMoreClickListener {
                fun showPopupMenu() {
                    CascadePopupMenu(
                        this@PetitionDetailAct, binding.titleBar.getMoreView()
                    ).also { popupMenu ->
                        popupMenu.menu.add(title = "扫描身份证", onClick = {
                            sendEvent(StartOrcEvent())
                        })
                        popupMenu.menu.add(title = "通知越级转办法院", onClick = {
                            binding.rv.models!!.filterIsInstance<PetitionField>().filter {
                                it.petitionFieldPosition in listOf(16, 17)
                            }.forEach {
                                if (it.value.isEmpty()) {
                                    ToastUtils.showShort("${it.label}不能为空")
                                    binding.rv.smoothScrollToPosition(it.modelPosition)
                                    return@add
                                }
                            }
                            ToastUtils.showShort("已通知!")
                        })
                    }.show()
                }
                showPopupMenu()
            }
        }
    }

    override fun initEvents() {
        receiveEvent<StartOrcEvent> {
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
                onPetitionFieldValueChange(event.petitionField, event.queryValue)
                return@receiveEvent
            }
            results.sortedWith(Collator.getInstance(Locale.CHINA))
            MaterialDialog(this@PetitionDetailAct, BottomSheet()).show {
                title(text = "请选择${event.petitionField.label}")
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

        fun calculateAge(birthdate: String): Int {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val birthdateObj = LocalDate.parse(birthdate, formatter)
            val age = ChronoUnit.YEARS.between(birthdateObj, today).toInt()

            if (today.monthValue < birthdateObj.monthValue || (today.monthValue == birthdateObj.monthValue && today.dayOfMonth < birthdateObj.dayOfMonth)) {
                return age - 1
            }

            return age
        }

        binding.rv.models!!.filterIsInstance<PetitionField>().also {
            onPetitionFieldValueChange(it[0], result.name.words)
            onPetitionFieldValueChange(it[1], calculateAge(result.birthday.words).toString())
            onPetitionFieldValueChange(it[2], result.gender.words)
            onPetitionFieldValueChange(it[3], result.ethnic.words)
            onPetitionFieldValueChange(it[4], "居民身份证")
            onPetitionFieldValueChange(it[5], result.idNumber.words)
        }
    }

    private var isEditable = false

    private var isCreating = false

    private var petition = Petition()


    // petitionFieldPosition:
    // modelPosition: AnyPosition
    private fun onPetitionFieldValueChange(
        item: PetitionField, value: String
    ) {
        binding.rv.bindingAdapter.apply {
            // 刷新界面
            item.value = value
            notifyItemChanged(item.modelPosition)

            // 修改 petition 对象
            val kProperty1 = Petition::class.memberProperties.elementAt(item.petitionFieldPosition)
            kProperty1 as KMutableProperty1<Petition, String>
            kProperty1.set(petition, value)
        }
    }

    private fun prepareData() {
        // 处理两个 position
        val data = binding.rv.models!!
        val petitionFields = data.filterIsInstance<PetitionField>()
        petitionFields.forEach { petitionField ->
            petitionField.petitionFieldPosition = petitionFields.indexOf(petitionField)
        }
        data.forEach { any ->
            if (any is PetitionField) {
                any.modelPosition = data.indexOf(any)
            }
        }
    }

    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun startOrcWrapper() {
        startOrc()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

}