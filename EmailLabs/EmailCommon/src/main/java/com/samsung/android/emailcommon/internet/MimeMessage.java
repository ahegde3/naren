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

package com.samsung.android.emailcommon.internet;

import com.samsung.android.emailcommon.mail.Address;
import com.samsung.android.emailcommon.mail.Body;
import com.samsung.android.emailcommon.mail.BodyPart;
import com.samsung.android.emailcommon.mail.Message;
import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.mail.Multipart;
import com.samsung.android.emailcommon.mail.Part;
import com.samsung.android.emailcommon.utility.EmailLog;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.mime4j.parser.ContentHandler;
//import org.apache.james.mime4j.io.EOLConvertingInputStream;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.util.CharsetUtil;
import org.apache.james.mime4j.secfunc.*;
import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * An implementation of Message that stores all of its metadata in RFC 822 and
 * RFC 2045 style headers. NOTE: Automatic generation of a local message-id is
 * becoming unwieldy and should be removed. It would be better to simply do it
 * explicitly on local creation of new outgoing messages.
 */
public class MimeMessage extends Message {
    private MimeHeader mHeader;
    private MimeHeader mExtendedHeader;

    // NOTE: The fields here are transcribed out of headers, and values stored
    // here will supercede
    // the values found in the headers. Use caution to prevent any out-of-phase
    // MimeExceptions. In
    // particular, any adds/changes/deletes here must be echoed by changes in
    // the parse() function.
    private Address[] mFrom;
    private Address[] mTo;
    private Address[] mCc;
    private Address[] mBcc;
    private Address[] mReplyTo;

    private Address[] mReadReceiptTo;

    private Date mSentDate;
    private Body mBody;
    protected int mSize;
    private boolean mInhibitLocalMessageId = false;
    private String mImportance; // yk84.kim@samsung.com : 2011-09-21 :
                                // Importance function for IMAP
    private String mComments;

    // Shared random source for generating local message-id values
    private static final java.util.Random sRandom = new java.util.Random();

    // In MIME, en_US-like date format should be used. In other words "MMM"
    // should be encoded to
    // "Jan", not the other localized format like "Ene" (meaning January in
    // locale es).
    // This conversion is used when generating outgoing MIME messages. Incoming
    // MIME date
    // headers are parsed by org.apache.james.mime4j.field.DateTimeField which
    // does not have any
    // localization code.
    private static final String DATE_FORMAT_TMPELATE = "EEE, dd MMM yyyy HH:mm:ss Z";

    // regex that matches content id surrounded by "<>" optionally.
    private static final Pattern REMOVE_OPTIONAL_BRACKETS = Pattern.compile("^<?([^>]+)>?$");
    // regex that matches end of line.
    private static final Pattern END_OF_LINE = Pattern.compile("\r?\n");

    private boolean mCollectPart = false;

    public MimeMessage() {
        super();
        mHeader = null;
    }

    /**
     * Generate a local message id. This is only used when none has been
     * assigned, and is installed lazily. Any remote (typically server-assigned)
     * message id takes precedence.
     *
     * @return a long, locally-generated message-ID value
     */
    private String generateMessageId() {
        // jk0112.lee,100630,improve performance
        StringBuffer sb = new StringBuffer(60);
        // jk0112.lee,100630,improve performance
        sb.append('<');
        for (int i = 0; i < 24; i++) {
            // We'll use a 5-bit range (0..31)
            int value = sRandom.nextInt() & 31;
            char c = "0123456789abcdefghijklmnopqrstuv".charAt(value);
            sb.append(c);
        }
        // jk0112.lee,100630,improve performance
        sb.append('.');
        sb.append(Long.toString(System.currentTimeMillis()));
        sb.append("@email.android.com>");
        return sb.toString();
    }

    /**
     * Parse the given InputStream using Apache Mime4J to build a MimeMessage.
     *
     * @param in
     * @throws IOException
     * @throws MessagingException
     */
    public MimeMessage(InputStream in) throws IOException, MessagingException {
        super();
        parse(in);
    }

    protected void parse(InputStream in) throws IOException, MessagingException {
        // Before parsing the input stream, clear all local fields that may be
        // superceded by
        // the new incoming message.
        getMimeHeaders().clear();
        mInhibitLocalMessageId = true;
        mFrom = null;
        mTo = null;
        mCc = null;
        mBcc = null;
        mReplyTo = null;
        mSentDate = null;
        mBody = null;
        mImportance = null; // yk84.kim@samsung.com : 2011-09-21 : Importance
                            // function for IMAP

        //MimeStreamParser parser = new MimeStreamParser();
        /**
         *  If the TO field exceeds 5000 lines, mime4j parsing fails with MaxLineLenException.
         *  To overcome it, we set no maxLineLimt.
         */
        
        MimeConfig mimeConfig = new MimeConfig();
        mimeConfig.setMaxLineLen(-1);
        MimeStreamParser parser = new MimeStreamParser(mimeConfig);
        parser.setContentHandler(new MimeMessageBuilder());
        parser.setCollectPart(mCollectPart);

        try {
            // parser.parse(new EOLConvertingInputStream(in));
            parser.parse(in);
        } catch (MimeException e) {
            // throw new MessagingException(e.getMessage());
            // e.printStackTrace();
        }
    }

    /**
     * Return the internal mHeader value, with very lazy initialization. The
     * goal is to save memory by not creating the headers until needed.
     */
    private MimeHeader getMimeHeaders() {
        if (mHeader == null) {
            mHeader = new MimeHeader();
        }
        return mHeader;
    }

    @Override
    public Date getReceivedDate() throws MessagingException {
        try {
            String strReceived = MimeUtility.unfoldAndDecode(getFirstHeader("Received"));
            if (strReceived != null) {
                String strSplit[] = strReceived.split(";");
                if (strSplit != null && strSplit.length > 1) {
                    DateTimeField field = (DateTimeField) org.apache.james.mime4j.secfunc.Field
                            .parse("Date: " + strSplit[1]);
                    return field.getDate();
                }
                else {
                    String strSplit2[] = strReceived.split("\t");
                    if (strSplit2 != null && strSplit2.length > 2) {
                        EmailLog.d("Email", "timestamp strSplit[2]  " + strSplit2[2]);
                        DateTimeField field = (DateTimeField) org.apache.james.mime4j.secfunc.Field
                                .parse("Date: " + strSplit2[2]);
                        return field.getDate();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Date getSentDate() throws MessagingException {
        if (mSentDate == null) {
            try {
                DateTimeField field = (DateTimeField) org.apache.james.mime4j.secfunc.Field
                        .parse("Date: " + MimeUtility.unfoldAndDecode(getFirstHeader("Date")));
                mSentDate = field.getDate();
            } catch (Exception e) {

            }
        }
        return mSentDate;
    }

    @Override
    public void setSentDate(Date sentDate) throws MessagingException {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
                DATE_FORMAT_TMPELATE, Locale.US);
        setHeader("Date", DATE_FORMAT.format(sentDate));
        this.mSentDate = sentDate;
    }

    @Override
    public String getContentType() throws MessagingException {
        String contentType = getFirstHeader(MimeHeader.HEADER_CONTENT_TYPE);
        if (contentType == null) {
            return "text/plain";
        } else {
            return contentType;
        }
    }

    public String getDisposition() throws MessagingException {
        String contentDisposition = getFirstHeader(MimeHeader.HEADER_CONTENT_DISPOSITION);
        if (contentDisposition == null) {
            return null;
        } else {
            return contentDisposition;
        }
    }

    public String getContentId() throws MessagingException {
        String contentId = getFirstHeader(MimeHeader.HEADER_CONTENT_ID);
        if (contentId == null) {
            return null;
        } else {
            // remove optionally surrounding brackets.
            return REMOVE_OPTIONAL_BRACKETS.matcher(contentId).replaceAll("$1");
        }
    }

    public String getMimeType() throws MessagingException {
        return MimeUtility.getHeaderParameter(getContentType(), null);
    }

    public int getSize() throws MessagingException {
        return mSize;
    }

    // SVL Start: Hanifa
    // used for partial fetching of the mail during SYNC
    @Override
    public void setSize(int size) throws MessagingException {
        // TODO Auto-generated method stub
    }

    // sVL End
    /**
     * Returns a list of the given recipient type from this message. If no
     * addresses are found the method returns an empty array.
     */
    @Override
    public Address[] getRecipients(RecipientType type) throws MessagingException {
        if (type == RecipientType.TO) {
            if (mTo == null) {
                mTo = Address.parse(MimeUtility.unfold(getFirstHeader("To")));
            }
            return mTo;
        } else if (type == RecipientType.CC) {
            if (mCc == null) {
                mCc = Address.parse(MimeUtility.unfold(getFirstHeader("CC")));
            }
            return mCc;
        } else if (type == RecipientType.BCC) {
            if (mBcc == null) {
                mBcc = Address.parse(MimeUtility.unfold(getFirstHeader("BCC")));
            }
            return mBcc;
        } else {
            throw new MessagingException("Unrecognized recipient type.");
        }
    }

    // change@siso:A.JULKA RDN/Draft sync support start
    public String getMessageDispostion() throws MessagingException {
        String[] msgDispoTo = null;
        try {
            msgDispoTo = getHeader("Disposition-Notification-To");
        } catch (MessagingException e) {
            msgDispoTo = null;
        } catch (Exception e) {
            msgDispoTo = null;
        }
        if (msgDispoTo == null || msgDispoTo[0] == null)
            return null;

        return msgDispoTo[0].trim();
    }

    public String getReturnPath() throws MessagingException {
        String[] returnRcptTo = null;
        try {
            returnRcptTo = getHeader("Return-Receipt-To");
        } catch (MessagingException e) {
            returnRcptTo = null;
        } catch (Exception e) {
            returnRcptTo = null;
        }

        if (returnRcptTo == null || returnRcptTo[0] == null)
            return null;

        return returnRcptTo[0].trim();
    }

    // change@siso:A.JULKA RDN/Draft sync support end

    @Override
    public void setRecipients(RecipientType type, Address[] addresses) throws MessagingException {
        final int TO_LENGTH = 4; // "To: "
        final int CC_LENGTH = 4; // "Cc: "
        final int BCC_LENGTH = 5; // "Bcc: "
        if (type == RecipientType.TO) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("To");
                this.mTo = null;
            } else {
                setHeader("To", MimeUtility.fold(Address.toHeader(addresses), TO_LENGTH));
                this.mTo = addresses;
            }
        } else if (type == RecipientType.CC) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("CC");
                this.mCc = null;
            } else {
                setHeader("CC", MimeUtility.fold(Address.toHeader(addresses), CC_LENGTH));
                this.mCc = addresses;
            }
        } else if (type == RecipientType.BCC) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("BCC");
                this.mBcc = null;
            } else {
                setHeader("BCC", MimeUtility.fold(Address.toHeader(addresses), BCC_LENGTH));
                this.mBcc = addresses;
            }
        } else {
            throw new MessagingException("Unrecognized recipient type.");
        }
    }

    private String getCharsetNamefromContentType(String data) throws MessagingException {
        String tempcharset = null;
        String datalow = data.toLowerCase();
        int nPosCharset = datalow.indexOf("charset");
        String charsetParam[] = data.substring(nPosCharset, data.length()).split("=", 2);
        if (charsetParam.length < 2) {
            // NOTE Some strange servers sends ':' also
            charsetParam = data.split(":", 2);
            if (charsetParam.length == 2) {
                tempcharset = charsetParam[1].replaceAll("\"", "");
            }
        } else {
            tempcharset = charsetParam[1].replaceAll("\"", "");
        }
        if (tempcharset != null && tempcharset.contains(";")) {
            charsetParam = tempcharset.split(";");
            if (charsetParam.length >= 1) {
                tempcharset = charsetParam[0];
            }
        }
        return tempcharset;
    }

    /**
     * Returns the unfolded, decoded value of the Subject header.
     */
    @Override
    public String getSubject() throws MessagingException {
        try {
            String data = getFirstHeader(MimeHeader.HEADER_CONTENT_TYPE);
            if (data != null) {
                if (data.contains("charset") && !data.contains("MBP")) {
                    String tempcharset = getCharsetNamefromContentType(data);
                    String charset = null;
                    if (tempcharset != null) {
                        charset = CharsetUtil.toJavaCharset(tempcharset.trim());
                    }
                    EmailLog.d("Email", "getsubject:org charset : " + tempcharset + ",javacharset="
                            + charset);
                    if (charset != null) {
                        return MimeUtility.unfoldAndDecode2(getFirstHeader("Subject"), charset);
                    }
                }
            }
        } catch (Exception e) {
        }
        return MimeUtility.unfoldAndDecode(getFirstHeader("Subject"));
    }

    @Override
    public void setSubject(String subject) throws MessagingException {
        final int HEADER_NAME_LENGTH = 9; // "Subject: "
        setHeader("Subject", MimeUtility.foldAndEncode2(subject, HEADER_NAME_LENGTH));
    }

    @Override
    public Address[] getFrom() throws MessagingException {
        if (mFrom == null) {
            String list = MimeUtility.unfold(getFirstHeader("From"));
            if (list == null || list.length() == 0) {
                list = MimeUtility.unfold(getFirstHeader("Sender"));
            }
            mFrom = Address.parse(list);
        }
        return mFrom;
    }

    @Override
    public Address[] getFromSMS() throws MessagingException {
        if (mFrom == null) {
            String list = MimeUtility.unfold(getFirstHeader("From"));
            if (list == null || list.length() == 0) {
                list = MimeUtility.unfold(getFirstHeader("Sender"));
            }
            mFrom = Address.parseSMS(list);
        }
        return mFrom;
    }

    @Override
    public Address[] getToSMS() throws MessagingException {
        if (mTo == null) {
            mTo = Address.parseSMS(MimeUtility.unfold(getFirstHeader("To")));
        }
        return mTo;
    }


    @Override
    public Address[] getReplyToSMS() throws MessagingException {
        if (mReplyTo == null) {
            mReplyTo = Address.parseSMS(MimeUtility.unfold(getFirstHeader("Reply-to")));
        }
        return mReplyTo;
    }

    @Override
    public String getComments() throws MessagingException {
        if (mComments == null) {
            mComments = MimeUtility.unfold(getFirstHeader("Comments"));
        }
        return mComments;
    }

    // yk84.kim@samsung.com : 2011-09-21 : Importance function for IMAP: Begin
    @Override
    public String getImportance() throws MessagingException {
        if (mImportance == null) {
            mImportance = MimeUtility.unfold(getFirstHeader("Importance"));
        }
        return mImportance;
    }

    // yk84.kim@samsung.com : 2011-09-21 : Importance function for IMAP: End
    @Override
    public void setFrom(Address from) throws MessagingException {
        final int FROM_LENGTH = 6; // "From: "
        if (from != null) {
            setHeader("From", MimeUtility.fold(from.toHeader(), FROM_LENGTH));
            this.mFrom = new Address[] {
                from
            };
        } else {
            this.mFrom = null;
        }
    }

    @Override
    public Address[] getReplyTo() throws MessagingException {
        if (mReplyTo == null) {
            mReplyTo = Address.parse(MimeUtility.unfold(getFirstHeader("Reply-to")));
        }
        return mReplyTo;
    }

    @Override
    public void setReplyTo(Address[] replyTo) throws MessagingException {
        final int REPLY_TO_LENGTH = 10; // "Reply-to: "
        if (replyTo == null || replyTo.length == 0) {
            removeHeader("Reply-to");
            mReplyTo = null;
        } else {
            setHeader("Reply-to", MimeUtility.fold(Address.toHeader(replyTo), REPLY_TO_LENGTH));
            mReplyTo = replyTo;
        }
    }

    /**
     * Set the mime "Message-ID" header
     *
     * @param messageId the new Message-ID value
     * @throws MessagingException
     */
    @Override
    public void setMessageId(String messageId) throws MessagingException {
        setHeader("Message-ID", messageId);
    }

    // TODO: RDN

    //
    public Address[] getReadReceiptTo() throws MessagingException {
        if (mReadReceiptTo == null) {
            mReadReceiptTo = Address.parse(MimeUtility
                    .unfold(getFirstHeader("Disposition-Notification-To")));
        }
        return mReadReceiptTo;
    }

    /**
     * Get the mime "Message-ID" header. This value will be preloaded with a
     * locally-generated random ID, if the value has not previously been set.
     * Local generation can be inhibited/ overridden by explicitly clearing the
     * headers, removing the message-id header, etc.
     *
     * @return the Message-ID header string, or null if explicitly has been set
     *         to null
     */
    @Override
    public String getMessageId() throws MessagingException {
        String messageId = getFirstHeader("Message-ID");
        if (messageId == null && !mInhibitLocalMessageId) {
            messageId = generateMessageId();
            setMessageId(messageId);
        }
        return messageId;
    }

    @Override
    public void saveChanges() throws MessagingException {
        throw new MessagingException("saveChanges not yet implemented");
    }

    @Override
    public Body getBody() throws MessagingException {
        return mBody;
    }

    @Override
    public void setBody(Body body) throws MessagingException {
        this.mBody = body;
        if (body instanceof Multipart) {
            Multipart multipart = ((Multipart) body);
            multipart.setParent(this);
            setHeader(MimeHeader.HEADER_CONTENT_TYPE, multipart.getContentType());
            setHeader("MIME-Version", "1.0");
        } else if (body instanceof TextBody) {
            setHeader(MimeHeader.HEADER_CONTENT_TYPE,
                    String.format("%s;\n charset=utf-8", getMimeType()));
            setHeader(MimeHeader.HEADER_CONTENT_TRANSFER_ENCODING, "base64");
        }
    }

    protected String getFirstHeader(String name) throws MessagingException {
        return getMimeHeaders().getFirstHeader(name);
    }

    @Override
    public void addHeader(String name, String value) throws MessagingException {
        getMimeHeaders().addHeader(name, value);
    }

    @Override
    public void setHeader(String name, String value) throws MessagingException {
        getMimeHeaders().setHeader(name, value);
    }

    @Override
    public String[] getHeader(String name) throws MessagingException {
        return getMimeHeaders().getHeader(name);
    }

    @Override
    public String[] getHeaders() throws MessagingException {
        return getMimeHeaders().getHeaders();
    }

    @Override
    public void removeHeader(String name) throws MessagingException {
        getMimeHeaders().removeHeader(name);
        if ("Message-ID".equalsIgnoreCase(name)) {
            mInhibitLocalMessageId = true;
        }
    }

    /**
     * Set extended header
     *
     * @param name Extended header name
     * @param value header value - flattened by removing CR-NL if any remove
     *            header if value is null
     * @throws MessagingException
     */
    public void setExtendedHeader(String name, String value) throws MessagingException {
        if (value == null) {
            if (mExtendedHeader != null) {
                mExtendedHeader.removeHeader(name);
            }
            return;
        }
        if (mExtendedHeader == null) {
            mExtendedHeader = new MimeHeader();
        }
        mExtendedHeader.setHeader(name, END_OF_LINE.matcher(value).replaceAll(""));
    }

    /**
     * Get extended header
     *
     * @param name Extended header name
     * @return header value - null if header does not exist
     * @throws MessagingException
     */
    public String getExtendedHeader(String name) throws MessagingException {
        if (mExtendedHeader == null) {
            return null;
        }
        return mExtendedHeader.getFirstHeader(name);
    }

    /**
     * Set entire extended headers from String
     *
     * @param headers Extended header and its value - "CR-NL-separated pairs if
     *            null or empty, remove entire extended headers
     * @throws MessagingException
     */
    public void setExtendedHeaders(String headers) throws MessagingException {
        if (TextUtils.isEmpty(headers)) {
            mExtendedHeader = null;
        } else {
            mExtendedHeader = new MimeHeader();
            for (String header : END_OF_LINE.split(headers)) {
                String[] tokens = header.split(":", 2);
                if (tokens.length != 2) {
                    throw new MessagingException("Illegal extended headers: " + headers);
                }
                mExtendedHeader.setHeader(tokens[0].trim(), tokens[1].trim());
            }
        }
    }

    /**
     * Get entire extended headers as String
     *
     * @return "CR-NL-separated extended headers - null if extended header does
     *         not exist
     */
    public String getExtendedHeaders() {
        if (mExtendedHeader != null) {
            return mExtendedHeader.writeToString();
        }
        return null;
    }

    /**
     * Write message header and body to output stream
     *
     * @param out Output steam to write message header and body.
     */
    public void writeTo(OutputStream out) throws IOException, MessagingException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out), 1024);
        // Force creation of local message-id
        getMessageId();
        getMimeHeaders().writeTo(out);
        // mExtendedHeader will not be write out to external output stream,
        // because it is intended to internal use.
        writer.write("\r\n");
        writer.flush();
        if (mBody != null) {
            mBody.writeTo(out);
        }
    }

    // Snc Porting
    // gunheng.lee 100809 append message including attachments
    public void writeTo(Context context, long messageId, OutputStream out) throws IOException,
            MessagingException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out), 1024);
        // Force creation of local message-id
        getMessageId();
        getMimeHeaders().writeTo(out);
        // mExtendedHeader will not be write out to external output stream,
        // because it is intended to internal use.
        writer.write("\r\n");
        writer.flush();
        if (mBody != null) {
            mBody.writeTo(context, messageId, out);
        }
    }

    public InputStream getInputStream() throws MessagingException {
        return null;
    }

    public void setCollectPart(boolean colPart) {
        mCollectPart = colPart;
    }

    class MimeMessageBuilder implements ContentHandler {
        private Stack<Object> stack = new Stack<Object>();

        public MimeMessageBuilder() {
        }

        /*
         * for display various languages(eq. Korean), 091015 fidelis.lee samsung
         * print string's each character to hex
         */
/*        private void printHex(String tag, String str) {
            *//*
             * //upgrade to HC3.1 if (EmailLog.LOGD) { System.out.println("Email "
             * + tag + " print hex --------"); try { System.out.print("Email " +
             * " |"); for(byte c : str.getBytes())
             * System.out.print(String.format("\\x%02x", c));
             * System.out.print("|\n"); } catch(Exception e) { Log.d("Email",
             * "print hex MimeException"); } }
             *//*
        }*/

        /*
         * for display various languages(eq. Korean), 091015 fidelis.lee samsung
         * If server's information doesn't involve encoding data, there is
         * problem to display in google widgets. So change string to utf-8.
         */
        private String convert2utf8(String str, String charset) {
            byte[] b;
            String after = null;

            try {
                b = str.getBytes("8859_1");
                CharsetDecoder decoder = Charset.forName(charset).newDecoder();
                try {
                    CharBuffer r = decoder.decode(ByteBuffer.wrap(b));
                    after = r.toString();
                    // Log.d(Log.LOG_TAG, str);
                    // printHex("convert2utf8 normal", after);
                    return after;
                } catch (CharacterCodingException e) {
                    after = new String(b, "UTF-8");
                    // Log.d(Log.LOG_TAG, str);
                    // printHex("convert2utf8 euc-kr", after);
                    return after;
                }
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            return after;
        }

        private void expect(Class<?> c) throws MimeException {
            if (!c.isInstance(stack.peek())) {
                throw new /* IllegalStateException */MimeException("Internal stack MimeException: "
                        + "Expected '" + c.getName() + "' found '"
                        + stack.peek().getClass().getName() + "'");
            }
        }

        public void startMessage() throws MimeException {
            if (stack.isEmpty()) {
                stack.push(MimeMessage.this);
            } else {
                expect(Part.class);
                try {
                    MimeMessage m = new MimeMessage();
                    ((Part) stack.peek()).setBody(m);
                    stack.push(m);
                } catch (MessagingException me) {
                    throw new MimeException(me);
                }
            }
        }

        public void endMessage() throws MimeException {
            // upgrade to HC3.1 if (EmailLog.LOGD) Log.e("endMessage",
            // "--- start");
            expect(MimeMessage.class);
            Part p = (Part) stack.pop();
            if (p instanceof Message) {
                try {
                    if (p.getHeader("Content-Type") == null)
                        return;
                    String data = p.getHeader("Content-Type")[0];
                    if (data != null) {
                        String datalow = data.toLowerCase();
                        EmailLog.d("Email", "content type : " + data);
                        if (datalow != null) {
                            if (datalow.contains("charset") && !data.contains("MBP")) {// MBP
                                                                                       // means
                                                                                       // Modified
                                                                                       // By
                                                                                       // header
                                                                                       // Postprocessing

                                String tempcharset = getCharsetNamefromContentType(data);
                                String charset = null;
                                if (tempcharset != null) {
                                    charset = CharsetUtil.toJavaCharset(tempcharset.trim());
                                }
                                EmailLog.d("Email", "endmessage:org charset : " + tempcharset
                                        + ",javacharset=" + charset);
                                if (charset == null)
                                    charset = tempcharset;
                                /*
                                 * String sub[] = p.getHeader("Subject"); if
                                 * (sub != null && charset != null) {
                                 * printHex("sub B", sub[0]); if
                                 * (!sub[0].contains(charset)) {
                                 * p.setHeader("Subject", convert2utf8(sub[0],
                                 * charset)); } }
                                 */

                                String from[] = p.getHeader("From");
                                /*
                                 * if(from != null) { for(String s : from) {
                                 * //== Resolve memory defect by RSAR:
                                 * yonghee2.lee 20100504 samsung StringBuffer
                                 * sbuf= new StringBuffer(); Log.d("Email",
                                 * sbuf.
                                 * append("from B : ").append(s).toString()); }
                                 * }
                                 */
                                if (from != null && charset != null) {
                                    // printHex("from B", from[0]);
                                    if (!from[0].contains(charset)) {
                                        p.setHeader("From", convert2utf8(from[0], charset));
                                    }
                                }
                                /*
                                 * if(p.getHeader("From") != null) { for(String
                                 * s : p.getHeader("From")) { //== Resolve
                                 * memory defect by RSAR: yonghee2.lee 20100504
                                 * samsung StringBuffer sbuf= new
                                 * StringBuffer(); Log.d("Email",
                                 * sbuf.append("from A : "
                                 * ).append(s).toString()); } }
                                 */

                                String to[] = p.getHeader("To");
                                /*
                                 * if (to != null) { for (String s : to) {
                                 * StringBuffer sbuf = new StringBuffer();
                                 * Log.d("Email",
                                 * sbuf.append("to B : ").append(s).toString());
                                 * } }
                                 */
                                if (to != null && charset != null) {
                                    // printHex("to B", to[0]);
                                    if (!to[0].contains(charset)) {
                                        p.setHeader("To", convert2utf8(to[0], charset));
                                    }
                                }
                                /*
                                 * if (p.getHeader("To") != null) { for (String
                                 * s : p.getHeader("To")) { StringBuffer sbuf =
                                 * new StringBuffer(); Log.d("Email",
                                 * sbuf.append("To A : ").append(s).toString());
                                 * } }
                                 */

                                String cc[] = p.getHeader("CC");
                                /*
                                 * if (cc != null) { for (String s : cc) {
                                 * StringBuffer sbuf = new StringBuffer();
                                 * Log.d("Email",
                                 * sbuf.append("CC B : ").append(s).toString());
                                 * } }
                                 */
                                if (cc != null && charset != null) {
                                    // printHex("CC B", cc[0]);
                                    if (!cc[0].contains(charset)) {
                                        p.setHeader("CC", convert2utf8(cc[0], charset));
                                    }
                                }
                                /*
                                 * if (p.getHeader("CC") != null) { for (String
                                 * s : p.getHeader("CC")) { StringBuffer sbuf =
                                 * new StringBuffer(); Log.d("Email",
                                 * sbuf.append("CC A : ").append(s).toString());
                                 * } }
                                 */
                            } else { // For case that has no charset and no
                                     // encoded-word (20100805 moojin.im)
                                /*
                                 * String sub[] = p.getHeader("Subject"); if
                                 * (sub != null) { if (!sub[0].contains("=?")) {
                                 * // no encode word String charset =
                                 * DecoderUtil.chardet(sub[0]); if (charset !=
                                 * null) p.setHeader("Subject",
                                 * convert2utf8(sub[0], charset)); //
                                 * p.setHeader("Subject", //
                                 * convert2utf8(sub[0], //
                                 * CharsetUtil.getLocalCharset())); } }
                                 */

                                String from[] = p.getHeader("From");
                                if (from != null) {
                                    if (!from[0].contains("=?")) { // no encode
                                                                   // word
                                        String charset = DecoderUtil.chardet(from[0]);
                                        if (charset != null)
                                            p.setHeader("From", convert2utf8(from[0], charset));
                                        // p.setHeader("From",
                                        // convert2utf8(from[0],
                                        // CharsetUtil.getLocalCharset()));
                                    }
                                }

                                String to[] = p.getHeader("To");
                                if (to != null) {
                                    if (!to[0].contains("=?")) { // no encode
                                                                 // word
                                        String charset = DecoderUtil.chardet(to[0]);
                                        if (charset != null)
                                            p.setHeader("To", convert2utf8(to[0], charset));
                                    }
                                }

                                String cc[] = p.getHeader("CC");
                                if (cc != null) {
                                    if (!cc[0].contains("=?")) { // no encode
                                                                 // word
                                        String charset = DecoderUtil.chardet(cc[0]);
                                        if (charset != null)
                                            p.setHeader("CC", convert2utf8(cc[0], charset));
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // upgrade to HC3.1 if (EmailLog.LOGD) {
            // upgrade to HC3.1 Log.e("endMessage", "--- pop " + p);
            // upgrade to HC3.1 }
        }

        public void startHeader() throws MimeException {
            expect(Part.class);
        }

        /*
         * public void field(String fieldData) { expect(Part.class); try {
         * String[] tokens = fieldData.split(":", 2); ((Part)
         * stack.peek()).addHeader(tokens[0], tokens[1].trim()); } catch
         * (MessagingException me) { throw new Error(me); } }
         */

        public void endHeader() throws MimeException {
            expect(Part.class);
            Part p = (Part) stack.peek();
            if (p instanceof Message) {
                try {
                    String[] headerData = p.getHeader("Content-Type");
                    if (headerData == null)
                        return;
                    String data = headerData[0];
                    if (data != null && data.contains("charset")) {
                        String tempcharset = getCharsetNamefromContentType(data);
                        String charset = null;
                        if (tempcharset != null) {
                            charset = CharsetUtil.toJavaCharset(tempcharset.trim());
                        }
                        EmailLog.d("Email", "endheder:org charset : " + tempcharset + ",javacharset="
                                + charset);
                        if (charset == null && tempcharset != null) {
                            data = data.replace("charset", "MBP; charset");
                            data = data.replace(tempcharset, "UTF-8");
                            p.setHeader("Content-Type", data);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                }
            }
        }

        @Override
        public void startMultipart(BodyDescriptor bd) throws MimeException {
            expect(Part.class);

            Part e = (Part) stack.peek();
            try {
                MimeMultipart multiPart = new MimeMultipart(e.getContentType());
                e.setBody(multiPart);
                stack.push(multiPart);
            } catch (MessagingException me) {
                // throw new MimeException(me); just fallthrough?
            }
        }

        public void body(BodyDescriptor bd, InputStream in) throws IOException, MimeException {
            expect(Part.class);
            Body body = MimeUtility.decodeBody(in, bd.getTransferEncoding());
            try {
                ((Part) stack.peek()).setBody(body);
            } catch (MessagingException me) {
                throw new MimeException(me);
            }
        }

        public void body(InputStream in) throws IOException, MimeException {
            expect(MimeMultipart.class);
            BinaryTempFileBody tempBody = new BinaryTempFileBody();
            OutputStream out = tempBody.getOutputStream();
            try {
                int numCopied = IOUtils.copy(in, out);
                setSize(numCopied);
            }
            catch (Exception bde)
            {
            } finally {
                out.close();
            }
            try {
                ((MimeMultipart)stack.peek()).setBody(tempBody);
            } catch (MessagingException me) {
                throw new MimeException(me);
            }
        }

        public void endMultipart() throws MimeException {
            stack.pop();
        }

        public void startBodyPart() throws MimeException {
            expect(MimeMultipart.class);

            try {
                MimeBodyPart bodyPart = new MimeBodyPart();
                ((MimeMultipart) stack.peek()).addBodyPart(bodyPart);
                stack.push(bodyPart);
            } catch (MessagingException me) {
                throw new MimeException(me);
            }
        }

        public void endBodyPart() throws MimeException {
            expect(BodyPart.class);
            stack.pop();
        }
        @Override
        public void epilogue(InputStream is) throws IOException, MimeException {
            expect(MimeMultipart.class);
//            StringBuffer sb = new StringBuffer();
//            int b;
//            while ((b = is.read()) != -1) {
//              sb.append((char) b);
//            }
            // ((Multipart) stack.peek()).setEpilogue(sb.toString());
        }

        public void preamble(InputStream is) throws IOException, MimeException {
            expect(MimeMultipart.class);
            StringBuffer sb = new StringBuffer();
            int b;
            try {
                while ((b = is.read()) != -1) {
                    sb.append((char) b);
                }
            } catch (OutOfMemoryError e) {
                throw new MimeException(e);
            }
            try {
                ((MimeMultipart) stack.peek()).setPreamble(sb.toString());
            } catch (MessagingException me) {
                throw new MimeException(me);
            }
        }
        @Override
        public void raw(InputStream is) throws IOException, MimeException {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public void field(Field rawField) throws MimeException {
            expect(Part.class);
            try {
                String[] tokens = rawField.toString().split(":", 2);
                ((Part) stack.peek()).addHeader(tokens[0], tokens[1].trim());
            } catch (MessagingException me) {
                throw new MimeException(me);
            }
        }
    }
}
