package tests;

import com.google.gson.Gson;
import dto.request.CreateCourierRequest;
import dto.request.LoginRequest;
import dto.response.OkResponse;
import dto.response.ErrorResponse;
import dto.response.LoginCourierResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.ServiceSteps;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCourierTest {

    private final ServiceSteps serviceSteps = new ServiceSteps();
    private final Gson gson = new Gson();
    CreateCourierRequest newCourier = new CreateCourierRequest("oddinson_courier", "Qwerty123@", "Тор");

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест успешного создания курьера")
    public void  checkCreateCourierTest(){
        String body = gson.toJson(newCourier);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
        OkResponse okResponse = gson.fromJson(response.body().asString(), OkResponse.class);
        assertTrue(okResponse.isOk());
    }

    @Test
    @DisplayName("Тест создания курьера c логином который уже есть")
    public void  checkDublicateCourierTest(){
        String body = gson.toJson(newCourier);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
        Response response2 = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response2, 409);
        ErrorResponse errorResponse = gson.fromJson(response2.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Этот логин уже используется", errorResponse.getMessage());
    }

    @Tag("skipCleanup")
    @ParameterizedTest(name = "Отсутствует поле {1}")
    @MethodSource("loginTestData")
    @DisplayName("Тест создание курьера без поля в теле запроса")
    public void  checkLoginCourierWithoutFieldTest(String body, String fieldName) {
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 400);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Недостаточно данных для создания учетной записи", errorResponse.getMessage());
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        if (testInfo.getTags().contains("skipCleanup")) {
            return;
        }
        LoginRequest loginRequest = new LoginRequest(newCourier.getLogin(), newCourier.getPassword());
        String body = gson.toJson(loginRequest);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier/login", body);
        serviceSteps.checkRequestStatus(response, 200);
        LoginCourierResponse loginResponse = gson.fromJson(response.body().asString(), LoginCourierResponse.class);
        serviceSteps.sendDeleteRequest("/api/v1/courier/"+loginResponse.getId());
    }

    static Stream<Arguments> loginTestData() {
        return Stream.of(
                Arguments.of("{\"login\":\"oddinson_courier\", \"password\":\"\", \"firstName\":\"Тор\"}", "пароля"),
                Arguments.of("{\"login\":\"\", \"password\":\"Qwerty123@\",\"firstName\":\"Тор\"}", "логина")
        );
    }

}
