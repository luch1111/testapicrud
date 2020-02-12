package com.randomname.integration;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PutTest extends AbstractTest {

    @Test
    public void updateExistentUserWithFullData() {

        String initFirstName = "test";
        String initLastName = "user";
        String updFirstName = "test_upd";
        String updLastName = "user_upd";

        HashMap<String, String> initHeaders = buildHeaders(initFirstName, initLastName);
        HashMap<String, String> updHeaders = buildHeaders(updFirstName, updLastName);

        //setup: add new user
        Response responsePost = given().headers(initHeaders).when().post(url);
        responsePost.then().log().ifValidationFails().statusCode(200);
        Map postData = parseResponseDataIntoMap(responsePost.asString().replaceAll(regexp, ""));
        String id = (String) postData.get("ID");

        //test execution
        given().headers(updHeaders)
                .when().put(url + id)
                .then().log().ifValidationFails().statusCode(200);

        Response responseGet = get(url + id);
        responseGet.then().log().ifValidationFails().statusCode(200);

        Map getData = parseResponseDataIntoMap(responseGet.asString().replaceAll(regexp, ""));

        assertThat(getData.get(ID)).isEqualTo(id);
        assertThat(getData.get(FIRSTNAME)).isEqualTo(updFirstName);
        assertThat(getData.get(LASTNAME)).isEqualTo(updLastName);
    }

    @Test
    public void updateNonexistentUser() {
        String initFirstName = "test";
        String initLastName = "user";
        HashMap<String, String> initHeaders = buildHeaders(initFirstName, initLastName);

        given().headers(initHeaders)
                .when().put(url + nonexistUser)
                .then().log().ifValidationFails().statusCode(404);
    }

    @Test
    public void updateExistentUserWithPartialData() {

        String initFirstName = "test";
        String initLastName = "user";
        String updLastName = "user_upd";

        HashMap<String, String> initHeaders = buildHeaders(initFirstName, initLastName);
        HashMap<String, String> updHeaders = buildHeaders(null, updLastName);

        //setup: add new user
        Response responsePost = given().headers(initHeaders).when().post(url);
        responsePost.then().log().ifValidationFails().statusCode(200);
        Map postData = parseResponseDataIntoMap(responsePost.asString().replaceAll(regexp, ""));
        String id = (String) postData.get("ID");

        //test execution
        given().headers(updHeaders)
                .when().put(url + id)
                .then().log().ifValidationFails().statusCode(200);

        Response responseGet = get(url + id);
        responseGet.then().log().ifValidationFails().statusCode(200);

        Map getData = parseResponseDataIntoMap(responseGet.asString().replaceAll(regexp, ""));

        assertThat(getData.get(ID)).isEqualTo(id);
        assertThat(getData.get(FIRSTNAME)).isEqualTo("");
        assertThat(getData.get(LASTNAME)).isEqualTo(updLastName);
    }

}
