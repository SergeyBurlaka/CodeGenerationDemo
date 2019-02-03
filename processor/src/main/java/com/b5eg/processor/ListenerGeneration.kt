package com.b5eg.processor

import com.b5eg.annotations.BindAction
import com.b5eg.annotations.BindListener
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(ListenerGeneration.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ListenerGeneration : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val generatedSourcesRoot: String =
            processingEnv.options[ListenerGeneration.KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

        if (generatedSourcesRoot.isEmpty()) {
            processingEnv.messager.errormessage { "Can't find the target directory for generated Kotlin files." }
            return false
        }

        val methods = ArrayList<ExecutableElement>()
        roundEnv.getElementsAnnotatedWith(BindAction::class.java).forEach { methodElement ->
            if (methodElement.kind != ElementKind.METHOD) {
                processingEnv.messager.errormessage { "Can only be applied to functions,  element: $methodElement " }
                return false
            }
            (methodElement as ExecutableElement).apply {
                methods.add(this)
            }
        }

        val listenerClasses = HashSet<TypeElement>()
        roundEnv.getElementsAnnotatedWith(BindListener::class.java).forEach { methodElement ->
            if (methodElement.kind != ElementKind.INTERFACE) {
                processingEnv.messager.errormessage { "Can only be applied to functions,  element: $methodElement " }
                return false
            }
            (methodElement as TypeElement).apply {
                listenerClasses.add(this)
            }
        }

        listenerClasses.forEach { typeElement ->

            //BUILD METHOD HEAD
            val funcBuilder =

                FunSpec.builder("bindFields_${typeElement.simpleName}")
                    .addModifiers(KModifier.PUBLIC)
                    .addParameter(
                        name = "listener",
                        type = typeElement.asType().asTypeName()
                    )
                    .addParameter(
                        name = "action",
                        type = ClassName("kotlin", "String")
                    )

            funcBuilder.addStatement(
                "when{"
            )

            typeElement.enclosedElements.forEach { innerMethods ->
                methods.forEach { annotatedMethod ->
                    if (annotatedMethod.simpleName == innerMethods.simpleName) {
                        funcBuilder.addStatement(
                            "action == \"${annotatedMethod.getAnnotation(BindAction::class.java).actionName}\" -> {" + "\n"
                                    + " listener.${annotatedMethod.simpleName}()" + "\n"
                                    + "}" + "\n"
                        )
                    }
                }
            }

            funcBuilder.addStatement("}")

            val file = File(generatedSourcesRoot)
            file.mkdir()
            FileSpec.builder("com.b5eg.codegeneration", "BindFieldsGenerated_${typeElement.simpleName}")
                .addFunction(funcBuilder.build()).build()
                .writeTo(file)
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(BindAction::class.java.canonicalName).apply {
            add(BindListener::class.java.canonicalName)
        }

}

