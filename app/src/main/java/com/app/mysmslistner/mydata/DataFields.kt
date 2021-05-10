package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class DataFields(
    @SerializedName("account_balance")
    var accountBalance: AccountBalance = AccountBalance(),
    @SerializedName("amount")
    var amount: Amount = Amount(),
    @SerializedName("chaining_rule")
    var chainingRule: ChainingRule = ChainingRule(),
    @SerializedName("contact")
    var contact: Contact = Contact(),
    @SerializedName("create_txn")
    var createTxn: CreateTxn = CreateTxn(),
    @SerializedName("currency")
    var currency: Currency = Currency(),
    @SerializedName("date")
    var date: Date = Date(),
    @SerializedName("deleted")
    var deleted: Boolean = false,
    @SerializedName("enable_chaining")
    var enableChaining: Boolean = false,
    @SerializedName("event_info")
    var eventInfo: EventInfo = EventInfo(),
    @SerializedName("event_location")
    var eventLocation: EventLocation = EventLocation(),
    @SerializedName("event_reminder_span")
    var eventReminderSpan: EventReminderSpan = EventReminderSpan(),
    @SerializedName("event_type")
    var eventType: String = "",
    @SerializedName("incomplete")
    var incomplete: Boolean = false,
    @SerializedName("location")
    var location: Location = Location(),
    @SerializedName("min_due_amount")
    var minDueAmount: MinDueAmount = MinDueAmount(),
    @SerializedName("name")
    var name: Name = Name(),
    @SerializedName("note")
    var note: Note = Note(),
    @SerializedName("otp")
    var otp: Otp = Otp(),
    @SerializedName("outstanding_balance")
    var outstandingBalance: OutstandingBalance = OutstandingBalance(),
    @SerializedName("pan")
    var pan: Pan = Pan(),
    @SerializedName("pnr")
    var pnr: Pnr = Pnr(),
    @SerializedName("pos")
    var pos: Pos = Pos(),
    @SerializedName("pos_type_rules")
    var posTypeRules: PosTypeRules = PosTypeRules(),
    @SerializedName("show_notification")
    var showNotification: Boolean = false,
    @SerializedName("statement_type")
    var statementType: String = "",
    @SerializedName("time")
    var time: Time = Time(),
    @SerializedName("transaction_category")
    var transactionCategory: String = "",
    @SerializedName("transaction_type")
    var transactionType: String = "",
    @SerializedName("transaction_type_rule")
    var transactionTypeRule: TransactionTypeRule = TransactionTypeRule()
)