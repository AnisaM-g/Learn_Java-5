package tests;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.HashMap;
import java.util.Map;

@Epic("Create user cases")
@Feature("Create user")

public class userRegisterTest_3 extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test // создание пользователя с существующим email

    @Description("This test create user with existing email")
    @DisplayName("Test negativ creat user with existing email")

    public void testCreateUserWithExistingEmail(){

        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);


        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"Users with email '" + email + "' already exists");
    }

    @Test //Создание пользователя с некорректным email - без символа @

    @Description("This test with not correction email")
    @DisplayName("Test negativ create user with not correction email")

    public void testCreateUserWithNotCorrectionEmail(){

        String email = "vinkotovexample.com";
        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"Invalid email format");
    }

    @Test //Создание пользователя с очень коротким именем в один символ
    @Description("This test with not correction username")
    @DisplayName("Test negativ create user with not correction min username")

    public void testCreateUserWithNotCorretionName(){

        Map<String, String> userData = new HashMap<>();
        userData.put("username", "a");
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"The value of 'username' field is too short");
    }



    @Test //Создание пользователя с очень длинным именем - длиннее 250 символов

    @Description("This test with not correction username")
    @DisplayName("Test negativ create user with not correction max username")

    public void testCreateUserWithNotCorretionMaxName(){

        String username = "dfasdfadfadsfajfhakjfdhksajhdfkajhfkajshdfkjahfdkaj" +
                "hfkdashsdfsdfsdfsmdfmsbfmnsbdfmnsbmfnsbdmfnsbmdfnbsmdfbsmndbfmsndbfmsb" +
                "dfmsnbfmsbfmsnbdmnfbsmnfbsmfdbsmdnfbdsmnbfmsdnbfmsbdfmsdbfsmsmmsdsmnfbjshfgsmdnbfjs " +
                "gmdnb fasdfaf aa dsf a adf asdf asdf asdf dsfs";

        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEqualse(responseCreateAuth,400);
        Assertions.assertResponseTextEqualse(responseCreateAuth,"The value of 'username' field is too long");
    }


    @ParameterizedTest //Создание пользователя без указания одного из полей
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})

    @Description("This test without field")
    @DisplayName("Test negativ test create user without field")

    public void testCreateUserWithoutField(String condition) {

        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData();

        userData.remove(condition);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEqualse(responseCreateAuth, 400);
        Assertions.assertResponseTextEqualse(responseCreateAuth, "The following required params are missed: "+ condition);
    }


    @Test
    @Description("This test create user")
    @DisplayName("Test positiv test create user")

    public void testCreateUserSuccessfuly(){

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestWithUserData("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEqualse(responseCreateAuth,200);
        Assertions.assertJsonHasKey(responseCreateAuth, "id");
    }
}

