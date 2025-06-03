package com.app.quizizo;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;
/* Ensure this import is correct */

class OTPService {

    public static final String ACCOUNT_SID = "ACe6bbd626a763cbbb2eda90593f206aa5"; // Replace with your Account SID
    public static final String AUTH_TOKEN = "a33efa11f31e303556f601c1bbf5e9d9"; // Replace with your Auth Token
    public static final String SERVICE_SID = "VAbd8856e946473d3e64326afd5f42e615"; // Replace with your Service SID

    static {
        // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static boolean sendOtp(String phoneNumberInput, String generatedOtp) {
        // Ensure the phone number is formatted correctly
        if (!phoneNumberInput.startsWith("+")) {
            phoneNumberInput = "+91" + phoneNumberInput; // Modify as necessary for your region
        }

        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberInput); // Use constructor

        try {
            Verification verification = Verification.creator(
                    SERVICE_SID,
                    String.valueOf(phoneNumber),
                    "sms"
            ).create();

            System.out.println("Verification SID: " + verification.getSid()); // Log the verification SID for debugging
            return true; // Indicate that the OTP was sent successfully
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyOtp(String phoneNumberInput, String otpCode) {
        // Ensure the phone number is formatted correctly
        if (!phoneNumberInput.startsWith("+")) {
            phoneNumberInput = "+91" + phoneNumberInput; // Modify as necessary for your region
        }

        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberInput); // Use constructor

        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(SERVICE_SID)
                    .setTo(String.valueOf(phoneNumber)) // User's phone number
                    .setCode(otpCode)   // OTP code entered by the user
                    .create();

            System.out.println("Verification Check Status: " + verificationCheck.getStatus());
            return "approved".equalsIgnoreCase(verificationCheck.getStatus()); // Return true if OTP is approved
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
