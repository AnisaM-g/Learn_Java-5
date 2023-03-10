package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("x-csrf-token", token)
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public Response makePostRequestWithAuthData(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }


    @Step("Make a POST-request with user data")
    public Response makePostRequestWithUserData(String url, Map<String, String> userData) {
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-request with user_id, token and cookie")
    public Response makePostRequestWithUserIdTokenCookie(String url, int user_id, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url + user_id)
                .andReturn();
    }

    @Step("Make a PUT-request with user_id, token, cookie and edit data")
    public Response makePutRequestWithUserIdTokenCookieEditData(String url, int user_id, String token, String cookie, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url + user_id)
                .andReturn();
    }

    @Step("Make a PUT request to an unauthorized user")
    public Response makePutRequestUnauthorisidUser(String url, int user_id, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url + user_id)
                .andReturn();
    }

    @Step("Make a DELETE-request")
    public Response makeDeleteUser(String url, int user_id, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url + user_id)
                .andReturn();
    }
}
