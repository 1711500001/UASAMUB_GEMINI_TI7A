package com.example.uas__amub_ti7a_01_07_10_11;


import android.net.Uri;

public class Transfer {

    private String user, u;
    private String jml;
    private String tgl;
    private String key;
    private Uri photo;

    public Transfer(String user, String jml, String tgl, Uri photo, String key, String u) {
        this.user = user;
        this.jml = jml;
        this.tgl = tgl;
        this.key = key;
        this.u = u;
        this.photo = photo;
    }
    public String getU() {
        return u;
    }

    public String getKey() {
        return key;
    }

    public Uri getPhoto() {
        return photo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getJml() {
        return jml;
    }

    public String getTgl() {
        return tgl;
    }

}
