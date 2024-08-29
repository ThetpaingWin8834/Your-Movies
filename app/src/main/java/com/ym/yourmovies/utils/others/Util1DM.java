package com.ym.yourmovies.utils.others;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.ym.yourmovies.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Util1DM {
    public static final String PACKAGE_NAME_1DM_PLUS = "idm.internet.download.manager.plus";
    public static final String PACKAGE_NAME_1DM_NORMAL = "idm.internet.download.manager";
    public static final String PACKAGE_NAME_1DM_LITE = "idm.internet.download.manager.adm.lite";
    public static final String DOWNLOADER_ACTIVITY_NAME_1DM = "idm.internet.download.manager.Downloader";
    public static final int SECURE_URI_1DM_SUPPORT_MIN_VERSION_CODE = 169;
    public static final int HEADERS_AND_MULTIPLE_LINKS_1DM_SUPPORT_MIN_VERSION_CODE = 157;
    public static final String GOOGLE_PLAY_STORE_SCHEMA = "market://details?id=";
    public static final String HUAWEI_APP_GALLERY_SCHEMA = "appmarket://details?id=";
    public static final String GOOGLE_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    public static final String EXTRA_SECURE_URI = "secure_uri";
    public static final String EXTRA_COOKIES = "extra_cookies";
    public static final String EXTRA_USERAGENT = "extra_useragent";
    public static final String EXTRA_REFERER = "extra_referer";
    public static final String EXTRA_HEADERS = "extra_headers";
    public static final String EXTRA_FILENAME = "extra_filename";
    public static final String EXTRA_URL_LIST = "url_list";
    public static final String EXTRA_URL_FILENAME_LIST = "url_list.filename";
    public static final String MESSAGE_INSTALL_1DM = "To download content install 1DM";
    public static final String MESSAGE_UPDATE_1DM = "To download content update 1DM";

    /*
	 Note 1) If you want to download torrent file from local storage add the below file provider in Manifest file
	 <provider
	 android:name="androidx.core.content.FileProvider"
	 android:authorities="${applicationId}.provider"
	 android:exported="false"
	 android:grantUriPermissions="true">
	 <meta-data
	 android:name="android.support.FILE_PROVIDER_PATHS"
	 android:resource="@xml/provider_paths" />
	 </provider>

	 Then create provider_paths file in xml folder with below content
	 <?xml version="1.0" encoding="utf-8"?>
	 <paths>
	 <external-path
	 name="external_files"
	 path="." />
	 <root-path
	 name="sdcard_files"
	 path="/storage/" />
	 <files-path
	 name="files"
	 path="." />
	 <cache-path
	 name="cache"
	 path="." />
	 </paths>

	 Note 2) If you're targeting Android 11+ (targetSdkVersion is 30+) then add below queries in manifest file. More details on https://developer.android.com/training/package-visibility
	 <queries>
	 <package android:name="idm.internet.download.manager" />
	 <package android:name="idm.internet.download.manager.adm.lite" />
	 <package android:name="idm.internet.download.manager.plus" />
	 </queries>

	 Or if you app is not on Play store use below permission
	 android.permission.QUERY_ALL_PACKAGES
     */

    public enum AppState {OK, UPDATE_REQUIRED, NOT_INSTALLED}

    public static void downloadTorrent(@NonNull Activity activity, @NonNull String torrentUrl, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, torrentUrl, null, null, null, null, null, false, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadTorrent(@NonNull Activity activity, @NonNull String torrentUrl, @Nullable Map<String, String> headers, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, torrentUrl, null, null, null, null, headers, false, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadMagnet(@NonNull Activity activity, @NonNull String magnetUrl, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, magnetUrl, null, null, null, null, null, false, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadFile(@NonNull Activity activity, @NonNull String url, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, url, null, null, null, null, null, secureUri, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadFile(@NonNull Activity activity, @NonNull String url, @Nullable Map<String, String> headers, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, url, null, null, null, null, headers, secureUri, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadFile(@NonNull Activity activity, @NonNull String url, @Nullable String referer, @Nullable String fileName, @Nullable String userAgent, @Nullable String cookies, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, url, referer, fileName, userAgent, cookies, null, secureUri, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadFile(@NonNull Activity activity, @NonNull String url, @Nullable String referer, @Nullable String fileName, @Nullable String userAgent, @Nullable String cookies, @Nullable Map<String, String> headers, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, null, url, referer, fileName, userAgent, cookies, headers, secureUri, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadFiles(@NonNull Activity activity, @NonNull Map<String, String> urlAndFileNames, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, urlAndFileNames, null, null, null, null, null, null, secureUri, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadFiles(@NonNull Activity activity, @NonNull Map<String, String> urlAndFileNames, @Nullable Map<String, String> headers, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadFilesInternal(activity, urlAndFileNames, null, null, null, null, null, headers, secureUri, askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadTorrentFile(@NonNull Activity activity, @NonNull File torrent, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        downloadTorrentFile(activity, torrent, activity.getPackageName() + ".provider", askUserToInstall1DMIfNotInstalled);
    }

    public static void downloadTorrentFile(@NonNull Activity activity, @NonNull File torrentFile, @NonNull String authority, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        String packageName = get1DMInstalledPackageName(activity, 0, askUserToInstall1DMIfNotInstalled);
        if (TextUtils.isEmpty(packageName))
            return;
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            uri = FileProvider.getUriForFile(activity, authority, torrentFile);
        else
            uri = Uri.fromFile(torrentFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName(packageName, DOWNLOADER_ACTIVITY_NAME_1DM));
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(intent);
    }

    private static void downloadFilesInternal(@NonNull Activity activity, @Nullable Map<String, String> urlAndFileNames, @Nullable String url, @Nullable String referer, @Nullable String fileName, @Nullable String userAgent, @Nullable String cookies, @Nullable Map<String, String> headers, boolean secureUri, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        int requiredVersionCode = secureUri ? SECURE_URI_1DM_SUPPORT_MIN_VERSION_CODE : !isEmpty(urlAndFileNames) || !isEmpty(headers) ? HEADERS_AND_MULTIPLE_LINKS_1DM_SUPPORT_MIN_VERSION_CODE : 0;
        String packageName = get1DMInstalledPackageName(activity, requiredVersionCode, askUserToInstall1DMIfNotInstalled);
        if (TextUtils.isEmpty(packageName))
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName(packageName, DOWNLOADER_ACTIVITY_NAME_1DM));
        intent.putExtra(EXTRA_SECURE_URI, secureUri);
        if (isEmpty(urlAndFileNames)) {
            intent.setData(Uri.parse(url));
            if (!TextUtils.isEmpty(referer))
                intent.putExtra(EXTRA_REFERER, referer);
            if (!TextUtils.isEmpty(userAgent))
                intent.putExtra(EXTRA_USERAGENT, userAgent);
            if (!TextUtils.isEmpty(cookies))
                intent.putExtra(EXTRA_COOKIES, cookies);
            if (!TextUtils.isEmpty(fileName))
                intent.putExtra(EXTRA_FILENAME, fileName);
        } else {
            ArrayList<String> urls = new ArrayList<>(urlAndFileNames.size());
            ArrayList<String> names = new ArrayList<>(urlAndFileNames.size());
            for (Map.Entry<String, String> entry : urlAndFileNames.entrySet()) {
                if (!TextUtils.isEmpty(entry.getKey())) {
                    urls.add(entry.getKey());
                    names.add(entry.getValue());
                }
            }
            if (urls.size() > 0) {
                intent.putExtra(EXTRA_URL_LIST, urls);
                intent.putExtra(EXTRA_URL_FILENAME_LIST, names);
                intent.setData(Uri.parse(urls.get(0)));
            }
        }
        if (!isEmpty(headers)) {
            Bundle extra = new Bundle();
            for (Map.Entry<String, String> entry : headers.entrySet())
                extra.putString(entry.getKey(), entry.getValue());
            intent.putExtra(EXTRA_HEADERS, extra);
        }
        activity.startActivity(intent);
    }

    public static void install1DM(final Activity activity, final String packageName, boolean update) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.title_1dm);
        builder.setMessage(update ? R.string.update_1dm : R.string.install_1dm)
                .setPositiveButton(update ? "Update" : "Install", (dialog, which) -> {
                    try {
                        try {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_SCHEMA + packageName + getStoreTracking(activity))));
                        } catch (ActivityNotFoundException e) {
                            try {
                                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HUAWEI_APP_GALLERY_SCHEMA + packageName + getStoreTracking(activity))));
                            } catch (ActivityNotFoundException e1) {
                                try {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_URL + packageName + getStoreTracking(activity))));
                                } catch (ActivityNotFoundException e2) {
                                    Toast.makeText(activity, e2.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();
    }

    private static <S, T> boolean isEmpty(Map<S, T> map) {
        return map == null || map.size() == 0;
    }

    private static String getStoreTracking(@NonNull Context context) {
        return "&referrer=utm_source%3D" + context.getPackageName() + "%26utm_medium%3DApp%26utm_campaign%3DDownload";
    }

    private static String get1DMInstalledPackageName(@NonNull Activity activity, int requiredVersionCode, boolean askUserToInstall1DMIfNotInstalled) throws Exception {
        PackageManager packageManager = activity.getPackageManager();
        String packageName = PACKAGE_NAME_1DM_PLUS;
        AppState state = get1DMAppState(packageManager, packageName, requiredVersionCode);
        if (state == AppState.NOT_INSTALLED) {
            packageName = PACKAGE_NAME_1DM_NORMAL;
            state = get1DMAppState(packageManager, packageName, requiredVersionCode);
            if (state == AppState.NOT_INSTALLED) {
                packageName = PACKAGE_NAME_1DM_LITE;
                state = get1DMAppState(packageManager, packageName, requiredVersionCode);
                if (state == AppState.NOT_INSTALLED) {
                    if (askUserToInstall1DMIfNotInstalled) {
                        install1DM(activity, PACKAGE_NAME_1DM_NORMAL, false);
                        return null;
                    } else
                        throw new Exception(MESSAGE_INSTALL_1DM);
                }
            }
        }
        if (state == AppState.UPDATE_REQUIRED) {
            if (askUserToInstall1DMIfNotInstalled) {
                install1DM(activity, packageName, true);
                return null;
            } else
                throw new Exception(MESSAGE_UPDATE_1DM);
        }
        return packageName;
    }

    private static AppState get1DMAppState(@NonNull PackageManager packageManager, @NonNull String packageName, int requiredVersion) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (requiredVersion <= 0 || packageInfo.versionCode >= requiredVersion)
                return AppState.OK;
            return AppState.UPDATE_REQUIRED;
        } catch (PackageManager.NameNotFoundException ignore) {
            return AppState.NOT_INSTALLED;
        }
    }
}