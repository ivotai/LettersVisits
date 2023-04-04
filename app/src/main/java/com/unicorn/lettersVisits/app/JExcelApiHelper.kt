package com.unicorn.lettersVisits.app

import android.content.Context
import com.unicorn.lettersVisits.data.model.ExcelData
import com.unicorn.lettersVisits.data.model.ExcelData_.*
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
            projectName = row[0].contents
            moduleName = row[1].contents
            entryName = row[2].contents
            level1 = row[3].contents
            level2 = row[4].contents
            level3 = row[5].contents
            level4 = row[6].contents
            level5 = row[7].contents
            level6 = row[8].contents
        }

        for (i in 1 until sheet.rows) {
             row = sheet.getRow(i)
            val data = ExcelData().apply {
                projectName = row[0].contents.ifEmpty { oldData.projectName }
                moduleName = row[1].contents.ifEmpty { oldData.moduleName }
                entryName = row[2].contents.ifEmpty { oldData.entryName }
                level1 = row[3].contents.ifEmpty { oldData.level1 }
                level2 = row[4].contents.ifEmpty { oldData.level2 }
                level3 = row[5].contents.ifEmpty { oldData.level3 }
                level4 = row[6].contents.ifEmpty { oldData.level4 }
                level5 = row[7].contents.ifEmpty { oldData.level5 }
                level6 = row[8].contents.ifEmpty { oldData.level6 }
            }
            datas.add(data)
            oldData = data
        }
        excelDataBox.put(datas)
    }

}