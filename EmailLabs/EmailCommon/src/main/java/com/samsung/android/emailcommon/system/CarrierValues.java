package com.samsung.android.emailcommon.system;

import android.os.SystemProperties;

import com.samsung.android.emailcommon.provider.AccountValues;

public interface CarrierValues {
	public static final String CSC_SALES_CODE = SystemProperties.get("ro.csc.sales_code");
    public static final String PRODUCT_NAME = SystemProperties.get("ro.product.name");
    public static final String RO_CARRIER = SystemProperties.get("ro.carrier");
    public static final String COUNTRY_CODE = SystemProperties.get("ro.csc.country_code");

    public static final String PRODUCT_MODEL = SystemProperties.get("ro.product.model", "Unknown");
    public static final String BUILD_PDA = SystemProperties.get("ro.build.PDA", "Unknown");
    public static final String BUILD_CHANGELIST = SystemProperties.get("ro.build.changelist", "Unknown");

    public static final String RO_SERIALNO = SystemProperties.get("ro.serialno", "unknown");
    public static final String RIL_SERIALNUMBER = SystemProperties.get("ril.serialnumber");
    public static final String OPERATOR_ISO_COUNTRY = SystemProperties.get("gsm.operator.iso-country", "");
    public static final String BUILD_CHARACTERISTICS = SystemProperties.get("ro.build.characteristics");

    // Sales_Code
    public static final boolean IS_CARRIER_VZW = "VZW".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_LRA = "LRA".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_ACG = "ACG".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_ATT = "ATT".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_TMB = "TMB".equals(CSC_SALES_CODE) || "TMK".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_RWC = "RWC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_FMC = "FMC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_MTA = "MTA".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_SPR = "SPR".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_BST = "BST".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_VMU = "VMU".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_XAS = "XAS".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_TFN = "TFN".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_USC = "USC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_DCM = "DCM".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CUE = "CUE".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_KDI = "KDI".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_SBM = "SBM".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CHN = "CHN".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CHM = "CHM".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CHU = "CHU".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_QHN = "QHN".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CHC = "CHC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CTC = "CTC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_TGY = "TGY".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_BRI = "BRI".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_ZZH = "ZZH".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_N08 = "N08".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_BNN = "BNN".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_XAR = "XAR".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_XAC = "XAC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_BMC = "BMC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_APT = "APT".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_ILO = "ILO".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_XAA = "XAA".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CCT = "CCT".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_BELL = "BMC".equals(CSC_SALES_CODE)
            || "BWA".equals(CSC_SALES_CODE) || "PCM".equals(CSC_SALES_CODE)
            || "SOL".equals(CSC_SALES_CODE) || "VMC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_ROGERS = "RWC".equals(CSC_SALES_CODE)
            || "FMC".equals(CSC_SALES_CODE) || "MTA".equals(CSC_SALES_CODE)
            || "CHR".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_TELLUS = "TLS".equals(CSC_SALES_CODE)
            || "KDO".equals(CSC_SALES_CODE) || "SPC".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_WHOLE_SPRINT = IS_CARRIER_SPR || IS_CARRIER_BST
            || IS_CARRIER_VMU || IS_CARRIER_XAS;
    public static final boolean IS_CARRIER_AIO = "AIO".equals(CSC_SALES_CODE);

    public static final boolean IS_CARRIER_GUM = "GUM".equals(CSC_SALES_CODE);

    public static final boolean IS_CARRIER_DT =   "COA".equals(CSC_SALES_CODE) ||
            "COS".equals(CSC_SALES_CODE) ||    "CRO".equals(CSC_SALES_CODE) ||
            "DAX".equals(CSC_SALES_CODE) ||    "TTR".equals(CSC_SALES_CODE) ||
            "MAX".equals(CSC_SALES_CODE) ||    "TRG".equals(CSC_SALES_CODE) ||
            "DDX".equals(CSC_SALES_CODE) ||    "DTM".equals(CSC_SALES_CODE) ||
            "DDE".equals(CSC_SALES_CODE) ||    "DHX".equals(CSC_SALES_CODE) ||
            "CRO".equals(CSC_SALES_CODE) ||    "DHR".equals(CSC_SALES_CODE) ||
            "DNX".equals(CSC_SALES_CODE) ||    "TNL".equals(CSC_SALES_CODE) ||
            "DNL".equals(CSC_SALES_CODE) ||    "DPX".equals(CSC_SALES_CODE) ||
            "TPL".equals(CSC_SALES_CODE) ||    "DPL".equals(CSC_SALES_CODE) ||
            "MBM".equals(CSC_SALES_CODE) ||    "TMH".equals(CSC_SALES_CODE) ||
            "TMS".equals(CSC_SALES_CODE) ||    "TMT".equals(CSC_SALES_CODE) ||
            "TMZ".equals(CSC_SALES_CODE);
    // Sales_Code

    // Country by Sales_Code
    public static final boolean IS_CARRIER_CANADA_OTHER = "GLW".equals(CSC_SALES_CODE)
            || "ESK".equals(CSC_SALES_CODE) || "MCT".equals(CSC_SALES_CODE)
            || "VTR".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_CANADA = IS_CARRIER_BELL || IS_CARRIER_ROGERS
            || IS_CARRIER_TELLUS || IS_CARRIER_CANADA_OTHER || IS_CARRIER_XAC;
    public static final boolean IS_CARRIER_AUS = "XSA".equals(CSC_SALES_CODE)
            || "OPS".equals(CSC_SALES_CODE) || "VAU".equals(CSC_SALES_CODE)
            || "TEL".equals(CSC_SALES_CODE);
    public static final boolean IS_CARRIER_NA = ((IS_CARRIER_ATT || IS_CARRIER_AIO
            || IS_CARRIER_VZW || IS_CARRIER_TMB || IS_CARRIER_WHOLE_SPRINT || IS_CARRIER_LRA
            || IS_CARRIER_ACG || IS_CARRIER_USC || IS_CARRIER_TFN  || IS_CARRIER_N08
            || IS_CARRIER_BNN || IS_CARRIER_XAR || IS_CARRIER_XAA || IS_CARRIER_CCT
            || IS_CARRIER_GUM));
    public static final boolean IS_CARRIER_CHINA = ((IS_CARRIER_CHN || IS_CARRIER_CHM
            || IS_CARRIER_CHU || IS_CARRIER_QHN || IS_CARRIER_CHC || IS_CARRIER_CTC
            || IS_CARRIER_TGY || IS_CARRIER_BRI || IS_CARRIER_ZZH));
    public static final boolean IS_CARRIER_JPN = IS_CARRIER_DCM || IS_CARRIER_KDI || IS_CARRIER_SBM;
    // Country by Sales_Code

    //Feature by Sales_Code
    public static final boolean IS_CARRIER_LIVEDEMOUNIT = "PAP".equals(CSC_SALES_CODE)
            || "FOP".equals(CSC_SALES_CODE);


    public static final boolean DEFAULT_ACCOUNT_CHECK_INTERVAL_MANUAL=
            "MAX".equals(CSC_SALES_CODE) || "DTM".equals(CSC_SALES_CODE)
                    || "TNL".equals(CSC_SALES_CODE) || "TMZ".equals(CSC_SALES_CODE)
                    || "TMU".equals(CSC_SALES_CODE) || "TPL".equals(CSC_SALES_CODE)
                    || "COS".equals(CSC_SALES_CODE) || "TMS".equals(CSC_SALES_CODE)
                    || "TRG".equals(CSC_SALES_CODE) || "MBM".equals(CSC_SALES_CODE)
                    || "TMT".equals(CSC_SALES_CODE) || "CHN".equals(CSC_SALES_CODE)
                    || "CHM".equals(CSC_SALES_CODE) || "CHU".equals(CSC_SALES_CODE)
                    || "CTC".equals(CSC_SALES_CODE) || "BRI".equals(CSC_SALES_CODE)
                    || "TGY".equals(CSC_SALES_CODE) || "ZZH".equals(CSC_SALES_CODE)
                    || IS_CARRIER_APT || "CHC".equals(CSC_SALES_CODE);

    public static final boolean CHINA_MODEL = (CarrierValues.IS_CARRIER_CHN || CarrierValues.IS_CARRIER_CHM
    || CarrierValues.IS_CARRIER_CHU || CarrierValues.IS_CARRIER_QHN
    || CarrierValues.IS_CARRIER_CHC || CarrierValues.IS_CARRIER_CTC)
    || ("China".equals(COUNTRY_CODE) && ("PAP".equals(CSC_SALES_CODE) || "BMW".equals(CSC_SALES_CODE))) ? true : false;

    //Feature by Sales_Code


    //Model by product name
    public static final boolean IS_DEVICE_VZWJ3 = "j3ltevzw".equals(PRODUCT_NAME);
    public static final boolean IS_DEVICE_T0 = PRODUCT_NAME.startsWith("t0");
    public static final boolean IS_DEVICE_GOLDEN = PRODUCT_NAME.startsWith("golden");
    public static final boolean IS_DEVICE_KONA = PRODUCT_NAME.startsWith("kona");
    public static final boolean IS_DEVICE_PHILIPPE = PRODUCT_NAME.startsWith("philippe");
    public static final boolean IS_BUILD_TYPE_ENG = SystemProperties.get("ro.build.type")
            .equalsIgnoreCase("eng");
    public static final boolean IS_DEVICE_MELIUS = PRODUCT_NAME.startsWith("melius");
    public static final boolean IS_DEVICE_MEGA2 = PRODUCT_NAME.startsWith("mega2")
            || PRODUCT_NAME.startsWith("vasta");
    public static final boolean IS_DEVICE_CRATER = PRODUCT_NAME.startsWith("crater");
    public static final boolean IS_DEVICE_FLTE = PRODUCT_NAME.startsWith("flte");
    public static final boolean IS_DEVICE_H = PRODUCT_NAME.startsWith("hlte")
            || PRODUCT_NAME.startsWith("ha3g") || PRODUCT_NAME.startsWith("h3g")
            || PRODUCT_NAME.startsWith("hl3g") || PRODUCT_NAME.startsWith("hllte")
            || PRODUCT_NAME.startsWith("ms01") || "ASH".equalsIgnoreCase(PRODUCT_NAME)
            || "SC-01F".equalsIgnoreCase(PRODUCT_NAME) || "Madrid".equalsIgnoreCase(PRODUCT_NAME)
            || "SCL22".equalsIgnoreCase(PRODUCT_NAME) || "JS01".equalsIgnoreCase(PRODUCT_NAME)
            || "SC-02F".equalsIgnoreCase(PRODUCT_NAME) || IS_DEVICE_FLTE
            || PRODUCT_NAME.startsWith("fresco");
    public static final boolean IS_DEVICE_H3GDUOSCTC = PRODUCT_NAME.startsWith("h3gduosctc");
    public static final boolean IS_DEVICE_MS01 = PRODUCT_NAME.startsWith("ms01");
    public static final boolean IS_DEVICE_TABE = PRODUCT_NAME.startsWith("gtes");
    public static final boolean IS_DEVICE_TABEX = PRODUCT_NAME.startsWith("gtex");
    public static final boolean IS_DEVICE_DEGAS = PRODUCT_NAME.startsWith("degas");

    public static final boolean IS_DEVICE_T = PRODUCT_NAME.startsWith("tblte")
            || PRODUCT_NAME.startsWith("tb3g") || PRODUCT_NAME.startsWith("trlte")
            || PRODUCT_NAME.startsWith("tre3g") || PRODUCT_NAME.startsWith("trhlte")
            || PRODUCT_NAME.startsWith("trhp0lte") || PRODUCT_NAME.startsWith("tbhplte")
            || PRODUCT_NAME.startsWith("trhplte") || PRODUCT_NAME.startsWith("trelte")
            || PRODUCT_NAME.startsWith("trwifi");
    public static final boolean IS_DEVICE_NOBLE = PRODUCT_NAME.startsWith("noblelte");
    public static final boolean IS_DEVICE_A8 = PRODUCT_NAME.startsWith("a8");
    public static final boolean IS_DEVICE_A8_JP = PRODUCT_NAME.startsWith("SCV32");
    public static final boolean IS_DEVICE_A9 = PRODUCT_NAME.startsWith("a9");
    public static final boolean IS_DEVICE_A9X = PRODUCT_NAME.startsWith("a9x");
    public static final boolean IS_DEVICE_ZEN = PRODUCT_NAME.startsWith("zen");
    public static final boolean IS_DEVICE_HERO = PRODUCT_NAME.startsWith("hero");
    public static final boolean IS_DEVICE_HERO2 = PRODUCT_NAME.startsWith("hero2");
    public static final boolean IS_DEVICE_HERO2_JP = PRODUCT_NAME.startsWith("SC-02H") || PRODUCT_NAME.startsWith("SGH-N611")//DCM : sgh-n611 is fakeId until unpack
                                                    || PRODUCT_NAME.startsWith("SCV33") || PRODUCT_NAME.startsWith("KSU"); //KDDI : KSU is fakeId until unpack
    public static final boolean IS_DEVICE_J7 = PRODUCT_NAME.startsWith("j7");
    public static final boolean IS_DEVICE_A7 = PRODUCT_NAME.startsWith("a7");

    public static final boolean IS_DEVICE_GRACE = PRODUCT_NAME.startsWith("grace") || PRODUCT_NAME.startsWith("SCV34") || PRODUCT_NAME.startsWith("LTV");//KDI fakeID

    public static final boolean IS_DEVICE_YPLATFORM = PRODUCT_NAME.startsWith("j2xlte"); 

    public static final boolean IS_DEVICE_VZWGD1LTE = "gd1ltevzw".equals(PRODUCT_NAME);
    public static final boolean IS_DEVICE_AEGIS2VZW = "aegis2vzw".equals(PRODUCT_NAME);
    //Model by product name



    public static final int VZW_AUTO_RETRY_LIMIT = 3;
    public static final int SPR_EAS_AUTO_RETRY_LIMIT = 1000;
    public static final int SPR_IMAP_POP_AUTO_RETRY_LIMIT = 5;

    public static final int DEFAULT_AUTORETRYTIMES =
            (IS_CARRIER_USC || IS_CARRIER_VZW) ? 3
                    : 5;
    public final static String DEFAULT_RINGTONEURI = IS_CARRIER_VZW ? ""
            : IS_CARRIER_AIO ? "/system/media/audio/notifications/Cricket_Keyboard.ogg"
                    : IS_CARRIER_ATT ? "/system/media/audio/notifications/S_Dew_drops.ogg"
                            : IS_CARRIER_USC ? ""
                                    :"/system/media/audio/notifications/Letter.ogg" ;
    public static final int DEFAULT_PEAK_DAYS = 0x3e; // | -- xx Mon Tue |
    public static final int DEFAULT_PEAK_START_MINUTE = 420;// 7 am
    public static final int DEFAULT_PEAK_END_MINUTE = 1320; // 22 pm
    public final static int CHECK_ROAMING_DEFAULT = 
    		IS_CARRIER_USC ||
    		IS_CARRIER_ACG ?
    		AccountValues.SyncTime.CHECK_ROAMING_SYNC_SCHEDULE : 
    		AccountValues.SyncTime.CHECK_ROAMING_MANUAL;

    public final static boolean DEFAULT_SHOWIMAGE = "wifi-only".equalsIgnoreCase(RO_CARRIER) ? true
            : (IS_CARRIER_NA || IS_CARRIER_DT || IS_CARRIER_AUS) ? false
            :   true;

    public static final boolean VIEW_AUTOIMAGE_DEFAULT = (CarrierValues.IS_CARRIER_NA
            || CarrierValues.IS_CARRIER_DT || IS_CARRIER_AUS) ? false : true;
    
    public static final boolean CANCEL_SENDING_MESSAGE_DEFAULT = (CarrierValues.IS_CARRIER_ATT || CarrierValues.IS_CARRIER_AIO) ? true
            : false;
    
    public static final boolean HIDE_CHECKBOXES_DEFAULT = CarrierValues.IS_CARRIER_ATT ? false : true;
    
    // pop imap default retrieve size
    public final static int DEFAULT_MESSAGESIZE = IS_CARRIER_VZW ? AccountValues.SyncSize.MESSAGE_SIZE_20_KB
            : IS_CARRIER_NA ? AccountValues.SyncSize.EMAIL_SIZE_51200
                    : AccountValues.SyncSize.EMAIL_SIZE_ALL_WITHOUT_ATTACHMENT;

    public final static int DEFAULT_MESSAGESIZE_ROAMING = IS_CARRIER_VZW ? AccountValues.SyncSize.MESSAGE_SIZE_2_KB : AccountValues.SyncSize.EMAIL_SIZE_51200;
    public static final int SYNC_WINDOW_EAS_DEFAULT = AccountValues.SyncWindow.SYNC_WINDOW_3_DAYS;

    public final static int SYNC_WINDOW_IMAP_DEFAULT = IS_CARRIER_VZW ? AccountValues.SyncWindow.SYNC_WINDOW_1_MONTH : AccountValues.SyncWindow.SYNC_WINDOW_2_WEEKS;

    public static final int CHECK_SYNC_INTERVAL = IS_CARRIER_ATT ? 60 : 15;

    public final static int DEFAULT_ACCOUNT_CHECK_INTERVAL_EUR_DT = -1;
    public static final int AUTO_ADVANCE_NEWER = 0;
    public static final int AUTO_ADVANCE_OLDER = 1;
    public static final int AUTO_ADVANCE_MESSAGE_LIST = 2;
    // "move to older" was the behavior on older versions.
    public static final int AUTO_ADVANCE_DEFAULT = IS_CARRIER_USC ? AUTO_ADVANCE_OLDER
            : AUTO_ADVANCE_MESSAGE_LIST;

}
