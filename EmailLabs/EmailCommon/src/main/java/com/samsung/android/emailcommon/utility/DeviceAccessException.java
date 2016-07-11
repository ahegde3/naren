
package com.samsung.android.emailcommon.utility;

import com.samsung.android.emailcommon.mail.MessagingException;

//change@siso.mahsky Allow/Block/Quarantine
public class DeviceAccessException extends MessagingException {

    public DeviceAccessException(int exceptionType, String message) {
        super(exceptionType, message);
        // TODO Auto-generated constructor stub
    }

    public DeviceAccessException(int exceptionType, int exceptionMessage) {
        super(exceptionType, exceptionMessage);
    }
}
// change@siso.mahsky Allow/Block/Quarantine
