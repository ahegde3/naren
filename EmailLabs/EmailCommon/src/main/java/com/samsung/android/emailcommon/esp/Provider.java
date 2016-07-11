package com.samsung.android.emailcommon.esp;

public class Provider {
    public static final int INVALID = 0xFF;
    public static final int EXCHANGE = 1;
    public static final int YAHOO = 2;
    public static final int GMAIL = 3;
    public static final int VERIZON = 4;
    public static final int AOL = 5;
    public static final int HOTMAIL = 6;
    public static final int OTHER = 7;
    public static final int AIM = 8;
    public static final int NAVER = 9;
    public static final int DAUM = 10;
    public static final int NATE = 11;
    public static final int Account163 = 12;

    /**
     * All the account types must be added in below array as well.
     */
    protected static final int[] ProviderTypes = {
        INVALID,EXCHANGE,YAHOO,GMAIL,VERIZON,AOL,HOTMAIL,OTHER,AIM,NAVER,DAUM,NATE
    };

    public static final String PROVIDER_NAME_GOOGLE = "Google";
    public static final String PROVIDER_NAME_AOL = "AOL";
    public static final String PROVIDER_NAME_YAHOO = "Yahoo";
    public static final String PROVIDER_NAME_MSN = "MSN";

    public static final String SCHEME_POP3 = "pop3";
    public static final String SCHEME_IMAP = "imap";
    public static final String SCHEME_SNC = "snc";
    public static final String SCHEME_EAS = "eas";

    public static final int ALL_ACCOUNT_IMAGE = 0;
    public static final int EXCHANGACTIVESYNC_IMAGE = 1;
    public static final int GMAIL_IMAGE = 2;
    public static final int HOTMAIL_IMAGE = 3;
    public static final int OTHERS_IMAGE = 4;
    public static final int YAHOOMAIL_IMAGE = 5;
    public static final int AOLMAIL_IMAGE = 6;

    public static final String[] strTransport = {
            "INVALID", "EAS", "POP", "IMAP", "OZ"
            // SNC-REMOVE-SECTION-START
            , "SNC"
            // SNC-REMOVE-SECTION-END
            ,
            // ++added by taesoo77.lee adapter_work
            "SEVEN"
    // --
    };
    

    public boolean isProviderExchange(int provider) {
        return provider == Provider.EXCHANGE;
    }
    
    public static boolean isProviderYahoo(int provider) {
        return provider == Provider.YAHOO;
    }

    public static boolean isProviderGmail(int provider) {
        return provider == Provider.GMAIL;
    }

    public static boolean isProviderVerizon(int provider) {
        return provider == Provider.VERIZON;
    }

    public static boolean isProviderAol(int provider) {
        return provider == Provider.AOL;
    }

    public static boolean isProviderHotmail(int provider) {
        return provider == Provider.HOTMAIL;
    }

    public static boolean isProviderOther(int provider) {
        return provider == Provider.OTHER;
    }

    public static boolean isProviderAim(int provider) {
        return provider == Provider.AIM;
    }

    public static boolean isTransportEas(int transport) {
        return transport == Transport.EAS;
    }

    public static boolean isTransportLegacyPop(int transport) {
        return transport == Transport.LEGACY_POP;
    }

    public static boolean isTransportLegacyImap(int transport) {
        return transport == Transport.LEGACY_IMAP;
    }

    public static boolean isTransportLegacy(int transport) {
        return (transport == Transport.LEGACY_IMAP || transport == Transport.LEGACY_POP);
    }

    public static boolean isTransportOz(int transport) {
        return transport == Transport.OZ;
    }

    public static boolean isTransportSnc(int transport) {
        return transport == Transport.SNC;
    }

    public static boolean isTransportSeven(int transport) {
        return transport == Transport.SEVEN;
    }

    public static boolean isBig4(String provider) {
        if (provider == null) {
            return false;
        }
        if (provider.contains(PROVIDER_NAME_GOOGLE) || provider.contains(PROVIDER_NAME_AOL) ||
                provider.contains(PROVIDER_NAME_YAHOO) || provider.contains(PROVIDER_NAME_MSN) ) {
            return true;
        }
        return false;
    }
    
    
    /**
     * Extracts transport part as String from the EmailContent.Account's Type
     * column
     * 
     * @param type
     * @return ServiceProvider.Transport value
     */
    public static String getTransportStringFromType(final int type) {
        int tr = getTransportFromType(type);
        return ((tr >= Transport.EAS) && (tr < Transport.TRANSPORT_MAX)) ? strTransport[tr]
                : "INVALID";
    }

    /**
     * Extracts transport part from the EmailContent.Account's Type column
     * 
     * @param type
     * @return ServiceProvider.Transport value
     */
    public static int getTransportFromType(final int type) {
        return (type >> 8) & 0xFF;
    }

    /**
     * Extracts provider part from the EmailContent.Account's Type column
     * 
     * @param type
     * @return ServiceProvider.Provider value
     */
    public static int getProviderFromType(final int type) {
        return type & 0xFF;
    }

    /**
     * Constructs EmailContent.Account's Type value from the pair of transport
     * and provider
     * 
     * @param type
     * @return EmailContent.Account's Type value
     */
    public static int makeType(final int transport, final int provider) {
        return ((transport & 0xFF) << 8) | (provider & 0xFF);
    }
    
    
    /**
     * Check passed account type is Exchange or not
     *
     * @param accountType
     * @return boolean
     */
    public static boolean isAccountTypeExchange(int accountType) {
        return getProviderFromType(accountType) == EXCHANGE;
    }
    

    
    
}
