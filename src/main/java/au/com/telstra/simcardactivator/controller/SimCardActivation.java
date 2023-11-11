package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.model.SimCard;
import au.com.telstra.simcardactivator.service.SimCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimCardActivation {
    @Autowired
    SimCardService simCardService;

    @PostMapping("/save-simCard")
    public SimCard createCustomerSimCard(@RequestBody SimCard simCard){
        return simCardService.saveSimCard(simCard);
    }

    @GetMapping("/get-simCard")
    public SimCard getCustomerSimCard(@RequestParam Long simCardId){
        return simCardService.getCustomerSimCard(simCardId);
    }
}
