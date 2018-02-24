package com.xxhx.xome.ui.disc.wealth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.xxhx.moduleclosedatabase.debt.DebtChange;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xxhx on 2018/2/23.
 */

public class DebtChangeAdapter extends ArrayAdapter<DebtChange> {

    public DebtChangeAdapter(Context context, List<DebtChange> objects) {
        super(context, 0, objects);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DebtChange debtChange = getItem(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_debt_change, null);
            holder = new ViewHolder();
            holder.hour = (TextView) convertView.findViewById(R.id.hour);
            holder.minute = (TextView) convertView.findViewById(R.id.minute);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.hour.setTypeface(App.getInstance().getRobotoTypeface());
        holder.minute.setTypeface(App.getInstance().getRobotoTypeface());
        holder.date.setTypeface(App.getInstance().getRobotoTypeface());
        holder.amount.setTypeface(App.getInstance().getRobotoTypeface());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(debtChange.getDate());
        holder.hour.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        holder.minute.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
        holder.date.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
        holder.description.setText(debtChange.getNotes());
        if(debtChange.getChangeInFens() >= 0) {
            holder.amount.setTextColor(getContext().getResources().getColor(R.color.positiveWealth));
        }
        else {
            holder.amount.setTextColor(getContext().getResources().getColor(R.color.negativeWealth));
        }
        holder.amount.setText(debtChange.getFormattedAmount());
        return convertView;
    }

    class ViewHolder {
        TextView hour;
        TextView minute;
        TextView date;
        TextView description;
        TextView amount;
    }
}
