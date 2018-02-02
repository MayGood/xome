package com.xxhx.xome.ui.disc.wealth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.ui.disc.wealth.data.Turnover;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xxhx on 2018/1/10.
 */

public class DebtChangeAdapter extends ArrayAdapter<Turnover> {

    public DebtChangeAdapter(Context context, List<Turnover> objects) {
        super(context, 0, objects);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Turnover turnover = getItem(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_turnover, null);
            holder = new ViewHolder();
            holder.hour = (TextView) convertView.findViewById(R.id.hour);
            holder.minute = (TextView) convertView.findViewById(R.id.minute);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            holder.type = (TextView) convertView.findViewById(R.id.type);
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
        calendar.setTime(turnover.getTime());
        holder.hour.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        holder.minute.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
        holder.date.setText((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
        holder.description.setText(turnover.getNote());
        holder.type.setText(turnover.getTurnoverType().getType());
        if(turnover.getAmountInFens() >= 0) {
            holder.amount.setTextColor(getContext().getResources().getColor(R.color.positiveWealth));
        }
        else {
            holder.amount.setTextColor(getContext().getResources().getColor(R.color.negativeWealth));
        }
        holder.amount.setText(turnover.getFormattedAmount());
        //view.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        long balanceInFens = mWealthAccount.getBalanceInFens();
        //        mWealthAccount.setBalanceInFens(balanceInFens - 923);
        //        App.getInstance().getDaoSession().getWealthAccountDao().update(mWealthAccount);
        //        //turnover.setTurnoverType(TurnoverType.TRANSFER);
        //        //App.getInstance().getDaoSession().getTurnoverDao().update(turnover);
        //        //App.getInstance().getDaoSession().getTurnoverDao().delete(turnover);
        //    }
        //});
        return convertView;
    }

    class ViewHolder {
        TextView hour;
        TextView minute;
        TextView date;
        TextView description;
        TextView amount;
        TextView type;
    }
}
