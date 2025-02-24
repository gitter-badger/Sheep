package cn.neday.sheep.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.blankj.utilcode.util.RomUtils;
import com.blankj.utilcode.util.Utils;

import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/05/20
 *     desc  : utils about app store
 * </pre>
 */
public class AppStoreUtils {

    private static final String TAG = "AppStoreUtils";

    private static final String GOOGLE_PLAY_APP_STORE_PACKAGE_NAME = "com.android.vending";

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @return 跳转到应用商店的 Intent
     */
    public static Intent getAppStoreIntent() {
        return getAppStoreIntent(Utils.getApp().getPackageName(), false);
    }

    /**
     * 获取跳转到应用商店的 Intent
     * <p>优先跳转到手机自带的应用市场</p>
     *
     * @param packageName              包名
     * @param isIncludeGooglePlayStore 是否包括 Google Play 商店
     * @return 跳转到应用商店的 Intent
     */
    private static Intent getAppStoreIntent(final String packageName, boolean isIncludeGooglePlayStore) {
        // 三星单独处理跳转三星市场
        if (RomUtils.isSamsung()) {
            Intent samsungAppStoreIntent = getSamsungAppStoreIntent(packageName);
            if (samsungAppStoreIntent != null) return samsungAppStoreIntent;
        }
        // 乐视单独处理跳转乐视市场
        if (RomUtils.isLeeco()) {
            Intent leecoAppStoreIntent = getLeecoAppStoreIntent(packageName);
            if (leecoAppStoreIntent != null) return leecoAppStoreIntent;
        }

        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> resolveInfos = Utils.getApp().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos.size() == 0) {
            Log.e(TAG, "No app store!");
            return null;
        }
        Intent googleIntent = null;
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.packageName;
            if (!GOOGLE_PLAY_APP_STORE_PACKAGE_NAME.equals(pkgName)) {
                if (isAppSystem(pkgName)) {
                    intent.setPackage(pkgName);
                    return intent;
                }
            } else {
                intent.setPackage(GOOGLE_PLAY_APP_STORE_PACKAGE_NAME);
                googleIntent = intent;
            }
        }
        if (isIncludeGooglePlayStore && googleIntent != null) {
            return googleIntent;
        }

        intent.setPackage(resolveInfos.get(0).activityInfo.packageName);
        return intent;
    }

    private static Intent getSamsungAppStoreIntent(final String packageName) {
        Intent intent = new Intent();
        intent.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        intent.setData(Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getAvailableIntentSize(intent) > 0) {
            return intent;
        }
        return null;
    }

    private static Intent getLeecoAppStoreIntent(final String packageName) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getAvailableIntentSize(intent) > 0) {
            return intent;
        }
        return null;
    }

    private static int getAvailableIntentSize(final Intent intent) {
        return Utils.getApp().getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size();
    }

    private static boolean isAppSystem(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}