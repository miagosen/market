package com.lx.market;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lx.market.config.MarketConfig;
import com.lx.market.constants.FileConstants;
import com.lx.market.constants.GlobalConstants;
import com.lx.market.model.DownloadInfo;
import com.lx.market.network.model.ChannelInfoBto;
import com.lx.market.network.model.Session;
import com.lx.market.network.protocol.GetMarketFrameResp;
import com.lx.market.utils.Logger;
import com.lx.market.utils.TDevice;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.List;

import market.lx.com.R;

/**
 * Created by Antikvo.Miao on 2014/7/23.
 */
public class MarketApplication extends Application {
    private static MarketApplication mInstance;
    public static Handler handler = new Handler();
    public List<DownloadInfo> downloadInfoList;
    public DbUtils dbUtils;
    // 记录当前 Context
    public static Context curContext;
    public ImageLoader imageLoader;
    private Session session;
    public GetMarketFrameResp marketFrameResp;
    private SharedPreferences sharedPreferences;
    private static String lastToast = "";
    private static long lastToastTime;
    public List<ChannelInfoBto> channelInfoBtos;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        curContext = this;
        initUtils();
        initImageLoader();
        initSession();
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        sharedPreferences = this.getSharedPreferences("config", 0);
    }

    public SharedPreferences getPreferences() {
        return sharedPreferences;
    }

    public static void showToast(int message) {
        showToast(message, Toast.LENGTH_LONG, 0);
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_LONG, 0, Gravity.FILL_HORIZONTAL | Gravity.TOP);
    }

    public static void showToast(int message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon);
    }

    public static void showToast(String message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon, Gravity.FILL_HORIZONTAL | Gravity.TOP);
    }

    public static void showToastShort(int message) {
        showToast(message, Toast.LENGTH_SHORT, 0);
    }

    public static void showToastShort(String message) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.FILL_HORIZONTAL | Gravity.TOP);
    }

    public static void showToastShort(int message, Object... args) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.FILL_HORIZONTAL | Gravity.TOP, args);
    }

    public static void showToast(int message, int duration, int icon) {
        showToast(message, duration, icon, Gravity.FILL_HORIZONTAL | Gravity.TOP);
    }

    public static void showToast(int message, int duration, int icon, int gravity) {
        showToast(getInstance().getString(message), duration, icon, gravity);
    }

    public static void showToast(int message, int duration, int icon, int gravity, Object... args) {
        showToast(getInstance().getString(message, args), duration, icon, gravity);
    }

    public static void showToast(String message, int duration, int icon, int gravity) {
        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast) || Math.abs(time - lastToastTime) > 2000) {
                View view = LayoutInflater.from(getInstance()).inflate(R.layout.oc_view_toast, null);
                ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                if (icon != 0) {
                    ((ImageView) view.findViewById(R.id.icon_iv)).setImageResource(icon);
                    ((ImageView) view.findViewById(R.id.icon_iv)).setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(getInstance());
                toast.setView(view);
                toast.setGravity(gravity, 0, TDevice.getActionBarHeight(getInstance()));
                // getToastMarignBottom()
                // toast.setGravity(Gravity.TOP|Gravity.LEFT,0 ,0);
                toast.setDuration(duration);
                toast.show();

                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }

    private void initSession() {
        try {
            session = dbUtils.findFirst(Session.class);
        } catch (DbException e) {
            Logger.p(e);
        }
    }

    public synchronized Session getSession() {
        if (session == null) {
            try {
                session = dbUtils.findFirst(Session.class);
            } catch (DbException e) {
                Logger.p(e);
            }
        }
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, FileConstants.IMAGE_PATH);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_icon).cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        builder.diskCacheSize(FileConstants.DISK_CACHE_SIZE).diskCacheFileCount(400).memoryCache(new LruMemoryCache(1024 * 1024)).memoryCacheSize(3 * 1024 * 1024)
                .threadPoolSize(5).defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY - 1);
        builder.diskCache(new UnlimitedDiscCache(cacheDir));
        ImageLoaderConfiguration configuration = builder.build();
        imageLoader.init(configuration);
    }

    private void initUtils() {
        dbUtils = DbUtils.create(this, FileConstants.DB_NAME, GlobalConstants.DB_VERSION, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                if (oldVersion <= newVersion) {
                    try {
                        dbUtils.dropTable(DownloadInfo.class);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dbUtils.configDebug(MarketConfig.getInstance().isDebugMode());
    }

    public static boolean isDownloadWIFIOnly() {
        // String wifiSettingString =
        // DataStoreUtils.readLocalInfo(DataStoreUtils.DOWNLOAD_WIFI_ONLY);
        // if (null != wifiSettingString) {
        // return
        // wifiSettingString.equals(DataStoreUtils.DOWNLOAD_WIFI_ONLY_ENABLE);
        // }
        // return false;
        SharedPreferences sp = MarketApplication.curContext.getSharedPreferences("setting", 0);
        boolean isWifidownloadNotice = sp.getBoolean("no_wifi", false);
        return isWifidownloadNotice;
    }

    public static MarketApplication getInstance() {
        return mInstance;
    }

}
