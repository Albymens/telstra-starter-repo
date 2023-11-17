package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.model.SimCard;
import au.com.telstra.simcardactivator.repository.SimCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class SimCardServiceTest {
    @Mock
    RestTemplate restTemplate;
    @Mock
    SimCardRepository simCardRepo;

    SimCardService simCardService;
    SimCard simCard;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        simCardService = new SimCardService();
        simCardService.restTemplate = restTemplate;

        simCard = new SimCard();
        simCard.setId(1234);
        simCard.setActive(true);
        simCard.setCustomerEmail("agent@gmail.com");
    }

    @Test
    public void saveSimCard() {
        Map<String, Object> actuatorResponse = new HashMap<>();
        actuatorResponse.put("success", true);

        ResponseEntity<Map> response = new ResponseEntity<>(actuatorResponse, HttpStatus.OK);

       doReturn(response).when(restTemplate).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
               Mockito.any(HttpEntity.class), Mockito.<Class<Map>>any());
        simCardService.saveSimCard(simCard);
        assertNotNull(simCard);
        assertEquals("agent@gmail.com", simCard.getCustomerEmail());
        assertTrue(simCard.getActive());
    }

    @Test
    public void getCustomerSimCard() {
        Optional<SimCard> optionalSimCard = Optional.of(simCard);
        doReturn(optionalSimCard).when(simCardRepo).findById(Mockito.anyLong());
        when(simCardService.getCustomerSimCard(Mockito.anyLong())).thenReturn(simCard);
        assertNotNull(simCard);
        assertEquals("agent@gmail.com", simCard.getCustomerEmail() );
        assertTrue(simCard.getActive());
    }
}