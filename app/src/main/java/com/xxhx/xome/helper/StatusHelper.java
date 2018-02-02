package com.xxhx.xome.helper;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import com.xxhx.xome.R;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.ui.activity.WebViewActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xxhx.xome.config.Constants.*;

/**
 * Created by xxhx on 2016/10/24.
 */

public class StatusHelper {
    private static final String URL_PATTERN_SQUARE = "http://ww1.sinaimg.cn/thumb180/%1$s";
    private static final String URL_PATTERN_THUMB = "http://ww2.sinaimg.cn/wap360/%1$s";
    private static final String URL_PATTERN_BMIDDLE = "http://ww3.sinaimg.cn/mw690/%1$s";
    private static final String URL_PATTERN_LARGE = "http://ww4.sinaimg.cn/large/%1$s";

    public static void testLinkify(TextView tv, String text) {
        tv.setText(text);
        String urlPattern = "((http|https)://)((([a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)*[a-zA-Z]([a-zA-Z0-9-]*[a-zA-Z0-9])?)|([0-9]+(\\.[0-9]+){3})(\\:[0-9]+)?)(\\/([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\:\\@\\&\\=]|(%[0-9a-fA-F]{2}))*(\\/([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\:\\@\\&\\=]|(%[0-9a-fA-F]{2}))*)*(\\?([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\:\\@\\&\\=]|(%[0-9a-fA-F]{2}))*)?)?";
        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Linkify.addLinks(tv, pattern, "http://", new Linkify.MatchFilter() {
            @Override
            public boolean acceptMatch(CharSequence s, int start, int end) {
                return true;
            }
        }, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return url.substring(2);
            }
        });
    }

    public static String getThumbPicUrl(String key) {
        return String.format(URL_PATTERN_THUMB, key);
    }

    public static List<String> getThumbPicUrls(List<String> keys) {
        List<String> urls = new ArrayList<String>();
        for(String key : keys) {
            urls.add(getThumbPicUrl(key));
        }
        return urls;
    }

    public static String getSquarePicUrl(String key) {
        return String.format(URL_PATTERN_SQUARE, key);
    }

    public static List<String> getSquarePicUrls(List<String> keys) {
        List<String> urls = new ArrayList<String>();
        for(String key : keys) {
            urls.add(getSquarePicUrl(key));
        }
        return urls;
    }

    public static String getLargePicUrl(String key) {
        return String.format(URL_PATTERN_LARGE, key);
    }

    public static List<String> getLargePicUrls(List<String> keys) {
        List<String> urls = new ArrayList<String>();
        for(String key : keys) {
            urls.add(getLargePicUrl(key));
        }
        return urls;
    }

    public static Spannable getDisplayStatusText(Context context, String text) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        //int size = text.length();
        int p = 0;
        int start, end;

        // handle with url
        String urlPattern = "((http|https)://)((([a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)*[a-zA-Z]([a-zA-Z0-9-]*[a-zA-Z0-9])?)|([0-9]+(\\.[0-9]+){3})(\\:[0-9]+)?)(\\/([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\:\\@\\&\\=]|(%[0-9a-fA-F]{2}))*(\\/([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\:\\@\\&\\=]|(%[0-9a-fA-F]{2}))*)*(\\?([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\:\\@\\&\\=]|(%[0-9a-fA-F]{2}))*)?)?";
        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find(p)) {
            start = matcher.start();
            end = matcher.end();
            spannable.setSpan(new UrlClickableSpan(context, text.substring(start, end)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //spannable.setSpan(createUrlSpan(context, textIn.substring(start, end)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //spannable.setSpan(new CustomClickableSpan(context, textIn.substring(start, end), CustomClickableSpan.TYPE_URL), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            p = end;
        }

        // handle with topic
        String topicPattern = "\\#[^\\#]+?\\#";
        pattern = Pattern.compile(topicPattern);
        matcher = pattern.matcher(text);
        p = 0;
        while(matcher.find(p)) {
            start = matcher.start();
            end = matcher.end();
            spannable.setSpan(UrlClickableSpan.newTopicSpan(context, text.substring(start, end)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //spannable.setSpan(new CustomClickableSpan(context, textIn.substring(start, end), CustomClickableSpan.TYPE_TOPIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            p = end;
        }

        // handle with mention
        String mentionPattern = "\\@[a-zA-Z0-9\\-\\_\u4e00-\u9fa5]+";
        pattern = Pattern.compile(mentionPattern);
        matcher = pattern.matcher(text);
        p = 0;
        while(matcher.find(p)) {
            start = matcher.start();
            end = matcher.end();
            spannable.setSpan(UrlClickableSpan.newMentionSpan(context, text.substring(start, end)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //spannable.setSpan(new CustomClickableSpan(context, textIn.substring(start, end), CustomClickableSpan.TYPE_MENTION), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            p = end;
        }

        return spannable;
    }

    static class UrlClickableSpan extends ClickableSpan {
        private static final int TYPE_URL = 0x00;
        private static final int TYPE_MENTION = 0x01;
        private static final int TYPE_TOPIC = 0x02;

        private Context mContext;
        private String mUrl;
        private int mType;

        private UrlClickableSpan(Context context, String url, int type) {
            mContext = context;
            mUrl = url;
            mType = type;
        }

        public static UrlClickableSpan newMentionSpan(Context context, String name) {
            String url = String.format(URL_PATTERN_MENTION, name.substring(1));
            return new UrlClickableSpan(context, url, TYPE_MENTION);
        }

        public static UrlClickableSpan newTopicSpan(Context context, String topic) {
            String url = String.format(URL_PATTERN_TOPIC, topic.substring(1, topic.length()-1));
            return new UrlClickableSpan(context, url, TYPE_TOPIC);
        }

        public UrlClickableSpan(Context context, String url) {
            this(context, url, TYPE_URL);
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra(WebViewActivity.EXTRA_URL, mUrl);
            mContext.startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            switch (mType) {
                case TYPE_URL:
                    ds.setColor(ds.linkColor);
                    ds.setUnderlineText(true);
                    ds.setTextSkewX(-0.15f);
                    break;
                case TYPE_MENTION:
                    ds.setColor(ds.linkColor);
                    break;
                case TYPE_TOPIC:
                    ds.setColor(ds.linkColor);
                    break;
            }
        }
    }
}
