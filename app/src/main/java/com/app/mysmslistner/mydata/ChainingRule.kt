package com.app.mysmslistner.mydata


import com.google.gson.annotations.SerializedName

data class ChainingRule(
    @SerializedName("parent_match")
    var parentMatch: ParentMatch = ParentMatch(),
    @SerializedName("parent_nomatch")
    var parentNomatch: ParentNomatch = ParentNomatch(),
    @SerializedName("parent_selection")
    var parentSelection: List<ParentSelection> = listOf()
)