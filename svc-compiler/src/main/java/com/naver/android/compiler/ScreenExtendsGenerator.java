package com.naver.android.compiler;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class ScreenExtendsGenerator {

  private final String packageName;
  private final ControlTowerAnnotatedClass controlTowerAnnotatedClass;
  private final RequireControlTowerAnnotatedClass requireControlTowerAnnotatedClass;

  public ScreenExtendsGenerator(
      String packageName,
      ControlTowerAnnotatedClass controlTowerAnnotatedClass,
      RequireControlTowerAnnotatedClass requireControlTowerAnnotatedClass) {
    this.packageName = packageName;
    this.controlTowerAnnotatedClass = controlTowerAnnotatedClass;
    this.requireControlTowerAnnotatedClass = requireControlTowerAnnotatedClass;
  }

  public TypeSpec generate() {
    TypeSpec.Builder builder =
        TypeSpec.classBuilder(getExtendsName())
            .addJavadoc(String.format("Generated by SVC processor. (https://github.com/naver/svc). Don't change this class.\n %s", requireControlTowerAnnotatedClass.viewsMetaData))
            .addModifiers(Modifier.PUBLIC)
            .addMethod(getControlTowerMethodSpec())
            .addMethod(getCreateViewsMethodSpec())
            .superclass(this.requireControlTowerAnnotatedClass.superClass);
    return builder.build();
  }

  private MethodSpec getControlTowerMethodSpec() {
    return MethodSpec.methodBuilder("controlTower")
        .addModifiers(Modifier.PUBLIC)
        .addStatement("return ($L) controlTower", requireControlTowerAnnotatedClass.controlTower)
        .returns(requireControlTowerAnnotatedClass.controlTower).build();
  }

  private MethodSpec getCreateViewsMethodSpec() {
    return MethodSpec.methodBuilder("createViews")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addStatement(String.format("return new %s()", requireControlTowerAnnotatedClass.baseViewName))
        .returns(requireControlTowerAnnotatedClass.baseView).build();
  }

  private String getExtendsName() {
    return "SVC_" + this.requireControlTowerAnnotatedClass.clazzName;
  }
}
