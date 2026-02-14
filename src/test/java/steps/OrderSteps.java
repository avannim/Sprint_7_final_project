package steps;

import com.google.gson.Gson;
import dto.response.GetOrderListResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderSteps {

    @Step("Преобразовать JSON ответ в объект GetOrderListResponse")
    public GetOrderListResponse deserializeResponseToGetOrderList (Response response){
        Gson gson = new Gson();
        GetOrderListResponse getOrderList;
        getOrderList = gson.fromJson(response.body().asString(), GetOrderListResponse.class);
        return getOrderList;
    }
}
