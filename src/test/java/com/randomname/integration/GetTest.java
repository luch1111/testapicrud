package com.randomname.integration;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class GetTest extends AbstractTest {

    private static String firstName = "test";
    private static String lastName = "user";

    private HashMap<String, String> headers = buildHeaders(firstName, lastName);

    @Test
    public void getExistentUser() {
        //setup: insert a new user and his ID
        Response responsePost = given().headers(headers).when().post(baseUrl);
        Map postData = parseResponseDataIntoMap(responsePost.asString().replaceAll(regexp, ""));
        String existentUser = (String) postData.get("ID");

        //test
        Response responseGet = get(baseUrl + existentUser);
        responseGet.then().log().ifValidationFails().statusCode(200);

        Map get = parseResponseDataIntoMap(responseGet.asString().replaceAll(regexp, ""));
        assertThat(get.get(ID)).isEqualTo(existentUser);
        assertThat(get.get(FIRSTNAME)).isEqualTo(firstName);
        assertThat(get.get(LASTNAME)).isEqualTo(lastName);
    }

    @Test
    public void getNonexistentUserWithNumericID() {

        Response responseGet = get(baseUrl + nonexistUser);
        responseGet.then().log().ifValidationFails().statusCode(200);
        assertThat(responseGet.asString()).isEqualTo("[]");
    }

    @Test
    public void getNonexistentUserWithAlphabeticID() {

        String id = "test";

        Response responseGet = get(baseUrl + id);
        responseGet.then().log().ifValidationFails().statusCode(200);
        assertThat(responseGet.asString()).isEqualTo("[]");
    }

    @Test
    public void getAllUsers() {

        //setup: insert 2 new users
        given().headers(headers).when().post(baseUrl);
        given().headers(headers).when().post(baseUrl);

        Response responseGet = get(baseUrl);
        responseGet.then().log().ifValidationFails().statusCode(200);
        String[] results = responseGet.asString().replaceAll(regexp, "").split("\\}, \\{");

        assertThat(results.length).isGreaterThanOrEqualTo(2);
    }
}
