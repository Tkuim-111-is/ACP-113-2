package Rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;


import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("generic")
public class GenericResource {
    @Resource(lookup = "java:global/jdbc/Title")
    private DataSource dataSource;
    
    private Connection getConnection() throws Exception {
        String url = "jdbc:derby://localhost:1527/Title"; // 你的 DB 名稱
        String user = "meowwu";
        String password = "meowwu";
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        return DriverManager.getConnection(url, user, password);
    }
    
    @GET
    @Produces("application/json")
    public String getJson() throws SQLException, Exception{
        Bean bean = new Bean();
        List<String> isbnList = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement getRecords = connection.prepareStatement(
                "SELECT ISBN FROM titles"
            );

            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getRecords.executeQuery()); 
            while (rowSet.next()) {
                isbnList.add(rowSet.getString("ISBN"));
            }

            Map<String, List<String>> result = new HashMap<>();
            result.put("ISBN", isbnList);

            return new Gson().toJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } finally {
            connection.close();
        }
    }
    
    @GET
    @Path("{ISBN}")
    @Produces("application/json")
    public String getJson(@PathParam("ISBN") String ISBN) throws SQLException, Exception{
        Connection connection = getConnection();
        try {
            Bean bean = new Bean();
            PreparedStatement getRecords = connection.prepareStatement(
                "SELECT * FROM titles where ISBN = ?"
            );
            getRecords.setString(1, ISBN);
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            
            rowSet.populate(getRecords.executeQuery()); 
            // 將路徑參數的值設給 bean 的屬性
            if(rowSet.next()){
                bean.setISBN(rowSet.getString("ISBN"));
                bean.setTitle(rowSet.getString("title"));
                bean.setEdition_number(rowSet.getInt("edition_number"));
                bean.setCopyright(rowSet.getString("copyright"));
            }else {
                System.out.println("❗查無資料");
            }

            // 將 bean 物件轉成 JSON 字串並回傳
            return new GsonBuilder().disableHtmlEscaping().create().toJson(bean);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}