package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void constructor_shouldCreateCustomerWithGivenValues() {

        Customer customer = new Customer("Arif", "Kara", "123", 1);

        assertEquals(1, customer.getAccId());
        assertEquals("Arif", customer.getName());
        assertEquals("Kara", customer.getSurname());
    }

    @Test
    void whenPasswordMatches_whenPasswordIsCorrect_shouldReturnTrue() {
        Customer customer = new Customer("Arif", "Kara", "123", 1);

        assertTrue(customer.passwordMatches("123"));
    }

    @Test
    void whenPasswordMatches_whenPasswordIsWrong_shouldReturnFalse() {

        Customer customer = new Customer("Arif", "Kara", "123", 1);

        assertFalse(customer.passwordMatches("120+3"));
    }
}
