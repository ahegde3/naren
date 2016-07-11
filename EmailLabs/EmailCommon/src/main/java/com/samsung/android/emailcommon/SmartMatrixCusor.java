package com.samsung.android.emailcommon;

import android.database.MatrixCursor;
import android.os.Bundle;

public class SmartMatrixCusor extends  MatrixCursor {

    Bundle mBundle;

    public SmartMatrixCusor(String[] columnNames) {
        super(columnNames);
        // TODO Auto-generated constructor stub
    }

    public void setBundle(Bundle b) {
        mBundle= b;
    }

    @Override
    public Bundle getExtras() {
        return mBundle;
    }
}
