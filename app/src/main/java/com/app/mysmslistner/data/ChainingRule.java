package com.app.mysmslistner.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ChainingRule {
    @SerializedName("parent_match")
    @Expose
    public ParentMatch parentMatch;
    @SerializedName("parent_nomatch")
    @Expose
    public ParentNomatch parentNomatch;
    @SerializedName("parent_selection")
    @Expose
    public ArrayList<ParentSelection> parentSelection = null;

    public class ParentMatch {
        @SerializedName("child_override")
        @Expose
        public ArrayList<ChildOverride> childOverride = null;
        @SerializedName("parent_override")
        @Expose
        public ArrayList<ParentOverride> parentOverride = null;

        public ParentMatch() {
        }
    }

    public class ParentNomatch {
        @SerializedName("child_override")
        @Expose
        public ArrayList<ChildOverride> childOverride = null;

        public ParentNomatch() {
        }
    }

    public class ParentOverride {
        @SerializedName("deleted")
        @Expose
        public Boolean deleted;
        @SerializedName("incomplete")
        @Expose
        public Boolean incomplete;

        public ParentOverride() {
        }
    }

    public class ChildOverride {
        @SerializedName("child_field")
        @Expose
        public String childField;
        @SerializedName("deleted")
        @Expose
        public Boolean deleted;
        @SerializedName("incomplete")
        @Expose
        public Boolean incomplete;
        @SerializedName("parent_field")
        @Expose
        public String parentField;

        public ChildOverride() {
        }
    }

    public class ParentSelection {
        @SerializedName("child_field")
        @Expose
        public ChildField childField;
        @SerializedName("match_type")
        @Expose
        public String matchType;
        @SerializedName("match_value")
        @Expose
        public Long matchValue;
        @SerializedName("parent_field")
        @Expose
        public String parentField;

        public ParentSelection() {
        }
    }

    public class ChildField {
        @SerializedName("field")
        @Expose
        public String field;
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("value")
        @Expose
        public String value;

        public ChildField() {
        }
    }
}
