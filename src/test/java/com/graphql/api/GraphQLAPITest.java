package com.graphql.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GraphQLAPITest {

    final String GRAPHQL_ENDPOINT = "https://gorest.co.in/public/v2/graphql";

    @Test
    public void testGraphQLAPI(){
        RestAssured.baseURI = "https://rickandmortyapi.com/graphql";

        String query = "query character($id: ID!) { character(id: $id) { origin { id } location { id } created } }";
        String variables = "{\"id\":\"3\"}";

       Response response =  given()
                    .contentType(ContentType.JSON)
                    .body("{\"query\":\"query character($id: ID!) { character(id: $id) { origin { id } location { id } created name } }\",\"variables\":{\"id\":\"1\"}}")
                    .when()
                    .post()
                    .then()
                    .extract().response();

        response.prettyPrint();

    }


    @Test
    public void testGraphQLAPI1(){
        RestAssured.baseURI = "https://rickandmortyapi.com/graphql";

        String query = "{\"query\":\"query {\\n  characters{\\n    info{\\n      count,\\n      pages\\n    }\\n    results{\\n      id,\\n      name,\\n      status\\n    }\\n  }\\n}\\n\"}";
        String variables = "{\"id\":\"3\"}";

        Response response =  given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post()
                .then()
                .extract().response();

        response.prettyPrint();

    }




        // Your GraphQL endpoint - replace with actual URL

        @Test
        public void testCreateUserMutation() {

            // GraphQL mutation as a String
            String mutation = """
            mutation CreateUser {
                createUser(
                    input: {
                        name: "naveen"
                        email: "naveenql@gmail.com"
                        gender: "male"
                        status: "active"
                    }
                ) {
                    user {
                        email
                        gender
                        id
                        name
                        status
                    }
                }
            }
        """;

            // Create JSON request body
            String requestBody = String.format("{\"query\": \"%s\"}",
                    mutation.replace("\n", "\\n").replace("\"", "\\\""));

            // Make the POST request and validate response
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer e4b8e1f593dc4a731a153c5ec8cc9b8bbb583ae964ce650a741113091b4e2ac6")
                    .body(requestBody)
                    // Add any required headers here
                    // .header("Authorization", "Bearer your-token")
                    .when()
                    .post(GRAPHQL_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body("data.createUser.user.name", equalTo("naveen"))
                    .body("data.createUser.user.email", equalTo("naveenql@gmail.com"))
                    .body("data.createUser.user.gender", equalTo("male"))
                    .body("data.createUser.user.status", equalTo("active"))
                    .body("data.createUser.user.id", notNullValue());
        }

        // Alternative approach using Map for request body
        @Test
        public void testCreateUserMutationUsingMap() {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("query", """
            mutation CreateUser {
                createUser(
                    input: {
                        name: "naveen"
                        email: "naveenql@gmail.com"
                        gender: "male"
                        status: "active"
                    }
                ) {
                    user {
                        email
                        gender
                        id
                        name
                        status
                    }
                }
            }
        """);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post(GRAPHQL_ENDPOINT)
                    .then()
                    .statusCode(200)
                    .body("data.createUser.user.name", equalTo("naveen"))
                    .body("data.createUser.user.email", equalTo("naveenql@gmail.com"))
                    .body("data.createUser.user.gender", equalTo("male"))
                    .body("data.createUser.user.status", equalTo("active"))
                    .body("data.createUser.user.id", notNullValue());
        }
    }

