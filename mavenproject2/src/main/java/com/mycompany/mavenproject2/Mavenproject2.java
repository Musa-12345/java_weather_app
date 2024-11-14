/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hc.core5.http.ParseException;

/**
 *
 * @author Musa
 */
public class Mavenproject2 {

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Hello World!");
        WeatherApp app= new WeatherApp();
    
        app.fetchWeatherDataForMultipleCities();
        
        // WeatherApp nesnesi üzerinden fetchWeatherData metodunu çağırıyoruz
        /*app.fetchWeatherDataForCity();
        
        app.getWeatherStackTemperature();
        app.getOpenWeatherMapTemperature();
        app.getWeatherbitTemperature();
        */
    }
}
