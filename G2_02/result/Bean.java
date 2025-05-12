// G2_02
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="Bean")
// 使用ViewScoped使用者進行多次操作能夠保存歷史紀錄
@ViewScoped
public class Bean {
    // 定義變數
    private final ArrayList<String> messageBoard = new ArrayList<>();
    private String nickName;
    private String post;
    
    //列印留言板內容
    public String printMessageBoard(){
        return messageBoard.toString();
    }
    
    // 暱稱get;set方法
    public String getNickName(){
        return nickName;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    
    //留言get;set方法
    public String getPost(){
        return post;
    }
    public void setPost(String post){
        this.post = post;
    }
    
    // 將暱稱、留言組合後存於留言板提供前台檢視
    public void sendPost(){
        String r = "暱稱: "+nickName+"</br>留言: "+post+"</br>";
        this.messageBoard.add(r);
    }
}
