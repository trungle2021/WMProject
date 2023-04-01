package wm.clientmvc.controllers.Customer;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/contact")
public class ContactController {
    private JavaMailSender javaMailSender;
    @Autowired
    public ContactController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to,String from ,String subject, String content) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject+" From: "+from);
        helper.setText(content, true); // true indicates that the content is HTML
        javaMailSender.send(message);
    }
    @PostMapping("")
    public String sendContact(@RequestParam("email")String email,@RequestParam("subject")String subject,@RequestParam("message")String message) throws MessagingException {
        sendEmail("ktkstore2022@gmail.com",email,subject,message);
        return "redirect:/home";
    }

}
