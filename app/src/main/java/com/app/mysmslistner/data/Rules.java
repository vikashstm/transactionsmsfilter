package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Rules {
    @SerializedName("blacklist_regex")
    @Expose
    public String blacklistRegex;
    @SerializedName("min_app_version")
    @Expose
    public String minAppVersion;
    @SerializedName("rules")
    @Expose
    public ArrayList<Sender> rules;
    @SerializedName("version")
    @Expose
    public String version;

    public String toString() {
        return "Rules{blacklistRegex='" + this.blacklistRegex + '\'' + ", minAppVersion='" + this.minAppVersion + '\'' + ", version='" + this.version + '\'' + ", rules=" + this.rules + '}';
    }
}
