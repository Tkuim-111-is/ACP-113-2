/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rest;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@ManagedBean(name = "Bean")
@ViewScoped
public class Bean {
    private String ISBN, title, copyright;
    private int edition_number;
    private String message;
    // JSF 按鈕觸發呼叫的相減方法
    public void searchBook() {
        try {
            URL url;
            if (ISBN == null || ISBN.isEmpty()) {
                url = new java.net.URL("http://localhost:8080/Homework4/webresources/generic");
            } else {
                url = new java.net.URL("http://localhost:8080/Homework4/webresources/generic/" + ISBN);
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            StringBuilder b_result = new StringBuilder();
            while ((output = br.readLine()) != null) {
                b_result.append(output);
            }
            this.message = b_result.toString();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            this.message = "錯誤: " + e.getMessage();
        }
    }
    public String getISBN(){
        return ISBN;
    }
    public void setISBN(String ISBN){
        this.ISBN = ISBN;
    }
    
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    
    public int getEdition_number(){
        return edition_number;
    }
    public void setEdition_number(int edition_number){
        this.edition_number = edition_number;
    }
    
    public String getCopyright(){
        return copyright;
    }
    public void setCopyright(String copyright){
        this.copyright = copyright;
    }
    
    public String getmessage(){
        return message;
    }
    public void setmessage(String message){
        this.message = message;
    }
}
