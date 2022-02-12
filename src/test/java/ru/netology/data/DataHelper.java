package ru.netology.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Locale;

@NoArgsConstructor
public class DataHelper {

    public static String approvedNumber = "4444 4444 4444 4441";
    public static String declinedNumber = "4444 4444 4444 4442";
    public static String notExistNumber = "4444 4444 4444 1234";
    public static String fakerNumber = new Faker(new Locale("en-US")).finance().creditCard();
    public static Faker fakerEN = new Faker(new Locale("en-US"));
    public static Faker fakerRU = new Faker(new Locale("ru-RU"));

    @Data
    @AllArgsConstructor
    public static class CardData {
        String number, month, year, holder, cvc;
    }

    // Генерация данных по картам
    public static CardData getCardDataEn(String number) {
        String month = String.format("%2d", fakerEN.number().numberBetween(1, 12)).replace(" ", "0");
        int numberYear = Calendar.getInstance().get(Calendar.YEAR);
        String year = Integer.toString(fakerEN.number().numberBetween(numberYear + 1, numberYear + 2)).substring(2);
        String holder = fakerEN.name().firstName() + " " + fakerEN.name().lastName();
        String cvc = fakerEN.numerify("###");
        return new CardData(number, month, year, holder, cvc);
    }

    // Данные валидной карты
    public static CardData getApprovedNumber() {
        return getCardDataEn(approvedNumber);
    }

    // Данные невалидной карты
    public static CardData getDeclinedNumber() {
        return getCardDataEn(declinedNumber);
    }

    // Пустое поле Номер карты
    public static CardData getEmptyNumber() {
        return getCardDataEn("");
    }

    // Ввод в поле Номер карты карты с недостаточным количеством цифр (минимальное количество цифр в карте 13)
    public static CardData getNumberIfFewDigits() {
        return getCardDataEn("card" + approvedNumber.substring(3));
    }

    // Несуществующий в БД номер карты
    public static CardData getNumberIfNotExistInBase() {
        return getCardDataEn(notExistNumber);
    }

    // Генерация разных типов карт
    public static CardData getNumberFaker() {
        return getCardDataEn(fakerNumber);
    }

    // Пустое поле Месяц
    public static CardData getEmptyMonth() {
        CardData card = getCardDataEn(approvedNumber);
        card.setMonth("");
        return card;
    }

    // Ввод в поле Месяц нулевых значений
    public static CardData getMonthWithZero() {
        CardData card = getCardDataEn(approvedNumber);
        card.setMonth(("00"));
        return card;
    }

    // Ввод в поле Месяц значения больше 12
    public static CardData getMonthMore12() {
        CardData card = getCardDataEn(approvedNumber);
        card.setMonth(Integer.toString(fakerEN.number().numberBetween(13, 99)));
        return card;
    }

    // Ввод в поле Месяц одной цифры
    public static CardData getMonthWithOneDigit() {
        CardData card = getCardDataEn(approvedNumber);
        card.setMonth(Integer.toString(fakerEN.number().numberBetween(1, 9)));
        return card;
    }

    // Пустое поле Год
    public static CardData getEmptyYear() {
        CardData card = getCardDataEn(approvedNumber);
        card.setYear("");
        return card;
    }

    // Ввод в поле Год значения намного позднее текущего года
    public static CardData getInvalidYearIfInTheFarFuture() {
        CardData card = getCardDataEn(approvedNumber);
        int numberYear = Calendar.getInstance().get(Calendar.YEAR) % 1000;
        card.setYear(Integer.toString(fakerEN.number().numberBetween(numberYear + 6, 99)));
        return card;
    }

    // Ввод в поле Год 1 цифры
    public static CardData getYearWithOneDigit() {
        CardData card = getCardDataEn(approvedNumber);
        card.setYear(Integer.toString(fakerEN.number().numberBetween(1, 9)));
        return card;
    }

    // Ввод в Поле Год нулевых значений
    public static CardData getYearWithZero() {
        CardData card = getCardDataEn(approvedNumber);
        card.setYear("00");
        return card;
    }

    // Истек срок действия карты
    public static CardData getExpiredCard() {
        CardData card = getCardDataEn(approvedNumber);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -0);
        int numberYear = calendar.get(Calendar.YEAR) % 1000;
        card.setYear(Integer.toString(numberYear));
        int numberMonth = calendar.get(Calendar.MONTH);
        card.setMonth(String.format("%2d", numberMonth).replace(" ", "0"));
        return card;
    }

    // Пустое поле Владелец
    public static CardData getEmptyHolder() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder("");
        return card;
    }

    // Ввод в поле Владелец только фамилии
    public static CardData getHolderWithoutName() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder(fakerEN.name().lastName());
        return card;
    }

    // Ввод в поле Владелец большого количества проблелов между фамилией и именем
    public static CardData getHolderWithManySpaces() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder(fakerEN.name().firstName() + "        " + fakerEN.name().lastName());
        return card;
    }

    // Ввод в поле Владелец цифр
    public static CardData getDigitsInHolder() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder(fakerEN.numerify("####################"));
        return card;
    }

    // Направление заявки с указанием в поле Владелец фамилии и имени на кириллице
    public static CardData getRussianHolder() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder(fakerRU.name().firstName() + " " + fakerRU.name().lastName());
        return card;
    }

    // Направление заявки с указанием в поле Владелец большого количества букв
    public static CardData getHolderWithManyLetters() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        return card;
    }

    // Ввод в поле Владелец спецсимволов
    public static CardData getSpecialCharactersInHolder() {
        CardData card = getCardDataEn(approvedNumber);
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-US"), new RandomService());
        card.setHolder(fakeValuesService.regexify("[\\-\\=\\+\\<\\>\\!\\@\\#\\$\\%\\^\\{\\}]{1,10}"));
        return card;
    }

    // Ввод в поле Владелец фамилии через дефис
    public static CardData getHolderSurnameWithDash() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder(fakerEN.name().firstName() + " " + fakerEN.name().lastName() + "-" + fakerEN.name().lastName());
        return card;
    }

    // Ввод в поле Владелец имени через дефис
    public static CardData getHolderNameWithDash() {
        CardData card = getCardDataEn(approvedNumber);
        card.setHolder(fakerEN.name().firstName() + "-" + fakerEN.name().firstName() + " " + fakerEN.name().lastName());
        return card;
    }

    // Пустое поле CVC-код
    public static CardData getEmptyCvcCode() {
        CardData card = getCardDataEn(approvedNumber);
        card.setCvc("");
        return card;
    }

    // Поле CVC-код с двумя цифрами
    public static CardData getCvcCodeWithTwoDigits() {
        CardData card = getCardDataEn(approvedNumber);
        card.setCvc(fakerEN.number().digits(2) + "w");
        return card;
    }

    // Поле CVC-код с нулевыми значениями
    public static CardData getCvcCodeWithZero() {
        CardData card = getCardDataEn(approvedNumber);
        card.setCvc("000");
        return card;
    }

    // Все поля формы пустые
    public static CardData getCardDataIfEmptyAllFields(){
        return new CardData("", "", "", "", "");
    }

}
