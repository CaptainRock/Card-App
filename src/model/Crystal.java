/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package com.bj.enterprise.simple.simple.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;


/**
 * Crystal POJO.
 */
@IgnoreExtraProperties
public class Crystal {

   // public static final String FIELD_NAME = "name";
    //public static final String FIELD_DESCRIPTION = "description";
    //public static final String FIELD_URL_SMALL = "urls";
    //public static final String FIELD_URL_BIG = "urlb";
    //public static final String FIELD_AVG_RATING = "avgRating";

    private String name;
    private String desc;
    private String urls;
    private String urlb;
    //private int price;
    //private int numRatings;
    //private double avgRating;

    public Crystal() {}

    public Crystal(String name, String description, String url_big, String url_small) {
        this.name = name;
        this.desc = description;
        this.urlb = url_big;
        this.urls = url_small;
    }

    @PropertyName("name")
    public String getName(){

        return name;
    }

    @PropertyName("name")
    public void setName(String name){

        this.name = name;
    }




    @PropertyName("urls")
    public void setUrls( String sUrl){

        this.urls = sUrl;
    }



    @PropertyName("urls")
   public String getUrls(){

        return urls;
   }


    @PropertyName("urlb")
    public String getUrlb(){

        return urlb;
    }

    @PropertyName("urlb")
    public void setUrlb(String bUrl){

        this.urlb = bUrl;
    }


    @PropertyName("desc")
    public String getDesc(){
         return desc;
    }

    @PropertyName("desc")
    public void setDesc(String desc){

        this.desc = desc;
    }


}
