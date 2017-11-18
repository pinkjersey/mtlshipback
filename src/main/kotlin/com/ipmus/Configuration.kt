package com.ipmus

/**
 * Created by mdozturk on 7/27/17.
 */

object Configuration {
    // TODO the code below doesn't work, input is always null. This code should be fixed and the input != null stuff
    // should be removed
    //private val input = javaClass.getResourceAsStream("config.txt")
    //private val lines = if (input != null) input.bufferedReader().lines().toArray() else arrayOf()
    //val dataLocation = if (input != null) if (lines[0] == "MAC") "/tmp/.mtlstore" else "/temp/.mtlstore" else "/tmp/.mtlstore"
    val dataLocation = "c:/temp/mtlStore"
}