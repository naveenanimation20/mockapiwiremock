package org.mock.api;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static org.hamcrest.Matchers.*;

public class MockShippingAPITest {
	
	@BeforeTest
    public void setup(){
        WireMockSetup.createWireMockServer();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8089;
        APIMocks.setupShippingMocks();
    }

    @AfterTest
    public void tearDown(){
        WireMockSetup.stopWireMockServer();
    }
    
    @Test
    public void testCalculateShipping() {
    	RestAssured.given()
            .contentType("application/json")
            .body("{\"from\": \"NY\", \"to\": \"CA\", \"weight\": 2.5}")
        .when()
            .post("/api/shipping/calculate")
        .then()
            .statusCode(200)
            .body("shippingCost", equalTo(10.00f))
            .body("estimatedDelivery", equalTo("3-5 business days"));
    }

    @Test
    public void testTrackShipment() {
    	RestAssured.given()
        .when()
            .get("/api/shipping/track/12345")
        .then()
            .statusCode(200)
            .body("trackingId", equalTo("12345"))
            .body("status", equalTo("In Transit"))
            .body("currentLocation", equalTo("Los Angeles, CA"));
    }

}
