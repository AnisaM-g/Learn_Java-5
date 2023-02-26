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

    String cookie;
    String header;
    int userId;
    int userId_2;
    String oldName;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test

    @Description("This is a test for changing user data without authorization")
    @DisplayName("Test for changing user data without authorization")


    public void testEditUserDataWithoutAutorization(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        this.userId = responseCreateAuth.jsonPath().getInt("id");
        System.out.println(userId);

        String oldName = responseCreateAuth.jsonPath().getString("firstName");
        System.out.println(oldName);

        //EDIT
        String newName = "Chanded Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestUnauthorisidUser("https://playground.learnqa.ru/api/user/",
                        this.userId,
                        editData);

        responseEditUser.prettyPrint();

        //String answer = responseEditUser.asString();
        //System.out.println(answer);

        Assertions.assertResponseTextEqualseHtml(responseEditUser, "Auth token not supplied");
        Assertions.assertResponseCodeEqualse(responseEditUser, 400);
    }

    //
    @Test

    @Description("This is a test of editing user data while being authorized by another user")
    @DisplayName("Test of editing user data while being authorized by another user")

    public void testEditCreateUserDataBeingAutorizedByAnotherUser(){
        //GENERATE USER ONE
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        this.userId = responseCreateAuth.jsonPath().getInt("id");
        //System.out.println(userId);

        //System.out.println(responseCreateAuth);
        //responseCreateAuth.prettyPrint();


        this.oldName = userData.get("firstName");
        //System.out.println(oldName);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        //System.out.println(this.header);
        //System.out.println(this.cookie);

        //GENERATE USER TWO
        Map<String, String> userData_2 = DataGenerator.getRegistrationData();

        Response responseCreateAuth_2 = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData_2);

        this.userId_2 = responseCreateAuth_2.jsonPath().getInt("id");
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
                        this.userId_2,
                        this.header,
                        this.cookie,
                        editData);

        //System.out.println("сообщение");

        //responseEditUser_2.prettyPrint();


        //GET
        Response responseUserData = apiCoreRequests
                .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                        this.userId_2,
                        this.header,
                        this.cookie);

        //System.out.println("сообщение2");

        //responseUserData.prettyPrint();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");

        String[] expectedFields = {"lastName", "email"};

        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
    }

}
