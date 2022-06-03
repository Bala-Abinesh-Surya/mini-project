package com.surya.miniproject.utility;

import static com.surya.miniproject.constants.Strings.ALL_DEPARTMENTS;

import java.security.KeyStore;
import java.time.LocalDateTime;

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

    // method to return the data
    // in format, 02-06-2022
    public String date(){
        return LocalDateTime.now().getDayOfMonth() + "-"
                + LocalDateTime.now().getMonthValue() + "-"
                + LocalDateTime.now().getYear();
    }
}
