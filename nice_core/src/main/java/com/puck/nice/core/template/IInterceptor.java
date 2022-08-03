package com.puck.nice.core.template;


import android.content.Context;

/**
 * @author: zzs
 * @desc:
 */
public interface IInterceptor {
    
    /**
     * 拦截器流程
     * @author zzs
     */
    void process(Postcard postcard, InterceptorCallback callback);

    /**
     * 在调用EasyRouter.init()初始化时，会调用到此方法
     * @author zzs
     */
    void init(Context context);
}
