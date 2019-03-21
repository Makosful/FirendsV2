package com.github.makosful.friendsv2.be;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Friend implements Serializable
{
    private Bitmap image;
    private String name;
    private String email;
    private String number;

    public Friend(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }
}
