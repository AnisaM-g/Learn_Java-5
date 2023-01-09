package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;

import java.util.HashMap;
import java.util.Map;



public class UserAuthTest extends BaseTestCase {


    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach // здесь пропишем логику с которой должны начинаться все тесты в классе. Например с подготовкой каких-то тестовых данных.

    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

/*
this - специальный указатель позволяющий делать переменную полем класса и как следствие передавать её значение
из одной функции в другие.
 */
    }


    @Test


    public void testAuthUser(){

        Response responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

       Assertions.asserJsonByName(responseCheckAuth,"user_id", this.userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})

    public void testNegativeAuthUser(String condition){

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth"); // задали урл на который будет отправлять запрос

        if(condition.equals("cookie")) { // условие: Если нам пришли куки, то подставляем только их, если пришли заголовки, то только их. В противном случае - исключение

            spec.cookie("auth_sid", this.cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        } else{
            throw new IllegalArgumentException("Condition value is known: " + condition); // исключение
        }


        Response responseForChek = spec
                .get()
                .andReturn(); // от переменной spec вызываем метод get, который задали выше и ответ парсим в переменную как json

        Assertions.asserJsonByName(responseForChek,"user_id",0);

    }

}
