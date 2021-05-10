package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.regex.Pattern;

public class Rule {
    private static final String TAG = Rule.class.getSimpleName();
    @SerializedName("account_name_override")
    @Expose
    public String accOverrideName;
    @SerializedName("account_type")
    @Expose
    public String accountType;
    @Expose(serialize = false)
    public String completeName;
    @SerializedName("data_fields")
    @Expose
    public DataFields dataFields;
    @SerializedName("set_account_as_expense")
    @Expose
    public Boolean isExpenseAcc;
    @Expose(serialize = false)
    public String name;
    @SerializedName("obsolete")
    @Expose
    public Boolean obsolete;
    @Expose(serialize = false)
    private Pattern pattern;
    @SerializedName("pattern_UID")
    @Expose
    public String patternUID;
    @Expose(serialize = false)
    public Sender.PreProcessor preProcessor;
    @SerializedName("regex")
    @Expose
    private String regex;
    @SerializedName("reparse")
    @Expose
    public Boolean reparse;
    @Expose(serialize = false)
    public Long senderUID;
    @SerializedName("sms_type")
    @Expose
    public String smsType;
    @SerializedName("sort_UID")
    @Expose
    public String sortUID;

    public void setRegex(String str) {
        if (str.contains("\\\\")) {
            this.regex = str.replaceAll("\\\\", "\\");
        } else {
            this.regex = str;
        }
    }

    public String getRegex() {
        return this.regex;
    }

    public Pattern getPattern() {
        if (this.pattern == null) {
            this.pattern = Pattern.compile(this.regex);
        }
        return this.pattern;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public long getSortUID() {
        return Long.parseLong(this.sortUID);
    }

    public void setSenderUID(String str) {
        this.senderUID = Long.valueOf(Long.parseLong(str));
    }

    public long getPatternUID() {
        return Long.parseLong(this.patternUID);
    }
}
