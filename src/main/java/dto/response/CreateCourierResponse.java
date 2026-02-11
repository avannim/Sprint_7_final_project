package dto.response;

public class CreateCourierResponse {

    boolean ok;

    public CreateCourierResponse(boolean ok) {
        this.ok = ok;
    }

    public CreateCourierResponse() {
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
