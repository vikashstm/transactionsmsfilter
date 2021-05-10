package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class TransactionTypeRule {
    @SerializedName("group_id")
    @Expose
    public Integer groupId;
    @SerializedName("rules")
    @Expose
    public ArrayList<Rule> rules = null;

    public static class Rule {
        @SerializedName("acc_type_override")
        @Expose
        public String accTypeOverride;
        @SerializedName("pos_override")
        @Expose
        public String posOverride;
        @SerializedName("set_no_pos")
        @Expose
        public Boolean setNoPos;
        @SerializedName("txn_type")
        @Expose
        public String txnType;
        @SerializedName("value")
        @Expose
        public String value;
    }
}
