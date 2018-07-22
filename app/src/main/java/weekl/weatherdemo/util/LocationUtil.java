package weekl.weatherdemo.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * 定位功能工具类
 */
public class LocationUtil {
    private static final String TAG = "LocationUtil";

    public interface LocationCallback {
        void onSuccess(Location location);

        void onFailed(String msg);
    }

    public static void getLocation(Context context, final LocationCallback callback) {
        //获取定位管理器
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //判断权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "getLocation: 权限不足");
            return;
        }
        if (locationManager != null) {
            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationManager.removeUpdates(this);
                    Log.d(TAG, "onLocationChanged: " + location);
                    if (location != null) {
                        Log.d(TAG, "onLocationChanged: " + location);
                        callback.onSuccess(location);
                    } else {
                        callback.onFailed("定位失败!");
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            Criteria crite = new Criteria();
            crite.setAccuracy(Criteria.ACCURACY_FINE); //精度
            crite.setPowerRequirement(Criteria.POWER_LOW); //功耗类型选择
            String provider = locationManager.getBestProvider(crite, true);
            if (provider != null) {
                locationManager.requestLocationUpdates(provider, 0, 0, listener);
                Log.d(TAG, "getLocation: 使用Provider ：" + provider);
            }else {
                Log.e(TAG, "getLocation: 没有可用的Provider");
            }
        } else {
            Log.e(TAG, "getLocation: 没有定位管理器！");
        }
    }
}
