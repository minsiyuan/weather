package com.example.weather;

public class Weather {
    private String province;
    private String city;
    private String weather;
    private String temperature;
    private String humidity;
    private String reporttime;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getReporttime() {
        return reporttime;
    }

    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }

    @Override
    public String toString() {
        return  "省份:" + province + '\n' +
                "城市:" + city + '\n' +
                "天气:" + weather + '\n' +
                "温度:" + temperature + '\n' +
                "湿度:" + humidity + '\n' +
                "更新时间:" + reporttime + '\n'
                ;
    }
}
