
package com.samsung.android.emailcommon.system;

import com.samsung.android.emailcommon.combined.common.VendorPolicyLoader.Provider;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.Utility;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class AccountSetupCustomer {
    private static final String DEBUG_TAG = "AccountSetupCustomer";

//    private static final String CUSTOMER_PATH = "/system/csc/";
//
//    private static final String CUSTOMER_XML = "customer.xml";

//    private static final String OTHERS_XML = "others.xml";

    private String mFilePath;

    private static final AccountSetupCustomer sInstance = new AccountSetupCustomer();

    static final String TAG_NODE = "Settings.Messages.Email";

    static final String TAG_CALENDAR_NODE = "Settings.Calendar";

    static final String TAG_EMAILSENDING = "EmailSending";

    static final String TAG_LIST = "Account";

    // jk0112.lee,101130,flag for combinded email
    static final String TAG_COMBINDED = "Combinded";

    // General
    static final String TAG_ACCOUNTNAME = "AccountName";

    static final String TAG_NETWORKNAME = "NetworkName";

    static final String TAG_EMAILADDR = "EmailAddr";

    static final String TAG_LOGINTYPE = "LoginType"; // username_type /

    // domain_type

    static final String TAG_USER_ID = "UserId";

    static final String TAG_PASSWORD = "Password";

    // Incoming Server
    static final String TAG_INCOMING = "Incoming";

    static final String TAG_IC_MAILBOX_TYPE = "MailboxType";

    static final String TAG_IC_SERVER = "ServAddr";

    static final String TAG_IC_PORT = "Port";

    static final String TAG_IC_DEF_PORT = "110";

    static final String TAG_IC_SECURE = "Secure";

    static final String TAG_IC_SECURE_ONOFF = "SecureLogin";

    static final String TAG_IC_AUTO_SYNC = "AutoSync";

    static final String TAG_IC_ATTACH = "Attach";

    static final String TAG_IC_OPTION = "Option";

    // Outgoing Server
    static final String TAG_OUTGOING = "Outgoing";

    static final String TAG_OG_SERVER = "ServAddr";

    static final String TAG_OG_PORT = "Port";

    static final String TAG_OG_DEF_PORT = "25";

    static final String TAG_OG_SMTPAUTH = "SmtpAuth"; // on / off

    static final String TAG_OG_AUTHENT = "Authent";

    static final String TAG_OG_SECURE = "Secure";

    static final String TAG_OG_IDPASS = "IDPassword";

    static final String TAG_OG_OPTION = "Option";

    static final String TAG_SECURE_OFF = "off";

    private Node mRoot;

    private Document mDoc;

    static Node mCustomerNode;

    static NodeList mCustomerList;

    // jk0112.lee,101130,flag for combinded email
    // minju911.kim 2011.07.07 / default setting :true->false;
    static boolean mCustomerEmailType = false;

    private static int mCustomerCount;

    private AccountSetupCustomer() {

    }

    public static AccountSetupCustomer getInstance() {
        if (sInstance != null) {
            sInstance.loadXMLFile();
        }
        return sInstance;
    }

    public static AccountSetupCustomer getInstance(String fileName) {
        if (sInstance != null) {
            sInstance.loadXMLFile(fileName);
        }
        return sInstance;
    }

    public void loadXMLFile() {
        try {
            mFilePath = CscParser.getCustomerPath();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            mDoc = builder.parse(new File(mFilePath));
            mRoot = mDoc.getDocumentElement();
            setAccountSetupCustomer();
        } catch (ParserConfigurationException ex) {
            EmailLog.e(DEBUG_TAG, "ParserConfigurationException:" + ex);
        } catch (SAXException ex) {
            EmailLog.e(DEBUG_TAG, "SAXException: " + ex);
        } catch (IOException ex) {
            EmailLog.e(DEBUG_TAG, "IOException: " + ex);
        }
    }

    public void loadXMLFile(String fileName) {
        try {
            mFilePath = CscParser.getCustomerPath();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            mDoc = builder.parse(new File(mFilePath));
            mRoot = mDoc.getDocumentElement();
            setAccountSetupCustomer();
        } catch (ParserConfigurationException ex) {
            EmailLog.e(DEBUG_TAG, "ParserConfigurationException:" + ex);
        } catch (SAXException ex) {
            EmailLog.e(DEBUG_TAG, "SAXException: " + ex);
        } catch (IOException ex) {
            EmailLog.e(DEBUG_TAG, "IOException: " + ex);
        }
    }

//    public static String getCustomerFilePath(String fileName) {
//        String filePath;
//        filePath = CUSTOMER_PATH + fileName;
//        return filePath;
//    }

    public Node getTagNode(String tagFullName) {
        if (mRoot == null) {
            return null;
        }

        Node node = mRoot;
        StringTokenizer tokenizer = new StringTokenizer(tagFullName, ".");

        while (tokenizer.hasMoreTokens()) {
            String tagName = tokenizer.nextToken();

            if (node == null) {
                return null;
            }
            node = getTagNode(node, tagName);
        }
        return node;
    }

    public Node getTagNode(Node parent, String tagName) {
        if (parent == null) {
            return null;
        }
        NodeList children = parent.getChildNodes();
        if (children != null) {
            int n = children.getLength();
            for (int i = 0; i < n; i++) {
                Node child = children.item(i);
                if (tagName.equals(child.getNodeName())) {
                    return child;
                }
            }
        }
        return null;
    }

    public boolean getTagEmailType(Node parent, String tagName) {
        if (parent == null) {
            return true;
        }
        NodeList children = parent.getChildNodes();
        if (children != null) {
            int n = children.getLength();
            for (int i = 0; i < n; i++) {
                Node child = children.item(i);
                if (child.getNodeName().equals(tagName)) {
                    String tagStr = getTagValue(child);
                    int tagInt = Integer.parseInt(tagStr);
                    if (tagInt == 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public NodeList getTagList(Node parent, String name) {
        if (mDoc == null || parent == null) {
            return null;
        }
        Element list = mDoc.createElement(parent.getNodeName());
        NodeList children = parent.getChildNodes();
        if (children != null) {
            int n = children.getLength();
            for (int i = 0; i < n; i++) {
                Node child = children.item(i);
                if (child.getNodeName().equals(name)) {
                    list.appendChild(child);
                }
            }
        }
        return list.getChildNodes();
    }

    public String getTagValue(Node node) {
        if (node == null) {
            return null;
        }
        return node.getFirstChild().getNodeValue();
    }

    public int getTagCount(NodeList list) {
        int count = 0;

        if (list != null) {
            count = list.getLength();
        }
        return count;
    }

    public void setAccountSetupCustomer() {
        mCustomerNode = getTagNode(TAG_NODE);

        // jk0112.lee,101130,flag for combinded email
        mCustomerEmailType = getTagEmailType(mCustomerNode, TAG_COMBINDED);
        mCustomerList = getTagList(mCustomerNode, TAG_LIST);
        mCustomerCount = getTagCount(mCustomerList);
    }

    public int getCustomerCount() {
        return mCustomerCount;
    }

    public Provider getProviderCustomer(String domain) {
        Provider provider = null;
        if (mCustomerNode == null) {
            EmailLog.d(DEBUG_TAG, "Error while trying to load mCustomerNode.");
            return provider;
        }
        EmailLog.d(DEBUG_TAG, "mCustomerCount: " + mCustomerCount);

        for (int i = 0; i < mCustomerCount; i++) {
            final Node accountNodeListChild = mCustomerList.item(i);
            // General
            String accountName = getTagValue(getTagNode(accountNodeListChild, TAG_ACCOUNTNAME));
            String emailAddr = getTagValue(getTagNode(accountNodeListChild, TAG_EMAILADDR));

            //ds7.hwang - ex. "@gmail.com" in MPS, it will be "gmail.com"
            if( emailAddr != null && emailAddr.contains("@") ){
                String[] part = emailAddr.split("@");
                emailAddr = part[part.length-1];
            }

            if (domain.equalsIgnoreCase("yahoo.com") && "Yahoo! Mail".equalsIgnoreCase(accountName)) {
                // TIM CSC not exist emailaddr of Yahoo.com by the tim spec.
                StringBuffer logsbuf = new StringBuffer();
                EmailLog.d(DEBUG_TAG, logsbuf.append("domain[yahoo]: ").append(domain)
                		        .append(", emailAddr: ").append(emailAddr).toString());
            } else if (emailAddr == null || !emailAddr.equalsIgnoreCase(domain)) {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer logsbuf = new StringBuffer();
                EmailLog.d(DEBUG_TAG, logsbuf.append("domain: ").append(domain).append(", emailAddr: ")
                        .append(emailAddr).toString());
                continue;
            }
            String serverType = null;
            String secureType = null;
            String serverAddr = null;
            String serverPort = null;
            String userName = null;
            String incomingUri = null;
            String outgoingUri = null;

            // incoming
            NodeList incomingList = getTagList(accountNodeListChild, TAG_INCOMING);
            /* add for resolve prevent defect, 100412 fidelis.lee samsung */
            if (incomingList == null) {
                return null;
            }
            // end of add
            // serverType: pop3, imap
            serverType = getTagValue(getTagNode(incomingList.item(0), TAG_IC_MAILBOX_TYPE));

            /* add for resolve prevent defect, 100412 fidelis.lee samsung */
            if (serverType == null) {
                return null;
            }
            // end of add

            if (serverType.equalsIgnoreCase("imap3") || serverType.equalsIgnoreCase("imap4") || serverType.equalsIgnoreCase("imap")) {
                serverType = "imap";
            } else {
                serverType = "pop3";
            }

            // secureType: none, tls, tls+, ssl, ssl+
            secureType = getTagValue(getTagNode(incomingList.item(0), TAG_IC_SECURE));

            if (secureType != null && !secureType.equalsIgnoreCase(TAG_SECURE_OFF)) {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer typesbuf = new StringBuffer();
                secureType = typesbuf.append('+').append(secureType).append('+').toString();
            } else {
                secureType = "";
            }

            // serverAddr
            serverAddr = getTagValue(getTagNode(incomingList.item(0), TAG_IC_SERVER));
            // if(secureType != null)
            {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer addrsbuf = new StringBuffer();
                serverAddr = addrsbuf.append("://").append(serverAddr).toString();
            }
            // serverPort
            serverPort = getTagValue(getTagNode(incomingList.item(0), TAG_IC_PORT));
            if (serverPort != null) {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer portsbuf = new StringBuffer();
                serverPort = portsbuf.append(':').append(serverPort).toString();
            }else {
                serverPort = "";
            }
            // username
            userName = getTagValue(getTagNode(accountNodeListChild, TAG_LOGINTYPE));
            if (userName != null && userName.equalsIgnoreCase("username_type")) {
                userName = "$user";
            } else {
                userName = "$email";
            }
            /*
             * //username userId = getTagValue(getTagNode(mCustomerList.item(i),
             * TAG_USER_ID)); if( userId != null && userId.contains("@")) {
             * userName = "$email"; }
             */
            // == Resolve memory defect by RSAR: yonghee2.lee 20100503 samsung
            StringBuffer inurisbuf = new StringBuffer();
            inurisbuf.append(serverType);
            if (secureType != null) {
                inurisbuf.append(secureType);
            }
            incomingUri = inurisbuf.append(serverAddr).append(serverPort).toString();

            // == Resolve memory defect by RSAR: yonghee2.lee 20100503 samsung
            StringBuffer insbuf = new StringBuffer();
            EmailLog.d(DEBUG_TAG, insbuf.append("incomingUri: ").append(incomingUri).toString());
            // outgoing
            NodeList outgoingList = getTagList(accountNodeListChild, TAG_OUTGOING);
            if (outgoingList == null) {
                return null;
            }
            // serverType: smtp
            serverType = "smtp";
            // secureType: none, tls, tls+, ssl, ssl+
            secureType = getTagValue(getTagNode(outgoingList.item(0), TAG_OG_SECURE));
            if (secureType != null && !secureType.equalsIgnoreCase(TAG_SECURE_OFF)) {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer typebuf = new StringBuffer();
                secureType = typebuf.append('+').append(secureType).append('+').toString();
            } else {
                secureType = "";
            }

            // serverAddr
            serverAddr = getTagValue(getTagNode(outgoingList.item(0), TAG_OG_SERVER));
            // if(secureType != null)
            {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer addrbuf = new StringBuffer();
                serverAddr = addrbuf.append("://").append(serverAddr).toString();
            }

            // serverPort
            serverPort = getTagValue(getTagNode(outgoingList.item(0), TAG_OG_PORT));
            if (serverPort != null) {
                // == Resolve memory defect by RSAR: yonghee2.lee 20100503
                // samsung
                StringBuffer portbuf = new StringBuffer();
                serverPort = portbuf.append(':').append(serverPort).toString();
            }
            else {
                serverPort = "";
            }
            // == Resolve memory defect by RSAR: yonghee2.lee 20100503 samsung
            StringBuffer outuribuf = new StringBuffer();
            outuribuf.append(serverType);
            if (secureType != null) {
                outuribuf.append(secureType);
            }
            outgoingUri = outuribuf.append(serverAddr).append(serverPort).toString();
            // == Resolve memory defect by RSAR: yonghee2.lee 20100503 samsung
            StringBuffer outbuf = new StringBuffer();

            EmailLog.d(DEBUG_TAG, outbuf.append("outgoingUri: ").append(outgoingUri).toString());
            String smtpAuth = getTagValue(getTagNode(outgoingList.item(0), TAG_OG_SMTPAUTH));

            try {
                provider = new Provider();
                provider.id = accountName;
                provider.label = accountName;
                provider.domain = domain;
                provider.note = null; // not support in customer.xml
                
                if (Utility.isMainlandChinaModel()
                		&& "pop3".equals(serverType)) {
                	provider.incomingUriTemplate_pop = new URI(incomingUri).toString();
                } else {
                	provider.incomingUriTemplate = new URI(incomingUri).toString();
                }
                
                provider.incomingUsernameTemplate = userName;
                provider.outgoingUriTemplate = new URI(outgoingUri).toString();
                if (smtpAuth == null || smtpAuth.equalsIgnoreCase("on")) {
                    provider.outgoingUsernameTemplate = userName;
                } else {
                    provider.outgoingUsernameTemplate = "";
                }
                // /* [ANDROID_NWFW] ds7.hwang 20.May.2010 start ==[[
                EmailLog.d(DEBUG_TAG, "<provider id=\"" + provider.id + "\" label=\"" + provider.label
                        + "\" domain=\"" + provider.domain + "\">");
                
                if (Utility.isMainlandChinaModel()
                		&& "pop3".equals(serverType)) {
                	EmailLog.d(DEBUG_TAG, "\t<incoming uri=\"" + provider.incomingUriTemplate_pop
                            + "\" username=\"" + provider.incomingUsernameTemplate + "\">");
                } else {
                	EmailLog.d(DEBUG_TAG, "\t<incoming uri=\"" + provider.incomingUriTemplate
                            + "\" username=\"" + provider.incomingUsernameTemplate + "\">");
                }
                
                EmailLog.d(DEBUG_TAG, "\t<outgoing uri=\"" + provider.outgoingUriTemplate
                        + "\" username=\"" + provider.outgoingUsernameTemplate + "\">");
                EmailLog.d(DEBUG_TAG, "</provider>");
                // * [ANDROID_NWFW] ds7.hwang 20.May.2010 ]]== End */
                return provider;
            } catch (Exception e) {
                EmailLog.e(DEBUG_TAG, "Error while trying to load customer provider settings.", e);
            }
        }
        return provider;
    }

    public String getStartDay() {
        Node node = getTagNode(TAG_CALENDAR_NODE);

        return getTagStartDay(node, "StartDay");
    }

    public String getTagStartDay(Node parent, String tagName) {
        if (parent == null) {
            return null;
        }
        NodeList children = parent.getChildNodes();
        if (children != null) {
            int n = children.getLength();
            for (int i = 0; i < n; i++) {
                Node child = children.item(i);
                if (child.getNodeName().equals(tagName)) {
                    String tagStr = getTagValue(child);

                    return tagStr;
                }
            }
        }

        return null;
    }

    public String getCSCTagValueforEmail(String name) {
        Node node = getTagNode(TAG_NODE);

        return getTagEmail(node, name);
    }

    public String getTagEmail(Node parent, String tagName) {
        if (parent == null) {
            return null;
        }
        NodeList children = parent.getChildNodes();
        if (children != null) {
            int n = children.getLength();
            for (int i = 0; i < n; i++) {
                Node child = children.item(i);
                if (child.getNodeName().equalsIgnoreCase(tagName)) {
                    String tagStr = getTagValue(child);

                    return tagStr;
                }

                if (child.getNodeName().equalsIgnoreCase(TAG_EMAILSENDING)) {
                    Node node = getTagNode(TAG_NODE + "." + TAG_EMAILSENDING);
                    String tagEmailNodeStr = getTagEmail(node, tagName);

                    if (tagEmailNodeStr != null)
                        return tagEmailNodeStr;
                }
            }
        }

        return null;
    }
}
