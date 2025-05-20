/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package G7_02;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("generic")
public class GenericResource {
    
    @Resource(lookup = "java:global/jdbc/G7_02")
    
    private Connection getConnection() throws SQLException {
        String url = "jdbc:derby://localhost:1527/G7_02"; // 你的 DB 名稱
        String user = "G7_02";
        String password = "G7_02";
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
                "SELECT addressid FROM addressesemail"
            );

            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getRecords.executeQuery()); 
            while (rowSet.next()) {
                isbnList.add(rowSet.getString("addressid"));
            }

            Map<String, List<String>> result = new HashMap<>();
            result.put("addressid", isbnList);

            return new Gson().toJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } finally {
            connection.close();
        }
    }
    
    @GET
    @Path("{addressid}")
    @Produces("application/json")
    public String getJson(@PathParam("addressid") int addressid) throws SQLException, Exception {
        Connection connection = getConnection();
        try {
            Bean bean = new Bean();
            PreparedStatement getRecords = connection.prepareStatement(
                "SELECT * FROM addressesemail where addressid = ?"
            );
            getRecords.setInt(1, addressid);
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            
            rowSet.populate(getRecords.executeQuery()); 
            // 將路徑參數的值設給 bean 的屬性
            if(rowSet.next()){
                bean.setAddressid(rowSet.getInt("Addressid"));
                bean.setLastname(rowSet.getString("lastname"));
                bean.setFirstname(rowSet.getString("firstname"));
                bean.setEmail(rowSet.getString("email"));
                bean.setPhonenumber(rowSet.getString("phonenumber"));
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
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String addJson(String data){
        Gson gson = new Gson();
        Map<String, String> newContact = gson.fromJson(data, Map.class);
        String firstname = newContact.get("FIRSTNAME");
        String lastname = newContact.get("LASTNAME");
        String email = newContact.get("EMAIL");
        String phonenumber = newContact.get("PHONENUMBER");

        String sql = "INSERT INTO ADDRESSESEMAIL (FIRSTNAME, LASTNAME, EMAIL, PHONENUMBER) VALUES (?,?,?,?)";
        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, email);
            pstmt.setString(4, phonenumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newAddressId = generatedKeys.getInt(1);
                    Map<String, Object> result = new HashMap<>();
                    result.put("ADDRESSID",newAddressId);
                    result.put("status","success");
                    return gson.toJson(result);
                }else{
                    return "{\"status\": \"error\", \"message\": \"Failed to retrieve new ADDRESSID.\"}";
                }
            }else{
                return "{\"status\": \"error\", \"message\": \"Failed to insert new record.\"}";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }

    }
}

