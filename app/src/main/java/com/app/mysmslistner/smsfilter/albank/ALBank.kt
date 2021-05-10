package com.app.mysmslistner.smsfilter.albank

import java.util.regex.Pattern

class ALBank {

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
        if (sender.contains("ALBANK", true)) {
            if (message.contains("UPI", true)) {
                if (message.contains("at", true)) {
                    // split from :
                    var data = message.split("at")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(".")[0]
                    }
                }

            }else  if (message.contains("Transfer", true)) {
                if (message.contains("by", true)) {
                    // split from :
                    var data = message.split("by")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(".")[0]
                    }
                }else if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(",")[0]
                    }
                }

            }else  if (message.contains("through", true)) {
                if (message.contains("through", true)) {
                    // split from :
                    var data = message.split("through")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split("by")[0]
                    }
                }else if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(",")[0]
                    }
                }

            }
            else  if (message.contains("Deducted", true)) {
                if (message.contains("by", true)) {
                    // split from :
                    var data = message.split("by")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(".")[0]
                    }
                }else if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(",")[0]
                    }
                }else if (message.contains("for", true)) {
                    // split from :
                    var data = message.split("A/c")
                    if (data.isNotEmpty()) {
                        payName=data[1].trim().split(",")[0]
                    }
                }

            }else  if (message.contains("credited", true)) {
                if (message.contains("by", true)) {
                    // split from :
                    var data = message.split("by")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(".")[0]
                    }
                } else if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                } else if (message.contains("for", true)) {
                    // split from :
                    var data = message.split("credited")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                }
            }
        }

        return payName
    }
}