package com.app.motiv.data.shared;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 123 on 26-09-2017.
 */
@Getter
@Setter
public class DataResponse<TModel> extends BaseResponse{

    public TModel data;

}
