package com.bishtlabs.cryptowatch.ui.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bishtlabs.cryptowatch.data.model.local.Coin;
import com.bishtlabs.cryptowatch.databinding.CoinListItemViewBinding;
import com.bishtlabs.cryptowatch.ui.base.BaseViewHolder;

import java.util.List;

/**
 * Created by nsbisht on 6/11/18.
 */

public class CoinsListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<Coin> mCoinsList;

    public CoinsListAdapter(List<Coin> coins) {
        this.mCoinsList = coins;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CoinListItemViewBinding binding = CoinListItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mCoinsList == null ? 0 : mCoinsList.size();
    }

    class ViewHolder extends BaseViewHolder {
        private CoinListItemViewBinding mBinding;
        private CoinsItemViewModel mViewModel;

        public ViewHolder(CoinListItemViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            Coin coin = mCoinsList.get(position);
            mViewModel = new CoinsItemViewModel(coin);
            mBinding.setViewModel(mViewModel);

            mBinding.executePendingBindings();
        }
    }
}
