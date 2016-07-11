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

import com.samsung.android.emailcommon.mail.BodyPart;
import com.samsung.android.emailcommon.mail.MessagingException;
import com.samsung.android.emailcommon.mail.Multipart;
import com.samsung.android.emailcommon.provider.EmailContent.Attachment;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.codec.EncoderUtil;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import com.samsung.android.emailcommon.mail.Body;

public class MimeMultipart extends Multipart {
    
    protected String mPreamble;

    protected String mBoundary;

    protected String mSubType;
    
    protected Body mBody = null;

    public MimeMultipart() throws MessagingException {
        super();
        mBoundary = generateBoundary();
        setSubType("mixed");
    }

    public MimeMultipart(String contentType) throws MessagingException {
        super();
        this.mContentType = contentType;
        try {
            mSubType = MimeUtility.getHeaderParameter(contentType, null).split("/")[1];
            mBoundary = MimeUtility.getHeaderParameter(contentType, "boundary");
            if (mBoundary == null) {
                throw new MessagingException("MultiPart does not contain boundary: " + contentType);
            }
        } catch (Exception e) {
            throw new MessagingException(
                    "Invalid MultiPart Content-Type; must contain subtype and boundary. ("
                            + contentType + ")", e);
        }
    }

    public String generateBoundary() {
        StringBuffer sb = new StringBuffer();
        sb.append("----");
        for (int i = 0; i < 30; i++) {
            sb.append(Integer.toString((int) (Math.random() * 35), 36));
        }
        return sb.toString().toUpperCase();
    }

    public String getPreamble() throws MessagingException {
        return mPreamble;
    }

    public void setPreamble(String preamble) throws MessagingException {
        this.mPreamble = preamble;
    }

    @Override
    public String getContentType() throws MessagingException {
        return mContentType;
    }

    public void setSubType(String subType) throws MessagingException {
        this.mSubType = subType;
        mContentType = String.format("multipart/%s; boundary=\"%s\"", subType, mBoundary);
    }

    public void writeTo(OutputStream out) throws IOException, MessagingException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out), 1024);

        if (mPreamble != null) {
            writer.write(mPreamble + "\r\n");
        }

        for (int i = 0, count = mParts.size(); i < count; i++) {
            BodyPart bodyPart = (BodyPart) mParts.get(i);
            writer.write("--" + mBoundary + "\r\n");
            writer.flush();
            bodyPart.writeTo(out);
            writer.write("\r\n");
        }

        writer.write("--" + mBoundary + "--\r\n");
        writer.flush();
    }

    // gunheng.lee 100809 append message including attachments
    public void writeTo(Context context, long messageId, OutputStream out) throws IOException,
            MessagingException {
        // OutputStreamWriter stream = new OutputStreamWriter(out);
        // BufferedWriter writer = new BufferedWriter(stream, 1024);
        OutputStream stream = new BufferedOutputStream(out, 1024);
        Writer writer = new OutputStreamWriter(stream);

        if (mPreamble != null) {
            writer.write(mPreamble + "\r\n");
        }

        for (int i = 0, count = mParts.size(); i < count; i++) {
            BodyPart bodyPart = (BodyPart) mParts.get(i);
            writer.write("--" + mBoundary + "\r\n");
            writer.flush();

            bodyPart.writeTo(out);

            writer.write("\r\n");
        }

        Uri uri = ContentUris.withAppendedId(Attachment.MESSAGE_ID_URI, messageId);
        Cursor attachmentsCursor = context.getContentResolver().query(uri,
                Attachment.CONTENT_PROJECTION, null, null, null);

        try {
            int attachmentCount = attachmentsCursor.getCount();

            if (attachmentCount > 0) {
                // Move to the first attachment; this must succeed because
                // multipart is true
                if (attachmentsCursor.moveToFirst()) {
                    // Write out the attachments until we run out
                    do {
                        writer.write("--" + mBoundary + "\r\n");
                        writer.flush();

                        Attachment attachment = Attachment.getContent(attachmentsCursor,
                                Attachment.class);
                        writeOneAttachment(context, writer, stream, attachment);

                        writer.write("\r\n");

                    } while (attachmentsCursor.moveToNext());
                }
            }
        } finally {
            attachmentsCursor.close();
        }

        writer.write("--" + mBoundary + "--\r\n");
        writer.flush();
        // out.flush();
    }

    /**
     * Write a single header with no wrapping or encoding
     * 
     * @param writer the output writer
     * @param name the header name
     * @param value the header value
     * @history gunheng.lee 100809 append message including attachments
     */
    private static void writeHeader(Writer writer, String name, String value) throws IOException {
        if (value != null && value.length() > 0) {
            writer.append(name);
            writer.append(": ");
            writer.append(value);
            writer.append("\r\n");
        }
    }

    /**
     * Write a single attachment and its payload
     * 
     * @history gunheng.lee 100809 append message including attachments
     */
    private void writeOneAttachment(Context context, Writer writer, OutputStream out,
            Attachment attachment) throws IOException, MessagingException {
        // adjust archer source to resolve Korea language encoding.
        // hanshik.jung, 2010.05.02
        // jh.rhim solved email > attach > gallery , received mail has no name
        // attached image problem

        Boolean ff = true;
        try {
            ff = EncoderUtil.hasToBeEncoded(attachment.mFileName, 0);
        } catch(IllegalArgumentException iae) {
            ff = false;
        }

        if (ff == true) {
            String encFileName = EncoderUtil.encodeAddressDisplayName(attachment.mFileName);
            writeHeader(writer, "Content-Type", attachment.mMimeType + ";\n name=\"" + encFileName
                    + "\"");
            writeHeader(writer, "Content-Transfer-Encoding", "base64");
            // Most attachments (real files) will send Content-Disposition. The
            // suppression option
            // is used when sending calendar invites.
            if ((attachment.mFlags & Attachment.FLAG_ICS_ALTERNATIVE_PART) == 0) {
                if (attachment.mIsInline == Attachment.IS_INLINE_ATTACHMENT) {
                    writeHeader(writer, "Content-Disposition", "inline;" + "\n filename*=\""
                            + encFileName + "\";" + "\n size=" + Long.toString(attachment.mSize));
                } else {
                    writeHeader(writer, "Content-Disposition", "attachment;" + "\n filename*=\""
                            + encFileName + "\";" + "\n size=" + Long.toString(attachment.mSize));
                }
            }
        } else {
            writeHeader(writer, "Content-Type", attachment.mMimeType + ";\n name=\""
                    + attachment.mFileName + "\"");
            writeHeader(writer, "Content-Transfer-Encoding", "base64");
            // Most attachments (real files) will send Content-Disposition. The
            // suppression option
            // is used when sending calendar invites.
            if ((attachment.mFlags & Attachment.FLAG_ICS_ALTERNATIVE_PART) == 0) {
                if (attachment.mIsInline == Attachment.IS_INLINE_ATTACHMENT) {
                    writeHeader(writer, "Content-Disposition",
                            "inline;" + "\n filename=\"" + attachment.mFileName + "\";"
                                    + "\n size=" + Long.toString(attachment.mSize));
                } else {
                    writeHeader(writer, "Content-Disposition",
                            "attachment;" + "\n filename=\"" + attachment.mFileName + "\";"
                                    + "\n size=" + Long.toString(attachment.mSize));
                }
            }
        }

        writeHeader(writer, "Content-ID", attachment.mContentId);
        writer.append("\r\n");

        // Set up input stream and write it out via base64
        InputStream inStream = null;
        Base64OutputStream base64Out = null;// sec.email tom.jung For RSAR 2nd
        try {
            // Use content, if provided; otherwise, use the contentUri
            if (attachment.mContentBytes != null) {
                inStream = new ByteArrayInputStream(attachment.mContentBytes);
            } else {
                // try to open the file
                if (attachment.mContentUri != null) {
                    Uri fileUri = Uri.parse(attachment.mContentUri);
                    inStream = context.getContentResolver().openInputStream(fileUri);
                }
            }

            if (inStream == null) {
                throw new MessagingException("inStream is null.");
            }
            
            // switch to output stream for base64 text output
            writer.flush();
            base64Out = new Base64OutputStream(// sec.email tom.jung For RSAR
                                               // 2nd
                    out, Base64.CRLF | Base64.NO_CLOSE);
            // copy base64 data and close up
            IOUtils.copy(inStream, base64Out);
            // base64Out.close();//sec.email tom.jung For RSAR 2nd

            // The old Base64OutputStream wrote an extra CRLF after
            // the output. It's not required by the base-64 spec; not
            // sure if it's required by RFC 822 or not.
            out.write('\r');
            out.write('\n');
            out.flush();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            // Ignore this - empty file is OK
        } catch (IOException ioe) {
            throw new MessagingException("Invalid attachment.", ioe);
        } finally {// sec.email tom.jung For RSAR 2nd
            if (base64Out != null)
                base64Out.close();
            if (inStream != null)
                inStream.close();
        }
    }

    public InputStream getInputStream() throws MessagingException {
    	if(null != mBody){
    		return mBody.getInputStream();
    	}
        return null;
    }

    public String getSubTypeForTest() {
        return mSubType;
    }
    
    public void setBody(Body body) throws MessagingException {
        this.mBody = body;
    }    
}
