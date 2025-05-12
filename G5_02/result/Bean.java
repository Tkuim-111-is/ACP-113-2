
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.inject.Named;

@DataSourceDefinition(
        name = "java:global/jdbc/G5_02",
        className = "org.apache.derby.jdbc.ClientDataSource",
        url = "jdbc:derby://localhost:1527/G5_02",
        databaseName = "G5_02",
        user = "G5_02",
        password = "G5_02")

@Named("Bean")
@javax.faces.view.ViewScoped
public class Bean implements Serializable {

    private String authorLastName = "";
    private String title = "";
    private String status = "";
    private boolean showResults = false; // 初始值为 false，表示不显示查询结果

    public boolean isShowResults() {
        return showResults;
    }
    @Resource(lookup = "java:global/jdbc/G5_02")
    DataSource dataSource;
    
    public void query() {
        try {
            // 觸發查詢，結果存儲於狀態屬性中
            ResultSet resultSet = getBookList();
            if (!resultSet.next()) { // 檢查是否有結果
                status = "查無資料";
                showResults = false;
            } else {
                status = "查詢成功 - " + new java.util.Date();
                showResults = true;
            }
        } catch (SQLException ex) {
            status = "查詢失敗: " + ex.getMessage();
            showResults = false;
        }
    }
    
    public ResultSet getBookList() throws SQLException {
        if (dataSource == null) {
            status = "Unable to obtain to DataSource";
            throw new SQLException("Unable to obtain to DataSource");
        }
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            status = "Unable to connect to DataSource";
            throw new SQLException("Unable to connect to DataSource");
        }
        
        try {
            String sql = (
                    "SELECT t.COPYRIGHT, a.LASTNAME, t.ISBN, t.TITLE, t.EDITIONNUMBER "+
                    "From AUTHORISBN ai "+
                    "JOIN TITLES t ON t.ISBN = ai.ISBN "+
                    "JOIN AUTHORS a ON a.AUTHORID = ai.AUTHORID "
            );
            List<String> conditions = new ArrayList<>();
            if (!authorLastName.isEmpty()) conditions.add("a.LASTNAME = ?");
            if (!title.isEmpty()) conditions.add("t.TITLE = ?");
            if (!conditions.isEmpty()) {
                sql+=(" WHERE ")+(String.join(" AND ", conditions));
            }
            
            PreparedStatement getRecords = connection.prepareStatement(sql);
            int index = 1;
            if(!authorLastName.isEmpty()) getRecords.setString(index++, authorLastName);
            if(!title.isEmpty()) getRecords.setString(index++, title);
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getRecords.executeQuery());
            return rowSet;
        } finally {
            connection.close();
        }
    }

    public String getAuthorLastName(){
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }
}
