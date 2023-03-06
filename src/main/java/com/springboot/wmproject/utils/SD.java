package com.springboot.wmproject.utils;

import java.util.ArrayList;
import java.util.List;

public class SD {
    public static List<String> ROLES = new ArrayList<>(){
        {
            add("ROLE_ADMIN");
            add("ROLE_EMPLOYEE");
        }
    };

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DOMAIN_APP = "http://localhost:8080/";
}
