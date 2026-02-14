package dto.response;

public class CreateOrderResponse {
    private int track;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(int track) {
        this.track = track;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }
}
