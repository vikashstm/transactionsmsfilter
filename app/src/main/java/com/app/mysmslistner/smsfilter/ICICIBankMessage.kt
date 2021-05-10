package com.app.mysmslistner.smsfilter

import java.util.regex.Pattern

class ICICIBankMessage {


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


     fun getPayeNameICICI(sender: String, message: String): String {
        var payName = ""
        if (sender.contains("ICICIB", true)) {
            // for message one
            if (message.contains("Debit Card", true)) {
                if (message.contains("Info", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("Imps", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                }else{
                    payName="debited"
                }
            } else if (message.contains("Credited", true) && message.contains("UPI", true)) {
                if (message.contains("from", true)) {
                    var payeTemp = message.split("from")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("Info", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                }else{
                    payName="credited"
                }
            } else if (message.contains("Credited", true)) {
                if (message.contains("from", true)) {
                    var payeTemp = message.split("from")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("Info", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("a/c", true)) {
                    var payeTemp = message.split("no.")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("Imps", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                }else{
                    payName="credited"
                }
            } else if (message.contains("Credit Card", true) || message.contains("UPI", true)) {
                if (message.contains("at", true)) {
                    var payeTemp = message.split("at")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("Imps", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                }else if (message.contains("Info", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                }
            } else {
                if (message.contains("Info", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                } else if (message.contains("Imps", true)) {
                    var payeTemp = message.split(":")
                    if (payeTemp.isNotEmpty()) {
                        // get temp
                        var payes = payeTemp[1].trim().split(".")
                        payName = payes[0]
                    }
                }
            }

        }


        return payName

    }
}