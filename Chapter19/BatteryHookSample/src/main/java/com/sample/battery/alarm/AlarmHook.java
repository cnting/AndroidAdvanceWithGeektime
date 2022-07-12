package com.sample.battery.alarm;

import android.content.Context;

import com.sample.battery.hook.BaseHookHandle;
import com.sample.battery.hook.ProxyHook;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by cnting on 2022/7/12
 */
public class AlarmHook extends ProxyHook {

    public AlarmHook(Context context) {
        super(context);
    }

    @Override
    public BaseHookHandle createBaseHookHandle() {
        return new AlarmHookHandle();
    }

    @Override
    public void onInstall() {
        //拿到的是 AlarmManager
        Object oldObj = context.getSystemService(Context.ALARM_SERVICE);
        Class<?> clazz = oldObj.getClass();

        try {
            Field field = clazz.getDeclaredField("mService");
            field.setAccessible(true);
            //拿到 AlarmManager.mService的值，也就是 AlarmManagerService
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
