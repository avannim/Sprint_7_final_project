package dto.response;

public class OkResponse {

    boolean ok;

    public OkResponse(boolean ok) {
        this.ok = ok;
    }

    public OkResponse() {
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
