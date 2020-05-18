package com.example.intern.Model;

public class UserInfo {
    String id,dogsName,ownerName,age,sex,breed,address,dp;
    String location;

    public UserInfo() {
    }


    public UserInfo(String id, String dogsName, String ownerName, String age, String sex, String breed, String location,String address, String dp) {
        this.id = id;
        this.dogsName = dogsName;
        this.ownerName = ownerName;
        this.age = age;
        this.sex = sex;
        this.breed = breed;
        this.location = location;
        this.address = address;
        this.dp = dp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDogsName() {
        return dogsName;
    }

    public void setDogsName(String dogsName) {
        this.dogsName = dogsName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
