package org.mock.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class MockGETUserQueryParamTest {

    @BeforeTest
    public void setup(){
        WireMockSetup.createWireMockServer();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8089;
    }

    @AfterTest
    public void tearDown(){
        WireMockSetup.stopWireMockServer();
    }

    @Test
    public void mockUserQueryParamAPITest(){
        APIMocks.getDummyUserWithQueryParams();

        RestAssured.given().log().all()
                .queryParam("param", "value")
                .when().log().all()
                .get("/api/users")
                .then().log().all()
                .statusCode(200)
                .body("result", equalTo("user is searched"));

    }


    @Test
    public void testGetWithQueryParams() {
        APIMocks.getWithQueryParams(); // Stub the GET endpoint

        Response response = RestAssured
                .given()
                .queryParam("param", "value") // Set the query parameter
                .when()
                .get("/api/users");

        response.then()
                .assertThat()
                .statusCode(200)
                .contentType("application/json");
    }





}
