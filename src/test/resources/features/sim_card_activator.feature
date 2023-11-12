Feature: Sim Card Activation

  Scenario: Successful Sim Card Activation
    When I send a request the response status code should be 200
    When I send a get request to get SimCard the response status code should be 200
    Then the Sim Card status should be active

  Scenario: Failed Sim Card Activation
    When I send a request for Sim Card Activation
    When I send a get request to get Failed SimCard the response status code should be 200
    Then the Sim Card should not be active
