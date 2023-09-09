package com.uva.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.TypeParameterResolver
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import com.uva.annotation.Codegen

class CodegenProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Codegen::class.qualifiedName!!)
            .forEach {
                it.accept(CodegenVisitor(), Unit)
            }
        return emptyList()
    }

    private inner class CodegenVisitor : KSVisitorVoid() {
        @OptIn(KotlinPoetKspPreview::class)
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val qualifiedName = classDeclaration.qualifiedName?.asString() ?: run {
                logger.error(
                    "must target classes with qualified names",
                    classDeclaration,
                )
                return
            }
            if (!classDeclaration.modifiers.contains(Modifier.DATA)) {
                logger.error(
                    "cannot target non-data class $qualifiedName",
                    classDeclaration,
                )
                return
            }
            val type = classDeclaration.asType(emptyList())
            val packageName = classDeclaration.packageName.asString()
            val fileSpec = FileSpec
                .builder(packageName, "${classDeclaration.simpleName.asString()}Ext")
                .apply {
                    addImport("kotlin.random", "Random")
                    addFunction(
                        FunSpec
                            .builder("getRandom")
                            .receiver(type.toTypeName(TypeParameterResolver.EMPTY))
                            .addParameter("start", Int::class)
                            .addParameter("end", Int::class)
                            .returns(Int::class)
                            .addStatement("val random =  Random.nextInt(start, end)")
                            .addStatement("return random")
                            .build(),
                    )
                }.build()
            // aggregating false means that processor is sure that the information
            // only comes from certain input files and never from other or new files.
            fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
        }
    }
}
