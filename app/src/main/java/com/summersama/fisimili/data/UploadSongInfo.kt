package com.summersama.fisimili.data

class UploadSongInfo {
      var sn: String=""
    var album=""
    var lyricist=""
    var composer=""
    var arrangement=""
    var singer = ""
    var nmn = "" // number music nxxx
    val wn = ""  // ji pu
    override fun toString(): String {
        return "UploadSongInfo(sn='$sn', album='$album', lyricist='$lyricist', composer='$composer', arrangement='$arrangement', singer='$singer', nmn='$nmn', wn='$wn')"
    }

}
