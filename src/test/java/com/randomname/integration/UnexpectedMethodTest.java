package com.randomname.integration;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;

public class UnexpectedMethodTest extends AbstractTest {

    HashMap<String, String> headers;

    @Before
    public void setup() {
        String fn = "test";
        String ln = "user";
        headers = buildHeaders(fn, ln);
    }

    @Test
    public void putRequestToBaseUrl() {

        given().headers(headers)
                .when().put(url)
                .then().log().ifValidationFails().statusCode(405);
    }

    @Test
    public void deleteRequestToBaseUrl() {

        delete(url).then().log().ifValidationFails().statusCode(405);
    }

    @Test
    public void postRequestToGetUserUrl() {

        given().headers(headers)
                .when().post(url + nonexistUser)
                .then().log().ifValidationFails().statusCode(405);
    }
}
