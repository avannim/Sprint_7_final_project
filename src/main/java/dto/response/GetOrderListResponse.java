package dto.response;

import model.ResponseAvailableStation;
import model.ResponseOrder;
import model.ResponsePageInfo;

import java.util.List;

public class GetOrderListResponse {
    private List<ResponseOrder> orders;

    private List<ResponseAvailableStation> availableStations;

    private ResponsePageInfo pageInfo;

    public List<ResponseOrder> getOrder() {
        return orders;
    }

    public void setOrder(List<ResponseOrder> order) {
        this.orders = order;
    }

    public List<ResponseAvailableStation> getAvailableStation() {
        return availableStations;
    }

    public void setAvailableStation(List<ResponseAvailableStation> availableStation) {
        this.availableStations = availableStation;
    }

    public ResponsePageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(ResponsePageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
