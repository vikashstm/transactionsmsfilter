package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PosTypeRule {
    @SerializedName("group_id")
    @Expose
    public Integer groupId;
    @SerializedName("rules")
    @Expose
    public ArrayList<Rule> rules = null;

    public static class Rule {
        @SerializedName("category")
        @Expose
        public String category;
        @SerializedName("income_flag_override")
        @Expose
        public Boolean incomeFlagOverride;
        @SerializedName("value")
        @Expose
        public String value;
        public Pattern valuePattern;
    }
}
