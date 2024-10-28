package org.mock.api;

import io.restassured.RestAssured;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class MockGETUserTest {

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
    public void mockUserAPITest(){

        APIMocks.getDummyUser();
        RestAssured.given().log().all()
                    .when()
                    .get("/api/users")
                    .then().log().all()
                        .statusCode(200)
                        .body("name", equalTo("Naveen"));

    }


    @Test
    public void getMockUserWithJsonTest(){

        APIMocks.getDummyUserWithFile();
        RestAssured.given().log().all()
                    .when()
                    .get("/api/users/2")
                    .then().log().all()
                        .statusCode(200)
                        .body("name", equalTo("Tom"));

    }




}
