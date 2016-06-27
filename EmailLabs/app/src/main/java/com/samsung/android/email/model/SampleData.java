package com.samsung.android.email.model;

import java.util.Date;

/**
 * Created by nsbisht on 5/31/16.
 */
public class SampleData {
    public static Email[] getSampleData() {
        Email[] emails = new Email[10];

        Email email1 = new Email();
        email1.setmFromAddress("Sta");
        email1.setmSubject("First Mail");
        email1.setmBody("compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email1.setmDate(new Date(System.currentTimeMillis()));
        emails[0] = email1;

        Email email2 = new Email();
        email2.setmFromAddress("New User");
        email2.setmSubject("Second Mail");
        email2.setmBody("compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email2.setmDate(new Date(System.currentTimeMillis() - 60*1000));
        emails[1] = email2;

        Email email3 = new Email();
        email3.setmFromAddress("Dacvi");
        email3.setmSubject("Third Mail");
        email3.setmBody("compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email3.setmDate(new Date(System.currentTimeMillis() - 5*60*60*1000));
        emails[2] = email3;

        Email email4 = new Email();
        email4.setmFromAddress("Susan");
        email4.setmSubject("Fourth Mail");
        email4.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email4.setmDate(new Date(System.currentTimeMillis() - 24*60*60*1000));
        emails[3] = email4;

        Email email5 = new Email();
        email5.setmFromAddress("Mat");
        email5.setmSubject("Five Mail");
        email5.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email5.setmDate(new Date(System.currentTimeMillis() - 48*60*60*1000));
        emails[4] = email5;

        Email email6 = new Email();
        email6.setmFromAddress("Sta");
        email6.setmSubject("Six Mail");
        email6.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email6.setmDate(new Date(System.currentTimeMillis() - 72*60*60*1000));
        emails[5] = email6;

        Email email7 = new Email();
        email7.setmFromAddress("Sonia");
        email7.setmSubject("Seven Mail");
        email7.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email7.setmDate(new Date(System.currentTimeMillis()));
        emails[6] = email7;

        Email email8 = new Email();
        email8.setmFromAddress("Elsa");
        email8.setmSubject("Eight Mail");
        email8.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email8.setmDate(new Date(System.currentTimeMillis()));
        emails[7] = email8;

        Email email9 = new Email();
        email9.setmFromAddress("Naren");
        email9.setmSubject("Nine Mail");
        email9.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email9.setmDate(new Date(System.currentTimeMillis()));
        emails[8] = email9;

        Email email10 = new Email();
        email10.setmFromAddress("Sta");
        email10.setmSubject("Ten Mail");
        email10.setmBody("o compile your app against the Android N platform and use some tools with Android Studio 2.1, you need to install the Java 8 Developer Kit (JDK 8). So, if you don't already have the latest version, download JDK 8 now.\n" +
                "\n" +
                "Then set the JDK version in Android Studio as follows:\n" +
                "\n" +
                "Open an Android project in Android Studio, then open the Project Structure dialog by selecting File > Project Structure. (Alternatively, you can set the default for all projects by selecting File > Other Settings > Default Project Structure.)\n" +
                "In the left panel of the dialog, click SDK Location.");
        email10.setmDate(new Date(System.currentTimeMillis()));
        emails[9] = email10;

        return emails;
    }
}
