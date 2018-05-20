package com.junburg.moon.rockbottom.util;

/**
 * Created by Junburg on 2018. 5. 2..
 */

/**
 * 해당 데이터의 존재 유무를 boolean 값으로 반환
 */
public interface DataExistCallback {
    void onDataExistCheck(boolean check);

}