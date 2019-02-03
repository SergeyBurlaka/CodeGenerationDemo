package com.b5eg.annotations


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class BindField(val viewIds: Array<String>, val viewName: String)



