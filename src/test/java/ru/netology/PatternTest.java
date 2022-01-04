package ru.netology;

import com.codeborne.selenide.conditions.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class PatternTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldAcceptsDelivery() {
        // выбираем город
        String neededCity = "Москва";
        $(By.xpath("//*[contains(@placeholder, 'Город')]")).setValue(neededCity);

        // Дата - не ранее трёх дней с текущей даты
        String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(By.xpath("//*[contains(@placeholder, 'Дата встречи')]")).setValue(date);

        // Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
        $("span[data-test-id='name'] input").setValue("Тодорико Сергей");

        // Поле телефон - только цифры (11 цифр), символ + (на первом месте)
        $("span[data-test-id='phone'] input").setValue("+79012345678");

        // Флажок согласия должен быть выставлен
        $("label[data-test-id='agreement']").click();

        $$("button").find(exactText("Забронировать")).click();

        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("div.notification__content").shouldBe(Text.text("Встреча успешно забронирована на " + date)).shouldBe(visible);

    }

}
