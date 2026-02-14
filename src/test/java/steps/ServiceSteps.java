package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceSteps {

    @Step("Отправить с методом GET запрос на {url}")
    public Response sendGetRequest (String url) {
        return given().get(url);
    }

    @Step("Отправить с методом GET запрос на {url} с параметром {param}")
    public Response sendGetRequestWithParam (String url, String param, int value) {
        return given().queryParam(param, value).get(url);
    }

    @Step("Отправить с методом POST запрос на {url}")
    public Response sendPostRequest (String url, String body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(url);
    }

    @Step("Отправить с методом PUT запрос на {url}")
    public Response sendPutRequestWithParam (String url, String param, int value) {
        return given()
                .queryParam(param, value)
                .put(url);
    }

    @Step("Отправить с методом PUT запрос на {url}")
    public Response sendPutRequest (String url, String body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(url);
    }
    @Step("Отправить с методом PUT запрос на {url}")
    public Response sendPutRequestWithoutBody (String url) {
        return given().post(url);
    }

    @Step("Отправить запрос с методом DELETE на {url}")
    public Response sendDeleteRequest (String url) {
        return given().delete(url);
    }

    @Step("Проверить соответствие статуса запроса из ответа статусу {status}")
    public void checkRequestStatus(Response response, int status) {
        response.then().statusCode(status);
    }

    @Step("Проверить соответствие текста ответа сервера тексту из документации")
    public void checkRequestMessage(String docMessage, String responseMessage) {
        assertEquals(docMessage, responseMessage, "Сервер вернул не корректное сообщение.");
    }

    public String createDate(int days){
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
