package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import lib.ApiCoreRequests;

@Epic("Authorisation cases")
@Feature("Athorisation")
public class UserAuthTest_2 extends BaseTestCase {


    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach // здесь пропишем логику с которой должны начинаться все тесты в классе. Например с подготовкой каких-то тестовых данных.

    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

/*
this - специальный указатель позволяющий делать переменную полем класса и как следствие передавать её значение
из одной функции в другие.
 */
    }


    @Test
    @Description("This test successfuly authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie
                );

       Assertions.assertJsonByName(responseCheckAuth,"user_id", this.userIdOnAuth);
    }

    @Description("This test checks autorisation status w/o sending auth cookie or token")
    @DisplayName("Test negativ auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})

    public void testNegativeAuthUser(String condition){

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth"); // задали урл на который будет отправлять запрос

        if(condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is not known: "+ condition);
        }
    }

}
