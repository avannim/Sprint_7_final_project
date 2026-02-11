package dto.response;

public class LoginCourierResponse {

    private int id;

    public LoginCourierResponse(int id) {
        this.id = id;
    }

    public LoginCourierResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
