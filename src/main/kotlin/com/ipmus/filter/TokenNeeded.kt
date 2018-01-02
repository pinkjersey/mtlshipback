package com.ipmus.filter

import javax.ws.rs.NameBinding
import java.lang.annotation.Retention

import java.lang.annotation.ElementType.METHOD
import java.lang.annotation.ElementType.TYPE
import java.lang.annotation.RetentionPolicy.RUNTIME

@NameBinding
@Retention(RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class TokenNeeded