package ru.netology.tests;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.APIHelper.*;
import static ru.netology.data.DataHelper.getApprovedNumber;
import static ru.netology.data.DataHelper.getDeclinedNumber;

public class APITest {

    @DisplayName("Оплата валидной картой, статус карты APPROVED")
    @Test
    void shouldStatusPayWithValidApprovedCardNumber() {
        val validApprovedCardNumber = getApprovedNumber();
        val status = payWithCardForm(validApprovedCardNumber);
        assertTrue(status.contains("APPROVED"));
    }

    @DisplayName("Оплата невалидной картой, статус карты DECLINED")
    @Test
    void shouldStatusPayWithValidDeclinedCardNumber() {
        val validDeclinedCardNumber = getDeclinedNumber();
        val status = payWithCardForm(validDeclinedCardNumber);
        assertTrue(status.contains("DECLINED"));
    }

    @DisplayName("Оплата валидной картой в кредит, статус карты APPROVED")
    @Test
    void shouldStatusPayWithCreditValidApprovedCardNumber() {
        val validApprovedCardNumber = getApprovedNumber();
        val status = payWithCreditForm(validApprovedCardNumber);
        assertTrue(status.contains("APPROVED"));
    }

    @DisplayName("Оплата невалидной картой в кредит, статус карты DECLINED")
    @Test
    void shouldStatusPayWithCreditValidDeclinedCardNumber() {
        val validDeclinedCardNumber = getDeclinedNumber();
        val status = payWithCreditForm(validDeclinedCardNumber);
        assertTrue(status.contains("DECLINED"));
    }
}
