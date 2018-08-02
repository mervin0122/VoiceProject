import com.cn.mcc.bean.Employee;
import com.cn.mcc.utils.HttpClientUtil;
import org.junit.Test;

/**
 * Created by mervin on 2018/6/26.
 */
public class LuceneText {
    @Test
    public  void init(){
        try {
            Employee employee=new Employee();
            employee.setId(1);
            String url ="http://localhost:8080/lucene/init" ;
            String res = HttpClientUtil.getHttpData(url, employee,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void search(){
        try {
            Employee employee=new Employee();
            employee.setId(1);
            employee.setLastName("me");
            String url ="http://localhost:8080/lucene/search" ;
            String res = HttpClientUtil.getHttpData(url, employee,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
