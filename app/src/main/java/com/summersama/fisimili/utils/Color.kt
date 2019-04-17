package com.summersama.fisimili.utils

class Color {
    var CMYK:List<String> = ArrayList()
    var RGB :List<String> = ArrayList()
    var hex:String = ""
    // for zimu
    var name:String = ""
    // for hanzi or japanese
    var pinyin:String = ""

    override fun toString(): String {
        return "Color(CMYK=$CMYK, RGB=$RGB, hex='$hex', name='$name', pinyin='$pinyin')"
    }


}