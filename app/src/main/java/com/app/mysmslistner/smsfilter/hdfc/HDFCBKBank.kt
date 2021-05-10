package com.app.mysmslistner.smsfilter.hdfc

import java.util.regex.Pattern

class HDFCBKBank {
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
        if (sender.contains("HDFCBK", true)) {
            if (message.contains("spent", true)) {
                if (message.contains("at", true)) {
                    // split from :
                    var data = message.split(".")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(".")[0]
                    }
                }

            } else if (message.contains("paying", true)) {
                if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(".")[0]
                    }
                } else if (message.contains("from", true)) {
                    // split from :
                    var data = message.split("from")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                }

            } else if (message.contains("from", true)) {
                if (message.contains("from", true)) {
                    // split from :
                    var data = message.split("from")
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
                    var data = message.split("A/c")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                }

            } else if (message.contains("credited", true)) {
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
                    var data = message.split("A/c")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                }
            } else if (message.contains("Credit card", true)) {
                if (message.contains("spent", true)) {
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
                            payName = data[1].trim().split(".")[0]
                        }
                    } else if (message.contains("at", true)) {
                        // split from :
                        var data = message.split("at")
                        if (data.isNotEmpty()) {
                            payName = data[1].trim().split(",")[0]
                        }
                    }
                }

            } else if (message.contains("deposited", true)) {
                if (message.contains("NEFT", true)) {
                    // split from :
                    var data = message.split("NEFT")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(".")[0]
                    }
                } else if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                } else if (message.contains("at", true)) {
                    // split from :
                    var data = message.split("at")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                }
            } else if (message.contains("debited", true)) {
                if (message.contains("Info", true)) {
                    // split from :
                    var data = message.split(":")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(".")[0]
                    }
                } else if (message.contains("to", true)) {
                    // split from :
                    var data = message.split("to")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                } else if (message.contains("at", true)) {
                    // split from :
                    var data = message.split("at")
                    if (data.isNotEmpty()) {
                        payName = data[1].trim().split(",")[0]
                    }
                }
            }
        }

        return payName
    }

     fun getPayGetInfo(message: String):String{
        var info=""
        if(message.contains("debited",true) && message.contains("Info-",true)){
            if(message.contains("Pay To",true)){
                var data = message.split("Pay To")
                if (data.isNotEmpty()) {
                    if(data[1].contains(".")) {
                        info = data[1].trim().split(".")[0]
                    }
                }
            }

        }else if(message.contains("NEFT",true)){
            if(message.contains("deposited",true)){
                var data = message.split("NEFT")
                if (data.isNotEmpty()) {
                    if(data[1].contains(".")) {
                        info = data[1].trim().split(".")[0]
                    }
                }
            }

        }else if(message.contains("Credit Card",true)){
            if(message.contains("spent",true)){
                var data = message.split("at")
                if (data.isNotEmpty()) {
                    if(data[1].contains("on")) {
                        info = data[1].trim().split("on")[0]
                    }
                }
            }else if(message.contains("BillPay")){
                var data = message.split("for")
                if (data.isNotEmpty()) {
                        info = data[1].trim().split(".")[0]

                }
            }

        }else if(message.contains("deposited",true) && message.contains("UPI-",true)){
            var data = message.split("for")
            if (data.isNotEmpty()) {
                info = data[1].trim().split(".")[0]

            }
        }else if(message.contains("Netbanking",true)){
            if(message.contains("paying",true)) {
                var temp = message.split("from")
                if (temp.isNotEmpty()) {
                    var data = temp[1].split("to")
                    if (data.isNotEmpty()) {
                        if (data[1].contains(".")) {
                            info = data[1].trim().split(".")[0]
                        }
                    }
                }
            }
        }else if(message.contains("Credited",true)){
            if(message.contains("VPA",true) && message.contains("(")
                && message.contains(")")){
                var data = message.split("VPA")
                if (data.isNotEmpty()) {
                    info = data[1].trim().split(")")[0]

                }
            }

        }else if(message.contains("debited",true) && message.contains("UPI Ref No")){
            if(message.contains("VPA",true) ){
                var data = message.split("VPA")
                if (data.isNotEmpty()) {
                    info = data[1].trim().split(")")[0]

                }
            }
        }
        return info
    }


}