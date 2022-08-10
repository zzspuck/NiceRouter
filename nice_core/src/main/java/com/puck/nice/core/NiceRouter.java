package com.puck.nice.core;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.puck.nice.core.implments.InterceptorImpl;
import com.puck.nice.core.template.IInterceptorGroup;
import com.puck.nice.core.template.IRouteRoot;
import com.puck.nice.core.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @author: zzs
 * @date: 2022/8/11
 */

class NiceRouter {
    private static final String TAG = "NiceRouter";
    private static final String ROUTE_ROOT_PAKCAGE = "com.puck.nicerouter.routes";
    private static final String SDK_NAME = "NiceRouter";
    private static final String SEPARATOR = "_";
    private static final String SUFFIX_ROOT = "Root";
    private static final String SUFFIX_INTERCEPTOR = "Interceptor";

    private static volatile NiceRouter sInstance;
    private static Application mContext;
    private Handler mHandler;

    private NiceRouter(){
        mHandler = new Handler(Looper.getMainLooper())
    }

    public static NiceRouter getInstance(){
        if (sInstance==null) {
            synchronized (NiceRouter.class){
                if (sInstance==null){
                    sInstance = new NiceRouter();
                }
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        mContext = application;
        try {
            loadInfo();
            InterceptorImpl.init(application.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "初始化失败!", e);
        }
    }

    /**
     * 分组表制作
     */
    private static void loadInfo() throws PackageManager.NameNotFoundException, InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //获得所有 apt生成的路由类的全类名 (路由表)
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(mContext, ROUTE_ROOT_PAKCAGE);
        for (String className : routerMap) {
            if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_ROOT)) {
                //root中注册的是分组信息 将分组信息加入仓库中
                ((IRouteRoot) Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.groupsIndex);
            } else if (className.startsWith(ROUTE_ROOT_PAKCAGE + "." + SDK_NAME + SEPARATOR + SUFFIX_INTERCEPTOR)) {

                ((IInterceptorGroup) Class.forName(className).getConstructor().newInstance()).loadInto(Warehouse.interceptorsIndex);
            }
        }
        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry : Warehouse.groupsIndex.entrySet()) {
            Log.d(TAG, "Root映射表[ " + stringClassEntry.getKey() + " : " + stringClassEntry.getValue() + "]");
        }

    }
}
