package com.wilson.annotation;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;


@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.wilson.annotation.TestInterface"})
@AutoService(Processor.class)
public class TestProcesser extends AbstractProcessor {

    private Messager messager; //日志处理

    public static boolean isField(Element annotatedClass) {
        return annotatedClass.getKind() == ElementKind.FIELD;
    }

    public static boolean isFinal(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(Modifier.FINAL);
    }

    public static boolean isPublic(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(Modifier.PUBLIC);
    }

    public static boolean isPrivate(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(Modifier.PRIVATE);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        messager.printMessage(Diagnostic.Kind.NOTE, "processing...");

        for (Element element : roundEnvironment.getElementsAnnotatedWith(TestInterface.class)) {
            if (!isField(element)) {
                messager.printMessage(Diagnostic.Kind.NOTE, String.format("%s field must be field.", element.getSimpleName()));
            }
            if (!isFinal(element)) {
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("%s field must be final.", element.getSimpleName()));
                return false;
            }
            if (!isPrivate(element)) {
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("%s field must be public.", element.getSimpleName()));
                return false;
            }
            VariableElement variableElement = (VariableElement) element;

        }


        return true;
    }
}
