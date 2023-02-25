package tests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

@Epic("Get user cases")
@Feature("Get user")
public class UserGetTest_2 extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test //получение данных пользователя, когда мы не авторизованы

    @Description("This test get user with not autorization")
    @DisplayName("Test negativ get user with not autorization")


    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonHasNotKey(responseUserData, "firstName");
        Assertions.assertJsonHasNotKey(responseUserData, "lastName");
        Assertions.assertJsonHasNotKey(responseUserData, "email");
    }

    @Test

    @Description("This test get user with autorization")
    @DisplayName("Test negativ get user with autorization")


    public void testGetUserDetailsAuthAsSameUser_2() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};

        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }

    //В этой задаче нужно написать тест, который авторизовывается одним пользователем,
    // но получает данные другого (т.е. с другим ID).
    // И убедиться, что в этом случае запрос также получает только username,
    // так как мы не должны видеть остальные данные чужого пользователя.

    @Test

    @Description("This test user login with another autorization data")
    @DisplayName("Test negativ user login with another autorization data")


    public void testGetUserDetailsAuthAsAnotherUser() {

        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestUserLogin("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth,"auth_sid");

        Response responseUserData = apiCoreRequests
                .makePostRequestGetUser(
                        "https://playground.learnqa.ru/api/user/",
                        1,
                        this.header,
                        this.cookie
                );

        System.out.println(responseUserData.asString());

        Assertions.assertJsonHasKey(responseUserData, "username");
        Assertions.assertJsonHasNotKey(responseUserData, "firstName");
        Assertions.assertJsonHasNotKey(responseUserData, "lastName");
        Assertions.assertJsonHasNotKey(responseUserData, "email");

    }

}
