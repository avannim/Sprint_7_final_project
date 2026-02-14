package tests;

import com.google.gson.Gson;
import dto.request.CreateCourierRequest;
import dto.request.LoginRequest;
import dto.response.OkResponse;
import dto.response.ErrorResponse;
import dto.response.LoginCourierResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.ServiceSteps;

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
    public void  checkDeleteCourierTest(){
        CreateCourierRequest newCourier = new CreateCourierRequest("oddinson_courier", "Qwerty123@", "Тор");
        String body = gson.toJson(newCourier);
        Response response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
        LoginRequest loginRequest = new LoginRequest(newCourier.getLogin(), newCourier.getPassword());
        String body2 = gson.toJson(loginRequest);
        response = serviceSteps.sendPostRequest("/api/v1/courier/login", body2);
        serviceSteps.checkRequestStatus(response, 200);
        LoginCourierResponse loginCourierResponse = gson.fromJson(response.body().asString(), LoginCourierResponse.class);
        response = serviceSteps.sendDeleteRequest("/api/v1/courier/"+loginCourierResponse.getId());
        serviceSteps.checkRequestStatus(response, 200);
        OkResponse okResponse = gson.fromJson(response.body().asString(), OkResponse.class);
        assertTrue(okResponse.isOk());
    }

    @Test
    @DisplayName("Тест удаления курьера без указания id")
    public void  checkDeleteCourierWithoutIdTest(){
        Response response = serviceSteps.sendDeleteRequest("/api/v1/courier/");
        serviceSteps.checkRequestStatus(response, 400);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Недостаточно данных для удаления курьера", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Тест удаления курьера с указанием несуществующего d")
    public void  checkDeleteCourierWithWrongIdTest(){
        Response response = serviceSteps.sendDeleteRequest("/api/v1/courier/6543252");
        serviceSteps.checkRequestStatus(response, 404);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Курьера с таким id нет", errorResponse.getMessage());
    }

}
