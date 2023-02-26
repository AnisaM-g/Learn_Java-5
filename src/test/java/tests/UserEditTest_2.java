package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

@Epic("Edit user cases")
@Feature("Edit user")

public class UserEditTest_2 extends BaseTestCase {

    String cookie;
    String header;
    int userId;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test

    public void testEditJastCreateTest(){
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        this.userId = responseCreateAuth.jsonPath().getInt("id");
        System.out.println(userId);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        //EDIT
        String newName = "Chanded Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithUserIdTokenCookieEditData("https://playground.learnqa.ru/api/user/",
                        this.userId,
                        this.header,
                        this.cookie,
                        editData);


        //GET
        Response responseUserData = apiCoreRequests
                .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                        this.userId,
                        this.header,
                        this.cookie);

        //System.out.println(responseEditUser.asString());
        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

}
