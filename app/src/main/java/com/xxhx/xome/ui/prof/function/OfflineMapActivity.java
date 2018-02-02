package com.xxhx.xome.ui.prof.function;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.xxhx.xome.R;
import com.xxhx.xome.ui.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class OfflineMapActivity extends BaseActivity implements
        OfflineMapManager.OfflineMapDownloadListener, OfflineMapManager.OfflineLoadedListener {
    private OfflineMapManager mOfflineMapManager;

    private ExpandableListView mListView;
    private OfflineMapAdapter mAdapter;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_offline_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ExpandableListView) findViewById(R.id.list);
        mOfflineMapManager = new OfflineMapManager(this, this);
        mOfflineMapManager.setOnOfflineLoadedListener(this);
        initView();
    }

    private void initView() {
        List<OfflineMapProvince> provinces = mOfflineMapManager.getOfflineMapProvinceList();
        mAdapter = new OfflineMapAdapter(provinces);
        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                    long id) {
                OfflineMapProvince province = mAdapter.getGroup(groupPosition);
                if(province.getCityList().size() == 1) {
                    try {
                        if(province.getState() == OfflineMapStatus.SUCCESS) {
                            mOfflineMapManager.remove(province.getProvinceName());
                        }
                        else {
                            mOfflineMapManager.downloadByProvinceName(province.getProvinceName());
                        }
                    } catch (AMapException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                    int childPosition, long id) {
                OfflineMapCity city = mAdapter.getChild(groupPosition, childPosition);
                try {
                    if(city.getState() == OfflineMapStatus.SUCCESS) {
                        mOfflineMapManager.remove(city.getCity());
                    }
                    else {
                        mOfflineMapManager.downloadByCityName(city.getCity());
                    }
                } catch (AMapException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public void onDownload(int i, int i1, String s) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckUpdate(boolean b, String s) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemove(boolean b, String s, String s1) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVerifyComplete() {
        mAdapter.notifyDataSetChanged();
    }

    class OfflineMapAdapter extends BaseExpandableListAdapter {
        private List<OfflineMapProvince> mProvinces;

        public OfflineMapAdapter(List<OfflineMapProvince> provinces) {
            mProvinces = new ArrayList<OfflineMapProvince>();
            if(provinces != null && provinces.size() > 0) {
                int insertIndex = 0;
                for(OfflineMapProvince province : provinces) {
                    if(province.getCityList().size() > 1) {
                        mProvinces.add(province);
                    }
                    else if("100000".equals(province.getProvinceCode())) {
                        mProvinces.add(0, province);
                        insertIndex++;
                    }
                    else {
                        mProvinces.add(insertIndex++, province);
                    }
                }
            }
        }

        @Override
        public int getGroupCount() {
            return mProvinces == null ? 0 : mProvinces.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            int size = getGroup(groupPosition).getCityList().size();
            return size == 1 ? 0 : size;
        }

        @Override
        public OfflineMapProvince getGroup(int groupPosition) {
            return mProvinces.get(groupPosition);
        }

        @Override
        public OfflineMapCity getChild(int groupPosition, int childPosition) {
            return getGroup(groupPosition).getCityList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private String parseStatus(int status) {
            switch (status) {
                case OfflineMapStatus.CHECKUPDATES:
                    return "默认状态";
                case OfflineMapStatus.ERROR:
                    return "解压失败";
                case OfflineMapStatus.EXCEPTION_AMAP:
                    return "AMap认证等异常";
                case OfflineMapStatus.EXCEPTION_NETWORK_LOADING:
                    return "网络问题";
                case OfflineMapStatus.EXCEPTION_SDCARD:
                    return "SD卡读写异常";
                case OfflineMapStatus.LOADING:
                    return "正在下载";
                case OfflineMapStatus.NEW_VERSION:
                    return "有更新";
                case OfflineMapStatus.PAUSE:
                    return "暂停下载";
                case OfflineMapStatus.START_DOWNLOAD_FAILD:
                    return "开始下载失败";
                case OfflineMapStatus.STOP:
                    return "停止下载";
                case OfflineMapStatus.SUCCESS:
                    return "已下载";
                case OfflineMapStatus.UNZIP:
                    return "正在解压";
                case OfflineMapStatus.WAITING:
                    return "等待下载";
            }
            return null;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(OfflineMapActivity.this).inflate(R.layout.group_item_province, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.size = (TextView) convertView.findViewById(R.id.size);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            OfflineMapProvince province = getGroup(groupPosition);
            if(province.getCityList().size() == 1) {
                OfflineMapCity city = province.getCityList().get(0);
                holder.name.setText(city.getCity() + "(" + parseStatus(city.getState()) + ")");
                holder.size.setText(String.format("%.02fMB", city.getSize() / (1024f * 1024f)));
                holder.size.setVisibility(View.VISIBLE);
                if(province.getState() == OfflineMapStatus.SUCCESS) {
                    holder.icon.setImageResource(R.drawable.ic_cloud_done);
                }
                else {
                    holder.icon.setImageResource(R.drawable.ic_cloud_download);
                }
            }
            else {
                holder.name.setText(province.getProvinceName() + "(" + parseStatus(province.getState()) + ")");
                holder.size.setVisibility(View.GONE);
                holder.icon.setImageResource(R.drawable.ic_arrow_drop_down);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(OfflineMapActivity.this).inflate(R.layout.list_item_city, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.size = (TextView) convertView.findViewById(R.id.size);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            OfflineMapCity city = getChild(groupPosition, childPosition);
            holder.name.setText(city.getCity() + "(" + parseStatus(city.getState()) + ")");
            holder.size.setText(String.format("%.02fMB", city.getSize() / (1024f * 1024f)));
            holder.size.setVisibility(View.VISIBLE);
            if(city.getState() == OfflineMapStatus.SUCCESS) {
                holder.icon.setImageResource(R.drawable.ic_cloud_done);
            }
            else {
                holder.icon.setImageResource(R.drawable.ic_cloud_download);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class ViewHolder {
        TextView name;
        TextView size;
        ImageView icon;
    }
}
