package com.app.mysmslistner.smsfilter.idfcbank

import java.util.regex.Pattern

class IDFCFBBank {

    fun getRefNumber(body: String): String {
        var refNumber = ""
        if (body.contains("REF", true)) {
            if (body.contains("RefNo", true)) {
                var dataList = body.split("RefNo")
                val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    val m1 = p1.matcher(dataList[1])
                    while (m1.find()) {
                        data = m1.group()
                        break
                    }
                } else {
                    val m1 = p1.matcher(dataList[0])
                    while (m1.find()) {
                        data = m1.group()
                        break
                    }
                }
                when {
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }

            } else if (body.contains("Ref no", true)) {
                var dataList = body.split("Ref no")
                val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    val m1 = p1.matcher(dataList[1])
                    while (m1.find()) {
                        data = m1.group()
                        break
                    }
                } else {
                    val m1 = p1.matcher(dataList[0])
                    while (m1.find()) {
                        data = m1.group()
                        break
                    }
                }
                when {
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }

            } else if (body.contains("Ref#", true)) {
                var dataList = body.split("Ref#")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                when {
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }

            }
            // var dataList = smsDto.body.split("Ref")
        }
        return refNumber
    }

    fun getPayTo(sender: String, message: String): String {
        var payName = ""
        if (sender.contains("IDFCFB", true)) {
            if(message.contains("purchase",true)){
                if(message.contains("on",true)){
                    // split from :
                    var data = message.split("on")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(".")[0]
                    }
                }
            }else if(message.contains("credited",true)){
                if(message.contains("to",true)){
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split("(")[0]
                    }
                }else if(message.contains("and",true)){
                    // split from :
                    var data = message.split("and")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split("(")[0]
                    }
                }else if(message.contains("Info:",true)){
                    if(message.contains("NEFT",true)) {
                        // split from :
                        var data = message.split("NEFT")
                        if (data.isNotEmpty()) {
                            payName = data[1].trim().split(".")[0]
                        }
                    }else if(message.contains("UPI",true)) {
                        // split from :
                        var data = message.split(":")
                        if (data.isNotEmpty()) {
                            payName = data[1].trim()
                        }
                    }
                }else if(message.contains("FasTag",true)){
                    // split from :
                    var data = message.split("by")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(".")[0]
                    }
                }
                else{
                    if(message.contains("IMPS",true)) {
                        payName = "IMPS"
                    }else {
                        payName = "credited"
                    }
                }
            }else if(message.contains("debited",true)){
                if(message.contains("from Tag",true)){
                    // split from :
                    var data = message.split("At")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split("(")[0]
                    }
                }else {
                    payName="debited"
                }
            }
        }
        return payName
    }
}