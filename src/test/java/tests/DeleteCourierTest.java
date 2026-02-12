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

public class DeleteCourierTest {

    private final Gson gson = new Gson();
    private final ServiceSteps serviceSteps = new ServiceSteps();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест успешного удаления курьера")
    public void  checkCreateCourierTest(){
        CreateCourierRequest newCourier = new CreateCourierRequest("oddinson_courier", "Qwerty123@", "Тор");
        String body = gson.toJson(newCourier);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
        CreateCourierResponse createResponse = gson.fromJson(response.body().asString(), CreateCourierResponse.class);
        assertTrue(createResponse.isOk());
        LoginRequest loginRequest = new LoginRequest(newCourier.getLogin(), newCourier.getPassword());
        String body = gson.toJson(loginRequest);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier/login", body);
        serviceSteps.checkRequestStatus(response, 200);
        LoginCourierResponse loginResponse = gson.fromJson(response.body().asString(), LoginCourierResponse.class);
        serviceSteps.sendDeleteRequest("/api/v1/courier/"+loginResponse.getId());
        serviceSteps.checkRequestStatus(response, 200);
    }

    @Test
    @DisplayName("Тест удаления курьера без указания id")
    public void  checkCreateCourierTest(){
        Response response = serviceSteps.sendDeleteRequest("/api/v1/courier/");
        serviceSteps.checkRequestStatus(response, 400);
        ErrorResponse createResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        assertEquals("Недостаточно данных для удаления курьера", createResponse.getMessage(), String.format("Сервер вернул не корректное сообщение об ошибке: \"%s\"", createResponse.getMessage()));
    }

    @Test
    @DisplayName("Тест удаления курьера с указанием несуществующего d")
    public void  checkCreateCourierTest(){
        Response response = serviceSteps.sendDeleteRequest("/api/v1/courier/6543252");
        serviceSteps.checkRequestStatus(response, 404);
        ErrorResponse createResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        assertEquals("Курьера с таким id нет", createResponse.getMessage(), String.format("Сервер вернул не корректное сообщение об ошибке: \"%s\"", createResponse.getMessage()));
    }

}
