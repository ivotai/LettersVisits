package com.unicorn.lettersVisits.app

import android.content.Context
import com.unicorn.lettersVisits.data.model.ExcelData
import io.objectbox.kotlin.boxFor
import jxl.Workbook
import java.io.InputStream


class JExcelApiHelper {

    private val excelDataBox = Global.boxStore.boxFor<ExcelData>()

    fun readExcel(context: Context) {
        if (excelDataBox.isEmpty.not()) return

        val inputStream: InputStream = context.assets.open("来访信息录入项.xls")
        val workbook = Workbook.getWorkbook(inputStream)
        val sheet = workbook.getSheet(0)

        val datas = mutableListOf<ExcelData>()

        var row = sheet.getRow(0)
        var oldData = ExcelData().apply {
            d1 = row[0].contents
            d2 = row[1].contents
            d3 = row[2].contents
            d4 = row[3].contents
            d5 = row[4].contents
            d6 = row[5].contents
            d7 = row[6].contents
            d8 = row[7].contents
            d9 = row[8].contents
        }

        for (i in 1 until sheet.rows) {
            row = sheet.getRow(i)
            val data = ExcelData().apply {
                d1 = row[0].contents.ifEmpty { oldData.d1 }
                d2 = row[1].contents.ifEmpty { oldData.d2 }
                d3 = row[2].contents.ifEmpty { oldData.d3 }
                d4 = row[3].contents.ifEmpty { oldData.d4 }
                d5 = row[4].contents.ifEmpty { oldData.d5 }
                d6 = row[5].contents.ifEmpty { oldData.d6 }
                d7 = row[6].contents.ifEmpty { oldData.d7 }
                d8 = row[7].contents.ifEmpty { oldData.d8 }
                d9 = row[8].contents.ifEmpty { oldData.d9 }
            }
            datas.add(data)
            oldData = data
        }
        excelDataBox.put(datas)
    }

}