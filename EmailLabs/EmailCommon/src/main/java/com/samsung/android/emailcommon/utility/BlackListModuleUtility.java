
package com.samsung.android.emailcommon.utility;

import java.util.ArrayList;
import java.util.List;


public class BlackListModuleUtility {

    public static boolean isSenderBlockedByUser(String senderAddress, List<String> blackListSenders) {
        if (null != senderAddress && null != blackListSenders && blackListSenders.size() > 0) {
            int count = 0;
            ArrayList<String> senderList = getSenderList(senderAddress);
            while (count < senderList.size()) {
                if (blackListSenders.contains(senderList.get(count))) {
                    return true;
                }
                count++;
            }
        }
        return false;
    }

    /**
     * This method prepares the list of senderaddress, domain and subdomain
     * 
     * @param senderAddress
     * @return arraylist of the sender, domain and subdomain
     */
    public static ArrayList<String> getSenderList(String senderAddress) {
        ArrayList<String> senderList = new ArrayList<String>();
        String domain = null;
        // add the sender address to the list
        if (senderAddress != null) {
            senderList.add(senderAddress);

            // add the domain of the sender to the list
            int amp = senderAddress.indexOf('@');
            domain = senderAddress.substring(amp + 1);
            if (domain != null) {
                senderList.add(domain);
            }
        }
        // add the subdomain of the sender to the list
        String arg[] = null;
        if (domain != null) {
            arg = domain.split("\\.");
        }
        if (arg != null && arg.length > 2) {
            String subDomain = getSubDomain(domain);
            if (subDomain != null) {
                senderList.add(subDomain);
            }
        }
        return senderList;
    }

    /**
     * This method returns last two words of a domain e.g. for the domain
     * "sales.samsung.com" it will return "sumsung.com"
     * 
     * @param domain
     * @return subdomain
     */
    public static String getSubDomain(String domain) {
        String subDomain = null;
        int pos = domain.length();
        for (int i = 0; i < 2; i++) {
            pos = domain.lastIndexOf('.', pos - 1);
        }
        subDomain = domain.substring(pos + 1);
        return subDomain;
    }
}
