/*
package com.app.mysmslistner

package com.app.mysmslistner

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.mysmslistner.models.LocalSmsGet
import com.app.mysmslistner.models.Sm
import com.app.mysmslistner.mydata.Rules
import com.google.gson.Gson
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private var listItem = ArrayList<String>()

    companion object {
        const val PERMISSIONS_REQUEST_READ_SMS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.myList)

    }

    override fun onResume() {
        super.onResume()
        if(listItem.isNotEmpty()){
            listItem.clear()
        }
        permission()
    }

    fun linearSearch(list:List<Any>, key:Any):Int?{
        for ((index, value) in list.withIndex()) {
            if (value == key){
                return index
            }
        }
        return null
    }


    private fun permission() {
        val permissions = arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
        Permissions.check(
            this */
/*context*//*
,
            permissions,
            null */
/*rationale*//*
,
            null */
/*options*//*
,
            object : PermissionHandler() {
                override fun onGranted() {
                    // do your task.
                    var v = fetchInbox()
                    if (!v.isNullOrEmpty()) {
                        var parseData = parsevalues(v[0].sms as ArrayList<Sm>)
                        // Log.e("SMS GET", parsevalues(v?.get(0)!!.sms!! as ArrayList<Sm>).toString())
                        //getSmSFiletr(v[0].sms as ArrayList<Sm>)

                        if (!parseData.isNullOrEmpty()) {
                            parseData.distinctBy { sm: Sm -> sm.refNumber  }
                            var copyData=parseData
                            for (i in 0 until parseData.size) {
                                if (parseData[i].transactionType != null && parseData[i].transactionType.trim()
                                        .isNotEmpty() && !parseData[i].body.contains("due", true)
                                    && !parseData[i].body.contains("Reminder", true)
                                    && !parseData[i].body.contains("EMI", true)
                                    && !parseData[i].body.contains("Delivered", true)
                                    && !parseData[i].body.contains("balance in", true)
                                    && !parseData[i].body.contains("E-statement", true)
                                    && !parseData[i].body.contains("Block", true)
                                    && !parseData[i].body.contains("Blocked", true)
                                    && parseData[i].transactionType.trim()
                                        .isNotEmpty() || parseData[i].body.contains(
                                        "SBIDrCARD",
                                        true
                                    )
                                )
                                // if ref number is empty then we show transaction type by client logic
                                    if(parseData[i].refNumber.trim().isEmpty()){
                                        parseData[i].refNumber=parseData[i].transactionType
                                    }


                                listItem.add(
                                    "\n \n FULL BODY -> " + parseData[i].body +
                                            " \n \nSender Type -> " + parseData[i].sender + " \n Type Card ->" + parseData[i].cardType
                                            + "\n Transaction Type-> " + parseData[i].transactionType + "\n Amount ->" + parseData[i].amount + "\n Account number ->  "
                                            + parseData[i].accountNumber +
                                            "\n INFO :-" + parseData[i].refNumber */
/*+
                                                "\n PAY TO:-" + parseData[i].payiName*//*

                                            + "\n \n ***************************"
                                )
                            }

                            val adapters: ArrayAdapter<String> = ArrayAdapter<String>(
                                this@MainActivity,
                                android.R.layout.simple_list_item_1, android.R.id.text1, listItem
                            )

                            listView?.adapter = adapters
                            Log.e("LIST SIZE"+ "onGranted: ",listItem.size.toString() )
                        } else {
                            val adapters: ArrayAdapter<String> = ArrayAdapter<String>(
                                this@MainActivity,
                                android.R.layout.simple_list_item_1, android.R.id.text1, listItem
                            )

                            listView?.adapter = adapters

                            Toast.makeText(
                                this@MainActivity,
                                "No Transaction SMS Found! ALL MSG",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No Transaction SMS Found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

    }


    @Throws(ParseException::class)
    fun isValidDate(pDateString: String?): Boolean {
        val date: Date = SimpleDateFormat("dd-MMM-yy").parse(pDateString)
        return Date().before(date)
    }


    // getting amount by matching the pattern
    fun getAmount(data: String?): String? {
        // pattern - rs. **,***.**
        val pattern1 = "(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)"
        val regex1: Pattern = Pattern.compile(pattern1)
        // pattern - inr **,***.**
        val pattern2 = "(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)"
        val regex2: Pattern = Pattern.compile(pattern2)
        val matcher1: Matcher = regex1.matcher(data)
        val matcher2: Matcher = regex2.matcher(data)
        if (matcher1.find()) {
            try {
                var a: String = matcher1.group(0)
                a = a.replace("inr", "")
                a = a.replace(" ", "")
                a = a.replace(",", "")
                return a
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (matcher2.find()) {
            try {
                // searched for rs or inr preceding number in the form of rs. **,***.**
                var a: String = matcher2.group(0)
                a = a.replace("rs", "")
                a = a.replaceFirst(".".toRegex(), "")
                a = a.replace(" ", "")
                a = a.replace(",", "")
                return a
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null

    }

    private fun getSmSFiletr(body_val: ArrayList<Sm>) {
        // dummy my Code
        val gson = Gson()
        val values: Rules =
            gson.fromJson(this.assets.readAssetsFile("rules.json"), Rules::class.java)
        for (i in 0 until body_val.size) {
            val smsDto: Sm = body_val[i]
            if (checkSenderIsValid(smsDto.sender)) {
                for (j in values.rules.indices) {
                    var model = values.rules[j]
                    //      for (sender in 0 until model.senders.size) {
                    if (model.name.contains("ICICI", true)
                        || model.name.contains("SBI", true)
                        || model.name.contains("IDFC", true)
                        || model.name.contains("HDFC", true)
                    ) {
                        for (k in model.patterns.indices) {
                            var patte = model.patterns[k]
                            val patternEx =
                                Pattern.compile(patte.regex)
                            // Find instance of pattern matches
                            val match = patternEx.matcher(smsDto.body)
                            if (match.find()) {
                                println("MY LOG " + match.group())
                                var list = ArrayList<String>()
                                listItem.add("Sender Body:-  ${smsDto.body} \n\n\n SENDER TYEP:-- " + smsDto.sender + "\n\n" + match.group())
                                val adapters: ArrayAdapter<String> = ArrayAdapter<String>(
                                    this@MainActivity,
                                    android.R.layout.simple_list_item_1,
                                    android.R.id.text1,
                                    listItem.distinct()
                                )
                                listView?.adapter = adapters
                            } else {
                                println("MY MERCHSNT " + "notfound")
                            }
                        }
                    }
                    //   }


                }
            }
        }
    }

    private fun getRefComanNumber(body: String): String {
        //Info, At, Linked to, NEFT, Ref, transfer from, transfer to, for, of, IMPS
        //Till dot Space, on, has
        var refNumber = ""
        if(body.contains("Credit card ending",true)){
            var dataList = body.toLowerCase().split("from")
            var data = ""
            data = if (dataList.size == 2) {
                dataList[1]
            } else {
                dataList[0]
            }
            refNumber = when {
                data.contains("on", true) -> {
                    data.split(" on")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                else -> {
                    data
                }
            }

        }else if (body.contains("NEFT", true)) {
            var dataList = body.toLowerCase().split("NEFT")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size == 2) {
                data = dataList[1]
            } else {
                data = dataList[0]
            }
            refNumber = when {
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                else -> {
                    data
                }
            }
        }else if (body.contains("IMPS", true)) {
            if (body.contains("Ref no")) {
                var dataList = body.split("Ref no")
                val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
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
            } else {
                var dataList = body.split("IMPS")
                val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
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
            }


        } else if (body.contains("RefNo", true)) {
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
                data.contains("on", true) -> {
                    refNumber = data.toLowerCase().split(" on")[0]
                }
                data.contains("has", true) -> {
                    refNumber = data.toLowerCase().split(" has")[0]
                }
                else -> {
                    refNumber = data
                }
            }

        } else if (body.contains("Ref no", true)) {
            if(body.contains("VPA",true)){
                var dataList = body.toLowerCase().split("VPA")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains("on", true) -> {
                        refNumber = data.split(" on")[0]
                    }
                    data.contains("has", true) -> {
                        refNumber = data.split(" has")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            }else{
                var dataList = body.toLowerCase().split("ref no")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    data.contains("on", true) -> {
                        refNumber = data.split(" on")[0]
                    }
                    data.contains("has", true) -> {
                        refNumber = data.split(" has")[0]
                    }
                    else -> {
                        refNumber = data
                    }
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
                data.decapitalize().contains("on", true) -> {
                    refNumber = data.split(" on")[0]
                }
                data.decapitalize().contains("has", true) -> {
                    refNumber = data.split(" has")[0]
                }
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
        } else if (body.contains("Info", true)) {
            var dataList = body.split("Info")
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
        } else if (body.contains("Received", true)) {
            if (body.contains("via", true)) {
                var dataList = body.split("via")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.decapitalize().contains("on", true) -> {
                        data.split(" on")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        data.split(".")[0]
                    }
                    else -> {
                        data
                    }
                }

            } else if (body.contains("has been", true)) {
                var dataList = body.split("has")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[2]
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.decapitalize().contains(" on", true) -> {
                        data.split("on")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        data.split(".")[0]
                    }
                    else -> {
                        data
                    }
                }
            }

        } else if (body.contains("ATM", true)) {
            if (body.contains("txn#",true)) {
                var dataList = body.split("ATM")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
                    if(dataList.size>2){
                        data = dataList[2]
                    }else{
                        data = dataList[1]
                    }
                } else {
                    data = dataList[0]
                }
                when {
                    data.decapitalize().contains("fm", true) -> {
                        refNumber = data.split("fm")[0]
                    }
                    data.decapitalize().contains("has", true) -> {
                        refNumber = data.split(" has")[0]
                    }
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
            } else if (body.contains("tx",true)) {
                var dataList = body.split("tx#")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains("fm", true) -> {
                        refNumber = data.split("fm")[0]
                    }
                    data.contains("for", true) -> {
                        refNumber = data.split("for")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }data.contains("has", true) -> {
                    refNumber = data.split(" has")[0]
                }
                    else -> {
                        refNumber = data
                    }
                }
            }

        }else if(body.contains("by transfer",true)){
            refNumber="Transfer"
        }else if (body.contains("for UPI", true)) {
            var dataList = body.toLowerCase().split("upi-")
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
        }else if (body.contains("Credit Card", true)) {
            if(body.contains("Credit card ending",true)){
                var dataList = body.toLowerCase().split("from")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                when {
                    data.contains("on", true) -> {
                        refNumber = data.split(" on")[0]
                    }
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

            }else  if(body.contains("form",true)){
                var dataList = body.toLowerCase().split("from")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                when {
                    data.contains("on", true) -> {
                        refNumber = data.split(" on")[0]
                    }
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
            }else{
                var dataList = body.toLowerCase().split(" at")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains("on", true) -> {
                        refNumber = data.split(" on")[0]
                    }
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

        }
        else if (body.contains("payment", true)) {
            var dataList = body.toLowerCase().split("for")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            data = if (dataList.size >1) {
                dataList[1]
            } else {
                dataList[0]
            }
            refNumber = when {
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                else -> {
                    data
                }
            }
        } else if (body.contains("cheque Number", true)
            || body.contains("cheque No", true)) {
            if (body.contains("cheque No", true)){
                var dataList = body.toLowerCase().split("cheque no")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                when {
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    data.contains("-", true) -> {
                        refNumber = data.split("-")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            }else if (body.contains("cheque Number", true)){
                var dataList = body.toLowerCase().split("cheque number")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                when {
                    data.contains(".", true) -> {
                        refNumber = data.split(".")[0]
                    }
                    data.contains("-", true) -> {
                        refNumber = data.split("-")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            }


        }
        // var dataList = smsDto.body.split("Ref")

        return refNumber

    }

    private fun parsevalues(body_val: ArrayList<Sm>): ArrayList<Sm>? {
        val resSms: ArrayList<Sm> = ArrayList()
        for (i in 0 until body_val.size) {
            val smsDto: Sm = body_val[i]
            val regEx =
                Pattern.compile("(?i)(?:RS|INR|MRP)?(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)+")
            // Find instance of pattern matches
            val m = regEx.matcher(smsDto.body)
            if (m.find()) {
                try {
                    if (checkSenderIsValid(smsDto.sender)) {
                        if (!smsDto.body.contains("stmt", true)) {
                            //  Log.e("amount_value= ", "" + m.group(0))
                            var amount = m.group(0).replace("inr".toRegex(), "")
                            amount = amount.replace("rs".toRegex(), "")
                            amount = amount.replace("inr".toRegex(), "")
                            amount = amount.replace(" ".toRegex(), "")
                            amount = amount.replace(",".toRegex(), "")
                            smsDto.amount = amount
                            // found out debit and credit
                            if (smsDto.body.contains("withdrawn", true)
                                || smsDto.body.contains("debited", true)
                                || smsDto.body.contains("spent", true)
                                || smsDto.body.contains("paying", true)
                                || smsDto.body.contains("payment", true)
                                || smsDto.body.contains("deducted", true)
                                || smsDto.body.contains("debited", true)
                                || smsDto.body.contains("purchase", true)
                                || smsDto.body.contains("dr", true)
                                && !smsDto.body.contains("otp", true)
                                || smsDto.body.contains("txn", true)
                                || smsDto.body.contains("transfer", true)
                            ) {
                                smsDto.transactionType = "debited"
                            } else if (smsDto.body.contains("credited", true)
                                || smsDto.body.contains("cr", true)
                                || smsDto.body.contains("deposited", true)
                                || smsDto.body.contains("received", true)
                                && !smsDto.body.contains("otp", true)
                                && !smsDto.body.contains("emi", true)
                            ) {
                                when {
                                    smsDto.body.contains("UPDATE:AVAILABLE bal",true) -> {
                                        smsDto.transactionType = "balance"
                                    }
                                    smsDto.body.contains("UPDATE: AVAILABLE bal",true) -> {
                                        smsDto.transactionType = "balance"
                                    }
                                    else -> {
                                        smsDto.transactionType = "credited"
                                    }
                                }

                            }
                            smsDto.parsed = "1"
                            Log.e("matchedValue= ", "" + amount)
                            when {
                                smsDto.body.contains("credit card ending", true) -> {
                                    var dataList = smsDto.body.split("ending")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                    //val m1 = p1.matcher(dataList[1])
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
                                    smsDto.accountNumber = data
                                }
                                smsDto.body.contains("UPI", true) -> {
                                    var dataList = smsDto.body.split("frm")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                    //val m1 = p1.matcher(dataList[1])
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
                                    smsDto.accountNumber = data
                                }
                                smsDto.body.contains("a/c", true) -> {
                                    if(smsDto.body.contains("no.",true)) {
                                        var dataList = smsDto.body.split("no.")
                                        val p1 =
                                            Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                        smsDto.accountNumber = data
                                    }else if(smsDto.body.contains("a/c",true)){
                                        var dataList = smsDto.body.toLowerCase().split("a/c ")
                                        val p1 =
                                            Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                        var data = ""
                                        if (dataList.size >1) {
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
                                        smsDto.accountNumber = data

                                    }
                                }

                                smsDto.body.contains("Acct", true) -> {
                                    var dataList = smsDto.body.split("Acct")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                    smsDto.accountNumber = data
                                }
                                smsDto.body.contains("Card ending", true) -> {
                                    if(smsDto.body.contains("ending", true)){
                                        var dataList = smsDto.body.toLowerCase().split("ending ")
                                        val p1 =
                                            Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                        smsDto.accountNumber = data
                                    }else  if(smsDto.body.contains("end", true)){
                                        var dataList = smsDto.body.toLowerCase().split("end")
                                        val p1 =
                                            Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                        smsDto.accountNumber = data
                                    }

                                }
                                smsDto.body.contains("account", true) -> {
                                    var dataList = smsDto.body.toLowerCase().split("account")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                    smsDto.accountNumber = data
                                }

                                smsDto.body.contains("Card", true) -> {
                                    var dataList = smsDto.body.toLowerCase().split("card")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                    smsDto.accountNumber = data
                                }

                            }
                            // check message is otp or not
                            if (!smsDto.body.contains("OTP", true)
                                && !smsDto.body.contains("minimum",true)
                                && !smsDto.body.contains("importance",true)
                                && !smsDto.body.contains("request",true)
                                && !smsDto.body.contains("limit",true)
                                && !smsDto.body.contains("convert",true)
                                && !smsDto.body.contains("emi",true)
                                && !smsDto.body.contains("avoid paying",true)
                                && !smsDto.body.contains("autopay",true)
                                && !smsDto.body.contains("E-statement",true)
                            ) {
                                // bank wise filter
                                smsDto.refNumber = getRefComanNumber(smsDto.body)
                                var cardType =
                                    findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                smsDto.cardType = cardType
                                resSms.add(smsDto)
                                */
/* when {
                                     smsDto.sender.contains("ICICIB", true) -> {
                                         var cardType =
                                             findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                         smsDto.cardType = cardType
                                         var icic = ICICIBankMessage()
                                         smsDto.refNumber = icic.getRefNumber(smsDto.body)
                                          smsDto.payiName = icic.getPayeNameICICI(smsDto.sender, smsDto.body)
                                         resSms.add(smsDto)
                                }
                                smsDto.sender.contains("SBIINB", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var sbiinbBank = SBIINBBank()
                                    smsDto.refNumber = sbiinbBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        sbiinbBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("CBSSBI", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var cbssbiBank = CBSSBIBank()
                                    smsDto.refNumber = cbssbiBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        cbssbiBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("SBIPSG", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var sbipsgBank = SBIPSGBank()
                                    smsDto.refNumber = sbipsgBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        sbipsgBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("SBIUPI", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var sbiupi = SBIUPIBank()
                                    smsDto.refNumber = sbiupi.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        sbiupi.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("SBICRD", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var sbicrd = SBICRDBank()
                                    smsDto.refNumber = sbicrd.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        sbicrd.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("ATMSBI", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var sbiAtm = ATMSBIBank()
                                    smsDto.refNumber = sbiAtm.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        sbiAtm.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("HDFCBK", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var hdfcbkBank = HDFCBKBank()
                                    smsDto.refNumber = hdfcbkBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        hdfcbkBank.getPayGetInfo(smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("ALBANK", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var alBank = ALBank()
                                    smsDto.refNumber = alBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        alBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("ANDBNK", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var andBank = ANDBank()
                                    smsDto.refNumber = andBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        andBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("IDFCFB", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var andBank = IDFCFBBank()
                                    smsDto.refNumber = andBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        andBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("QPMYAMEX", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var andBank = QPMYAMEXBank()
                                    smsDto.refNumber = andBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        andBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                                smsDto.sender.contains("AXISBK", true) -> {
                                    var cardType =
                                        findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                    smsDto.cardType = cardType
                                    var andBank = AXISBKBanking()
                                    smsDto.refNumber = andBank.getRefNumber(smsDto.body)
                                    smsDto.payiName =
                                        andBank.getPayTo(smsDto.sender, smsDto.body)
                                    resSms.add(smsDto)
                                }
                            }*//*

                            }

                        }
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                Log.e("No_matchedValue ", "No_matchedValue ")

            }
        }
        return resSms
    }

    private fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName).bufferedReader().use { it.readText() }


    @SuppressLint("NewApi")
    fun getAllSms(): List<String>? {
        val lstSms: MutableList<String> = ArrayList()
        val cr = contentResolver
        val c: Cursor? = cr.query(
            Telephony.Sms.Inbox.CONTENT_URI, arrayOf(Telephony.Sms.Inbox.BODY),  // Select body text
            null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        ) // Default
        // sort
        // order);
        val totalSMS: Int = c?.count!!
        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {
                lstSms.add(c.getString(0))
                c.moveToNext()
            }
        } else {
            throw RuntimeException("You have no SMS in Inbox")
        }
        c.close()
        return lstSms
    }


    fun fetchInbox(): ArrayList<LocalSmsGet>? {
        val sms = ArrayList<LocalSmsGet>()
        val cursor = contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(
                Telephony.Sms.Inbox.SUBSCRIPTION_ID,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.DATE,
                Telephony.Sms.Inbox.BODY
            ),  // Select body text
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )

        cursor!!.moveToFirst()
        var list = ArrayList<Sm>()
        while (cursor.moveToNext()) {
            val address = cursor.getString(1)
            val body = cursor.getString(3)
            */
/*  println("======&gt; Mobile number =&gt; $address")
                println("=====&gt; SMS Text =&gt; $body")*//*

            list.add(Sm(body, address))
            */
/* var tempAddresses = address.split("-")
             if (tempAddresses.isNotEmpty() && tempAddresses.size == 2) {

             }*//*


        }
        sms.add(LocalSmsGet(list))
        cursor.close()
        return sms
    }


    private fun findCreditCardOrDebitCard(msg: String, sender: String): String {
        if (sender.contains("ICICIB", true)
            || sender.contains("HDFCBK", true)
            || sender.contains("SBMSMS", true)
            || sender.contains("SBIINB", true)
            || sender.contains("SCISMS", true)
            || sender.contains("CBSSBI", true)
            || sender.contains("SBIPSG", true)
            || sender.contains("SBIUPI", true)
            || sender.contains("SBICRD", true)
            || sender.contains("ATMSBI", true)
            || sender.contains("QPMYAMEX", true)
            || sender.contains("IDFCFB", true)
            || sender.contains("UCOBNK", true)
            || sender.contains("CANBNK", true)
            || sender.contains("BOIIND", true)
            || sender.contains("AXISBK", true)
            || sender.contains("PAYTMB", true)
            || sender.contains("UnionB", true)
            || sender.contains("INDBNK", true)
            || sender.contains("KOTAKB", true)
            || sender.contains("CENTBK", true)
            || sender.contains("SCBANK", true)
            || sender.contains("PNBSMS", true)
            || sender.contains("DOPBNK", true)
            || sender.contains("YESBNK", true)
            || sender.contains("IDBIBK", true)
            || sender.contains("ALBANK", true)
            || sender.contains("CITIBK", true)
            || sender.contains("ANDBNK", true)
            || sender.contains("BOBTXN", true)
            || sender.contains("IOBCHN", true)
            || sender.contains("MAHABK", true)
            || sender.contains("OBCBNK", true)
            || sender.contains("RBLBNK", true)
            || sender.contains("RBLCRD", true)
            || sender.contains("SPRCRD", true)
            || sender.contains("HSBCBK", true)
            || sender.contains("HSBCIN", true)
            || sender.contains("INDUSB", true)
        ) {
            return if (msg.contains("CREDIT CARD", ignoreCase = true) ||
                msg.contains("SBICARD", ignoreCase = true)
            ) {
                "credit card"
            } else {
                "debit card"

            }
        }
        return ""

    }

    private fun checkSenderIsValid(sender: String): Boolean {
        return (sender.contains("ICICIB", true)
                || sender.contains("HDFCBK", true)
                || sender.contains("SBIINB", true)
                || sender.contains("SBMSMS", true)
                || sender.contains("SCISMS", true)
                || sender.contains("CBSSBI", true)
                || sender.contains("SBIPSG", true)
                || sender.contains("SBIUPI", true)
                || sender.contains("SBICRD", true)
                || sender.contains("ATMSBI", true)
                || sender.contains("QPMYAMEX", true)
                || sender.contains("IDFCFB", true)
                || sender.contains("UCOBNK", true)
                || sender.contains("CANBNK", true)
                || sender.contains("BOIIND", true)
                || sender.contains("AXISBK", true)
                || sender.contains("PAYTMB", true)
                || sender.contains("UnionB", true)
                || sender.contains("INDBNK", true)
                || sender.contains("KOTAKB", true)
                || sender.contains("CENTBK", true)
                || sender.contains("SCBANK", true)
                || sender.contains("PNBSMS", true)
                || sender.contains("DOPBNK", true)
                || sender.contains("YESBNK", true)
                || sender.contains("IDBIBK", true)
                || sender.contains("ALBANK", true)
                || sender.contains("CITIBK", true)
                || sender.contains("ANDBNK", true)
                || sender.contains("BOBTXN", true)
                || sender.contains("IOBCHN", true)
                || sender.contains("MAHABK", true)
                || sender.contains("OBCBNK", true)
                || sender.contains("RBLBNK", true)
                || sender.contains("RBLCRD", true)
                || sender.contains("SPRCRD", true)
                || sender.contains("HSBCBK", true)
                || sender.contains("HSBCIN", true)
                || sender.contains("INDUSB", true))
    }


    private fun getInfoNumber(bodyMsg: String): String {
        var payTo = ""
        if (bodyMsg.contains("Info", true)) {
            when {
                bodyMsg.contains("Info:UPI-", true) -> {
                    var dataList = bodyMsg.split("UPI-")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    if (dataList.size > 2) {
                        var data = dataList[1]
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    } else {
                        var data = dataList[0]
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }

                bodyMsg.contains("Info:", true) -> {
                    var dataList = bodyMsg.split(":")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    if (dataList.size > 2) {
                        var data = dataList[1]
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    } else {
                        var data = dataList[0]
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }
            }

        } else if (bodyMsg.contains("IMPS", true)) {
            when {
                bodyMsg.contains("IMPS:", true) -> {
                    var dataList = bodyMsg.split(":")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    if (dataList.size > 2) {
                        var data = dataList[1]
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    } else {
                        var data = dataList[0]
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }
            }

        } else if (bodyMsg.contains("transfer", true)) {
            when {
                bodyMsg.contains("transfer", true) -> {
                    var dataList = bodyMsg.split(".")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    var data = dataList[0]
                    if (data.isNotEmpty()) {
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }
            }

        } else if (bodyMsg.contains("ATM", true)) {
            when {
                bodyMsg.contains("atm", true) -> {
                    var dataList = bodyMsg.split("ATM")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    var data = dataList[0]
                    if (data.isNotEmpty()) {
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[0]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[0]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }
            }

        } else if (bodyMsg.contains("Tos", true)) {
            when {
                bodyMsg.contains("toss", true) -> {
                    var dataList = bodyMsg.split("to")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    var data = dataList[1]
                    if (data.isNotEmpty()) {
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[1]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[1]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }
            }

        } else if (bodyMsg.contains("BIL", true)) {
            when {
                bodyMsg.contains("BIL", true) -> {
                    var dataList = bodyMsg.split("*")
                    //val p1 = Pattern.compile( "([A-Z]+).*")
                    var data = dataList[1]
                    if (data.isNotEmpty()) {
                        payTo = when {
                            data.contains(")", true) -> {
                                data.split("(")[1]
                            }
                            data.contains(".", true) -> {
                                data.split(".")[1]
                            }
                            else -> {
                                data
                            }
                        }
                    }
                }
            }

        }
        return payTo
    }


    private fun getRefNumber(body: String): String {
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


    private fun getPayeNameICICI(sender: String, message: String): String {
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
                } else if (message.contains("Info", true)) {
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


}*/
