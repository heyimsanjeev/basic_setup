package com.app.motiv.ui.base;

/**
 * Created by 123 on 04-Jan-18.
 */

public interface BasePresentor<T> {

    void attachView(T view);

    void dropView();
}
