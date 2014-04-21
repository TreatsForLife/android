package org.treatsforlife.app.entities;

import com.google.gson.annotations.SerializedName;

public class Media {
    @SerializedName("_id")
    public String id;
    public String type;
    public String url;
}
