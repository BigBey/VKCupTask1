package ru.bey_sviatoslav.android.vkcuptask1.extra

import java.util.*

fun Long.getSize() : String{
    when {
        this < 1000000 -> return "${(this/100.0).toInt()/10.0} КБ"
        else -> return "${(this/100000.0).toInt()/10.0} МБ"
    }
}

fun Long.toDate() : String {
    val mydate = Calendar.getInstance()
    mydate.timeInMillis = this * 1000
    if(mydate.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
        return "${mydate.get(Calendar.DAY_OF_MONTH)} ${mydate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale("ru")).substring(0,3)}"
    }else{
        return "${mydate.get(Calendar.DAY_OF_MONTH)} ${mydate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale("ru")).substring(0,3)} ${mydate.get(Calendar.YEAR)}"
    }
}