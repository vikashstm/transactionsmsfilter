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
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
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
        if (listItem.isNotEmpty()) {
            listItem.clear()
        }

        permission()
    }

    fun linearSearch(list: List<Any>, key: Any): Int? {
        for ((index, value) in list.withIndex()) {
            if (value == key) {
                return index
            }
        }
        return null
    }


    private fun allAreSame(stringList: ArrayList<Sm>, compareTo: String): Boolean {
        for (s in stringList) {
            if (!s.refNumber.contains(compareTo, true)) return false
        }
        return true
    }

    // Function to remove duplicates from an ArrayList
    fun <Sm> removeDuplicates(list: ArrayList<Sm>): ArrayList<Sm> {
        // Create a new ArrayList
        val newList = ArrayList<Sm>()
        // Traverse through the first list
        for (element in list) {
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element)
            }
        }

        // return the new list
        return newList
    }

    private fun permission() {
        val permissions = arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
        Permissions.check(
            this /*context*/,
            permissions,
            null /*rationale*/,
            null /*options*/,
            object : PermissionHandler() {
                override fun onGranted() {
                    // do your task.
                    var v = getAllSms()
                    if (!v.isNullOrEmpty()) {
                        var parseData = parsevalues(v[0].sms as ArrayList<Sm>)

                        if (!parseData.isNullOrEmpty()) {
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
                                    if (parseData[i].refNumber.trim().isEmpty()) {
                                        parseData[i].refNumber = parseData[i].transactionType
                                    }
                                // if transaction type is balance then avlbal is amount
                                if (parseData[i].transactionType.trim().contains("balance", true)) {
                                    parseData[i].avlBal = parseData[i].amount
                                    // as per ayushi she want amount is empty for balance scenario
                                    parseData[i].amount = "N/A"
                                    parseData[i].refNumber = parseData[i].transactionType
                                }
                                // check prev result is available or not
                                if (!listItem.contains(parseData[i].refNumber))
                                    listItem.add(
                                        "\n \n FULL BODY -> " + parseData[i].body +
                                                " \n \nSender Type -> " + parseData[i].sender + " \n Type Card ->" + parseData[i].cardType
                                                + "\n Transaction Type-> " + parseData[i].transactionType + "\n Amount ->" + parseData[i].amount + "\n Account number ->  "
                                                + parseData[i].accountNumber +
                                                "\n INFO :-" + parseData[i].refNumber +
                                                "\n BALANCE:-" + parseData[i].avlBal
                                                + "\n DATE:- " + millisToDate(parseData[i].date.toLong())
                                                + "\n \n ***************************"
                                    )
                            }

                            val adapters: ArrayAdapter<String> = ArrayAdapter<String>(
                                this@MainActivity,
                                android.R.layout.simple_list_item_1, android.R.id.text1, listItem
                            )

                            listView?.adapter = adapters
                            Log.e("LIST SIZE" + "onGranted: ", listItem.size.toString())
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


    private fun getRefComanNumber(body: String): String {
        //Info, At, Linked to, NEFT, Ref, transfer from, transfer to, for, of, IMPS
        //Till dot Space, on, has
        var refNumber = ""
        if (body.contains("NetBanking", true)) {
            // refNumber = " NetBanking"
            if (body.toLowerCase().contains(" to ", true)) {
                var dataList = body.toLowerCase().split(" to ")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.contains(". ", true) -> {
                        data.split(". ")[0]
                    }
                    data.contains(" on ", true) -> {
                        data.split(" on ")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }

                    else -> {
                        //data
                        "NetBanking"
                    }
                }
            }else if (body.toLowerCase().contains(" for ", true)) {
                var dataList = body.toLowerCase().split(" for ")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.contains(". ", true) -> {
                        data.split(". ")[0]
                    }
                    data.contains(" on ", true) -> {
                        data.split(" on ")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }

                    else -> {
                        //data
                        "NetBanking"
                    }
                }
            }

        } else if (body.contains("Cash Deposit", true)) {
            refNumber = "Cash Deposit"
        } else if (body.contains("withdrawn", true)) {
            var dataList = body.toLowerCase().split(" at ")
            var data = ""
            data = if (dataList.size > 1) {
                dataList[1]
            } else {
                dataList[0]
            }
            refNumber = "withdrawn at " + when {
                data.contains("on", true) -> {
                    data.split(" on")[0]
                }
                data.contains(".", true) -> {
                    data.split(". ")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }
        } else if (body.contains("towards", true)) {
            var dataList = body.toLowerCase().split("towards ")
            var data = ""
            data = if (dataList.size > 1) {
                dataList[1]
            } else {
                dataList[0]
            }
            refNumber = when {
                data.contains(" avl ", true) -> {
                    data.split(" avl ")[0]
                }
                data.contains(". ", true) -> {
                    data.split(". ")[0]
                }
                data.contains("on", true) -> {
                    data.split(" on")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }

        } else if (body.contains("thru", true)) {
            if (!body.contains("thru clg", true)) {
                var dataList = body.toLowerCase().split("thru ")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.contains(". ", true) -> {
                        data.split(".")[0]
                    }
                    data.contains("on", true) -> {
                        data.split(" on")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    else -> {
                        data
                    }
                }
            }

        } else if (body.contains("Credit card ending", true)) {
            if (body.contains("has been", true) && body.contains("from", true)) {
                var dataList = body.toLowerCase().split("from ")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.contains(" on", true) -> {
                        data.split(" on")[0]
                    }
                    data.contains(". ", true) -> {
                        data.split(". ")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    else -> {
                        data
                    }
                }
            } else if (body.contains("has been", true)) {
                var dataList = body.toLowerCase().split("has been ")
                var data = ""
                data = if (dataList.size > 1) {
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
                    data.contains(". ", true) -> {
                        data.split(".")[0]
                    }
                    else -> {
                        data
                    }
                }
            } else {
                var dataList = body.toLowerCase().split("from ")
                var data = ""
                data = if (dataList.size > 1) {
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
                    data.contains(". ", true) -> {
                        data.split(".")[0]
                    }
                    else -> {
                        data
                    }
                }
            }

        } else if (body.contains("NEFT", true)) {
            var dataList = body.toLowerCase().split("neft")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size > 1) {
                data = dataList[1].trim()
            } else {
                data = dataList[0].trim()
            }
            refNumber = "NEFT " + when {
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                data.contains(".", true) -> {
                    data.split(". ")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                else -> {
                    data
                }
            }
        } else if (body.contains("IMPS", true)) {
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
                        refNumber = "IMPS Ref no" + data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = "IMPS Ref no" + data.split(".")[0]
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
                        refNumber = "IMPS " + data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = "IMPS " + data.split(". ")[0]
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
                    refNumber = "RefNo " + data.split(")")[0]
                }
                data.contains(".", true) -> {
                    refNumber = "RefNo " + data.split(". ")[0]
                }
                data.contains("on", true) -> {
                    refNumber = "RefNo " + data.toLowerCase().split(" on")[0]
                }
                data.contains("has", true) -> {
                    refNumber = "RefNo " + data.toLowerCase().split(" has")[0]
                }
                else -> {
                    refNumber = data
                }
            }

        } else if (body.contains("Ref no", true)) {
            if (body.contains("VPA", true)) {
                var dataList = body.toLowerCase().split("vpa ")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains(".", true) -> {
                        refNumber = "VPA " + data.split(". ")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = "VPA " + data.split(")")[0]
                    }
                    data.contains("on", true) -> {
                        refNumber = "VPA " + data.split(" on")[0]
                    }
                    data.contains("has", true) -> {
                        refNumber = "VPA " + data.split(" has")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            } else {
                var dataList = body.toLowerCase().split("ref no")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains(")", true) -> {
                        refNumber = "Ref no" + data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = "Ref no" + data.split(". ")[0]
                    }
                    data.contains("on", true) -> {
                        refNumber = "Ref no" + data.split(" on")[0]
                    }
                    data.contains("has", true) -> {
                        refNumber = "Ref no" + data.split(" has")[0]
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
                    refNumber = "Ref no" + data.split(" on")[0]
                }
                data.decapitalize().contains("has", true) -> {
                    refNumber = "Ref no" + data.split(" has")[0]
                }
                data.contains(")", true) -> {
                    refNumber = "Ref no" + data.split(")")[0]
                }
                data.contains(".", true) -> {
                    refNumber = "Ref no" + data.split(".")[0]
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
                refNumber = "VIA " + when {
                    data.decapitalize().contains("on", true) -> {
                        data.split(" on")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    data.contains(". ", true) -> {
                        data.split(".")[0]
                    }
                    else -> {
                        data
                    }
                }

            } else if (body.contains("has been", true)) {
                var dataList = body.split("has ")
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
                    data.contains(". ", true) -> {
                        data.split(".")[0]
                    }
                    else -> {
                        data
                    }
                }
            }

        } else if (body.contains("ATM", true)) {
            if (body.contains("txn#", true)) {
                var dataList = body.split("ATM")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
                    if (dataList.size > 2) {
                        data = dataList[2]
                    } else {
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
                        refNumber = data.split(". ")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            } else if (body.contains("tx", true)) {
                var dataList = body.split("tx#")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains("fm ", true) -> {
                        refNumber = "ATM " + data.split("fm ")[0]
                    }
                    data.contains("for", true) -> {
                        refNumber = "ATM " + data.split("for ")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = "ATM " + data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = "ATM " + data.split(". ")[0]
                    }
                    data.contains("has", true) -> {
                        refNumber = "ATM " + data.split(" has")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            } else if (body.contains("withdrawn", true)) {
                var dataList = body.toLowerCase().split("at ")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
                    if (dataList.size > 2) {
                        data = dataList[2]
                    } else {
                        data = dataList[1]
                    }
                } else {
                    data = dataList[0]
                }
                when {
                    data.toLowerCase().contains("on", true) -> {
                        refNumber = data.split(" on")[0]
                    }
                    data.toLowerCase().contains("has", true) -> {
                        refNumber = data.split(" has")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(". ")[0]
                    }
                    else -> {
                        refNumber = "ATM " + data
                    }
                }
            } else if (body.contains("tx", true)) {
                var dataList = body.split("tx#")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains("fm ", true) -> {
                        refNumber = "ATM " + data.split("fm ")[0]
                    }
                    data.contains("for", true) -> {
                        refNumber = "ATM " + data.split("for ")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = "ATM " + data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = "ATM " + data.split(". ")[0]
                    }
                    data.contains("has", true) -> {
                        refNumber = "ATM " + data.split(" has")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            } else if (body.contains("has been", true)) {
                var dataList = body.toLowerCase().split("by")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1].trim()
                } else {
                    dataList[0]
                }
                refNumber = when {
                    data.contains(" on", true) -> {
                        data.split(" on")[0]
                    }
                    data.contains(". ", true) -> {
                        data.split(". ")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    else -> {
                        data
                    }
                }
            }

        } else if (body.contains("by transfer", true)) {
            if (body.contains("Deposit by", true)) {
                var dataList = body.toLowerCase().split("deposit by ")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                refNumber =  when {
                    data.contains(" avl ", true) -> {
                        data.split(" avl ")[0]
                    }
                    data.contains(".", true) -> {
                        data.split(".")[0]
                    }
                    data.contains("-", true) -> {
                        data.split("-")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    else -> {
                        data
                    }
                }
            }else {
                refNumber = "Transfer"
            }
        } else if (body.contains("for UPI", true)) {
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
                    refNumber = data.split(". ")[0]
                }
                else -> {
                    refNumber = data
                }
            }
        } else if (body.contains("Credit Card", true)) {
            if (body.contains("Credit card ending", true)) {
                var dataList = body.toLowerCase().split("from ")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size > 1) {
                    data = dataList[1].trim()
                } else {
                    data = dataList[0]
                }
                when {
                    data.contains("on", true) -> {
                        refNumber = getFirstWord(data.split(" on")[0])
                    }
                    data.contains(")", true) -> {
                        refNumber = getFirstWord(data.split(")")[0])
                    }
                    data.contains(".", true) -> {
                        refNumber = getFirstWord(data.split(". ")[0])
                    }
                    else -> {
                        refNumber = getFirstWord(data)
                    }
                }

            } else if (body.contains("form", true)) {
                var dataList = body.toLowerCase().split("from ")
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
                        refNumber = data.split(". ")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            } else if (body.contains("spent", true)) {
                var dataList = body.toLowerCase().split("at ")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains(" on ", true) -> {
                        refNumber = data.split(" on ")[0]
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
            } else {
                var dataList = body.toLowerCase().split("at")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                data = if (dataList.size > 1) {
                    dataList[1]
                } else {
                    dataList[0]
                }
                when {
                    data.contains(" on ", true) -> {
                        refNumber = data.split(" on ")[0]
                    }
                    data.contains(")", true) -> {
                        refNumber = data.split(")")[0]
                    }
                    data.contains(".", true) -> {
                        refNumber = data.split(". ")[0]
                    }
                    else -> {
                        refNumber = data
                    }
                }
            }

        } else if (body.contains("payment", true) && !body.contains("spent", true)) {
            var dataList = body.toLowerCase().split("for")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            data = if (dataList.size > 1) {
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
        } else if (body.contains("spent", true)) {
            var dataList = body.toLowerCase().split(" at ")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            data = if (dataList.size > 1) {
                dataList[1].trim()
            } else {
                dataList[0]
            }
            when {
                data.contains(" on ", true) -> {
                    refNumber = data.split(" on ")[0]
                }
                data.contains(".", true) -> {
                    refNumber = data.split(". ")[0]
                }
                data.contains(")", true) -> {
                    refNumber = data.split(")")[0]
                }
                else -> {
                    refNumber = data
                }
            }
        } else if (body.contains("cheque Number", true)
            || body.contains("cheque No", true)
        ) {
            if (body.contains("cheque No", true)) {
                var dataList = body.toLowerCase().split("cheque no ")
                //val p1 = Pattern.compile("([0-9]+).*")
                var data = ""
                if (dataList.size == 2) {
                    data = dataList[1]
                } else {
                    data = dataList[0]
                }
                var temp = when {
                    data.contains(".", true) -> {
                        data.split(".")[0]
                    }
                    data.contains("-", true) -> {
                        data.split("-")[0]
                    }
                    data.contains(")", true) -> {
                        data.split(")")[0]
                    }
                    else -> {
                        data
                    }
                }
               refNumber="Cheque No " +getFirstWord(temp.trim())
            } else if (body.contains("cheque Number", true)) {
                var dataList = body.toLowerCase().split("cheque number ")
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


        } else if (body.contains("credit for", true)) {
            var dataList = body.toLowerCase().split("credit for ")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size == 2) {
                data = dataList[1]
            } else {
                data = dataList[0]
            }
            refNumber = "Credit " + when {
                data.contains(" of ", true) -> {
                    data.split(" of ")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }
        }else if (body.contains("Deposit by ", true)) {
            var dataList = body.toLowerCase().split("Deposit by ")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size >1) {
                data = dataList[1].trim()
            } else {
                data = dataList[0]
            }
            refNumber =  when {
                data.contains(" avl ", true) -> {
                    data.split(" of ")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }
        }else if (body.contains("ref", true)) {
            var dataList = body.toLowerCase().split("ref")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            data = if (dataList.size > 1) {
                dataList[1].trim()
            } else {
                dataList[0]
            }

            refNumber="Ref "+getFirstWord(data)
        }else if (body.contains("cheque of", true)) {
            var dataList = body.toLowerCase().split("cheque of ")

            refNumber="Cheque"
        }else if (body.contains("UPI", true)) {
            var dataList = body.toLowerCase().split("upi")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size >1) {
                data = dataList[1].trim()
            } else {
                data = dataList[0]
            }
            refNumber ="UPI"+  when {
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }
        }else if (body.contains("Credited", true)) {
            var dataList = body.toLowerCase().split(" account of ")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size >1) {
                data = dataList[1].trim()
            } else {
                data = dataList[0]
            }
            refNumber ="Credited:"+  when {
                data.contains("a/c", true) -> {
                    data.split("a/c")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }
        }else if (body.contains("deducted", true)) {
            var dataList = body.toLowerCase().split(" for ")
            //val p1 = Pattern.compile("([0-9]+).*")
            var data = ""
            if (dataList.size >1) {
                data = dataList[1].trim()
            } else {
                data = dataList[0]
            }
            refNumber ="Credited:"+  when {
                data.contains("a/c", true) -> {
                    data.split("a/c")[0]
                }
                data.contains(".", true) -> {
                    data.split(".")[0]
                }
                data.contains("-", true) -> {
                    data.split("-")[0]
                }
                data.contains(")", true) -> {
                    data.split(")")[0]
                }
                else -> {
                    data
                }
            }
        }

        // var dataList = smsDto.body.split("Ref")

        return refNumber

    }

    @SuppressLint("LongLogTag")
    private fun getAvailableBalance(sm: Sm) {
        val smsDto: Sm = sm
        val regEx =
            Pattern.compile("(?i)(?:RS|INR|MRP)?(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)+")
        // Find instance of pattern matches
        if (smsDto.body.contains("curr o/s - ", true)) {
            var newBody = smsDto.body.split("o/s - ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("The Balance is", true)) {
            var newBody = smsDto.body.split("The Balance is ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("The Available Balance is", true)) {
            var newBody = smsDto.body.split("The Available Balance is ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("Avbl Lmt:", true)) {
            var newBody = smsDto.body.split("Avbl Lmt:")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("Avlbal", true)) {
            var newBody = smsDto.body.split("Avlbal")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("balance is", true)) {
            var newBody = smsDto.body.toLowerCase().split("balance is ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("AvBl Bal:", true)) {
            var newBody = smsDto.body.toLowerCase().split("avbl bal: ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("Avl. Bal:", true)) {
            var newBody = smsDto.body.toLowerCase().split("avl. bal:")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("AVl BAL", true)) {
            if (smsDto.body.contains("Avl. Bal:", true)) {
                var newBody = smsDto.body.toLowerCase().split("avl. bal:")
                val m = regEx.matcher(newBody[1].trim())
                if (m.find()) {
                    var amount = m.group(0).replace("inr".toRegex(), "")
                    amount = amount.replace("rs".toRegex(), "")
                    amount = amount.replace("inr".toRegex(), "")
                    amount = amount.replace(" ".toRegex(), "")
                    amount = amount.replace(",".toRegex(), "")
                    smsDto.avlBal = amount
                    Log.e("BALANCE", "getAvailableBalance: $amount")
                }
            } else {
                var newBody = smsDto.body.toLowerCase().split("avl bal ")
                val m = regEx.matcher(newBody[1].trim())
                if (m.find()) {
                    var amount = m.group(0).replace("inr".toRegex(), "")
                    amount = amount.replace("rs".toRegex(), "")
                    amount = amount.replace("inr".toRegex(), "")
                    amount = amount.replace(" ".toRegex(), "")
                    amount = amount.replace(",".toRegex(), "")
                    smsDto.avlBal = amount
                    Log.e("BALANCE", "getAvailableBalance: $amount")
                }
            }
        } else if (smsDto.body.contains("Avail Bal", true)) {
            var newBody = smsDto.body.toLowerCase().split("avail bal ")
            val m = regEx.matcher(newBody[1].trim().replace("\\s".toRegex(), ""))
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("The combine BAL is", true)) {
            var newBody = smsDto.body.toLowerCase().split("bal is ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = amount
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("The balance in", true)) {
            var newBody = smsDto.body.toLowerCase().split("balance in ")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                smsDto.avlBal = "N/A"
                smsDto.amount = amount
                smsDto.transactionType = "balance"
                // smsDto.refNumber="balance"
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else if (smsDto.body.contains("Available balance:", true)) {
            var newBody = smsDto.body.toLowerCase().split("available balance:")
            val m = regEx.matcher(newBody[1].trim())
            if (m.find()) {
                var amount = m.group(0).replace("inr".toRegex(), "")
                amount = amount.replace("rs".toRegex(), "")
                amount = amount.replace("inr".toRegex(), "")
                amount = amount.replace(" ".toRegex(), "")
                amount = amount.replace(",".toRegex(), "")
                if (smsDto.body.contains("credited", true)
                    || smsDto.body.contains("cash deposit", true)
                ) {
                    smsDto.transactionType = "credited"
                    smsDto.avlBal = amount
                    //smsDto.amount=amount
                } else if (smsDto.body.contains("debited", true)
                    || smsDto.body.contains("withdrawn", true)
                ) {
                    smsDto.transactionType = "debited"
                    smsDto.avlBal = amount
                    //smsDto.amount=amount
                } else {
                    smsDto.transactionType = "balance"
                    smsDto.avlBal = "N/A"
                    smsDto.amount = amount
                }
                // smsDto.refNumber="balance"
                Log.e("BALANCE", "getAvailableBalance: $amount")
            }
        } else {
            smsDto.avlBal = "N/A"
        }

    }

    private fun millisToDate(currentTime: Long): String? {
        val finalDate: String
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        val date = calendar.time
        finalDate = date.toString()
        return finalDate
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


    private fun parsevalues(body_val: ArrayList<Sm>): ArrayList<Sm>? {
        val resSms: ArrayList<Sm> = ArrayList()
        for (i in 0 until body_val.size) {
            val smsDto: Sm = body_val[i]
            Log.e("SMS****", smsDto.body)
            // findMerchantName(smsDto)
            val regEx =
                Pattern.compile("(?i)(?:RS|INR|MRP)?(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)+")
            // Find instance of pattern matches
            val m = regEx.matcher(smsDto.body)
            if (m.find()) {
                try {
                    if (checkSenderIsValid(smsDto.sender)) {
                        if (!smsDto.body.contains("stmt", true)) {
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
                                && !smsDto.body.contains("We are pleased to inform that", true)
                                && !smsDto.body.contains("has been opened", true)
                            ) {
                                //  Log.e("amount_value= ", "" + m.group(0))
                                var amount = m.group(0).replace("inr".toRegex(), "")
                                amount = amount.replace("rs".toRegex(), "")
                                amount = amount.replace("inr".toRegex(), "")
                                amount = amount.replace(" ".toRegex(), "")
                                amount = amount.replace(",".toRegex(), "")
                                smsDto.amount = amount
                                smsDto.transactionType = "debited"
                            } else if (smsDto.body.contains("credited", true)
                                || smsDto.body.contains("cr", true)
                                || smsDto.body.contains("deposited", true)
                                || smsDto.body.contains("deposit", true)
                                || smsDto.body.contains("received", true)
                                && !smsDto.body.contains("otp", true)
                                && !smsDto.body.contains("emi", true)
                            ) {
                                //  Log.e("amount_value= ", "" + m.group(0))
                                var amount = m.group(0).replace("inr".toRegex(), "")
                                amount = amount.replace("rs".toRegex(), "")
                                amount = amount.replace("inr".toRegex(), "")
                                amount = amount.replace(" ".toRegex(), "")
                                amount = amount.replace(",".toRegex(), "")
                                smsDto.amount = amount
                                when {
                                    smsDto.body.contains("UPDATE:AVAILABLE Bal in", true) -> {
                                        smsDto.transactionType = "balance"
                                        smsDto.avlBal = smsDto.amount
                                    }
                                    smsDto.body.contains("UPDATE: AVAILABLE Bal in", true) -> {
                                        smsDto.transactionType = "balance"
                                        smsDto.avlBal = smsDto.amount
                                    }
                                    else -> {
                                        smsDto.transactionType = "credited"
                                    }
                                }

                            }
                            smsDto.parsed = "1"
                            //   Log.e("matchedValue= ", "" + amount)
                            when {
                                smsDto.body.contains("SBIDrCARD", true)
                                        && smsDto.body.contains("tx#", true) -> {
                                    var dataList = smsDto.body.toLowerCase().split("sbidrcard ")
                                    // val p1 =
                                    //    Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                    //val m1 = p1.matcher(dataList[1])
                                    var data = ""
                                    if (dataList.size > 1) {
                                        data = dataList[1].trim()
                                    } else {
                                        data = dataList[0]
                                    }
                                    smsDto.accountNumber = getFirstWord(data)
                                }
                                smsDto.body.contains("Customer ID ", true) -> {
                                    var dataList = smsDto.body.toLowerCase().split("customer id ")
                                    // val p1 =
                                    //    Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                    //val m1 = p1.matcher(dataList[1])
                                    var data = ""
                                    if (dataList.size > 1) {
                                        data = dataList[1]
                                    } else {
                                        data = dataList[0]
                                    }
                                    smsDto.accountNumber = getFirstWord(data)
                                }
                                smsDto.body.contains("Deposit No ", true) -> {
                                    var dataList = smsDto.body.toLowerCase().split("deposit no ")
                                    // val p1 =
                                    //    Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                    //val m1 = p1.matcher(dataList[1])
                                    var data = ""
                                    if (dataList.size > 1) {
                                        data = dataList[1]
                                    } else {
                                        data = dataList[0]
                                    }
                                    smsDto.accountNumber = getFirstWord(data)
                                }
                                smsDto.body.contains("credit card ending", true) -> {
                                    var dataList = smsDto.body.split("ending ")
                                    // val p1 =
                                    //    Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                    //val m1 = p1.matcher(dataList[1])
                                    var data = ""
                                    if (dataList.size > 1) {
                                        data = dataList[1]
                                    } else {
                                        data = dataList[0]
                                    }
                                    smsDto.accountNumber = getFirstWord(data)
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
                                    if (smsDto.body.contains("no.", true)) {
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
                                    } else if (smsDto.body.contains("A/c No", true)) {
                                        if (smsDto.body.contains("XX", true)) {
                                            var dataList = smsDto.body.split("A/c No")
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
                                        } else {
                                            var dataList = smsDto.body.split("a/c no ")
                                            // val p1 =
                                            //    Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                            //val m1 = p1.matcher(dataList[1])
                                            var data = ""
                                            data = if (dataList.size > 1) {
                                                dataList[1]
                                            } else {
                                                dataList[0]
                                            }
                                            smsDto.accountNumber = data.toLowerCase().split("as")[0]
                                        }

                                    } else if (smsDto.body.contains("a/c no ", true)) {
                                        var dataList = smsDto.body.split("a/c no ")
                                        // val p1 =
                                        //    Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
                                        //val m1 = p1.matcher(dataList[1])
                                        var data = ""
                                        data = if (dataList.size > 1) {
                                            dataList[1]
                                        } else {
                                            dataList[0]
                                        }
                                        smsDto.accountNumber = data.toLowerCase().split("as")[0]

                                    } else if (smsDto.body.contains("a/c", true)) {
                                        if (!smsDto.body.contains("xx", true)
                                            && !smsDto.body.contains("x", true)
                                        ) {

                                            var dataList = smsDto.body.toLowerCase().split("a/c ")
                                            var data = ""
                                            data = if (dataList.size > 1) {
                                                dataList[1]
                                            } else {
                                                dataList[0]
                                            }
                                            if (data.contains("as")) {
                                                smsDto.accountNumber =
                                                    data.toLowerCase().split("as")[0]
                                            } else {
                                                smsDto.accountNumber = getFirstWord(
                                                    data.toLowerCase().split("\\s")[0]
                                                ).filter { it.isDigit() }
                                            }
                                            // smsDto.accountNumber = data
                                        } else {
                                            var dataList = smsDto.body.toLowerCase().split("a/c ")
                                            val p1 =
                                                Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                            smsDto.accountNumber = data
                                        }

                                    }
                                }

                                smsDto.body.contains("Acct", true) -> {
                                    var dataList = smsDto.body.split("Acct ")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                    smsDto.accountNumber = data
                                }
                                smsDto.body.contains("Card ending", true) -> {
                                    if (smsDto.body.contains("ending", true)) {
                                        if (smsDto.body.contains("XX", true)) {
                                            var dataList =
                                                smsDto.body.toLowerCase().split("ending ")
                                            val p1 =
                                                Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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
                                            smsDto.accountNumber = data
                                        } else {
                                            var dataList =
                                                smsDto.body.toLowerCase().split("ending ")
                                            var data = dataList[1].trim()
                                            smsDto.accountNumber = getFirstWord(data)
                                        }

                                    } else if (smsDto.body.contains("end", true)) {
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
                                    var dataList = smsDto.body.toLowerCase().split("account ")
                                    val p1 =
                                        Pattern.compile("[0-9]*[Xx\\*]*[0-9]*[Xx\\*]+[0-9]{3,}")
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

                                smsDto.body.contains("Ac", true) -> {
                                    var dataList = smsDto.body.toLowerCase().split("ac ")
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
                                && !smsDto.body.contains("minimum", true)
                                && !smsDto.body.contains("importance", true)
                                && !smsDto.body.contains("request", true)
                                && !smsDto.body.contains("limit", true)
                                && !smsDto.body.contains("convert", true)
                                && !smsDto.body.contains("emi", true)
                                && !smsDto.body.contains("avoid paying", true)
                                && !smsDto.body.contains("autopay", true)
                                && !smsDto.body.contains("E-statement", true)
                                && !smsDto.body.contains("funds are blocked", true)
                                && !smsDto.body.contains("SmartPay", true)
                                && !smsDto.body.contains("We are pleased to inform that", true)
                                && !smsDto.body.contains("has been opened", true)
                                && !smsDto.transactionType.trim().isNullOrEmpty()
                            ) {
                                // bank wise filter
                                getAvailableBalance(smsDto)
                                smsDto.refNumber = getRefComanNumber(smsDto.body)
                                var cardType =
                                    findCreditCardOrDebitCard(smsDto.body, smsDto.sender)
                                smsDto.cardType = cardType
                                resSms.add(smsDto)
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

    private fun getFirstWord(text: String): String {
        val index = text.indexOf(' ')
        return if (index > -1) { // Check if there is more than one word.
            text.substring(0, index).trim { it <= ' ' } // Extract first word.
        } else {
            text// Text is the first word itself.
        }
    }

    private fun fetchInbox(): ArrayList<LocalSmsGet>? {
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
            val date = cursor.getLong(2)
            val body = cursor.getString(3)
            list.add(Sm(body = body, sender = address, date = date.toString()))
        }
        sms.add(LocalSmsGet(list))
        cursor.close()
        return sms
    }

    @SuppressLint("NewApi", "LongLogTag")
    fun getAllSms(): ArrayList<LocalSmsGet>? {
        val sms = ArrayList<LocalSmsGet>()
        val cr = contentResolver
        val c: Cursor? = cr.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            /*arrayOf(Telephony.Sms.Inbox.BODY,Telephony.Sms.Inbox.ADDRESS)*/
            arrayOf(
                Telephony.Sms.Inbox.SUBSCRIPTION_ID,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.DATE,
                Telephony.Sms.Inbox.BODY
            ),  // Select body text
            null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        ) // Default
        // sort
        // order);
        val totalSMS: Int = c?.count!!
        if (c.moveToFirst()) {
            var list = ArrayList<Sm>()
            for (i in 0 until totalSMS) {
                val address = c.getString(1)
                val date = c.getLong(2)
                val body = c.getString(3)
                list.add(Sm(body = body, sender = address, date = date.toString()))
                // lstSms.add(c.getString(1)+"\n"+c.getString(0))
                c.moveToNext()
            }
            sms.add(LocalSmsGet(list))
        } else {
            throw RuntimeException("You have no SMS in Inbox")
        }
        c.close()
        return sms
    }


    private fun findCreditCardOrDebitCard(msg: String, sender: String): String {
        if (sender.trim().contains("+918586980859", true)
            || sender.contains("08586980869", true)
            || sender.contains("085869", true)
            || sender.contains("ICICIB", true)
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
        return (sender.trim().contains("+918586980859", true)
                || sender.contains("08586980869", true)
                || sender.contains("085869", true)
                || sender.contains("ICICIB", true)
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


}