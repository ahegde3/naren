
package com.samsung.android.emailcommon.combined.common;

import com.samsung.android.emailcommon.provider.EmailContent;
import com.samsung.android.emailcommon.provider.EmailContent.Mailbox;
import com.samsung.android.emailcommon.provider.EmailContent.MailboxColumns;
import com.samsung.android.emailcommon.provider.EmailContent.MessageColumns;

import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

public class QueryUtil {

    /**
     * make string for database query
     *
     * @param distinct
     * @param tables
     * @param columns
     * @param where
     * @return
     */
    public static String buildQueryString(boolean distinct, String tables, String columns,
            String where) {
        if (TextUtils.isEmpty(tables)) {
            throw new IllegalArgumentException("no tables");
        }

        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (!TextUtils.isEmpty(columns)) {
            query.append(columns + " ");
        } else {
            query.append("* ");
        }
        query.append("FROM ");
        query.append(tables);
        if (where != null)
        	appendClause(query, " WHERE ", where);
        return query.toString();
    }

    /**
     * appendClause
     *
     * @param s
     * @param name
     * @param clause
     */
    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!TextUtils.isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }


    /**
     * Add the names that are non-null in columns to s, separating them with
     * commas.
     */
    public static void appendColumns(StringBuilder s, String[] columns, String tableAlias) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];

            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                if (column.equals("_id")) {
                    s.append(tableAlias + "." + "_id");
                } else {
                    s.append(column);
                }
            }
        }
        s.append(' ');
    }

    public static String getAccountQueryString(String[] projection, String selection,
            String sortOrder) {
//        StringBuffer sql = new StringBuffer();
//
//        // 1. make field name string for query
//        /*
//         * String needColumns =
//         * "t1.*, t2.accountKey, t2.sevenAccountKey, t2.typeMsg, t2.timeLimit, t2.sizeLimit, "
//         * + "t2.pushSync, t2.syncSchedule, t2.keepConnLowBat ";
//         */
////        String needColumns = "t1.*, t2.accountKey, t2.sevenAccountKey, t2.typeMsg, t2.timeLimit, t2.sizeLimit,"
////                + " t2.peakTime, t2.offPeakTime, t2.days, t2.peakStartTime, t2.peakEndTime, t2.whileRoaming,"
////                + " t2.attachmentEnabled ";
//
//        String needColumns = "t1.* ";
//
//        // 2. make sub query string
//        sql.append("(");
////        sql.append(buildQueryString(false, "Account t1, Account_CB t2", needColumns,
//        sql.append(buildQueryString(false, "Account t1", needColumns,
//                null));
//        sql.append(")");
        // 3. make all of query string
        return SQLiteQueryBuilder.buildQueryString(false, "Account", projection, selection,
                null, null, sortOrder, null);
    }

    public static String getMessageQueryString(String[] projection, String selection,
            String sortOrder) {
        return SQLiteQueryBuilder.buildQueryString(false, "Message", projection, selection,
                null, null, sortOrder, null);
    }

    public static String getMessageThreadQueryString(String[] projection, String selection,
            String selectionArgs[], String sortOrder) {
        String pro[] = new String[projection.length];
        for (int i= 0; i < projection.length ; i++) {
            pro[i] = projection[i];
        }
        String sel = (selection != null) ? selection : "";

//        sel = sel.replaceAll(MessageColumns.THREAD_ID, "m." + MessageColumns.THREAD_ID);
        // must add "unread", "total" in projection that you use.

        if(pro.length > 7){
            pro[pro.length - 6] = "sum(flagRead) unread";
            pro[pro.length - 5] = "count(threadId) total";
            pro[pro.length - 4] = "sum(flagFavorite) favoriteCount";
            pro[pro.length - 3] = "count(case when flagStatus = 1 then 1 end) completedCount";
            pro[pro.length - 2] = "count(case when flagStatus = 2 then 1 end) flaggedCount";
        }
//        for (int i = 0; i < pro.length; i ++) {
//            if (MessageColumns.THREAD_ID.equals(pro[i])) {
//                pro[i] = "m." + MessageColumns.THREAD_ID;
//                break;
//            }
//        }
        int mailboxType= -1;
        long accountId = -1;
        long mailboxId = -1;
        if (selectionArgs != null && selectionArgs.length == 3) {
            accountId = Long.parseLong(selectionArgs[0]);
            mailboxId = Long.parseLong(selectionArgs[1]);
            mailboxType = Integer.parseInt(selectionArgs[2]);
        }

        String prj1 = TextUtils.join(", ", pro);
        StringBuilder query = new StringBuilder();
        query.append("SELECT ").append(prj1);
        query.append(" FROM (");
        query.append(" SELECT * ");
        query.append(" FROM Message ");
        query.append(" WHERE ").append(MessageColumns.MAILBOX_KEY);

        if(mailboxId == Mailbox.QUERY_ALL_INBOXES){
            query.append(" IN ( SELECT _id FROM Mailbox WHERE ( ").append(MailboxColumns.TYPE).append(" = ").append(Mailbox.TYPE_INBOX).append(" ) ");
            query.append(" ) ");
            query.append(" AND threadId IN ( ");
            query.append(" SELECT ").append(MessageColumns.THREAD_ID).append(" FROM MESSAGE ").append(" WHERE ").append(MessageColumns.MAILBOX_KEY);
            query.append(" IN ( SELECT _id FROM Mailbox WHERE ( ").append(MailboxColumns.TYPE).append(" = ").append(Mailbox.TYPE_INBOX).append(" ) ");
            query.append(" ) ");
//            query.append(" AND ").append(EmailContent.Message.FLAG_LOADED_SELECTION);
//            query.append(" AND ").append(MessageColumns.FLAG_DELETEHIDDEN + "=0");

            query.append(" ) ");

        } else {
            query.append(" = ").append(mailboxId);
            query.append(" AND threadId IN ( ");
            query.append(" SELECT ").append(MessageColumns.THREAD_ID).append(" FROM MESSAGE ").append(" WHERE ").append(MessageColumns.MAILBOX_KEY);
            query.append(" = ").append(mailboxId).append(" ) ");
//            query.append(" AND ").append(EmailContent.Message.FLAG_LOADED_SELECTION);
//            query.append(" AND ").append(MessageColumns.FLAG_DELETEHIDDEN + "=0 )");
        }

        query.append(" AND ").append(EmailContent.Message.FLAG_LOADED_SELECTION);
        query.append(" AND ").append(MessageColumns.FLAG_DELETEHIDDEN + "=0");

        query.append(" ORDER BY ");
        query.append(MessageColumns.TIMESTAMP).append(" COLLATE NOCASE ASC ) ");
        query.append(" GROUP BY ").append(MessageColumns.THREAD_ID);
        query.append(" ORDER BY ").append(sortOrder);
//        Log.e("sungboo.woo", "query is :" + query.toString());







//
//        String prj1 = TextUtils.join(", ", pro);
//        StringBuilder query = new StringBuilder();
//        query.append("SELECT ").append(prj1);
//        query.append(" FROM (");
//        query.append(" SELECT * FROM ");
//
//        if(mailboxType == Mailbox.TYPE_INBOX || mailboxType == Mailbox.TYPE_SENT) {
//            query.append(" Message m, ( ");
//            query.append(" SELECT ").append(MessageColumns.ID).append(" FROM Mailbox ").append(" WHERE (");
//
//            if (mailboxId != Mailbox.QUERY_ALL_INBOXES) {
//                query.append(MailboxColumns.TYPE).append(" = ").append(Mailbox.TYPE_INBOX).append(" OR ").append(MailboxColumns.TYPE).append(" = ").append(Mailbox.TYPE_SENT).append(" ) ");
//                query.append(" AND ").append(MailboxColumns.ACCOUNT_KEY).append(" = ").append(accountId).append(" ) mailbox");
//
//            } else if (mailboxId == Mailbox.QUERY_ALL_INBOXES) {
//
//                query.append(MailboxColumns.TYPE).append(" = ").append(Mailbox.TYPE_INBOX).append(" OR ").append(MailboxColumns.TYPE).append(" = ").append(Mailbox.TYPE_SENT).append(" )) mailbox");
//
//            }
//            query.append(" WHERE ").append(" mailbox._id = m.mailboxKey ");
//        } else {
//            query.append(" Message m WHERE m.mailboxKey = ").append(mailboxId);
//        }
//        query.append(" AND ").append(EmailContent.Message.FLAG_LOADED_SELECTION);
//        query.append(" AND ").append(MessageColumns.FLAG_DELETEHIDDEN + "=0");
//
//        query.append(" ORDER BY ");
//        if(mailboxType == Mailbox.TYPE_INBOX || mailboxType == Mailbox.TYPE_SENT){
//            query.append(MessageColumns.MAILBOX_TYPE);
//            if(mailboxType == Mailbox.TYPE_INBOX){
//                query.append(" DESC , ");
//            } else {
//                query.append(" ASC , ");
//            }
//        }
//        query.append(MessageColumns.TIMESTAMP).append(" COLLATE NOCASE ASC )");
//        query.append(" GROUP BY ").append(MessageColumns.THREAD_ID);
//        query.append(" ORDER BY ").append(sortOrder).append(" )");
//        if(mailboxType == Mailbox.TYPE_INBOX || mailboxType == Mailbox.TYPE_SENT){
//
//            query.append(" WHERE ").append(MessageColumns.MAILBOX_TYPE).append(" = ").append(mailboxType);
//        }


//        query.append("SELECT ").append(prj1);
//        query.append(" from Message as m ");
//        query.append("inner join ( select ").append(MessageColumns.THREAD_ID).append(",");
//        query.append(" sum(").append(MessageColumns.FLAG_READ).append(") as unread").append(",");
//        query.append(" count(").append(MessageColumns.THREAD_ID).append(") as total");
//        query.append(" from Message where ").append(selection).append(" group by ").append(MessageColumns.THREAD_ID).append(") as x ");
//        query.append("inner join (select max(").append(MessageColumns.TIMESTAMP).append(") as recent, ").append(MessageColumns.THREAD_ID);
//        query.append(" from Message where ").append(selection);
//        if (!TextUtils.isEmpty(mailboxType)) {
//            query.append(" and ").append(MessageColumns.MAILBOX_KEY).append(" IN ( select ").append(MailboxColumns.ID).append(" from ").append(Mailbox.TABLE_NAME);
//            query.append(" where ").append(MailboxColumns.TYPE).append("=").append(mailboxType).append(")");
//        }
//        query.append(" group by ").append(MessageColumns.THREAD_ID).append(") as y");
//        query.append(" on m.").append(MessageColumns.THREAD_ID).append("=x.").append(MessageColumns.THREAD_ID);
//        query.append(" and m.").append(MessageColumns.THREAD_ID).append("=y.").append(MessageColumns.THREAD_ID).append(" and m.").append(MessageColumns.TIMESTAMP).append("=y.recent");
//        query.append(" where ").append(sel);
//        if (!TextUtils.isEmpty(mailboxType)) {
//            query.append(" and ").append(MessageColumns.MAILBOX_KEY).append(" IN ");
//            query.append("( SELECT ").append(MailboxColumns.ID).append(" from ").append(Mailbox.TABLE_NAME);
//            query.append(" where ").append(MailboxColumns.TYPE).append("=").append(mailboxType).append(")");
//        }
//        query.append(" group by ").append(" m.").append(MessageColumns.THREAD_ID);
//        query.append(" order by ").append(sortOrder);
        return query.toString();
    }

    public static String getProtocolQueryString(String[] projection, String selection,
            String sortOrder) {
        StringBuffer sql = new StringBuffer();
        sql.append("( ");
        sql.append("select c.* ");
        sql.append("     , (select protocol from hostAuth x where x._id = a.hostAuthKeyRecv) recvProtocol ");
        sql.append("     , (select protocol from hostAuth x where x._id = a.hostAuthKeySend) sendProtocol ");
        sql.append(" from account a, account_cb c ");
        sql.append(" where a._id = " + selection);
        sql.append(" and a._id = c.accountKey ");
        sql.append(") ");

        return SQLiteQueryBuilder.buildQueryString(false, sql.toString(), projection, null, null,
                null, sortOrder, null);
    }

    public static String getMailboxQueryString(String[] projection, String selection,
            String sortOrder) {
        StringBuffer sql = new StringBuffer();
        // 1. make field name string for query
//        String needColumns = "t1.*, t2.mailboxKey, t2.typeMsg, t2.sevenMailboxKey, t2.syncFlag ";
        String needColumns = "t1.* ";
        // 2. make sub query string
        sql.append("(");
//        sql.append(buildQueryString(false, "Mailbox t1, Mailbox_CB t2", needColumns,
//                "t1._id = t2.mailboxKey"));
        sql.append(buildQueryString(false, "Mailbox t1", needColumns,
                null));
        sql.append(")");
        // 3. make all of query string
        return SQLiteQueryBuilder.buildQueryString(false, sql.toString(), projection, selection,
                null, null, sortOrder, null);
    }

    public static String getMessageAllSearchQueryString(String[] projection, String selection, String selectionArg, String sortOrder) {
        String pro[] = new String[projection.length];
        for (int i= 0; i < projection.length ; i++) {
            pro[i] = projection[i];
        }
        pro[pro.length - 1] = "IFNULL(attach.count, 0) attachSearchCount";
        String prj1 = TextUtils.join(", ", pro);
        StringBuilder query = new StringBuilder();
        query.append("SELECT ").append(prj1);
        query.append(" FROM (").append(getMessageQueryString(projection, selection, sortOrder)).append(") a left outer join (");
        query.append("SELECT messageKey, count(messageKey) count FROM Attachment WHERE (fileName like '%").append(selectionArg);
        query.append("%' ESCAPE '\\' AND isInline = '0'");
        query.append(") group by messageKey) attach ON a._id = attach.messageKey");
        return query.toString();
    }
}
