package com.github.makosful.friendsv2.be;

import android.graphics.Bitmap;
import android.location.Location;

import java.io.Serializable;
import java.util.Date;

public class Friend implements Serializable
{
    private String name;
    private String adress;
    private Location location;
    private String phone;
    private String email;
    private String website;
    private Date birthDate;
    private Bitmap picture;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAdress()
    {
        return adress;
    }

    public void setAdress(String adress)
    {
        this.adress = adress;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public Bitmap getPicture()
    {
        return picture;
    }

    public void setPicture(Bitmap picture)
    {
        this.picture = picture;
    }
}
