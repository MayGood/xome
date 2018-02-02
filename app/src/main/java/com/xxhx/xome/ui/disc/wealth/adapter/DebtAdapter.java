package com.xxhx.xome.ui.disc.wealth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.xxhx.moduleclosedatabase.debt.Debt;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xxhx on 2018/1/10.
 */

public class DebtAdapter extends ArrayAdapter<Debt> {

    public DebtAdapter(Context context, List<Debt> objects) {
        super(context, 0, objects);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Debt debt = getItem(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_debt, null);
            holder = new ViewHolder();
            holder.hour = (TextView) convertView.findViewById(R.id.hour);
            holder.minute = (TextView) convertView.findViewById(R.id.minute);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.creditor = (TextView) convertView.findViewById(R.id.creditor);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.base = (TextView) convertView.findViewById(R.id.base);
            convertView.setTag(holder);

            holder.hour.setTypeface(App.getInstance().getRobotoTypeface());
            holder.minute.setTypeface(App.getInstance().getRobotoTypeface());
            holder.date.setTypeface(App.getInstance().getRobotoTypeface());
            holder.amount.setTypeface(App.getInstance().getRobotoTypeface());
            holder.base.setTypeface(App.getInstance().getRobotoTypeface());
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(debt.getAddedDate());
        holder.hour.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        holder.minute.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
        holder.date.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
        holder.creditor.setText(debt.getCreditor());
        holder.amount.setText(debt.getFormattedAmount());
        holder.base.setText("(" + debt.getFormattedBase() + ")");
        return convertView;
    }

    class ViewHolder {
        TextView hour;
        TextView minute;
        TextView date;
        TextView creditor;
        TextView amount;
        TextView base;
    }
}
