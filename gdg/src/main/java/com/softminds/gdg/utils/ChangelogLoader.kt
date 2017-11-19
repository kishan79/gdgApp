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


import org.jetbrains.annotations.Contract

/*
    Todo : Hardcode all the new Changes before releasing a new Version on Stable Channel. No Changes in beta channel will be made
 */


object ChangelogLoader {

    @Contract(pure = true)
    fun loadAll(): String {
        return "* What's new section : This will show all the new changes that will be coming with a upgrade" + "\n\n" +
                "* Auto Update Announcer : The App will automatically announce the new updates. As soon as one is available." + "\n\n" +
                "* Many Small UI and Bug Fixes : Some Issues with Landscape mode of Home Screen fixed. Event Detail Screen also have some major Changes."
    }
}
