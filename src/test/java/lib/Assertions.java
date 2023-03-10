package lib;

import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

public class Assertions {


    // метод, чтобы убедиться, что значение внутри ответа доступно по определенному имени. Ожидаемый результат int
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$",hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    // метод, чтобы убедиться, что значение внутри ответа доступно по определенному имени. Ожидаемый результат String
    public static void assertJsonByName(Response Response, String name, String expectedValue){
        Response.then().assertThat().body("$",hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    // метод, который проверяет, что текст ответа сервера равен ожидаемому
    public static void assertResponseTextEqualse(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

    // метод, который проверяет, что текст ответа сервера равен ожидаемому
    public static void assertResponseTextEqualseJson(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.jsonPath().getString("error"),
                "Response text is not as expected"
        );
    }

    // метод, который проверяет, что текст ответа сервера равен ожидаемому
    public static void assertResponseTextEqualseHtml(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.htmlPath().getString("Auth token not supplied"),
                "Response text is not as expected"
        );
    }



    // метод, который проверяет, что код ответа сервера равен ожидаемому
    public static void assertResponseCodeEqualse(Response Response, int expectedStatusCode){
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }

    //
    public static void assertJsonHasKey(Response Response, String expectedFieldName){
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    //метод позволяет проверить, что в ответе нет каких-то полей по названиям
    public static void assertJsonHasNotKey(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    //
    public static void assertJsonHasField(Response Response, String expectedFieldName){
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    //
    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertJsonHasFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

    public static void assertJsonHasNotFields(Response Response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasNotField(Response, expectedFieldName);
        }
    }

    public static void assertEqualsEquality(Response Response, String name, String expectedMassage) {
        assertEquals(expectedMassage,
                Response.jsonPath().getString(name),
                "JSON value is not equal to expected value");
        }
    }

