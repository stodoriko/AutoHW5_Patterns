package ru.netology;

import com.codeborne.selenide.conditions.Text;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class PatternTest {

    private static Faker faker;

    @BeforeAll
    static void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }
    String neededCity = faker.address().city();
    String fullName = faker.name().fullName();
    String phone = faker.phoneNumber().phoneNumber();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldAcceptsDelivery() {
        // выбираем город
        $(By.xpath("//*[contains(@placeholder, 'Город')]")).setValue(neededCity);

        // Дата - не ранее трёх дней с текущей даты
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).setValue(date);

        // Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
        $("span[data-test-id='name'] input").setValue(fullName);

        // Поле телефон - только цифры (11 цифр), символ + (на первом месте)
        $("span[data-test-id='phone'] input").setValue(phone);

        // Флажок согласия должен быть выставлен
        $("label[data-test-id='agreement']").click();

        $$("button").find(exactText("Запланировать")).click();

        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("div.notification__content").shouldBe(Text.text("Встреча успешно запланирована на " + date)).shouldBe(visible);
    }


    @Test
    void shouldAcceptsRescheduleDelivery() {
        // выбираем город
        $(By.xpath("//*[contains(@placeholder, 'Город')]")).setValue(neededCity);

        // Дата - не ранее трёх дней с текущей даты
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).setValue(date);

        // Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
        $("span[data-test-id='name'] input").setValue(fullName);

        // Поле телефон - только цифры (11 цифр), символ + (на первом месте)
        $("span[data-test-id='phone'] input").setValue(phone);

        // Флажок согласия должен быть выставлен
        $("label[data-test-id='agreement']").click();

        $$("button").find(exactText("Запланировать")).click();

        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("div.notification__content").shouldBe(Text.text("Встреча успешно запланирована на " + date)).shouldBe(visible);
        $(By.xpath("//div[contains(@class, 'notification_status_ok')]//button[contains(@class, 'notification__closer')]")).click();


        String newDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).setValue(newDate);
        $$("button").find(exactText("Запланировать")).click();
        $(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(1));
        $("div.notification_status_error").shouldBe(Text.text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $$("button").find(exactText("Перепланировать")).click();

    }

}
