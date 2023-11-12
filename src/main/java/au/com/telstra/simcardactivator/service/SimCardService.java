package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.model.SimCard;
import au.com.telstra.simcardactivator.repository.SimCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimCardService {
    Logger logger = LoggerFactory.getLogger(SimCardService.class);
    @Autowired
    SimCardRepository simCardRepository;

   public SimCard saveSimCard(SimCard simCard){
       Map<String, String> customerSimCardRequestPayload = new HashMap<>();
       String iccid;
       if(null != simCard.getIccid())
           iccid = simCard.getIccid();
       else
           iccid = UUID.randomUUID().toString();
       customerSimCardRequestPayload.put("iccid", iccid);
       customerSimCardRequestPayload.put("customerEmail", simCard.getCustomerEmail());

       String url = "http://localhost:8444/actuate";

       RestTemplate restTemplate = new RestTemplate();
       HttpEntity entity = new HttpEntity<>(customerSimCardRequestPayload);
       try {
           ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

           if(response.getStatusCode().equals(HttpStatus.OK)){
               Map<String, Object> actuatorResponse = (Map<String, Object>) response.getBody();
              if(null != actuatorResponse && actuatorResponse.containsKey("success")){
                  simCard.setIccid(iccid);
                  simCard.setCustomerEmail(simCard.getCustomerEmail());
                  simCard.setActive((Boolean) actuatorResponse.get("success"));
                  simCardRepository.save(simCard);
                  logger.info("Sim Card saved Successfully");
              }
           }
       } catch (Exception ex){
           logger.error("Unable to reach Actuator {}", ex.getMessage());
       }
       return  simCardRepository.save(simCard);
    }

    public SimCard getCustomerSimCard(Long simCardId) {
       Optional<SimCard> getCustomerSimCard =  simCardRepository.findById(simCardId);
       return getCustomerSimCard.isPresent() ? getCustomerSimCard.get() : null;
    }

}
