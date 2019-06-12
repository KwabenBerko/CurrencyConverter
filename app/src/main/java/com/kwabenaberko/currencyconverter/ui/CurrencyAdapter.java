package com.kwabenaberko.currencyconverter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.kwabenaberko.currencyconverter.R;
import com.kwabenaberko.currencyconverter.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends BaseAdapter implements SpinnerAdapter {


    private List<Currency> mCurrencies = new ArrayList<>();


    static class CurrencyViewHolder{
        TextView currencyName;
        LinearLayout itemLayout;
    }


    @Override
    public int getCount() {
        return mCurrencies.size();
    }


    @Override
    public Object getItem(int position) {
        return mCurrencies.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setCurrencies(List<Currency> currencies){
        mCurrencies.clear();
        mCurrencies.addAll(currencies);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CurrencyViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new CurrencyViewHolder();

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_spinner_item, parent, false);

            viewHolder.currencyName = convertView.findViewById(R.id.currency_name);
            viewHolder.itemLayout = convertView.findViewById(R.id.item_layout);

            convertView.setTag(viewHolder);
        }

        else{
            viewHolder = (CurrencyViewHolder) convertView.getTag();
        }

        Currency currency = mCurrencies.get(position);
        viewHolder.currencyName.setText(String.format("%s(%s)", currency.getCurrencyName(), currency.getId()));


        return convertView;
    }
}
