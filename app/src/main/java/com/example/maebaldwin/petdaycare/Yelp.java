package com.example.maebaldwin.petdaycare;

/**
 * Created by PETERSE_DOUG on 4/25/2017.
 */

public class Yelp {
    private YelpDay [] day;
    private String ID, name, address, url, phone;
    private Double rating;

    public Yelp(String ID, String name, String address, String url, String phone, Double rating){
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.url = url;
        this.phone = phone;
        this.rating = rating;
        this.day = new YelpDay[]{new YelpDay(0), new YelpDay(1), new YelpDay(2), new YelpDay(3),
                new YelpDay(4), new YelpDay(5), new YelpDay(6)};
    }

    public Yelp(String ID, String name, String address, String url, String phone){
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.url = url;
        this.phone = phone;
    }

    public Yelp(String ID, String name, String address, String url, Double rating){
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.url = url;
        this.rating = rating;
    }

    public Yelp(String ID, String name, String address, String url){
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.url = url;
    }

    public YelpDay getDay(int d){return day[d];}
    public void setDay(int d, int start, int end){
        day[d].setDay(d);
        day[d].setStart(start);
        day[d].setEnd(end);
    }

    public String getID(){return this.ID;}
    public void setID(String ID){this.ID = ID;}

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public String getAddress(){return this.address;}
    public void setAddress(String address){this.address = address;}

    public String getUrl(){return this.url;}
    public void setUrl(String url){this.url = url;}

    public String getPhone(){return this.phone;}
    public void setPhone(String phone){this.phone = phone;}

    public Double getRating(){return this.rating;}
    public void setRating(Double rating){this.rating = rating;}

    public String toString(){
        String string = "Yelp object:\nID = [" + this.getID() + "]\nname = [" + this.getName() +
                "]\naddress = [" + this.getAddress() + "]\nURL = [" + this.getUrl() + "]\nphone = ["
                + this.getPhone() + "]\nrating = [" + this.getRating() + "]\nHours of Operation:\nMonday: "
                + this.getDay(0).toString() + "Tuesday: " + this.getDay(1).toString() + "Wednesday: "
                + this.getDay(2).toString() + "Thursday: " + this.getDay(3).toString() + "Friday: "
                + this.getDay(4).toString() + "Saturday: " + this.getDay(5).toString()
                + "Sunday: " + this.getDay(6).toString();

        return string;
    }

}
