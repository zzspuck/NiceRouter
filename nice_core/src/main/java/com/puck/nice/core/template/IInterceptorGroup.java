package com.puck.nice.core.template;

import java.util.Map;

/**
 * @author zzs
 * @date
 */
public interface IInterceptorGroup {

    /**
     * key为拦截器的优先级，value为拦截器
     * @author zzs
     * @param map
     */
    void loadInto(Map<Integer, Class<? extends IInterceptor>> map);
}
