package utils;

import com.github.javafaker.Faker;
import entities.UserInfo;

import java.util.Locale;
import java.util.Random;


public class DataGenerator {
    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateInfo(String locale) {
            Faker faker = new Faker(new Locale(locale));
            String[] cities = {"Москва", "Хабаровск", "Екатеринбург", "Новосибирск", "Курск", "Майкоп",
                    "Горно-Алтайск", "Уфа", "Улан-Удэ", "Махачкала", "Магас", "Нальчик", "Элиста",
                    "Черкесск", "Петрозаводск", "Сыктывкар", "Симферополь", "Йошкар-Ола", "Саранск", "Якутск",
                    "Владикавказ", "Казань", "Кызыл", "Ижевск", "Абакан", "Грозный", "Чебоксары", "Барнаул", "Чита",
                    "Петропавловск-Камчатский", "Краснодар", "Красноярск", "Пермь", "Владивосток", "Ставрополь",
                    "Хабаровск", "Благовещенск"};
            Random rand = new Random();

            return new UserInfo(cities[rand.nextInt(cities.length)],
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber()
            );
        }
    }
}

