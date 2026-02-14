package tests;

import dto.response.GetOrderListResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

import steps.*;

public class GetOrderListTest {

    private final ServiceSteps serviceSteps = new ServiceSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void  getOrderList(){
        Response response = serviceSteps.sendGetRequest("/api/v1/orders");
        serviceSteps.checkRequestStatus(response, 200);
        GetOrderListResponse getOrderListResponse = orderSteps.deserializeResponseToGetOrderList(response);
        MatcherAssert.assertThat(getOrderListResponse, notNullValue());
    }
}
