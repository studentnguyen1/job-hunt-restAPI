package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("create new an subscriber")
    public ResponseEntity<Subscriber> createNewSubscriber(@Valid @RequestBody Subscriber newSub)
            throws IdInvalidException {
        boolean isEmailExist = this.subscriberService.isEmailExist(newSub.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + newSub.getEmail() + " đã tồn tại, vui lòng thêm email khác");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.handleCreatSubscriber(newSub));
    }

    @PutMapping("/subscribers")
    @ApiMessage("update an subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber newSub)
            throws IdInvalidException {
        boolean isIdExist = this.subscriberService.isIdExist(newSub.getId());
        if (!isIdExist) {
            throw new IdInvalidException("Id " + newSub.getId() + "không tồn tại, vui lòng nhập id khác");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.handleUpdateSubscriber(newSub));
    }

}
