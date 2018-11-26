package com.app.motiv.data.shared;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 123 on 03-10-2017.
 */
@Getter
@Setter
public class PageResponse<M> extends BaseResponse{

    public List<M> data;
}
