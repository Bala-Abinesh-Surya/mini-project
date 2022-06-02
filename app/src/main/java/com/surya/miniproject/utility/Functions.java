package com.surya.miniproject.utility;

import static com.surya.miniproject.constants.Strings.ALL_DEPARTMENTS;

public class Functions {
    // class contains all the commonly used functions throughout the application

    // method to return the department
    public String department(String department){
        for(String x : ALL_DEPARTMENTS){
            if(x.contains(department)){
                return x;
            }
        }

        // unreachable statement
        return null;
    }
}
