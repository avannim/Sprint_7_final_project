package tests;

import com.google.gson.Gson;
import dto.request.CreateCourierRequest;
import dto.request.LoginRequest;
import dto.response.ErrorResponse;
import dto.response.LoginCourierResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.ServiceSteps;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginCourierTest {

    private final Gson gson = new Gson();
    private final ServiceSteps serviceSteps = new ServiceSteps();
    private CreateCourierRequest newCourier;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        newCourier = new CreateCourierRequest("oddinson_courier", "Qwerty123@", "Тор");
        String body = gson.toJson(newCourier);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
    }

    @Test
    @DisplayName("Тест успешной авторизации курьера")
    public void  checkLoginCourierTest() {
        LoginRequest loginRequest = new LoginRequest(newCourier.getLogin(), newCourier.getPassword());
        String body2 = gson.toJson(loginRequest);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier/login", body2);
        serviceSteps.checkRequestStatus(response, 200);
        LoginCourierResponse loginResponse = gson.fromJson(response.body().asString(), LoginCourierResponse.class);
        assertNotNull(loginResponse.getId());
    }

    @ParameterizedTest
    @MethodSource("loginTestData")
    @DisplayName("Тест авторизации курьера без одного из полей в теле запроса")
    public void  checkLoginCourierWithoutFieldTest(String body) {
        Response response = serviceSteps.sendPostRequest("/api/v1/courier/login", body);
        serviceSteps.checkRequestStatus(response, 400);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Недостаточно данных для входа", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Тест авторизации курьера с нечуществующими логином и паролем")
    public void  checkLoginCourierTestWithWrongLoginPassword() {
        String loginRequest = "{\"login\":\"oddinson\", \"password\":\"Qwerty123\"}";
        Response response = serviceSteps.sendPostRequest("/api/v1/courier/login", loginRequest);
        serviceSteps.checkRequestStatus(response, 404);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Учетная запись не найдена", errorResponse.getMessage());
    }

    @AfterEach
    public void tearDown() {
        LoginRequest loginRequest = new LoginRequest(newCourier.getLogin(), newCourier.getPassword());
        String body = gson.toJson(loginRequest);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier/login", body);
        serviceSteps.checkRequestStatus(response, 200);
        LoginCourierResponse loginResponse = gson.fromJson(response.body().asString(), LoginCourierResponse.class);
        serviceSteps.sendDeleteRequest("/api/v1/courier/"+loginResponse.getId());
    }

    static Stream<Arguments> loginTestData() {
        return Stream.of(
                Arguments.of("{\"login\":\"oddinson_courier\", \"password\":\"\"}"),
                Arguments.of("{\"login\":\"\", \"password\":\"Qwerty123@\"}")
        );
    }
}
