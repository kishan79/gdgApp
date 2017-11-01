
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


package com.softminds.gdg.utils;


import android.content.Intent;
import android.os.Bundle;

import java.util.List;

@SuppressWarnings("unused") //same reason as that of AppUsers
public class GdgEvents {
    public static final int CONFERENCE = 1;
    public static final int MEET_UP = 2;
    public static final int HACK_ATHON = 3;
    public static final int IDEA_THON = 4;
    public static final int DISCUSSION = 5;
    public static final int SEMINAR = 6;
    public static final int WORKSHOP = 7;

    //data-fields
    private long time;
    private String venue;
    private String name;
    private String author;
    private String extra_details; //any extra payload for event
    private List<String> PicsUrl; //contains list of urls of current event
    private String headIconUrl; //contains central event url
    private int type;

    public GdgEvents(){
        //required a empty constructor
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public List<String> getPicsUrl() {
        return PicsUrl;
    }

    public long getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public String getAuthor() {
        return author;
    }

    public String getExtra_details() {
        return extra_details;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setExtra_details(String extra_details) {
        this.extra_details = extra_details;
    }

    public void setPicsUrl(List<String> picsUrl) {
        PicsUrl = picsUrl;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

}
