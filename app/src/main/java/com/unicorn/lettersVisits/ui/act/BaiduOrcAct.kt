package com.unicorn.lettersVisits.ui.act

import androidx.viewbinding.ViewBinding
import com.unicorn.lettersVisits.ui.base.BaseAct
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
abstract class BaiduOrcAct<VB : ViewBinding>: BaseAct<VB>() {

    abstract fun onResult(result: String)

}