package com.puck.nice.compiler.processor;

import com.google.auto.service.AutoService;
import com.puck.nice.annotation.Route;
import com.puck.nice.annotation.modle.RouteMeta;
import com.puck.nice.compiler.utils.Constant;
import com.puck.nice.compiler.utils.Log;
import com.puck.nice.compiler.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author: zzs
 * @date: 2022/6/9
 */
@AutoService(Processor.class)

/**
 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(Constant.ARGUMENTS_NAME)

/**
 * 注册给哪些注解的  替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 */
@SupportedAnnotationTypes(Constant.ANNOTATION_TYPE_ROUTE)
public class RouterProcessor extends AbstractProcessor {
    private Log log;

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;
    /**
     * type(类信息)工具类
     */
    private Types typeUtils;
    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;
    private String moduleName;
    /**
     * 分组 key:组名 value:对应组的路由信息
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        log = Log.newLog(processingEnv.getMessager());
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        filerUtils = processingEnv.getFiler();

        Map<String, String> options = processingEnv.getOptions();
        if (!Utils.isEmpty(options)) {
            moduleName = options.get(Constant.ARGUMENTS_NAME);
        }

        if (Utils.isEmpty(moduleName)) {
            throw new RuntimeException("NOT set processor moduleName option !");
        }
        log.i("init RouterProcessor " + moduleName + "success !");
    }

    /**
     * @param set              使用了支持处理注解的节点集合
     * @param roundEnvironment 表示当前或者之前的运行环境，可以通过该对象查找找到的注解
     * @return true 表示后续处理器不会再处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Utils.isEmpty(set)) {
            //被Route注解的节点集合
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
            if (!Utils.isEmpty(elementsAnnotatedWith)) {
                processRouter(elementsAnnotatedWith);
            }
            return true;
        }
        return false;
    }

    private void processRouter(Set<? extends Element> elementsAnnotatedWith) {

        TypeElement activity = elementUtils.getTypeElement(Constant.ACTIVITY);
        TypeElement service = elementUtils.getTypeElement(Constant.ISERVICE);
        for (Element element : elementsAnnotatedWith) {
            RouteMeta routeMeta;
            // 类信息
            TypeMirror typeMirror = element.asType();
            log.i("Router class: " + typeMirror.toString());
            Route route = element.getAnnotation(Route.class);
            if (typeUtils.isSubtype(typeMirror, activity.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
            } else if (typeUtils.isSubtype(typeMirror, service.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ISERVICE, route, element);
            } else {
                throw new RuntimeException("just support Activity or Iservice Route: ");
            }

            categories(routeMeta);
        }

        TypeElement iRouteGroup = elementUtils.getTypeElement(Constant.IROUTE_GROUP);
        TypeElement iRouteRoot = elementUtils.getTypeElement(Constant.IROUTE_ROOT);
        // 生成Group记录分组表
        generatedGroup(iRouteGroup);
    }

    private void generatedGroup(TypeElement iRouteGroup) {
        // 创建参数类型 Map<String, RouteMeta>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));
        ParameterSpec atlas = ParameterSpec.builder(parameterizedTypeName, "atlas").build();
        //  void loadInto(Map<String, RouteMeta> atlas);
        for (Map.Entry<String, List<RouteMeta>> entry: groupMap.entrySet()){
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constant.METHOD_LOAD_INTO)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(atlas);

            String groupName = entry.getKey();
            List<RouteMeta> groupData = entry.getValue();
            for (RouteMeta routeMeta:groupData){
                // 函数体添加
                methodBuilder.addStatement("atlas.put(" +
                                ",$T.build($T.$L,$T.class,$S,$S))",
                        routeMeta.getGroup(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(),
                        ClassName.get(((TypeElement) routeMeta.getElement())),
                        routeMeta.getPath(),
                        routeMeta.getGroup());
            }

        }
    }

    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            log.i("Group : " + routeMeta.getGroup() + " path=" + routeMeta.getPath());
            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
            if (Utils.isEmpty(routeMetas)) {
                routeMetas = new ArrayList<>();
                routeMetas.add(routeMeta);
                groupMap.put(routeMeta.getGroup(), routeMetas);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            log.i("Group info error:" + routeMeta.getPath());
        }
    }

    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath();
        String group = routeMeta.getGroup();
        // 必须以/ 开始来指示路由地址
        if (!path.startsWith("/")) {
            return false;
        }
        //如果group没有设置 我们从path中获得group
        if (Utils.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (Utils.isEmpty(defaultGroup)) {
                return false;
            }
            routeMeta.setGroup(defaultGroup);
        }
        return true;
    }
}
