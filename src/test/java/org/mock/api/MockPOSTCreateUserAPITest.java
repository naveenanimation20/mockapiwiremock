package org.mock.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;


public class MockPOSTCreateUserAPITest {

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
    public void createDummyUserTest(){
        APIMocks.createDummyUser();

       Response response = RestAssured.given().log().all()
                    .contentType("application/json")
                    .body("{\"name\": \"Naveen\"}")
                    .when()
                    .post("/api/users");

        response.then().log().all()
                .assertThat().statusCode(201)
                            .contentType("application/json")
                            .statusLine("HTTP/1.1 201 user is created")
                            .body("id", notNullValue());

        System.out.println(response.getBody().asString());

    }
    
    @Test
    public void createDummyUserWithJsonFileTest(){
        APIMocks.createDummyUserWithJson();

       Response response = RestAssured.given().log().all()
                    .contentType("application/json")
                    .body("{\"name\": \"Tom\"}")
                    .when()
                    .post("/api/users");

        response.then().log().all()
                .assertThat().statusCode(201)
                            .contentType("application/json")
                            .statusLine("HTTP/1.1 201 user is created")
                            .body("id", notNullValue());

        System.out.println(response.getBody().asString());

    }






}
