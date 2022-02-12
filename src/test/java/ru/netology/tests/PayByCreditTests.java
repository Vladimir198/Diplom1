package ru.netology.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pages.MainPage;
import ru.netology.pages.PurchasePage;

import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;
import static ru.netology.data.SQLHelper.getBankId;

public class PayByCreditTests extends BaseUITest {

    MainPage mainPage = new MainPage();
    PurchasePage purchasePage = new PurchasePage();

    @BeforeEach
    void setUpForPayWithCard() {
        mainPage.payWithCredit();
    }

    // Успешная покупка тура за счет кредитных средств, карта со статусом APPROVED (тест прошел)
    @Test
    public void shouldSuccessCreditRequestIfValidApprovedCard() {
        val cardData = getApprovedNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();

        val expectedStatus = "APPROVED";
        val actualStatus = getCardStatusForPayWithCredit();
        assertEquals(expectedStatus, actualStatus);

        val bankIdExpected = getBankId();
        val paymentIdActual = getPaymentIdForPayWithCredit();
        assertNotNull(bankIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(bankIdExpected, paymentIdActual);
    }

    // Нуспешная покупка за счет кредитных средств. карта со статусом DECLINED (тест не прошел,оплата успешная)
    @Test
    public void shouldFailureCreditRequestIfValidDeclinedCard() {
        val cardData = getDeclinedNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();

        val expectedStatus = "DECLINED";
        val actualStatus = getCardStatusForPayWithCredit();
        assertEquals(expectedStatus, actualStatus);

        val bankIdExpected = getBankId();
        val paymentIdActual = getPaymentIdForPayWithCredit();
        assertNotNull(bankIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(bankIdExpected, paymentIdActual);
    }

    // Пустое поле Номер карты (тест не прошел, появляется ошибка "Неверный формат" вместо "Поле обязательно для
    // заполнения")
    @Test
    public void shouldHaveEmptyNumber() {
        val cardData = getEmptyNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Ввод в поле Номер карты недосточного количества цифр (тест прошел, но лучше указывать ошибку "Указано
    // недостаточно цифр")
    @Test
    public void shouldHaveNumberIfFewDigits() {
        val cardData = getNumberIfFewDigits();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Оплата картой, которой нет в БД (тест прошел)
    @Test
    public void shouldHaveNumberIfOutOfBase() {
        val cardData = getNumberIfNotExistInBase();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();
    }

    // Оплата картой разных форматов,которых нет в БД (тест не проходит, если количество цифр в карте меньше или больше
    // 16, хотя существуют карты от 13 до 19 цифр)
    @Test
    public void shouldHaveNumberIfFakerCard() {
        val cardData = getNumberFaker();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();
    }

    // Пустое поле Месяц (тест не прошел,неверная ошибка "Неверный формат" вместо "Поле обязательно для заполнения")
    @Test
    public void shouldHaveEmptyMonth() {
        val cardData = getEmptyMonth();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Ввод в поле Месяц нулевых значений (тест не прошел, успешная оплата)
    @Test
    public void shouldHaveMonthWithZero() {
        val cardData = getMonthWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц значения больше 12 (тест прошел, но в качестве пожелания лучше указать ошибку "Введиту срок
    // действия как указано на карте")
    @Test
    public void shouldHaveMonthMore12() {
        val cardData = getMonthMore12();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    // Ввод в поле Месяц 1 цифры (тест прошел)
    @Test
    public void shouldHaveMonthWithOneDigit() {
        val cardData = getMonthWithOneDigit();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Пустое поле Год (тест не прошел, ошибка Неверный формат, а не Поле обязательно для заполнения)
    @Test
    public void shouldHaveEmptyYear() {
        val cardData = getEmptyYear();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Истек срок действия карты (тест не прошел, неверная ошибка "Неверно указан срок действия карты" вместо
    // "Истек срок действия карты")
    @Test
    public void shouldHaveYearBeforeCurrentYear() {
        val cardData = getExpiredCard();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.expiredDatePassError();
    }

    // Год намного позднее текущего (тест прошел)
    @Test
    public void shouldHaveYearInTheFarFuture() {
        val cardData = getInvalidYearIfInTheFarFuture();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    // Поле Год с одной цифрой (тест прошел)
    @Test
    public void shouldHaveYearWithOneDigit() {
        val cardData = getYearWithOneDigit();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Поле Год с нулевыми значениями (тест прошел)
    @Test
    public void shouldHaveYearWithZero() {
        val cardData = getYearWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.expiredDatePassError();
    }

    // Пустое поле Владелец (тест прошел)
    @Test
    public void shouldHaveEmptyHolder() {
        val cardData = getEmptyHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Ввод в поле Владелец только фамилии (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveHolderWithoutName() {
        val cardData = getHolderWithoutName();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Указание Владельца кириллицей (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveRussianHolder() {
        val cardData = getRussianHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Ввод в поле Владелец цифр (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveDigitsInHolder() {
        val cardData = getDigitsInHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Поле Владелец с указанием спецсимволов (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveSpecialCharactersInHolder() {
        val cardData = getSpecialCharactersInHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества пробелов между фамилией и именем (тест не прошел, оплата успешная.
    // Необходима доработка в виде появляющейся ошибки "Допустим только один пробел между именем и фамилией")
    @Test
    public void shouldHaveManySpacesInCardHolder() {
        val cardData = getHolderWithManySpaces();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Ввод в поле Владелец большого количества символов (тест не прошел, оплата успешная. Необходима доработка в виде
    // появляющейся ошибки "Допустимо не более ** символов)
    @Test
    public void shouldHaveHolderWithManyLetters() {
        val cardData = getHolderWithManyLetters();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Указание в поле Владелец фамилии через дефис (тест прошел)
    @Test
    public void shouldHaveHolderSurnameWithDash() {
        val cardData = getHolderSurnameWithDash();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();
    }

    // Указание в поле Владелец имени через дефис (тест прошел)
    @Test
    public void shouldHaveHolderNameWithDash() {
        val cardData = getHolderNameWithDash();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();
    }

    // Пустое поле CVC-код (тест не прошел,ошибка появляется под полем Владелец)
    @Test
    public void shouldHaveEmptyCvcCode() {
        val cardData = getEmptyCvcCode();
        purchasePage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cvvFieldSub = fieldSub.get(2);
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    // Поле CVC-код с 2 цифрами (тест прошел, но необходима доработка в виде сообщения "Поле должно состоять из 3 цифр")
    @Test
    public void shouldHaveCvcCodeWithTwoDigits() {
        val cardData = getCvcCodeWithTwoDigits();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Поле CVC-код с нулевыми значениями (тест не прошел, оплата успешная)
    @Test
    public void shouldHaveCvcCodeWithZero() {
        val cardData = getCvcCodeWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    // Все поля формы пустые (тест не прошел, ошибка "Неверный формат" во всех полях кроме поля Владелец)
    @Test
    public void shouldHaveEmptyAllFields() {
        val cardData = getCardDataIfEmptyAllFields();
        purchasePage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cardNumberFieldSub = fieldSub.get(1);
        final SelenideElement monthFieldSub = fieldSub.get(2);
        final SelenideElement yearFieldSub = fieldSub.get(3);
        final SelenideElement holderFieldSub = fieldSub.get(4);
        final SelenideElement cvvFieldSub = fieldSub.get(5);
        cardNumberFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        monthFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        yearFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        holderFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }
}