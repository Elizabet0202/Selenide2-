package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator2;
import ru.netology.data.DataGenerator2.User;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    // 1. Успешный вход с активным зарегистрированным пользователем
    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        User user = DataGenerator2.getRegisteredActiveUser();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        // здесь подставь селектор/текст того, что видно на успешной странице
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    // 2. Ошибка для заблокированного пользователя
    @Test
    void shouldShowErrorForBlockedUser() {
        User user = DataGenerator2.getRegisteredBlockedUser();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    // 3. Ошибка для незарегистрированного пользователя
    @Test
    void shouldShowErrorForNotRegisteredUser() {
        User user = DataGenerator2.getNotRegisteredUser();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    // 4. Ошибка при неверном пароле
    @Test
    void shouldShowErrorForWrongPassword() {
        User user = DataGenerator2.getUserWithWrongPassword();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    // 5. Ошибка при неверном логине
    @Test
    void shouldShowErrorForWrongLogin() {
        User user = DataGenerator2.getUserWithWrongLogin();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}

