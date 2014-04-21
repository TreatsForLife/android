package org.treatsforlife.app.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pet {
    @SerializedName("_id")
    public String id;
    public String name;
    public List<Media> media;
    public String createdAt;
    public String updatedAt;
}
