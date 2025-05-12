import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="calculateBean")
@RequestScoped
public class HW1 {
    private String number1;
    private String number2;
    private int sum;
    private String result;
    public String getNumber1() {
        return number1;
    }
    
    public void setNumber1(String number1) {
        this.number1 = number1;
    }
    
    public String getNumber2() {
        return number2;
    }
    
    public void setNumber2(String number2) {
        this.number2 = number2;
    }
    
    public int getSum() {
        return sum;
    }
    
    public void setSum(int sum) {
        this.sum = sum;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public void checkSum() {
        int n1=Integer.parseInt(number1);
        int n2=Integer.parseInt(number2);
        if(n1<10||n1>99||n2<10||n2>99){
            result="錯誤";
            return;
        }
        int calculatedSum = n1 + n2;
        if(calculatedSum>150){
            result="總和大於150，錯誤";
            return;
        }
        result = "計算過程 : "+number1+" + "+number2+" = ";
        if (calculatedSum == sum) {
            result += sum;
        } else {
            result += "錯誤";
        }
    }
} 