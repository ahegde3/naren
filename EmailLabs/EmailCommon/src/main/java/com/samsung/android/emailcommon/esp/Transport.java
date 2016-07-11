package com.samsung.android.emailcommon.esp;

public interface Transport {
	 public static final int INVALID = 0xFF;
     public static final int EAS = 1;
     public static final int LEGACY_POP = 2;
     public static final int LEGACY_IMAP = 3;
     public static final int OZ = 4;
     // SNC-REMOVE-SECTION-START
     public static final int SNC = 5;
     // SNC-REMOVE-SECTION-END
     // ++added by taesoo77.lee adapter_work
     public static final int SEVEN = 6;
     // --
     public static final int TRANSPORT_MAX = 7;
}
