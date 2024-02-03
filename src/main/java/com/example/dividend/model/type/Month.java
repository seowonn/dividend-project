package com.example.dividend.model.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Month {

    JAN("Jan", 1),
    FEB("Feb", 2),
    MAR("Mar", 3),
    APR("Apr", 4),
    MAY("May", 5),
    JUN("Jun", 6),
    JUL("Jul", 7),
    AUG("Aug", 8),
    SEP("Sep", 9),
    OCT("Oct", 10),
    NOV("Nov", 11),
    DEC("Dec", 12);

    private final String strMonth;
    private final int number;

    public static int strToNumber(String str){
        for (var month : Month.values()){
            if(month.strMonth.equals(str)){
                return month.number;
            }
        }
        return -1;
    }
}
