package com.puck.nice.core.template;

import com.puck.nice.annotation.modle.RouteMeta;

import java.util.Map;

/**
 *
 * @author zzs
 */
public interface IRouteGroup {
    void loadInto(Map<String, RouteMeta> atlas);
}
