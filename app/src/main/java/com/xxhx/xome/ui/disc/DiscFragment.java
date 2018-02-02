package com.xxhx.xome.ui.disc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.helper.ContextHelper;
import com.xxhx.xome.ui.BaseFragment;
import com.xxhx.xome.view.BadgeView;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.text.TextUtils.isEmpty;

/**
 * Created by xxhx on 2016/9/19.
 */
public class DiscFragment extends BaseFragment implements DiscContract.View {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private DiscContract.Presenter mPresenter;

    private ViewGroup mPublicSection;
    private ViewGroup mPrivateSection;

    public static DiscFragment newInstance(int sectionNumber) {
        DiscFragment fragment = new DiscFragment();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String retrieveTitle() {
        return ContextHelper.getString(R.string.disc);
    }

    @Override
    public int retrieveThemeColor() {
        return ContextHelper.getColor(R.color.disc);
    }

    @Override
    public int retrieveThemeDarkColor() {
        return ContextHelper.getColor(R.color.darkDisc);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DiscPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_disc, container, false);
        mPublicSection = (ViewGroup) rootView.findViewById(R.id.section_public);
        mPrivateSection = (ViewGroup) rootView.findViewById(R.id.section_private);
        mPresenter.subscribe();
        return rootView;
    }

    /**
     * Generate a id for use in ItemView.
     * Copied from View.generateViewId()
     * @return a generated ID value
     */
    private int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private int getPopulateColor(@DrawableRes int iconResId) {
        int populateColor = 0;
        Palette.Swatch populateSwatch = null;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconResId);
        Palette palette = new Palette.Builder(bitmap).generate();
        if(palette != null) {
            List<Palette.Swatch> swatches = palette.getSwatches();
            if(swatches != null && swatches.size() > 0) {
                populateSwatch = swatches.get(0);
                for(int i = 1; i < swatches.size(); i++) {
                    Palette.Swatch temp = swatches.get(i);
                    if(temp.getPopulation() > populateSwatch.getPopulation()) {
                        populateSwatch = temp;
                    }
                }
            }
        }
        if(populateSwatch != null) {
            populateColor = populateSwatch.getRgb();
        }
        return populateColor;
    }

    private View getView(@DrawableRes int iconResId, String title, String message, int badgeCount, ViewGroup parent) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_disc, parent, false);
        ItemViewHolder holder = new ItemViewHolder();
        holder.icon = itemView.findViewById(R.id.icon);
        holder.title = (TextView) itemView.findViewById(R.id.title);
        holder.message = (TextView) itemView.findViewById(R.id.message);
        holder.badge = (BadgeView) itemView.findViewById(R.id.badge);
        itemView.setTag(holder);
        holder.icon.setImageResource(iconResId);
        holder.title.setText(title);
        //holder.title.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        if(!isEmpty(message)) {
            int messageTextColor = getPopulateColor(iconResId);
            if(messageTextColor != 0) {
                holder.message.setTextColor(messageTextColor);
            }
            holder.message.setText(message);
            holder.message.setVisibility(View.VISIBLE);
        }
        if(badgeCount > 0) {
            holder.badge.showBadgeCount(badgeCount);
        }
        //itemView.setId(generateViewId());
        return itemView;
    }

    @Override
    public int addPublicItem(int itemId, @DrawableRes int iconResId, String title, String message,
            int badgeCount, View.OnClickListener onClickListener) {
        View item = getView(iconResId, title, message, badgeCount, mPublicSection);
        item.setId(itemId);
        if(onClickListener != null) {
            item.setOnClickListener(onClickListener);
        }
        ViewGroup.LayoutParams params = item.getLayoutParams();
        if(params != null) {
            mPublicSection.addView(item, params);
        }
        else {
            mPublicSection.addView(item);
        }
        if(mPublicSection.getVisibility() != View.VISIBLE) {
            mPublicSection.setVisibility(View.VISIBLE);
        }
        return item.getId();
    }

    @Override
    public int addPrivateItem(int itemId, @DrawableRes int iconResId, String title, String message,
            int badgeCount, View.OnClickListener onClickListener) {
        View item = getView(iconResId, title, message, badgeCount, mPrivateSection);
        item.setId(itemId);
        if(onClickListener != null) {
            item.setOnClickListener(onClickListener);
        }
        ViewGroup.LayoutParams params = item.getLayoutParams();
        if(params != null) {
            mPrivateSection.addView(item, params);
        }
        else {
            mPrivateSection.addView(item);
        }
        if(mPrivateSection.getVisibility() != View.VISIBLE) {
            mPrivateSection.setVisibility(View.VISIBLE);
        }
        return item.getId();
    }

    @Override
    public void updatePublicItem(int viewId, String message, int badgeCount) {
        View item = mPublicSection.findViewById(viewId);
        if(item != null) {
            ItemViewHolder holder = (ItemViewHolder) item.getTag();
            if(isEmpty(message)) {
                holder.message.setVisibility(View.GONE);
            }
            else {
                holder.message.setText(message);
                holder.message.setVisibility(View.VISIBLE);
            }
            holder.badge.showBadgeCount(badgeCount);
        }
    }

    @Override
    public void updatePrivateItem(int viewId, String message, int badgeCount) {
        View item = mPrivateSection.findViewById(viewId);
        if(item != null) {
            ItemViewHolder holder = (ItemViewHolder) item.getTag();
            if(isEmpty(message)) {
                holder.message.setVisibility(View.GONE);
            }
            else {
                holder.message.setText(message);
                holder.message.setVisibility(View.VISIBLE);
            }
            holder.badge.showBadgeCount(badgeCount);
        }
    }

    class ItemViewHolder {
        ImageView icon;
        TextView title;
        TextView message;
        BadgeView badge;
    }
}
