//package org.example;

public class Payment {

    private static boolean validateCreditCard(String cardNumber){
        if (cardNumber == null || cardNumber.length() < 16){
            return false;
        }
        char firstDigit = cardNumber.charAt(0);
        return firstDigit == '5'           // Mastercard
                || firstDigit == '4';      // Visa
    }

    private static boolean validateCCV(int ccv) {
        return String.valueOf(ccv).matches("\\d{3,4}");
    }

    public static boolean processPayment(String cardNumber, int ccv) {
        if (!validateCreditCard(cardNumber)) {
            System.out.println("Invalid credit card number.");
            return false;
        }
        if (!validateCCV(ccv)) {
            System.out.println("Invalid CCV.");
            return false;
        }
        return true;
    }
}
