
package com.samsung.android.emailcommon.utility;

import android.content.Context;
import android.content.Intent;

import com.samsung.android.emailcommon.mail.MessagingException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewPatternMatching {
    private static final String TAG = "MessageView P-matching>>";
    // global const
    public static final int PATTERNMATCHING_OPTION_DEFAULT = 0x17;
    public static final int PATTERNMATCHING_OPTION_ALL = 0xff;
    public static final int PATTERNMATCHING_NONE = 0;
    public static final int PATTERNMATCHING_ENABLE_PHONE = 1;
    public static final int PATTERNMATCHING_ENABLE_CALENDAR = 2;
    public static final int PATTERNMATCHING_ENABLE_CALENDAR_KEYWORD = 4;
    public static final int PATTERNMATCHING_ENABLE_ADDR = 8;
    public static final int PATTERNMATCHING_LANG_USE_PHONESETTING = 0x10;
    public static final int PATTERNMATCHING_LANG_USE_GEOLOCATION = 0x20;
    public static final int PATTERNMATCHING_LANG_USE_ALL = 0xF0;

    private static final int PATTERN_MATCHING_TIMEOUT = 4000;
    // internal option
    private static final boolean enableStreetView = false;
    boolean enablePhyAddressCheck = false;// use network...

    // internal const
    private int nTimeout = PATTERN_MATCHING_TIMEOUT;// time out - ms

    private static final int PATTERN_SUPPORT_LANG = 0x00;
    private static final int PATTERN_LOCATION_LOCALENG = 0x01;// pattern_location_localeng
    private static final int PATTERN_LOCATION_LOCALLANG = 0x02;// pattern_location_locallang
    private static final int PATTERN_DATETIME_LOCALDATE = 0x10;// calendar_localdate
    private static final int PATTERN_DATETIME_LOCALTIME = 0x11;// calendar_localtime
    private static final int PATTERN_DATETIME_KEYWORDWEEK = 0x12;// calendar_localkeywordweek
    private static final int PATTERN_DATETIME_KEYWORDMOMENT = 0x13;// calendar_localkeywordmoment
    private static final int PATTERN_DATETIME_KEYWORDSPECIAL = 0x14;// calendar_localkeywordspecial
    private static final int PATTERN_DATETIME_DATETIME_SEPARATOR = 0x15;// new
    private static final int PATTERN_DATETIME_REPLACEINPUT = 0x20;// calendar_localreplaceinput
    private static final int PATTERN_DATETIME_REPLACEOUTPUT = 0x21;// calendar_localreplaceoutput
    private static final int PATTERN_DATETIME_REPLACECONFLICTINPUT = 0x22;// calendar_localreplaceconflictinput
    private static final int PATTERN_DATETIME_REPLACECONFLICTOUTPUT = 0x23;// calendar_localreplaceconflictoutput

    private static final int nDatePostLen = 30;
    private static final int nTimePostLen = 18;
    private static final int nMatchingPostPos = 4;

    private static boolean enablePatternPhone = true;
    private static boolean enablePatternCalendar = true;
    private static boolean enablePatternLocation = false;
    private static boolean enablePatternCalendarKeyword = true;
    private static boolean enablePatternLangUsePhone = true;
    private static boolean enablePatternLangUseGeo = false;
    private static boolean enablePatternLangAll = false;

    // variables
    private Context mContext;
    private long mTimeOfMessage = 0;
    private int mCurrentPos = 0;

    private static final String PATTERN_ERR_STRING = "z/z";
    private static final String[] mPattern_support_lang = {
            "kor", "zho", "jpn"
    };
    private static final String[] mPattern_location_localeng = {
            "(?:(?:[\\w\\-]{2,20}(?:[Dd]o|[Ss]i)[\\s,]{0,3})?[\\w\\-]{2,20}(?:[Ss]i|[Gg]un|[Dd]o)?[\\s,]{0,3}(?:(?:[\\w\\-]{2,20}(?:[Ss]i|[Gg]u)[\\s,]{0,3}[\\w\\-]{2,20}(?:[Ee]up|[Mm]yeon))|(?:[\\w\\-]{2,20}(?:[Ss]i|[Gg]u|[Ee]up|[Mm]yeon)))[\\s,]{1,3}[\\w\\-\\(\\)]{2,20}(?:[Rr]i|[Dd]ong|[Rr]o|[Gg]a|[Gg]il)[\\s,]{1,3}[\\w]{1,9}(?:[\\-\\s@#]\\w{1,9})?)|(?:[\\d]{1,5}(?:[\\-\\s@#][\\d]{1,5})?(?:[\\s,]{1,3}[a-zA-Z]{1,12}(?:[\\s\\-][a-zA-Z]{0,4}[Rr]o)?)?[\\s,]{1,3}[\\w\\-\\(\\)]{1,20}(?:[Rr]i|[Dd]ong|[a-zA-Z]{0,4}[Rr]o|[Gg]a|[Gg]il)[\\s,]{1,3}[\\w\\-]{1,20}(?:(?:[\\w\\-]{2,20}(?:[Ee]up|[Mm]yeon)[\\s,]{0,3}[\\w\\-]{2,20}(?:[Ss]i|[Gg]u))|(?:[\\w\\-]{2,20}(?:[Ss]i|[Gg]u|[Ee]up|[Mm]yeon)))[\\s,]{1,3}[a-zA-Z\\-]{1,20}(?:[Ss]i|[Gg]un|[Dd]o)?([\\s,]{0,3}[a-zA-Z\\-]{0,20}(?:[Ss]i|[Dd]o)?)?(?:[\\s,]{1,3}(?:[Ss]outh[\\s])?[Kk][Oo][Rr][a-zA-Z\\.]*)?)",
            "(?:\\b(?:[\\d]{1,4}[\u53f7\u865f]?(?:\\s|\\,|(?:\\&nbsp;)){1,3})(?:[\u00a0-\uaf00\\'\\w\\s#@\\-\\,\\.]{4,40})(?:\\s|\\,|(?:\\&nbsp;)){0,3}(?:Beijing|Chengdu|Chongqing|Changsha|Fuzhou|Guangzhou|Hangzho|Haerbin|Hefei|Hong\\sKong|Jiangsu|Jinan|Kunming|Lanzhou|macau|Nanjing|Ningbo|Ruian|Shandong|Shanghai|Shenyang|Shenzhen|Shijiazhuang|Suzhou|Tianjin|Weifang|Wuhan|Wulumuqi|Xi[\\']?an|Yinchuan|Zhanjiang|Zhengzhou)\\b(?:\\s|\\,|(?:\\&nbsp;)){0,3}(?:[Cc]hina)?)",
            PATTERN_ERR_STRING
    };
    private static final String[] mPattern_location_locallang = {
            "(?:(?:\\w{1,6}[\ub3c4\uc2dc][\\s])?\\w{1,6}[\ub3c4\uc2dc\uad70][\\s](?:(?:\\w{1,6}[\uc2dc\uad6c][\\s]\\w{1,6}[\uc74d\uba74])|(?:\\w{1,6}[\uc2dc\uad6c\uc74d\uba74]))[\\s]\\w{1,9}[\ub9ac\ub3d9\ub85c\uac00\uae38]\\s\\w{1,9}(?:[\\-\\s@#]\\w{1,9})?)",
            "(?:\\b(?:(?:\u422d\u56fd|\u9999\u6e2f)(?:\\s|\\,|(?:\\&nbsp;)|(?:\\&middot;)){0,3})?)(?:\\b[\\w\u00a0-\uaf00](?:(?:(?:[\u00a0-\uaf00\\'\\w\\s#@\\-\\,\\.]|(?:\\&nbsp;)){2,8})[\u5e02\u53bf\u533a\u5340\u9053\u8857]){1,3})(?:(?:[\u00a0-\uaf00\\'\\w\\s#@\\-\\,\\.]{0,10})[\u91cc\u8def\u53f7\u865f\u6a13\\d])\\b",
            "(?:[\u3012\u3036][\\d]{3}[\\s\\-]{0,2}[\\d]{4}(?:[\\s\\x0d\\x0a]|(?:\\&nbsp;)){0,3}(?i:<[^>]+>(?:[\\s\\x0d\\x0a]|(?:\\&nbsp;)){0,3}){0,5})?\\b[\u00a0-\uaf00\\w#\\-\\,]{1,6}[\u90FD\u9053\u5E9C\u770C\u5E02][\u00a0-\uaf00\\w#\\-\\,]{1,6}[\u5E02\u533A\u753A\u6751](?:[\u00a0-\uaf00\\w#\\-\\,]{0,10}[\\w]\\b)?\\b"
    };
    private static final String[] mPattern_datetime_localdate = {
            "(?:[1-2][\\d]{3}(?:\\s|(?:\\&nbsp;))?[\\-\\/\\.\ub144](?:\\s|(?:\\&nbsp;))?[0-1]?\\d(?:\\s|(?:\\&nbsp;))?[\\-\\/\\.\uc6d4](?:\\s|(?:\\&nbsp;))?[0-3]?\\d(?:(?:\\s|(?:\\&nbsp;))?[\\.\uc77c])?)(?:(?:\\s|(?:\\&nbsp;)){0,3}(?:\\(|\\s|(?:\\&nbsp;)){0,3}[\uc77c\uc6d4\ud654\uc218\ubaa9\uae08\ud1a0]\\w{0,2}(?:\\s|(?:\\&nbsp;))?[\\)]?)?",
            PATTERN_ERR_STRING,
            "(?:[1-2][\\d]{3}(?:\\s|(?:\\&nbsp;))?[\\-\\/\\.\u5e74](?:\\s|(?:\\&nbsp;))?[0-1]?\\d(?:\\s|(?:\\&nbsp;))?[\\-\\/\\.\u6708](?:\\s|(?:\\&nbsp;))?[0-3]?\\d(?:(?:\\s|(?:\\&nbsp;))?[\\.\u65e5])?)(?:(?:\\s|(?:\\&nbsp;)){0,3}(?:\\(|\\s|(?:\\&nbsp;)){0,3}[\u65e5\u6708\u706b\u6c34\u6728\u91d1\u571f]\\w{0,2}(?:\\s|(?:\\&nbsp;))?[\\)]?)?"
    };
    private static final String mPattern_datetime_localdateAll = "(?:[1-2][\\d]{3}(?:\\s|(?:\\&nbsp;))?[\\-\\/\\.\ub144\u5e74](?:\\s|(?:\\&nbsp;))?[0-1]?\\d(?:\\s|(?:\\&nbsp;))?[\\-\\/\\.\uc6d4\u6708](?:\\s|(?:\\&nbsp;))?[0-3]?\\d(?:(?:\\s|(?:\\&nbsp;))?[\\.\uc77c\u65e5])?)(?:(?:\\s|(?:\\&nbsp;)){0,3}(?:\\(|\\s|(?:\\&nbsp;)){0,3}[\uc77c\uc6d4\ud654\uc218\ubaa9\uae08\ud1a0\u65e5\u6708\u706b\u6c34\u6728\u91d1\u571f]\\w{0,2}(?:\\s|(?:\\&nbsp;))?[\\)]?)?";
    private static final String[] mPattern_datetime_localtime = {
            "(?:[0-2]?[\\d](?:\\s|(?:\\&nbsp;))?[\uc2dc](?:\\s|(?:\\&nbsp;))?[0-6]?[\\d](?:\\s|(?:\\&nbsp;))?[\ubd84])|(?:[0-2]?[\\d](?:\\s|(?:\\&nbsp;))?[\uc2dc])",
            PATTERN_ERR_STRING,
            "(?:[0-2]?[\\d](?:\\s|(?:\\&nbsp;))?[\u6642](?:\\s|(?:\\&nbsp;))?[0-6]?[\\d](?:\\s|(?:\\&nbsp;))?[\u5206])|(?:[0-2]?[\\d](?:\\s|(?:\\&nbsp;))?[\u6642])"
    };
    private static final String mPattern_datetime_localtimeAll ="(?:[0-2]?[\\d](?:\\s|(?:\\&nbsp;))?[\uc2dc\u6642](?:\\s|(?:\\&nbsp;))?[0-6]?[\\d](?:\\s|(?:\\&nbsp;))?[\ubd84\u5206])|(?:[0-2]?[\\d](?:\\s|(?:\\&nbsp;))?[\uc2dc\u6642])";
    private static final String[] mPattern_datetime_keywordweek = {
            "\uc77c(?:\uc694\uc77c)*|\uc6d4(?:\uc694\uc77c)*|\ud654(?:\uc694\uc77c)*|\uc218(?:\uc694\uc77c)*|\ubaa9(?:\uc694\uc77c)*|\uae08(?:\uc694\uc77c)*|\ud1a0(?:\uc694\uc77c)*",
            PATTERN_ERR_STRING, // sun/mon/...
            "\u65e5(?:\u66dc\u65e5)*|\u6708(?:\u66dc\u65e5)*|\u706b(?:\u66dc\u65e5)*|\u6c34(?:\u66dc\u65e5)*|\u6728(?:\u66dc\u65e5)*|\u91d1(?:\u66dc\u65e5)*|\u571f(?:\u66dc\u65e5)*"
    };
    private static final String[] mPattern_datetime_keywordmoment = {
            "\uc0c8\ubcbd|\uc544\uce68|\uc624\uc804|\uc624\ud6c4|\uc800\ub141|\ubc24",
            PATTERN_ERR_STRING, // the dawn, morning, morning, noon, ...
            "\u65e9\u671d|\u5348\u524d|\u5348\u524d|\u5348\u5f8c|\u5348\u5f8c|\u591c"
    };
    private static final String[] mPattern_datetime_keywordspecial = {
            "\uc624\ub298|\uae08\uc77c|\ub0b4\uc77c|\uba85\uc77c|\uba85\uc77c",
            PATTERN_ERR_STRING, // today, today, tomorrow, tomorrow, not used(special)
            "\u4eca\u65e5|\u4eca\u65e5|\u660e\u65e5|\u660e\u65e5|\u660e\u65e5"
    };
    private static final String[] mPattern_datetime_localseparator = {
            "\ubd80\ud130",
            PATTERN_ERR_STRING,
            "\u304b\u3089"
    };
    private static final String[] mPattern_datetime_replaceinput = {
            "\ubc24|\uc2dc|\ubd84|[\uc77c\uc6d4\ud654\uc218\ubaa9\uae08\ud1a0]\uc694\uc77c|\ubd80\ud130",
            PATTERN_ERR_STRING,
            "\u591c|\u6642|\u5206|[\u65e5\u6708\u706b\u6c34\u6728\u91d1\u571f]\u66dc\u65e5|\u66dc|\u304b\u3089"
    };
    private static final String[] mPattern_datetime_replaceoutput = {
            "pm|:|^|^|^",
            PATTERN_ERR_STRING,
            "pm|:|^|^|^|^"
    };
    private static final String[] mPattern_datetime_replaceconflictinput = {
            "\ub144|\uc6d4|\uc694|\uc77c",
            PATTERN_ERR_STRING,
            "\u5e74|\u6708|\u66dc|\u65e5"
    };
    private static final String[] mPattern_datetime_replaceconflictoutput = {
            "/|/|^|^",
            PATTERN_ERR_STRING,
            "/|/|^|^"
    };
    private static final String Pattern_DateTime_KeywordWeek = "[Ss]un[\\w\\.]{0,4}|[Mm]on[\\w\\.]{0,4}|[Tt]ue[\\w\\.]{0,5}|[Ww]ed[\\w\\.]{0,7}|[Tt]hu[\\w\\.]{0,6}|[Ff]ri[\\w\\.]{0,4}|[Ss]at[\\w\\.]{0,6}";

    // private const
    // Regex that matches Web URL protocol part as case insensitive.
    private static final Pattern WEB_URL_PROTOCOL = Pattern.compile("(?i)http|https|rtsp://");
    private static final String GOOD_IRI_CHAR = "a-zA-Z0-9\u00C0-\uD7FF\uF900-\uFDCF\uFDF0-\uFF19\uFF21-\uFFDF";
    //private static final String GOOD_IRI_CHAR = "a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF";
    private static final String TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL = "(?:"
            + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
            + "|(?:biz|b[abdefghijmnorstvwyz])"
            + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
            + "|d[ejkmoz]"
            + "|(?:edu|e[cegrstu])"
            + "|f[ijkmor]"
            + "|(?:gov|g[abdefghilmnpqrstuwy])"
            + "|h[kmnrtu]"
            + "|(?:info|int|i[delmnoqrst])"
            + "|(?:jobs|j[emop])"
            + "|k[eghimnprwyz]"
            + "|l[abcikrstuvy]"
            + "|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])"
            + "|(?:name|net|n[acefgilopruz])"
            + "|(?:org|om)"
            + "|(?:pro|p[aefghklmnrstwy])"
            + "|qa"
            + "|r[eosuw]"
            + "|s[abcdeghijklmnortuvyz]"
            + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
            + "|u[agksyz]"
            + "|v[aceginu]"
            + "|w[fs]"
            + "|(?:xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn\\-\\-80akhbyknj4f|xn\\-\\-9t4b11yi5a|xn\\-\\-deba0ad|xn\\-\\-g6w251d|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-zckzah)"
            + "|y[etu]" + "|z[amw]))";

    private static final Pattern OWN_WEB_URL = Pattern
            .compile("(?:(?:(?:http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                    + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                    + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                    + "(?:(?:(?:["
                    + GOOD_IRI_CHAR
                    + "]["
                    + GOOD_IRI_CHAR
                    + "\\-]{0,64}\\.)+" // named host
                    + TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL
                    + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                    + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9])))" + "(?:\\:\\d{1,5})?)" // plus
                    // option
                    // port
                    // number
                    + "(?:[\\/\\?](?:(?:[" + GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~" // plus
                    // option
                    // query
                    // params
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?" + "(?:\\b|$)"); // and

    private static final Pattern EMAIL_ADDRESS = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "(?:\\@|(?:\\&\\#[0]*64\\;))"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(?:" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

// end constant
    
    public ViewPatternMatching(){
    }

    private void pmLoadArray(Context ct) throws MessagingException {
        if (ct == null)
            return;
		/*	if you want to use resource file...
        String[] langs = ct.getResources().getStringArray(R.array.pattern_support_langs);
        if (langs != null && langs.length > 1) {
            int len = langs.length;
            if (len > mPattern_support_lang.length)
                len = mPattern_support_lang.length;
            for (int i = 0; i < len; i++) {
                if (langs[i] != null && !langs[i].isEmpty())
                    mPattern_support_lang[i] = langs[i];
            }
        } ...
		*/
    }

    public interface PatternMatchingCallBack {
        String getAppName();
    }

    PatternMatchingCallBack mCallBack = null;
    public void setCallBack(PatternMatchingCallBack callBack) {
        mCallBack = callBack;
    }

    private String pmGetDataFromField(int nCnt, String[] mPattern, int[] findIndex)
            throws MessagingException {
        String outResult = "";
        boolean bFound = false;

        for (int i = 0; i < nCnt; i++) {
            if (!PATTERN_ERR_STRING.equalsIgnoreCase(mPattern[findIndex[i]])) {
                if (bFound)
                    outResult = outResult.concat("|");
                bFound = true;
                outResult = outResult.concat("(?:" + mPattern[findIndex[i]] + ")");
            }
        }
        if (!bFound)
            outResult = PATTERN_ERR_STRING;
        return outResult;
    }

    private String pmGetDataFromFieldAddSpace(int nCnt, String[] mPattern, int[] findIndex)
            throws MessagingException {
        String outResult = "";
        boolean bFound = false;

        for (int i = 0; i < nCnt; i++) {
            if (!PATTERN_ERR_STRING.equalsIgnoreCase(mPattern[findIndex[i]])) {
                bFound = true;
                outResult = outResult.concat(mPattern[findIndex[i]] + " ");
            }
        }
        if (!bFound)
            outResult = PATTERN_ERR_STRING;
        else
            outResult = outResult.trim();

        return outResult;
    }

    private String pmGetResource(int nID) {
        final String errStr = PATTERN_ERR_STRING;
        String outResult = PATTERN_ERR_STRING;
        int findIndex[] = {
                0, 0, 0
        };
        int nCnt = 0, i = 0;
        boolean bFound = false;

        if (enablePatternLangAll) {
            for (i = 0; i < mPattern_support_lang.length; i++)
                findIndex[nCnt++] = i;
            bFound = true;
        } else {
            if (enablePatternLangUsePhone) {
                try {
                    Locale currentLocale = Locale.getDefault();
                    final String currentLang = currentLocale.getISO3Language();
                    for (i = 0; i < mPattern_support_lang.length; i++) {
                        if (currentLang.equalsIgnoreCase(mPattern_support_lang[i])) {
                            findIndex[nCnt++] = i;
                            bFound = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    return errStr;
                }
            }
            // not supported yet...
            // if (enablePatternLangUseGeo) {}
        }
        // err check
        if (nCnt < 1 || nCnt > findIndex.length)
            return errStr;
        for (i = 0; i < nCnt; i++) {
            if (findIndex[i] == -1 || findIndex[i] >= mPattern_support_lang.length) {
                bFound = false;
                break;
            }
        }
        if (!bFound)
            return errStr;

        try {
            switch (nID) {
                case PATTERN_SUPPORT_LANG:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_support_lang, findIndex);
                    break;
                case PATTERN_LOCATION_LOCALENG:
                    outResult = pmGetDataFromField(nCnt, mPattern_location_localeng, findIndex);
                    break;
                case PATTERN_LOCATION_LOCALLANG:
                    outResult = pmGetDataFromField(nCnt, mPattern_location_locallang, findIndex);
                    break;
                case PATTERN_DATETIME_LOCALDATE:
                    if (enablePatternLangAll)
                        outResult = mPattern_datetime_localdateAll;
                    else
                        outResult = pmGetDataFromField(nCnt, mPattern_datetime_localdate, findIndex);
                    break;
                case PATTERN_DATETIME_LOCALTIME:
                    if (enablePatternLangAll)
                        outResult = mPattern_datetime_localtimeAll;
                    else
                        outResult = pmGetDataFromField(nCnt, mPattern_datetime_localtime, findIndex);
                    break;
                case PATTERN_DATETIME_KEYWORDWEEK:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_datetime_keywordweek,
                            findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_KEYWORDMOMENT:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_datetime_keywordmoment,
                            findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_KEYWORDSPECIAL:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_datetime_keywordspecial,
                            findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_DATETIME_SEPARATOR:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_datetime_localseparator,
                            findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_REPLACEINPUT:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_datetime_replaceinput,
                            findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_REPLACEOUTPUT:
                    outResult = pmGetDataFromFieldAddSpace(nCnt, mPattern_datetime_replaceoutput,
                            findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_REPLACECONFLICTINPUT:
                    outResult = pmGetDataFromFieldAddSpace(nCnt,
                            mPattern_datetime_replaceconflictinput, findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                case PATTERN_DATETIME_REPLACECONFLICTOUTPUT:
                    outResult = pmGetDataFromFieldAddSpace(nCnt,
                            mPattern_datetime_replaceconflictoutput, findIndex);
                    outResult = outResult.replaceAll(" ", "|");
                    break;
                default:
                    return errStr;
            }
        } catch (Exception e) {
            return errStr;
        }
        if (outResult.contains(errStr))
            return errStr;
        return outResult;
    }

    public void pmInit(Context ct, int nOption, long nLocalTimeOfMessage) {
        mContext = ct;
        if (nOption < 0)
            nOption = PATTERNMATCHING_OPTION_DEFAULT;
        pmSetEnableOption(PATTERNMATCHING_OPTION_ALL, false);
        pmSetEnableOption(nOption, true);
        mTimeOfMessage = nLocalTimeOfMessage;
        mCurrentPos = 0;
        nTimeout = PATTERN_MATCHING_TIMEOUT;// allways default
        try {
            pmLoadArray(ct);
        } catch (Exception e) {
        }
    }

    public void pmSetEnableOption(int nOption, boolean value) {
        if (nOption < 0)
            nOption = PATTERNMATCHING_OPTION_DEFAULT;
        if ((nOption & PATTERNMATCHING_ENABLE_PHONE) == PATTERNMATCHING_ENABLE_PHONE)
            enablePatternPhone = value;
        if ((nOption & PATTERNMATCHING_ENABLE_CALENDAR) == PATTERNMATCHING_ENABLE_CALENDAR)
            enablePatternCalendar = value;
        if ((nOption & PATTERNMATCHING_ENABLE_CALENDAR_KEYWORD) == PATTERNMATCHING_ENABLE_CALENDAR_KEYWORD)
            enablePatternCalendarKeyword = value;
        if ((nOption & PATTERNMATCHING_ENABLE_ADDR) == PATTERNMATCHING_ENABLE_ADDR)
            enablePatternLocation = value;
        if ((nOption & PATTERNMATCHING_LANG_USE_PHONESETTING) == PATTERNMATCHING_LANG_USE_PHONESETTING )
            enablePatternLangUsePhone = value;
        if ((nOption & PATTERNMATCHING_LANG_USE_GEOLOCATION) == PATTERNMATCHING_LANG_USE_GEOLOCATION )
            enablePatternLangUseGeo = value;
        if ((nOption & PATTERNMATCHING_LANG_USE_ALL) == PATTERNMATCHING_LANG_USE_ALL )
            enablePatternLangAll = value;
    }

    public int pmFindPos() {
        return mCurrentPos;
    }

    public void pmSettimeout(int timeout) {
        nTimeout = timeout;
    }

    // post-processing functions ----> start
    private String pmReplaceAlphaphoneToNum(String text) {
        String strOut = text;

        try {
            text = text.replaceAll("\\-", "");
            if (text.length() == 11 || text.length() == 12) {
                text = text.replaceAll("[ABC]", "2");
                text = text.replaceAll("[DEF]", "3");
                text = text.replaceAll("[GHI]", "4");
                text = text.replaceAll("[JKL]", "5");
                text = text.replaceAll("[MNO]", "6");
                text = text.replaceAll("[PQRS]", "7");
                text = text.replaceAll("[TUV]", "8");
                text = text.replaceAll("[WXYZ]", "9");
                if (text.length() == 12)
                    strOut = text.substring(0, 11);
                else
                    strOut = text;
            }
        } catch (Exception e) {
            // fall through
        }
        return strOut;
    }

    private int pmRemovePatternPosition(String text, String removePattern, String postFindPattern) {
        int nOutResult = 0;
        if ( removePattern == null || removePattern.isEmpty() )
            return nOutResult;
        try {
            Matcher matcherPostRemove = Pattern.compile(removePattern).matcher(text);
            if (matcherPostRemove.find()) {
                int end = matcherPostRemove.end();
                if (text.length() < end + 1)
                    return end;
                String strPost = text.substring(end + 1);
                Matcher matcherPostCheck = Pattern.compile(postFindPattern).matcher(strPost);
                if (matcherPostCheck.find()) {
                    int start = matcherPostCheck.start();
                    nOutResult = end + start + 1;
                }
            }
        } catch (Exception e) {
        }
        return nOutResult;
    }

    private double dlatitude = 0.0;
    private double dlongitude = 0.0;
    private String lGeoString = "";

    private double[] pmCheckPhyAddrGetGeo(String text, boolean bRecalc) {
        double[] outDouble = {
                0, 0
        };

        if (!enablePhyAddressCheck)
            return outDouble;
        if (text == null || text.isEmpty() || lGeoString == null || lGeoString.isEmpty()) {
            return outDouble;
        }
        if (bRecalc || !text.equals(lGeoString)) {
            pmCheckPhyAddr(text);
        }
        outDouble[0] = dlatitude;
        outDouble[1] = dlongitude;
        return outDouble;
    }

    private boolean pmCheckPhyAddr(String text) {
        EmailLog.d(TAG, text);

        if (!enablePhyAddressCheck)
            return true;

        dlatitude = 0.0;
        dlongitude = 0.0;
        lGeoString = "";
        /*
         * to use google map ================================================
         * try { Geocoder gc = new Geocoder(mContext); List<Address> addresses =
         * null; addresses = gc.getFromLocationName(text, 2); if (addresses !=
         * null && !addresses.isEmpty() && addresses.size() == 1) { lGeoString =
         * text; Address localAddress = addresses.get(0); dlatitude =
         * localAddress.getLatitude(); dlongitude = localAddress.getLongitude();
         * } if (addresses == null || addresses.isEmpty()) return false; } catch
         * (Exception e) { return false; }
         */
        return true;
    }

    private String pmRemoveTag(String linkText){
        try {
            linkText = linkText.replaceAll("<[^>]+>"," ");
            linkText = linkText.replaceAll("[\\,\\x0d\\x0a]|(?:nbsp;)", " ");
            linkText = linkText.replaceAll("  ", " ");
        } catch (Exception e) {
            // fall through
        }
        return linkText;
    }

    // post-processing functions <--- -end

    // main function -- 
    public String pmDataMatching(String text, int nType, int nStartPosition)
            throws MessagingException {
        int nFindStart = nStartPosition;
        boolean bTimeOut = false;
        mStop = false;
        mChangeContents = false;

        // local-feature
        if (mContext == null) {
            throw new MessagingException("Uninitialized. call first init");
        }
        if (nStartPosition > text.length() || nStartPosition < 0) {
            throw new MessagingException("invalid agument");
        }
// DO NOT APPLY CODING RULE - START
        String strDateSeperate = "[\\/\\s\\-]";// |\\.
        String strStandardDate = "(?:(?:[0-3]?\\d)|(?:[1-2][\\d]{3}))" + strDateSeperate
                + "(?:[0-3]?\\d)" + strDateSeperate + "(?:(?:[1-2][\\d]{3})|(?:[0-3]?[\\d]))";

        String strUSKeyword = null;

        String strLocalDate = null;
        String strLocalTime = null;
        String strLocalKeyword = null;

        String strGeneralDate = null;
        String strGeneralDateExceptYear = null;
        String strGeneralTime = null;
        String strGeneralKeyword = null;

        long time = 0, time2 = 0;
        int nTextLen = text.length();
        Matcher memail = null, matchPostMail = null;
        Pattern patPostDate = null, patPostTime = null;
        String mPhoneOrWebOrEmail = null;
        String outText = null;

        StringBuffer sb = new StringBuffer();
        StringBuffer sbTemp = new StringBuffer();
        /*
         * String urlPattern = "((\""+ OWN_WEB_URL.pattern() + "[^\"]*\")" +
         * "([^>]*>[^<]*"+OWN_WEB_URL.pattern()+"[^<]*<)?)" + "|((\"mailto:"+
         * Patterns.EMAIL_ADDRESS.pattern() + "[^\"]*\")" +
         * "([^>]*>"+Patterns.EMAIL_ADDRESS.pattern()+"<)?)" + "|([(][^)]*"+
         * OWN_WEB_URL.pattern() + "[^)]*[)])"; //?
         */
        // Patterns.PHONE.pattern() -> (\+[0-9]+[\- \.]*)?(\([0-9]+\)[\-\.]*)?([0-9][0-9\- \.][0-9\- \.]+[0-9])
        // strPhonePattern =
        // "(?:(?:[\\+]|(?:\\&\\#43\\;))[0-9]{1,4}[\\-\\.\\s]*)?(?:[0-9\\-\\.\\s]{1,6}|(?:\\([0-9]{1,5}\\)[\\s]?))?(?:[0-9]{1,4}[0-9\\-\\.\\s][0-9]{1,4}[0-9\\-\\.\\s][0-9]{2,12})";
        // strPhonePattern =
        // "(?:(?:[\\+]|(?:\\&\\#43\\;))[0-9]{1,4}[\\-\\.\\s]*)?(?:[0-9\\s]{1,6}[\\-\\s\\.]?|(?:\\([0-9]{1,4}\\)[\\s]?))?(?:[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,12})";
        // poland office pattern | general pattern | Alphabetic mnemonic system
        String strPhoneUSTollFree = "\\b(?:1-(7|8)[\\d]{2}-[A-Z0-9\\-]{7,10})\\b";
        String strCCExtention =
                "\\s*[\\,;]?\\s*"                                           // [optional spaces][optional comma or semicolon][optional spaces] can precede the pattern.
                + "(?i:"                                                    // Capture case-insensitive extension keyword (like "PIN" or "ext.") defined as one of the following rules:
                + "[\\,;#*(]+"                                              //   - comma, semicolon, hash, opening parenthesis or combination thereof;
                + "|x|ext|extension"                                        //   - common abbreviations for "extension";
                + "|\\bp"                                                   //   - common abbreviations for "password" is placed at start of word;
                + "|pwd|password|code"                                      //   - common abbreviations for "password" or "code";
                + "|cr|conf|conference"                                     //   - common abbreviations for "conference";
                + "|(?:conf\\.?)?(?:conference)?\\s*(?:bridge|code|id)"     //   - sentences "conf"/"conf."/"conference" + "bridge"/"code"/"id" with arbitrary amount of spaces before the word "bridge";
                + "|(?:pass)\\s*(?:code)?|(?:pin)\\s*(?:code)?"             //   - sentences "pass"/"pin" + "code" with arbitrary amount of spaces before the optional word "code";
                + "|(?:participant)'?s?\\s*(?:code)?"                       //   - sentences "participant"/"participants"/"participant's" + "code" with arbitrary amount of spaces before the optional word "code";
                + "|(?:attendee)'?s?\\s*(?:code)?"                          //   - sentences "attendee"/"attendees"/"attendee's" + "code" with arbitrary amount of spaces before the optional word "code".
                + ")\\.?\\s*[#\\:\\-]*\\s*"                                 // [optional dot][optional spaces][optional hash, colon, dash or combination thereof][optional spaces];
                + "([0-9\\s]+)\\)*(\\#)?";
        // "&#43;" => "+"
        String strPhonePattern = "(?:"
                + "(?:(?:(?:[\\+]|(?:\\&\\#43\\;))[0-9]{1,4}[\\s](?:\\&nbsp;)*)[\\(]?[0-9][\\)][\\s](?:\\&nbsp;)*(?:[0-9]{1,2}[\\s](?:\\&nbsp;)*)(?:[0-9][0-9][\\s](?:\\&nbsp;)*){3,4})|"
                + "(?:(?:(?:[\\+]|(?:\\&\\#43\\;))?[0-9]{1,4}[\\-\\.\\s]*)?(?:[0-9][0-9\\s]{0,5}[\\-\\s\\.]?|(?:[\\(]?[0-9]{1,4}[\\)][\\s]?))?(?:[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,12}))|"
                + strPhoneUSTollFree + ")(?:" + strCCExtention + ")?"; // Conference Call

        //String urlPattern = "((<[.]*)?(\"(" + OWN_WEB_URL.pattern() + "|cid:"
        //        + Patterns.EMAIL_ADDRESS.pattern() + ")[^\"]*\"))" + "|((\"mailto:"
        //        + Patterns.EMAIL_ADDRESS.pattern() + "[^\"]*\")" + "([^>]*>"
        //        + Patterns.EMAIL_ADDRESS.pattern() + "<)?)" + "|([(][^)]*" + OWN_WEB_URL.pattern()
        //        + "[^)]*[)])";        
        String urlRefPattern = "(?:<[aA][\\s][^>]+>)";
        // old urlRefPattern = "(?:<[aA] [hH][rR][eE][fF]=[^>]+>.+</[aA]>)|(?:<[^>]+>)"        
        String urlPattern = urlRefPattern + "|(?:<[^>]+>)";

        // location
        // String strLocationPatern = "<[Aa]ddress>[^<]+</[Aa]ddress>";//addtag
        // strLocationPostalCode  = " EU | UK | Canada | usa postal code"
        String strLocationStreetNum = "\\b(?:(?:[\\w]?[\\d]{1,4}[\\-\\s](?:\\d[0-9a-zA-Z]{0,4})?)|(?:\\d[0-9a-zA-Z]{0,4}))\\b";
        String strSeparateCityStates = "(?:\\s|\\,|(?:\\&nbsp;)|(?:\\&middot;)){0,3}";
        String strLocationPostalCode = "\\b(?:(?:[\\d]{4})|(?:[a-zA-Z][a-zA-Z0-9]{1,3}[\\-\\s][0-9][a-zA-Z][a-zA-Z])|(?:[a-zA-Z][0-9][a-zA-Z][\\-\\s][0-9][a-zA-Z][0-9])|(?:[\\d]{5}(?:[\\-\\s][\\d]{4})?))";
        String strLocationCityStates = "(?:[a-zA-Z\u00a0-\uaf00\\'\\s]{2,20}" + strSeparateCityStates + "[a-zA-Z\u00a0-\uaf00\\']{2,10})";
        String strLocationCountry = "(?:[\\s\\,][\\s]?(?:[Uu]nited\\s)?[\u00a0-\uaf00\\'\\w\\.]{2,20}\\b)?";
        String strWorldCity = "(?:"
                + "(?:Ankara)|(?:Athens)|(?:Atlanta)|(?:Baghdad)|(?:Bandung)|(?:Bangalore)|(?:Bangkok)|(?:Barcelona)|(?:Beijing)|(?:Berlin)|(?:Bombay)|(?:Boston)|(?:Brasillia)|(?:Buenos\\sAires)|(?:Busan)|"
                + "(?:Cairo)|(?:Calcutta)|(?:Casablandca)|(?:Chicago)|(?:Chongqing)|(?:Dallas)|(?:Delhi)|(?:Detroit)|(?:Dhaka)|(?:Guangzhou)|(?:Hanoi)|(?:Hong\\sKong)|(?:Houston)|"
                + "(?:Istanbul)|(?:Karachi)|(?:Jakarta)|(?:Kobe)|(?:Lagos)|(?:Lahore)|(?:Lima)|(?:London)|(?:Los\\sAngeles)|"
                + "(?:Madrid)|(?:Melbourne)|(?:Metro\\sManila)|(?:Mexico\\sCity)|(?:Miami)|(?:Milan)|(?:Montreal)|(?:Moscow)|(?:Mumbai)|(?:New\\sYork)|(?:Osaka)|"
                + "(?:Paris)|(?:Philadelphia)|(?:Phoenix)|(?:Pusan)|(?:Rio\\sde\\sJaneiro)|(?:Santiago)|(?:Sao\\sPaulo)|(?:Seoul)|(?:Shanghai)|(?:Shenyang)|(?:Singapore)|(?:Sydney)|"
                + "(?:Tehran)|(?:Tianjin)|(?:Tokyo)|(?:Toronto)|(?:Washington(?:[\\,\\s]{1,2}[Dd][\\.]?[Cc][\\.]?))|(?:Wuhan)|(?:Xi[\\']?an)"
                + ")";
        String strLocationUS = strLocationStreetNum + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}[\\w\u00a0-\uaf00]"
                + "(?:[\u00a0-\uaf00\\'\\w\\s#@\\-\\,\\.]{4,40})"
                + strSeparateCityStates + "(?i:<[^>]+>(?:[\\s\\x0d\\x0a]|(?:\\&nbsp;)){0,2}){0,5}"
                + "(?:(?:" + strLocationCityStates + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationPostalCode +"\\b)|"
                + "(?:" + strLocationPostalCode + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationCityStates +"\\b)|"
                + "(?:" + strWorldCity + "))"
                + strLocationCountry;
        String strLocationUS2 = "(?:\\b(?:\\b[Pp](?:ost)?[\\s\\.]*[Oo](?:ffice)?[\\s\\.]*(?:[Bb][Oo][Xx]))\\b[\\s]*[0-9]{0,10})"
                + "(?:(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + "(?i:<[^>]+>(?:[\\s\\x0d\\x0a]|(?:\\&nbsp;)){0,2}){0,5}"
                + "(?:(?:" + strLocationCityStates + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationPostalCode +"\\b)|"
                + "(?:" + strLocationPostalCode + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationCityStates +"\\b)|"
                + "(?:" + strWorldCity + "))"
                + strLocationCountry
                + ")?";
        String strLocationGeo = "(?:[\\f\\n\\r\\t\\>][#\\w\\s\\(\\)\\-]{0,30}[\\,\\-]?[\\w\\s\\#\\(\\)\\-]{0,30})?@[\\-0-9]{1,3}[\\.][0-9]{1,7}[\\s\\,]{1,2}[\\-0-9]{1,4}[\\.][0-9]{1,7}";
        // String strLocationLocalE =
        // mContext.getString(R.string.pattern_location_localeng);
        // String strLocationLocalL =
        // mContext.getString(R.string.pattern_location_locallang)
        String strLocationLocalE = pmGetResource(PATTERN_LOCATION_LOCALENG);
        String strLocationLocalL = pmGetResource(PATTERN_LOCATION_LOCALLANG);
        String strLocationLocal = null;
        String strLocationPatern = null;// this is pattern-data
// DO NOT APPLY CODING RULE - END

        if (!PATTERN_ERR_STRING.equals(strLocationLocalE)) {
            strLocationLocal = strLocationLocalE;
        }
        if (!PATTERN_ERR_STRING.equals(strLocationLocalL)) {
            if (strLocationLocal == null)
                strLocationLocal = strLocationLocalL;
            else
                strLocationLocal = strLocationLocal.concat("|" + strLocationLocalL);
        }
        if (strLocationLocal == null)
            strLocationPatern = strLocationGeo + "|" + strLocationUS2 + "|" + strLocationUS;
        else
            strLocationPatern = strLocationGeo + "|" + strLocationUS2 + "|" + strLocationUS + "|"
                    + strLocationLocal;

        // date - time
        //String strStandardTime = mContext.getString(R.string.calendar_standardtime);
        String strStandardTime = "(?:[0-2]?[\\d][\\:][0-6]?[\\d](?:[\\s]?\\:[0-6]?\\d)?(?:[\\s]?(?:am|[aA][\\.]?[mM][\\.]?|pm|[pP][\\.]?[mM][\\.]?))?)|(?:[0-2]?[\\d][\\s]?(?:am|[aA][\\.]?[mM][\\.]?|pm|[pP][\\.]?[mM][\\.]?))";
        // resource -- us data
        //String strUSkeywordT1 = mContext.getString(R.string.calendar_keywordweek);
        String strUSkeywordT1 = Pattern_DateTime_KeywordWeek;
        //String strUSkeywordT2 = mContext.getString(R.string.calendar_keywordmoment);
        //String strUSkeywordT2 = "[Mm]orning|[Mm]orning|[Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight"; <- this is original code
        String strUSkeywordT2 = "[Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight";
        //String strUSkeywordT3 = mContext.getString(R.string.calendar_keywordspecial);
        //String strUSkeywordT3 = "[Tt]oday|[Tt]oday|[Tt]omorrow|[Tt]omorrow|[Tt]onight"; <- this is original code
        String strUSkeywordT3 = "[Tt]oday|[Tt]omorrow|[Tt]onight";

        //String strUSDateExceptYear = mContext.getString(R.string.calendar_standarddateexceptyear);
        final String strUSWeek = "(?i)(?:(?:jan(?:uary)?)|(?:feb(?:ruary)?)|(?:mar(?:ch)?)|(?:apr(?:il)?)|(?:may)|(?:jun[e]?)|(?:jul[y]?)|(?:aug(?:ust)?)|(?:sep(?:tember)?)|(?:oct(?:ober)?)|(?:nov(?:ember)?)|(?:dec(?:ember)?))";
        String strUSDateExceptYear = "(?:"
                + strUSWeek
                + "[\\.\\,]?[\\s][0-3]?[\\d](?:th|st|nd|rd|\\,)?)|(?:[0-3]?[\\d](?:th|st|nd|rd)?[\\s]"
                + strUSWeek + "[\\.\\,]?)";
        // String strUSDate =
        // mContext.getString(R.string.calendar_standarddate);
        String strUSDate = "(?:"
                + strUSWeek
                + "[\\.\\,]?[\\s][0-3]?[\\d](?:th|st|nd|rd)?[\\s\\,]{1,2}(?:[1-2]\\d\\d\\d))|(?:[0-3]?[\\d](?:th|st|nd|rd)?[\\s]"
                + strUSWeek + "[\\.\\,]?[\\s][1-2]\\d\\d\\d)";
        strUSKeyword = "(?:(?:" + strUSkeywordT1 + ")[\\s\\,]{1,2}(?:(?:" + strUSkeywordT2
                + ")[\\s\\,]{1,2})?" + strStandardTime + ")|(?:" + strUSkeywordT3
                + "(?:[\\s\\,]{1,2}" + strUSkeywordT2 + ")?)";
        // strUSKeyword =
        // "(([mM]on\\w+|[Tt]ue\\w+|[Ww]ed\\w+|[Tt]hu\\w+|[Ff]ri\\w+|[Ss]at\\w+|[ss]un\\w+)(\\.)?(\\s|\\,)(([Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight)(\\s|\\,))?"+strStandardTime+")|([Tt]onight|[Tt]oday|[Tt]omorrow)((\\s|\\,)([Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight)(\\s|\\,)?)?";

        // resource - localdata : add to all string file, usdata : add to
        // string.xml only		
        //String strLocalKeywordT1 = mContext.getString(R.string.calendar_localkeywordweek);
        //String strLocalKeywordT2 = mContext.getString(R.string.calendar_localkeywordspecial);
        //String strLocalKeywordT3 = mContext.getString(R.string.calendar_localkeywordmoment);
        //String strLocalDateRsc = mContext.getString(R.string.calendar_localdate);
        //String strLocalTimeRsc = mContext.getString(R.string.calendar_localtime);
        String strLocalKeywordT1 = pmGetResource(PATTERN_DATETIME_KEYWORDWEEK);
        String strLocalKeywordT2 = pmGetResource(PATTERN_DATETIME_KEYWORDSPECIAL);
        String strLocalKeywordT3 = pmGetResource(PATTERN_DATETIME_KEYWORDMOMENT);
        String strLocalDateRsc = pmGetResource(PATTERN_DATETIME_LOCALDATE);
        String strLocalTimeRsc = pmGetResource(PATTERN_DATETIME_LOCALTIME);

        String strLocalKeywordRsc = "(?:\\b(?:" + strLocalKeywordT1 + "|" + strLocalKeywordT2
                + ")[\\.]?[\\s\\,]{0,2}(?:(?:" + strLocalKeywordT3 + ")?[\\s\\,]{0,2})(?:"
                + strStandardTime + "|" + strLocalTimeRsc + "))|(?:\\b" + strLocalKeywordT3
                + ")[\\s]?(?:" + strStandardTime + "|" + strLocalTimeRsc + ")";

        String strPostPatternSeparLocal = pmGetResource(PATTERN_DATETIME_DATETIME_SEPARATOR);
        String strPostPatternSeparUS = "(?i:through)";
        String strPostPatternSepar = "";
        if (strPostPatternSeparLocal == "" || PATTERN_ERR_STRING.equals(strPostPatternSeparLocal)) {
            strPostPatternSepar = strPostPatternSeparUS;
        } else {
            strPostPatternSepar = strPostPatternSeparUS + "|(?:" + strPostPatternSeparLocal + ")";
        }
        String strPostPatternSeparate = "(?:(?:\\&ndash;)|[\\-\\~\u2010-\u2015\ufe58\u301c\u3030\u2053\u007e\uff5e]|"
                + strPostPatternSepar + ")";

        // local data
        strLocalDate = strLocalDateRsc;
        strLocalTime = strLocalTimeRsc;
        if (PATTERN_ERR_STRING.equals(strLocalKeywordT3))
            strLocalKeywordT3 = strUSkeywordT3;
        if (PATTERN_ERR_STRING.equals(strLocalKeywordT1) || PATTERN_ERR_STRING.equals(strLocalKeywordT2))
            strLocalKeyword = PATTERN_ERR_STRING;
        else
            strLocalKeyword = strLocalKeywordRsc;

        // locale check
        if (strLocalDate == "" || PATTERN_ERR_STRING.equals(strLocalDate)) {
            strGeneralDate = "(?:" + strStandardDate + ")|(?:(?:" + strUSkeywordT1
                    + ")[\\s\\,\\.]{0,2})?(?:" + strUSDate + ")";
        } else {
            strGeneralDate = "(?:" + strLocalDate + ")|(?:(?:" + strUSkeywordT1
                    + ")[\\s\\,\\.]{0,2})?(?:" + strUSDate + ")|(?:" + strStandardDate + ")";
        }
        // if (strLocalExceptYear=="" || "z/z".equals(strLocalExceptYear)) {
        strGeneralDateExceptYear = "(?:(?:" + strUSkeywordT1 + ")[\\s\\,\\.]{0,2})?(?:"
                + strUSDateExceptYear + ")(?:[\\.\\,\\s]|(?:\\&nbsp;)){1,2}"
                + strPostPatternSeparate;

        if (strLocalTime == "" || PATTERN_ERR_STRING.equals(strLocalTime)) {
            strGeneralTime = strStandardTime;
        } else {
            strGeneralTime = "(?:" + strStandardTime + ")|(?:" + strLocalTime + ")";
        }
        if (PATTERN_ERR_STRING.equals(strLocalKeyword)) {
            strGeneralKeyword = strUSKeyword;
        } else {
            strGeneralKeyword = strUSKeyword + "|" + strLocalKeyword;
        }

        if (nType == 1) // html type - url pattern
        {
            mPhoneOrWebOrEmail = "(?:" + urlPattern + ")";

            if (enablePatternCalendar)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralDateExceptYear
                        + ")|(?:" + strGeneralDate + ")|(?:" + strGeneralTime + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" + strGeneralDate +
            // "|" + strGeneralTime;

            if (enablePatternCalendarKeyword)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralKeyword + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" +
            // strGeneralKeyword;

            if (enablePatternLocation)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strLocationPatern + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" +
            // strLocationPatern;

            mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?i)(?:" + EMAIL_ADDRESS.pattern()
                    + ")|(?:" + OWN_WEB_URL.pattern() + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|(?i)" +
            // OWN_WEB_URL.pattern() + "|" + Patterns.EMAIL_ADDRESS.pattern();
            if (enablePatternPhone)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strPhonePattern + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" + strPhonePattern;
            /*
             * mPhoneOrWebOrEmail = "\\n|<br>|" + strLocationPatern + "|" +
             * urlPattern + "|" + strGeneralDate + "|" + strGeneralTime + "|" +
             * strGeneralKeyword + "|(?i)" + OWN_WEB_URL.pattern() + "|" +
             * Patterns.EMAIL_ADDRESS.pattern() + "|" + strPhonePattern;
             */
        } else {
            mPhoneOrWebOrEmail = "(?i)(?:" + OWN_WEB_URL.pattern() + ")";
            if (enablePatternLocation)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strLocationPatern + ")");
            if (enablePatternCalendar)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralDateExceptYear
                        + ")|(?:" + strGeneralDate + ")|(?:" + strGeneralTime + ")");
            if (enablePatternCalendarKeyword)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralKeyword + ")");
            mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:"
                    + EMAIL_ADDRESS.pattern() + ")");
            if (enablePatternPhone)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strPhonePattern + ")");
            mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?=[\\w]{512,})");
            /*
             * mPhoneOrWebOrEmail = "<br>|(?i)" + OWN_WEB_URL.pattern() + "|" +
             * strGeneralDate + "|" + strGeneralTime + "|" + strGeneralKeyword +
             * "|" + Patterns.EMAIL_ADDRESS.pattern() + "|" + strPhonePattern +
             * "|(?=[\\w]{512,}+)";
             */
        }
        time = System.currentTimeMillis();
        memail = Pattern.compile(mPhoneOrWebOrEmail).matcher(text);

        // for post processing value
        String strPostGeneralTime = "(?:(?:\\&nbsp;)|\\s){0,2}" + strPostPatternSeparate + "[\\s]?"
                + "(?:" + strLocalKeywordT3 + ")?" + "(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}(?:"
                + strGeneralTime + ")";
        String strPostGeneralDate = "(?:(?:\\&nbsp;)|\\s){0,2}" + strPostPatternSeparate + "[\\s]?"
                + strGeneralDate;

        patPostDate = Pattern
                .compile("(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}(?:" + strGeneralDate + ")");
        patPostTime = Pattern.compile("(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}" + "(?:"
                + strLocalKeywordT3 + ")?" + "(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}(?:"
                + strGeneralTime + ")");
        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody :" + mPhoneOrWebOrEmail);
        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody :" + text);

        if (nType == 1 && nFindStart < 1) {
            int nTempPosition = 0;
            nTempPosition = text.indexOf("<body");
            if (nTempPosition < 1)
                nTempPosition = text.indexOf("<Body");
            if (nTempPosition < 1)
                nTempPosition = text.indexOf("<BODY");
            if (nTempPosition > nFindStart)
                nFindStart = nTempPosition;
        }

        Pattern pUrlRefPattern = Pattern.compile(urlRefPattern);
        Pattern pUrlPattern = Pattern.compile(urlPattern);
        Pattern pWEBUrl = Pattern.compile("(?i)" + OWN_WEB_URL.pattern());
        Pattern pWEBUrl2 = Pattern.compile(OWN_WEB_URL.pattern());
        Pattern pStrGeneralDateExceptYear = Pattern.compile(strGeneralDateExceptYear);
        Pattern pStrGeneralDate = Pattern.compile(strGeneralDate);
        Pattern pStrGeneralTime = Pattern.compile(strGeneralTime);
        Pattern pStrGeneralKeyword = Pattern.compile(strGeneralKeyword);
        Pattern pStrDateMonth = Pattern.compile("\\b((?i)(?:(?:jan(?:uary)?)|(?:feb(?:ruary)?)|(?:mar(?:ch)?)|(?:apr(?:il)?)|(?:may)|(?:jun[e]?)|(?:jul[y]?)|(?:aug(?:ust)?)|(?:sep(?:tember)?)|(?:oct(?:ober)?)|(?:nov(?:ember)?)|(?:dec(?:ember)?)))\\b");
        Pattern pEMAILPattern = Pattern.compile(EMAIL_ADDRESS.pattern());
        Pattern pStrPhoneUSTollFree = Pattern.compile(strPhoneUSTollFree);
        Pattern pStrPhonePattern = Pattern.compile(strPhonePattern);
        Pattern pStrLocationGeo = Pattern.compile(strLocationGeo);
        Pattern pStrLocationLocalL = Pattern.compile(strLocationLocalL);
        Pattern pStrLocationPatern = Pattern.compile(strLocationPatern);

        Pattern pStrPostGeneralTime = Pattern.compile(strPostGeneralTime);
        Pattern pStrPostGeneralDate = Pattern.compile(strPostGeneralDate);
        Pattern pStrLocationUS2 = Pattern.compile(strLocationUS2);

        while (memail.find(nFindStart) && !mStop) {
            // original matched text
            String matchedText = memail.group();
            String matchedFirst = "";
            boolean bModifiedMatchedText = false;
            // it will be a target of href
            String linkText = matchedText;
            String protoType = null;
            String strPost;
            int stPos = 0, endPos;
            int nRemoveCheck = 0;
            boolean bErr = false;

            nFindStart = memail.end();

            time2 = System.currentTimeMillis();
            if ((time2 - time) > nTimeout) {
                EmailLog.logv(TAG, "reloadUiFromBody : timeout=" + matchedText);
                bTimeOut = true;// timeout
                memail.appendReplacement(sb, matchedText);
                mCurrentPos = sb.length() - matchedText.length();
                break;
            }
            // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody :" +
            // matchedText+",time="+(time2-time));
            // if web url
            if (pUrlRefPattern.matcher(matchedText).matches()) { // Pattern.matches(urlRefPattern, matchedText)) {
                // find "</a>"
                int nLocalFindA = 0;
                if ( matchedText.indexOf("<a") >= 0 ) { // find /a
                    nLocalFindA = text.indexOf("</a>", memail.end());
                }
                else if ( matchedText.indexOf("<A") >= 0 ) { // find /A
                    nLocalFindA = text.indexOf("</A>", memail.end());
                }
                if (nLocalFindA > nFindStart)
                    nFindStart = nLocalFindA + 4;
                continue;
            }

            if (pUrlPattern.matcher(matchedText).matches()) { // Pattern.matches(urlPattern, matchedText)) {
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody : url match");
                continue;
            } else if (pWEBUrl.matcher(matchedText).matches() // Pattern.matches("(?i)" + OWN_WEB_URL.pattern(), matchedText)
                    || pWEBUrl2.matcher(matchedText).matches()) { // Pattern.matches(OWN_WEB_URL.pattern(), matchedText)) {
                // int start = mPhoneOrWebOrEmail.start();
                // //if email, it would be linked by webview
                // if( start != 0 && text.charAt(start - 1) == '@')
                // continue;
                endPos = memail.end();
                try {
                    if (endPos + 1 < nTextLen) {
                        strPost = text.substring(endPos, endPos + 1);
                        if (strPost.matches("[\\.]")) {
                            continue;
                        }
                    }

                        int startPos = memail.start();
                        if (startPos - 5 > 0) {
                            strPost = text.substring(startPos - 5, startPos);
                            if(strPost.matches("(?i):?url\\('?")) {
                                continue;
                            }

                    }
                } catch (Exception e) {
                }
                Matcher proto = WEB_URL_PROTOCOL.matcher(matchedText);
                if (proto.find()) {
                    // it already has a protocol type
                    protoType = "";
                } else {
                    protoType = "http://";
                }
            } else if (enablePatternCalendar
                    && pStrGeneralDateExceptYear.matcher(matchedText).matches()) { // Pattern.matches(strGeneralDateExceptYear, matchedText)) {
                // sat, oct 15 - (period)
                stPos = memail.end();
                endPos = stPos + nDatePostLen;
                if (nTextLen < endPos)
                    endPos = nTextLen - 1;
                try { // [[ Post processing type 1 - find datetime (oct 17,
                    // 2012)
                    strPost = text.substring(stPos, endPos);
                    // strPost=strPost.replaceAll("&nbsp;", " ");//space
                    matchPostMail = patPostDate.matcher(strPost);
                    int nRefPos = 0;
                    if (strPost.contains("&n"))
                        nRefPos = 8;
                    if (matchPostMail.find() && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                        nRemoveCheck++;
                        matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                        linkText = matchedText;// maybe sat, oct 15 - oct 17,
                        // 2012
                        protoType = "calendar:T6:";
                        nFindStart = stPos + matchPostMail.end();
                    } else
                        continue;
                } catch (Exception e) {
                    continue;
                } // end post type 1]]
            } else if (enablePatternCalendar && pStrGeneralDate.matcher(matchedText).matches()) { // Pattern.matches(strGeneralDate, matchedText)) {
                // oct 17, 2012
                stPos = matchedText.length();
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody strGeneralDate:"
                // +matchedText+","+stPos);
                if (stPos < 5)
                    continue;
                try {
                    if (stPos < 8) {
                        strPost = matchedText.substring(0, 2);
                        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody GDate:["
                        // +strPost+"]");
                        if (strPost.matches("0(\\s|\\/|\\-)") || strPost.equals("00"))
                            continue;
                        strPost = matchedText.substring(stPos - 2, stPos);
                        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody GDate:["
                        // +strPost+"]");
                        if (strPost.matches("(\\s|\\/|\\-)0") || strPost.equals("00"))
                            continue;
                    }
                    if (stPos < 9) {
                        endPos = memail.start();
                        if (endPos - 1 > 1) {
                            strPost = text.substring(endPos - 1, endPos);
                            if (strPost.matches("\\w"))
                                continue;
                        }
                    }
                    endPos = memail.end();
                    if (endPos + 1 < nTextLen) {
                        strPost = text.substring(endPos - 1, endPos + 1);
                        if (strPost.matches("\\w\\d")) {
                            continue;
                        }
                    }
                    strPost = matchedText;
                    if (strPost.trim().matches(strStandardDate)) {
                        int index = 0, ct = 0;
                        for (index = 0; index < strPost.length(); index++)
                            if (strPost.charAt(index) <= '9' && strPost.charAt(index) >= '0') {
                                ct++;
                            }
                        if (ct < 6)
                            continue;
                    }
                    if (strPost.contains(" 0 ") || strPost.contains("/0/")
                            || strPost.contains("-0-"))
                        continue;
                    stPos = memail.end();
                    endPos = stPos + nTimePostLen;
                    try { // post processing type 2 - find time (10:00)
                        if (nTextLen < endPos)
                            endPos = nTextLen - 1;
                        strPost = text.substring(stPos, endPos);
                        // 2001/02/20 20: or 12/12/20- no space
                        if (strPost.charAt(0) == ':'
                                || (strPost.charAt(0) == '-' && matchedText.charAt(matchedText
                                .length() - 1) != ' '))
                            continue;
                        protoType = "calendar:T2:";
                        // EmailLog.logv(Email.LOG_TAG,
                        // "reloadUiFromBody GDate:["+strPost+"]");
                        matchPostMail = patPostTime.matcher(strPost);
                        int nRefPos = 0;
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            linkText = matchedText; // march 30 2012 12:00
                            protoType = "calendar:T7:";
                            // post processing --
                            stPos = stPos + matchPostMail.end();
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                            String strCheck = null;
                            if (endPos - stPos > 3)
                                strCheck = strPost.substring(0, 2);
                            if (strCheck != null && strCheck.contains("&") && endPos - stPos > 10)
                                strCheck = strPost.substring(0, 9);
                            if (strCheck != null
                                    && strCheck.matches(".*(" + strPostPatternSeparate + ").*")) {
                                Pattern patPost3 = null;
                                if (strCheck.matches(".*(" + strPostPatternSeparUS + ").*")) {
                                    if (endPos + 8 < nTextLen)
                                        strPost = text.substring(stPos, endPos + 8);
                                    else
                                        strPost = text.substring(stPos, nTextLen - 1);
                                }
                                patPost3 = pStrPostGeneralTime; // Pattern.compile(strPostGeneralTime);
                                matchPostMail = patPost3.matcher(strPost);
                                if (strPost.contains("&n")
                                        || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                                    nRefPos = 8;
                                else
                                    nRefPos = 0;
                                if (matchPostMail.find()
                                        && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                                    nRemoveCheck++;
                                    nFindStart = stPos + matchPostMail.end();
                                    // march 30 2012 12:00 - 15:00                                    
                                    if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                        linkText = matchedText
                                                + "~"
                                                + strPost.substring(matchPostMail.start(),
                                                matchPostMail.end());
                                    else
                                        linkText = matchedText
                                                + strPost.substring(0, matchPostMail.end());
                                    matchedText = matchedText
                                            + strPost.substring(0, matchPostMail.end());
                                    //linkText = matchedText;
                                    protoType = "calendar:T7:";
                                    stPos = stPos + matchPostMail.end();
                                    endPos = stPos + nDatePostLen;
                                    if (nTextLen < endPos)
                                        endPos = nTextLen - 1;
                                    strPost = text.substring(stPos, endPos);
                                } else { // it is very very complicated..
                                    String tempMatched = null;
                                    patPost3 = pStrPostGeneralDate; // Pattern.compile(strPostGeneralDate);
                                    matchPostMail = patPost3.matcher(strPost);
                                    if (matchPostMail.find()
                                            && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                                        // march 30 2012 12:00 - april 1 2012
                                        tempMatched = matchedText
                                                + strPost.substring(0, matchPostMail.end());
                                        // last...
                                        stPos = stPos + matchPostMail.end();
                                        endPos = stPos + nDatePostLen;
                                        if (nTextLen < endPos)
                                            endPos = nTextLen - 1;
                                        strPost = text.substring(stPos, endPos);
                                        matchPostMail = patPostTime.matcher(strPost);
                                        if (strPost.contains("&n")
                                                || strPost.matches(".*(" + strPostPatternSeparUS
                                                + ").*"))
                                            nRefPos = 8;
                                        else
                                            nRefPos = 0;
                                        if (matchPostMail.find()
                                                && matchPostMail.start() < nMatchingPostPos
                                                + nRefPos) {
                                            nRemoveCheck = nRemoveCheck + 2;
                                            nFindStart = stPos + matchPostMail.end();
                                            // march 30 2012 12:00 -
                                            // april 1 2012 15:00
                                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                                linkText = tempMatched
                                                        + "~"
                                                        + strPost.substring(matchPostMail.start(),
                                                        matchPostMail.end());
                                            else
                                                linkText = tempMatched
                                                        + strPost.substring(0, matchPostMail.end());
                                            matchedText = tempMatched
                                                    + strPost.substring(0, matchPostMail.end());
                                            // linkText = matchedText;
                                            protoType = "calendar:T3:";
                                        }
                                    }
                                }
                            }
                        } else { // post processing type 3 - find date&time
                            Pattern patPost3 = null;
                            patPost3 = pStrPostGeneralDate; // Pattern.compile(strPostGeneralDate);
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                            matchPostMail = patPost3.matcher(strPost);
                            if (strPost.contains("&n")
                                    || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                                nRefPos = 8;
                            else
                                nRefPos = 0;
                            if (matchPostMail.find()
                                    && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                                nRemoveCheck++;
                                nFindStart = stPos + matchPostMail.end();
                                // march 30 2012 - april 1 2012
                                if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                    linkText = matchedText
                                            + "~"
                                            + strPost.substring(matchPostMail.start(),
                                            matchPostMail.end());
                                else
                                    linkText = matchedText
                                            + strPost.substring(0, matchPostMail.end());
                                matchedText = matchedText
                                        + strPost.substring(0, matchPostMail.end());
                                //linkText = matchedText;
                                protoType = "calendar:T2:";
                            }
                        }
                    } catch (Exception e) {
                        // fallthrough
                    } // end post type 2]]
                } catch (Exception e) {
                    // fallthrough
                }
                // protoType = "calendar:";
            } else if (enablePatternCalendar && pStrGeneralTime.matcher(matchedText).matches()) { // Pattern.matches(strGeneralTime, matchedText)) {
                // EmailLog.logv(Email.LOG_TAG,
                // "reloadUiFromBody strGTime:"+matchedText);
                stPos = memail.start();
                endPos = memail.end();
                try {
                    if (stPos > 2) {
                        strPost = text.substring(stPos - 1, stPos);
                        if (strPost.matches("[\\w\\d\\-@#:]")) {
                            continue;
                        }
                    }
                    if (endPos + 2 < nTextLen) {
                        strPost = text.substring(endPos, endPos + 2);
                        if (strPost.matches("([\\.:]\\d)|(\\d.)")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                stPos = memail.end();
                endPos = stPos + nDatePostLen;// date 12 /time 10 - local? 30
                if (nTextLen < endPos)
                    endPos = nTextLen - 1;
                linkText = matchedText;
                protoType = "calendar:T1:";
                try {
                    strPost = text.substring(stPos, endPos);
                    // 1. 10:00 -~ 11:00 find -~ 11:00
                    String strCheck = null;
                    int nRefPos = 0;
                    if (endPos - stPos > 3)
                        strCheck = strPost.substring(0, 2);
                    if (strCheck != null && strCheck.contains("&") && endPos - stPos > 10)
                        strCheck = strPost.substring(0, 9);
                    if (strCheck != null && strCheck.matches(".*(" + strPostPatternSeparate + ").*")) {
                        Pattern patPost3 = null;
                        matchPostMail = null;
                        if (strCheck.matches(".*(" + strPostPatternSeparUS + ").*")) {
                            if (endPos + 8 < nTextLen)
                                strPost = text.substring(stPos, endPos + 8);
                            else
                                strPost = text.substring(stPos, nTextLen - 1);
                        }
                        patPost3 = pStrPostGeneralTime; // Pattern.compile(strPostGeneralTime);
                        matchPostMail = patPost3.matcher(strPost);
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // 12:00 - 15:00
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            //linkText = matchedText;
                            protoType = "calendar:T1:";
                            stPos = stPos + matchPostMail.end();
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                        }
                    }
                    // 2. original post code
                    // strPost=strPost.replaceAll("&nbsp;", " ");//space
                    matchPostMail = patPostDate.matcher(strPost);
                    if (strPost.contains("&n")
                            || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                        nRefPos = 8;
                    else
                        nRefPos = 0;
                    if (matchPostMail.find() && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                        nRemoveCheck++;
                        nFindStart = stPos + matchPostMail.end();
                        if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                            linkText = matchedText + "~"
                                    + strPost.substring(matchPostMail.start(), matchPostMail.end());
                        else
                            linkText = matchedText + " "
                                    + strPost.substring(matchPostMail.start(), matchPostMail.end());
                        matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                        //linkText = matchedText;// if ( nRemoveCheck == 2 ) type4
                        protoType = "calendar:T4:";
                    }
                } catch (Exception e) {
                }
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody strGeneralTime:"
                // + strPost + "," + matchedText);
                // protoType = "calendar:";
            } else if (enablePatternCalendarKeyword
                    && pStrGeneralKeyword.matcher(matchedText).matches()) { // Pattern.matches(strGeneralKeyword, matchedText)) {
                // EmailLog.logv(Email.LOG_TAG,
                // "reloadUiFromBody strGeneralKeyword:" +matchedText);
                stPos = memail.end();
                endPos = stPos + nTimePostLen;// date 12 /time 10
                if (nTextLen < endPos)
                    endPos = nTextLen - 1;
                protoType = "calendar:T5:";
                linkText = matchedText;
                try {
                    strPost = text.substring(stPos, endPos);
                    String strCheck = null;
                    int nRefPos = 0;
                    if (endPos - stPos > 3)
                        strCheck = strPost.substring(0, 2);
                    if (strCheck != null && strCheck.contains("&") && endPos - stPos > 10)
                        strCheck = strPost.substring(0, 9);
                    if (strCheck != null && !(strCheck.contains("?") && strCheck.indexOf("?") < 3)
                            && !(strCheck.matches(".*(" + strPostPatternSeparate + ").*"))) {
                        // strPost=strPost.replaceAll("&nbsp;", " ");//space
                        matchPostMail = patPostTime.matcher(strPost);
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // Today 16:10
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + " "
                                        + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            // linkText = matchedText;
                            stPos = stPos + matchPostMail.end();
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                        }
                        // one more post processing -
                        Pattern patPost3 = null;
                        matchPostMail = null;
                        patPost3 = pStrPostGeneralTime;// Pattern.compile(strPostGeneralTime);
                        matchPostMail = patPost3.matcher(strPost);
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        else
                            nRefPos = 0;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // Today 16:10 - 6:23
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + " "
                                        + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            // linkText = matchedText;
                            protoType = "calendar:T5:";
                        }
                    } else { // post processing type 3 - find date&time
                        Pattern patPost3 = null;
                        matchPostMail = null;
                        if (strCheck != null && strCheck.matches(".*(" + strPostPatternSeparUS + ").*")) {
                            if (endPos + 8 < nTextLen)
                                strPost = text.substring(stPos, endPos + 8);
                            else
                                strPost = text.substring(stPos, nTextLen - 1);
                        }
                        patPost3 = pStrPostGeneralTime; // Pattern.compile(strPostGeneralTime);
                        matchPostMail = patPost3.matcher(strPost);
                        if (matchPostMail.find() && matchPostMail.start() < 4) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // Monday 4:30 - 6:23
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + " "
                                        + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            // linkText = matchedText;
                            protoType = "calendar:T5:";
                        }
                    }
                } catch (Exception e) {
                }
                // protoType = "calendar:T5:";
            } else if (pEMAILPattern.matcher(matchedText).matches()) { // Pattern.matches(EMAIL_ADDRESS.pattern(), matchedText)) {
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody Email_Address:"
                // +matchedText);
                protoType = "mailto:";
            } else if (enablePatternPhone && pStrPhoneUSTollFree.matcher(matchedText).matches()) { // Pattern.matches(strPhoneUSTollFree, matchedText)) {
                linkText = pmReplaceAlphaphoneToNum(matchedText);
                try {
                    if (!linkText.matches("[\\d]{10,11}")) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                protoType = "tel:";
            } else if (enablePatternPhone && pStrPhonePattern.matcher(matchedText).matches()) { // Pattern.matches(strPhonePattern, matchedText)) {
                stPos = memail.start();
                endPos = memail.end();
                try {
                    if (matchedText.contains(".0.") || matchedText.charAt(0) == '-') {
                        continue;// not phone num
                    }
                    strPost = matchedText;
                    if (strPost.trim().matches(strStandardDate)) {
                        continue;// date? confused?
                    }
                    int index = 0, ct = 0;
                    for (index = 0; index < strPost.length(); index++)
                        if (strPost.charAt(index) <= '9' && strPost.charAt(index) >= '0') {
                            ct++;
                        }
                    if (ct < 7)
                        continue;
                    if (stPos > 2 && stPos + 1 <= endPos) {
                        strPost = text.substring(stPos - 1, stPos);
                        // EmailLog.d(Email.LOG_TAG,
                        // "patternMatchURLDatetime : strPost="+strPost+",match"+matchedText);
                        if (!text.substring(stPos, stPos + 1).matches("\\s")
                                && strPost.matches("[\\w\\d\\-@#]")) {
                            continue;
                        }
                    }
                    if (endPos + 1 < nTextLen) {
                        strPost = text.substring(endPos, endPos + 1);
                        // EmailLog.d(Email.LOG_TAG,"patternMatchURLDatetime : endPost="
                        // + strPost+",match"+matchedText);
                        if (strPost.matches("\\w|\\d")) {
                            continue;
                        }
                        if (!strPost.equalsIgnoreCase("<") && strPost.matches("[\\w\\d\\-:@]")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                protoType = "tel:";
            } else if (pStrLocationGeo.matcher(matchedText).matches()) { // Pattern.matches(strLocationGeo, matchedText)) { 
                try {
                    int nIndex = linkText.indexOf('@');
                    if (nIndex >= 0)
                        linkText = linkText.substring(nIndex+1);
                } catch (Exception e) {
                }
                if (enableStreetView) {
                    // google.streetview:cbll=lat,lng&cbp=1,yaw,,pitch,zoom&mz=mapZoom
                    protoType = "google.streetview:cbll=";
                } else {
                    // geo:latitude,longitude
                    protoType = "geo:";
                }
                // linkText = linkText + "?z=20"; // for using zoomrate 2-23
            } else if (pStrLocationLocalL.matcher(matchedText).matches()) { // Pattern.matches(strLocationLocalL, matchedText)) {
                if (matchedText.length() < 6)
                    continue;
                linkText = pmRemoveTag(linkText);
                // post-processing 1.1 - remove company name (for local lang)
                // post-processing 1,2 - add 'geo:0,0?q=business+near+city' type?
                // post-processing 2 - pre-query? (is it real address?)
                if (!pmCheckPhyAddr(linkText)) {
                    continue; // it is not real address
                }
                double[] dgeopoint = pmCheckPhyAddrGetGeo(linkText, false);
                if (dgeopoint != null && dgeopoint.length == 2 && dgeopoint[0] != 0.0
                        && dgeopoint[1] != 0.0) {
                    if (enableStreetView) {
                        protoType = "google.streetview:cbll=";
                        linkText = dgeopoint[0] + "," + dgeopoint[1];
                    } else {
                        protoType = "geo:";
                        linkText = dgeopoint[0] + "," + dgeopoint[1] + "?z=20";
                    }
                } else {
                    protoType = "geo:0,0?q=";// geo:0,0?q=my+street+address
                }
                //protoType = "geo:0,0?q="; // local address just fallthrough?
            } else if (pStrLocationPatern.matcher(matchedText).matches()) { // Pattern.matches(strLocationPatern, matchedText)) {
                if (matchedText.length() < 16)
                    continue;
                stPos = memail.start();
                endPos = memail.end();
                try {
                    if (stPos != 0 && matchedText.charAt(0) != ' ') {
                        strPost = text.substring(stPos - 1, stPos);
                        if (!strPost.equalsIgnoreCase(">") && strPost.matches("[\\w\\d]")) {
                            continue;
                        }
                    }
                    if (endPos < text.length() - 2 && matchedText.charAt(endPos - 1) != ' ') {
                        strPost = text.substring(endPos, endPos + 1);
                        if (!strPost.equalsIgnoreCase("<") && strPost.matches("[\\w\\d]")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                // if have some preposition, it is not address?
                try {
                    if (matchedText
                            .matches(".*\\b(on|in|of|at|to|for|before|after|till|with|within|from|over|under|upon|about|into|by|except|between|since)\\b.*"))
                        continue;
                } catch (Exception e) {
                }

                // if have some month, it is not address?
                try {
                    if (pStrDateMonth.matcher(matchedText).find()) {
                        nFindStart = stPos + 1;
                        continue;
                    }
                } catch (Exception e) {
                }
                // post-processing 1.1 - remove company name (only eng?)
                // http://en.wikipedia.org/wiki/Incorporation_(business)
                // http://en.wikipedia.org/wiki/Corporation
                String strAbbreviationCompany = "(?:(Inc)|(C[Oo][\\w]{0,2})|(L[Tt][Dd][\\w]?)|([\\w]{0,2}LP)|([\\w]{0,2}LC)|(Pte[\\w]?))[\\.]?";
                // and abb=P.C. + PC + RC + SE
                String strCompany = "(?:(?i)(Company)|(Corp\\w+)|(Partnership))[\\.\\,\\s]?";
                String removePattern = "\\b(?:(?:" + strAbbreviationCompany + ")|(?:" + strCompany + "))\\b";
                int nRemovePos = pmRemovePatternPosition(linkText, removePattern,
                        strLocationStreetNum);
                if (nRemovePos > 0) {
                    try {
                        if (nRemovePos > linkText.length() - 1)
                            continue;
                        matchedFirst = matchedText.substring(0, nRemovePos);
                        linkText = matchedText.substring(nRemovePos);
                        if ( Pattern.matches(strLocationPatern, linkText) ){
                            bModifiedMatchedText = true;
                            matchedText = linkText;
                        }
                        else {
                            if ( stPos + nRemovePos > 0 || stPos + nRemovePos < endPos)
                                nFindStart = stPos + nRemovePos;
                            continue;
                        }
                    } catch (Exception e) {
                        // fall through
                    }
                }
                // if pattern contains pobox?
                if (!matchedText.matches(strLocationUS2)) {
                    Matcher matcherPostRemove = pStrLocationUS2.matcher(matchedText); // Pattern.compile(strLocationUS2).matcher(matchedText);
                    if (matcherPostRemove.find()) {
                        int start = matcherPostRemove.start();
                        if (bModifiedMatchedText) {
                            matchedFirst = matchedFirst + matchedText.substring(0, start);
                        } else {
                            bModifiedMatchedText = true;
                            matchedFirst = matchedText.substring(0, start);
                        }
                        linkText = matchedText.substring(start);
                        matchedText = linkText;
                    }
                }
                linkText = pmRemoveTag(linkText);
                // post-processing 1,2 - add 'geo:0,0?q=business+near+city' type?
                // post-processing 2 - pre-query? (is it real address?)
                if (!pmCheckPhyAddr(linkText)) {
                    continue; // it is not real address
                }
                double[] dgeopoint = pmCheckPhyAddrGetGeo(linkText, false);
                if (dgeopoint != null && dgeopoint.length == 2 && dgeopoint[0] != 0.0
                        && dgeopoint[1] != 0.0) {
                    if (enableStreetView) {
                        protoType = "google.streetview:cbll=";
                        linkText = dgeopoint[0] + "," + dgeopoint[1];
                    } else {
                        protoType = "geo:";
                        linkText = dgeopoint[0] + "," + dgeopoint[1] + "?z=20";
                    }
                } else {
                    protoType = "geo:0,0?q=";// geo:0,0?q=my+street+address
                }
                // protoType = "geo:0,0?q=";// geo:0,0?q=my+street+address
            } else {
                // EmailLog.d(Email.LOG_TAG, "patternMatchURLDatetime : error??"
                // + matchedText);
                bErr = true;
            }
            // click test for removing previous date
            // if ( protoType != null && protoType.startsWith("calendar") ){
            // if ( !hitTestForCalendar(matchedText, protoType)) continue;
            // }

            if (protoType != null && protoType.length() == 0 && linkText != null
                    && linkText.length() > 0) {
                int index = linkText.indexOf(":");
                if (index != -1) {
                    linkText = linkText.substring(0, index).toLowerCase()
                            + linkText.substring(index);
                }
            }
            if (!bErr) {
                String href = "";
                if (protoType != null && linkText != null
                        && protoType.length() + linkText.length() < 4) {
                    href = matchedText;
                } else if (bModifiedMatchedText) {
                    href = String.format("%s<a href=\"%s\">%s</a>",
                            matchedFirst, protoType + linkText, matchedText);
                    matchedFirst = "";
                } else {
                    href = String.format("<a href=\"%s\">%s</a>", protoType
                            + linkText, matchedText);
                }
                bModifiedMatchedText = false;
                memail.appendReplacement(sb, href);
                mChangeContents = true;
            }
            if (nRemoveCheck > 0) {
                int tempend = 0;
                for (int i = 0; i < nRemoveCheck; i++) {
                    if (memail.find()) {
                        matchedText = memail.group();
                        memail.appendReplacement(sbTemp, "test");
                        tempend = memail.end();
                        mChangeContents = true;
                    } else if (tempend != nFindStart && nFindStart > 5) { // tel-date
                        try {
                            memail.reset();
                            if (memail.find(nFindStart - 5)) {
                                tempend = memail.end();
                                if (Math.abs(tempend - nFindStart) < 2) {
                                    memail.appendReplacement(sbTemp, "test");
                                    nFindStart = tempend;
                                    mChangeContents = true;
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            nRemoveCheck = 0;
            mCurrentPos = sb.length();
            /*
             * if ( bOut ) { EmailLog.e(Email.LOG_TAG,
             * "patternMatchURLDatetime : time out=["+(time2-time)+"]"); break;
             * }
             */
        }

        if(mStop) {
            mCurrentPos = 0;
            EmailLog.logv(TAG, "reloadUiFromBody : return, mStop = true");
            return null;
        }

        memail.appendTail(sb);
        sbTemp.delete(0, sbTemp.length());
        EmailLog.logv(TAG, "reloadUiFromBody : time1=[" + (System.currentTimeMillis() - time)
                + "]" + memail.groupCount());

//        sb.append("</body></html>");
        try {
            outText = sb.toString();
        } catch (OutOfMemoryError oe) {
            mCurrentPos = 0;
            return text;
        }

        sb.delete(0, sb.length());
        if (!bTimeOut || mCurrentPos <= nStartPosition) {
            mCurrentPos = 0;
        }
        return outText;
    }

    static public class PatternMatchingResult {
        public String mPatternData = null;
        public int mStartIndex = -1;
        public int mEndIndex = -1;
        public boolean mFindPattern = false;
    }

    /**
     * @param text : data
     * @param nSelectedPosition : selected postion
     * @return PatternMatchingResult : result data 
     * @throws MessagingException
     */
    public PatternMatchingResult pmDataMatching(String text, int nSelectedPosition)
            throws MessagingException {


        PatternMatchingResult resultVal = new PatternMatchingResult();
        resultVal.mPatternData = text;
        resultVal.mEndIndex = nSelectedPosition;
        resultVal.mFindPattern = false;

        /* Disable by duplication code and now don't use.

        int nFindStart = 0;
        int nType = 0; // 0 : Text, 1 : HTML
        boolean bTimeOut = false;
        mStop = false;
        mChangeContents = false;

        // local-feature
        if (mContext == null) {
            throw new MessagingException("Uninitialized. call first init");
        }

// DO NOT APPLY CODING RULE - START
        String strDateSeperate = "[\\/\\s\\-]";// |\\.
        String strStandardDate = "(?:(?:[0-3]?\\d)|(?:[1-2][\\d]{3}))" + strDateSeperate
                + "(?:[0-3]?\\d)" + strDateSeperate + "(?:(?:[1-2][\\d]{3})|(?:[0-3]?[\\d]))";

        String strUSKeyword = null;

        String strLocalDate = null;
        String strLocalTime = null;
        String strLocalKeyword = null;

        String strGeneralDate = null;
        String strGeneralDateExceptYear = null;
        String strGeneralTime = null;
        String strGeneralKeyword = null;

        long time = 0, time2 = 0;
        int nTextLen = text.length();
        Matcher memail = null, matchPostMail = null;
        Pattern patPostDate = null, patPostTime = null;
        String mPhoneOrWebOrEmail = null;
        String outText = null;

        StringBuffer sb = new StringBuffer();
        StringBuffer sbTemp = new StringBuffer();

//          String urlPattern = "((\""+ OWN_WEB_URL.pattern() + "[^\"]*\")" +
//          "([^>]*>[^<]*"+OWN_WEB_URL.pattern()+"[^<]*<)?)" + "|((\"mailto:"+
//          Patterns.EMAIL_ADDRESS.pattern() + "[^\"]*\")" +
//          "([^>]*>"+Patterns.EMAIL_ADDRESS.pattern()+"<)?)" + "|([(][^)]*"+
//          OWN_WEB_URL.pattern() + "[^)]*[)])"; //?

        // Patterns.PHONE.pattern() -> (\+[0-9]+[\- \.]*)?(\([0-9]+\)[\-\.]*)?([0-9][0-9\- \.][0-9\- \.]+[0-9])
        // strPhonePattern =
        // "(?:(?:[\\+]|(?:\\&\\#43\\;))[0-9]{1,4}[\\-\\.\\s]*)?(?:[0-9\\-\\.\\s]{1,6}|(?:\\([0-9]{1,5}\\)[\\s]?))?(?:[0-9]{1,4}[0-9\\-\\.\\s][0-9]{1,4}[0-9\\-\\.\\s][0-9]{2,12})";
        // strPhonePattern =
        // "(?:(?:[\\+]|(?:\\&\\#43\\;))[0-9]{1,4}[\\-\\.\\s]*)?(?:[0-9\\s]{1,6}[\\-\\s\\.]?|(?:\\([0-9]{1,4}\\)[\\s]?))?(?:[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,12})";
        // poland office pattern | general pattern | Alphabetic mnemonic system 
        String strPhoneUSTollFree = "\\b(?:1-8[\\d]{2}-[A-Z0-9\\-]{7,10})\\b";
        String strPhonePattern = "(?:"
                + "(?:(?:(?:[\\+]|(?:\\&\\#43\\;))[0-9]{1,4}[\\s](?:\\&nbsp;)*)[\\(]?[0-9][\\)][\\s](?:\\&nbsp;)*(?:[0-9]{1,2}[\\s](?:\\&nbsp;)*)(?:[0-9][0-9][\\s](?:\\&nbsp;)*){3,4})|"
                + "(?:(?:(?:[\\+]|(?:\\&\\#43\\;))?[0-9]{1,4}[\\-\\.\\s]*)?(?:[0-9][0-9\\s]{0,5}[\\-\\s\\.]?|(?:[\\(]?[0-9]{1,4}[\\)][\\s]?))?(?:[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,4}[\\-\\.\\s]?[0-9]{2,12}))|"
                + strPhoneUSTollFree
                + ")(?:(?:(?:\\&\\#44\\;)|[\\,0-9]){1,40}[0-9][\\#]?)?";

        //String urlPattern = "((<[.]*)?(\"(" + OWN_WEB_URL.pattern() + "|cid:"
        //        + Patterns.EMAIL_ADDRESS.pattern() + ")[^\"]*\"))" + "|((\"mailto:"
        //        + Patterns.EMAIL_ADDRESS.pattern() + "[^\"]*\")" + "([^>]*>"
        //        + Patterns.EMAIL_ADDRESS.pattern() + "<)?)" + "|([(][^)]*" + OWN_WEB_URL.pattern()
        //        + "[^)]*[)])";        
        String urlRefPattern = "(?:<[aA][\\s][^>]+>)";
        // old urlRefPattern = "(?:<[aA] [hH][rR][eE][fF]=[^>]+>.+</[aA]>)|(?:<[^>]+>)"        
        String urlPattern = urlRefPattern + "|(?:<[^>]+>)";

        // location
        // String strLocationPatern = "<[Aa]ddress>[^<]+</[Aa]ddress>";//addtag
        // strLocationPostalCode  = " EU | UK | Canada | usa postal code"
        String strLocationStreetNum = "\\b(?:(?:[\\w]?[\\d]{1,4}[\\-\\s](?:\\d[0-9a-zA-Z]{0,4})?)|(?:\\d[0-9a-zA-Z]{0,4}))\\b";
        String strSeparateCityStates = "(?:\\s|\\,|(?:\\&nbsp;)|(?:\\&middot;)){0,3}";
        String strLocationPostalCode = "\\b(?:(?:[\\d]{4})|(?:[a-zA-Z][a-zA-Z0-9]{1,3}[\\-\\s][0-9][a-zA-Z][a-zA-Z])|(?:[a-zA-Z][0-9][a-zA-Z][\\-\\s][0-9][a-zA-Z][0-9])|(?:[\\d]{5}(?:[\\-\\s][\\d]{4})?))";
        String strLocationCityStates = "(?:[a-zA-Z\u00a0-\uaf00\\'\\s]{2,20}" + strSeparateCityStates + "[a-zA-Z\u00a0-\uaf00\\']{2,10})";
        String strLocationCountry = "(?:[\\s\\,][\\s]?(?:[Uu]nited\\s)?[\u00a0-\uaf00\\'\\w\\.]{2,20}\\b)?";
        String strWorldCity = "(?:"
                + "(?:Ankara)|(?:Athens)|(?:Atlanta)|(?:Baghdad)|(?:Bandung)|(?:Bangalore)|(?:Bangkok)|(?:Barcelona)|(?:Beijing)|(?:Berlin)|(?:Bombay)|(?:Boston)|(?:Brasillia)|(?:Buenos\\sAires)|(?:Busan)|"
                + "(?:Cairo)|(?:Calcutta)|(?:Casablandca)|(?:Chicago)|(?:Chongqing)|(?:Dallas)|(?:Delhi)|(?:Detroit)|(?:Dhaka)|(?:Guangzhou)|(?:Hanoi)|(?:Hong\\sKong)|(?:Houston)|"
                + "(?:Istanbul)|(?:Karachi)|(?:Jakarta)|(?:Kobe)|(?:Lagos)|(?:Lahore)|(?:Lima)|(?:London)|(?:Los\\sAngeles)|"
                + "(?:Madrid)|(?:Melbourne)|(?:Metro\\sManila)|(?:Mexico\\sCity)|(?:Miami)|(?:Milan)|(?:Montreal)|(?:Moscow)|(?:Mumbai)|(?:New\\sYork)|(?:Osaka)|"
                + "(?:Paris)|(?:Philadelphia)|(?:Phoenix)|(?:Pusan)|(?:Rio\\sde\\sJaneiro)|(?:Santiago)|(?:Sao\\sPaulo)|(?:Seoul)|(?:Shanghai)|(?:Shenyang)|(?:Singapore)|(?:Sydney)|"
                + "(?:Tehran)|(?:Tianjin)|(?:Tokyo)|(?:Toronto)|(?:Washington(?:[\\,\\s]{1,2}[Dd][\\.]?[Cc][\\.]?))|(?:Wuhan)|(?:Xi[\\']?an)"
                + ")";
        String strLocationUS = strLocationStreetNum + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}[\\w\u00a0-\uaf00]"
                + "(?:[\u00a0-\uaf00\\'\\w\\s#@\\-\\,\\.]{4,40})"
                + strSeparateCityStates + "(?i:<[^>]+>(?:[\\s\\x0d\\x0a]|(?:\\&nbsp;)){0,2}){0,5}"
                + "(?:(?:" + strLocationCityStates + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationPostalCode +"\\b)|"
                + "(?:" + strLocationPostalCode + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationCityStates +"\\b)|"
                + "(?:" + strWorldCity + "))"
                + strLocationCountry;
        String strLocationUS2 = "(?:\\b(?:\\b[Pp](?:ost)?[\\s\\.]*[Oo](?:ffice)?[\\s\\.]*(?:[Bb][Oo][Xx]))\\b[\\s]*[0-9]{0,10})"
                + "(?:(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + "(?i:<[^>]+>(?:[\\s\\x0d\\x0a]|(?:\\&nbsp;)){0,2}){0,5}"
                + "(?:(?:" + strLocationCityStates + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationPostalCode +"\\b)|"
                + "(?:" + strLocationPostalCode + "(?:\\s|\\,|(?:\\&nbsp;)){1,3}" + strLocationCityStates +"\\b)|"
                + "(?:" + strWorldCity + "))"
                + strLocationCountry
                + ")?";
        String strLocationGeo = "(?:[\\f\\n\\r\\t\\>][#\\w\\s\\(\\)\\-]{0,30}[\\,\\-]?[\\w\\s\\#\\(\\)\\-]{0,30})?@[\\-0-9]{1,3}[\\.][0-9]{1,7}[\\s\\,]{1,2}[\\-0-9]{1,4}[\\.][0-9]{1,7}";
        // String strLocationLocalE =
        // mContext.getString(R.string.pattern_location_localeng);
        // String strLocationLocalL =
        // mContext.getString(R.string.pattern_location_locallang)
        String strLocationLocalE = pmGetResource(PATTERN_LOCATION_LOCALENG);
        String strLocationLocalL = pmGetResource(PATTERN_LOCATION_LOCALLANG);
        String strLocationLocal = null;
        String strLocationPatern = null;// this is pattern-data
// DO NOT APPLY CODING RULE - END

        if (!PATTERN_ERR_STRING.equals(strLocationLocalE)) {
            strLocationLocal = strLocationLocalE;
        }
        if (!PATTERN_ERR_STRING.equals(strLocationLocalL)) {
            if (strLocationLocal == null)
                strLocationLocal = strLocationLocalL;
            else
                strLocationLocal = strLocationLocal.concat("|" + strLocationLocalL);
        }
        if (strLocationLocal == null)
            strLocationPatern = strLocationGeo + "|" + strLocationUS2 + "|" + strLocationUS;
        else
            strLocationPatern = strLocationGeo + "|" + strLocationUS2 + "|" + strLocationUS + "|"
                    + strLocationLocal;

        // date - time
        //String strStandardTime = mContext.getString(R.string.calendar_standardtime);
        String strStandardTime = "(?:[0-2]?[\\d][\\:][0-6]?[\\d](?:[\\s]?\\:[0-6]?\\d)?(?:[\\s]?(?:am|[aA][\\.]?[mM][\\.]?|pm|[pP][\\.]?[mM][\\.]?))?)|(?:[0-2]?[\\d][\\s]?(?:am|[aA][\\.]?[mM][\\.]?|pm|[pP][\\.]?[mM][\\.]?))";
        // resource -- us data
        //String strUSkeywordT1 = mContext.getString(R.string.calendar_keywordweek);
        String strUSkeywordT1 = Pattern_DateTime_KeywordWeek;
        //String strUSkeywordT2 = mContext.getString(R.string.calendar_keywordmoment);
        //String strUSkeywordT2 = "[Mm]orning|[Mm]orning|[Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight"; <- this is original code
        String strUSkeywordT2 = "[Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight";
        //String strUSkeywordT3 = mContext.getString(R.string.calendar_keywordspecial);
        //String strUSkeywordT3 = "[Tt]oday|[Tt]oday|[Tt]omorrow|[Tt]omorrow|[Tt]onight"; <- this is original code
        String strUSkeywordT3 = "[Tt]oday|[Tt]omorrow|[Tt]onight";

        //String strUSDateExceptYear = mContext.getString(R.string.calendar_standarddateexceptyear);
        final String strUSWeek = "(?i)(?:(?:jan(?:uary)?)|(?:feb(?:ruary)?)|(?:mar(?:ch)?)|(?:apr(?:il)?)|(?:may)|(?:jun[e]?)|(?:jul[y]?)|(?:aug(?:ust)?)|(?:sep(?:tember)?)|(?:oct(?:ober)?)|(?:nov(?:ember)?)|(?:dec(?:ember)?))";
        String strUSDateExceptYear = "(?:"
                + strUSWeek
                + "[\\.\\,]?[\\s][0-3]?[\\d](?:th|st|nd|rd|\\,)?)|(?:[0-3]?[\\d](?:th|st|nd|rd)?[\\s]"
                + strUSWeek + "[\\.\\,]?)";
        // String strUSDate =
        // mContext.getString(R.string.calendar_standarddate);
        String strUSDate = "(?:"
                + strUSWeek
                + "[\\.\\,]?[\\s][0-3]?[\\d](?:th|st|nd|rd)?[\\s\\,]{1,2}(?:[1-2]\\d\\d\\d))|(?:[0-3]?[\\d](?:th|st|nd|rd)?[\\s]"
                + strUSWeek + "[\\.\\,]?[\\s][1-2]\\d\\d\\d)";
        strUSKeyword = "(?:(?:" + strUSkeywordT1 + ")[\\s\\,]{1,2}(?:(?:" + strUSkeywordT2
                + ")[\\s\\,]{1,2})?" + strStandardTime + ")|(?:" + strUSkeywordT3
                + "(?:[\\s\\,]{1,2}" + strUSkeywordT2 + ")?)";
        // strUSKeyword =
        // "(([mM]on\\w+|[Tt]ue\\w+|[Ww]ed\\w+|[Tt]hu\\w+|[Ff]ri\\w+|[Ss]at\\w+|[ss]un\\w+)(\\.)?(\\s|\\,)(([Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight)(\\s|\\,))?"+strStandardTime+")|([Tt]onight|[Tt]oday|[Tt]omorrow)((\\s|\\,)([Mm]orning|[Aa]fternoon|[Ee]vening|[Nn]ight)(\\s|\\,)?)?";

        // resource - localdata : add to all string file, usdata : add to
        // string.xml only      
        //String strLocalKeywordT1 = mContext.getString(R.string.calendar_localkeywordweek);
        //String strLocalKeywordT2 = mContext.getString(R.string.calendar_localkeywordspecial);
        //String strLocalKeywordT3 = mContext.getString(R.string.calendar_localkeywordmoment);
        //String strLocalDateRsc = mContext.getString(R.string.calendar_localdate);
        //String strLocalTimeRsc = mContext.getString(R.string.calendar_localtime);
        String strLocalKeywordT1 = pmGetResource(PATTERN_DATETIME_KEYWORDWEEK);
        String strLocalKeywordT2 = pmGetResource(PATTERN_DATETIME_KEYWORDSPECIAL);
        String strLocalKeywordT3 = pmGetResource(PATTERN_DATETIME_KEYWORDMOMENT);
        String strLocalDateRsc = pmGetResource(PATTERN_DATETIME_LOCALDATE);
        String strLocalTimeRsc = pmGetResource(PATTERN_DATETIME_LOCALTIME);

        String strLocalKeywordRsc = "(?:\\b(?:" + strLocalKeywordT1 + "|" + strLocalKeywordT2
                + ")[\\.]?[\\s\\,]{0,2}(?:(?:" + strLocalKeywordT3 + ")?[\\s\\,]{0,2})(?:"
                + strStandardTime + "|" + strLocalTimeRsc + "))|(?:\\b" + strLocalKeywordT3
                + ")[\\s]?(?:" + strStandardTime + "|" + strLocalTimeRsc + ")";

        String strPostPatternSeparLocal = pmGetResource(PATTERN_DATETIME_DATETIME_SEPARATOR);
        String strPostPatternSeparUS = "(?i:through)";
        String strPostPatternSepar = "";
        if (strPostPatternSeparLocal == "" || PATTERN_ERR_STRING.equals(strPostPatternSeparLocal)) {
            strPostPatternSepar = strPostPatternSeparUS;
        } else {
            strPostPatternSepar = strPostPatternSeparUS + "|(?:" + strPostPatternSeparLocal + ")";
        }
        String strPostPatternSeparate = "(?:(?:\\&ndash;)|[\\-\\~\u2010-\u2015\ufe58\u301c\u3030\u2053\u007e\uff5e]|"
                + strPostPatternSepar + ")";

        // local data
        strLocalDate = strLocalDateRsc;
        strLocalTime = strLocalTimeRsc;
        if (PATTERN_ERR_STRING.equals(strLocalKeywordT3))
            strLocalKeywordT3 = strUSkeywordT3;
        if (PATTERN_ERR_STRING.equals(strLocalKeywordT1) || PATTERN_ERR_STRING.equals(strLocalKeywordT2))
            strLocalKeyword = PATTERN_ERR_STRING;
        else
            strLocalKeyword = strLocalKeywordRsc;

        // locale check
        if (strLocalDate == "" || PATTERN_ERR_STRING.equals(strLocalDate)) {
            strGeneralDate = "(?:" + strStandardDate + ")|(?:(?:" + strUSkeywordT1
                    + ")[\\s\\,\\.]{0,2})?(?:" + strUSDate + ")";
        } else {
            strGeneralDate = "(?:" + strLocalDate + ")|(?:(?:" + strUSkeywordT1
                    + ")[\\s\\,\\.]{0,2})?(?:" + strUSDate + ")|(?:" + strStandardDate + ")";
        }
        // if (strLocalExceptYear=="" || "z/z".equals(strLocalExceptYear)) {
        strGeneralDateExceptYear = "(?:(?:" + strUSkeywordT1 + ")[\\s\\,\\.]{0,2})?(?:"
                + strUSDateExceptYear + ")(?:[\\.\\,\\s]|(?:\\&nbsp;)){1,2}"
                + strPostPatternSeparate;

        if (strLocalTime == "" || PATTERN_ERR_STRING.equals(strLocalTime)) {
            strGeneralTime = strStandardTime;
        } else {
            strGeneralTime = "(?:" + strStandardTime + ")|(?:" + strLocalTime + ")";
        }
        if (PATTERN_ERR_STRING.equals(strLocalKeyword)) {
            strGeneralKeyword = strUSKeyword;
        } else {
            strGeneralKeyword = strUSKeyword + "|" + strLocalKeyword;
        }

        if (nType == 1) // html type - url pattern
        {
            mPhoneOrWebOrEmail = "(?:" + urlPattern + ")";
            if (enablePatternLocation)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strLocationPatern + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" +
            // strLocationPatern;
            if (enablePatternCalendar)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralDateExceptYear
                        + ")|(?:" + strGeneralDate + ")|(?:" + strGeneralTime + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" + strGeneralDate +
            // "|" + strGeneralTime;
            if (enablePatternCalendarKeyword)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralKeyword + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" +
            // strGeneralKeyword;
            mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?i)(?:" + EMAIL_ADDRESS.pattern()
                    + ")|(?:" + OWN_WEB_URL.pattern() + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|(?i)" +
            // OWN_WEB_URL.pattern() + "|" + Patterns.EMAIL_ADDRESS.pattern();
            if (enablePatternPhone)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strPhonePattern + ")");
            // mPhoneOrWebOrEmail = mPhoneOrWebOrEmail + "|" + strPhonePattern;

//              mPhoneOrWebOrEmail = "\\n|<br>|" + strLocationPatern + "|" +
//              urlPattern + "|" + strGeneralDate + "|" + strGeneralTime + "|" +
//              strGeneralKeyword + "|(?i)" + OWN_WEB_URL.pattern() + "|" +
//              Patterns.EMAIL_ADDRESS.pattern() + "|" + strPhonePattern;

        } else {
            mPhoneOrWebOrEmail = "(?i)(?:" + OWN_WEB_URL.pattern() + ")";
            if (enablePatternLocation)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strLocationPatern + ")");
            if (enablePatternCalendar)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralDateExceptYear
                        + ")|(?:" + strGeneralDate + ")|(?:" + strGeneralTime + ")");
            if (enablePatternCalendarKeyword)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strGeneralKeyword + ")");
            mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:"
                    + EMAIL_ADDRESS.pattern() + ")");
            if (enablePatternPhone)
                mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?:" + strPhonePattern + ")");
            mPhoneOrWebOrEmail = mPhoneOrWebOrEmail.concat("|(?=[\\w]{512,})");

//             mPhoneOrWebOrEmail = "<br>|(?i)" + OWN_WEB_URL.pattern() + "|" +
//             strGeneralDate + "|" + strGeneralTime + "|" + strGeneralKeyword +
//             "|" + Patterns.EMAIL_ADDRESS.pattern() + "|" + strPhonePattern +
//             "|(?=[\\w]{512,}+)";

        }
        time = System.currentTimeMillis();
        memail = Pattern.compile(mPhoneOrWebOrEmail).matcher(text);

        // for post processing value
        String strPostGeneralTime = "(?:(?:\\&nbsp;)|\\s){0,2}" + strPostPatternSeparate + "[\\s]?"
                + "(?:" + strLocalKeywordT3 + ")?" + "(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}(?:"
                + strGeneralTime + ")";
        String strPostGeneralDate = "(?:(?:\\&nbsp;)|\\s){0,2}" + strPostPatternSeparate + "[\\s]?"
                + strGeneralDate;

        patPostDate = Pattern
                .compile("(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}(?:" + strGeneralDate + ")");
        patPostTime = Pattern.compile("(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}" + "(?:"
                + strLocalKeywordT3 + ")?" + "(?:[\\.\\,\\s]|(?:\\&nbsp;)){0,2}(?:"
                + strGeneralTime + ")");
        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody :" + mPhoneOrWebOrEmail);
        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody :" + text);

        if (nType == 1 && nFindStart < 1) {
            int nTempPosition = 0;
            nTempPosition = text.indexOf("<body");
            if (nTempPosition < 1)
                nTempPosition = text.indexOf("<Body");
            if (nTempPosition < 1)
                nTempPosition = text.indexOf("<BODY");
            if (nTempPosition > nFindStart)
                nFindStart = nTempPosition;
        }

        Pattern pUrlRefPattern = Pattern.compile(urlRefPattern);
        Pattern pUrlPattern = Pattern.compile(urlPattern);
        Pattern pWEBUrl = Pattern.compile("(?i)" + OWN_WEB_URL.pattern());
        Pattern pWEBUrl2 = Pattern.compile(OWN_WEB_URL.pattern());
        Pattern pStrGeneralDateExceptYear = Pattern.compile(strGeneralDateExceptYear);
        Pattern pStrGeneralDate = Pattern.compile(strGeneralDate);
        Pattern pStrGeneralTime = Pattern.compile(strGeneralTime);
        Pattern pStrGeneralKeyword = Pattern.compile(strGeneralKeyword);
        Pattern pEMAILPattern = Pattern.compile(EMAIL_ADDRESS.pattern());
        Pattern pStrPhoneUSTollFree = Pattern.compile(strPhoneUSTollFree);
        Pattern pStrPhonePattern = Pattern.compile(strPhonePattern);
        Pattern pStrLocationGeo = Pattern.compile(strLocationGeo);
        Pattern pStrLocationLocalL = Pattern.compile(strLocationLocalL);
        Pattern pStrLocationPatern = Pattern.compile(strLocationPatern);

        Pattern pStrPostGeneralTime = Pattern.compile(strPostGeneralTime);
        Pattern pStrPostGeneralDate = Pattern.compile(strPostGeneralDate);
        Pattern pStrLocationUS2 = Pattern.compile(strLocationUS2);

        while (memail.find(nFindStart) && !mStop) {
            // original matched text
            String matchedText = memail.group();
            String matchedFirst = "";
            boolean bModifiedMatchedText = false;
            // it will be a target of href
            String linkText = matchedText;
            String protoType = null;
            String strPost;
            int stPos = 0, endPos;
            int nRemoveCheck = 0;
            boolean bErr = false;

            nFindStart = memail.end();

            time2 = System.currentTimeMillis();
            if (false && (time2 - time) > nTimeout) {
                EmailLog.logv(TAG, "reloadUiFromBody : timeout=" + matchedText);
                bTimeOut = true;// timeout
                memail.appendReplacement(sb, matchedText);
                mCurrentPos = sb.length() - matchedText.length();
                break;
            }
            // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody :" +
            // matchedText+",time="+(time2-time));
            // if web url
            if (pUrlRefPattern.matcher(matchedText).matches()) { // Pattern.matches(urlRefPattern, matchedText)) {
                // find "</a>"
                int nLocalFindA = 0;
                if ( matchedText.indexOf("<a") >= 0 ) { // find /a
                    nLocalFindA = text.indexOf("</a>", memail.end());
                }
                else if ( matchedText.indexOf("<A") >= 0 ) { // find /A
                    nLocalFindA = text.indexOf("</A>", memail.end());
                }
                if (nLocalFindA > nFindStart)
                    nFindStart = nLocalFindA + 4;
                continue;
            }

            if (pUrlPattern.matcher(matchedText).matches()) { // Pattern.matches(urlPattern, matchedText)) {
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody : url match");
                continue;
            } else if (pWEBUrl.matcher(matchedText).matches() // Pattern.matches("(?i)" + OWN_WEB_URL.pattern(), matchedText)
                    || pWEBUrl2.matcher(matchedText).matches()) { // Pattern.matches(OWN_WEB_URL.pattern(), matchedText)) {
                // int start = mPhoneOrWebOrEmail.start();
                // //if email, it would be linked by webview
                // if( start != 0 && text.charAt(start - 1) == '@')
                // continue;
                endPos = memail.end();
                try {
                    if (endPos + 1 < nTextLen) {
                        strPost = text.substring(endPos, endPos + 1);
                        if (strPost.matches("[\\.]")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                Matcher proto = WEB_URL_PROTOCOL.matcher(matchedText);
                if (proto.find()) {
                    // it already has a protocol type
                    protoType = "";
                } else {
                    protoType = "http://";
                }
            } else if (enablePatternCalendar
                    && pStrGeneralDateExceptYear.matcher(matchedText).matches()) { // Pattern.matches(strGeneralDateExceptYear, matchedText)) {
                // sat, oct 15 - (period)
                stPos = memail.end();
                endPos = stPos + nDatePostLen;
                if (nTextLen < endPos)
                    endPos = nTextLen - 1;
                try { // [[ Post processing type 1 - find datetime (oct 17,
                    // 2012)
                    strPost = text.substring(stPos, endPos);
                    // strPost=strPost.replaceAll("&nbsp;", " ");//space
                    matchPostMail = patPostDate.matcher(strPost);
                    int nRefPos = 0;
                    if (strPost.contains("&n"))
                        nRefPos = 8;
                    if (matchPostMail.find() && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                        nRemoveCheck++;
                        matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                        linkText = matchedText;// maybe sat, oct 15 - oct 17,
                        // 2012
                        protoType = "calendar:T6:";
                        nFindStart = stPos + matchPostMail.end();
                    } else
                        continue;
                } catch (Exception e) {
                    continue;
                } // end post type 1]]
            } else if (enablePatternCalendar && pStrGeneralDate.matcher(matchedText).matches()) { // Pattern.matches(strGeneralDate, matchedText)) {
                // oct 17, 2012
                stPos = matchedText.length();
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody strGeneralDate:"
                // +matchedText+","+stPos);
                if (stPos < 5)
                    continue;
                try {
                    if (stPos < 8) {
                        strPost = matchedText.substring(0, 2);
                        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody GDate:["
                        // +strPost+"]");
                        if (strPost.matches("0(\\s|\\/|\\-)") || strPost.equals("00"))
                            continue;
                        strPost = matchedText.substring(stPos - 2, stPos);
                        // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody GDate:["
                        // +strPost+"]");
                        if (strPost.matches("(\\s|\\/|\\-)0") || strPost.equals("00"))
                            continue;
                    }
                    if (stPos < 9) {
                        endPos = memail.start();
                        if (endPos - 1 > 1) {
                            strPost = text.substring(endPos - 1, endPos);
                            if (strPost.matches("\\w"))
                                continue;
                        }
                    }
                    endPos = memail.end();
                    if (endPos + 1 < nTextLen) {
                        strPost = text.substring(endPos - 1, endPos + 1);
                        if (strPost.matches("\\w\\d")) {
                            continue;
                        }
                    }
                    strPost = matchedText;
                    if (strPost.trim().matches(strStandardDate)) {
                        int index = 0, ct = 0;
                        for (index = 0; index < strPost.length(); index++)
                            if (strPost.charAt(index) <= '9' && strPost.charAt(index) >= '0') {
                                ct++;
                            }
                        if (ct < 6)
                            continue;
                    }
                    if (strPost.contains(" 0 ") || strPost.contains("/0/")
                            || strPost.contains("-0-"))
                        continue;
                    stPos = memail.end();
                    endPos = stPos + nTimePostLen;
                    try { // post processing type 2 - find time (10:00)
                        if (nTextLen < endPos)
                            endPos = nTextLen;
                        strPost = text.substring(stPos, endPos);
                        // 2001/02/20 20: or 12/12/20- no space
                        if (strPost.charAt(0) == ':'
                                || (strPost.charAt(0) == '-' && matchedText.charAt(matchedText
                                .length() - 1) != ' '))
                            continue;
                        protoType = "calendar:T2:";
                        // EmailLog.logv(Email.LOG_TAG,
                        // "reloadUiFromBody GDate:["+strPost+"]");
                        matchPostMail = patPostTime.matcher(strPost);
                        int nRefPos = 0;
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            linkText = matchedText; // march 30 2012 12:00
                            protoType = "calendar:T7:";
                            // post processing --
                            stPos = stPos + matchPostMail.end();
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                            String strCheck = null;
                            if (endPos - stPos > 3)
                                strCheck = strPost.substring(0, 2);
                            if (strCheck != null && strCheck.contains("&") && endPos - stPos > 10)
                                strCheck = strPost.substring(0, 9);
                            if (strCheck != null
                                    && strCheck.matches(".*(" + strPostPatternSeparate + ").*")) {
                                Pattern patPost3 = null;
                                if (strCheck.matches(".*(" + strPostPatternSeparUS + ").*")) {
                                    if (endPos + 8 < nTextLen)
                                        strPost = text.substring(stPos, endPos + 8);
                                    else
                                        strPost = text.substring(stPos, nTextLen - 1);
                                }
                                patPost3 = pStrPostGeneralTime; // Pattern.compile(strPostGeneralTime);
                                matchPostMail = patPost3.matcher(strPost);
                                if (strPost.contains("&n")
                                        || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                                    nRefPos = 8;
                                else
                                    nRefPos = 0;
                                if (matchPostMail.find()
                                        && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                                    nRemoveCheck++;
                                    nFindStart = stPos + matchPostMail.end();
                                    // march 30 2012 12:00 - 15:00                                    
                                    if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                        linkText = matchedText
                                                + "~"
                                                + strPost.substring(matchPostMail.start(),
                                                matchPostMail.end());
                                    else
                                        linkText = matchedText
                                                + strPost.substring(0, matchPostMail.end());
                                    matchedText = matchedText
                                            + strPost.substring(0, matchPostMail.end());
                                    //linkText = matchedText;
                                    protoType = "calendar:T7:";
                                    stPos = stPos + matchPostMail.end();
                                    endPos = stPos + nDatePostLen;
                                    if (nTextLen < endPos)
                                        endPos = nTextLen - 1;
                                    strPost = text.substring(stPos, endPos);
                                } else { // it is very very complicated..
                                    String tempMatched = null;
                                    patPost3 = pStrPostGeneralDate; // Pattern.compile(strPostGeneralDate);
                                    matchPostMail = patPost3.matcher(strPost);
                                    if (matchPostMail.find()
                                            && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                                        // march 30 2012 12:00 - april 1 2012
                                        tempMatched = matchedText
                                                + strPost.substring(0, matchPostMail.end());
                                        // last...
                                        stPos = stPos + matchPostMail.end();
                                        endPos = stPos + nDatePostLen;
                                        if (nTextLen < endPos)
                                            endPos = nTextLen - 1;
                                        strPost = text.substring(stPos, endPos);
                                        matchPostMail = patPostTime.matcher(strPost);
                                        if (strPost.contains("&n")
                                                || strPost.matches(".*(" + strPostPatternSeparUS
                                                + ").*"))
                                            nRefPos = 8;
                                        else
                                            nRefPos = 0;
                                        if (matchPostMail.find()
                                                && matchPostMail.start() < nMatchingPostPos
                                                + nRefPos) {
                                            nRemoveCheck = nRemoveCheck + 2;
                                            nFindStart = stPos + matchPostMail.end();
                                            // march 30 2012 12:00 -
                                            // april 1 2012 15:00
                                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                                linkText = tempMatched
                                                        + "~"
                                                        + strPost.substring(matchPostMail.start(),
                                                        matchPostMail.end());
                                            else
                                                linkText = tempMatched
                                                        + strPost.substring(0, matchPostMail.end());
                                            matchedText = tempMatched
                                                    + strPost.substring(0, matchPostMail.end());
                                            // linkText = matchedText;
                                            protoType = "calendar:T3:";
                                        }
                                    }
                                }
                            }
                        } else { // post processing type 3 - find date&time
                            Pattern patPost3 = null;
                            patPost3 = pStrPostGeneralDate; // Pattern.compile(strPostGeneralDate);
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                            matchPostMail = patPost3.matcher(strPost);
                            if (strPost.contains("&n")
                                    || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                                nRefPos = 8;
                            else
                                nRefPos = 0;
                            if (matchPostMail.find()
                                    && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                                nRemoveCheck++;
                                nFindStart = stPos + matchPostMail.end();
                                // march 30 2012 - april 1 2012
                                if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                    linkText = matchedText
                                            + "~"
                                            + strPost.substring(matchPostMail.start(),
                                            matchPostMail.end());
                                else
                                    linkText = matchedText
                                            + strPost.substring(0, matchPostMail.end());
                                matchedText = matchedText
                                        + strPost.substring(0, matchPostMail.end());
                                //linkText = matchedText;
                                protoType = "calendar:T2:";
                            }
                        }
                    } catch (Exception e) {
                        // fallthrough
                    } // end post type 2]]
                } catch (Exception e) {
                    // fallthrough
                }
                // protoType = "calendar:";
            } else if (enablePatternCalendar && pStrGeneralTime.matcher(matchedText).matches()) { // Pattern.matches(strGeneralTime, matchedText)) {
                // EmailLog.logv(Email.LOG_TAG,
                // "reloadUiFromBody strGTime:"+matchedText);
                stPos = memail.start();
                endPos = memail.end();
                try {
                    if (stPos > 2) {
                        strPost = text.substring(stPos - 1, stPos);
                        if (strPost.matches("[\\w\\d\\-@#:]")) {
                            continue;
                        }
                    }
                    if (endPos + 2 < nTextLen) {
                        strPost = text.substring(endPos, endPos + 2);
                        if (strPost.matches("([\\.:]\\d)|(\\d.)")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                stPos = memail.end();
                endPos = stPos + nDatePostLen;// date 12 /time 10 - local? 30
                if (nTextLen < endPos)
                    endPos = nTextLen - 1;
                linkText = matchedText;
                protoType = "calendar:T1:";
                try {
                    strPost = text.substring(stPos, endPos);
                    // 1. 10:00 -~ 11:00 find -~ 11:00
                    String strCheck = null;
                    int nRefPos = 0;
                    if (endPos - stPos > 3)
                        strCheck = strPost.substring(0, 2);
                    if (strCheck != null && strCheck.contains("&") && endPos - stPos > 10)
                        strCheck = strPost.substring(0, 9);
                    if (strCheck != null && strCheck.matches(".*(" + strPostPatternSeparate + ").*")) {
                        Pattern patPost3 = null;
                        matchPostMail = null;
                        if (strCheck.matches(".*(" + strPostPatternSeparUS + ").*")) {
                            if (endPos + 8 < nTextLen)
                                strPost = text.substring(stPos, endPos + 8);
                            else
                                strPost = text.substring(stPos, nTextLen - 1);
                        }
                        patPost3 = pStrPostGeneralTime; // Pattern.compile(strPostGeneralTime);
                        matchPostMail = patPost3.matcher(strPost);
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // 12:00 - 15:00
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            //linkText = matchedText;
                            protoType = "calendar:T1:";
                            stPos = stPos + matchPostMail.end();
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                        }
                    }
                    // 2. original post code
                    // strPost=strPost.replaceAll("&nbsp;", " ");//space
                    matchPostMail = patPostDate.matcher(strPost);
                    if (strPost.contains("&n")
                            || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                        nRefPos = 8;
                    else
                        nRefPos = 0;
                    if (matchPostMail.find() && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                        nRemoveCheck++;
                        nFindStart = stPos + matchPostMail.end();
                        if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                            linkText = matchedText + "~"
                                    + strPost.substring(matchPostMail.start(), matchPostMail.end());
                        else
                            linkText = matchedText + " "
                                    + strPost.substring(matchPostMail.start(), matchPostMail.end());
                        matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                        //linkText = matchedText;// if ( nRemoveCheck == 2 ) type4
                        protoType = "calendar:T4:";
                    }
                } catch (Exception e) {
                }
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody strGeneralTime:"
                // + strPost + "," + matchedText);
                // protoType = "calendar:";
            } else if (enablePatternCalendarKeyword
                    && pStrGeneralKeyword.matcher(matchedText).matches()) { // Pattern.matches(strGeneralKeyword, matchedText)) {
                // EmailLog.logv(Email.LOG_TAG,
                // "reloadUiFromBody strGeneralKeyword:" +matchedText);
                stPos = memail.end();
                endPos = stPos + nTimePostLen;// date 12 /time 10
                if (nTextLen < endPos)
                    endPos = nTextLen - 1;
                protoType = "calendar:T5:";
                linkText = matchedText;
                try {
                    strPost = text.substring(stPos, endPos);
                    String strCheck = null;
                    int nRefPos = 0;
                    if (endPos - stPos > 3)
                        strCheck = strPost.substring(0, 2);
                    if (strCheck != null && strCheck.contains("&") && endPos - stPos > 10)
                        strCheck = strPost.substring(0, 9);
                    if (strCheck != null && !(strCheck.contains("?") && strCheck.indexOf("?") < 3)
                            && !(strCheck.matches(".*(" + strPostPatternSeparate + ").*"))) {
                        // strPost=strPost.replaceAll("&nbsp;", " ");//space
                        matchPostMail = patPostTime.matcher(strPost);
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // Today 16:10
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + " "
                                        + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            // linkText = matchedText;
                            stPos = stPos + matchPostMail.end();
                            endPos = stPos + nDatePostLen;
                            if (nTextLen < endPos)
                                endPos = nTextLen - 1;
                            strPost = text.substring(stPos, endPos);
                        }
                        // one more post processing -
                        Pattern patPost3 = null;
                        matchPostMail = null;
                        patPost3 = pStrPostGeneralTime;// Pattern.compile(strPostGeneralTime);
                        matchPostMail = patPost3.matcher(strPost);
                        if (strPost.contains("&n")
                                || strPost.matches(".*(" + strPostPatternSeparUS + ").*"))
                            nRefPos = 8;
                        else
                            nRefPos = 0;
                        if (matchPostMail.find()
                                && matchPostMail.start() < nMatchingPostPos + nRefPos) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // Today 16:10 - 6:23
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + " "
                                        + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            // linkText = matchedText;
                            protoType = "calendar:T5:";
                        }
                    } else { // post processing type 3 - find date&time
                        Pattern patPost3 = null;
                        matchPostMail = null;
                        if (strCheck != null && strCheck.matches(".*(" + strPostPatternSeparUS + ").*")) {
                            if (endPos + 8 < nTextLen)
                                strPost = text.substring(stPos, endPos + 8);
                            else
                                strPost = text.substring(stPos, nTextLen - 1);
                        }
                        patPost3 = pStrPostGeneralTime; // Pattern.compile(strPostGeneralTime);
                        matchPostMail = patPost3.matcher(strPost);
                        if (matchPostMail.find() && matchPostMail.start() < 4) {
                            nRemoveCheck++;
                            nFindStart = stPos + matchPostMail.end();
                            // Monday 4:30 - 6:23
                            if (strPost.matches(".*(" + strPostPatternSepar + ").*"))
                                linkText = matchedText
                                        + "~"
                                        + strPost.substring(matchPostMail.start(),
                                        matchPostMail.end());
                            else
                                linkText = matchedText + " "
                                        + strPost.substring(0, matchPostMail.end());
                            matchedText = matchedText + strPost.substring(0, matchPostMail.end());
                            // linkText = matchedText;
                            protoType = "calendar:T5:";
                        }
                    }
                } catch (Exception e) {
                }
                // protoType = "calendar:T5:";
            } else if (pEMAILPattern.matcher(matchedText).matches()) { // Pattern.matches(EMAIL_ADDRESS.pattern(), matchedText)) {
                // EmailLog.logv(Email.LOG_TAG, "reloadUiFromBody Email_Address:"
                // +matchedText);
                protoType = "mailto:";
            } else if (enablePatternPhone && pStrPhoneUSTollFree.matcher(matchedText).matches()) { // Pattern.matches(strPhoneUSTollFree, matchedText)) {
                linkText = pmReplaceAlphaphoneToNum(matchedText);
                try {
                    if (!linkText.matches("[\\d]{10,11}")) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                protoType = "tel:";
            } else if (enablePatternPhone && pStrPhonePattern.matcher(matchedText).matches()) { // Pattern.matches(strPhonePattern, matchedText)) {
                stPos = memail.start();
                endPos = memail.end();
                try {
                    if (matchedText.contains(".0.") || matchedText.charAt(0) == '-') {
                        continue;// not phone num
                    }
                    strPost = matchedText;
                    if (strPost.trim().matches(strStandardDate)) {
                        continue;// date? confused?
                    }
                    int index = 0, ct = 0;
                    for (index = 0; index < strPost.length(); index++)
                        if (strPost.charAt(index) <= '9' && strPost.charAt(index) >= '0') {
                            ct++;
                        }
                    if (ct < 7)
                        continue;
                    if (stPos > 2 && stPos + 1 <= endPos) {
                        strPost = text.substring(stPos - 1, stPos);
                        // EmailLog.d(Email.LOG_TAG,
                        // "patternMatchURLDatetime : strPost="+strPost+",match"+matchedText);
                        if (!text.substring(stPos, stPos + 1).matches("\\s")
                                && strPost.matches("[\\w\\d\\-@#]")) {
                            continue;
                        }
                    }
                    if (endPos + 1 < nTextLen) {
                        strPost = text.substring(endPos, endPos + 1);
                        // EmailLog.d(Email.LOG_TAG,"patternMatchURLDatetime : endPost="
                        // + strPost+",match"+matchedText);
                        if (strPost.matches("\\w|\\d")) {
                            continue;
                        }
                        if (!strPost.equalsIgnoreCase("<") && strPost.matches("[\\w\\d\\-:@]")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                protoType = "tel:";
            } else if (pStrLocationGeo.matcher(matchedText).matches()) { // Pattern.matches(strLocationGeo, matchedText)) { 
                try {
                    int nIndex = linkText.indexOf('@');
                    if (nIndex >= 0)
                        linkText = linkText.substring(nIndex+1);
                } catch (Exception e) {
                }
                if (enableStreetView) {
                    // google.streetview:cbll=lat,lng&cbp=1,yaw,,pitch,zoom&mz=mapZoom
                    protoType = "google.streetview:cbll=";
                } else {
                    // geo:latitude,longitude
                    protoType = "geo:";
                }
                // linkText = linkText + "?z=20"; // for using zoomrate 2-23
            } else if (pStrLocationLocalL.matcher(matchedText).matches()) { // Pattern.matches(strLocationLocalL, matchedText)) {
                if (matchedText.length() < 6)
                    continue;
                linkText = pmRemoveTag(linkText);
                // post-processing 1.1 - remove company name (for local lang)
                // post-processing 1,2 - add 'geo:0,0?q=business+near+city' type?
                // post-processing 2 - pre-query? (is it real address?)
                if (!pmCheckPhyAddr(linkText)) {
                    continue; // it is not real address
                }
                double[] dgeopoint = pmCheckPhyAddrGetGeo(linkText, false);
                if (dgeopoint != null && dgeopoint.length == 2 && dgeopoint[0] != 0.0
                        && dgeopoint[1] != 0.0) {
                    if (enableStreetView) {
                        protoType = "google.streetview:cbll=";
                        linkText = dgeopoint[0] + "," + dgeopoint[1];
                    } else {
                        protoType = "geo:";
                        linkText = dgeopoint[0] + "," + dgeopoint[1] + "?z=20";
                    }
                } else {
                    protoType = "geo:0,0?q=";// geo:0,0?q=my+street+address
                }
                //protoType = "geo:0,0?q="; // local address just fallthrough?
            } else if (pStrLocationPatern.matcher(matchedText).matches()) { // Pattern.matches(strLocationPatern, matchedText)) {
                if (matchedText.length() < 16)
                    continue;
                stPos = memail.start();
                endPos = memail.end();
                try {
                    if (stPos != 0 && matchedText.charAt(0) != ' ') {
                        strPost = text.substring(stPos - 1, stPos);
                        if (!strPost.equalsIgnoreCase(">") && strPost.matches("[\\w\\d]")) {
                            continue;
                        }
                    }
                    if (endPos < text.length() - 2 && matchedText.charAt(endPos - 1) != ' ') {
                        strPost = text.substring(endPos, endPos + 1);
                        if (!strPost.equalsIgnoreCase("<") && strPost.matches("[\\w\\d]")) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
                // if have some preposition, it is not address?
                try {
                    if (matchedText
                            .matches(".*\\b(on|in|of|at|to|for|before|after|till|with|within|from|over|under|upon|about|into|by|except|between|since)\\b.*"))
                        continue;
                } catch (Exception e) {
                }
                // post-processing 1.1 - remove company name (only eng?)
                // http://en.wikipedia.org/wiki/Incorporation_(business)
                // http://en.wikipedia.org/wiki/Corporation
                String strAbbreviationCompany = "(?:(Inc)|(C[Oo][\\w]{0,2})|(L[Tt][Dd][\\w]?)|([\\w]{0,2}LP)|([\\w]{0,2}LC)|(Pte[\\w]?))[\\.]?";
                // and abb=P.C. + PC + RC + SE
                String strCompany = "(?:(?i)(Company)|(Corp\\w+)|(Partnership))[\\.\\,\\s]?";
                String removePattern = "\\b(?:(?:" + strAbbreviationCompany + ")|(?:" + strCompany + "))\\b";
                int nRemovePos = pmRemovePatternPosition(linkText, removePattern,
                        strLocationStreetNum);
                if (nRemovePos > 0) {
                    try {
                        if (nRemovePos > linkText.length() - 1)
                            continue;
                        matchedFirst = matchedText.substring(0, nRemovePos);
                        linkText = matchedText.substring(nRemovePos);
                        if ( Pattern.matches(strLocationPatern, linkText) ){
                            bModifiedMatchedText = true;
                            matchedText = linkText;
                        }
                        else {
                            if ( stPos + nRemovePos > 0 || stPos + nRemovePos < endPos)
                                nFindStart = stPos + nRemovePos;
                            continue;
                        }
                    } catch (Exception e) {
                        // fall through
                    }
                }
                // if pattern contains pobox?
                if (!matchedText.matches(strLocationUS2)) {
                    Matcher matcherPostRemove = pStrLocationUS2.matcher(matchedText); // Pattern.compile(strLocationUS2).matcher(matchedText);
                    if (matcherPostRemove.find()) {
                        int start = matcherPostRemove.start();
                        if (bModifiedMatchedText) {
                            matchedFirst = matchedFirst + matchedText.substring(0, start);
                        } else {
                            bModifiedMatchedText = true;
                            matchedFirst = matchedText.substring(0, start);
                        }
                        linkText = matchedText.substring(start);
                        matchedText = linkText;
                    }
                }
                linkText = pmRemoveTag(linkText);
                // post-processing 1,2 - add 'geo:0,0?q=business+near+city' type?
                // post-processing 2 - pre-query? (is it real address?)
                if (!pmCheckPhyAddr(linkText)) {
                    continue; // it is not real address
                }
                double[] dgeopoint = pmCheckPhyAddrGetGeo(linkText, false);
                if (dgeopoint != null && dgeopoint.length == 2 && dgeopoint[0] != 0.0
                        && dgeopoint[1] != 0.0) {
                    if (enableStreetView) {
                        protoType = "google.streetview:cbll=";
                        linkText = dgeopoint[0] + "," + dgeopoint[1];
                    } else {
                        protoType = "geo:";
                        linkText = dgeopoint[0] + "," + dgeopoint[1] + "?z=20";
                    }
                } else {
                    protoType = "geo:0,0?q=";// geo:0,0?q=my+street+address
                }
                // protoType = "geo:0,0?q=";// geo:0,0?q=my+street+address
            } else {
                // EmailLog.d(Email.LOG_TAG, "patternMatchURLDatetime : error??"
                // + matchedText);
                bErr = true;
            }
            // click test for removing previous date
            // if ( protoType != null && protoType.startsWith("calendar") ){
            // if ( !hitTestForCalendar(matchedText, protoType)) continue;
            // }

            if (protoType != null && protoType.length() == 0 && linkText != null
                    && linkText.length() > 0) {
                int index = linkText.indexOf(":");
                if (index != -1) {
                    linkText = linkText.substring(0, index).toLowerCase()
                            + linkText.substring(index);
                }
            }
            if (!bErr) {
                String href = "";
                if (protoType != null && linkText != null
                        && protoType.length() + linkText.length() < 4) {
                    href = matchedText;
                } else if (bModifiedMatchedText) {
                    href = String.format("%s<a href=\"%s\">%s</a>",
                            matchedFirst, protoType + linkText, matchedText);
                    matchedFirst = "";
                } else {
                    href = String.format("<a href=\"%s\">%s</a>", protoType
                            + linkText, matchedText);
                    int startPosition = memail.start();
                    int endPosition = startPosition + matchedText.length();

                    if(startPosition < nSelectedPosition && nSelectedPosition < endPosition) {
                        resultVal.mPatternData = protoType
                                + linkText;
                        resultVal.mStartIndex = startPosition;
                        resultVal.mEndIndex = endPosition;
                    }
                }
                bModifiedMatchedText = false;
                //memail.appendReplacement(sb, href);
                mChangeContents = true;
            }
            if (nRemoveCheck > 0) {
                int tempend = 0;
                for (int i = 0; i < nRemoveCheck; i++) {
                    if (memail.find()) {
                        matchedText = memail.group();
                        memail.appendReplacement(sbTemp, "test");
                        tempend = memail.end();
                        mChangeContents = true;
                    } else if (tempend != nFindStart && nFindStart > 5) { // tel-date
                        try {
                            memail.reset();
                            if (memail.find(nFindStart - 5)) {
                                tempend = memail.end();
                                if (Math.abs(tempend - nFindStart) < 2) {
                                    memail.appendReplacement(sbTemp, "test");
                                    nFindStart = tempend;
                                    mChangeContents = true;
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            nRemoveCheck = 0;
            mCurrentPos = sb.length();


//             if ( bOut ) { EmailLog.e(Email.LOG_TAG,
//             "patternMatchURLDatetime : time out=["+(time2-time)+"]"); break;
//             }

        }

        if(mStop) {
            mCurrentPos = 0;
            EmailLog.logv(TAG, "reloadUiFromBody : return, mStop = true");
            return null;
        }

        memail.appendTail(sb);
        sbTemp.delete(0, sbTemp.length());
        EmailLog.logv(TAG, "reloadUiFromBody : time1=[" + (System.currentTimeMillis() - time)
                + "]" + memail.groupCount());

        resultVal.mFindPattern = mChangeContents;

        try {
            outText = sb.toString();
        } catch (OutOfMemoryError oe) {
            mCurrentPos = 0;
            return resultVal;
        }

        sb.delete(0, sb.length());
        if (!bTimeOut || mCurrentPos <= 0) {
            mCurrentPos = 0;
        }
        */

        return resultVal;
    }

    // datetime action functions ----> start
    // convert to common format if involve month character in schedule string
    private String convertDateReplace(String inputText, int nType, String inputSeper1,
                                      String inputSeper2) {
        String strFirst, strSec, strThr;
        String result = inputText;
        int i, nCnt;

        strFirst = "";
        strSec = "";
        strThr = "";
        for (i = 0, nCnt = 0; i < result.length(); i++) {
            if ('/' == result.charAt(i)) {
                nCnt++;
                continue;
            }
            if (nCnt == 0)
                strFirst += result.charAt(i);
            else if (nCnt == 1)
                strSec += result.charAt(i);
            else if (nCnt == 2)
                strThr += result.charAt(i);
            else
                break;
        }
        if (strThr.length() == 1)
            strThr = "0" + strThr;
        if (strSec.length() == 1)
            strSec = "0" + strSec;
        if (strFirst.length() == 1)
            strFirst = "0" + strFirst;

        if (nType == 1) // mm/dd/yyyy
        {
            i = (int) Long.parseLong(strFirst);
            EmailLog.logv(TAG, "CALENDAR1: matchedText = " + i);
            if (i < 1 || i > 12) {
                nCnt = (int) Long.parseLong(strSec);
                if (nCnt < 1 || nCnt > 12)
                    result = strSec + inputSeper1 + strFirst + inputSeper2 + strThr;
                else
                    result = strFirst + inputSeper1 + strSec + inputSeper2 + strThr;
            } else
                result = strSec + inputSeper1 + strFirst + inputSeper2 + strThr;
        } else if (nType == 2) // yyyy/mm/dd
        {
            result = strThr + inputSeper1 + strSec + inputSeper2 + strFirst;
        } else // dd//mm/yyyy
        {
            i = (int) Long.parseLong(strSec);
            if (i < 1 || i > 12) {
                nCnt = (int) Long.parseLong(strFirst);
                if (nCnt < 1 || nCnt > 12)
                    result = strFirst + inputSeper1 + strSec + inputSeper2 + strThr;
                else
                    result = strSec + inputSeper1 + strFirst + inputSeper2 + strThr;
            } else
                result = strFirst + inputSeper1 + strSec + inputSeper2 + strThr;
        }

        return result;// dd/mm/yyyy
    }

    private String[] convertKeywordToDateTime(String info, boolean mHasDateInfo) {
        String strUsWeekend[] = {
                "(su\\w+)", "(mo\\w+)", "(tu\\w+)", "(we\\w+)", "(th\\w+)", "(fr\\w+)", "(sa\\w+)"
        };
        String strUSMoment[] = {
                "(dawn)", "(mor\\w+)", "(mor\\w+)", "(\\w+noon)", "(eve\\w+)", "(nig\\w+)"
        };
        String strUSKeyword[] = {
                "(tod\\w+)", "(tod\\w+)", "(tom\\w+)", "(tom\\w+)", "(ton\\w+)"
        }; // all, all, all, all, special
        final int nLenKeyword = 5;

        boolean bFind = false, bFind2 = false;
        boolean bAm = true;

        // String strLocalKeywordT1 =
        // mContext.getString(R.string.calendar_localkeywordweek);
        // String strLocalKeywordT2 =
        // mContext.getString(R.string.calendar_localkeywordmoment);
        // String strLocalKeywordT3 =
        // mContext.getString(R.string.calendar_localkeywordspecial);
        String strLocalKeywordT1 = pmGetResource(PATTERN_DATETIME_KEYWORDWEEK);
        String strLocalKeywordT2 = pmGetResource(PATTERN_DATETIME_KEYWORDMOMENT);
        String strLocalKeywordT3 = pmGetResource(PATTERN_DATETIME_KEYWORDSPECIAL);

        String result = info;
        String outResult[] = {
                "", ""
        };
        //String strOutMon = null;
        String strPattern = null;
        String matchedText = null;
        //String strToday = null;
        String strAllDay = null;
        Matcher mMatKeyword;

        //long nMonth = 0, nDay = 0;
        int i, nCnt = 0, day_of_week = 1;
        int addDay = 0, nTomorrow = 0;
        java.util.Calendar cal = java.util.Calendar.getInstance();

        try {
            // if ( mMessage != null) {// datetime of email
            // Date date = new Date(mMessage.mTimeStamp);
            if (mTimeOfMessage != 0) {
                Date date = new Date(mTimeOfMessage);
                cal.setTimeInMillis(date.getTime());
                // EmailLog.d(TAG,
                // "convertKeywordToDateTime: convertKeywordToDate, orignal = "+cal);

            } else { // today
                cal.set(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
                        java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
                        java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), 0,
                        0, 0);
            }
        } catch (Exception e) {
            cal.set(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
                    java.util.Calendar.getInstance().get(java.util.Calendar.MONTH),
                    java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), 0, 0, 0);
        }
        // cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        day_of_week = cal.get(Calendar.DAY_OF_WEEK);

        // sun\\w+|\\. -> sun\\w+
        strLocalKeywordT1 = strLocalKeywordT1.replace("|\\.", "");

        String strLocalWeekend[] = {
                "", ""
        };
        String strLocalMoment[] = {
                "", ""
        };
        String strLocalkeyword[] = {
                "", ""
        };
        try {
            if (PATTERN_ERR_STRING.equals(strLocalKeywordT3)) {
                strLocalkeyword = strUSKeyword;
            } else {
                strLocalKeywordT3 = strLocalKeywordT3.replace("|", " ");
                strLocalkeyword = strLocalKeywordT3.split(" ");
            }
            if (PATTERN_ERR_STRING.equals(strLocalKeywordT2)) {
                strLocalMoment = strUSMoment;
            } else {
                strLocalKeywordT2 = strLocalKeywordT2.replace("|", " ");
                strLocalMoment = strLocalKeywordT2.split(" ");
            }
            if (PATTERN_ERR_STRING.equals(strLocalKeywordT1)) {
                strLocalWeekend = strUsWeekend;
            } else {
                strLocalKeywordT1 = strLocalKeywordT1.replace("|", " ");
                strLocalWeekend = strLocalKeywordT1.split(" ");
            }
        } catch (Exception e) {
        }
        // EmailLog.d(TAG,
        // "ADD2CALENDAR: convertKeywordToDate, orignal = "+cal);
        // EmailLog.d(TAG,
        // "ADD2CALENDAR: convertKeywordToDate, orignal = "+info);

        result = result.replace(".", " ");
        result = result.replace(",", " ");
        result = result.replace("  ", " ");
        strAllDay = "false";
        try {
            for (i = 0; i < strLocalkeyword.length; i++) {
                strPattern = "(?i)" + strUSKeyword[i % nLenKeyword] + "|" + strLocalkeyword[i];
                // EmailLog.d(TAG,
                // "convertKeywordToDateTime: keyword strPattern = " +
                // strPattern);
                mMatKeyword = Pattern.compile(strPattern).matcher(result);
                if (mMatKeyword.find()) {
                    matchedText = mMatKeyword.group();
                    if ((i % nLenKeyword) > 1) { // 2 or 3 means tomorrow
                        nTomorrow = 1;
                    }
                    result = result.replace(matchedText, "");
                    if ((i % nLenKeyword) == nLenKeyword - 1) {
                        strAllDay = "special";
                        nTomorrow = 0;
                    } else
                        strAllDay = "true";
                    bFind = true;
                    // EmailLog.d(TAG,
                    // "convertKeywordToDateTime: convertKeywordToDate keyword = "
                    // + matchedText+ ",i=" + i);
                    break;
                }
            }
            try {
                for (i = strLocalWeekend.length - 1; i > -1; i--) {
                    strPattern = "(?i)" + strUsWeekend[i % 7] + "|" + strLocalWeekend[i];
                    mMatKeyword = Pattern.compile(strPattern).matcher(result);
                    // EmailLog.d(TAG, "convertKeywordToDateTime: firstweek=" +
                    // i + "," + strPattern);
                    if (mMatKeyword.find()) {
                        matchedText = mMatKeyword.group();
                        if (day_of_week < (i % 7) + 1)
                            addDay = (i % 7) + 1 - day_of_week;
                        else if (day_of_week > (i % 7) + 1)
                            addDay = 7 + (i % 7) + 1 - day_of_week;
                        else
                            addDay = 0;
                        /*
                         * Matcher mMatKeyword2 =
                         * Pattern.compile("\\d+[^\\d]*").matcher(result); if
                         * (mMatKeyword2.find()) { end = mMatKeyword2.start(); }
                         * try { result = result.substring(0, start) +
                         * result.substring(end); } catch (Exception e) { result
                         * = result.replace(matchedText, ""); }
                         */
                        result = result.replace(matchedText, "").trim();
                        bFind2 = true;
                        bFind = true;
                        break;
                    }
                }
            } catch (Exception e) {
                EmailLog.d(TAG, "convertKeywordToDateTime: parse error - index ?");
            }
            // if ( bFind2 )
            {
                for (i = 0; i < strLocalMoment.length; i++) {
                    strPattern = "(?i)" + strUSMoment[i % 6] + "|" + strLocalMoment[i];
                    // EmailLog.d(TAG,
                    // "convertKeywordToDateTime: moment strPattern = " +
                    // strPattern);
                    mMatKeyword = Pattern.compile(strPattern).matcher(result);
                    if (mMatKeyword.find()) {
                        nCnt = mMatKeyword.end();
                        matchedText = mMatKeyword.group();
                        // EmailLog.d(TAG,
                        // "convertKeywordToDateTime: convertKeywordToDate nCnt = "
                        // + nCnt);
                        if ((i % 6) < 3)
                            bAm = true;
                        else
                            bAm = false;
                        if (bAm)
                            result = result.replace(matchedText, "am"); // for 12:00
//                        else if (bFind2)
//                            result = result.replace(matchedText, "pm");
                        else
                            result = result.replace(matchedText, "pm");
                        // EmailLog.d(TAG,
                        // "convertKeywordToDateTime: convertKeywordToDate keyword = "+
                        // mMatKeyword.group() + ",rst=" + result + "," + bAm);
                        bFind = true;
                        strAllDay = "false";
                        break;
                    }
                }
            }
            // insert to today date
            if (!mHasDateInfo) {
                result = (cal.get(Calendar.DAY_OF_MONTH) + addDay + nTomorrow) + "/"
                        + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR) + " "
                        + result;
            }
            result = result.replace("  ", " ");
        } catch (Exception e) {
            EmailLog.d(TAG, "convertKeywordToDateTime: parse error");
        }// parse exception
        if (bFind || mHasDateInfo == false) {
            outResult[0] = result;
            outResult[1] = strAllDay;
        } else {
            outResult[0] = info;
            outResult[1] = "false";
        }
        EmailLog.d(TAG, "convertKeywordToDateTime: convertKeywordToDate, out(DMY) = " + info
                + ",rst=" + result + ",allday" + strAllDay);
        return outResult;
    }

    private String[] convertLocalDateToTime(String info) {
        // String result = info;
        String outResult[] = {
                "", ""
        };

        // String strReplaceSetIn =
        // mContext.getString(R.string.calendar_localreplaceinput);
        // String strReplaceSetOut =
        // mContext.getString(R.string.calendar_localreplaceoutput);
        // String strLocalKeywordMoment =
        // mContext.getString(R.string.calendar_localkeywordmoment);
        // String strReplaceConfIn =
        // mContext.getString(R.string.calendar_localreplaceconflictinput);
        // String strReplaceConfOut =
        // mContext.getString(R.string.calendar_localreplaceconflictoutput);
        String strReplaceSetIn = pmGetResource(PATTERN_DATETIME_REPLACEINPUT);
        String strReplaceSetOut = pmGetResource(PATTERN_DATETIME_REPLACEOUTPUT);
        String strLocalKeywordMoment = pmGetResource(PATTERN_DATETIME_KEYWORDMOMENT);
        String strReplaceConfIn = pmGetResource(PATTERN_DATETIME_REPLACECONFLICTINPUT);
        String strReplaceConfOut = pmGetResource(PATTERN_DATETIME_REPLACECONFLICTOUTPUT);

        int lenIn, lenOut, i;

        strReplaceSetIn = strReplaceSetIn.replace("|", " ");
        strReplaceSetOut = strReplaceSetOut.replace("|", " ");
        strReplaceConfIn = strReplaceConfIn.replace("|", " ");
        strReplaceConfOut = strReplaceConfOut.replace("|", " ");

        // allways replace
        if (!strReplaceSetIn.equals(" ")) {
            String strReplaceSplitIn[] = strReplaceSetIn.split(" ");
            String strReplaceSplitOut[] = strReplaceSetOut.split(" ");

            lenIn = strReplaceSplitIn.length;
            lenOut = strReplaceSplitOut.length;
            if (lenIn != lenOut) {
                EmailLog.e(TAG, "convertLocalDateToTime: something wrong inlen=" + lenIn
                        + ",lenout=" + lenOut);
                if (lenIn > lenOut)
                    lenIn = lenOut;
            }
            for (i = 0; i < lenIn; i++) {
                // EmailLog.d(TAG, "convertLocalDateToTime: in= " +
                // strReplaceSplitIn[i] + ",out="+ strReplaceSplitOut[i]);
                info = info.replaceAll(strReplaceSplitIn[i], strReplaceSplitOut[i]);
            }
        }
        // conditional replace
        if (!strReplaceConfIn.equals(" ")) {
            String strReplaceSplitConfIn[] = strReplaceConfIn.split(" ");
            String strReplaceSplitConfOut[] = strReplaceConfOut.split(" ");
            lenIn = strReplaceSplitConfIn.length;
            lenOut = strReplaceSplitConfOut.length;
            if (lenIn != lenOut) {
                EmailLog.e(TAG, "convertLocalDateToTime: something wrong inlen=" + lenIn
                        + ",lenout=" + lenOut);
                if (lenIn > lenOut)
                    lenIn = lenOut;
            }
            if (lenIn > 1) {
                int nLen = (int) (lenIn / 4);
                boolean bReplace = false;
                try {
                    for (i = 0; i < nLen; i++) {
                        if (info.contains(strReplaceSplitConfIn[(i * 4) % lenIn]))
                            bReplace = true;
                    }
                    if (bReplace) {
                        for (i = 0; i < lenIn; i++) {
                            info = info
                                    .replace(strReplaceSplitConfIn[i], strReplaceSplitConfOut[i]);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        // - local moment delete -

        try {
            String strLocalMoment[] = {
                    "", ""
            };
            if (!PATTERN_ERR_STRING.equals(strLocalKeywordMoment)) {
                strLocalKeywordMoment = strLocalKeywordMoment.replace("|", " ");
                strLocalMoment = strLocalKeywordMoment.split(" ");
                if (strLocalMoment.length >= 6) {
                    int nLocalCnt = (int) (strLocalMoment.length / 6);
                    for (int j = 0; j < nLocalCnt; j++) {
                        for (i = 0; i < 3; i++) {
                            info = info.replace(strLocalMoment[j * 6 + i], "am");// for 12:00 
                        }
                        for (i = 3; i < 6; i++) {
                            info = info.replace(strLocalMoment[j * 6 + i], "pm");
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        info = info.replace("^", " ");
        info = info.replace("/ ", "/");
        info = info.replace("//", "/");
        info = info.replace(" :", ":");
        info = info.replace(": ", ":");
        info = info.replace("  ", " ");

        EmailLog.d(TAG, "convertLocalDateToTime: rst=" + info);

        outResult[0] = info;
        outResult[1] = "false";
        return outResult;
    }

    private String convertStandardRemoveDayofWeek(String info) {
        // String strUSkeywordT1 =
        // mContext.getString(R.string.calendar_keywordweek);
        // String strLocalKeywordT1 =
        // mContext.getString(R.string.calendar_localkeywordweek);
        String strUSkeywordT1 = Pattern_DateTime_KeywordWeek;
        String strLocalKeywordT1 = pmGetResource(PATTERN_DATETIME_KEYWORDWEEK);

        String matchedText = null;
        String result = info;
        Matcher mMatKeyword;
        String strPattern = null;

        if (PATTERN_ERR_STRING.equals(strLocalKeywordT1))
            strPattern = "(?i)" + strUSkeywordT1;
        else
            strPattern = "(?i)" + strUSkeywordT1 + "|" + strLocalKeywordT1;

        try {
            mMatKeyword = Pattern.compile(strPattern).matcher(info);
            while (mMatKeyword.find()) {
                int start = mMatKeyword.start();
                int end = mMatKeyword.end();
                String isSpace = "00";
                try {
                    isSpace = info.substring(start - 1, start);
                } catch (Exception e) {
                    try {
                        isSpace = info.substring(end, end + 1);
                    } catch (Exception e2) {
                        isSpace = " ";
                    }
                    if (",".equals(isSpace)) {
                        isSpace = " ";
                        info = info.replace("\\,", " ");
                    }
                }
                matchedText = mMatKeyword.group();
                String matchedLay[] = matchedText.split("\\d");
                if (matchedLay.length <= 1 && " ".equals(isSpace)) {
                    try {
                        result=info.substring(0, start) + info.substring(end);
                    }catch(Exception e){
                        result = info.replace(matchedText, "").trim();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            EmailLog.e(TAG, "convertStandardRemoveDayofWeek: error? = " + result);
        }
        EmailLog.d(TAG, "convertStandardRemoveDayofWeek: strPattern = " + result);
        return result;
    }

    private String[] convertStandardDateToTime(String info) {
        // return string -> DMY sytle 31/01/2001
        String strDateSeperate = "(\\.|\\/|\\s|\\-)";
        String strStandardDateMDY = "([0-3]?\\d)" + strDateSeperate + "([0-3]?\\d)"
                + strDateSeperate + "([1-2]\\d\\d\\d)";
        String strStandardDateMDY2 = "([0-3]?\\d)" + strDateSeperate + "([0-3]?\\d)"
                + strDateSeperate + "([0-3]?\\d)";
        String strStandardDateYMD = "(([1-2]\\d\\d\\d))" + strDateSeperate + "([0-3]?\\d)"
                + strDateSeperate + "([0-3]?\\d)";

        String strMonPattern[] = {
                "(jan(\\w+)?)", "(feb(\\w+)?)", "(mar(\\w+)?)", "(apr(\\w+)?)", "(may(\\w+)?)",
                "(jun(\\w+)?)", "(jul(\\w+)?)", "(aug(\\w+)?)", "(sep(\\w+)?)", "(oct(\\w+)?)",
                "(nov(\\w+)?)", "(dec(\\w+)?)"
        };
        String strUSMonPattern = "(?i)((jan(\\w+)?)|(feb(\\w+)?)|(mar(\\w+)?)|(apr(\\w+)?)|(may(\\w+)?)|(jun(\\w+)?)|(jul(\\w+)?)|(aug(\\w+)?)|(sep(\\w+)?)|(oct(\\w+)?)|(nov(\\w+)?)|(dec(\\w+)?))";
        String strUSDateMDY = "(" + strUSMonPattern + ")(\\.)?[\\s\\,]{1,2}([0-3]?\\d)(th|st|nd|rd|,)?[\\s\\,]{1,2}([1-2]\\d\\d\\d)";
        String strUSDateDMY = "([0-3]?\\d)(th|st|nd|rd|,)?(\\s)(" + strUSMonPattern + ")(\\.)?[\\s\\,]{1,2}([1-2]\\d\\d\\d)";

        String strOutMon = null;
        String strPattern = null;
        String result = "";
        String outResult[] = {
                "", ""
        };
        Matcher mPhoneOrWeb;

        outResult[1] = "false";
        // standard style
        strPattern = strStandardDateMDY + "|" + strStandardDateMDY2 + "|" + strStandardDateYMD
                + "|" + strUSDateMDY + "|" + strUSDateDMY;

        mPhoneOrWeb = Pattern.compile(strPattern).matcher(info);

        EmailLog.d(TAG, "convertStandardDateToTime: orignal = " + info);
        try {
            int nIndex = 0, id = 0;
            while (mPhoneOrWeb.find(nIndex)) {

                String matchedText = mPhoneOrWeb.group();
                try {
                    char separator[] = { '.', '/', ' ', '-' };// strDateSeperate-values
                    for (id = 0; id < 4; id++) {
                        if (matchedText
                                .split(Character.toString(separator[id])).length == 3)
                            break;
                    }
                } catch (Exception p) {
                }
                if (id == 4 && mPhoneOrWeb.start() != 0) {
                    nIndex += mPhoneOrWeb.start() + 2;
                    continue;
                }
                result = matchedText;
                outResult[1] = "true";
                // EmailLog.d(TAG, "convertStandardDateToTime: matchedText = " +
                // matchedText);
                if (Pattern.matches(strStandardDateMDY, matchedText)) // 10/29/2001
                // t1? t0?
                {
                    result = result.replace(".", "/");
                    result = result.replace(" ", "/");
                    result = result.replace("-", "/");
                    result = result.replace("//", "/");
                    try {
                        Date myDate = new Date("12/25/2000");
                        String myString = android.text.format.DateFormat.getDateFormat(mContext)
                                .format(myDate);

                        if (myString.substring(0, 2).equals("12"))
                            result = convertDateReplace(result, 1, "/", "/");
                        else
                            // default dd/mm/yy
                            result = convertDateReplace(result, 0, "/", "/");
                    } catch (Exception e) { // default dd/mm/yy
                        result = convertDateReplace(result, 0, "/", "/");
                    }
                    // EmailLog.d(TAG, "convertStandardDateToTime: t1(DMY) = " +
                    // result);
                } else if (Pattern.matches(strStandardDateMDY2, matchedText)) // 10/10/01
                // t0
                {
                    result = result.replace(".", "/");
                    result = result.replace(" ", "/");
                    result = result.replace("-", "/");
                    result = result.replace("//", "/");
                    String strAddyear = "/20";
                    try {
                        Date myDate = new Date("12/25/2000");
                        String myString = android.text.format.DateFormat.getDateFormat(mContext)
                                .format(myDate);

                        if (mTimeOfMessage != 0) {
                            try {
                                java.util.Calendar cal = java.util.Calendar.getInstance();
                                cal.setTimeInMillis(mTimeOfMessage);
                                strAddyear = String.format("/%d", cal.get(Calendar.YEAR));
                                strAddyear = strAddyear.substring(0, 3);
                            } catch (Exception e) {
                            }
                        }

                        if (myString.substring(0, 2).equals("12"))
                            result = convertDateReplace(result, 1, "/", strAddyear);
                        else
                            // default dd/mm/yy
                            result = convertDateReplace(result, 0, "/", strAddyear);
                    } catch (Exception e) { // default dd/mm/yy
                        result = convertDateReplace(result, 0, "/", strAddyear);
                    }
                    // EmailLog.d(TAG, "convertStandardDateToTime: t2(DMY) = " +
                    // result);
                } else if (Pattern.matches(strStandardDateYMD, matchedText)) // 2001/01/31
                // t2
                {
                    // SimpleDateFormat sDateFormat = new
                    // SimpleDateFormat("yyyy/mm/dd");
                    result = result.replace(".", "/");
                    result = result.replace(" ", "/");
                    result = result.replace("-", "/");
                    result = result.replace("//", "/");
                    result = convertDateReplace(result, 2, "/", "/");
                    // EmailLog.d(TAG, "convertStandardDateToTime: t3(DMY) = " +
                    // nYear);
                } else if (Pattern.matches(strUSDateMDY, matchedText)) // jan
                // 12nd
                // 2001
                // t1
                {
                    Matcher mMatMonth;
                    result = result.replace(".", "/");
                    result = result.replace(" ", "/");
                    result = result.replace("-", "/");
                    result = result.replace("//", "/");
                    result = result.replace("st", "");
                    result = result.replace("nd", "");
                    result = result.replace("rd", "");
                    result = result.replace("th", "");
                    result = result.replace(",", "");
                    // EmailLog.d(TAG, "THISTHISHTIS: dd(DMY) result = " +
                    // result);

                    for (int i = 0; i < 12; i++) {
                        mMatMonth = Pattern.compile(strMonPattern[i]).matcher(result);
                        if (mMatMonth.find()) {
                            strOutMon = String.format("%02d", i + 1);
                            result = result.replace(mMatMonth.group(), strOutMon);
                            // EmailLog.d(TAG,
                            // "convertStandardDateToTime: jan 1 2001 = "+strOutMon+","+result);
                            break;
                        }
                    }
                    result = convertDateReplace(result, 1, "/", "/");
                    // EmailLog.d(TAG, "convertStandardDateToTime: t4(DMY) = " +
                    // strOutMon);
                } else if (Pattern.matches(strUSDateDMY, matchedText)) // 12nd
                // jan
                // 2001
                {
                    Matcher mMatMonth;
                    result = result.replace(".", "/");
                    result = result.replace(" ", "/");
                    result = result.replace("-", "/");
                    result = result.replace("//", "/");
                    result = result.replace("st", "");
                    result = result.replace("nd", "");
                    result = result.replace("rd", "");
                    result = result.replace("th", "");
                    result = result.replace(",", "");

                    for (int i = 0; i < 12; i++) {
                        mMatMonth = Pattern.compile(strMonPattern[i]).matcher(result);
                        if (mMatMonth.find()) {
                            strOutMon = String.format("%02d", i + 1);
                            result = result.replace(mMatMonth.group(), strOutMon);
                            // EmailLog.d(TAG,
                            // "convertStandardDateToTime: jan 1 2001 = "+strOutMon+","+result);
                            break;
                        }
                    }
                    result = convertDateReplace(result, 0, "/", "/");
                    // EmailLog.d(TAG, "convertStandardDateToTime: t5(DMY) = " +
                    // strOutMon);
                } else {
                    EmailLog.e(TAG, "convertStandardDateToTime:error? = " + result);
                }
                info = info.replace(matchedText, result);
                nIndex += mPhoneOrWeb.end();
            }
        } catch (Exception e) {
            EmailLog.e(TAG, "convertStandardDateToTime: parse error");
        }// parse exception
        EmailLog.d(TAG, "convertStandardDateToTime: out(DMY) = " + info);

        outResult[0] = info;
        return outResult;
    }

    private String[] calendarGetStandardDate(String tempinfo) // return
    // dd/mm/yyyy
    {
        String outResult[] = {
                "", ""
        };
        boolean mHasDateInfo = false;

        tempinfo = convertStandardRemoveDayofWeek(tempinfo);
        outResult = convertLocalDateToTime(tempinfo); // result dd/mm/yyyy
        tempinfo = outResult[0];
        if (outResult[1].equals("true"))
            mHasDateInfo = true;
        outResult = convertStandardDateToTime(tempinfo); // result dd/mm/yyyy
        tempinfo = outResult[0];
        if (outResult[1].equals("true"))
            mHasDateInfo = true;
        tempinfo = tempinfo.replaceAll(",", " ");
        outResult[0] = tempinfo.trim();
        if (mHasDateInfo)
            outResult[1] = "true";
        else
            outResult[1] = "false";
        return outResult;
    }

    private long calnendarGetTime(String info, long time) {
        String tempDay;
        int mAmPm = -1, mTempTime = 0;
        String strOut[] = {
                "", "false"
        };
        String[] scheduleInfo = info.split(" ");
        boolean mHasTimeInfo = false;

        mAmPm = -1;// default - 1-am, 2-pm, 3-pm & add12
        for (int i = 0; i < scheduleInfo.length; i++) {
            EmailLog.d(TAG, "calnendarGetTime:schinfo[" + i + "]=" + scheduleInfo[i]);
            if (scheduleInfo[i].contains(":")) {
                try {
                    tempDay = scheduleInfo[i];
                    if (tempDay.contains("am")) { // if "5am"
                        tempDay = tempDay.replace("am", "");
                        mAmPm = 1;
                    }
                    if (tempDay.contains("pm")) {
                        tempDay = tempDay.replace("pm", "");
                        mAmPm = 2;
                    }
                    strOut = tempDay.split(":");

                    if (!((mAmPm == 1) && (Long.parseLong(strOut[0]) == 12)))
                        time += Long.parseLong(strOut[0]) * 60 * 60 * 1000;
                    // EmailLog.d(TAG, "calnendarGetTime:strOut=" + strOut[0] +
                    // ",cnt=" + scheduleInfo.length);
                    tempDay = null;
                    if (strOut.length > 1)
                        tempDay = strOut[1];
                    // time +=
                    // Long.parseLong(tempDay.split("am")[0])*60*1000;
                    if (i + 1 < scheduleInfo.length) {
                        if (scheduleInfo[i + 1].contains("pm"))
                            mAmPm = 2;
                    }
                    if (tempDay != null) {
                        time += Long.parseLong(tempDay) * 60 * 1000;
                    }
                    if (Long.parseLong(strOut[0]) < 12 && mAmPm == 2) {
                        time += (long) 12 * 60 * 60 * 1000;
                        mAmPm = 2;
                    }
                } catch (Exception e) {
                    EmailLog.e(TAG, "calnendarGetTime: #3 timeinfo '>>:' parse err!!!!!!!!!!!!!!!!");
                }

                mHasTimeInfo = true;
                // EmailLog.d(TAG, "calnendarGetTime: include time info :" +
                // String.valueOf(time));
                continue;
            } else {
                EmailLog.d(TAG,
                        "calnendarGetTime: calc tonight keyword info :" + String.valueOf(time));
                try {
                    if (scheduleInfo[i].contains("am")) { // if "5am"
                        tempDay = scheduleInfo[i].replace("am", "");
                        mAmPm = 1;

                        if (tempDay == null || tempDay.isEmpty() || tempDay.length() < 1)
                            continue;
                        if (!(Long.parseLong(tempDay) == 12))
                            time += Long.parseLong(tempDay) * 60 * 60 * 1000;
                        // EmailLog.d(TAG,
                        // "calnendarGetTime: include am info : " +
                        // String.valueOf(time));
                        mHasTimeInfo = true;
                        continue;
                    } else if (scheduleInfo[i].contains("pm")) { // if "5pm"
                        tempDay = scheduleInfo[i].replace("pm", "");
                        mAmPm = 2;

                        if (tempDay == null || tempDay.isEmpty() || tempDay.length() < 1)
                            continue;
                        time += (Long.parseLong(tempDay)) * 60 * 60 * 1000;
                        mTempTime = (int) Long.parseLong(tempDay);
                        if (mTempTime < 12)
                            time += (long) 12 * 60 * 60 * 1000;
                        // EmailLog.d(TAG,
                        // "calnendarGetTime: include pm info : " +
                        // String.valueOf(time));
                        mHasTimeInfo = true;
                        continue;
                    } else if (i + 1 < scheduleInfo.length) { // if "5 pm"
                        time += Long.parseLong(scheduleInfo[i]) * 60 * 60 * 1000;
                        if (scheduleInfo[i + 1].contains("pm")) {
                            mAmPm = 2;
                            mTempTime = (int) Long.parseLong(scheduleInfo[i]);
                            if (mTempTime < 12)
                                time += (12) * 60 * 60 * 1000;
                        }
                        mHasTimeInfo = true;
                    }
                } catch (Exception e) {
                    EmailLog.e(TAG, "calnendarGetTime: #4 timeinfo ':>>' parse err!!!!!!!!!!!!!!!!");
                }
            }
        }
        return time;
    }

    public Intent addToCalendarPeriod(String info, char cType) {
        return addToCalendarPeriodWithTitle(info, cType, "");
    }
    // action main function - major fun #2
    public Intent addToCalendarPeriodWithTitle(String info, char cType, String strCalTitle) {
        String tempDay = "";
        String splitchar = "~";
        long time = 0, newtime = 0;
        long starttime = 0, endtime = 0;
        //int mHasDatePattern = -1;
        int mTimeOffset = 0;
        boolean mHasTimeInfo = false;
        boolean mHasDateInfo = false;
        boolean mIsAllDay = false;
        boolean mIsSpecial = false;
        // boolean mHasTonightKeyword = false;

        if (mContext == null) {
            return null;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sDayFormat = new SimpleDateFormat("EEE");
        String strOut[] = {
                "", "false"
        };
        java.util.Calendar cal = java.util.Calendar.getInstance();
        TimeZone localTimezone;

        cal.set(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), java.util.Calendar
                        .getInstance().get(java.util.Calendar.MONTH),
                java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), 0, 0, 0);
        localTimezone = cal.getTimeZone();
        mTimeOffset = cal.get(java.util.Calendar.ZONE_OFFSET)
                + cal.get(java.util.Calendar.DST_OFFSET);
        //cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        sDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        sDayFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        // EmailLog.d(TAG, "addToCalendarPeriod: cal=" + cal);

        info = info.trim().toLowerCase();
        info = info.replaceAll("\\s(?i)on\\s", " ");// general ex?
        info = info.replaceAll("\\s(?i)at\\s", " ");// general ex?
        info = info.replaceAll("&nbsp;", " ");// ' '
        info = info.replaceAll("[\\.\u00a0]", " ");// ' '
        // info = info.replaceAll("[\\(\\)]", " ");// ()
        info = info.replaceAll("[\u3001\uff64\uff0c\u201a]", ",");// ' '
        info = info.replaceAll("&ndash;", "-");// '-'
        info = info.replaceAll("[\u2010-\u2015\ufe58]", "-");// '-'x
        info = info.replaceAll("[\u301c\u3030\u2053\u007e\uff5e]", "-");// '~'x
        // remove '(blarblar..)' format
        try {
            Matcher mMatKeyword = Pattern.compile("[\\(][\\s]{0,2}[\\w]{1,3}[\\s]{0,2}[\\)]")
                    .matcher(info);
            // important replaceAll(mMatKeyword.group(), "")<-not working
            if (mMatKeyword.find()) {// first
                info = info.substring(0, mMatKeyword.start()) + info.substring(mMatKeyword.end());
                if (mMatKeyword.find()) {// second
                    mMatKeyword = Pattern.compile("[\\(][\\s]{0,2}[\\w]{1,3}[\\s]{0,2}[\\)]")
                            .matcher(info);
                    if (mMatKeyword.find()) {
                        info = info.substring(0, mMatKeyword.start())
                                + info.substring(mMatKeyword.end());
                    }
                }
            }
        } catch (Exception e) {
        }
        info = info.replaceAll("\\s+", " ");

        switch (cType) {
        /*
         * case '0':// all data automatic parsing code - but we do not use.
         * addToCalendar(info); return;
         */
            case '3':// datetime - datetime ex> 2012/2/12 1:00 - 2012/3/13 12:00
            {
                splitchar = "~";
                if (!info.contains("~")) {
                    try {
                        int nDashCnt = 0;
                        int nDashPos[] = {
                                0, 0, 0, 0, 0
                        };
                        int nRef = info.length();
                        for (int i = 0; i < nRef; i++) {
                            if (info.charAt(i) == '-') {
                                nDashPos[nDashCnt++] = i;
                                if (nDashCnt > 4)
                                    break;
                            }
                        }
                        if (nDashCnt == 1)
                            info = info.replaceAll("-", "~");
                        else if (nDashCnt == 5) {
                            nRef = nDashPos[2];
                            info = info.substring(0, nRef) + " ~ "
                                    + info.substring(nRef + 1, info.length());
                        } else if (nDashCnt == 3) {
                            nRef = nRef / 2;
                            if (Math.abs(nRef - nDashPos[0]) > Math.abs(nRef - nDashPos[2]))
                                nRef = nDashPos[2];
                            else
                                nRef = nDashPos[0];
                            info = info.substring(0, nRef) + " ~ "
                                    + info.substring(nRef + 1, info.length());
                        }
                    } catch (Exception e) {
                        EmailLog.d(TAG, "addToCalendarPeriod:T3 split error" + info);
                    }
                }
                try {
                    String[] scheduleInfo = info.split(splitchar);
                    if (scheduleInfo.length != 0) {
                        strOut = calendarGetStandardDate(scheduleInfo[0]);
                        scheduleInfo[0] = strOut[0].trim();
                        if (strOut[1].equals("true"))
                            mHasDateInfo = true;
                        String[] scheduleDate = scheduleInfo[0].split(" ");
                        starttime = (sDateFormat.parse(scheduleDate[0]).getTime());
                        info = scheduleInfo[0].replaceAll(scheduleDate[0], "");
                    }
                    if (scheduleInfo.length > 1) {
                        strOut = calendarGetStandardDate(scheduleInfo[1]);
                        scheduleInfo[1] = strOut[0];
                        if (strOut[1].equals("true"))
                            mHasDateInfo = true;
                        String[] scheduleDate = scheduleInfo[1].split(" ");
                        endtime = (sDateFormat.parse(scheduleDate[0]).getTime());
                        info = info + "~" + scheduleInfo[1].replaceAll(scheduleDate[0], "");
                    }
                    mHasTimeInfo = true;
                } catch (Exception e) {
                    EmailLog.d(TAG, "addToCalendarPeriod:T3 parse error" + info);
                }
            }
            break;
            case '2':// date - date ex> 2012/2/12 - 2012/2/13
            {
                splitchar = "~";
                if (!info.contains("~")) {
                    try {
                        int nDashCnt = 0;
                        int nDashPos[] = {
                                0, 0, 0, 0, 0
                        };
                        int nRef = info.length();
                        for (int i = 0; i < nRef; i++) {
                            if (info.charAt(i) == '-') {
                                nDashPos[nDashCnt++] = i;
                                if (nDashCnt > 4)
                                    break;
                            }
                        }
                        if (nDashCnt == 1)
                            info = info.replaceAll("-", "~");
                        else if (nDashCnt == 5) {
                            nRef = nDashPos[2];
                            info = info.substring(0, nRef) + " ~ "
                                    + info.substring(nRef + 1, info.length());
                        } else if (nDashCnt == 3) {
                            nRef = nRef / 2;
                            if (Math.abs(nRef - nDashPos[0]) > Math.abs(nRef - nDashPos[2]))
                                nRef = nDashPos[2];
                            else
                                nRef = nDashPos[0];
                            info = info.substring(0, nRef) + " ~ "
                                    + info.substring(nRef + 1, info.length());
                        }
                    } catch (Exception e) {
                        EmailLog.d(TAG, "addToCalendarPeriod:T2 split error" + info);
                    }
                }
                try {
                    String[] scheduleInfo = info.split(splitchar);
                    if (scheduleInfo.length != 0) {
                        strOut = calendarGetStandardDate(scheduleInfo[0]);
                        scheduleInfo[0] = strOut[0];
                        if (strOut[1].equals("true"))
                            mHasDateInfo = true;
                        info = scheduleInfo[0];
                    }
                    if (scheduleInfo.length > 1) {
                        strOut = calendarGetStandardDate(scheduleInfo[1]);
                        scheduleInfo[1] = strOut[0];
                        if (strOut[1].equals("true"))
                            mHasDateInfo = true;
                        info = scheduleInfo[0] + "~" + scheduleInfo[1];
                    }
                    mHasTimeInfo = false;
                } catch (Exception e) {
                    EmailLog.d(TAG, "addToCalendarPeriod:T2 parse error" + info);
                }
            }
            break;
            case '6':// dow date except year - dow date ex> sat oct 15 - mon oct
                // 17 2012
            {
                if (info.contains("-"))
                    splitchar = "-";
                if (info.contains("~"))
                    splitchar = "~";
                String[] scheduleInfo = info.split(splitchar);
                String[] dateInfo;
                String tempinfo = "", outstr = "";
                if (scheduleInfo.length > 1) {
                    tempinfo = scheduleInfo[1];
                    strOut = calendarGetStandardDate(tempinfo);
                    tempinfo = strOut[0];
                    if (strOut[1].equals("true"))
                        mHasDateInfo = true;
                    dateInfo = tempinfo.split("/");
                    if (dateInfo.length != 3) {
                        EmailLog.e(TAG, "addToCalendarPeriod:T6 incorrect date=[" + tempinfo
                                + "] or parse err=" + Arrays.toString(dateInfo));
                    }
                    outstr = tempinfo;// 22/01/2001
                    tempinfo = convertStandardRemoveDayofWeek(scheduleInfo[0]);
                    tempinfo = tempinfo + " " + dateInfo[2];
                    strOut = convertLocalDateToTime(tempinfo); // result
                    // dd/mm/yyyy
                    tempinfo = strOut[0];
                    if (strOut[1].equals("true"))
                        mHasDateInfo = true;
                    strOut = convertStandardDateToTime(tempinfo); // result
                    // dd/mm/yyyy
                    tempinfo = strOut[0];
                    tempinfo = tempinfo.trim().replaceAll(" ", "");
                    info = tempinfo + "~" + outstr;
                } else {
                    EmailLog.e(TAG, "addToCalendarPeriod:T6 incorrect type=[" + cType
                            + "] or date=" + info);
                }
                mHasTimeInfo = false;
            }
            break;
            case '7':// date time-time ex> (sat) 02/12/2012 2:00-4:00
                strOut = calendarGetStandardDate(info);
                info = strOut[0];
                if (strOut[1].equals("true"))
                    mHasDateInfo = true;
                if (mHasDateInfo) {
                    try {
                        String[] scheduleInfo = info.split(" ");
                        tempDay = scheduleInfo[0];
                        time = (sDateFormat.parse(tempDay).getTime());
                        info = info.replaceAll(tempDay, "");
                    } catch (Exception e) {
                        EmailLog.e(TAG, "addToCalendarPeriod:T7 parse err=" + tempDay);
                    }
                }
                starttime = time;
                endtime = time;
                mHasTimeInfo = true;
                mHasDateInfo = false;
                break;
            case '5': // keyword time-time ex> Today 2:00-4:00
                mHasDateInfo = false;
                strOut = convertKeywordToDateTime(info, mHasDateInfo);
                if (strOut[1].equals("true"))
                    mIsAllDay = true;
                else if (strOut[1].equals("special"))
                    mIsSpecial = true;
                try {
                    String[] scheduleInfo = strOut[0].split(" ");
                    tempDay = scheduleInfo[0];
                    time = (sDateFormat.parse(tempDay).getTime());
                    if (scheduleInfo.length > 1) {
                        info = scheduleInfo[1];
                        for (int i = 2; i < scheduleInfo.length; i++) {
                            info += scheduleInfo[i];
                        }
                        mHasTimeInfo = true;
                        mIsSpecial = false;
                        mIsAllDay = false;
                    } else
                        mHasTimeInfo = false;
                } catch (Exception e) {
                    EmailLog.e(TAG, "addToCalendarPeriod:T5 parse err=" + tempDay);
                }
                starttime = time;
                endtime = time;
                mHasDateInfo = false;
                break;
            case '4':// time-time date ex> 2:00 - 4:00 29th Oct 2012
                strOut = calendarGetStandardDate(info);
                info = strOut[0];
                if (strOut[1].equals("true"))
                    mHasDateInfo = true;
                if (mHasDateInfo) {
                    try {
                        String[] scheduleInfo = info.split(" ");
                        tempDay = scheduleInfo[scheduleInfo.length - 1];
                        time = (sDateFormat.parse(tempDay).getTime());
                        info = info.replaceAll(tempDay, "");
                    } catch (Exception e) {
                        EmailLog.e(TAG, "addToCalendarPeriod:T4 parse err=" + tempDay);
                    }
                }
                starttime = time;
                endtime = time;
                mHasTimeInfo = true;
                mHasDateInfo = false;
                break;
            // - FALL THROUGH - IMPORTANT!! (actually we need to just case 1
            // code
            case '1':// time-time ex> 2:00 - 4:00
                try {
                    // if ( mMessage != null) {// datetime of email
                    // Date date = new Date(mMessage.mTimeStamp);
                    if (mTimeOfMessage != 0) {
                        Date date = new Date(mTimeOfMessage);
                        cal.setTimeInMillis(date.getTime());
                    }/*
                      * else { // today
                      * cal.set(java.util.Calendar.getInstance().
                      * get(java.util.Calendar.YEAR), java.util.Calendar
                      * .getInstance().get(java.util.Calendar.MONTH),
                      * java.util.Calendar.getInstance().get(
                      * java.util.Calendar.DAY_OF_MONTH), 0, 0, 0); }
                      */
                } catch (Exception e) {/*
                                        * cal.set(java.util.Calendar.getInstance(
                                        * ).get(java.util.Calendar.YEAR),
                                        * java.util.Calendar
                                        * .getInstance().get(java
                                        * .util.Calendar.MONTH),
                                        * java.util.Calendar.getInstance().get(
                                        * java.util.Calendar.DAY_OF_MONTH), 0,
                                        * 0, 0);
                                        */
                }
                try {
                    tempDay = (cal.get(Calendar.DAY_OF_MONTH)) + "/"
                            + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR) + " ";
                    time = (sDateFormat.parse(tempDay).getTime());
                } catch (Exception e) {
                    EmailLog.e(TAG, "addToCalendarPeriod:T1 parse err=" + time);
                }
                starttime = time;
                endtime = time;
                mHasTimeInfo = true;
                mHasDateInfo = false;
                break;
            default:
                // Toast.makeText(mContext, cType+":"+info,
                // Toast.LENGTH_SHORT).show();
                return null;
        }

        EmailLog.d(TAG, "addToCalendarPeriod: inputdata=" + info + ", CType[" + cType + "]");
        info = info.replaceAll(",", " ");

        if (mHasTimeInfo) { // just time info
            try {
                strOut = convertLocalDateToTime(info);
                info = strOut[0].trim();
                info = info.replaceAll(" ", "");
                EmailLog.d(TAG, "addToCalendarPeriod: mHasTimeInfo=" + info);

                if (info.contains("-"))
                    splitchar = "-";
                if (info.contains("~"))
                    splitchar = "~";
                String[] scheduleInfo = info.split(splitchar);
                if (scheduleInfo.length != 0) {
                    newtime = calnendarGetTime(scheduleInfo[0], starttime);
                    if (newtime > starttime) {
                        starttime = newtime;
                    }
                }
                if (scheduleInfo.length > 1) {
                    long endnewtime = calnendarGetTime(scheduleInfo[1], endtime);
                    if (endnewtime < newtime) // ex) 12:00 pm ~ 1:00 am
                        newtime = endnewtime + 24 * 60 * 60 * 1000; // +1day
                    else
                        newtime = endnewtime;
                    if (newtime > endtime) {
                        endtime = newtime;
                    }
                    String temp = scheduleInfo[0];
                    if(temp.contains(":"))
                        temp = temp.split(":")[0];

                    // ex) 2:40 - 4:40 pm -> 2:40 pm - 4:40 pm
                    if (!scheduleInfo[0].matches(".*(?:(?i:am)|(?i:pm)).*")
                            && scheduleInfo[1].matches(".*(?i:pm).*") && !(Long.parseLong(temp) == 12)) {
                        starttime = starttime + 12 * 60 * 60 * 1000;
                    }
                }
            } catch (Exception e) {
            }
        }
        if (mHasDateInfo && !mHasTimeInfo) { // 21/02/2012~22/02/2012
            info = info.trim().replaceAll(" ", "");
            String[] scheduleInfo = info.split("~");
            if (scheduleInfo.length != 0) {
                try {
                    starttime = (sDateFormat.parse(scheduleInfo[0]).getTime());
                } catch (Exception e) {
                }
            }
            if (scheduleInfo.length > 1) {
                try {
                    endtime = (sDateFormat.parse(scheduleInfo[1]).getTime());
                } catch (Exception e) {
                }
            }
            mIsAllDay = true;
        }
        if (!mIsAllDay && !mHasTimeInfo && mHasDateInfo)
            mIsAllDay = true;
        EmailLog.d(TAG, "addToCalendarPeriod: hasTime=" + mHasTimeInfo + ", hasDate="
                + mHasDateInfo);
        if (!mIsAllDay && mIsSpecial && !mHasTimeInfo) {
            starttime += (20) * 60 * 60 * 1000;// if 'tonight', then +8:00pm
            endtime += (20) * 60 * 60 * 1000;
        }
        if (mIsAllDay && mHasTimeInfo)
            mIsAllDay = false; // 'time' info first!!

        // our datetime is GMT+0
        if (mIsAllDay) { // because calendar app we add to local TimeZone
            // 2000/01/01 00:00:00 ~ 2000/01/02 23:59:59
            try {
                cal.setTimeInMillis(starttime);
                cal.setTimeZone(localTimezone);
                starttime -= (cal.get(java.util.Calendar.ZONE_OFFSET) + cal
                        .get(java.util.Calendar.DST_OFFSET));
            } catch (Exception e) {
                starttime -= mTimeOffset;
            }
            try {
                cal.setTimeInMillis(endtime);
                cal.setTimeZone(localTimezone);
                endtime -= (cal.get(java.util.Calendar.ZONE_OFFSET) + cal
                        .get(java.util.Calendar.DST_OFFSET));
            } catch (Exception e) {
                endtime -= mTimeOffset;
            }
            endtime += ((24) * 60 * 60 * 1000 - 1);
        } else { // automatic?? calendar add to timezone? +xx??
            try {
                cal.setTimeInMillis(starttime);
                cal.setTimeZone(localTimezone);
                starttime -= (cal.get(java.util.Calendar.ZONE_OFFSET) + cal
                        .get(java.util.Calendar.DST_OFFSET));
            } catch (Exception e) {
                starttime -= mTimeOffset;
            }
            try {
                cal.setTimeInMillis(endtime);
                cal.setTimeZone(localTimezone);
                endtime -= (cal.get(java.util.Calendar.ZONE_OFFSET) + cal
                        .get(java.util.Calendar.DST_OFFSET));
            } catch (Exception e) {
                endtime -= mTimeOffset;
            }
        }
        if (starttime == 0)
            endtime = 0;
        else if (starttime >= endtime)
            endtime = starttime + 60 * 60 * 1000;
        EmailLog.d(TAG, "addToCalendarPeriod time info : " + String.valueOf(time) + ", allday="
                + mIsAllDay);

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra(android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME, starttime);// ms
            intent.putExtra(android.provider.CalendarContract.EXTRA_EVENT_END_TIME, endtime);
            intent.putExtra(android.provider.CalendarContract.Events.ALL_DAY, mIsAllDay);
            intent.putExtra(android.provider.CalendarContract.Events.TITLE, strCalTitle);
            intent.putExtra(android.provider.CalendarContract.Events.DESCRIPTION, mCallBack != null ? mCallBack.getAppName() : "Email");
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // mContext.startActivity(intent);
            return intent;
        } catch (Exception e) {
            EmailLog.e(TAG, "addToCalendarPeriod #11 calendar intent err !!!!!");
        }
        return null;
    }
    // datetime action functions ----> end

    private boolean mStop = false;
    public void stopPatternMatching() {
        mStop = true;
    }

    private boolean mChangeContents = false;
    public boolean getIsChangeContents() {
        return mChangeContents;
    }
}