package com.crazyorange.spider.processor;

import com.crazyorange.spider.annotation.RouterPage;
import com.crazyorange.spider.annotation.model.AnnotationNode;
import com.crazyorange.spider.annotation.model.NodeType;
import com.crazyorange.spider.annotation.model.SpiderNode;
import com.crazyorange.spider.processor.contants.Constant;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
/**
 * generator code sample
 * <p>
 * public class ARouter$$Group$$m2 implements ISpiderGroup {
 *
 * @Override public void load(Map<String, SpiderNode> container) {
 * container.put("/module/2", RouteMeta.build(....));
 * }
 * }
 */

/**
 * @author crazyorange
 * @Date 2020-01-11
 * <p>
 * Spider 的注解处理器，负责翻译所有注解的代码
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SpiderAnnotationProcessor extends BaseAnnotationProcess {
    // 收集组别相同的节点的信息
    // key 组名
    // value 该组所有的节点
    private Map<String, Set<AnnotationNode>> mGroupInfos = new HashMap<>();

    @Override
    protected boolean processAnnotation(Set<? extends TypeElement> set, RoundEnvironment env) {
        processLog("Start spider annotation process");
        // 遍历所有使用了 RouterPage 的节点
        if (set == null || set.isEmpty()) {
            return false;
        }
        for (Element element : env.getElementsAnnotatedWith(RouterPage.class)) {
            // 获取节点的注解对象
            RouterPage routerAnnotation = element.getAnnotation(RouterPage.class);
            AnnotationNode annotationNode = null;
            if (isActivity(element) || isFragment(element)) {
                if (isActivity(element)) {
                    processLog("Spider annotation is Activity ");
                    annotationNode = new AnnotationNode(NodeType.ACTIVITY, routerAnnotation,
                            element
                    );
                } else if (isFragment(element)) {
                    // todo 处理 Fragment
                }
            }
            // 将 Group 信息收集起来，用来生成 Group 节点
            collectionGroupInfo(annotationNode);
        }
        // 1. 生成 group 节点
        generatorGroupClass();
        // 2. 将所有生成的 group 节点都保存到 root 类中
        return true;
    }

    /**
     * public class SpiderTestmoudle implements ISpiderGroup {
     *   @Override
     *   public void load(Map<String, SpiderNode> container) {
     *     container.put("main",new SpiderNode(NodeType.ACTIVITY ,"main" ,"moudle" ,MainActivity.class));
     *     container.put("test",new SpiderNode(NodeType.ACTIVITY ,"test" ,"moudle" ,TestActivity.class));
     *   }
     * }
     */
    private void generatorGroupClass() {
        for (Map.Entry<String, Set<AnnotationNode>> entry : mGroupInfos.entrySet()) {
            String groupName = entry.getKey();
            MethodSpec.Builder loadMethod = buildLoadMethod();

            for (AnnotationNode node : entry.getValue()) {
                if (node == null) {
                    return;
                }
                /**
                 *    public SpiderNode(NodeType type,
                 *                        String path,
                 *                       String group) {
                 *         this.type = type;
                 *         this.path = path;
                 *         this.group = group;
                 *     }
                 */
                CodeBlock.Builder codeBuilder = CodeBlock.builder();
                CodeBlock.Builder createNode = CodeBlock.builder();
                ClassName className = ClassName.get((TypeElement) node.getElement());
                createNode.add("new $T($T." + node.getType() + " ,$S" + " ,$S" + " ,$T.class" + ")",
                        ClassName.get(SpiderNode.class),
                        ClassName.get(NodeType.class),
                        node.getPageAnnotation().path().toLowerCase(),
                        node.getPageAnnotation().group().toLowerCase(),
                        className
                );
                codeBuilder.addStatement(Constant.PARAM_GROUP_NAME + ".put($S,$L)", node.getPageAnnotation().path()
                        , createNode.build());
                loadMethod.addCode(codeBuilder.build());
            }
            // 创建 group method 的构建对象

            MethodSpec groupMethod = loadMethod.build();
            String className = Constant.SPIDER_GROUP_CLASS_PREFIX + groupName;
            JavaFile file = JavaFile.builder(Constant.GROUP_PACKAGE,
                    TypeSpec.classBuilder(className)
                            .addSuperinterface(className(Constant.GROUP_INTERFACE_ISPIDER_GROUP))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(groupMethod)
                            .build())
                    .build();
            try {
                file.writeTo(mFilerUtils);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void collectionGroupInfo(AnnotationNode node) {
        if (node == null || node.getPageAnnotation() == null) {
            return;
        }
        Set<AnnotationNode> currentGroup = mGroupInfos.get(node.getPageAnnotation().group());
        if (currentGroup == null || currentGroup.isEmpty()) {
            Set<AnnotationNode> group = new TreeSet<AnnotationNode>(new Comparator<AnnotationNode>() {
                @Override
                public int compare(AnnotationNode node1, AnnotationNode node2) {
                    try {
                        return node1.getPageAnnotation().path().compareTo(node2.getPageAnnotation().path());
                    } catch (NullPointerException npe) {
                        processLog(npe.getMessage());
                        return 0;
                    }
                }
            });
            group.add(node);
            mGroupInfos.put(node.getPageAnnotation().group(), group);
            processLog("Spider Node1 group: " + node.getPageAnnotation().group() + " path: "
                    + node.getPageAnnotation().group() + " size " + group.size());
        } else {
            currentGroup.add(node);
            processLog("Spider Node2 group: " + node.getPageAnnotation().group() + " path: "
                    + node.getPageAnnotation().group() + " size " + currentGroup.size());
        }
    }

    private MethodSpec.Builder buildLoadMethod() {
        // 生成 method
        // public void load(Map<String, SpiderNode> container)
        MethodSpec.Builder loadMethod = MethodSpec.methodBuilder(Constant.METHOD_LOAD)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(buildLoadParameter())
                .returns(void.class);
        return loadMethod;
    }

    // 生成 Load 函数的参数
    private ParameterSpec buildLoadParameter() {
        // Map<String, SpiderNode>
        ParameterizedTypeName paramType = ParameterizedTypeName.get(ClassName.get(Map.class)
                , ClassName.get(String.class), ClassName.get(SpiderNode.class));
        // Map<String, SpiderNode> container
        return ParameterSpec.builder(paramType, Constant.PARAM_GROUP_NAME)
                .build();
    }
}
