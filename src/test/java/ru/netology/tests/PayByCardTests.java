package ru.netology.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.pages.MainPage;
import ru.netology.pages.PurchasePage;

import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.*;

//gradle test -Ddb.url=jdbc:mysql://localhost:3306/app allureReport
//gradle test -Ddb.url=jdbc:mysql://localhost:3306/app allureServe
//java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar artifacts/aqa-shop.jar
//java -Dspring.datasource-postgresql.url=jdbc:postgresql://localhost:5432/app -jar artifacts/aqa-shop.jar
//gradlew test -Ddb.url=jdbc:postgresql://localhost:5432/app allureServe

public class PayByCardTests extends BaseUITest {

    MainPage mainPage = new MainPage();
    PurchasePage purchasePage = new PurchasePage();

    @BeforeEach
    void setUpForPayWithCard() {
        mainPage.payWithCard();
    }

    // (тест прошел)
    @DisplayName("Успешная покупка, карта со статусом APPROVED")
    @Test
    public void shouldSuccessPayIfValidApprovedCard() {
        val cardData = getApprovedNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();

        val expectedStatus = "APPROVED";
        val actualStatus = getCardStatusForPayWithCard();
        assertEquals(expectedStatus, actualStatus);

        val expectedAmount = "4500000";
        val actualAmount = getAmountPurchase();
        assertEquals(expectedAmount, actualAmount);

        val transactionIdExpected = getTransactionId();
        val paymentIdActual = getPaymentIdForPayWithCard();
        assertNotNull(transactionIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(transactionIdExpected, paymentIdActual);
    }

    // (тест не прошел, оплата успешная)
    @DisplayName("Неуспешная покупка, карта  со статусом DECLINED")
    @Test
    public void shouldFailurePayIfValidDeclinedCard() {
        val cardData = getDeclinedNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();

        val expectedStatus = "DECLINED";
        val actualStatus = getCardStatusForPayWithCard();
        assertEquals(expectedStatus, actualStatus);

        val transactionIdExpected = getTransactionId();
        val paymentIdActual = getPaymentIdForPayWithCard();
        assertNotNull(transactionIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(transactionIdExpected, paymentIdActual);
    }

    // (тест не прошел, появляется ошибка 'Неверный формат' вместо 'Поле обязательно для заполнения')
    @DisplayName("Пустое поле Номер карты")
    @Test
    public void shouldHaveEmptyNumber() {
        val cardData = getEmptyNumber();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    //(тест прошел, но лучше"указывать ошибку 'Указано недостаточно цифр')
    @DisplayName("Ввод в поле Номер карты недостаточного количества цифр")
    @Test
    public void shouldHaveNumberIfFewDigits() {
        val cardData = getNumberIfFewDigits();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    @DisplayName("Оплата картой, которой нет в БД (тест прошел)")
    @Test
    public void shouldHaveNumberIfOutOfBase() {
        val cardData = getNumberIfNotExistInBase();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();
    }

    @DisplayName("Оплата картой разных форматов, которых нет в БД")
    @Test
    public void shouldHaveNumberIfFakerCard() {
        val cardData = getNumberFaker();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.failureResultNotification();
    }

    @DisplayName("Пустое поле Месяц")
    @Test
    public void shouldHaveEmptyMonth() {
        val cardData = getEmptyMonth();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    //  (тест не прошел, оплата успешная)
    @DisplayName("Ввод в поле Месяц нулевых значений")
    @Test
    public void shouldHaveMonthWithZero() {
        val cardData = getMonthWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    //  (тест прошел, но в качестве пожелания лучше указать ошибку "Введите срок
    // действия как указано на карте")
    @DisplayName("Ввод в поле Месяц значения больше 12")
    @Test
    public void shouldHaveMonthMore12() {
        val cardData = getMonthMore12();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    //  (тест прошел)
    @DisplayName("Ввод в поле Месяц 1 цифры")
    @Test
    public void shouldHaveMonthWithOneDigit() {
        val cardData = getMonthWithOneDigit();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест прошел)
    @DisplayName("Пустое поле Год")
    @Test
    public void shouldHaveEmptyYear() {
        val cardData = getEmptyYear();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    // Истек срок действия карты (тест прошел)
    @DisplayName("Истек срок действия карты")
    @Test
    public void shouldHaveYearBeforeCurrentYear() {
        val cardData = getExpiredCard();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.expiredDatePassError();
    }

    //  (тест прошел)
    @DisplayName("Год намного позднее текущего")
    @Test
    public void shouldHaveYearInTheFarFuture() {
        val cardData = getInvalidYearIfInTheFarFuture();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.invalidCardExpirationDateError();
    }

    //  (тест прошел)
    @DisplayName("Поле Год с одной цифрой")
    @Test
    public void shouldHaveYearWithOneDigit() {
        val cardData = getYearWithOneDigit();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест прошел)
    @DisplayName("Поле Год с нулевыми значениями")
    @Test
    public void shouldHaveYearWithZero() {
        val cardData = getYearWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.expiredDatePassError();
    }

    //  (тест прошел)
    @DisplayName("Пустое поле Владелец")
    @Test
    public void shouldHaveEmptyHolder() {
        val cardData = getEmptyHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.emptyFieldError();
    }

    //  (тест не прошел, оплата успешная)
    @DisplayName("Ввод в поле Владелец только фамилии")
    @Test
    public void shouldHaveHolderWithoutName() {
        val cardData = getHolderWithoutName();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, оплата успешная)
    @DisplayName("Указание Владельца кириллицей")
    @Test
    public void shouldHaveRussianHolder() {
        val cardData = getRussianHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, оплата успешная)
    @DisplayName("Ввод в поле Владелец цифр")
    @Test
    public void shouldHaveDigitsInHolder() {
        val cardData = getDigitsInHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, оплата успешная)
    @DisplayName("Поле Владелец с указанием спецсимволов")
    @Test
    public void shouldHaveSpecialCharactersInHolder() {
        val cardData = getSpecialCharactersInHolder();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, оплата успешная.
    // Необходима доработка в виде появляющегося предупреждения "Допустим только один пробел между именем и фамилией")
    @DisplayName("Ввод в поле Владелец большого количества пробелов между фамилией и именем")
    @Test
    public void shouldHaveHolderWithManySpaces() {
        val cardData = getHolderWithManySpaces();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, оплата успешная. Необходима доработка в виде
    // появляющегося предупреждения "Допустимо не более ** символов")
    @DisplayName("Ввод в поле Владелец большого количества символов")
    @Test
    public void shouldHaveHolderWithManyLetters() {
        val cardData = getHolderWithManyLetters();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест прошел)
    @DisplayName("Указание в поле Владелец фамилии через дефис")
    @Test
    public void shouldHaveHolderSurnameWithDash() {
        val cardData = getHolderSurnameWithDash();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();
    }

    //  (тест прошел)
    @DisplayName("Указание в поле Владелец имени через дефис")
    @Test
    public void shouldHaveHolderNameWithDash() {
        val cardData = getHolderNameWithDash();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.successResultNotification();
    }

    //  (тест не прошел, ошибка появляется под полем Владелец)
    @DisplayName("Пустое поле CVC-код")
    @Test
    public void shouldHaveEmptyCvcCode() {
        val cardData = getEmptyCvcCode();
        purchasePage.completedPurchaseForm(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cvvFieldSub = fieldSub.get(2);
        cvvFieldSub.shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    //  с 2 цифрами (тест прошел, но необходима доработка в виде сообщения "Поле должно состоять из 3 цифр")
    @DisplayName("Поле CVC-код")
    @Test
    public void shouldHaveCvcCodeWithTwoDigits() {
        val cardData = getCvcCodeWithTwoDigits();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, оплата успешная)
    @DisplayName("Поле CVC-код с нулевыми значениями")
    @Test
    public void shouldHaveCvcCodeWithZero() {
        val cardData = getCvcCodeWithZero();
        purchasePage.completedPurchaseForm(cardData);
        purchasePage.incorrectFormatError();
    }

    //  (тест не прошел, ошибка "Неверный формат" во всех полях кроме поля Владелец)
    @DisplayName("Все поля формы пустые")
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