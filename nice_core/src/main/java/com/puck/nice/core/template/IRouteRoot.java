package com.puck.nice.core.template;

import java.util.Map;


/**
 * @author zzs
 */
public interface IRouteRoot {
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
