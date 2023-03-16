//package wm.clientmvc.securities.config;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.web.session.SessionInformationExpiredEvent;
//import org.springframework.security.web.session.SessionInformationExpiredStrategy;
//
//import java.io.IOException;
//
//public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
//
//    private final String loginUrl;
//
//    public CustomSessionInformationExpiredStrategy(String loginUrl) {
//        this.loginUrl = loginUrl;
//    }
//
//    @Override
//    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
//        HttpServletResponse response = event.getResponse();
//        response.sendRedirect(loginUrl);
//    }
//}