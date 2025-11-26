package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator2 {
    private DataGenerator2() {
    }

    private static final Faker faker = new Faker(new Locale("ru"));

    // общая спецификация для Rest-Assured
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Value
    public static class User {
        String login;
        String password;
        String status;
    }

    private static String randomLogin() {
        return faker.name().username();
    }

    private static String randomPassword() {
        return faker.internet().password();
    }

    // просто сгенерировать юзера (без регистрации в системе)
    public static User generateUser(String status) {
        return new User(
                randomLogin(),
                randomPassword(),
                status
        );
    }

    // зарегистрировать пользователя через API
    private static void registerUser(User user) {
        given()
                .spec(requestSpec)
                .body(new UserDto(user.getLogin(), user.getPassword(), user.getStatus()))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // === Готовые методы для тестов ===

    // 1) Зарегистрированный активный пользователь
    public static User getRegisteredActiveUser() {
        var user = generateUser("active");
        registerUser(user);
        return user;
    }

    // 2) Зарегистрированный заблокированный пользователь
    public static User getRegisteredBlockedUser() {
        var user = generateUser("blocked");
        registerUser(user);
        return user;
    }

    // 3) Незарегистрированный пользователь
    public static User getNotRegisteredUser() {
        return generateUser("active"); // просто НЕ вызываем registerUser
    }

    // 4) Пользователь с неправильным паролем (логин есть в базе)
    public static User getUserWithWrongPassword() {
        var registered = getRegisteredActiveUser();
        return new User(
                registered.getLogin(),
                randomPassword(), // другой пароль
                registered.getStatus()
        );
    }

    // 5) Пользователь с неправильным логином (пароль правильный от созданного)
    public static User getUserWithWrongLogin() {
        var registered = getRegisteredActiveUser();
        return new User(
                randomLogin(),       // другой логин
                registered.getPassword(),
                registered.getStatus()
        );
    }
}

