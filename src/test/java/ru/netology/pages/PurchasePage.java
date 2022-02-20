package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class PurchasePage {

    private final SelenideElement numberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final SelenideElement holderField = $x("//span[text()='Владелец']/parent::span/span/input");
    private final SelenideElement cvcCodeField = $("input[placeholder='999']");

    private final SelenideElement emptyField = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement incorrectFormat = $(byText("Неверный формат"));
    private final SelenideElement invalidCardExpirationDate = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement expiredDatePass = $(byText("Истёк срок действия карты"));
    private final SelenideElement successResult = $(byText("Операция одобрена Банком."));
    private final SelenideElement failureResult = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement continueButton = $$("button").find(exactText("Продолжить"));

    public void completedPurchaseForm(DataHelper.CardData cardData) {
        numberField.setValue(cardData.getNumber());
        monthField.setValue(cardData.getMonth());
        yearField.setValue(cardData.getYear());
        holderField.setValue(cardData.getHolder());
        cvcCodeField.setValue(cardData.getCvc());
        continueButton.click();
    }

    public void emptyFieldError() {
        emptyField.shouldBe(Condition.visible);
    }

    public void incorrectFormatError() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void invalidCardExpirationDateError() {
        invalidCardExpirationDate.shouldBe(Condition.visible);
    }

    public void expiredDatePassError() {
        expiredDatePass.shouldBe(Condition.visible);
    }

    public void successResultNotification() {
        successResult.shouldHave(Condition.visible, Duration.ofSeconds(20));
    }

    public void failureResultNotification() {
        failureResult.shouldHave(Condition.visible, Duration.ofSeconds(20));
    }
}
