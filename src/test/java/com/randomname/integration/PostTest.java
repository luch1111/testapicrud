package com.randomname.integration;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTest extends AbstractTest {

    @Test
    public void postUserWithFirstAndLastName() {

        String firstName = "test";
        String lastName = "user";
        HashMap<String, String> headers = buildHeaders(firstName, lastName);
        Response responsePost = given().headers(headers).when().post(url);
        responsePost.then().log().ifValidationFails().statusCode(200);

        Map postData = parseResponseDataIntoMap(responsePost.asString().replaceAll(regexp, ""));
        assertThat(postData.get(FIRSTNAME)).isEqualTo(firstName);
        assertThat(postData.get(LASTNAME)).isEqualTo(lastName);

        String id = (String) postData.get("ID");

        Response responseGet = get(url + id);
        responseGet.then().log().ifValidationFails().statusCode(200);

        Map getData = parseResponseDataIntoMap(responseGet.asString().replaceAll(regexp, ""));
        assertThat(getData.get(ID)).isEqualTo(id);
        assertThat(getData.get(FIRSTNAME)).isEqualTo(firstName);
        assertThat(getData.get(LASTNAME)).isEqualTo(lastName);
    }

    @Test
    public void postUserWithOnlyFirstName() {
        String firstName = "test";
        HashMap<String, String> headers = buildHeaders(firstName, null);

        Response responsePost = given().headers(headers).when().post(url);

        //to clarify requirements about expected status
        responsePost.then().log().ifValidationFails().statusCode(200);
    }

    @Test
    public void postUserWithOnlyLastName() {
        String lastName = "test";
        HashMap<String, String> headers = buildHeaders(null, lastName);

        Response responsePost = given().headers(headers).when().post(url);

        //to clarify requirements about expected status
        responsePost.then().log().ifValidationFails().statusCode(200);
    }

    @Test
    public void postUserWithoutFirstAndLastNames() {

        Response responsePost = post(url);
        responsePost.then().log().ifValidationFails().statusCode(400);
    }
}
