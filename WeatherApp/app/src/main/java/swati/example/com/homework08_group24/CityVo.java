package swati.example.com.homework08_group24;

import android.util.Log;

/**
 * Created by swati,Mihai on 4/8/2017.
 */

public class CityVo implements Comparable<CityVo> {

    String id;
    String citykey;
    String cityname;
    String country;
    String temperature;
    String favorite;
    String date;

    public int compareTo(CityVo cityVo) {

            Log.d("Compare","Cpma");
            if(this.getFavorite().equals(cityVo.getFavorite()))
                return 0;
            else if (this.getFavorite().compareTo(cityVo.getFavorite()) > 0) {
                    return -1;
                } else {
                    return 1;
                }
    }

    public String getCitykey() {
        return citykey;
    }

    public void setCitykey(String citykey) {
        this.citykey = citykey;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
