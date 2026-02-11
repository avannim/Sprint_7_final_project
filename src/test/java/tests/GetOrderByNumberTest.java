package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public class GetOrderByNumberTest {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
