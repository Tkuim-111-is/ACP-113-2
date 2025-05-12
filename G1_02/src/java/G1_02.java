/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "temperatureConverter")
@ViewScoped
public class G1_02 {
    private double fahrenheit;
    private double celsius;
    private double kelvin;
    public double change;
    private String resultMessage;
    private final ArrayList<String> history = new ArrayList<>();
    
    public String printHistory() {
        return String.join("<br/>", history);
    }

    public double getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public double getCelsius() {
        return celsius;
    }

    public void setCelsius(double celsius) {
        this.celsius = celsius;
    }

    public double getKelvin() {
        return kelvin;
    }

    public void setKelvin(double kelvin) {
        this.kelvin = kelvin;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public void FahrenheitToCelsius() {
        if (fahrenheit < -58.67) {
            resultMessage = "華氏溫度過低！";
        }
        else if(fahrenheit > 212){
            resultMessage = "華氏溫度過高！";
        }
        else {
            change = 5.0 / 9.0 * (fahrenheit - 32);
            resultMessage = "華氏 " + fahrenheit + " = 攝氏 " + change;
             // Save the conversion result to history
        }
        this.history.add(resultMessage);
    }
    
    public void FahrenheitToKelvin(){
        if (fahrenheit < -58.67) {
            resultMessage = "華氏溫度過低！";
        }
        else if(fahrenheit > 212){
            resultMessage = "華氏溫度過高！";
        }
        else {
            change = 5.0 / 9.0 * (fahrenheit - 32) + 273.15;
            resultMessage = "華氏 " + fahrenheit + " = 凱氏 " + change;
        }
        this.history.add(resultMessage);
    }

    public void CelsiusToFahrenheit() {
        if (celsius < -50) {
            resultMessage = "攝氏溫度過低！";
        }
        else if(celsius > 100){
            resultMessage = "攝氏溫度過高！";
        }
        else {
            change = (celsius * 9.0 / 5.0) + 32;
            resultMessage = "攝氏 " + celsius + " = 華氏 " + change;
        }
        history.add(resultMessage);
    }

    public void CelsiusToKelvin() {
        if (celsius < -50) {
            resultMessage = "攝氏溫度過低！";
        }
        else if(celsius > 100){
            resultMessage = "攝氏溫度過高！";
        }
        else {
            change = celsius + 273.15;
            resultMessage = "攝氏 " + celsius + " = 凱氏 " + change;
        }
        history.add(resultMessage);
    }

    public void KelvinToCelsius() {
        if (kelvin < 223.15) {
            resultMessage = "凱氏溫度過低！";
        }
        else if(kelvin > 373.15){
            resultMessage = "凱氏溫度過高！";
        }
        else {
            change = kelvin - 273.15;
            resultMessage = "凱氏 " + kelvin + " = 攝氏 " + change;
        }
        history.add(resultMessage);
    }

    public void KelvinToFahrenheit() {
        if (kelvin < 223.15) {
            resultMessage = "凱氏溫度過低！";
        }
        else if(kelvin > 373.15){
            resultMessage = "凱氏溫度過高！";
        }
        else {
            change = (kelvin - 273.15) * 9.0 / 5.0 + 32;
            resultMessage = "凱氏 " + kelvin + " = 華氏 " + change;
        }
        history.add(resultMessage);
    }
}