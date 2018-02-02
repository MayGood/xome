package com.xxhx.xome.ui.disc.wealth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.xxhx.moduleclosedatabase.test.Test;
import com.xxhx.xome.App;
import com.xxhx.xome.R;
import com.xxhx.xome.config.Identity;
import com.xxhx.xome.data.CreditBillDao;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.ui.disc.wealth.data.AccountType;
import com.xxhx.xome.ui.disc.wealth.data.CombinedAccount;
import com.xxhx.xome.ui.disc.wealth.data.CreditBill;
import com.xxhx.xome.ui.disc.wealth.data.WealthAccount;
import com.xxhx.xome.util.CommonUtil;
import java.util.Calendar;
import java.util.List;

public class MyWealthActivity extends BaseActivity implements MyWealthContract.View {

    private ListView mAccountListView;
    private WealthAccountAdapter mAdapter;

    private MyWealthContract.Presenter mPresenter;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_wealth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_my_wealth);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAccountListView = (ListView) findViewById(R.id.list_accounts);
        mAccountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyWealthActivity.this, WealthAccountDetailActivity.class);
                intent.putExtra(WealthAccountDetailActivity.EXTRA_ID, id);
                startActivity(intent);
            }
        });

        mPresenter = new MyWealthPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_wealth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_statistics:
                gotoOverview();
                break;
            case R.id.action_add:
                gotoAddAccount();
                return true;
            case R.id.action_debt:
                gotoDebt();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoOverview() {
        Intent intent = new Intent(this, WealthOverviewActivity.class);
        startActivity(intent);
    }

    private void gotoAddAccount() {
        Intent intent = new Intent(this, NewWealthAccountActivity.class);
        startActivity(intent);
    }

    private void gotoDebt() {
        Intent intent = new Intent(this, DebtListActivity.class);
        startActivity(intent);
    }

    @Override
    protected Identity getRequiredIdentity() {
        return Identity.Manager;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showWealthAccounts(List<CombinedAccount> combinedAccounts) {
        if(mAdapter == null) {
            mAdapter = new WealthAccountAdapter(this, combinedAccounts);
            mAccountListView.setAdapter(mAdapter);
        }
        else {
            mAdapter.clear();
            mAdapter.addAll(combinedAccounts);
            mAdapter.notifyDataSetChanged();
        }
    }

    class WealthAccountAdapter extends ArrayAdapter<CombinedAccount> {

        public WealthAccountAdapter(Context context, List<CombinedAccount> accounts) {
            super(context, R.layout.list_item_wealth_account, accounts);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        private CharSequence getDisplayNameWithBillTip(String name) {
            TextView v = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.rounded_tip_text, null);
            v.setText("账单");
            int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            v.measure(spec, spec);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.draw(c);

            SpannableStringBuilder ssb = new SpannableStringBuilder(name);
            ssb.append(" ");
            int start = ssb.length();
            ssb.append('\uFFFC');
            int end = ssb.length();
            ssb.setSpan(new CenterVerticalImageSpan(getContext(), b), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }

        private CharSequence getDisplayNameWithDeadlineTip(String name, long dayToDeadline) {
            TextView v = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.rounded_tip_text, null);
            v.setText(dayToDeadline + "天");
            int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            v.measure(spec, spec);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.draw(c);

            SpannableStringBuilder ssb = new SpannableStringBuilder(name);
            ssb.append(" ");
            int start = ssb.length();
            ssb.append('\uFFFC');
            int end = ssb.length();
            ssb.setSpan(new CenterVerticalImageSpan(getContext(), b), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_wealth_account, null);
                holder = new ViewHolder();
                holder.organization = (ImageView) convertView.findViewById(R.id.item_org);
                holder.displayName = (TextView) convertView.findViewById(R.id.item_display_name);
                holder.balance = (TextView) convertView.findViewById(R.id.item_balance);
                holder.balance.setTypeface(App.getInstance().getRobotoTypeface());
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            CombinedAccount account = getItem(position);
            //holder.type.setText(wealthAccount.getOrganization().name());
            switch (account.getOrganization()) {
                case ICBC:
                    holder.organization.setImageResource(R.drawable.org_bank_icbc);
                    break;
                case CMB:
                    holder.organization.setImageResource(R.drawable.org_bank_cmb);
                    break;
                case BOC:
                    holder.organization.setImageResource(R.drawable.org_bank_boc);
                    break;
                case PSBC:
                    holder.organization.setImageResource(R.drawable.org_bank_psbc);
                    break;
                case BJBANK:
                    holder.organization.setImageResource(R.drawable.org_bank_bjbank);
                    break;
                case CITIC:
                    holder.organization.setImageResource(R.drawable.org_bank_citic);
                    break;
                case ANT:
                    holder.organization.setImageResource(R.drawable.org_internet_ant);
                    break;
                case BAIDU:
                    holder.organization.setImageResource(R.drawable.org_internet_baidu);
                    break;
                case JD:
                    holder.organization.setImageResource(R.drawable.org_internet_jd);
                    break;
            }
            if(account.getType() == AccountType.CREDIT) {
                long dayToDeadline = Long.MAX_VALUE;
                List<CreditBill> unpaidBills = account.getUnpaidBills();
                if(unpaidBills != null && unpaidBills.size() > 0) {
                    String deadline = unpaidBills.get(0).getDeadline();
                    if(deadline != null && deadline.length() == 8) {
                        try {
                            int year = Integer.parseInt(deadline.substring(0, 4));
                            int month = Integer.parseInt(deadline.substring(4, 6));
                            int dayOfMonth = Integer.parseInt(deadline.substring(6));
                            Calendar billCalendar = Calendar.getInstance();
                            billCalendar.set(year, month, dayOfMonth);
                            dayToDeadline = -CommonUtil.getRelativeDays(billCalendar.getTime().getTime());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    if(dayToDeadline < 7) {
                        holder.displayName.setText(getDisplayNameWithDeadlineTip(account.getDisplayName(), dayToDeadline));
                    }
                    else {
                        holder.displayName.setText(account.getDisplayName());
                    }
                }
                else {
                    if(account.getCurrentBill() == null) {
                        holder.displayName.setText(getDisplayNameWithBillTip(account.getDisplayName()));
                    }
                    else {
                        holder.displayName.setText(account.getDisplayName());
                    }
                }
            }
            else {
                holder.displayName.setText(account.getDisplayName());
            }
            if(account.getBalanceInFens() >= 0) {
                holder.balance.setTextColor(getResources().getColor(R.color.positiveWealth));
            }
            else {
                holder.balance.setTextColor(getResources().getColor(R.color.negativeWealth));
            }
            holder.balance.setText(account.getFormattedBalance());
            return convertView;
        }
    }

    class CenterVerticalImageSpan extends ImageSpan {

        public CenterVerticalImageSpan(Context context, Bitmap b) {
            super(context, b);
        }

        //@Override
        //public int getSize(Paint paint, CharSequence text,
        //        int start, int end,
        //        Paint.FontMetricsInt fm) {
        //    Drawable d = getCachedDrawable();
        //    Rect rect = d.getBounds();
        //
        //    if (fm != null) {
        //        fm.ascent = -rect.bottom;
        //        fm.descent = 0;
        //
        //        fm.top = fm.ascent;
        //        fm.bottom = 0;
        //    }
        //
        //    return rect.right;
        //}

        @Override
        public void draw(Canvas canvas, CharSequence text,
                int start, int end, float x,
                int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();

            int transY = top + (bottom - top - b.getBounds().bottom) / 2;

            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }

    class ViewHolder {
        ImageView organization;
        TextView displayName;
        TextView balance;
    }
}
