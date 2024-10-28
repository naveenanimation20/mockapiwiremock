package org.mock.api;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import java.util.concurrent.ThreadLocalRandom;


public class APIMocks {

    public static void getDummyUser(){
        //create stub for get user:
        stubFor(get(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "\"name\": \"Naveen\"\n" +
                                "}")));
    }
    
    
    public static void getDummyUserWithFile() {
        stubFor(get(urlEqualTo("/api/users/2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("user.json")));
    }
    


    public static void getDummyUserWithQueryParams(){
        //create stub for get user:
        stubFor(get(urlPathEqualTo("/api/users"))
                .withQueryParam("param", equalTo("value"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\": \"user is searched\"}")));
    }


//    public static void createDummyUser(){
//        stubFor(post(urlEqualTo("/api/users"))
//                .withHeader("Content-Type", WireMock.equalTo("application/json"))
//                .withRequestBody(equalToJson("{\"name\": \"Naveen\"}"))
//                .willReturn(aResponse()
//                        .withStatus(201)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"message\": \"user is created\"}")));
//    }
    
   

//    public static void createDummyUser() {
//        stubFor(post(urlEqualTo("/api/users"))
//                .withHeader("Content-Type", equalTo("application/json"))
//                .withRequestBody(equalToJson("{\"name\": \"Naveen\"}"))
//                .willReturn(aResponse()
//                        .withStatus(201)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"message\": \"user is created\", \"id\": \"{{randomValue type='UUID'}}\"}")
//                        .withTransformers("response-template")));
//    }
    

    public static void createDummyUser() {
        // Generate a random integer between 2 and 3
        int randomId = ThreadLocalRandom.current().nextInt(2, 4); // 4 is exclusive, so it generates 2 or 3.

        stubFor(post(urlEqualTo("/api/users"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withStatusMessage("user is created")
                        .withBody("{ \"id\": " + randomId + ", \"name\": \"naveen\"}")));
    }
    
    public static void createDummyUserWithJson() {

        stubFor(post(urlEqualTo("/api/users"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withStatusMessage("user is created")
                        .withBodyFile("user.json")));
    }




    public static void deleteDummyUser(){
        stubFor(delete(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(204)
                		.withBody("Resource not found")));
    }


    public static void getWithQueryParams() {
        stubFor(get(urlPathEqualTo("/api/users"))
                .withQueryParam("param", equalTo("value")) // Query parameter
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\": \"Query success!\"}")));
    }
    
    
    
    public static void setupPaymentMock() {
        stubFor(post(urlEqualTo("/api/payments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"paymentId\": \"123456\", \"status\": \"success\", \"message\": \"Payment processed successfully.\"}")));
    }

    public static void setupOrderMock() {
        stubFor(post(urlEqualTo("/api/orders"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"orderId\": \"order789\", \"status\": \"pending\", \"message\": \"Order created successfully.\"}")));
    }
    
    public static void setupShippingMocks() {
        stubFor(post(urlEqualTo("/api/shipping/calculate"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson("{\"from\": \"NY\", \"to\": \"CA\", \"weight\": 2.5}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"shippingCost\": 10.00, \"estimatedDelivery\": \"3-5 business days\"}")));

        // Mock for tracking a shipment
        stubFor(get(urlPathMatching("/api/shipping/track/[0-9]+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"trackingId\": \"12345\", \"status\": \"In Transit\", \"currentLocation\": \"Los Angeles, CA\"}")));
    }


}
