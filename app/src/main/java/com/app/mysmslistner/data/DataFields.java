package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class DataFields {
    @SerializedName("account_balance")
    @Expose
    public Amount accountBalance;
    @SerializedName("amount")
    @Expose
    public Amount amount;
    @SerializedName("chaining_rule")
    @Expose
    public ChainingRule chainingRule;
    @SerializedName("contact")
    @Expose
    public Contact contact;
    @SerializedName("create_txn")
    @Expose
    public CreateTxn createTxn;
    @SerializedName("currency")
    @Expose
    public Group currency;
    @SerializedName("date")
    @Expose
    public Date date;
    @SerializedName("deleted")
    @Expose
    public Boolean deleted;
    @SerializedName("enable_chaining")
    @Expose
    public Boolean enableChaining;
    @SerializedName("event_info")
    @Expose
    public Group eventInfo;
    @SerializedName("event_location")
    @Expose
    public Group eventLocation;
    @SerializedName("event_reminder_span")
    @Expose
    public EventReminderSpan eventReminderSpan;
    @SerializedName("event_type")
    @Expose
    public String eventType;
    @SerializedName("incomplete")
    @Expose
    public Boolean incomplete;
    @SerializedName("location")
    @Expose
    public Group location;
    @SerializedName("min_due_amount")
    @Expose
    public Amount minDueAmount;
    @SerializedName("name")
    @Expose
    public Group name;
    @SerializedName("note")
    @Expose
    public Note note;
    @SerializedName("otp")
    @Expose
    public Group otp;
    @SerializedName("outstanding_balance")
    @Expose
    public Amount outstandingBalance;
    @SerializedName("pan")
    @Expose
    public Pan pan;
    @SerializedName("pnr")
    @Expose
    public Group pnr;
    @SerializedName("pos")
    @Expose
    public Pos pos;
    @SerializedName("pos_type_rules")
    @Expose
    public PosTypeRule posTypeRule;
    @SerializedName("set_txn_as_expense")
    @Expose
    public Boolean setTxnAsExpense;
    @SerializedName("show_notification")
    @Expose
    public Boolean showNotification;
    @SerializedName("statement_type")
    @Expose
    public String statementType;
    @SerializedName("time")
    @Expose
    public Group time;
    @SerializedName("transaction_category")
    @Expose
    public String transactionCategory;
    @SerializedName("transaction_type")
    @Expose
    public String transactionType;
    @SerializedName("transaction_type_rule")
    @Expose
    public TransactionTypeRule transactionTypeRule;

    public static class Amount {
        @SerializedName("create_txn")
        @Expose
        public Boolean createTxn;
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("group_ids")
        @Expose
        public ArrayList<Integer> groupIds;
        @SerializedName("txn_direction")
        @Expose
        public String txnDirection;
        @SerializedName("value")
        @Expose
        public Long value;
    }

    public static class CreateTxn {
        @SerializedName("amount")
        @Expose
        public Amount amount;
    }

    public static class Date {
        @SerializedName("formats")
        @Expose
        public ArrayList<Format> formats;
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("group_ids")
        @Expose
        public ArrayList<Integer> groupIds;
        @SerializedName("use_sms_time")
        @Expose
        public Boolean useSmsTime;

        public static class Format {
            @SerializedName("format")
            @Expose
            public String format;
            @SerializedName("use_sms_time")
            @Expose
            public Boolean useSmsTime;
        }
    }

    public static class Pan {
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("value")
        @Expose
        public String value;
    }

    public static class Pos {
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("group_ids")
        @Expose
        public ArrayList<Integer> groupIds;
        @SerializedName("set_no_pos")
        @Expose
        public Boolean setNoPos;
        @SerializedName("value")
        @Expose
        public String value;
    }

    public class Group {
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("group_ids")
        @Expose
        public ArrayList<Integer> groupIds;
        @SerializedName("value")
        @Expose
        public String value;

        public Group() {
        }

        public String toString() {
            return "Group{groupId=" + this.groupId + ", groupIds=" + this.groupIds + ", value='" + this.value + '\'' + '}';
        }
    }

    public class Note {
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("prefix")
        @Expose
        public String prefix;
        @SerializedName("value")
        @Expose
        public String value;

        public Note() {
        }
    }

    public class EventReminderSpan {
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("group_ids")
        @Expose
        public ArrayList<Integer> groupIds;
        @SerializedName("value")
        @Expose
        public Long value;

        public EventReminderSpan() {
        }

        public String toString() {
            return "Group{groupId=" + this.groupId + ", groupIds=" + this.groupIds + ", value='" + this.value + '\'' + '}';
        }
    }

    public class Contact {
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("group_ids")
        @Expose
        public ArrayList<Integer> groupIds;
        @SerializedName("prefix")
        @Expose
        public String prefix;

        public Contact() {
        }
    }

    public String toString() {
        return "DataFields{transactionType='" + this.transactionType + '\'' + ", statementType='" + this.statementType + '\'' + ", eventType='" + this.eventType + '\'' + ", deleted=" + this.deleted + ", incomplete=" + this.incomplete + ", enableChaining=" + this.enableChaining + ", pan=" + this.pan + ", pos=" + this.pos + ", amount=" + this.amount + ", date=" + this.date + ", note=" + this.note + ", currency=" + this.currency + ", minDueAmount=" + this.minDueAmount + ", location=" + this.location + ", name=" + this.name + ", pnr=" + this.pnr + ", eventInfo=" + this.eventInfo + ", time=" + this.time + ", contact=" + this.contact + ", eventLocation=" + this.eventLocation + ", eventReminderSpan=" + this.eventReminderSpan + ", accountBalance=" + this.accountBalance + ", outstandingBalance=" + this.outstandingBalance + ", createTxn=" + this.createTxn + ", transactionTypeRule=" + this.transactionTypeRule + '}';
    }
}
