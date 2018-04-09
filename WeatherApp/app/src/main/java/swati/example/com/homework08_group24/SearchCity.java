package swati.example.com.homework08_group24;

/**
 * Created by swati,Mihai on 4/8/2017.
 */

public class SearchCity {
    String id;
    String city;
    String country;
    String key;
    String headlline;
    String date;
    String mintemp;
    String maxtemp;
    String dayIcon;
    String nightIcon;
    String dayPhrase;
    String nightPhrase;
    String moreDetails;
    String extendedDeatils;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadlline() {
        return headlline;
    }

    public void setHeadlline(String headlline) {
        this.headlline = headlline;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMintemp() {
        return mintemp;
    }

    public void setMintemp(String mintemp) {
        this.mintemp = mintemp;
    }

    public String getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(String dayIcon) {
        this.dayIcon = dayIcon;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(String maxtemp) {
        this.maxtemp = maxtemp;
    }

    public String getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(String nightIcon) {
        this.nightIcon = nightIcon;
    }

    public String getDayPhrase() {
        return dayPhrase;
    }

    public void setDayPhrase(String dayPhrase) {
        this.dayPhrase = dayPhrase;
    }

    public String getNightPhrase() {
        return nightPhrase;
    }

    public void setNightPhrase(String nightPhrase) {
        this.nightPhrase = nightPhrase;
    }

    public String getExtendedDeatils() {
        return extendedDeatils;
    }

    public void setExtendedDeatils(String extendedDeatils) {
        this.extendedDeatils = extendedDeatils;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SearchCity{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headlline='" + headlline + '\'' +
                ", date='" + date + '\'' +
                ", mintemp='" + mintemp + '\'' +
                ", maxtemp='" + maxtemp + '\'' +
                ", dayIcon='" + dayIcon + '\'' +
                ", nightIcon='" + nightIcon + '\'' +
                ", dayPhrase='" + dayPhrase + '\'' +
                ", nightPhrase='" + nightPhrase + '\'' +
                ", moreDetails='" + moreDetails + '\'' +
                ", extendedDeatils='" + extendedDeatils + '\'' +
                '}';
    }
}
