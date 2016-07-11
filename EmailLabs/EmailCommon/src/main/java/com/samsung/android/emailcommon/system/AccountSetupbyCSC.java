
package com.samsung.android.emailcommon.system;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.samsung.android.emailcommon.provider.EmailContent.Account;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.Log;
import com.samsung.android.emailcommon.utility.Utility;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class CscParser {
    public static String TAG = "CscParser";

    public static String CSC_XML_FILE = "/system/csc/customer.xml";
    public static String CSC_OTHERS_FILE = "/system/csc/others.xml";

    public static String CSC_ID_FILE = "/system/SW_Configuration.xml";

    public static String CSC_CHAMELEON_FILE = "/carrier/chameleon.xml";

    // private static CscParser sInstance = new CscParser(CSC_XML_FILE);

    /*
     * private static CscParser sInstance = new CscParser(getCustomerPath());
     * private static CscParser sOthersInstance = new
     * CscParser(getOthersPath()); private static CscParser sChameleonInstance =
     * new CscParser(getChameleonPath());
     */

    // dwheo@hdlnc.com 20100217 In order to bring the tone Uri string which hits
    // to a corresponding type
    // private Context mContext;
    public static Uri value_uri = null;

    public static int Media_Type = 1/* TYPE_RINGTONE */;

    // private Handler mHandler = new Handler();

    // private final String CSC_XML_FILE = "/data/customer.xml";

    public Node mRoot;

    public Document mDoc;

/*    public CscParser(Context context) {
        // mContext = context;
    }*/

    public CscParser(String fileName) {
        // mFileName = fileName;
        try {
            update(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * public static CscParser getInstance() { try {
     * sInstance.update(getCustomerPath()); } catch (Exception e) {
     * e.printStackTrace(); } return sInstance; } public static CscParser
     * getInstance(String fileName) { try { sInstance.update(fileName);
     * EmailLog.d(TAG, "getInstance fileName =" + fileName); if
     * (fileName.equalsIgnoreCase(getCustomerPath())) {
     * sInstance.update(getCustomerPath()); return sInstance; } else if
     * (fileName.equalsIgnoreCase(getOthersPath())) {
     * sOthersInstance.update(getOthersPath()); return sOthersInstance; } }
     * catch (Exception e) { e.printStackTrace(); } return sInstance; } public
     * static CscParser getChameleonInstance(String fileName) { try {
     * sChameleonInstance.update(getChameleonPath()); return sChameleonInstance;
     * } catch (Exception e) { e.printStackTrace(); } return null; }
     */

    public void update(String fileName) throws ParserConfigurationException, SAXException,
            IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File fe = new File(fileName);
        if (fe.exists()) {
            // 20100420 sylee null check for filename
            // if(fileName != null)
            // EmailLog.e(TAG, "fileName + " + fileName); // logging test file, User
            // Information
            EmailLog.e(TAG, "update(): xml file exist");
            mDoc = builder.parse(new File(fileName));
            mRoot = mDoc.getDocumentElement();
        } else {
            EmailLog.e(TAG, "update(): xml file not exist");
        }
    }

    public String get(String path) {
        Node node = search(path);

        if (null == node)
            return null;

        Node firstChild = node.getFirstChild();
        return (null != firstChild ? firstChild.getNodeValue() : null);
    }

    public Node search(String path) {
        if (path == null)
            return null;

        Node node = mRoot;
        StringTokenizer tokenizer = new StringTokenizer(path, ".");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (node == null) {
                return null;
            }
            node = search(node, token);
        }

        return node;
    }

    public Node search(Node parent, String name) {
        if (parent == null)
            return null;

        NodeList children = parent.getChildNodes();

        if (children != null) {
            int n = children.getLength();

            for (int i = 0; i < n; i++) {
                Node child = children.item(i);

                if (child.getNodeName().equals(name)) {
                    return child;
                }
            }
        }

        return null;
    }

    public NodeList searchList(Node parent, String name) {
        if (parent == null)
            return null;

        try {
            Element list = mDoc.createElement(parent.getNodeName());
            NodeList children = parent.getChildNodes();

            if (children != null) {
                int n = children.getLength();

                for (int i = 0; i < n; i++) {
                    Node child = children.item(i);

                    if (child.getNodeName().equals(name)) {
                        try {
                            list.appendChild(child);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return list.getChildNodes();
        } catch (Exception e) {
            return null;
        }
    }

    public String getValue(Node node) {
        if (node == null) {
            return null;
        }
        // Moon seung-bae add for & parsing problem
        if (node.getChildNodes().getLength() > 1) {
            String stringValue = "";
            int idx;
            StringBuffer stringValuebuf = new StringBuffer(stringValue);
            for (idx = 0; idx < node.getChildNodes().getLength(); idx++) {
                stringValuebuf.append(node.getChildNodes().item(idx).getNodeValue());
            }
            stringValue = stringValuebuf.toString();
            return stringValue;
        }
        // Moon seung-bae add for & parsing problem

        Node firstChild = node.getFirstChild();
        return (null != firstChild ? firstChild.getNodeValue() : null);
    }

    public String getAttrbute(String tagPath, int index, int mode) {
        String attribute = null;
        String[] tagSplit = tagPath.split("[.]");
        int tagCount = tagSplit.length;

        if (tagCount-- < 3)
            return attribute;

        // tagNode.tagList.tagAttr
        // ex)Settings.Main.Sound.MessageTone.src
        String tagAttr = tagSplit[tagCount--];
        String tagList = tagSplit[tagCount];
        String tagNode = null;

        for (int i = 0; i < tagCount; i++) {
            if (tagNode == null)
                tagNode = tagSplit[i];
            else
                tagNode = tagNode + "." + tagSplit[i];
        }

        NodeList nodeList = searchList(search(tagNode), tagList);
        if (nodeList != null && nodeList.getLength() > index) {
            Element list = (Element) nodeList.item(index);
            // Element attr = (Element)
            // list.getElementsByTagName(tagList).item(0);
            attribute = list.getAttribute(tagAttr);
        }

        // Only file name(ex, RingTone, MessageTone)
        // ex)RingTone src = /customer/RingTone.mp3
        if (attribute != null && mode == 1) {
            String[] attrSlash = attribute.split("/");
            int cntSlash = attrSlash.length - 1;

            if (attrSlash[cntSlash] != null) {
                String[] attrSplit = attrSlash[cntSlash].split("[.]");
                if (attrSplit[0] != null)
                    attribute = attrSplit[0];
            }
        }

        EmailLog.d(TAG, tagList + ": " + attribute);
        return attribute;
    }
    
    public static String getOmcCustomerPath() {
        String mps_code = CarrierValues.CSC_SALES_CODE;
        if (mps_code != null)
            return "/carrier/omc/"+mps_code+"/customer.xml";
        return null;
    }

    public static String getOmcOthersPath() {
        String mps_code = CarrierValues.CSC_SALES_CODE;
        if (mps_code != null)
            return "/carrier/omc/"+mps_code+"/others.xml";
        return null;
    }
    
    public static String getCustomerPath() {
        String omc_customer_file = getOmcCustomerPath();
        if (omc_customer_file != null) {
            File file = new File(omc_customer_file);

            if (file.exists()) {
                if (file.canRead()) {
                    Log.i(TAG, "getCustomerPath : omc customer file can read");
                    return omc_customer_file;
                } else {
                    Log.e(TAG, "getCustomerPath : omc customer file exist but can't read");
                    return CSC_XML_FILE;
                }
            }
        }
        Log.e(TAG, "getCustomerPath : customer file exist");
        return CSC_XML_FILE; 

    }

    public static String getOthersPath() {
        String omc_others_file = getOmcOthersPath();
        if (omc_others_file != null) {
            File file = new File(omc_others_file);

            if (file.exists())
            {
                if (file.canRead()) {
                    Log.i(TAG,"getOthersPath : omc others file can read");
                    return omc_others_file;
                }else {
                    Log.e(TAG, "getOthersPath : omc others file exist but can't read");
                    return CSC_OTHERS_FILE;
                }
            }
        }
        Log.e(TAG, "getOthersPath : others file exist");  
        return CSC_OTHERS_FILE;
    }

    public static String getChameleonPath() {
        String chameleon_path = CSC_CHAMELEON_FILE;
        return chameleon_path;
    }

    public static String getIDPath() {
        String ID_path = CSC_ID_FILE;
        return ID_path;
    }
}

public class AccountSetupbyCSC extends Application {
    public static final String DEBUG_TAG = "SetupAccountbyCSC";

    //public static final String EMAIL_CSC_PATH = "/data/data/com.samsung.android.email.ui/shared_prefs/";

	public CscParser mParser = null;

    public static final String[] KEY_PRFS = {
            // CSCDATA_EmailEAS.xml
            "csc_pref_key_Settings_Messages_Email_AutoAdvance",
            "csc_pref_key_Settings_Messages_Email_ConfirmDelete",
            "csc_pref_key_Settings_Messages_Email_EmailSending_AutoResendTime",            
            "csc_pref_key_Settings_Messages_Email_EmailReceiving_PollTime",
            "csc_pref_key_Settings_ActiveSync_UseSSL",
            "csc_pref_key_Settings_ActiveSync_AcceptAllSSLCert",
            "csc_pref_key_Settings_ActiveSync_SyncCalendar",
            "csc_pref_key_Settings_ActiveSync_SyncContacts",
            "csc_pref_key_Settings_ActiveSync_RoamingSetting",
            "csc_pref_key_Settings_ActiveSync_Signature",
            "csc_pref_key_Settings_ActiveSync_SyncSchedule_PeakDuration",
            "csc_pref_key_Settings_ActiveSync_SyncSchedule_OffPeakDuration",
            "csc_pref_key_Settings_ActiveSync_SyncSchedule_PeakDayStart",
            "csc_pref_key_Settings_ActiveSync_SyncSchedule_PeakDayEnd",
            "csc_pref_key_Settings_ActiveSync_SyncSchedule_PeakTimeStart",
            "csc_pref_key_Settings_ActiveSync_SyncSchedule_PeakTimeEnd",
            "csc_pref_key_Settings_ActiveSync_EasEmail_PeriodEmail",
            "csc_pref_key_Settings_ActiveSync_EasEmail_SizeEmail",
            "csc_pref_key_Settings_ActiveSync_EasCalendar_PeriodCalendar",
            "csc_pref_key_Settings_ActiveSync_SyncTasks",
            "csc_pref_key_Settings_ActiveSync_SyncSms"
    };

    public static final String[] USER_KEY_PRFS = {
            // customer
            "pref_key_Settings_Messages_Email_AutoAdvance",
            "pref_key_Settings_Messages_Email_ConfirmDelete",
            "pref_key_Settings_Messages_Email_EmailSending_AutoResendTime",
            "pref_key_Settings_Messages_Email_EmailReceiving_PollTime",           
            "pref_key_Settings_ActiveSync_UseSSL", "pref_key_Settings_ActiveSync_AcceptAllSSLCert",
            "pref_key_Settings_ActiveSync_SyncCalendar",
            "pref_key_Settings_ActiveSync_SyncContacts",
            "pref_key_Settings_ActiveSync_RoamingSetting",
            "pref_key_Settings_ActiveSync_Signature",
            "pref_key_Settings_ActiveSync_SyncSchedule_PeakDuration",
            "pref_key_Settings_ActiveSync_SyncSchedule_OffPeakDuration",
            "pref_key_Settings_ActiveSync_SyncSchedule_PeakDayStart",
            "pref_key_Settings_ActiveSync_SyncSchedule_PeakDayEnd",
            "pref_key_Settings_ActiveSync_SyncSchedule_PeakTimeStart",
            "pref_key_Settings_ActiveSync_SyncSchedule_PeakTimeEnd",
            "pref_key_Settings_ActiveSync_EasEmail_PeriodEmail",
            "pref_key_Settings_ActiveSync_EasEmail_SizeEmail",
            "pref_key_Settings_ActiveSync_EasCalendar_PeriodCalendar",
            "pref_key_Settings_ActiveSync_EasCalendar_SyncTasks",
            "pref_key_Settings_ActiveSync_EasCalendar_SyncSms"
    };

    private Context CSC_context;
    public static SharedPreferences emailcsc_SharedPreferences;

    /*
     * // can not be used API public AccountSetupbyCSC(){
     * emailcsc_SharedPreferences =
     * getApplicationContext().getSharedPreferences(CSCDATA_FILENAME,
     * Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
     * //loadXMLFile_eas(); }
     */

    public AccountSetupbyCSC(Context context) {
        CSC_context = context;
        EmailLog.d(DEBUG_TAG, "AccountSetupbyCSC context value : " + CSC_context);
        loadXMLFile_eas();
    }

    public AccountSetupbyCSC(Context context, boolean isCameleoncheck) {
        CSC_context = context;
        EmailLog.d(DEBUG_TAG, "AccountSetupbyCSC context value : " + CSC_context);
        if (isCameleoncheck) {
            loadXMLFile_cameleon();
        }
    }

    public void loadXMLFile_eas() {

        emailcsc_SharedPreferences = CSC_context.getSharedPreferences(Utility.CSCDATA_FILENAME,
                Context.MODE_PRIVATE);
		mParser = new CscParser(CscParser.CSC_XML_FILE);
    }

    final public void loadXMLFile_cameleon() {
        mParser = new CscParser(CscParser.CSC_CHAMELEON_FILE);
    }

    public boolean is_CSCDATA() {
        return true;
    }

    public boolean is_cameleonDATA() {
        boolean check_val = false;
        File files = new File(CscParser.CSC_CHAMELEON_FILE);
        if (files.exists() == true) {
            EmailLog.d(DEBUG_TAG, "CSCDATA_EmailEAS file existed.");
            check_val = true;
        } else {
            EmailLog.d(DEBUG_TAG, "[NOFILE HERE] CSCDATA_EmailEAS file not existed.");
            check_val = false;
        }
        return check_val;
    }
    
    public String getFiledValue_came(String rcv_value) {

        String default_value = rcv_value;
        String Field_value = "";

        if(mParser != null)
            Field_value= mParser.get("Operators.EmailSignature");
        else
            return default_value;

        if (Field_value != null) {
            if (Field_value.equalsIgnoreCase("NONE") == false)
                default_value = Field_value;
            EmailLog.d(DEBUG_TAG, "Cameleon Signature return value -  " + default_value);
        }else{
            EmailLog.d(DEBUG_TAG, "Cameleon Signature Field_value null");
        }
        return default_value;
    }

    public String getFiledValue_eas(String Field) {
        String Field_value = "NONE";
        int found_index = 0;
        for (int i = 0; i < Utility.KEY_CSC_FIELD.length; i++) {
            if (Utility.KEY_CSC_FIELD[i].equalsIgnoreCase(Field) == true)
                found_index = i;
        }
        // oong Field_value =
        // emailcsc_SharedPreferences.getString(KEY_PRFS[found_index], "NONE");
        Field_value = mParser.get(Utility.KEY_CSC[found_index]);
        EmailLog.d("AccountSetupbyCSC", "Key=" + Utility.KEY_CSC[found_index] + " | value = "
                + Field_value);
        if (Field_value == null)
            Field_value = "NONE";
        return Field_value;
    }

    // GETTING ENUM VALUE API [ ref - Arrays.xml (email\res\values) ]

    /**
     * This method get PeakDuration value CSC VALUE =
     * Settings.ActiveSync.SyncSchedule.PeakDuration Enumerated String : Push /
     * Manual / 5 mins / 15 mins / 1 hour / 2 hours / 4 hours / 12 hours
     */


    public int get_PeakDuration(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("PeakDuration");

        int default_value = -2;
        if (rcv_value == -2 || rcv_value == -1 /*|| rcv_value == 5*/ || rcv_value == 15
                || rcv_value == 60 || rcv_value == 120 || rcv_value == 240 || rcv_value == 720)
            default_value = rcv_value;

        default_value = getDuration(default_value, rcv_Field_value);
        EmailLog.d(DEBUG_TAG, "PeakDuration : " + rcv_Field_value + " value : " + default_value);
        return default_value;

    }

    /**
     * This method get OffPeakDuration value CSC VALUE =
     * Settings.ActiveSync.SyncSchedule.OffPeakDuration Enumerated String : Push
     * / Manual / 5 mins / 15 mins / 1 hour / 2 hours / 4 hours / 12 hours
     */


    public int get_OffPeakDuration(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("OffPeakDuration");

        int default_value = -2;
        if (rcv_value == -2 || rcv_value == -1 /*|| rcv_value == 5 */|| rcv_value == 15
                || rcv_value == 60 || rcv_value == 120 || rcv_value == 240 || rcv_value == 720)
            default_value = rcv_value;

        default_value = getDuration(default_value, rcv_Field_value);
        EmailLog.d(DEBUG_TAG, "OffPeakDuration : " + rcv_Field_value + " value : " + default_value);
        return default_value;

    }

    private int getDuration(int default_value, String rcv_Field_value) {
        
        if (rcv_Field_value.equalsIgnoreCase("PUSH"))
            default_value = -2;
        else if (rcv_Field_value.equalsIgnoreCase("MANUAL"))
            default_value = -1;
//        else if (rcv_Field_value.equalsIgnoreCase("5M"))
//            default_value = 5;
        else if (rcv_Field_value.equalsIgnoreCase("15M"))
            default_value = 15;
        else if (rcv_Field_value.equalsIgnoreCase("1H"))
            default_value = 60;
        else if (rcv_Field_value.equalsIgnoreCase("2H"))
            default_value = 120;
        else if (rcv_Field_value.equalsIgnoreCase("4H"))
            default_value = 240;
        else if (rcv_Field_value.equalsIgnoreCase("12H"))
            default_value = 720;
        
        return default_value;
    }
    
    /**
     * This method get PeriodCalendar value CSC VALUE =
     * Settings.ActiveSync.EasCalendar.PeriodCalendar Enumerated String : 2
     * weeks / 1 month / 3 months / 6 months / All calendar
     */

    public int get_PeriodCalendar(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("PeriodCalendar");

        int default_value = 0;
        if (rcv_value >= 0 && rcv_value <= 4)
            default_value = rcv_value;

        if (rcv_Field_value.equalsIgnoreCase("2W"))
            default_value = 0;
        else if (rcv_Field_value.equalsIgnoreCase("1M"))
            default_value = 1;
        else if (rcv_Field_value.equalsIgnoreCase("3M"))
            default_value = 2;
        else if (rcv_Field_value.equalsIgnoreCase("6M"))
            default_value = 3;
        else if (rcv_Field_value.equalsIgnoreCase("ALL"))
            default_value = 4;

        EmailLog.d(DEBUG_TAG, "PeriodCalendar -  " + rcv_Field_value + " == value " + default_value);

        return default_value;

    }

    /**
     * This method get PeriodEmail value CSC VALUE =
     * Settings.ActiveSync.EasEmail.PeriodEmail Enumerated String :
     * Automatic/All/1 day/3 days/1 week/2 weeks/1 month
     */

    public int get_PeriodEmail(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("PeriodEmail");

        int default_value = 0; // Automatic
        if (rcv_value > 0 && rcv_value <= 6)
            default_value = rcv_value;

        // using for setSelection values
        if (rcv_Field_value.equalsIgnoreCase("ALL"))
            default_value = 6;
        else if (rcv_Field_value.equalsIgnoreCase("1D"))
            default_value = 1;
        else if (rcv_Field_value.equalsIgnoreCase("3D"))
            default_value = 2;
        else if (rcv_Field_value.equalsIgnoreCase("1W"))
            default_value = 3;
        else if (rcv_Field_value.equalsIgnoreCase("2W"))
            default_value = 4;
        else if (rcv_Field_value.equalsIgnoreCase("1M"))
            default_value = 5;

        EmailLog.d(DEBUG_TAG, "PeriodEmail : " + rcv_Field_value + " value : " + default_value);
        return default_value;

    }

    /**
     * This method get AutoPoll value CSC VALUE =
     * Settings.Messages.Email.EmailReceiving.AutoPoll Enumerated String : Never
     * / Every 5 mins / Every 15 mins / Every 30 mins / Every 1 hour / Every 4
     * hours / once a day
     */

    public int get_AutoPoll(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("PollTime");

        int default_value = 15; // Every 15 mins
        if (rcv_value == -1 /*|| rcv_value == 5 || rcv_value == 10*/ || rcv_value == 15
                || rcv_value == 30 || rcv_value == 60 || rcv_value == 120 || rcv_value == 240 || rcv_value == 1140)
            default_value = rcv_value;

        if (rcv_Field_value.equalsIgnoreCase("OFF"))
            default_value = -1;
//        else if (rcv_Field_value.equalsIgnoreCase("5M"))
//            default_value = 5;
//        else if (rcv_Field_value.equalsIgnoreCase("10M"))
//            default_value = 10;
        else if (rcv_Field_value.equalsIgnoreCase("15M"))
            default_value = 15;
        else if (rcv_Field_value.equalsIgnoreCase("30M"))
            default_value = 30;
        else if (rcv_Field_value.equalsIgnoreCase("1H"))
            default_value = 60;
        else if (rcv_Field_value.equalsIgnoreCase("2H"))
            default_value = 120;
        else if (rcv_Field_value.equalsIgnoreCase("4H"))
            default_value = 240;
        else if (rcv_Field_value.equalsIgnoreCase("24H"))
            default_value = 1440;

        EmailLog.d(DEBUG_TAG, "PollTime : " + rcv_Field_value + " value : " + default_value);
        return default_value;

    }

	/**
     * This method get RetrieveSizeEmail value CSC VALUE =
     * Settings.ActiveSync.Email.PeriodEmail.RetrieveSizeEmail Enumerated String : 2
     * KB / 50 KB / 100 KB / All / ALL WITH ATTACHMENTS
     */
    
    public int get_RetrieveSizeEmailDefault(int rcv_value , boolean isPop) {
        String rcv_Field_value = getFiledValue_eas("SizeEmail");

        int default_value = 51200; // 50KB
        if (rcv_value == 1 || rcv_value == 2 || rcv_value == 2048 || rcv_value == 51200 || rcv_value == 102400|| rcv_value == 307200)
            default_value = rcv_value;

        if (rcv_Field_value.equalsIgnoreCase("Headers only")  || rcv_Field_value.equalsIgnoreCase("headersonly")  )
            default_value = 0;
        else if (rcv_Field_value.equalsIgnoreCase("2K"))
            default_value = 2048;
        else if (rcv_Field_value.equalsIgnoreCase("50K"))
            default_value = 51200;
        else if (rcv_Field_value.equalsIgnoreCase("100K"))
            default_value = 102400;
        else if (!isPop && rcv_Field_value.equalsIgnoreCase("300K"))  // support only Imap
            default_value = 307200;
        else if (rcv_Field_value.equalsIgnoreCase("ALL"))
            default_value = 1;
        else if (rcv_Field_value.equalsIgnoreCase("ALLWA"))
            default_value = 2;
        EmailLog.d(DEBUG_TAG, "RetrieveSizeEmail value -  " + default_value);

        return default_value;

    }
    
    public int get_RetrieveSizeEmail(int rcv_value, boolean isPop) {
        String rcv_Field_value = getFiledValue_eas("SizeEmail");

        int default_value = 2; // 50KB
        if (isPop) {
        	if (rcv_value >= 1 && rcv_value <= 4)
                default_value = rcv_value;

            if (rcv_Field_value.equalsIgnoreCase("Headers only")  || rcv_Field_value.equalsIgnoreCase("headersonly")  )
                default_value = 0;
            else if (rcv_Field_value.equalsIgnoreCase("2K"))
                default_value = 1;
            else if (rcv_Field_value.equalsIgnoreCase("50K"))
                default_value = 2;
            else if (rcv_Field_value.equalsIgnoreCase("100K"))
                default_value = 3;
            else if (rcv_Field_value.equalsIgnoreCase("ALLWA"))
                default_value = 4;
        } else {
            if (rcv_value >= 1 && rcv_value <= 6)
                default_value = rcv_value;

            if (rcv_Field_value.equalsIgnoreCase("Headers only")
                    || rcv_Field_value.equalsIgnoreCase("headersonly"))
                default_value = 0;
            else if (rcv_Field_value.equalsIgnoreCase("2K"))
                default_value = 1;
            else if (rcv_Field_value.equalsIgnoreCase("50K"))
                default_value = 2;
            else if (rcv_Field_value.equalsIgnoreCase("100K"))
                default_value = 3;
            else if (rcv_Field_value.equalsIgnoreCase("300K"))
                default_value = 4;
            else if (rcv_Field_value.equalsIgnoreCase("ALL"))
                default_value = 5;
            else if (rcv_Field_value.equalsIgnoreCase("ALLWA"))
                default_value = 6;
        }
        EmailLog.d(DEBUG_TAG, "RetrieveSizeEmail value -  " + default_value);

        return default_value;

    }

    /**
     * This method get SizeEmail value CSC VALUE =
     * Settings.ActiveSync.EasEmail.PeriodEmail.SizeEmail Enumerated String : 2
     * KB / 5 KB / 10 KB / 20 KB / 50 KB / 100 KB / All
     */
    public int get_SizeEmail(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("SizeEmail");

        int default_value = 3; // 2KB
        if (rcv_value >= 1 && rcv_value <= 9)
            default_value = rcv_value;

        if (rcv_Field_value.equalsIgnoreCase("Headers only")  || rcv_Field_value.equalsIgnoreCase("headersonly")  )
            default_value = 0;
        else if (rcv_Field_value.equalsIgnoreCase("0.5 KB"))
            default_value = 1;
        else if (rcv_Field_value.equalsIgnoreCase("1 KB"))
            default_value = 2;
        else if (rcv_Field_value.equalsIgnoreCase("2K"))
            default_value = 3;
        else if (rcv_Field_value.equalsIgnoreCase("5K"))
            default_value = 4;
        else if (rcv_Field_value.equalsIgnoreCase("10K"))
            default_value = 5;
        else if (rcv_Field_value.equalsIgnoreCase("20K"))
            default_value = 6;
        else if (rcv_Field_value.equalsIgnoreCase("50K"))
            default_value = 7;
        else if (rcv_Field_value.equalsIgnoreCase("100K"))
            default_value = 8;
        else if (rcv_Field_value.equalsIgnoreCase("ALL"))
            default_value = 9;
        EmailLog.d(DEBUG_TAG, "SizeEmail value -  " + default_value);

        return default_value;

    }

    /**
     * This method get PeakTimeStart value CSC VALUE =
     * Settings.ActiveSync.SyncSchedule.PeakTimeStart Enumerated String : Accept
     * all time data type
     */
    public int get_PeakTimeStart() {
        String rcv_Field_value = getFiledValue_eas("PeakTimeStart");

        int default_value = CarrierValues.DEFAULT_PEAK_START_MINUTE;
        if ("NONE".equalsIgnoreCase(rcv_Field_value) == false && rcv_Field_value != null) {
            String[] Time_str;
            Time_str = rcv_Field_value.split(":");
            int hour = 0, min = 0;
            hour = (Integer.parseInt(Time_str[0]));
            min = (Integer.parseInt(Time_str[1]));

            default_value = (hour * 60) + min;
        }

        EmailLog.d(DEBUG_TAG, "PeakTimeStart -  " + default_value);

        return default_value;
    }

    /**
     * This method get PeakTimeEnd value CSC VALUE =
     * Settings.ActiveSync.SyncSchedule.PeakTimeEnd Enumerated String : Accept
     * all time data type
     */
    public int get_PeakTimeEnd() {
        String rcv_Field_value = getFiledValue_eas("PeakTimeEnd");

        int default_value = CarrierValues.DEFAULT_PEAK_END_MINUTE;

        if ("NONE".equalsIgnoreCase(rcv_Field_value) == false && rcv_Field_value != null) {
            String[] Time_str;
            Time_str = rcv_Field_value.split(":");
            int hour = 0, min = 0;
            hour = (Integer.parseInt(Time_str[0]));
            min = (Integer.parseInt(Time_str[1]));

            default_value = (hour * 60) + min;
        }

        EmailLog.d(DEBUG_TAG, "PeakTimeEnd -  " + default_value);
        return default_value;
    }

    /**
     * This method get get_PeakDaysStart/End filed value CSC VALUE =
     * Settings.ActiveSync.SyncSchedule.PeakDaysStart/PeakDaysEnd Enumerated
     * String : SecFeature.MEA_PTR ? 0x1F : 0x3e; // | -- xx Mon Tue |// Wed
     * Thur Fri xx |
     */
    public int get_PeakDays() {
        String rcv_start = getFiledValue_eas("PeakDayStart");
        String rcv_end = getFiledValue_eas("PeakDayEnd");

        int default_value;
        if(CarrierValues.IS_CARRIER_ILO) {
        	default_value = Account.DEFAULT_PEAK_DAYS_ISL; // Sun ~ Thu
        } else {
        	default_value = Account.DEFAULT_PEAK_DAYS; // Mon ~ FRI
		}

        if (rcv_start.equalsIgnoreCase("MONDAY")) {
            if (rcv_end.equalsIgnoreCase("FRIDAY")) {
                default_value = 0x3e; // Mon ~ FRI
            }

        }

        if (rcv_start.equalsIgnoreCase("SUNDAY")) // ISRAEL
        {
            if (rcv_end.equalsIgnoreCase("THURSDAY")) {
                default_value = 0x1f; // Sun-Thu
            }

        }

        return default_value;

    }

    public boolean getDefaultValue(String rcv_Field_value){
        return getDefaultValue(rcv_Field_value, false);
    }

    public boolean getDefaultValue(String rcv_Field_value, boolean default_value){

        if(rcv_Field_value != null) {
            if (rcv_Field_value.equalsIgnoreCase("OFF"))
                default_value = false;
            else if (rcv_Field_value.equalsIgnoreCase("ON"))
                default_value = true;
        }

        return default_value;
    }

    /**
     * This method get get_checkbox_filed value CSC VALUE =
     * Settings.ActiveSync.UseSecureSSL / AcceptAllSSLcertificets / SyncCalendar
     * Synccontacts / AutoSync / Whileroaming Enumerated String : ON / OFF
     */
    public boolean get_checkbox_filed(String checkbox_name) {
        String rcv_Field_value = getFiledValue_eas(checkbox_name);

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_UseSSL() {
        String rcv_Field_value = getFiledValue_eas("UseSSL");

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_AcceptAllSSLCert() {
        String rcv_Field_value = getFiledValue_eas("AcceptAllSSLCert");

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_SyncCalendar() {
        String rcv_Field_value = getFiledValue_eas("SyncCalendar");

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_SyncCalendar(boolean rcv_value) {
        String rcv_Field_value = getFiledValue_eas("SyncCalendar");

        return getDefaultValue(rcv_Field_value, rcv_value);

    }

    public boolean get_SyncContacts() {
        String rcv_Field_value = getFiledValue_eas("Synccontacts");

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_SyncContacts(boolean rcv_value) {
        String rcv_Field_value = getFiledValue_eas("Synccontacts");

        return getDefaultValue(rcv_Field_value, rcv_value);

    }

    public boolean get_SyncTasks() {
        String rcv_Field_value = getFiledValue_eas("SyncTasks");

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_SyncTasks(boolean rcv_value) {
        String rcv_Field_value = getFiledValue_eas("SyncTasks");

        return getDefaultValue(rcv_Field_value, rcv_value);

    }

    public boolean get_SyncSms() {
        String rcv_Field_value = getFiledValue_eas("SyncSms");

        return getDefaultValue(rcv_Field_value);

    }

    public boolean get_SyncSms(boolean rcv_value) {
        String rcv_Field_value = getFiledValue_eas("SyncSms");

        return getDefaultValue(rcv_Field_value, rcv_value);

    }

    public boolean get_AutoSync() {
        String rcv_Field_value = getFiledValue_eas("AutoSync");

        return getDefaultValue(rcv_Field_value);
    }

    public int isRoamPrefMenuDisplayed(){
        int default_Value = Utility.DEFAULT_ROAMING_PREFERENCE_SPRINT; //show menu item
        String field_Value = mParser.get("RoamPref.MenuDisplay");
        if (field_Value != null) {
            if (field_Value.equalsIgnoreCase("NONE") == false)
                default_Value = Integer.parseInt(field_Value);
        } else {
            field_Value = mParser.get("RoamPref.Menu");
            if(field_Value != null){
                if (field_Value.equalsIgnoreCase("NONE") == false)
                    default_Value = get_RoamingPreference(field_Value);
            }
        }
        return default_Value;
    }

    /**
     * This method reads the roaming pattern value
     * if all the values in the pattern is set to 1,The roaming option is displayed in settings
     * else the option is removed
     */
    public int get_RoamingPreference(String roaming_pattern) {
        String strarray[] = roaming_pattern.split(",");
        int sumOfPattern = 0;
            try{
                for (int count = 0; count < strarray.length ; count++) {
                    sumOfPattern += Integer.parseInt(strarray[count]);
                }
                if(sumOfPattern >= 3) {
                    return Utility.DEFAULT_ROAMING_PREFERENCE_SPRINT;
                }
            } catch (NumberFormatException ex){
                ex.printStackTrace();
                return Utility.DEFAULT_ROAMING_PREFERENCE_SPRINT;
            }
        return 0;
    }

    public int get_RoamingSetting() {
        String rcv_Field_value = getFiledValue_eas("RoamingSetting");
        int default_value = CarrierValues.CHECK_ROAMING_DEFAULT;

        if(rcv_Field_value.equalsIgnoreCase("MANUAL"))
			default_value = 0;
        else if(rcv_Field_value.equalsIgnoreCase("USE_SYNC_SETTING"))
            default_value = 1;
        EmailLog.d(DEBUG_TAG, "RoamingSettingreturn value -  " + default_value);

        return default_value;

    }

    public int get_RoamingSetting(int rcv_value) {
        String rcv_Field_value = getFiledValue_eas("RoamingSetting");
        int default_value = CarrierValues.CHECK_ROAMING_DEFAULT;
        if (rcv_value == 0 || rcv_value == 1)
            default_value = rcv_value;

        if(rcv_Field_value.equalsIgnoreCase("MANUAL"))
			default_value = 0;
        else if(rcv_Field_value.equalsIgnoreCase("USE_SYNC_SETTING"))
            default_value = 1;
        EmailLog.d(DEBUG_TAG, "RoamingSettingreturn value -  " + default_value);

        return default_value;

    }

    /**
     * This method get Signature value CSC VALUE = Settings.ActiveSync.Signature
     * Enumerated String : Accept all string type
     */
    public String get_Signature() {
        String rcv_Field_value = getFiledValue_eas("Signature");
        String default_value = "";

//        if (CSC_context != null) {
//            default_value = CSC_context
//                    .getString(R.string.account_settings_signature_default_value);
//        }
        // @change : hyelim86.lee - prevent null check start
        if(rcv_Field_value != null) {
            if (rcv_Field_value.equalsIgnoreCase("NONE") == false)
        // @change : hyelim86.lee - prevent null check end        
                default_value = rcv_Field_value;

            EmailLog.d(DEBUG_TAG, "Signature return value -  " + default_value);
        }
        return default_value;
    }

    public String get_Signature(String rcv_value) {
        String rcv_Field_value = getFiledValue_eas("Signature");
        String default_value = rcv_value;

        // @change : hyelim86.lee - prevent null check start
        if(rcv_Field_value != null) {
            if (rcv_Field_value.equalsIgnoreCase("NONE") == false)
        // @change : hyelim86.lee - prevent null check end
                default_value = rcv_Field_value;

            EmailLog.d(DEBUG_TAG, "Signature return value -  " + default_value);
        }
        return default_value;
    }
	
    public static String getCSCTagValueforEmail(String tagName) {
        String Field_value = "NONE";
        AccountSetupCustomer customer = AccountSetupCustomer.getInstance();

        if(customer != null) {
            String Tag_value = customer.getCSCTagValueforEmail(tagName);
            if(!TextUtils.isEmpty(Tag_value))
                Field_value = Tag_value.trim();
        }
  
        return Field_value;
    }
    
    /**
     * This method get AutoAdvanceDirection value CSC VALUE =
     * Settings.Messages.Email.AutoAdvance String : Next / Previous / List
     */
    
    public static int get_AutoAdvanceDirectionfromCSC(int rcv_value) {
        String rcv_Field_value = getCSCTagValueforEmail("AutoAdvance");    	
        
        int default_value = 2; // List
        if (rcv_value >= 0 && rcv_value <= 2)
            default_value = rcv_value;

        if(rcv_Field_value != null) {
            if (rcv_Field_value.equalsIgnoreCase("NONE"))
                return default_value;
	        
            if (rcv_Field_value.equalsIgnoreCase("Next"))
                default_value = 0;
            else if (rcv_Field_value.equalsIgnoreCase("Previous"))
                default_value = 1;
            else if (rcv_Field_value.equalsIgnoreCase("List"))
                default_value = 2;	
        }
        
        return default_value;
    }
    
    /**
     * This method get DeleteEmailConfirmf value CSC VALUE =
     * Settings.Messages.Email.ConfirmDelete String : ON / OFF
     */
    
    public static boolean get_DeleteEmailConfirmfromCSC(boolean rcv_value) {
        String rcv_Field_value = getCSCTagValueforEmail("ConfirmDelete");		
    	
        boolean default_value = rcv_value;

        if(rcv_Field_value != null) {    	
            if (rcv_Field_value.equalsIgnoreCase("NONE"))
                return default_value;
        
            if (rcv_Field_value.equalsIgnoreCase("OFF"))
                default_value = false;
            else if (rcv_Field_value.equalsIgnoreCase("ON"))
                default_value = true;
        }

        return default_value;
    }
	
	/**
     * This method get AutoRetryTimesfromCSC value CSC VALUE =
     * Settings.Messages.Email.AutoResendTime String : No Limit / 
     * OFF / 1 time / 3 times / 5 times / 10 times / 20 times
     */
	
    public static int get_AutoRetryTimesfromCSC(int rcv_value) {
        String rcv_Field_value = getCSCTagValueforEmail("AutoResendTime");        
        int default_value = rcv_value;

        if(rcv_Field_value != null) {		
            if (rcv_Field_value.equalsIgnoreCase("NONE"))	//if AutoResendTime is null values
                return default_value;

            if (rcv_Field_value.equalsIgnoreCase("NoLimit")){	//Defensive code for previous No Limit concept.
            	if(CarrierValues.IS_CARRIER_WHOLE_SPRINT)
            		default_value = 1000;
            	else
            		default_value = 20;
            }
            else if (rcv_Field_value.equalsIgnoreCase("OFF"))	//real None values
                default_value = 0;
            else if (rcv_Field_value.equalsIgnoreCase("1"))
                default_value = 1;
            else if (rcv_Field_value.equalsIgnoreCase("3"))
                default_value = 3;
            else if (rcv_Field_value.equalsIgnoreCase("5"))
                default_value = 5;
            else if (rcv_Field_value.equalsIgnoreCase("10"))
                default_value = 10;
            else if (rcv_Field_value.equalsIgnoreCase("20"))
                default_value = 20;
        }

        return default_value;
    }
    
}
