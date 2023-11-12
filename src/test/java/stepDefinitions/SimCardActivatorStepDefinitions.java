package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity response;

    private static final Logger logger = LoggerFactory.getLogger(SimCardActivatorStepDefinitions.class);

    @When("I send a request the response status code should be {int}")
    public void activateSimCard(int statusCode){
        Map<String, String> sampleRequest = new HashMap<>();
        sampleRequest.put("iccid", "1255789453849037777");
        sampleRequest.put("customerEmail", "peter@gmail.com");
        try {
            response = restTemplate.postForEntity("http://localhost:8045/save-simCard", sampleRequest, Map.class);
            assertEquals(statusCode, response.getStatusCodeValue());
        } catch (Exception e){
            logger.error("Remote Server Error " + e.getMessage());
        }
    }

    @When("I send a get request to get SimCard the response status code should be {int}")
    public void getSimCard(int statusCode){
        try {
            response = restTemplate.getForEntity("http://localhost:8045/get-simCard?simCardId=1", Map.class);
            assertEquals(statusCode, response.getStatusCodeValue());
        } catch (Exception ex){
            logger.error("Remote Server Error " + ex.getMessage());
        }
    }

    @Then("the Sim Card status should be active")
    public void simCardShouldBeActive(){
        Map<String, Object> responseDetails = (Map<String, Object>) response.getBody();
        if(null != responseDetails)
            assertTrue((Boolean) responseDetails.get("active"));
        else
            logger.error("Unable to retrieve Sim Card Details");
    }

    @When("I send a request for Sim Card Activation")
    public void simCardActivation(){
        Map<String, String> sampleRequest = new HashMap<>();
        sampleRequest.put("iccid", "8944500102198304826");
        sampleRequest.put("customerEmail", "alby@gmail.com");
        try {
            response = restTemplate.postForEntity("http://localhost:8045/save-simCard", sampleRequest, Map.class);
        } catch (Exception e){
            logger.error("Remote Server Error " + e.getMessage());
        }
    }

    @When("I send a get request to get Failed SimCard the response status code should be {int}")
    public void getFailedSimCard(int statusCode){
        try {
            response = restTemplate.getForEntity("http://localhost:8045/get-simCard?simCardId=2", Map.class);
            assertEquals(statusCode, response.getStatusCodeValue());
        } catch (Exception ex){
            logger.error("Remote Server Error " + ex.getMessage());
        }
    }

    @Then("the Sim Card should not be active")
    public void simCardShouldNotBeActive(){
        Map<String, Object> responseDetails = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseDetails.get("active"));
    }

}