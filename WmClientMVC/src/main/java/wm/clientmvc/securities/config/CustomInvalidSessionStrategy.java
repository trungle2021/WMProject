//package wm.clientmvc.securities.config;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.web.session.InvalidSessionStrategy;
//
//import java.io.IOException;
//
//public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {
//
//    private final String loginUrl;
//
//    public CustomInvalidSessionStrategy(String loginUrl) {
//        this.loginUrl = loginUrl;
//    }
//
//    @Override
//    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        response.sendRedirect(loginUrl);
//    }
//}
//
//
