/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;

import com.google.gson.Gson;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Item;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.Serializable;

@Named("bufferBean")
@SessionScoped
public class BufferBean implements Serializable {
    private String result = "";

    public void produce() {
        try {
            URL url = new URL("http://localhost:8080/G8_02/api/v1/produce");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            Item item = new Item();
            item.setProducer_name("tainan");
            item.setItem_id("t" + System.currentTimeMillis());

            String json = new Gson().toJson(item);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            result = "Produce response: " + sb.toString();
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }
    }

    public void consume() {
        try {
            URL url = new URL("http://localhost:8080/G8_02/api/v1/consume/taipei");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            result = "Consume response: " + sb.toString();
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }
    }

    public String getResult() {
        return result;
    }
}