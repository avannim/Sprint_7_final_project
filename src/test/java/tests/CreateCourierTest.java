package tests;

import com.google.gson.Gson;
import dto.request.CreateCourierRequest;
import dto.request.LoginRequest;
import dto.response.CreateCourierResponse;
import dto.response.ErrorResponse;
import dto.response.LoginCourierResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.ServiceSteps;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        CreateCourierResponse createResponse = gson.fromJson(response.body().asString(), CreateCourierResponse.class);
        assertTrue(createResponse.isOk());
    }

    @Test
    @DisplayName("Тест создания курьера c логином который уже есть")
    public void  checkDublicateCourierTest(){
        String body = gson.toJson(newCourier);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
        Response response2 = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response2, 409);
        ErrorResponse createResponse = gson.fromJson(response2.body().asString(), ErrorResponse.class);
        assertEquals("Этот логин уже используется", createResponse.getMessage(), String.format("Сервер вернул не корректное сообщение об ошибке: \"%s\"", createResponse.getMessage()));
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

}
