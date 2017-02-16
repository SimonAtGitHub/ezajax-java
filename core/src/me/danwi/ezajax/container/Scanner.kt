package me.danwi.ezajax.container

import org.apache.commons.io.FileUtils
import java.io.File
import java.util.jar.JarFile

/**
 * 扫描
 * Created by demon on 2017/2/13.
 */

/**
 * 获取到所有的类
 */
fun scanAllClasses(): List<String> {
    //获取到classpath
    val url = Thread.currentThread().contextClassLoader.getResource("")
    val classesPath = File(url.path)
    var classes = (getClassesFromPath(classesPath))
    //获取到lib目录
    val jarsPath = File(url.path.replace("classes", "lib"))
    var jars = jarsPath.listFiles { dir, filename -> filename.endsWith(".jar") }
    jars.forEach { classes += getClassesFromJar(it) }
    return classes
}

/**
 * 获取到指定文件目录下所有的class包名
 */
private fun getClassesFromPath(path: File): List<String> {
    return FileUtils.listFiles(path, arrayOf("class"), true)
            .map {
                it.absolutePath
                        .drop(path.absolutePath.length + 1)
                        .replace(File.separatorChar, '.')
                        .dropLast(6)
            }
}

/**
 * 获取指定jar文件里面所有的class包名
 */
private fun getClassesFromJar(jarFile: File): List<String> {
    val jar = JarFile(jarFile)
    return jar.entries()
            .toList()
            .filter { it.name.endsWith(".class") }
            .map { it.name.replace(File.separatorChar, '.').dropLast(6) }
}