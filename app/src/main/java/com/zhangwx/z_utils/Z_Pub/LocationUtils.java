package com.zhangwx.z_utils.Z_Pub;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.Z_Permission.PermissionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangwx on 2017/5/12.
 */

public class LocationUtils {

    public static final String TAG = "LocationUtils";

    public static Location getLocation() {
        Location mLocation = null;
        try {
            LocationManager mLocationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
            if (PermissionUtils.checkLocationPermission(MyApplication.getContext())) {
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (mLocation == null) {
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return mLocation;
    }


    /**
     * 通过地理位置拿国家码
     * @return
     */
    public static String getCountryCodeByLocation() {
        Location location = getLocation();
        Geocoder geocoder = new Geocoder(MyApplication.getContext(), Locale.getDefault());

        List<Address> addresses = null;
        String countryCode = "";
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            countryCode = address.getCountryCode();//国家码
            Log.e(TAG, "getCountryCodeByLocation: ");
            Log.e(TAG, "  CountryName = " + address.getCountryName());
            Log.e(TAG, "  AddressLine = " + address.getAddressLine(0));
            Log.e(TAG, "  AddressLine1 = " + address.getAddressLine(1));
            Log.e(TAG, "  AddressLine2 = " + address.getAddressLine(2));
            Log.e(TAG, "  FeatureName = " + address.getFeatureName());
            Log.e(TAG, "  AdminArea = " + address.getAdminArea());
            Log.e(TAG, "  SubAdminArea = " + address.getSubAdminArea());
            Log.e(TAG, "  Locality = " + address.getLocality());
            Log.e(TAG, "  SubLocality = " + address.getSubLocality());
            Log.e(TAG, "  Phone = " + address.getPhone());
            Log.e(TAG, "  PostalCode = " + address.getPostalCode());
            Log.e(TAG, "  Premises = " + address.getPremises());
            Log.e(TAG, "  Thoroughfare = " + address.getThoroughfare());
            Log.e(TAG, "  SubThoroughfare = " + address.getSubThoroughfare());
            Log.e(TAG, "  Url = " + address.getUrl());
            Log.e(TAG, "  MaxAddressLineIndex = " + address.getMaxAddressLineIndex());
            Log.e(TAG, "  countryCode = " + countryCode);
            /**
             * CountryName = 中国
             * AddressLine = 中国
             * ddressLine1 = 北京市朝阳区
             * AddressLine2 = 黄杉木店路186号
             * FeatureName = 186
             * AdminArea = 北京市
             * SubAdminArea = null
             * Locality = null
             * SubLocality = null
             * Phone = null
             * PostalCode = null
             * Premises = null
             * Thoroughfare = 黄杉木店路
             * SubThoroughfare = 186
             * Url = null
             * MaxAddressLineIndex = 3
             * countryCode = CN
             */
        }
        return countryCode;
    }
}
