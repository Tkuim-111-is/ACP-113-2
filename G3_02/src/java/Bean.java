import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.annotation.sql.DataSourceDefinition;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.faces.bean.ManagedBean;

// 定義資料來源
@DataSourceDefinition(
   name = "java:global/jdbc/G3_02",
   className = "org.apache.derby.jdbc.ClientDataSource",
   url = "jdbc:derby://localhost:1527/G3_02",
   databaseName = "G3_02",
   user = "G3_02",
   password = "G3_02")

@ManagedBean(name="Bean")
public class Bean {
    
    @Resource(lookup="java:global/jdbc/G3_02")
    DataSource dataSource;
    
    private String voterName;
    private String candidateName;
    
    public ResultSet getVotesList() throws SQLException{
        if(dataSource==null){
            throw new SQLException("Unable to obtain DataSource");
        }
        
        Connection connection = dataSource.getConnection();
        
        if(connection == null){
            throw new SQLException("Unable to obtain DataSource");
        }
        
        try{
            PreparedStatement getVotesList = connection.prepareStatement(
                "SELECT VOTERNAME, CANDIDATENAME"+
                "FROM VOTES");
            
            CachedRowSet rowSet =
                    RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getVotesList.executeQuery());
            return rowSet;
        
        }finally{
            connection.close();
        }
    }
    
    public String getVoterName(){
        return voterName;
    }

    public void setVoterName(String voterName){
        this.voterName = voterName;
    }

    public String getCandidateName(){
        return candidateName;
    }

    public void setCandidateName(String candidateName){
        this.candidateName = candidateName;
    }

    public void submitVote() throws SQLException{
        if(dataSource == null){
            throw new SQLException("Unable to obtain DataSource");
        }
        
        Connection connection = dataSource.getConnection();
        
        if(connection == null){
            throw new SQLException("Unable to obtain DataSource");
        }
        
        try{
            PreparedStatement addEntry =
                    connection.prepareStatement("INSERT INTO VOTES" +
                       "(VOTER_NAME,CANDIDATE_NAME)" +
                       "VALUES (?,?)");
            addEntry.setString(1,getVoterName());
            addEntry.setString(2,getCandidateName());
            addEntry.executeUpdate();
        }finally{
            connection.close();
        }
    }
}
