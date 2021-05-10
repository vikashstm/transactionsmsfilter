package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Sender {
    @SerializedName("full_name")
    @Expose
    public String completeName;
    @SerializedName("misc_information")
    @Expose
    public MiscInfo miscInfo;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("patterns")
    @Expose
    public ArrayList<Rule> patterns;
    @SerializedName("sms_preprocessor")
    @Expose
    public PreProcessor preProcessor;
    @SerializedName("sender_UID")
    @Expose
    public String senderUID;
    @SerializedName("senders")
    @Expose
    public ArrayList<String> senders;
    @SerializedName("set_account_as_expense")
    @Expose
    public Boolean setAccountAsExpense;

    public static class PreProcessor {
        @SerializedName("by")
        @Expose

        /* renamed from: by */
        public String f677by;
        @SerializedName("replace")
        @Expose
        public String replace;
    }
}
