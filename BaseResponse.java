package com.app.motiv.data.shared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 123 on 26-09-2017.
 */

@Getter
@Setter
public class BaseResponse {

    @Expose
    @SerializedName("return")
    public int _return;

    public String result;
    public String message;
}
