package com.example.user.travel360.Story;

import java.io.Serializable;

/**
 * Created by user on 2016-09-25.
 */
public class Image implements Serializable
{
    int picture_group_seq;
    String picture_loc;
    int seq;

    public Image()
    {
    }

    public Image(int picture_group_seq, String picture_loc, int seq)
    {
        this.picture_group_seq = picture_group_seq;
        this.picture_loc = picture_loc;
        this.seq = seq;
    }

    public int getPicture_group_seq()
    {
        return picture_group_seq;
    }

    public void setPicture_group_seq(int picture_group_seq)
    {
        this.picture_group_seq = picture_group_seq;
    }

    public String getPicture_loc()
    {
        return picture_loc;
    }

    public void setPicture_loc(String picture_loc)
    {
        this.picture_loc = picture_loc;
    }

    public int getSeq()
    {
        return seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }
}