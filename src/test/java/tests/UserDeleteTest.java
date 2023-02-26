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

    public class UserDeleteTest extends BaseTestCase {
        private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


        // - попытку удалить пользователя по ID 2
        @Test

        @Description("This test tries to remove the user with id 2")
        @DisplayName("Test tries to remove the user with id 2")


        public void testDeleteUserWith2() {

            //LOGIN
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "vinkotov@example.com");
            authData.put("password", "1234");

            Response responseGetAuth = apiCoreRequests
                    .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

            String header = this.getHeader(responseGetAuth, "x-csrf-token");
            String cookie = this.getCookie(responseGetAuth, "auth_sid");
            int userId = 2;


            //DELETE

            Response reponseDeleteUser = apiCoreRequests
                    .makeDeleteUser("https://playground.learnqa.ru/api/user/",
                            userId,
                            header,
                            cookie);

            reponseDeleteUser.prettyPrint();
            Assertions.assertResponseTextEqualseHtml(reponseDeleteUser,
                    "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");


            //GET
            Response responseUserData = apiCoreRequests
                    .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                            userId,
                            header,
                            cookie);

            String[] expectedFields = {"username", "firstName", "lastName", "email"};

            Assertions.assertJsonHasFields(responseUserData, expectedFields);

        }


        //Создать пользователя, авторизоваться из-под него, удалить,
        // затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален.

        @Test

        @Description("This test creates a user, logs in, deletes the user, and checks that the user has been deleted")
        @DisplayName("Test tries to remove the user")


        public void testEditCreateUserOnInvalidEmail(){
            //GENERATE USER ONE
            Map<String, String> userData = DataGenerator.getRegistrationData();

            Response responseCreateAuth = apiCoreRequests
                    .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

            int userId = responseCreateAuth.jsonPath().getInt("id");


            //LOGIN
            Map<String, String> authData = new HashMap<>();
            authData.put("email", userData.get("email"));
            authData.put("password", userData.get("password"));

            Response responseGetAuth = apiCoreRequests
                    .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

            String header = this.getHeader(responseGetAuth, "x-csrf-token");
            String cookie = this.getCookie(responseGetAuth, "auth_sid");


            //DELETE

            Response reponseDeleteUser = apiCoreRequests
                    .makeDeleteUser("https://playground.learnqa.ru/api/user/",
                            userId,
                            header,
                            cookie);

            reponseDeleteUser.prettyPrint();



            //GET
            Response responseUserData = apiCoreRequests
                    .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                            userId,
                            header,
                            cookie);

            responseUserData.prettyPrint();

            Assertions.assertResponseTextEqualseHtml(responseUserData, "User not found");
        }

        // - Попытаемся удалить пользователя, будучи авторизованными другим пользователем
        @Test

        @Description("This is a test delete user while being authorized by another user")
        @DisplayName("test delete user while being authorized by another user")


        public void testEditCreateUserWithInvalidEmail(){
            //GENERATE USER ONE
            Map<String, String> userData = DataGenerator.getRegistrationData();

            Response responseCreateAuth = apiCoreRequests
                    .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);


            //LOGIN
            Map<String, String> authData = new HashMap<>();
            authData.put("email", userData.get("email"));
            authData.put("password", userData.get("password"));

            Response responseGetAuth = apiCoreRequests
                    .makePostRequestWithAuthData("https://playground.learnqa.ru/api/user/login", authData);

            String header = this.getHeader(responseGetAuth, "x-csrf-token");
            String cookie = this.getCookie(responseGetAuth, "auth_sid");


            //GENERATE USER TWO
            Map<String, String> userData_2 = DataGenerator.getRegistrationData();

            Response responseCreateAuth_2 = apiCoreRequests
                    .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData_2);

            int userId_2 = responseCreateAuth_2.jsonPath().getInt("id");


            //DELETE

            Response reponseDeleteUser = apiCoreRequests
                    .makeDeleteUser("https://playground.learnqa.ru/api/user/",
                            userId_2,
                            header,
                            cookie);

            reponseDeleteUser.prettyPrint();


            //GET
            Response responseUserData = apiCoreRequests
                    .makePostRequestWithUserIdTokenCookie("https://playground.learnqa.ru/api/user/",
                            userId_2,
                            header,
                            cookie);


            Assertions.assertJsonHasField(responseUserData, "username");

        }




    }