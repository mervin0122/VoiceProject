import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.junit.Test;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.*;

/**
 * Created by mervin on 2018/6/26.
 */
public class Test1 {
    @Test
    public  void vtt(){
        try {
            Date a = new Date();
            Iat iat=new Iat();
            iat.setFilePath("E:\\Git\\VoiceProject\\rtasr-demo\\resource\\sound_demo.pcm");
            String url ="http://localhost:8080/voice/vtt" ;
            //开放平台实时撰写业务，按并发路数收费，鸡棚，一时间允许进行实时撰写的western连接数，单价为2万元路每年
            String res = HttpClientUtil.getHttpData(url, iat,true);
            JSONObject obj= JSON.parseObject(res);
            String statusCode=obj.getString("statusCode");
            if (statusCode.equals("200")){
                Date b = new Date();
                long interval = (b.getTime() - a.getTime())/1000;
                System.out.println("相差"+interval+"秒");//会打印出相差3秒
            }
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    @Test
    public  void vysb(){
        try {
            Iat iat=new Iat();
<<<<<<< HEAD
            iat.setFilePath("msc/8k_test.pcm");
            String url ="http://localhost:8080/voice/vysb" ;
            String res = HttpClientUtil.getHttpData(url, iat,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void ws(){
        try {
            Iat iat=new Iat();
            iat.setFilePath("c:\\temp\\hts002d4c04@ch348b0eb0ece0477600.wav");
            String url ="http://localhost:8080/voice/ws" ;
=======
            iat.setFilePath("msc/655.pcm");
            String url ="http://localhost:8080/voice/ws" ;
            //开放平台实时撰写业务，按并发路数收费，鸡棚，一时间允许进行实时撰写的western连接数，单价为2万元路每年
>>>>>>> 74e89ddbc1caee1e5c663fa60ef5d0297f1ea6ec
            String res = HttpClientUtil.getHttpData(url, iat,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void iat(){
        try {
            Date a = new Date();
            Iat iat=new Iat();
            iat.setFilePath("c:\\temp\\hts00104c3b@ch16ee0ea78a96477400.wav");
            String url ="http://localhost:8080/voice/iat" ;
            //开放平台实时撰写业务，按并发路数收费，鸡棚，一时间允许进行实时撰写的western连接数，单价为2万元路每年
            String res = HttpClientUtil.getHttpData(url, iat,true);
            JSONObject obj= JSON.parseObject(res);
           String statusCode=obj.getString("statusCode");
            if (statusCode.equals("0")){
                Date b = new Date();
                long interval = (b.getTime() - a.getTime())/1000;
                System.out.println("相差"+interval+"秒");//会打印出相差3秒
            }
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void iatb(){
        try {
           // 音频文件，转二进制数组，然后Base64编码
            byte[] audioByteArray = FileUtil.read2ByteArray("c:\\temp\\tts.wav");
             String audioBase64 = new String(Base64.encodeBase64(audioByteArray), "UTF-8");
            Iat iat=new Iat();
            iat.setFilePath(audioBase64);
            String url ="http://localhost:8080/voice/iatb" ;
            String res = HttpClientUtil.getHttpData(url, iat,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void tts(){
        try {
            Tts tts =new Tts();
            tts.setAue("raw");
           tts.setTexts("如果需要使用实时识别、长语音、唤醒词、语义解析等其它语音功能，请使用Android或者iOS SDK 或 Linux C++ SDK 等，请严格按照文档里描述的参数进行开发，" +
                   "特别请关注原始录音参数以及语音压缩格式的建议，否则会影响识别率，进而影响到产品的用户体验。法国夺得冠军，我非常高兴的要去俄罗斯看球赛，法国夺得冠军!如果需要使用实时识别、长语音、唤醒词、语义解析等其它语音功能，请使用Android或者iOS SDK 或 Linux C++ SDK 等，请严格按照文档里描述的参数进行开发，" +
                   "特别请关注原始录音参数以及语音压缩格式的建议，否则会影响识别率，进而影响到产品的用户体验。法国夺得冠军，我非常高兴的要去俄罗斯看球赛，法国夺得冠军");
            String url ="http://localhost:8080/voice/tts" ;
            String res = HttpClientUtil.getHttpData(url, tts,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public  void listening(){
        try {
            String url ="http://localhost:8080/voice/listening" ;
            String res = HttpClientUtil.getHttpData(url, null,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //长语音转文字
    @Test
    public  void vic(){
        try {
            Iat iat=new Iat();
            iat.setFilePath("c:\\temp\\tts2.wav");
            String url ="http://localhost:8080/voice/vic" ;
            String res = HttpClientUtil.getHttpData(url, iat,true);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void  main(String[] args) throws IOException {
        String text = "基于java语言开发的轻量级的中文分词工具包";//开放平台实时转写业务按并发路数收费  前任拉甘送苏宁首败落后恒大6分争冠难了
        JiebaSegmenter segmenter = new JiebaSegmenter();
        System.out.println(segmenter.sentenceProcess(text));

    }
    @Test
    public void testDemo() {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }
    /* 关键字提取 */
    public String getKeyWord(String strContent) {
        StringBuilder strKeyWork = new StringBuilder();
        KeyWordComputer kwc = new KeyWordComputer(5);

        Collection<Keyword> keywordCollection = kwc.computeArticleTfidf("", strContent);
        if (keywordCollection.size() > 0) {
            for (Keyword key : keywordCollection) {
                System.out.println(key.getName());
                if (strKeyWork != null && strKeyWork.toString().length() > 0) {
                    strKeyWork.append(",'").append(key.getName()).append("'");
                } else {
                    strKeyWork.append("('").append(key.getName()).append("'");
                }
            }
            strKeyWork.append(")");
        } else {
            strKeyWork.append("");
        }

        return strKeyWork.toString();
    }
    /* 关键字提取 */
    @Test
    public void getKeyWordDemo() {
        System.out.println(getKeyWord("我不喜欢日本和服"));
    }
    /* 关键字提取 */
    @Test
    public void getKeyWordDemo1() {
        String text = "基于java语言开发的轻量级的中文分词工具包";//开放平台实时转写业务按并发路数收费  前任拉甘送苏宁首败落后恒大6分争冠难了
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String aa=segmenter.sentenceProcess(text).toString();
        System.out.println(segmenter.sentenceProcess(text));
        System.out.println(getKeyWord(aa));

    }
    public static void test(String str) {
        //只关注这些词性的词
        Set<String> expectedNature = new HashSet<String>() {{
            add("n");add("v");add("vd");add("vn");add("vf");
            add("vx");add("vi");add("vl");add("vg");
            add("nt");add("nz");add("nw");add("nl");
            add("ng");add("userDefine");add("wh");add("r");add("ns");
        }};
        //String str = "欢迎使用ansj_seg,基于java语言开发的轻量级的中文分词工具包!" ;
        Result result = ToAnalysis.parse(str); //分词结果的一个封装，主要是一个List<Term>的terms
        System.out.println(result.getTerms());

        List<Term> terms = result.getTerms(); //拿到terms
        System.out.println(terms.size());

        for(int i=0; i<terms.size(); i++) {
            String word = terms.get(i).getName(); //拿到词
            String natureStr = terms.get(i).getNatureStr(); //拿到词性
            if(expectedNature.contains(natureStr)) {
               // System.out.println(word + ":" + natureStr);
                System.out.print(word + ":" + natureStr+",");
            }
        }
    }
    @Test
    public void fenci() {
        test("我今天非常荣幸的被邀请参观中国电信广东分公司");
    }//基于java语言开发的轻量级的中文分词工具包!
}
