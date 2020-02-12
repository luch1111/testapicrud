package com.randomname.integration;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteTest extends AbstractTest {

    @Test
    public void deleteExistentUser() {

        String existentUser;
        String firstName = "test";
        String lastName = "user";

        //setup: add user and get his ID
        HashMap<String, String> headers = buildHeaders(firstName, lastName);
        Response responsePost = given().headers(headers).when().post(url);

        Map postData = parseResponseDataIntoMap(responsePost.asString().replaceAll(regexp, ""));
        existentUser = (String) postData.get("ID");

        //test
        delete(url + existentUser).then().log().ifValidationFails().statusCode(200);

        Response responseGet = get(url + existentUser);
        assertThat(responseGet.asString()).isEqualTo("[]");
    }

    @Test
    public void deleteNonexistentUser() {
        delete(url + nonexistUser).then().log().ifValidationFails().statusCode(200);
    }

    @Test
    public void deleteUserWithAlphabeticalId() {
        delete(url + "test").then().log().ifValidationFails().statusCode(200);
    }
}
