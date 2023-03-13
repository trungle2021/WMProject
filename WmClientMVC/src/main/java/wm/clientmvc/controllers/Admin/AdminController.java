//package wm.clientmvc.controllers.Admin;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import wm.clientmvc.DTO.RegisterCustomerDTO;
//import wm.clientmvc.DTO.RegisterDTO;
//import wm.clientmvc.utils.SD_CLIENT;
//
//@Controller
//@RequestMapping("/staff/admin")
//public class AdminController {
//    @GetMapping("/addStaff")
//    public String addStaff(Model model){
//        RegisterDTO registerDTO = new RegisterDTO();
//        model.addAttribute("staff_roles", SD_CLIENT.STAFF_ROLES);
//        model.addAttribute("registerDTO", registerDTO);
//        return "adminTemplate/addStaff";
//    }
//}
