/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HW5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.Json;
import javax.json.JsonObject;

@ManagedBean(name = "Bean")
@SessionScoped
public class Bean {
    private String sentence;
    private String feel;
    
    public void checkFeeling(){
        try{
            URL url = new URL("http://localhost:8080/HW5/webresources/generic");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            
            String inputJson = "{\"sentence\":\"" + sentence + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(inputJson.getBytes("UTF-8"));
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null){
                    response.append(line);
                }

                JsonObject json = Json.createReader(new StringReader(response.toString())).readObject();
                feel = json.getString("feel");
            }
        }catch (Exception e) {
            e.printStackTrace();
            feel = "錯誤: " + e.getMessage(); // 發生錯誤時儲存錯誤訊息
        }
    }
    
    public String getSentence(){
        return sentence;
    }
    public void setSentence(String sentence){
        this.sentence = sentence;
    }
    
    public String getFeel(){
        return feel;
    }
    public void setFeel(String feel){
        this.feel = feel;
    }
}
