/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.codec;

import org.apache.james.mime4j.Log;
import org.apache.james.mime4j.LogFactory;
import org.apache.james.mime4j.util.CharsetUtil;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.UniversalDetector;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Static methods for decoding strings, byte arrays and encoded words.
 */
public class DecoderUtil {

    private static final Pattern PATTERN_ENCODED_WORD = Pattern.compile(
            "(.*?)=\\?(.+?)\\?(\\w)\\?(.*?)\\?=", Pattern.DOTALL);

    private static Log log = LogFactory.getLog(/*DecoderUtil.class*/);

    protected static boolean isAscii(byte[] aBuf, int aLen) {
        for (int i = 0; i < aLen; i++) {
            if ((0x0080 & aBuf[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    public static String chardet(String s) {
        if (s == null)
            return s;

        try {
            // (2)
            ByteArrayOutputStream imp = new ByteArrayOutputStream();

            char[] chars = s.toCharArray();
            for (char c : chars)
                imp.write(c);

            byte[] buf = imp.toByteArray();
            imp.flush();
            // imp.close();
            chars = null;

            if (isAscii(buf, buf.length)) {
                return null;
            }
            if (s.contains("<") && s.contains("@") && s.contains(">")) { // email
                                                                         // address?
                chars = s.substring(0, s.indexOf("<")).replaceAll("\"", " ").trim().toCharArray();
                imp.reset();
                for (char c : chars)
                    imp.write(c);
                buf = imp.toByteArray();
                imp.flush();
                chars = null;
            }
            imp.close();
            // (1)
            UniversalDetector detector = new UniversalDetector(null);
            final int nRefLength = 50;
            // (3)
            if (buf.length < nRefLength) {
                int nTotal = 0;
                for (int i = 0; i < nRefLength; i++) {
                    detector.handleData(buf, 0, buf.length);
                    nTotal += buf.length;
                    if (nTotal > nRefLength || detector.isDone())
                        break;
                }
            } else
                detector.handleData(buf, 0, buf.length);
            detector.dataEnd();

            // (4)
            String encoding = detector.getDetectedCharset();

            if (encoding != null) {
                log.debug("Detected encoding = " + encoding);
                if (Constants.CHARSET_UTF_16BE.equals(encoding)
                        || Constants.CHARSET_UTF_16LE.equals(encoding)
                        || Constants.CHARSET_UTF_32BE.equals(encoding)
                        || Constants.CHARSET_UTF_32LE.equals(encoding))
                    encoding = null;
            } else {
                log.debug("No encoding detected.");
            }

            String defaultCharset = CharsetUtil.getLocalCharset();

            if (defaultCharset != null && !defaultCharset.equals("ASCII")) {
                try {
                    String getDetectedAllCharset = detector.getDetectedAllCharset();
                    String[] tokens1 = getDetectedAllCharset.trim().split(" ");
                    for (String charset : tokens1) {
                        if (charset.equals(defaultCharset)) {
                            encoding = defaultCharset;
                            break;
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (encoding == null) {
                encoding = defaultCharset;
            }
            // (5)
            detector.reset();
            detector = null;
            return encoding;
        } catch (Exception e) {
        }
        return null;
    }

    /*
     * // adjust archer source to resolve Korea language encoding. hanshik.jung,
     * // 2010.05.02 public static String chardet2(String s) { if (s == null)
     * return s; // Initalize the nsDetector() with default locale Kr (5) or all
     * // (nsPSMDetector.ALL) int lang = nsPSMDetector.ALL; nsDetector det = new
     * nsDetector(lang); // Set an observer... // The Notify() will be called
     * when a matching charset is found. det.Init(new
     * nsICharsetDetectionObserver() { public void Notify(String charset) {
     * log.info("Detected CHARSET = " + charset); } }); String result;
     * ByteArrayOutputStream imp = new ByteArrayOutputStream(); char[] chars =
     * s.toCharArray(); for (char c : chars) imp.write(c); byte[] buf =
     * imp.toByteArray(); boolean isAscii = true; // Check if the stream is only
     * ascii. isAscii = det.isAscii(buf, buf.length); // DoIt if non-ascii and
     * not done yet. if (!isAscii) { det.DoIt(buf, buf.length, false); }
     * det.DataEnd(); result = null; if (isAscii) { // result = new
     * String("US-ASCII"); result = null; } else { String prob[] =
     * det.getProbableCharsets(); boolean bTag1=false; String
     * defaultCharset=CharsetUtil.getLocalCharset(); if ( prob[0] != null ) {
     * result = prob[0]; for ( String charset : prob ) { if (
     * !(charset.toLowerCase
     * (Locale.ENGLISH).contains("utf16")||charset.toLowerCase
     * (Locale.ENGLISH).contains("utf-16"))) { result = charset; break; } } } if
     * ( defaultCharset == null ) defaultCharset = String.valueOf("ASCII"); //
     * case Korean. for (String charset : prob) { if (charset.equals("EUC_KR"))
     * { bTag1 = true; break; } if ( charset.equals(defaultCharset) ){ result =
     * defaultCharset; break; } } if ( bTag1 ) result =
     * String.valueOf("EUC-KR"); } return result; }
     */
    /**
     * Check if it is encoded words and unless it is, decode it with euc-kr
     * 
     * @author chinst
     * @date 2010.2.20.
     * @param s the string to decode
     * @return the decoded bytes
     */

    public static String justDecode(String s, String charset) {
        // if it is encoded words, just return as it was.
        if (s == null || s.indexOf("=?") != -1) {
            return s;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String result;
        try {
            char[] chars = s.toCharArray();

            for (char c : chars) {
                baos.write(c);
            }
            result = new String(baos.toByteArray(),
                    CharsetUtil.toJavaCharset(charset.toLowerCase()));
        } catch (UnsupportedEncodingException e) {
            result = s;
        }
        return result;
    }

    /**
     * Check if the string is an encoded-words, then decode it if it is not an
     * encoded-words, check if it is encoded if it is, justDecode and return the
     * result
     * 
     * @author chinst
     * @date 2010.2.20
     * @param s the string to check and decode
     * @return the decoded string
     */

    public static String decodeGeneric(String s) {

        if (s == null)
            return s;

        String result;
        if (s.indexOf("=?") == -1) {
            // detect
            String charset = chardet(s);
            if (charset == null)
                result = s;
            else
                result = justDecode(s, charset);
        } else {
            result = decodeEncodedWords(s);
        }

        return result;
    }
	
    /**
     * Decodes a string containing quoted-printable encoded data.
     *
     * @param s the string to decode.
     * @return the decoded bytes.
     */
    private static byte[] decodeQuotedPrintable(String s, DecodeMonitor monitor) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QuotedPrintableInputStream is = null;
        try {
            byte[] bytes = s.getBytes("US-ASCII");

            is = new QuotedPrintableInputStream(new ByteArrayInputStream(bytes), monitor);

            int b = 0;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {
            // This should never happen!
            // throw new IllegalStateException(e);
            log.error(e);
        } finally {
            try {
                if (is != null)// sec.email tom.jung For RSAR 2nd
                    is.close();
            } catch (IOException e) {
                // nothing to do.
            }
        }

        return baos.toByteArray();
    }

    /**
     * Decodes a string containing base64 encoded data.
     *
     * @param s the string to decode.
     * @param monitor
     * @return the decoded bytes.
     */
    private static byte[] decodeBase64(String s, DecodeMonitor monitor) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base64InputStream is = null;
        try {
            byte[] bytes = s.getBytes("US-ASCII");

            is = new Base64InputStream(new ByteArrayInputStream(bytes), monitor);

            int b = 0;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {
            // This should never happen!
            // throw new IllegalStateException(e);
            log.error(e);
        } finally {
            try {
                if (is != null)// sec.email tom.jung For RSAR 2nd
                    is.close();
            } catch (IOException e) {
                // nothing to do.
            }
        }

        return baos.toByteArray();
    }

    /**
     * Decodes an encoded text encoded with the 'B' encoding (described in
     * RFC 2047) found in a header field body.
     *
     * @param encodedText the encoded text to decode.
     * @param charset the Java charset to use.
     * @param monitor
     * @return the decoded string.
     * @throws UnsupportedEncodingException if the given Java charset isn't
     *         supported.
     */
    static byte[] decodeB(String encodedText, /*String charset,*/ DecodeMonitor monitor)
            throws UnsupportedEncodingException {
        //byte[] decodedBytes = decodeBase64(encodedText, monitor);
        //return new String(decodedBytes, charset);
        return decodeBase64(encodedText, monitor);
    }

    /**
     * Decodes an encoded text encoded with the 'Q' encoding (described in
     * RFC 2047) found in a header field body.
     *
     * @param encodedText the encoded text to decode.
     * @param charset the Java charset to use.
     * @return the decoded string.
     * @throws UnsupportedEncodingException if the given Java charset isn't
     *         supported.
     */
    static byte[] decodeQ(String encodedText, /*String charset,*/ DecodeMonitor monitor)
            throws UnsupportedEncodingException {
        encodedText = replaceUnderscores(encodedText);

        //byte[] decodedBytes = decodeQuotedPrintable(encodedText, monitor);
        //return new String(decodedBytes, charset);
        return decodeQuotedPrintable(encodedText, monitor);
    }

    public static String decodeEncodedWords(String body)  {
        //return decodeEncodedWords(body, DecodeMonitor.SILENT);
        try {
            return decodeEncodedWords(body, DecodeMonitor.SILENT);            
        } catch (Exception e) {
            return body;
        }
    }

    /**
     * Decodes a string containing encoded words as defined by RFC 2047. Encoded
     * words have the form =?charset?enc?encoded-text?= where enc is either 'Q'
     * or 'q' for quoted-printable and 'B' or 'b' for base64.
     *
     * @param body the string to decode
     * @param monitor the DecodeMonitor to be used.
     * @return the decoded string.
     * @throws IllegalArgumentException only if the DecodeMonitor strategy throws it (Strict parsing)
     */
    public static String decodeEncodedWords(String body, DecodeMonitor monitor)
            throws IllegalArgumentException {
        int tailIndex = 0;
        boolean lastMatchValid = false;

        if (body.length() < 1)
            return body;
        String charset = "ASCII";
        CharBuffer outData = CharBuffer.allocate(body.length() + 1);

        String separator2 = "";
        String mimeCharset2 = "";
        String encoding2 = "";
        String encodedText2 = "";
        byte decodedAll[] = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream(); 
        boolean exceptionInDecode = false;
        
        for (Matcher matcher = PATTERN_ENCODED_WORD.matcher(body); matcher.find();) {
            String separator = matcher.group(1);
            String mimeCharset = matcher.group(2);
            String encoding = matcher.group(3);
            String encodedText = matcher.group(4);
            
            separator2 = separator;
            mimeCharset2 = mimeCharset;
            encoding2 = encoding;
            encodedText2 = encodedText2.concat(encodedText);
            
            /*
             * String decoded = null; decoded =
             * tryDecodeEncodedWord(mimeCharset, encoding, encodedText,
             * monitor); if (decoded == null) { sb.append(matcher.group(0)); }
             * else { if (!lastMatchValid ||
             * !CharsetUtil.isWhitespace(separator)) { sb.append(separator); }
             * sb.append(decoded); }
             */
            Charset csCharset = null;
            try {
                mimeCharset = CharsetUtil.toJavaCharset(mimeCharset);
                if (mimeCharset == null || mimeCharset.isEmpty())
                    mimeCharset = CharsetUtil.getLocalCharset();
                csCharset = CharsetUtil.lookup(mimeCharset);
                if (csCharset == null)
                    csCharset = Charset.forName(charset);
            } catch (Exception e) {
            }
            byte[] decoded = null;
            try {
                if (mimeCharset != null && (encodedText != null && encodedText.length() == 0)
                        && ("Q".equalsIgnoreCase(encoding) || "B".equalsIgnoreCase(encoding))) {
                    outData = outData.put("");
                } else {
                    CharsetDecoder csDecoder = csCharset.newDecoder();
                    decoded = tryDecodeEncodedWord(mimeCharset, encoding, encodedText, monitor);
                    try {
                        if (decoded == null) {
                            outData = outData.put(matcher.group(0));
                        } else {
                            if (!lastMatchValid || !CharsetUtil.isWhitespace(separator)) {
                                outData = outData.put(separator);
                            }
                            
                            // Add decoded bytes to output stream. We will use it later to make final bytes array
                            if (os != null) os.write(decoded);
                            final CharBuffer cb = csDecoder.decode(ByteBuffer.wrap(decoded));
                            outData = outData.put(cb);
                        }
                    } catch (Exception e) {
                    	exceptionInDecode = true;
                    }
                }
            } catch (Exception e1) {
            }

            tailIndex = matcher.end();
            lastMatchValid = decoded != null;
        }

        if (os != null) {
            try {
                decodedAll = os.toByteArray();
            } catch (Exception e) {
            } finally {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                }
            }
        }
        
        
        
        if (tailIndex == 0) {
            return body;
        } else {
            // sb.append(body.substring(tailIndex));
            // return sb.toString();
            outData = outData.put(body.substring(tailIndex));
            String out = "";
            try {
                outData.flip();
                out = outData.toString().trim();
            } catch (Exception e) {
                out = body;
            }
            
            if (TextUtils.isEmpty(out) || exceptionInDecode) {
            	out = decodeEncodedWords2(/*separator2,*/ mimeCharset2,
                		encoding2, encodedText2, decodedAll, monitor);
            }
            
            return out;
        }
    }
    
    // Decode again with full string of encoded words
    private static String decodeEncodedWords2(/*String separator,*/
            String mimeCharset, String encoding, String encodedText, byte[] decodedBytes,
            DecodeMonitor monitor) throws IllegalArgumentException {

        String originalCharset = mimeCharset;
        String out = "";
        String charset = "ASCII";
        Charset csCharset = null;
        
        try {
            mimeCharset = CharsetUtil.toJavaCharset(mimeCharset);
            if (mimeCharset == null || mimeCharset.isEmpty())
                mimeCharset = CharsetUtil.getLocalCharset();
            csCharset = CharsetUtil.lookup(mimeCharset);
            if (csCharset == null)
                csCharset = Charset.forName(charset);
        } catch (Exception e) {
        }

        byte[] decoded = null;
        CharsetDecoder csDecoder = csCharset.newDecoder();
        decoded = tryDecodeEncodedWord(mimeCharset, encoding, encodedText,
                monitor);

        try {
            if(decoded != null) {
                final CharBuffer cb = csDecoder.decode(ByteBuffer.wrap(decoded));
                out = cb.toString();
            }
        } catch (Exception e) {
            try {
                boolean decodeAllFailed = false;
                // Try to decode all bytes decoded from Base64 together
                if (decodedBytes != null) {
                    try {
                        final CharBuffer cb = csDecoder.decode(ByteBuffer.wrap(decodedBytes));
                        if (cb != null) out = cb.toString();
                    } catch (Exception ex) {
                        decodeAllFailed = true;
                    }
                }
                
                if (decodedBytes == null || decodeAllFailed) {
                    // We have specific issue with some Chinese emails
                    // By some reason sometimes last character in Chinese Base64/GB2312 string is corrupted
                    // Probably, it happened, because some Chinese characters have 3 bytes size
                    // We found, that such issue can be fixed, if we add "=" symbol at the end of Base64 encoded string
                    // Try this solution if all normal cases were failed
                    if ("gb2312".equalsIgnoreCase(originalCharset)) {
                        encodedText = encodedText + "=";
                        decoded = tryDecodeEncodedWord(mimeCharset, encoding, encodedText,
                                monitor);
                        try {
                            if(decoded != null) {
                                final CharBuffer cb = csDecoder.decode(ByteBuffer.wrap(decoded));
                                out = cb.toString();
                            }
                        } catch (Exception e1) {
                            // If CharsetDecoder failed, try simple decoding
                            if (decoded != null) {
                                out = new String(decoded, "GB3212");
                            }
                        }
                    }
                    
                    // We could not decode with specified charset. 
                    // Finally, try simple decoding with UTF-8
                    if (out == null && decoded != null) {
                        out = new String(decoded, "UTF-8");
                    }
                }
            } catch (Exception ex) {
            }
        }
            return out;
	}

    // return null on error
    private static byte[] tryDecodeEncodedWord(final String mimeCharset,
            final String encoding, final String encodedText, final DecodeMonitor monitor) {
        Charset charset = CharsetUtil.lookup(mimeCharset);
        if (charset == null) {
            monitor(monitor, mimeCharset, encoding, encodedText, "leaving word encoded",
                    "Mime charser '", mimeCharset, "' doesn't have a corresponding Java charset");
            return null;
        }

        if (encodedText.length() == 0) {
            monitor(monitor, mimeCharset, encoding, encodedText, "leaving word encoded",
                    "Missing encoded text in encoded word");
            return null;
        }

        try {
            if (encoding.equalsIgnoreCase("Q")) {
                return DecoderUtil.decodeQ(encodedText, /*charset.name(),*/ monitor);
            } else if (encoding.equalsIgnoreCase("B")) {
                return DecoderUtil.decodeB(encodedText, /*charset.name(),*/ monitor);
            } else {
                monitor(monitor, mimeCharset, encoding, encodedText, "leaving word encoded",
                        "Warning: Unknown encoding in encoded word");
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            // should not happen because of isDecodingSupported check above
            monitor(monitor, mimeCharset, encoding, encodedText, "leaving word encoded",
                    "Unsupported encoding (", e.getMessage(), ") in encoded word");
            return null;
        } catch (RuntimeException e) {
            monitor(monitor, mimeCharset, encoding, encodedText, "leaving word encoded",
                    "Could not decode (", e.getMessage(), ") encoded word");
            return null;
        }
    }

    private static void monitor(DecodeMonitor monitor, String mimeCharset, String encoding,
            String encodedText, String dropDesc, String... strings) throws IllegalArgumentException {
        if (monitor.isListening()) {
            String encodedWord = recombine(mimeCharset, encoding, encodedText);
            StringBuilder text = new StringBuilder();
            for (String str : strings) {
                text.append(str);
            }
            text.append(" (");
            text.append(encodedWord);
            text.append(")");
            String exceptionDesc = text.toString();
            if (monitor.warn(exceptionDesc, dropDesc))
                throw new IllegalArgumentException(text.toString());
        }
    }

    private static String recombine(final String mimeCharset,
            final String encoding, final String encodedText) {
        return "=?" + mimeCharset + "?" + encoding + "?" + encodedText + "?=";
    }

    // Replace _ with =20
    private static String replaceUnderscores(String str) {
        // probably faster than String#replace(CharSequence, CharSequence)

        StringBuilder sb = new StringBuilder(128);

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                sb.append("=20");
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
    
    public static String charsetDetect(byte[] data,int size)
    {
        UniversalDetector detector = new UniversalDetector(null);

        detector.handleData(data, 0, size);
        detector.dataEnd();
        String charset = detector.getDetectedCharset();
        detector.reset();
        
        return charset;
    }
}
