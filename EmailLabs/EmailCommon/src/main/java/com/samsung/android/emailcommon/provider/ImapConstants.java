package com.samsung.android.emailcommon.provider;

import com.samsung.android.emailcommon.Preferences;

public final class ImapConstants {
    private ImapConstants() {
    }

    public static final String FETCH_FIELD_BODY_PEEK_BARE = "BODY.PEEK";
    public static final String FETCH_FIELD_BODY_PEEK = FETCH_FIELD_BODY_PEEK_BARE + "[]";
    public static final String FETCH_FIELD_BODY_PEEK_SANE = String.format("BODY.PEEK[]<0.%d>",
            Preferences.MAX_SMALL_MESSAGE_SIZE);

    // TODO: RDN public static final String FETCH_FIELD_HEADERS =
    public static final String FETCH_FIELD_HEADERS = "BODY.PEEK[HEADER.FIELDS (date subject x-protective-marking from content-type to cc reply-to message-id disposition-notification-to x-priority importance)]";

    public static final int FETCH_BODY_SANE_SUGGESTED_SIZE = Preferences.MAX_SMALL_MESSAGE_SIZE;
    public static final String FETCH_FIELD_PART_SANE = String.format("<0.%d>",
            FETCH_BODY_SANE_SUGGESTED_SIZE);

    public static final String ALERT = "ALERT";
    public static final String APPEND = "APPEND";
    public static final String BAD = "BAD";
    public static final String BADCHARSET = "BADCHARSET";
    public static final String BODY = "BODY";
    public static final String BODY_BRACKET_HEADER = "BODY[HEADER";
    public static final String BODYSTRUCTURE = "BODYSTRUCTURE";
    public static final String BYE = "BYE";
    public static final String CAPABILITY = "CAPABILITY";
    public static final String CHECK = "CHECK";
    public static final String CLOSE = "CLOSE";
    public static final String COPY = "COPY";
    public static final String CREATE = "CREATE";
    public static final String DELETE = "DELETE";
    public static final String EXAMINE = "EXAMINE";
    public static final String EXISTS = "EXISTS";
    public static final String EXPUNGE = "EXPUNGE";
    public static final String FETCH = "FETCH";
    public static final String FLAG_ANSWERED = "\\ANSWERED";
    public static final String FLAG_DELETED = "\\DELETED";
    public static final String FLAG_FLAGGED = "\\FLAGGED";
    public static final String FLAG_FORWARDED = "$Forwarded";
    public static final String FLAG_NO_SELECT = "\\NOSELECT";
    public static final String FLAG_SEEN = "\\SEEN";
    public static final String FLAGS = "FLAGS";
    public static final String FLAGS_SILENT = "FLAGS.SILENT";
    public static final String ID = "ID";
    public static final String INBOX = "INBOX";
    public static final String INTERNALDATE = "INTERNALDATE";
    public static final String LIST = "LIST";
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String LSUB = "LSUB";
    public static final String NAMESPACE = "NAMESPACE";
    public static final String NO = "NO";
    public static final String NOOP = "NOOP";
    public static final String OK = "OK";
    public static final String PARSE = "PARSE";
    public static final String PERMANENTFLAGS = "PERMANENTFLAGS";
    public static final String PREAUTH = "PREAUTH";
    public static final String READ_ONLY = "READ-ONLY";
    public static final String READ_WRITE = "READ-WRITE";
    public static final String RENAME = "RENAME";
    public static final String RFC822_SIZE = "RFC822.SIZE";
    public static final String SEARCH = "SEARCH";
    public static final String SELECT = "SELECT";
    public static final String STARTTLS = "STARTTLS";
    public static final String STATUS = "STATUS";
    public static final String STORE = "STORE";
    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String TEXT = "TEXT";
    public static final String TRYCREATE = "TRYCREATE";
    public static final String UID = "UID";
    public static final String UID_COPY = "UID COPY";
    public static final String UID_FETCH = "UID FETCH";
    public static final String UID_SEARCH = "UID SEARCH";
    public static final String UID_STORE = "UID STORE";
    public static final String UIDNEXT = "UIDNEXT";
    public static final String UIDVALIDITY = "UIDVALIDITY";
    public static final String UNSEEN = "UNSEEN";
    public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
    public static final String APPENDUID = "APPENDUID";
    public static final String NIL = "NIL";
    public static final String XLIST = "XLIST";
    public static final String XLIST_INBOX = "\\Inbox";
    public static final String SENT = "\\Sent";
    public static final String TRASH = "\\Trash";
    public static final String JUNK = "\\Junk";
    public static final String DRAFTS = "\\Drafts";
	public static final String SPAM = "\\Spam";
    public static final String DRAFTS_FOLDER = "Drafts";
    public static final String YAHOO_DRAFTS_FOLDER = "\"Draft\"";
    public static final String YAHOO_IMAP_SERVER = "yahoo";
    public static final String FLAG_DRAFT = "\\DRAFT";
    public static final String UTF8_ACCEPT = "UTF8=ACCEPT";
    public static final String UTF8_ONLY = "UTF8=ONLY";
    public static final String SPECIAL_USE = "SPECIAL-USE";

    // SncAdapter Porting
    // IMAP Lemonade Extensions constants
    public static final String VANISHED = "VANISHED";
    public static final String HIGHESTMODSEQ = "HIGHESTMODSEQ";
    public static final String NOMODSEQ = "NOMODSEQ";
    public static final String LITERALPLUS = "LITERAL+";
    public static final String CONDSTORE = "CONDSTORE";
    public static final String QRESYNC = "QRESYNC";
    public static final String ENABLE = "ENABLE";
    public static final String CATENATE = "CATENATE";
    public static final String URLAUTH = "URLAUTH";
    public static final String IDLE = "IDLE";
    public static final String DONE = "DONE";
    public static final String ANNOTATION = "ANNOTATION";
    public static final String GENURLAUTH = "GENURLAUTH";
    public static final String BURL = "BURL";
    public static final String ESEARCH = "ESEARCH";
    public static final String UID_SORT = "UID SORT";
    public static final String REVERSE = "REVERSE";
    public static final String DATE = "DATE";
    public static final String UTF_8 = "UTF-8";
    public static final String AUTHENTICATE = "AUTHENTICATE";
    public static final String XYMCOOKIE64 = "XYMCOOKIEB64";
    public static final String CLIENTBUG = "CLIENTBUG";
    public static final String XAOLREAD = "XAOL-READ";
    public static final String XYMHIGHESTMODSEQ = "XYMHIGHESTMODSEQ";
    public static final String MOVE = "MOVE";
    public static final String UID_MOVE = "UID MOVE";
    public static final String REALM = "realm";
    public static final String NONCE = "nonce";
    public static final String AUTH = "auth";
    public static final String DIGEST_MD5 = "DIGEST-MD5";
    public static final String URLMECH = "URLMECH";
    public static final String XOAUTH2 = "XOAUTH2";

    // SncAdapter Porting
    // Verizon specific extended header constant
    public static final String VSENT = "vSent";
    public static final String VDRAFT = "vDraft";
    public static final String VZW_SRCUSR = "X-VZW-SOURCE-USER";
    public static final String VZW_SRCFOLDER = "X-VZW-SOURCE-MESSAGE-FOLDER";
    public static final String COPYUID = "COPYUID";
    public static final String UNIFIED_INBOX = "UnifiedInbox";
    public static final String UNIFIED_TRASH = "Trash";

    //Limit the number of search responses to 100 similar to EAS
    public static final int IMAP_SEARCH_SERVER_RESPONSE_LIMIT = 99;

    //SMTP 8BITMIME Support
    public static final String SMTP_8BITMIME = "8BITMIME";

    //IMAP Compress (deflate) support
    public static final String COMPRESS_DEFALTE = "COMPRESS=DEFLATE";
    public static final String COMPRESS = "COMPRESS";
    public static final String DEFALTE = "DEFLATE";

}
