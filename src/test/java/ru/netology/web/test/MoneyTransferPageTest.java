package ru.netology.web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.DashboardPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.page.DashboardPage.*;

class MoneyTransferPageTest {
    private int[] cardsBalance;
    private int sum1 = 5_637;
    private int sum2 = 4_126;
    private int sum3 = 1_968;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        new LoginPage().validLogin(getAuthInfo()).validVerify(getVerificationCodeFor(getAuthInfo()));
        cardsBalance = cardsBalance();
        cardsBalance = justifyBalance(cardsBalance[1], cardsBalance[2]);
        assertEquals(cardsBalance[1], cardsBalance[2]);
    }

    @AfterEach
    void asserting() {
        cardsBalance = cardsBalance();
        cardsBalance = justifyBalance(cardsBalance[1], cardsBalance[2]);
        assertEquals(cardsBalance[1], cardsBalance[2]);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        int expectedFirst = cardsBalance[1] + sum1 + sum2 - sum3;
        int expectedSecond = cardsBalance[2] - sum1 - sum2 + sum3;
        var dashboard = new DashboardPage();
        dashboard.moneyTransfer(cardNumber(1)).transaction(Integer.toString(sum1), cardNumber(2));
        dashboard.moneyTransfer(cardNumber(1)).transaction(Integer.toString(sum2), cardNumber(2));
        dashboard.moneyTransfer(cardNumber(2)).transaction(Integer.toString(sum3), cardNumber(1));
        cardsBalance = cardsBalance();
        assertEquals(expectedFirst, cardsBalance[1]);
        assertEquals(expectedSecond, cardsBalance[2]);
    }
}
