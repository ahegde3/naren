package com.samsung.android.emailcommon.provider;

public interface AccountValues {
	public interface SyncWindow {
		public static final int SYNC_WINDOW_AUTO = -2;
		public static final int SYNC_WINDOW_USER = -1;
		public static final int SYNC_WINDOW_UNKNOWN = 0;
		public static final int SYNC_WINDOW_1_DAY = 1;
		public static final int SYNC_WINDOW_3_DAYS = 2;
		public static final int SYNC_WINDOW_1_WEEK = 3;
		public static final int SYNC_WINDOW_2_WEEKS = 4;
		public static final int SYNC_WINDOW_1_MONTH = 5;
		public static final int SYNC_WINDOW_ALL = 6;

		public static final int OZ_SYNC_WINDOW_USER = -1;
		public static final int OZ_SYNC_WINDOW_50_DAYS = 1;
		public static final int OZ_SYNC_WINDOW_100_DAYS = 2;
		public static final int OZ_SYNC_WINDOW_200_DAYS = 3;
		
	    public static final String FILTER_AUTO = Integer.toString(SYNC_WINDOW_AUTO);
	    public static final String FILTER_ALL = "0";
	    public static final String FILTER_1_DAY = Integer.toString(SYNC_WINDOW_1_DAY);
	    public static final String FILTER_3_DAYS = Integer.toString(SYNC_WINDOW_3_DAYS);
	    public static final String FILTER_1_WEEK = Integer.toString(SYNC_WINDOW_1_WEEK);
	    public static final String FILTER_2_WEEKS = Integer.toString(SYNC_WINDOW_2_WEEKS);
	    public static final String FILTER_1_MONTH = Integer.toString(SYNC_WINDOW_1_MONTH);
	    public static final String FILTER_3_MONTHS = "6";
	    public static final String FILTER_6_MONTHS = "7";
	    // change@wtl.skumar for tasks start
	    public static final String FILTER_BY_INCOMPLETE_TASKS = "8";
	}
	public interface SyncSize {
		public static final int MESSAGE_SIZE_1_5_K = 0;
		public static final int MESSAGE_SIZE_60_K = 1;
		public static final int MESSAGE_SIZE_100_K = 2;
		public static final int MESSAGE_SIZE_20_K = 3;        

		public static final int MESSAGE_SIZE_2_KB  = 2048;//2 KB
		public static final int MESSAGE_SIZE_20_KB = 20480;//20 KB
		public static final int EMAIL_SIZE_2048 = 2048;
		public static final int EMAIL_SIZE_51200 = 51200;
		public static final int EMAIL_SIZE_102400 = 102400;
		public static final int EMAIL_SIZE_ALL_WITHOUT_ATTACHMENT = 1;
        public static final int EMAIL_SIZE_ALL_WITH_ATTACHMENT = 2;

		public static final int MESSAGE_ROAMING_SIZE_USE_DEFAULT = 1;
		public static final int MESSAGE_ROAMING_SIZE_1_5_K = 0;
		public static final int MESSAGE_ROAMING_SIZE_20_K = 2;
		public static final int MESSAGE_ROAMING_SIZE_60_K = 3;
		public static final int MESSAGE_ROAMING_SIZE_100_K = 4;

		// SncAdapter Porting
		public static final int MAX_SMALL_MESSAGE_SIZE = (60 * 1024);
		
	    // For EAS 12, we use HTML, so we want a larger size than in EAS 2.5
	    public static final String EAS12_TRUNCATION_SIZE = "400000";

	    // For EAS 2.5, truncation is a code; the largest is "7", which is 100k
	    // change@wtl.dtuttle start
	    // dr_8: WRONG!!! 7 is for 50k. I checked the dataviz code, which indicates
	    // that
	    // 7 => 50k and 8 => 100k. I tested this with "7" and it truncated a 60k
	    // email message.
	    // public static final String EAS2_5_TRUNCATION_SIZE = "7";
	    public static final String EAS2_5_TRUNCATION_SIZE = "8";
        public static final String EAS_MIME_SYNC_TRUNCATION_SIZE = "1";

	    // change@wtl.dtuttle end

	    // change@wtl.dtuttle start
	    // dr_79: Add the email size enum to support EAS enumeration for this data.
	    // frameworks/base/activesyncfwk/dataviz/rc/inc/IStargatePreferences.h from
	    // cupcake
	    // provided the inspiration for this.
	    
	    // 2003
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_FOUR_KB_03 = 1;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_FIVE_KB_03 = 2;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_SEVEN_KB_03 = 3;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_TEN_KB_03 = 4;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_TWENTY_KB_03 = 5;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_FIFTY_KB_03 = 6;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_HUNDRED_KB_03 = 7;
        public static final int EMAIL_RETRIEVE_SIZE_INDEX_NO_LIMIT_03 = 8;
	    
	    // 2007, 2010
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_HEADER_ONLY = 0;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_HALF_KB = 1;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_ONE_KB = 2;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_TWO_KB = 3;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_FIVE_KB = 4;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_TEN_KB = 5;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_TWENTY_KB = 6;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_FIFTY_KB = 7;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_HUNDRED_KB = 8;
	    public static final int EMAIL_RETRIEVE_SIZE_INDEX_NO_LIMIT = 9;
	}
	
	public interface SyncTime {
	    //Account sync schedule
	    // Sentinel values for the mSyncInterval field of both Account records
	    public static final int CHECK_INTERVAL_NEVER = -1;

	    public static final int CHECK_INTERVAL_PUSH = -2;
	    // GGSM. taebong.ha@Add mSyncInterval field for some sales code
        // Ping indicates that the EAS mailbox is synced based on a "ping" from
        // the server
        public static final int CHECK_INTERVAL_PING = -3;

        // Push-Hold indicates an EAS push or ping Mailbox shouldn't sync just
        // yet
        public static final int CHECK_INTERVAL_PUSH_HOLD = -4;

        // huijae.lee
        public static final int CHECK_INTERVAL_DEFAULT = 0;
	    public static final int CHECK_INTERVAL_15_MINS = 15;

	    public static final int CHECK_INTERVAL_5_MINS = 5;

	    public static final int CHECK_INTERVAL_1HOUR = 60;
	    
	    public static final int CHECK_ROAMING_MANUAL = 0; //

	    public static final int CHECK_ROAMING_SYNC_SCHEDULE = 1;//
	    


	}
    // From EAS spec
    // Mail Cal
    // 0 No filter Yes Yes
    // 1 1 day ago Yes No
    // 2 3 days ago Yes No
    // 3 1 week ago Yes No
    // 4 2 weeks ago Yes Yes
    // 5 1 month ago Yes Yes
    // 6 3 months ago No Yes
    // 7 6 months ago No Yes

    // change@wtl.skumar for tasks end
    public static final String BODY_PREFERENCE_TEXT = "1";
    public static final String BODY_PREFERENCE_HTML = "2";
    public static final String MIME_BODY_PREFERENCE_TEXT = "0";
    public static final String MIME_BODY_PREFERENCE_SMIME = "1";
    public static final String MIME_BODY_PREFERENCE_MIME = "2";
    // change@wtl.rprominski smime support start
    public static final String BODY_PREFERENCE_MIME = "4";
    // change@wtl.prominski smime support end
}
