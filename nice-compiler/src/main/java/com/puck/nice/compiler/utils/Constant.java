package com.puck.nice.compiler.utils;

/**
 * @author: zzs
 * @date: 2022/6/9
 */

public class Constant {

    public static final String ACTIVITY = "android.app.Activity";
    public static final String ISERVICE = "com.puck.nice.core.template.IService";

    public static final String ARGUMENTS_NAME = "moduleName";
    public static final String ANNOTATION_TYPE_ROUTE = "com.puck.nice.annotation.Route";
    public static final String ANNOTATION_TYPE_INTERCEPTOR = "com.puck.nice.annotation.Interceptor";

    public static final String IROUTE_GROUP = "com.puck.nice.core.template.IRouteGroup";
    public static final String IROUTE_ROOT = "com.puck.nice.core.template.IRouteRoot";

    public static final String IINTERCEPTOR = "com.puck.nice.core.template.IInterceptor";
    public static final String IINTERCEPTOR_GROUP = "com.puck.nice.core.template.IInterceptorGroup";

    public static final String METHOD_LOAD_INTO = "loadInto";
    public static final String METHOD_LOAD_EXTRA = "loadExtra";

    public static final String SEPARATOR = "_";
    public static final String PROJECT = "NiceRouter";
    public static final String NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR;
    public static final String NAME_OF_ROOT = PROJECT + SEPARATOR + "Root" + SEPARATOR;
    public static final String PACKAGE_OF_GENERATE_FILE = "com.puck.nicerouter.routes";

    public static final String NAME_OF_EXTRA = SEPARATOR + "Extra";
    public static final String NAME_OF_INTERCEPTOR = PROJECT + SEPARATOR + "Interceptor" + SEPARATOR;
}
