/*
*   Copyright (c) Ashar Khan 2017. <ashar786khan@gmail.com>
*    This file is part of Google Developer Group's Android Application.
*   Google Developer Group 's Android Application is free software : you can redistribute it and/or modify
*    it under the terms of GNU General Public License as published by the Free Software Foundation,
*   either version 3 of the License, or (at your option) any later version.
*
*   This Application is distributed in the hope that it will be useful
*    but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*   or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General  Public License for more details.
*
*   You should have received a copy of the GNU General Public License along with this Source File.
*   If not, see <http:www.gnu.org/licenses/>.
 */


package com.softminds.gdg.utils

class GdgEvents {

    //data-fields
    var time: Long = 0
    var venue: String? = null
    var name: String? = null
    var author: String? = null
    var agenda: String? = null
    var speakers: List<String>? = null
    var extra_details: String? = null //any extra payload for event
    var picsUrl: List<String>? = null //contains list of urls of current event
    var headIconUrl: String? = null //contains central event url
    var type: Int = 0

    companion object {
        val CONFERENCE = 1
        val MEET_UP = 2
        val HACK_ATHON = 3
        val IDEA_THON = 4
        val DISCUSSION = 5
        val SEMINAR = 6
        val WORKSHOP = 7
        val FEST = 8
    }

}//required a empty constructor
