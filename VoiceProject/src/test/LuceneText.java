import com.cn.mcc.bean.Employee;
import com.cn.mcc.bean.Iat;
import com.cn.mcc.bean.Tts;
import com.cn.mcc.jiaba.JiebaSegmenter;
import com.cn.mcc.utils.HttpClientUtil;
import com.cn.mcc.utils.voice.iat.FileUtil;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
