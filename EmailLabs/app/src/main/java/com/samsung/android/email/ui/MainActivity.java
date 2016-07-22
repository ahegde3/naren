package com.samsung.android.email.ui;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.samsung.android.email.R;
import com.samsung.android.email.model.SampleData;
import com.samsung.android.email.utils.RuntimePermissionChecker;
import com.samsung.android.emailcommon.utility.EmailLog;
import com.samsung.android.emailcommon.utility.EmailRuntimePermission;

/**
 * Created by nsbisht on 5/31/16.
 */
public class MainActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!RuntimePermissionChecker.hasPermissions(
                this, RuntimePermissionChecker.PERMISSION_STORAGE)) {
            RuntimePermissionChecker.requestPermission(RuntimePermissionChecker.PERM_REQUEST_TYPE_STORAGE, this);
        } else {
            initViews();
        }
    }

    private void initViews() {
        setContentView(R.layout.activity_main_recycler_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new EmailListAdapter(this, SampleData.getSampleData());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimePermissionChecker.PERM_REQUEST_TYPE_STORAGE) {
            boolean granted = true;

            for (int res : grantResults) {
                if (res == PackageManager.PERMISSION_DENIED) {
                    granted = false;
                    finish();
                    break;
                } else {
                    initViews();
                    break;
                }
            }
        }
    }
}
