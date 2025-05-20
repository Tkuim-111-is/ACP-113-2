package Rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import Rest.Address;

@Path("generic")
public class GenericResource {
    
    @Resource(lookup = "java:global/jdbc/G7_02")
    
    private Connection getConnection() throws Exception {
        String url = "jdbc:derby://localhost:1527/G7_02"; // 你的 DB 名稱
        String user = "G7_02";
        String password = "G7_02";
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
                System.out.println("查無資料");
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
    public String addJson(Address input) throws SQLException, Exception {
        Connection conn = getConnection();
        try{
            PreparedStatement ps = conn.prepareStatement(
                    "insert into ADDRESSESEMAIL(firstname, lastname, email, phonenumber) values(?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, input.firstname);
            ps.setString(2, input.lastname);
            ps.setString(3, input.email);
            ps.setString(4, input.phonenumber);
            
            int result = ps.executeUpdate();
            int addressid = 0;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                addressid = rs.getInt(1);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("ADDRESSID", addressid);
            response.put("status", result == 1 ? "success" : "fail");

            return new Gson().toJson(response);
        }catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}

