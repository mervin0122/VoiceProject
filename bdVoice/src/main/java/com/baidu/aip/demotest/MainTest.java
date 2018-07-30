package com.baidu.aip.demotest;

import com.baidu.aip.talker.facade.Controller;
import com.baidu.aip.talker.facade.upload.LogBeforeUploadListener;

import java.io.FileInputStream;
import java.util.Properties;

public class MainTest {

    public static void main(String[] args) throws Exception {

        System.out.println("请等待程序正常退出， 否则测试用户将导致10分钟内无法正常使用。");
        String dir = "src/test/resources/pcm";
        Controller controller = new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener());

        // 选择开启 asrOne 或者 asrBoth ， asrOne即一个通话1路音频， asrBoth为1个通话，2路音频
        // 8k_test.pcm 较短  salesman.pcm 较长，客服  customer.pcm 用户
        //  BiccTest.asrOne(controller, dir + "/8k_test.pcm");
        // BiccTest.asrBoth(controller, "src/test/resources/pcm/8k_test.pcm", "src/test/resources/pcm/8k_test.pcm");

        // 请等待程序正常退出，即end包发送完成。否则测试用户将导致10分钟内无法正常使用。
        BiccTest.asrOne(controller,dir + "/8k_test.pcm");
        // BiccTest.asrOne(controller,dir + "/customer.pcm");
        // BiccTest.asrBoth(controller, dir + "/salesman.pcm", dir + "/customer.pcm");
        controller.stop();
    }
    public static String bb(String world){
        System.out.println("-----------------------------------"+world);
        return  world;
    }
    /**
     * 默认读取conf/sdk.properties, 您也可以用下面的构造方法，传入Properties类
     *
     * Controller controller =
     * new Controller(new LogBeforeUploadListener(), new PrintAfterDownloadListener(), getProperties());
     * @return
     * @throws Exception
     */
    private static Properties getProperties() throws Exception {
        String fullFilename = System.getProperty("user.dir") + "/conf/sdk.properties";
        Properties properties = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(fullFilename);
            properties.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }


}
