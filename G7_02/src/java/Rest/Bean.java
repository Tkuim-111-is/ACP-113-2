package Rest;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import Rest.Address;

@ManagedBean(name = "Bean")
@RequestScoped
public class Bean {

    private Integer addressid;
    private String firstname,lastname,email,phonenumber;
    private String message;
    private String message1, message2;

    public void addressbook() {
        try {
            URL url1, url2;
            url1 = new java.net.URL("http://localhost:8080/G7_02/webresources/generic");
            // 建立 HTTP 連線
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            conn1.setRequestMethod("GET");
            conn1.setRequestProperty("Accept", "application/json");
             
            // 讀取伺服器回傳的 JSON 字串
            BufferedReader br1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
            String output1;
            StringBuilder b_result1 = new StringBuilder();
            while ((output1 = br1.readLine()) != null) {
                b_result1.append(output1); // 把每行回應加進 StringBuilder
            }

            // 將結果儲存到 message 屬性，JSF 頁面會顯示這個訊息
            this.message1 = b_result1.toString();
            conn1.disconnect();
            
            if(addressid != null){
                url2 = new java.net.URL("http://localhost:8080/G7_02/webresources/generic/" + addressid);
                // 建立 HTTP 連線
                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                conn2.setRequestMethod("GET");
                conn2.setRequestProperty("Accept", "application/json");
            
                // 讀取伺服器回傳的 JSON 字串
                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                String output2;
                StringBuilder b_result2 = new StringBuilder();
                while ((output2 = br2.readLine()) != null) {
                    b_result2.append(output2); // 把每行回應加進 StringBuilder
                }

                // 將結果儲存到 message 屬性，JSF 頁面會顯示這個訊息
                this.message2 = b_result2.toString();
                conn2.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            message1 = "錯誤: " + e.getMessage(); // 發生錯誤時儲存錯誤訊息
        }
    }
    
    public void addBook(){
        try {
            URL url;
            url = new java.net.URL("http://localhost:8080/G7_02/webresources/generic");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true); // 允許寫入
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Address addr = new Address(firstname, lastname, email, phonenumber);
            // 構造 JSON 字串
            String json = new Gson().toJson(addr);
            // 將 JSON 寫入輸出流
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP 錯誤代碼: " + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                result.append(output);
            }
            
            this.message = "新增成功：\n" + result.toString();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            this.message = "發生錯誤：" + e.getClass().getName() + " - " + e.getMessage();
            // 額外：顯示是哪一行錯的（加印 stack trace 的第一行）
            StackTraceElement[] trace = e.getStackTrace();
            if (trace.length > 0) {
                this.message += "\n發生在：" + trace[0].getClassName() + "." + trace[0].getMethodName() + "() 行 " + trace[0].getLineNumber();
            }
        }
    }
    
    public Integer getAddressid(){
        return addressid;
    }
    public void setAddressid(Integer addressid){
        this.addressid = addressid;
    }
    
    public String getFirstname(){
        return firstname;
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    
    public String getLastname(){
        return lastname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPhonenumber(){
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }
    
    public String getMessage(){
        return message;
    }
    public String getMessage1(){
        return message1;
    }
    public String getMessage2(){
        return message2;
    }
}
