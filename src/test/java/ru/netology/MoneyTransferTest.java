package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.MoneyTransferPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;


public class MoneyTransferTest {

    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);

    @BeforeEach
    void setupTest() {
        open("http://localhost:9999");
        new LoginPage()
                .validLogin(authInfo)
                .validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsSecondToFirstCard() {
        var cards = DataHelper.getCardNumbersFor(authInfo);
        int amount = 200;
        DashboardPage.equalizeBalance(authInfo);
        var balanceBeforeFirstCard = DashboardPage.getCardBalance(cards.getFirstCardId());
        var balanceBeforeSecondCard = DashboardPage.getCardBalance(cards.getSecondCardId());
        int balanceAfterFirstCard = balanceBeforeFirstCard + amount;
        int balanceAfterSecondCard = balanceBeforeSecondCard - amount;

        DashboardPage.transfer(cards.getFirstCardId());
        MoneyTransferPage.moneyTransfer(amount, cards.getSecondCardNumber());

        DashboardPage.cardBalance(cards.getFirstCardId())
                .shouldHave(exactText("**** **** **** 0001, баланс: " + balanceAfterFirstCard + " р.\nПополнить"));
        DashboardPage.cardBalance(cards.getSecondCardId())
                .shouldHave(exactText("**** **** **** 0002, баланс: " + balanceAfterSecondCard + " р.\nПополнить"));
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsFirstToSecondCard() {
        var cards = DataHelper.getCardNumbersFor(authInfo);
        int amount = 200;
        DashboardPage.equalizeBalance(authInfo);
        var balanceBeforeFirstCard = DashboardPage.getCardBalance(cards.getFirstCardId());
        var balanceBeforeSecondCard = DashboardPage.getCardBalance(cards.getSecondCardId());
        int balanceAfterFirstCard = balanceBeforeFirstCard - amount;
        int balanceAfterSecondCard = balanceBeforeSecondCard + amount;

        DashboardPage.transfer(cards.getSecondCardId());
        MoneyTransferPage.moneyTransfer(amount, cards.getFirstCardNumber());

        DashboardPage.cardBalance(cards.getFirstCardId())
                .shouldHave(exactText("**** **** **** 0001, баланс: " + balanceAfterFirstCard + " р.\nПополнить"));
        DashboardPage.cardBalance(cards.getSecondCardId())
                .shouldHave(exactText("**** **** **** 0002, баланс: " + balanceAfterSecondCard + " р.\nПополнить"));
    }

    @Test
    void shouldErrorNotEnoughMoneyFirstToSecondCard() {
        var cards = DataHelper.getCardNumbersFor(authInfo);
        int amount = 10200;
        DashboardPage.equalizeBalance(authInfo);

        DashboardPage.transfer(cards.getSecondCardId());
        MoneyTransferPage.moneyTransfer(amount, cards.getFirstCardNumber());

        $x("//*[text()='Ошибка! Недостаточно средств!']").shouldBe(visible);
    }

    @Test
    void shouldErrorNotEnoughMoneySecondToFirstCard() {
        var cards = DataHelper.getCardNumbersFor(authInfo);
        int amount = 10200;
        DashboardPage.equalizeBalance(authInfo);

        DashboardPage.transfer(cards.getFirstCardId());
        MoneyTransferPage.moneyTransfer(amount, cards.getSecondCardNumber());

        $x("//*[text()='Ошибка! Недостаточно средств!']").shouldBe(visible);
    }
}
