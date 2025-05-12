
import java.util.HashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="Bean")
@ViewScoped
public class Bean {
    private String username;
    private String password;
    private String resultMessage="未登入";
    private HashMap<String,String> userPassword = new HashMap<>();
    private boolean loggedIn = false;
    
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getResultMessage(){
        return resultMessage;
    }
    
    public String getPassword(){
        return "";
    }
    public void setPassword(String password){
        this.password = password;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void signIn(){
        if(userPassword.containsKey(username)&&userPassword.get(username).equals(password)){
            resultMessage="已登入, 用戶: "+username;
            loggedIn = true;
            clearFields();
        }else{
            resultMessage="用戶或密碼錯誤";
            clearFields();
            loggedIn = false;
        }
    }

    public void signOut(){
        resultMessage="未登入";
        clearFields();
        loggedIn = false;
    }

    public void signUp(){
        if(!userPassword.containsKey(username)){
            userPassword.put(username, password);
            resultMessage="已登入, 用戶: "+username;
            clearFields();
            loggedIn = true;
        }else{
            resultMessage="用戶名已註冊";
            clearFields();
            loggedIn = false;
        } 
    }

    private void clearFields() {
        username = "";
        password = "";
    }
}
