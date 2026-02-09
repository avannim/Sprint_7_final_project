import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class GetOrderListTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
