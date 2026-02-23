package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    // @Scheduled(cron = "*/60 * * * * *")
    // @Transactional
    public String getMethodName() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("nguyengoby@gmail.com", "Test Email", "<h1>
        // <b>Hello from Spring Boot</b></h1>",
        // false, true);
        System.out.println("Sending emails to subscribers...");
        this.subscriberService.sendSubscribersEmailJobs();
        return "Test";
    }

}
