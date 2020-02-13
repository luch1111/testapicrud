package com.randomname.integration;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteTest extends AbstractTest {

    private String firstName = "test";
    private String lastName = "user";
    HashMap<String, String> headers;

    @Before
    public void setup() {
        headers = buildHeaders(firstName, lastName);
    }

    @Test
    public void deleteExistentUser() {

        String existentUser;

        //setup: add user and get his ID
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

        //setup: insert user
        given().headers(headers).when().post(url);

        Response getAllBefore = get(url);

        //test
        delete(url + nonexistUser).then().log().ifValidationFails().statusCode(200);
        Response getAllAfter = get(url);

        assertThat(getAllAfter.asString()).isEqualTo(getAllBefore.asString());
    }

    @Test
    public void deleteUserWithAlphabeticalId() {
        delete(url + "test").then().log().ifValidationFails().statusCode(200);
    }
}
