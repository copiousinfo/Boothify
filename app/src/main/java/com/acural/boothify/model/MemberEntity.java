package com.acural.boothify.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "members")
public class MemberEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // First Fragment
    public String name;
    public String mobile;
    public String father;
    public String gender;
    public String dob;
    public String age;
    public String block;
    public String division;
    public String district;
    public String assembly;

    // Second Fragment
    public String voterId;
    public String occupation;
    public String education;
    public String imagePath;
}
