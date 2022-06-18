package com.birthdaymanager.util;

import android.telephony.SmsManager;

public class SMSUtils {
    String sPhone = "5554";
    String sMessage;

    public SMSUtils(String message) {
        this.sMessage = message;
    }

    public void attemptSendMessage() {
        sendMessage();
    }

    private void sendMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sPhone, null, sMessage, null, null);
    }
}
