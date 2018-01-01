package com.ipmus

import org.slf4j.LoggerFactory

/**
 * Created by mdozturk on 7/27/17.
 */

object Configuration {
    //private val input = javaClass.classLoader.getResourceAsStream("config.txt")
    //private val lines = if (input != null) input.bufferedReader().lines().toArray() else arrayOf()
    private val defaultLoc = "mtlstore"
    val dataLocation = if (System.getProperty("os.name").toLowerCase().contains("windows")) "c:/temp/mtlStore" else defaultLoc
}