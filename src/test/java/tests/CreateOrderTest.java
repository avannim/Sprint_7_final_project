package tests;

import com.google.gson.Gson;
import dto.request.CreateOrderRequest;
import dto.response.CreateOrderResponse;
import dto.response.OkResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.ServiceSteps;


import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateOrderTest {

    private final ServiceSteps serviceSteps = new ServiceSteps();
    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @ParameterizedTest
    @MethodSource("orderTestData")
    @DisplayName("Тест успешного создания курьера")
    public void  checkCreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone,
                                      int rentTime, int deliveryDate, String comment, List<String> color){
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(firstName, lastName, address, metroStation, phone,
        rentTime, serviceSteps.createDate(deliveryDate), comment, color);
        String body = gson.toJson(createOrderRequest);
        Response response = serviceSteps.sendPostRequest("/api/v1/orders", body);
        serviceSteps.checkRequestStatus(response, 201);
        CreateOrderResponse createOrderResponse= gson.fromJson(response.body().asString(), CreateOrderResponse.class);
        assertNotNull(createOrderResponse.getTrack());
    }


    static Stream<Arguments> orderTestData() {
        return Stream.of(
                Arguments.of("Евгений", "Петров", "г. Москва, ул. Петрозаводская, д.1", 16, "+79996352519", 1, 5,"Позвоните когда подъедите", List.of("BLACK", "GRAY")),
                Arguments.of("Елена", "Журчалко", "г. Москва, проспект Жуковского, д.123", 5, "+79203958642", 6, 3,"Получать будет Марина", List.of("BLACK")),
                Arguments.of("Мефистея", "Генералова", "г. Москва, Органный переулок, д.4, корп. 1", 28, "+79065986235", 5, 0,"Везите быстрее", List.of("GRAY")),
                Arguments.of("Георгий", "Мкртчан", "г. Москва, ул. Краснознаменная, д.56", 7, "+79915638213", 2, 1,"Только попробуйте опоздать", List.of())
        );
    }
}
