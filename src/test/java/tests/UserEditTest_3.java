package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit user cases")
@Feature("Edit user")

public class UserEditTest_3 extends BaseTestCase {

    //String cookie;
    //String header;
    //int userId;
    //int userId_2;
    //String oldName;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    // - Попытаемся изменить данные пользователя, будучи неавторизованными
    @Test

    @Description("This is a test for changing user data without authorization")
    @DisplayName("Test for changing user data without authorization")


    public void testEditUserDataWithoutAutorization(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        int userId = responseCreateAuth.jsonPath().getInt("id");
        System.out.println(userId);

        String oldName = responseCreateAuth.jsonPath().getString("firstName");
        System.out.println(oldName);

        //EDIT
        String newName = "Chanded Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestUnauthorisidUser("https://playground.learnqa.ru/api/user/",
                        userId,
                        editData);

        responseEditUser.prettyPrint();

        //String answer = responseEditUser.asString();
        //System.out.println(answer);

        Assertions.assertResponseTextEqualseHtml(responseEditUser, "Auth token not supplied");
        Assertions.assertResponseCodeEqualse(responseEditUser, 400);
    }

    // - Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем
    @Test

    @Description("This is a test of editing user data while being authorized by another user")
    @DisplayName("Test of editing user data while being authorized by another user")


    public void testEditCreateUserWithInvalidEmail(){
        //GENERATE USER ONE
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        //int userId = responseCreateAuth.jsonPath().getInt("id");
        //System.out.println(userId);

        //System.out.println(responseCreateAuth);
        //responseCreateAuth.prettyPrint();


        //String oldName = userData.get("firstName");
        //System.out.println(oldName);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        //System.out.println(this.header);
        //System.out.println(this.cookie);

        //GENERATE USER TWO
        Map<String, String> userData_2 = DataGenerator.getRegistrationData();

        Response responseCreateAuth_2 = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData_2);

        int userId_2 = responseCreateAuth_2.jsonPath().getInt("id");
        //System.out.println(userId_2);

        //System.out.println(header);
        //System.out.println(cookie);
        //System.out.println(userId_2);

        //EDIT
        String newName = "Chanded Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser_2 = apiCoreRequests
                .makePutRequestWithUserIdTokenCookieEditData("https://playground.learnqa.ru/api/user/",
                        userId_2,
                        header,
                        cookie,
                        editData);

        //System.out.println("сообщение");

        //responseEditUser_2.prettyPrint();


        //GET
        Response responseUserData = apiCoreRequests
                .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                        userId_2,
                        header,
                        cookie);

        //System.out.println("сообщение2");

        //responseUserData.prettyPrint();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");

        String[] expectedFields = {"lastName", "email"};

        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
    }

    //Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем,
    // на новый email без символа @

    @Test

    @Description("This is a test for editing user data on invalid email")
    @DisplayName("test for editing user data on invalid email")


    public void testEditCreateUserOnInvalidEmail(){
        //GENERATE USER ONE
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        int userId = responseCreateAuth.jsonPath().getInt("id");
        String email = userData.get("email");


        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        //EDIT
        String newEmail = "vinkotovexample.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithUserIdTokenCookieEditData("https://playground.learnqa.ru/api/user/",
                        userId,
                        header,
                        cookie,
                        editData);

        System.out.println("сообщение");

        responseEditUser.prettyPrint();

        Assertions.assertResponseTextEqualseHtml(responseEditUser, "Invalid email format");
        Assertions.assertResponseCodeEqualse(responseEditUser, 400);


        //GET
        Response responseUserData = apiCoreRequests
                .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                        userId,
                        header,
                        cookie);
        //String newEmail_2 = responseUserData.jsonPath().getString("email");


        Assertions.assertEqualsEquality(responseUserData, "email", email);
    }

    //- Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем,
    // на очень короткое значение в один символ

    @Test

    @Description("This is a test for editing user data on invalid first name")
    @DisplayName("test for editing user data on invalid first name")


    public void testEditCreateUserOnInvalidFirstName(){
        //GENERATE USER ONE
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        int userId = responseCreateAuth.jsonPath().getInt("id");
        //String firstName = userData.get("firstName");


        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        //EDIT
        String newFirstName = "a";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newFirstName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithUserIdTokenCookieEditData("https://playground.learnqa.ru/api/user/",
                        userId,
                        header,
                        cookie,
                        editData);

        System.out.println("сообщение");

        responseEditUser.prettyPrint();

        Assertions.assertResponseTextEqualseJson(responseEditUser, "Too short value for field firstName");
        Assertions.assertResponseCodeEqualse(responseEditUser, 400);


        //GET
        Response responseUserData = apiCoreRequests
                .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                        userId,
                        header,
                        cookie);
        String newFirstName_2 = responseUserData.jsonPath().getString("firstName");


        Assertions.assertEqualsEquality(responseUserData, "firstName", newFirstName_2);
    }

}
