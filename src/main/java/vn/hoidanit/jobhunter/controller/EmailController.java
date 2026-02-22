package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String getMethodName() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("nguyengoby@gmail.com", "Test Email", "<h1>
        // <b>Hello from Spring Boot</b></h1>",
        // false, true);
        this.emailService.sendEmailFromTemplateSync("nguyengoby@gmail.com", "Test Email Template", "job");
        return "Test";
    }

}
