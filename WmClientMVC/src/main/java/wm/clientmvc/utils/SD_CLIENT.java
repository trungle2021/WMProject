package wm.clientmvc.utils;

import java.util.ArrayList;
import java.util.List;

public class SD_CLIENT {
    public static List<String> ROLES = new ArrayList<>(){
        {
            add("ROLE_ADMIN");
            add("ROLE_EMPLOYEE");
        }
    };

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DOMAIN_APP_API = "http://localhost:8080/";
    public static final String DOMAIN_APP_CLIENT = "http://localhost:9999/";
}
