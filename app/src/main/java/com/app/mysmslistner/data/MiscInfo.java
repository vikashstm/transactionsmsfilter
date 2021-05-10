package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class MiscInfo {
    @SerializedName("get_balance")
    @Expose
    public ArrayList<BalanceInfo> getBalance;

    public static class BalanceInfo {
        @SerializedName("account_type")
        @Expose
        public String accountType;
        @SerializedName("contact_info")
        @Expose
        public ArrayList<ContactInfo> contactInfos;

        public static class ContactInfo {
            @SerializedName("balance_refresh_text")
            @Expose
            public String balanceRefreshText;
            @SerializedName("format")
            @Expose
            public String format;
            @SerializedName("numbers")
            @Expose
            public ArrayList<String> numbers;
            @SerializedName("type")
            @Expose
            public String type;
        }
    }
}
