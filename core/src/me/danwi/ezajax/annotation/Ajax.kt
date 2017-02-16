package me.danwi.ezajax.annotation

/**
 * Ajax模块注解
 * Created by demon on 2017/2/14.
 */

@Target(AnnotationTarget.CLASS)
annotation class Ajax(val value: String = "")

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ParamName(val value: String)