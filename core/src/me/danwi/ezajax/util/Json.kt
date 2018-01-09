package me.danwi.ezajax.util

import com.google.gson.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 自定义JSON解析器
 * Created by demon on 2017/2/14.
 */
val JSON = GsonBuilder()
        .registerTypeAdapter(Date::class.java, JsonDeserializer<Any> { json, _, _ ->
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.parse(json.toString().replace("\"", ""))
        })
        .registerTypeAdapter(Date::class.java, JsonSerializer<Any> { src, _, _ ->
            val date = (src as Date)
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            JsonPrimitive(formatter.format(date))
        }).create()!!