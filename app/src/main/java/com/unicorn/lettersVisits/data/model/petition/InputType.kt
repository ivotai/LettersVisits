package com.unicorn.lettersVisits.data.model.petition

enum class InputType(
    val hint: String = "",
) {
    TEXT(hint = "请输入"),
    NUMBER,
    DATE,
    TIME,
    DATETIME(hint = "请选择"),
    SELECT(hint = "请选择"),
    MULTISELECT,
    RADIO,
    CHECKBOX,
    TEXTAREA,
    FILE,
    IMAGE,
    SIGNATURE,
    LOCATION,
    BARCODE,
    QR,
    EMAIL,
    PHONE,
    URL,
    PASSWORD,
    HIDDEN,
    ;

}