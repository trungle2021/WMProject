package com.springboot.wmproject.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class SD {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DOMAIN_APP = "http://localhost:8080/";
    public static final String DOMAIN_APP_CLIENT = "http://localhost:9999/";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SALE = "ROLE_SALE";
    public static final String ROLE_ORGANIZE = "ROLE_ORGANIZE";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String TEAM_ADMINISTRATOR = "TEAM_ADMINISTRATOR";

    public  static String orderStatusOrdered ="ordered";
    public static String orderStatusDeposited="deposited";
    public  static String orderStatusCancel="canceling";
    public static String orderStatusCanceled="canceled";
    public static String orderStatusWarning="warning";
    public static String orderStatusConfirm="confirm";
    public static String orderStatusRefund="refunded";

}
