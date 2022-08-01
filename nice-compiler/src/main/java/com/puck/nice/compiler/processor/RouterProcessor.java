package com.puck.nice.compiler.processor;

import com.google.auto.service.AutoService;
import com.puck.nice.annotation.Route;
import com.puck.nice.compiler.utils.Constant;
import com.puck.nice.compiler.utils.Log;
import com.puck.nice.compiler.utils.Utils;

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
import javax.lang.model.element.TypeElement;
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

    }
}
