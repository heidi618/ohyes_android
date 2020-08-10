package com.example.mywifi_demo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {
    public String id;
    public String name;
    public Long birth;
    public String gender;
    public String cmkg;

    public FirebasePost(){

    }
    public FirebasePost(String id, String name, Long birth, String gender, String cmkg) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.cmkg = cmkg;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("birth", birth);
        result.put("gender", gender);
        result.put("cmkg", cmkg);
        return result;
    }
}
