package com.springboot.wmproject.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class SD {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DOMAIN_APP = "http://localhost:8080/";

    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(),"");
    }
}
