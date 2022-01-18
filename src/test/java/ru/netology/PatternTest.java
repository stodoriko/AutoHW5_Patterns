package ru.netology;

import com.codeborne.selenide.conditions.Text;
import com.github.javafaker.Faker;
import entities.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import utils.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class PatternTest {

    private static Faker faker;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldAcceptsRescheduleDelivery() {
        UserInfo userInfo = DataGenerator.Registration.generateInfo("ru");
        String city =  userInfo.getCity();
        String fullName = userInfo.getFullName();
        String phoneNumber = userInfo.getPhoneNumber();

        // выбираем город
        $(By.xpath("//input[contains(@placeholder, 'Город')]")).setValue(city);
        // Дата - не ранее трёх дней с текущей даты
        String date = DataGenerator.Registration.setDate(3);
        $(By.xpath("//input[contains(@placeholder, 'Дата встречи')]")).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(By.xpath("//input[contains(@placeholder, 'Дата встречи')]")).setValue(date);
        // Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
        $("span[data-test-id='name'] input").setValue(fullName);
        // Поле телефон - только цифры (11 цифр), символ + (на первом месте)
        $("span[data-test-id='phone'] input").setValue(phoneNumber);
        // Флажок согласия должен быть выставлен
        $("label[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("div.notification__content").shouldBe(Text.text("Встреча успешно запланирована на " + date)).shouldBe(visible);
        $(By.xpath("//div[contains(@class, 'notification_status_ok')]//button[contains(@class, 'notification__closer')]")).click();


        $(By.xpath("//input[contains(@placeholder, 'Город')]")).sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
        $(By.xpath("//input[contains(@placeholder, 'Город')]")).setValue(city);
        String newDate = DataGenerator.Registration.setDate(4);
        $(By.xpath("//input[contains(@placeholder, 'Дата встречи')]")).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(By.xpath("//input[contains(@placeholder, 'Дата встречи')]")).setValue(newDate);
        $("span[data-test-id='phone'] input").sendKeys(Keys.LEFT_CONTROL + "a" + Keys.BACK_SPACE);
        $("span[data-test-id='phone'] input").setValue(phoneNumber);
        $$("button").find(exactText("Запланировать")).click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(3));
        $("div.notification_status_error").shouldBe(Text.text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $$("button").find(exactText("Перепланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("div.notification__content").shouldBe(Text.text("Встреча успешно запланирована на " + newDate)).shouldBe(visible);
    }

}
