package com.crazyorange.spider.processor;

import com.crazyorange.spider.annotation.RouterPage;
import com.crazyorange.spider.annotation.model.ParamType;
import com.crazyorange.spider.processor.contants.Constant;
import com.crazyorange.spider.processor.log.SpiderLog;
import com.squareup.javapoet.ClassName;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.crazyorange.spider.processor.contants.Constant.*;

// 抽象类上不可以使用 AutoService 注解
public abstract class BaseAnnotationProcess extends AbstractProcessor {
    // 文件生成器 生成文件时需要使用到
    protected Filer mFilerUtils;
    // 类型工具类
    protected Types mTypesUtils;
    // 查询元素节点信息的类  https://blog.csdn.net/u010126792/article/details/95614328
    // https://zhuanlan.zhihu.com/p/32340546
    // 在编译时可用，表示的是一个元素的信息:类，包，方法...
    // 在编译期间会扫描源代码类 这时的每一个源代码中的元素都是 Element
    protected Elements mElementsUtils;
    // 编译时打印日志用到
    protected Messager mLogMessageUtils;

    protected TypeMirror mParcelableType;
    protected TypeMirror mSerializableType;
    protected SpiderLog mLogger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFilerUtils = processingEnvironment.getFiler();
        mTypesUtils = processingEnvironment.getTypeUtils();
        mElementsUtils = processingEnvironment.getElementUtils();
        mLogMessageUtils = processingEnvironment.getMessager();
        mLogger = new SpiderLog(mLogMessageUtils);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        // 未使用目标注解 直接返回
        if (unusedTargetAnnotation(set)) {
            return false;
        }
        return processAnnotation(set, env);
    }

    public boolean unusedTargetAnnotation(Set<? extends TypeElement> set) {
        return set.isEmpty();
    }

    // 设置支持的注解类型
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(RouterPage.class.getName()));
    }

    public void processLog(String content) {
        mLogMessageUtils.printMessage(Diagnostic.Kind.NOTE, content);
    }

    /**
     * 是否为 Activity 类型
     * 1. 不是一个抽象类
     * 2. 继承自 Activity
     */
    public boolean isActivity(Element element) {
        return isConcreteSubType(element, Constant.ACTIVITY_TYPE);
    }

    public boolean isFragment(Element element) {
        return isConcreteSubType(element, Constant.FRAGMENT_TYPE);
    }

    public boolean isFragmentV4(Element element) {
        return isConcreteSubType(element, Constant.FRAGMENT_V4_TYPE);
    }

    public boolean isSubType(Element element, String className) {
        return element != null && isSubType(element.asType(), className);
    }

    public boolean isSubType(TypeMirror type, String className) {
        return type != null && mTypesUtils.isSubtype(type, typeMirror(className));
    }

    /**
     * 从字符串获取TypeMirror对象
     * TypeElement 只能获取类本身的信息，无法获取到类的父类相关的信息
     */
    public TypeMirror typeMirror(String className) {
        return typeElement(className).asType();
    }

    /**
     * 从字符串获取TypeElement对象
     */
    public TypeElement typeElement(String className) {
        return mElementsUtils.getTypeElement(className);
    }

    /**
     * 非抽象子类
     */
    public boolean isConcreteSubType(Element element, String className) {
        return isConcreteType(element) && isSubType(element, className);
    }

    public String getElementPkgName(Element element) {
        return mElementsUtils.getPackageOf(element).getQualifiedName().toString();
    }

    public String getElementClassName(Element element) {
        if (element instanceof TypeElement) {
            return element.getSimpleName().toString();
        }
        return "";
    }

    /**
     * 获取ClassName对象 如果引入 Android 或者外部的包作为参数
     * 需要通过 ClassName 让编译器自动加入导包语句
     */
    public ClassName className(String className) {
        return ClassName.get(typeElement(className));
    }

    /**
     * 去指定包下找对应的类
     *
     * @param pkgName   包名
     * @param className 类名
     * @return
     */
    public ClassName className(String pkgName, String className) {
        return ClassName.get(pkgName, className);
    }

    /**
     * 一个具像的类 非抽象类
     */
    public boolean isConcreteType(Element element) {
        // 1.它是一个类 2.类的修饰符不包含 Abstract
        return element instanceof TypeElement && !element.getModifiers().contains(
                Modifier.ABSTRACT);
    }

    // ARouter 中将 Java 的类型对应了 ParamType 中的自定义类型
    public int translateType(Element element) {
        TypeMirror typeMirror = element.asType();
        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case BYTE:
                return ParamType.BYTE.ordinal();
            case SHORT:
                return ParamType.SHORT.ordinal();
            case INTEGER:
                return ParamType.INT.ordinal();
            case LONG:
                return ParamType.LONG.ordinal();
            case FLOAT:
                return ParamType.FLOAT.ordinal();
            case DOUBEL:
                return ParamType.DOUBLE.ordinal();
            case BOOLEAN:
                return ParamType.BOOLEAN.ordinal();
            case CHAR:
                return ParamType.CHAR.ordinal();
            case STRING:
                return ParamType.STRING.ordinal();
            default:
                // 其他类型，可能是  parcelable serializable object 类型
                if (mTypesUtils.isSubtype(typeMirror, mParcelableType)) {
                    // PARCELABLE
                    return ParamType.PARCELABLE.ordinal();
                } else if (mTypesUtils.isSubtype(typeMirror, mSerializableType)) {
                    // SERIALIZABLE
                    return ParamType.SERIALIZABLE.ordinal();
                } else {
                    return ParamType.OBJECT.ordinal();
                }
        }
    }

    public static boolean isEmptyStr(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    protected abstract boolean processAnnotation(Set<? extends TypeElement> set, RoundEnvironment env);
}