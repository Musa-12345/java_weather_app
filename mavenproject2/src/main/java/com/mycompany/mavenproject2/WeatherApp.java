/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class WeatherApp {
    private static final String OPENWEATHERMAP_API_KEY = "aaac9a4a7487a99531a5f138aeabddbf";
    private static final String WEATHERSTACK_API_KEY = "e153ed02d13fe5827b2b61a9fad1ae9e";
    private static final String WEATHERBIT_API_KEY = "d8d358fdae874eeaba055d68fd9899df";
    
    /*private static final String[] CITIES = {"Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", 
    "Artvin", "Aydin", "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", 
    "Bolu", "Burdur", "Bursa", "Canakkale", "Cankiri", "Corum", "Denizli", "Diyarbakir", "Duzce", 
    "Edirne", "Elazig", "Erzincan", "Erzurum", "Eskisehir", "Gaziantep", "Giresun", "Gumushane", 
    "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul", "Izmir", "Kahramanmaras", "Karabuk", 
    "Karaman", "Kars", "Kastamonu", "Kayseri", "Kirikkale", "Kirklareli", "Kirsehir", "Kilis", 
    "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa", "Mardin", "Mersin", "Mugla", "Mus", 
    "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt", 
    "Sinop", "Sırnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", 
    "Yozgat", "Zonguldak"}; // Şehirleri buraya ekleyin*/
    private static final String[] CITIES={"Adana"};

    public void fetchWeatherDataForMultipleCities() {
        for (String city : CITIES) {
            fetchWeatherDataForCity(city);
            //System.out.println(city);
        }
    }

    public void fetchWeatherDataForCity(String city) {
        CountDownLatch latch = new CountDownLatch(3);
        double[] temperatures = new double[3];

        // Thread for OpenWeatherMap
        new Thread(() -> {
            try {
                temperatures[0] = getOpenWeatherMapTemperature(city);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        // Thread for Weatherstack
        new Thread(() -> {
            try {
                temperatures[1] = getWeatherstackTemperature(city);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } finally {
                latch.countDown(); 
            }
        }).start();

        // Thread for Weatherbit
        new Thread(() -> {
            try {
                temperatures[2] = getWeatherbitTemperature(city);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } finally {
                latch.countDown(); 
            }
        }).start();

        try {
            latch.await(); // Tüm threadlerin tamamlanmasını bekle
            double averageTemp = (temperatures[0] + temperatures[1] + temperatures[2]) / 3;
            System.out.println(city + " için ortalama sıcaklık: " + averageTemp + "°C");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // OpenWeatherMap'den sıcaklık verisi çeken metot
    public double getOpenWeatherMapTemperature(String city) throws IOException, ParseException {
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, OPENWEATHERMAP_API_KEY);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.getJSONObject("main").getDouble("temp");
            }
        }
    }

    // Weatherstack'den sıcaklık verisi çeken metot
    public double getWeatherstackTemperature(String city) throws IOException, ParseException {
        String url = String.format("http://api.weatherstack.com/current?access_key=%s&query=%s", WEATHERSTACK_API_KEY, city);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);

                if (jsonObject.has("current")) {
                    return jsonObject.getJSONObject("current").getDouble("temperature");
                } else {
                    System.out.println("API yanıtında 'current' verisi yok.");
                    return 0.0;
                }
            }
        }
    }

    // Weatherbit'den sıcaklık verisi çeken metot
    public double getWeatherbitTemperature(String city) throws IOException, ParseException {
        String url = String.format("https://api.weatherbit.io/v2.0/current?city=%s&key=%s", city, WEATHERBIT_API_KEY);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.getJSONArray("data").getJSONObject(0).getDouble("temp");
            }
        }
    }

    void getWeatherStackTemperature() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
