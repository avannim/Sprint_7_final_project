package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ServiceSteps {

    @Step("Отправить запрос на {url}")
    public Response sendGetRequest (String url) {
        return given().get(url);
    }

    @Step("Отправить запроса на {url}")
    public Response sendPostRequest (String url, String body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(url);
    }

    @Step("Отправить запрос на {url}")
    public Response sendDeleteRequest (String url) {
        return given().delete(url);
    }

    @Step("Проверить соответствия статуса запроса из ответа статусу {status}")
    public void checkRequestStatus(Response response, int status) {
        response.then().statusCode(status);
    }
}
