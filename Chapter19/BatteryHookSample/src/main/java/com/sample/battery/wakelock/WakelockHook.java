package com.sample.battery.wakelock;

import android.content.Context;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.ProxyHook;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by cnting on 2022/7/12
 */
public class WakelockHook extends ProxyHook {

    public WakelockHook(Context context) {
        super(context);
    }

    @Override
    public BaseHookHandle createBaseHookHandle() {
        return new WakelockHookHandle();
    }

    @Override
    public void onInstall() {
        //拿到的是 PowerManager
        Object oldObj = context.getSystemService(Context.POWER_SERVICE);
        Class<?> clazz = oldObj.getClass();

        try {
            Field field = clazz.getDeclaredField("mService");
            field.setAccessible(true);
            //拿到 PowerManager.mService的值，也就是 PowerManagerService
            final Object service = field.get(oldObj);
            setProxyObj(service);
            Object newObj = Proxy.newProxyInstance(this.getClass().getClassLoader(), service.getClass().getInterfaces(), this);
            field.set(oldObj, newObj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
