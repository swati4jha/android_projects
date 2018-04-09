package swati.example.com.homework08_group24;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by swati,Mihai on 4/7/2017.
 */

public class City {
    String city_name;
    String country_name;
    String city_key;
    String temp;
    String time;
    String cloudy;
    String wearherIcon;

    public static City createCity(JSONObject obj){
        City city = new City();

        try{
            city.setCloudy(obj.getString("WeatherText").toString());
            city.setTime(Ptime.PtimeFormatter.getPtimeFrom(obj.getString("LocalObservationDateTime").toString()));
            city.setWearherIcon(obj.getString("WeatherIcon").toString());
            JSONObject temp = obj.getJSONObject("Temperature");
            JSONObject metric = temp.getJSONObject("Metric");
            Log.d("Temp",metric.getString("Value").toString());
            city.setTemp(metric.getString("Value").toString());

        }catch (JSONException ex){
            Log.d("Exception",ex.toString());
        }

        return city;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCity_key() {
        return city_key;
    }

    public void setCity_key(String city_key) {
        this.city_key = city_key;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCloudy() {
        return cloudy;
    }

    public void setCloudy(String cloudy) {
        this.cloudy = cloudy;
    }

    public String getWearherIcon() {
        return wearherIcon;
    }

    public void setWearherIcon(String wearherIcon) {
        this.wearherIcon = wearherIcon;
    }

    @Override
    public String toString() {
        return "City{" +
                "city_name='" + city_name + '\'' +
                ", country_name='" + country_name + '\'' +
                ", city_key='" + city_key + '\'' +
                ", temp='" + temp + '\'' +
                ", time='" + time + '\'' +
                ", cloudy='" + cloudy + '\'' +
                ", wearherIcon='" + wearherIcon + '\'' +
                '}';
    }
}
