package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class userRegisterTest extends BaseTestCase {

    @Test // создание пользователя с существующим email
    public void testCreateUserWithExistingEmail(){

        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        //System.out.println(responseCreateAuth.asString());
        //System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"Users with email '" + email + "' already exists");
    }

    @Test //Создание пользователя с некорректным email - без символа @

    public void testCreateUserWithNotCorretionEmail(){

        String email = "vinkotovexample.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"Invalid email format");
    }

    @Test //Создание пользователя с очень коротким именем в один символ

    public void testCreateUserWithNotCorretionName(){

        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "a");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"The value of 'username' field is too short");
    }



    @Test //Создание пользователя с очень длинным именем - длиннее 250 символов

    public void testCreateUserWithNotCorretionMaxName(){

        String username = "dfasdfadfadsfajfhakjfdhksajhdfkajhfkajshdfkjahfdkaj" +
                "hfkdashsdfsdfsdfsmdfmsbfmnsbdfmnsbmfnsbdmfnsbmdfnbsmdfbsmndbfmsndbfmsb" +
                "dfmsnbfmsbfmsnbdmnfbsmnfbsmfdbsmdnfbdsmnbfmsdnbfmsbdfmsdbfsmsmmsdsmnfbjshfgsmdnbfjs " +
                "gmdnb fasdfaf aa dsf a adf asdf asdf asdf dsfs";

        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", username);
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"The value of 'username' field is too long");
    }


    @ParameterizedTest //Создание пользователя без указания одного из полей
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})

    public void testCreateUserWithoutFied(String condition) {

        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        userData.remove(condition);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEqualse(responseCreateAuth, 400);
        Assertions.assertResponseTextEqualse(responseCreateAuth, "The following required params are missed: "+ condition);
    }


    @Test

    public void testCreateUserSuccessfuly(){

        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEqualse(responseCreateAuth,200);
        //System.out.println(responseCreateAuth.asString());
        Assertions.assertJsonHasKey(responseCreateAuth, "id");
    }
}

