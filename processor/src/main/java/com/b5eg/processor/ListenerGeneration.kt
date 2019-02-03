package com.b5eg.processor

import com.b5eg.annotations.BindField
import com.b5eg.annotations.BindListener
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(ListenerGeneration.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ListenerGeneration : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {

        // val bricks = HashMap<VariableElement, String/*ArrayList<String>*/>()

        val generatedSourcesRoot: String =
            processingEnv.options[BindFieldsProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()

        if (generatedSourcesRoot.isEmpty()) {
            processingEnv.messager.errormessage { "Can't find the target directory for generated Kotlin files." }
            return false
        }

        val variables = HashSet<TypeElement>()

        roundEnv.getElementsAnnotatedWith(BindListener::class.java).forEach { methodElement ->

            if (methodElement.kind != ElementKind.INTERFACE) {
                processingEnv.messager.errormessage { "Can only be applied to functions,  element: $methodElement " }
                return false
            }

            (methodElement as TypeElement).apply {

                variables.add(this)

//                parameters[0].also { variableElement ->
//
//
//                    //поле при аргументе метода
//                    //  val fieldsInArgument = ElementFilter.fieldsIn(variableAsElement.enclosedElements)
//
//                    //from anotation
//                    //  val methodName = methodElement.getAnnotation(BindListener::class.java).methodName
//
//                    bricks.put(variableElement, this.simpleName.toString())
//
//                    variables.add(variableElement)
//
//                }
            }
        }

        //аргументы при анотации
        //val annotationArgs = method.getAnnotation(BindField::class.java).viewIds


        variables.forEach { variableElement ->

            val funcBuilder =

                FunSpec.builder("bindFields2")

                    .addModifiers(KModifier.PUBLIC)
                    .addParameter(
                        name = "listener",
                        type = variableElement.asType().asTypeName()
                    )

            val variableAsElement1 = processingEnv.typeUtils.asElement(variableElement.asType())

            funcBuilder.addStatement(
                // "listener.%L()"+"\n"+
                "%T.d(\"HACK\",\"try ${variableAsElement1.enclosedElements.size}\")",
                // str,
                ClassName("android.util", "Log")
            )

            funcBuilder.addStatement(
                // "listener.%L()"+"\n"+
                "%T.d(\"HACK\",\"try ${variableAsElement1.asType().asTypeName()}\")",
                // str,
                ClassName("android.util", "Log")
            )

            ElementFilter.methodsIn(variableAsElement1.enclosedElements).forEach { innerMethods ->
                funcBuilder.addStatement(
                    // "listener.%L()"+"\n"+
                    "%T.d(\"HACK\",\"${innerMethods.simpleName}\")",
                    // str,
                    ClassName("android.util", "Log")
                )
            }

            val file = File(generatedSourcesRoot)

            file.mkdir()

            FileSpec.builder("com.b5eg.codegeneration", "BindFieldsGenerated2")
                .addFunction(funcBuilder.build()).build()
                .writeTo(file)

        }
        return false
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(BindField::class.java.canonicalName)
    }
}

