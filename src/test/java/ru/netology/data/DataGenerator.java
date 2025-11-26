package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private DataGenerator() {

    }

    private static final Faker faker = new Faker(new Locale("ru"));
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Random random = new Random();

    public static String generateDate(int shiftDays) {
        return LocalDate.now().plusDays(shiftDays).format(formatter);
    }


    private static final List<String> cities = List.of(
            "Москва", "Санкт-Петербург", "Казань", "Новосибирск", "Нижний Новгород",
            "Екатеринбург", "Ростов-на-Дону", "Челябинск", "Самара", "Уфа"
    );

    public static String generateCity() {
        return cities.get(random.nextInt(cities.size()));
    }

    public static String generateName() {
        // ФИО в формате "Фамилия Имя"
        var lastName = faker.name().lastName();
        var firstName = faker.name().firstName();
        return lastName + " " + firstName;
    }

    public static String generatePhone() {
        return faker.phoneNumber().phoneNumber(); // даст российский формат
    }

    public static RegistrationInfo generateUser() {
        return new RegistrationInfo(
                generateCity(),
                generateName(),
                generatePhone()
        );
    }

    @Value
    public static class RegistrationInfo {
        String city;
        String name;
        String phone;
    }
}
