package G6_02;
// 註解用GPT生的，其他都是淚水與汗水
// Java 內建的類別，用於處理 HTTP 請求
import java.net.HttpURLConnection;
import java.net.URL;

// JSF 註解：宣告這是一個 Managed Bean，可被 JSF 頁面存取
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

// 用於讀取 HTTP 回應資料
import java.io.BufferedReader;
import java.io.InputStreamReader;

@ManagedBean(name = "Bean") // 指定此 Bean 的名稱為 "Bean"，JSF 頁面可透過 #{Bean} 存取
@ViewScoped // View 範圍的生命週期，適合一個頁面內的使用需求
public class Bean {
    private int number1, number2; // 儲存使用者輸入的兩個數字
    private int result; // 儲存計算結果（目前未使用）
    private String message; // 用來儲存 REST 回傳的字串訊息（JSON 字串）

    // JSF 按鈕觸發呼叫的相減方法
    public void subtract() {
        try {
            // 建立 URL：將使用者輸入的數字附加在 REST API 的 URL 後方
            URL url = new URL("http://localhost:8080/G6_02/webresources/subtract/" + number1 + "/" + number2);
            
            // 建立 HTTP 連線
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // 設定 HTTP 方法為 GET
            conn.setRequestProperty("Accept", "application/json"); // 希望取得 JSON 格式的回應

            // 若 HTTP 回應不是 200 (成功)，則丟出例外
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error: " + conn.getResponseCode());
            }

            // 讀取伺服器回傳的 JSON 字串
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            StringBuilder b_result = new StringBuilder();
            while ((output = br.readLine()) != null) {
                b_result.append(output); // 把每行回應加進 StringBuilder
            }

            // 將結果儲存到 message 屬性，JSF 頁面會顯示這個訊息
            this.message = b_result.toString();

            // 關閉連線
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            message = "錯誤: " + e.getMessage(); // 發生錯誤時儲存錯誤訊息
        }
    }

    // Getter 與 Setter 方法：讓 JSF 頁面或程式碼可以讀寫變數值

    public int getNumber1() {
        return number1;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }
}
