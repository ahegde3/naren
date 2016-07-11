package com.samsung.android.emailcommon.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;


public class MediaFileMini {

    private static HashMap<String, String> sFileTypeMap = new HashMap<String, String>();

    static void addFileType(String extension, String mimeType) {
        sFileTypeMap.put(extension, mimeType);
    }

    public static boolean isSCCFile(String path) throws IOException, FileNotFoundException {
        File sccFile = new File(path);
        FileInputStream fis = null; // new FileInputStream(sccFile);
        byte[] pklenBuf = new byte[4];
        byte[] mimetypeBuf = new byte[128];

        try {
            fis = new FileInputStream(sccFile);

            long skipped = fis.skip(22);

            if (skipped < 22) return false;

            int ret = fis.read(pklenBuf, 0, 4);

            if (ret <= 0) return false;

            int pklen = byteToInt(pklenBuf, ByteOrder.LITTLE_ENDIAN);
            skipped = fis.skip(12);
            if (skipped < 12) return false;

            ret = fis.read(mimetypeBuf, 0, pklen);
            if (ret <= 0) return false;

            String result = new String(mimetypeBuf);
            if (result.contains("application/vnd.samsung.scc")) return true;
            else return false;
        } catch (FileNotFoundException e) {

            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {

            throw new IOException(e.getMessage());
        } finally {
            if (fis != null)
                fis.close();
            fis = null;
        }

    }

    public static String getMimetypeFromSCCFile(String path) throws IOException,
            FileNotFoundException {
        File sccFile = new File(path);
        FileInputStream fis = null;
        byte[] pklenBuf = new byte[4];
        byte[] mimetypeBuf = new byte[128];

        try {
            fis = new FileInputStream(sccFile);
            long skipped = fis.skip(22);

            if (skipped < 22) return "ERR_NOT_SCC_FILE";

            int read = fis.read(pklenBuf, 0, 4);

            if (read <= 0) return "ERR_NOT_SCC_FILE";

            int pklen = byteToInt(pklenBuf, ByteOrder.LITTLE_ENDIAN);
            skipped = fis.skip(12);
            if (skipped < 12) return "ERR_NOT_SCC_FILE";

            read = fis.read(mimetypeBuf, 0, pklen);
            if (read <= 0) return "ERR_NOT_SCC_FILE";

            String result = new String(mimetypeBuf);
            fis.close();
            if (!result.contains("application/vnd.samsung.scc")) {
                return "ERR_NOT_SCC_FILE";
            }
            return result.trim();
        } catch (FileNotFoundException e) {

            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            if (fis != null)
                fis.close();

            fis = null;
        }
    }

    private static int byteToInt(byte[] bytes, ByteOrder order) {
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.order(order);
        buff.put(bytes);
        buff.flip();
        return buff.getInt();
    }

    static {
        addFileType("EML", "message/rfc822");
        addFileType("MP3", "audio/mpeg");
        addFileType("M4A", "audio/mp4");
        addFileType("WAV", "audio/x-wav");
        addFileType("AMR", "audio/amr");
        addFileType("AWB", "audio/amr-wb");
        addFileType("WMA", "audio/x-ms-wma");
        addFileType("OGG", "audio/ogg");
        addFileType("OGA", "audio/ogg");
        addFileType("AAC", "audio/aac");

        addFileType("3GA", "audio/3gpp");
        addFileType("FLAC", "audio/flac");
        addFileType("MPGA", "audio/mpeg");
        addFileType("MP4_A", "audio/mp4");
        addFileType("3GP_A", "audio/3gpp");
        addFileType("3G2_A", "audio/3gpp2");
        addFileType("ASF_A", "audio/x-ms-asf");
        addFileType("3GPP_A", "audio/3gpp");
        addFileType("MID", "audio/midi");
        addFileType("XMF", "audio/midi");

        addFileType("MXMF", "audio/midi");
        addFileType("RTTTL", "audio/midi");
        addFileType("SMF", "audio/sp-midi");
        addFileType("IMY", "audio/imelody");
        addFileType("MIDI", "audio/midi");
        addFileType("RTX", "audio/midi");
        addFileType("OTA", "audio/midi");
        addFileType("PYA", "audio/vnd.ms-playready.media.pya");
        addFileType("QCP", "audio/qcelp");
        addFileType("MPEG", "video/mpeg");

        addFileType("MPG", "video/mpeg");
        addFileType("MP4", "video/mp4");
        addFileType("M4V", "video/mp4");
        addFileType("3GP", "video/3gpp");
        addFileType("3GPP", "video/3gpp");
        addFileType("3G2", "video/3gpp2");
        addFileType("3GPP2", "video/3gpp2");
        addFileType("WMV", "video/x-ms-wmv");
        addFileType("ASF", "video/x-ms-asf");
        addFileType("AVI", "video/avi");

        addFileType("DIVX", "video/divx");
        addFileType("FLV", "video/flv");
        addFileType("MKV", "video/mkv");
        addFileType("SDP", "application/sdp");
        addFileType("MOV", "video/quicktime");
        addFileType("PYV", "video/vnd.ms-playready.media.pyv");
        addFileType("WEBM", "video/webm");
        addFileType("JPG", "image/jpeg");
        addFileType("JPEG", "image/jpeg");
        addFileType("MY5", "image/vnd.tmo.my5");

        addFileType("GIF", "image/gif");
        addFileType("PNG", "image/png");
        addFileType("BMP", "image/x-ms-bmp");
        addFileType("WBMP", "image/vnd.wap.wbmp");
        addFileType("M3U", "audio/x-mpegurl");
        addFileType("PLS", "audio/x-scpls");
        addFileType("WPL", "application/vnd.ms-wpl");
        addFileType("PDF", "application/pdf");
        addFileType("RTF", "application/msword");
        addFileType("DOC", "application/msword");
        addFileType("GOLF", "image/*");

        addFileType("DOCX", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileType("DOT", "application/msword");
        addFileType("DOTX", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        addFileType("CSV", "text/comma-separated-values");
        addFileType("XLS", "application/vnd.ms-excel");
        addFileType("XLSX", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        addFileType("XLTX", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        addFileType("PPS", "application/vnd.ms-powerpoint");
        addFileType("PPT", "application/vnd.ms-powerpoint");

        addFileType("PPTX", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        addFileType("POT", "application/vnd.ms-powerpoint");
        addFileType("POTX", "application/vnd.openxmlformats-officedocument.presentationml.template");
        addFileType("ASC", "text/plain");
        addFileType("TXT", "text/plain");
        addFileType("GUL", "application/jungumword");
        addFileType("EPUB", "application/epub+zip");
        addFileType("ACSM", "application/vnd.adobe.adept+xml");
        addFileType("SWF", "application/x-shockwave-flash");
        addFileType("SVG", "image/svg+xml");

        addFileType("DCF", "application/vnd.oma.drm.content");
        addFileType("ODF", "application/vnd.oma.drm.content");
        addFileType("APK", "application/apk");
        addFileType("JAD", "text/vnd.sun.j2me.app-descriptor");
        addFileType("JAR", "application/java-archive ");
        addFileType("VCS", "text/x-vCalendar");
        addFileType("ICS", "text/x-vCalendar");
        addFileType("VTS", "text/x-vtodo");
        addFileType("VCF", "text/x-vcard");
        addFileType("VNT", "text/x-vnote");

        addFileType("HTML", "text/html");
        addFileType("HTM", "text/html");
        addFileType("XHTML", "text/html");
        addFileType("XML", "application/xhtml+xml");
        addFileType("MHT", "multipart/related");
        addFileType("MHTM", "multipart/related");
        addFileType("MHTML", "multipart/related");
        addFileType("WGT", "application/vnd.samsung.widget");
        addFileType("HWP", "application/x-hwp");
        addFileType("ZIP", "application/zip");
        addFileType("SNB", "application/snb");
        addFileType("SSF", "application/ssf");
        addFileType("WEBP", "image/webp");
        addFileType("SNBKP", "application/octet-stream");
        addFileType("SMBKP", "application/octet-stream");
        addFileType("SPD", "application/spd");
        addFileType("SCC", "application/scc");

        addFileType("PFX", "application/x-pkcs12");
        addFileType("P12", "application/x-pkcs12");
    }

    public static String getMimeTypeForView(String extension) {

        if (extension == null)
            return null;

        String mediaType = sFileTypeMap.get(extension.toUpperCase());

        if (mediaType == null)
            mediaType = "application/" + extension;

        return mediaType;
    }
}
