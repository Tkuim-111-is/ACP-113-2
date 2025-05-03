package G6_02;
// 註解用GPT生的，其他都是淚水與汗水
// 匯入 Gson 用於將 Java 物件轉換成 JSON 格式
import com.google.gson.Gson;

// JAX-RS 註解，用來宣告這是一個 RESTful 資源
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("subtract") // 將此類別註冊為對應於路徑 "/subtract" 的資源
public class Resource {

    @GET // 指定這是一個 HTTP GET 方法
    @Path("{number1}/{number2}") // 接收 URL 路徑參數，例如 /subtract/9/3
    @Produces("application/json") // 回傳的資料格式是 JSON
    public String getJson(@PathParam("number1") int number1, @PathParam("number2") int number2) {

        // 建立一個 Bean 實例，用來包裝運算結果
        Bean bean = new Bean();

        // 將路徑參數的值設給 bean 的屬性
        bean.setNumber1(number1);
        bean.setNumber2(number2);

        // 執行相減並設置結果
        bean.setResult(number1 - number2);

        // 將 bean 物件轉成 JSON 字串並回傳
        return new Gson().toJson(bean);
    }
}
