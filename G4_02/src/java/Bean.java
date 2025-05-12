import java.sql.*;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

// 定義 DataSource
@DataSourceDefinition(
    name = "java:global/jdbc/addressDB",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/addressDB",
    databaseName = "addressDB",
    user = "meowwu",
    password = "meowwu"
)

@ManagedBean(name="bean")
@ViewScoped 
public class Bean {
    private String lastName="";
    private String firstName="";
    private String email="";
    private String phoneNumber="";
    private String status;
    private int id = 1;
    
    // 注入 DataSource
    @Resource(lookup = "java:global/jdbc/addressDB")
    private DataSource dataSource;
    
    public ResultSet getAddressList() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            PreparedStatement getRecords = connection.prepareStatement(
                "SELECT LASTNAME, FIRSTNAME, EMAIL, PHONENUMBER FROM ADDRESSESEMAIL"
            );

            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getRecords.executeQuery());

            return rowSet;
        } finally {
            connection.close();
        }
    }
    
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    public int CountTable() throws SQLException{
        Connection conn = dataSource.getConnection();
        PreparedStatement count = conn.prepareStatement("SELECT COUNT(*) FROM ADDRESSESEMAIL");
        ResultSet num = count.executeQuery();
        num.next();
        return num.getInt(1);
    }
    
    public void newAddress() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement addEntry = connection.prepareStatement(
                "INSERT INTO ADDRESSESEMAIL (LASTNAME, FIRSTNAME, EMAIL, PHONENUMBER) VALUES (?, ?, ?, ?)"
            );
            addEntry.setString(1, lastName);
            addEntry.setString(2, firstName);
            addEntry.setString(3, email);
            addEntry.setString(4, phoneNumber);

            addEntry.executeUpdate();
            int num = CountTable();
            
            lastName = "";
            firstName = "";
            email = "";
            phoneNumber = "";
            
            status = "新增成功，已將資料存進第" + num + "筆資料";
            id=1;
        } catch (SQLException e) {
            status = "新增失敗：" + e.getMessage();
            throw e;
        }
    }

    
    public void nextAddress() throws SQLException{
        Connection connection = dataSource.getConnection();
        PreparedStatement check = connection.prepareStatement(
            "SELECT * FROM ADDRESSESEMAIL WHERE ADDRESSID = ?"
        );
        check.setInt(1, id+1);
        ResultSet rs = check.executeQuery();
        if(rs.next()){
            id++;
            PreparedStatement load = connection.prepareStatement(
                "SELECT LASTNAME, FIRSTNAME, EMAIL, PHONENUMBER FROM ADDRESSESEMAIL WHERE ADDRESSID = ?"
            );
            load.setInt(1, id);
            ResultSet data = load.executeQuery();
            if (data.next()) {
                lastName = data.getString("LASTNAME");
                firstName = data.getString("FIRSTNAME");
                email = data.getString("EMAIL");
                phoneNumber = data.getString("PHONENUMBER");
                status = "移到第 " + id + " 筆資料";
            }
        }
        else{
            status = "已經是最後一筆資料了";
        }
    }
    
    public void previousAddress() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement load = connection.prepareStatement("SELECT LASTNAME, FIRSTNAME, EMAIL, PHONENUMBER FROM ADDRESSESEMAIL WHERE ADDRESSID = ?");
            load.setInt(1, id);
            ResultSet data = load.executeQuery();

            if (data.next()) {
                lastName = data.getString("LASTNAME");
                firstName = data.getString("FIRSTNAME");
                email = data.getString("EMAIL");
                phoneNumber = data.getString("PHONENUMBER");
                status = "移到第 " + id + " 筆資料";
            } 
            else {
                status = "資料遺失，請檢查資料庫";
            }
        }
        if (id <= 1) {
            status = "已經是第一筆資料了";
            return;
        }
        id--; // id 減 1
    }
     
    public void delete()throws SQLException{
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement load = connection.prepareStatement("delete from addressesemail where lastname = ?");
            load.setString(1, lastName);
            load.executeUpdate();
            status="刪除成功";
        }catch (SQLException e) {
            status = "刪除失敗：" + e.getMessage();
            throw e;
        }
    }
    
    public void revise() throws SQLException{
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement load = connection.prepareStatement("update addressesemail set lastname = ?, firstname=?, email=?, phonenumber=? where lastname=?");
            load.setString(1, lastName);
            load.setString(2, firstName);
            load.setString(3, email);
            load.setString(4, phoneNumber);
            load.setString(5, lastName);
            load.executeUpdate();
            status="修改成功";
        }catch (SQLException e) {
            status = "修改失敗：" + e.getMessage();
            throw e;
        }
    }
    
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void clearFields(){
        lastName="";
        firstName="";
        email="";
        phoneNumber="";
        status = "已清除欄位值";
    }
}
