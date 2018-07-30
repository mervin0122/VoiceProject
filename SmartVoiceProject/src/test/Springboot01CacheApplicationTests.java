
import com.cn.mcc.bean.Employee;
import com.cn.mcc.dao.EmployeeDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot01CacheApplicationTests {

	@Autowired
    EmployeeDao empolyeeMapper;

	@Test
	public void contextLoads()
	{
		Employee empByid=empolyeeMapper.getEmpById(1);
		System.out.println(empByid);
	}

}
