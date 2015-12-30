/**
 * Created by NikhilVerma on 12/10/15.
 */
public class StringCode {
    String symbol;
    int code;
    StringCode next;

    public StringCode(String symbol, int code) {
        this.symbol = symbol;
        this.code = code;
    }

    public String toString(){
        return code+":"+symbol;
    }
}
