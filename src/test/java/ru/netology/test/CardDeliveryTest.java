package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.holdBrowserOpen = false;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void openPage() {
        open("http://localhost:8080");
    }

    @Test
    void shouldPlanAndReplanMeeting() {
        // данные пользователя из генератора
        var user = DataGenerator.generateUser();
        String firstMeetingDate = DataGenerator.generateDate(4);
        String secondMeetingDate = DataGenerator.generateDate(7);

        // заполняем форму
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Запланировать")).click();

        // проверяем уведомление об успешном планировании
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));

        // меняем дату
        $("[data-test-id=date] input").doubleClick().sendKeys(secondMeetingDate);
        $$("button").find(Condition.exactText("Запланировать")).click();

        // появилось модальное окно с подтверждением
        $("[data-test-id=replan-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату"));

        // нажимаем «Перепланировать»
        $("[data-test-id=replan-notification] button")
                .shouldHave(Condition.exactText("Перепланировать"))
                .click();

        // проверяем итоговое уведомление
        $("[data-test-id=success-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}

