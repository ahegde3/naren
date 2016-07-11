package com.samsung.android.emailcommon.provider;

public class BaseItem {
    public long id;
    
    public BaseItem()  {
        id = -1;
    }
    
    public BaseItem(long accountId)   {
        id = accountId;
    }
}