package com.naver.android.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "unused"})
public class ControlTowerGenerator {

  private final String packageName;
  private final ControlTowerAnnotatedClass annotatedClazz;

  public ControlTowerGenerator(
      String packageName,
      ControlTowerAnnotatedClass annotatedClazz) {
    this.packageName = packageName;
    this.annotatedClazz = annotatedClazz;
  }

  public TypeSpec generate() {
    TypeSpec.Builder builder =
        TypeSpec.classBuilder(getControlTowerName())
            .addJavadoc("Generated by SVC processor. (https://github.com/naver/svc). Don't change this class.\n")
            .addModifiers(Modifier.PUBLIC)
            .addMethod(getScreenMethodSpec())
            .addMethod(getViewMethodSpec())
            .superclass(ClassName.get("com.naver.android.svc.core.controltower", "ControlTower"));
    return builder.build();
  }

  private MethodSpec getScreenMethodSpec() {
    return MethodSpec.methodBuilder("getScreen")
        .addModifiers(Modifier.PUBLIC)
        .addStatement("return ($L) getBaseScreen()", annotatedClazz.annotatedElement)
        .returns(TypeName.get(annotatedClazz.annotatedElement.asType())).build();
  }

  private MethodSpec getViewMethodSpec() {
    return MethodSpec.methodBuilder("getView")
        .addModifiers(Modifier.PUBLIC)
        .addStatement("return ($L) getBaseViews()", annotatedClazz.baseView)
        .returns(annotatedClazz.baseView).build();
  }

  private String getControlTowerName() {
    return "SVC_" + this.annotatedClazz.clazzName + "ControlTower";
  }
}
