import java.sql.*;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@DataSourceDefinition(
    name = "java:global/jdbc/BookDB",
    className = "org.apache.derby.jdbc.ClientDataSource",
    url = "jdbc:derby://localhost:1527/BookDB",
    databaseName = "BookDB",
    user = "meowwu",
    password = "meowwu"
)

@ManagedBean(name="bean")
@ViewScoped 
public class Bean {
    private String memberID;
    private String titleISBN;
    private String status;
    
    // 注入 DataSource
    @Resource(lookup = "java:global/jdbc/BookDB")
    private DataSource dataSource;
    
    public ResultSet getShoppingCart() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            PreparedStatement getRecords = connection.prepareStatement(
                "SELECT mi.transaction_id, m.member_id, m.first_name, m.last_name, t.ISBN, t.title " +
                "FROM member_isbn mi " +
                "JOIN members m ON mi.member_id = m.member_id " +
                "JOIN titles t ON mi.ISBN = t.ISBN " +
                "ORDER BY m.first_name"
            );

            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getRecords.executeQuery());

            return rowSet;
        } finally {
            connection.close();
        }
    }

    
    public String getMemberID(){
        return memberID;
    }
    public void setMemberID(String memberID){
        this.memberID = memberID;
    }
    
    public String getTitleISBN(){
        return titleISBN;
    }
    public void setTitleISBN(String titleISBN){
        this.titleISBN = titleISBN;
    }
    
    public void add() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // 1. 查會員
            PreparedStatement getmem = connection.prepareStatement(
                "SELECT member_id FROM members WHERE member_id = ?"
            );
            getmem.setString(1, memberID);
            ResultSet mem_id = getmem.executeQuery();

            if (!mem_id.next()) {
                status = "查無會員資料，請確認會員ID";
                return;
            }
            String member_id_val = mem_id.getString("member_id");

            // 2. 查書籍
            PreparedStatement getbook = connection.prepareStatement(
                "SELECT ISBN FROM titles WHERE ISBN = ?"
            );
            getbook.setString(1, titleISBN);
            ResultSet book_id = getbook.executeQuery();

            if (!book_id.next()) {
                status = "查無書籍資料，請確認ISBN";
                return;
            }
            String isbn_val = book_id.getString("ISBN");

            // 3. 加入購物車
            PreparedStatement check = connection.prepareStatement(
                "SELECT transaction_id FROM member_ISBN WHERE member_id = ? AND ISBN = ?"
            );
            check.setString(1, member_id_val);
            check.setString(2, isbn_val);
            ResultSet num = check.executeQuery();
            PreparedStatement setshoppingcart = connection.prepareStatement(
                "INSERT INTO member_isbn (member_id, ISBN) VALUES (?, ?)"
            );
            setshoppingcart.setString(1, member_id_val);
            setshoppingcart.setString(2, isbn_val);
            setshoppingcart.executeUpdate();
            status = "購買成功";
        } catch (SQLException e) {
            status = "錯誤：" + e.getMessage();
            throw e;
        }
    }

    
    public String getStatus(){
        return status;
    }
}

/*
if(num.next()){
                int transactionId = num.getInt("transaction_id");
    
                // 2. 根據 transaction_id 更新欄位
                PreparedStatement update = connection.prepareStatement(
                    "UPDATE member_ISBN SET transaction_id = ? WHERE transaction_id = ?"
                );
                update.setInt(1, transactionId+1);
                update.setInt(2, transactionId);
                update.executeUpdate();
                status = "資料已更新";
            }
            else{
                PreparedStatement setshoppingcart = connection.prepareStatement(
                    "INSERT INTO member_isbn (member_id, ISBN) VALUES (?, ?)"
                );
                setshoppingcart.setString(1, member_id_val);
                setshoppingcart.setString(2, isbn_val);
                setshoppingcart.executeUpdate();
            }
*/
