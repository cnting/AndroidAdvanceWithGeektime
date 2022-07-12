package com.sample.battery.gps;

import android.content.Context;
import android.util.Log;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.ProxyHook;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by cnting on 2022/7/12
 */
public class GPSHook extends ProxyHook {
    public GPSHook(Context context) {
        super(context);
    }

    @Override
    public BaseHookHandle createBaseHookHandle() {
        return new GPSHookHandle();
    }

    @Override
    public void onInstall() {
        //拿到的是 LocationManager
        Object oldObj = context.getSystemService(Context.LOCATION_SERVICE);
        Class<?> clazz = oldObj.getClass();

        try {
            Field field = clazz.getDeclaredField("mService");
            field.setAccessible(true);
            //拿到 LocationManager.mService的值，也就是 LocationManagerService
            final Object service = field.get(oldObj);
            setProxyObj(service);

            Log.d("===>","service.getClass().getInterfaces():"+ Arrays.toString(service.getClass().getInterfaces()));
            //service.getClass().getInterfaces()拿到的是 IPowerManager
            Object newObj = Proxy.newProxyInstance(this.getClass().getClassLoader(), service.getClass().getInterfaces(), this);
            field.set(oldObj, newObj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
