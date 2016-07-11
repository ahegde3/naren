/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsung.android.emailcommon.utility;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailHtmlUtil {

    // Regex that matches characters that have special meaning in HTML. '<',
    // '>', '&' and
    // multiple continuous spaces.
    private static final Pattern PLAIN_TEXT_TO_ESCAPE = Pattern.compile("[<>&]| {2,}|\r?\n");

    /**
     * Escape some special character as HTML escape sequence.
     * 
     * @param text Text to be displayed using WebView.
     * @return Text correctly escaped.
     */
    // jk0112.lee,for fixing memory leak
    static StringBuilder out = null;

    public static String escapeCharacterToDisplay(String text) {
        Pattern pattern = PLAIN_TEXT_TO_ESCAPE;
        Matcher match = pattern.matcher(text);

        if (match.find()) {
            // jk0112.lee,for fixing memory leak
            if (out == null)
                out = new StringBuilder();
            int end = 0;
            do {
                int start = match.start();
                out.append(text.substring(end, start));
                end = match.end();
                int c = text.codePointAt(start);
                if (c == ' ') {
                    // Escape successive spaces into series of "&nbsp;".
                    for (int i = 1, n = end - start; i < n; ++i) {
                        out.append("&nbsp;");
                    }
                    out.append(' ');
                } else if (c == '\r' || c == '\n') {
                    out.append("<br>");
                } else if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                }
            } while (match.find());
            out.append(text.substring(end));
            text = out.toString();
            // jk0112.lee,for fixing memory leak
            out.delete(0, out.length());
        }
        return text;
    }

    public static String extractTextFromHtml(final String str) {
        StringBuffer tempString = new StringBuffer();
        StringBuffer tempString2 = new StringBuffer();
        String returnText = null;

        if (str == null)
            return "";

        char[] c = str.toCharArray();
        char ch;

        int position = 0;
        boolean check = false;
        boolean scriptChkeck = false;
        boolean styleCheck = false;

        for (int i = 0, len = c.length; i < len; i++) {
            ch = c[i];

            if (ch == '<') {
                check = true;

                try {
                    String temp = str.substring(i + 1, str.indexOf('>', i + 1));
                    String attrib = temp.trim().toLowerCase(Locale.ENGLISH);

                    if ((attrib.equals("br") || attrib.equals("/br"))
                            || (attrib.equals("p") || attrib.equals("/p"))) {

                        tempString.append(ch).append(temp).append('>');
                        i += (temp.length() + 1);
                        check = false;
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!check && !scriptChkeck && !styleCheck) {
                tempString.append(ch);
            }

            position++;
            tempString2.append(ch);

            if (position > 9) {
                tempString2.delete(0, 1);
            }

            if (!scriptChkeck) {
                if (tempString2.toString().toLowerCase(Locale.ENGLISH).indexOf("<script") == 0) {
                    scriptChkeck = true;
                }
            }

            if (scriptChkeck) {
                if (tempString2.toString().toLowerCase(Locale.ENGLISH).indexOf("</script>") == 0) {
                    scriptChkeck = false;
                }
            }

            if (!styleCheck) {
                if (tempString2.toString().toLowerCase(Locale.ENGLISH).indexOf("<style") == 0) {
                    styleCheck = true;
                }
            }

            if (styleCheck) {
                if (tempString2.toString().toLowerCase(Locale.ENGLISH).indexOf("</style>") == 0) {
                    styleCheck = false;
                }
            }

            if (ch == '>') {
                check = false;
            }
        }

        returnText = tempString.toString();

        returnText = returnText.replaceAll("&nbsp;", "");
        returnText = returnText.replaceAll("&lt;", "<");
        returnText = returnText.replaceAll("&gt;", ">");
        returnText = returnText.replaceAll("&#43;", "+");

        if (returnText.length() == 0)
            return str;

        return returnText;
    }
}