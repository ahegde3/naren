package com.example.naren.cardviewproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nsbisht on 1/11/18.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    //private List<SavedLocation> mDataSet;
    private Context mContext;

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_card_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, final int position) {
        holder.mCardNumber.setText("*****4099");
        holder.mCardDetail.setText("Bank of America, Texas");
        holder.mCreditCardImage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_credit_card_black_36dp));

        holder.mActionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.doAnimation(mContext, v, R.anim.alpha, new AnimationProgressCallback() {
                    @Override
                    public void onAnimationOver() {
                        Toast.makeText(mContext, "Edit Action", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.mActionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.doAnimation(mContext, v, R.anim.alpha, new AnimationProgressCallback() {
                    @Override
                    public void onAnimationOver() {
                        Toast.makeText(mContext, "Delete Action", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return 1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mCardDetail = null;
        private TextView mCardNumber = null;
        private ImageView mCreditCardImage = null;
        private ImageView mActionEdit = null;
        private ImageView mActionDelete = null;

        public ViewHolder(View v) {
            super(v);
            mCardDetail = v.findViewById(R.id.card_description);
            mCardNumber = v.findViewById(R.id.credit_card_number);
            mCreditCardImage = v.findViewById(R.id.card_image);
            mActionEdit = v.findViewById(R.id.action_edit);
            mActionDelete = v.findViewById(R.id.action_delete);
        }

    }

    public CustomAdapter(Context context) {
        mContext = context;
    }
}
