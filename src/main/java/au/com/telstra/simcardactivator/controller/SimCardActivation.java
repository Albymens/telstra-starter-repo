package au.com.telstra.simcardactivator.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
public class SimCardActivation {

    @PostMapping("/create-sim-card")
    public void createCustomerSimCard(@RequestBody Map<String, String> request){

        Map<String, String> customerSimCardRequestPayload = new HashMap<>();
        customerSimCardRequestPayload.put("iccid", UUID.randomUUID().toString());
        customerSimCardRequestPayload.put("customerEmail", request.get("customerEmail"));

        String url = "http://localhost:8444/actuate";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity<>(customerSimCardRequestPayload);
        try {
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if(response.getStatusCode().equals(HttpStatus.OK)){
                Map<String, Object> actuatorResponse = (Map<String, Object>) response.getBody();
                if(null != actuatorResponse){
                    if(Objects.equals(Boolean.TRUE, actuatorResponse.get("success")))
                        System.out.println("Sim Card Activated Successfully");
                    else
                        System.out.println("Unable to Activate Sim Card. Please Contact Support");
                }
            }
        } catch (HttpClientErrorException ex){
            System.out.println("Error " + ex.getMessage());
        }
    }
}
