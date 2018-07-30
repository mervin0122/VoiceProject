package com.cn.mcc.controller;

import com.cn.mcc.bean.Employee;
import com.cn.mcc.service.EmployeeService;
import com.cn.mcc.utils.BaseController;
import com.cn.mcc.utils.Config;
import net.sf.json.JSONObject;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.lucene6.AnsjAnalyzer;
import org.ansj.lucene6.AnsjAnalyzer.TYPE;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@RestController
public class LuceneIndex extends BaseController {
    @Autowired
    EmployeeService employeeService;
    /*
    * 初始化Lucene的索引
    * */
  /*  public void initLuceneindex() throws Exception {

        EmployeeService employeeService = new EmployeeService();
        List<Employee> list=new ArrayList<>();
        // 获取列表
        list = employeeService.empList();

        if (list != null && list.size() > 0) {
            for (Employee entity : list) {

                if (list != null && list.size() > 0) {
                    IndexWriter writer = getWriter(entity.getId().toString());
                    // 清除所有索引
                    writer.deleteAll();
                    // 关闭IndexWrite，Lucene的IndexWrite是线程安全，使用完不关闭的话，后面再打开IndexWrite就会报错
                    writer.close();
                    // 添加索引
                    addIndex(entity.getId().toString(), entity.getLastName());
                }
            }
        }
    }*/

    /*
    * 初始化Lucene的索引
    * */
    @RequestMapping(value="/lucene/init",method = RequestMethod.POST)
    @ResponseBody
    public com.cn.mcc.utils.Result initLuceneindexById(@RequestBody Employee employee) {
        String code="200";
        String msg="";
        JSONObject jo = new JSONObject();

        Employee emp=new Employee();
        try {
            emp = employeeService.getEmp(employee.getId());

            if (emp != null ) {
                IndexWriter writer = getWriter(employee.getId().toString());
                // 清除所有索引
                writer.deleteAll();
                // 关闭IndexWrite，Lucene的IndexWrite是线程安全，使用完不关闭的话，后面再打开IndexWrite就会报错
                writer.close();
                // 添加索引
                addIndex(employee.getId().toString(), emp);
                msg="生成索引成功";
            } else {
                code="0";
                msg="查询不到答案信息，无法生成索引";

            }
        } catch (Exception e) {
            e.printStackTrace();
            code="0";
            msg="异常错误：" + e.getMessage();
        }
        return  result(code,msg);
    }
    /**
     * 添加索引和数据
     */
    public void addIndex(String strId, Employee employee) throws Exception {

        IndexWriter writer = getWriter(strId);
        Document doc = new Document();
        /**
         * yes是会将数据存进索引，如果查询结果中需要将记录显示出来就要存进去，如果查询结果
         * 只是显示标题之类的就可以不用存，而且内容过长不建议存进去
         */
        doc.add(new StringField("lastName", employee.getLastName(), Field.Store.YES));
        doc.add(new StringField("email", employee.getEmail().toString(), Field.Store.YES));
        doc.add(new StringField("gender", employee.getGender().toString(), Field.Store.YES));
        writer.addDocument(doc);
        writer.commit();
        writer.close();
    }
    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter(String strId) throws Exception {

        /**
         * 生成的索引，可以根据自己的需要放在具体位置
         */
        Directory dir = FSDirectory.open(Paths.get(Config.getInstance().get("INDEX_FILE_PATH").trim()+ "//Busid" + strId));
        PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new AnsjAnalyzer(TYPE.index_ansj));
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, iwc);

        return writer;
    }

    /*
      * 查询关键字
      * */
    @RequestMapping(value="/lucene/search",method = RequestMethod.POST)
    @ResponseBody
    public com.cn.mcc.utils.Result searchRecord(@RequestBody Employee employee) throws Exception {//String strId, String content
        List<Employee> employees = new ArrayList<Employee>();
        String code="200";
        String msg="";
        try {
        Directory dir = FSDirectory.open(Paths.get(Config.getInstance().get("TTS_URL").trim()+ "//Busid" + employee.getId()));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        Result parse = IndexAnalysis.parse(employee.getLastName());
        for (org.ansj.domain.Term term : parse.getTerms()) {
            // 通配符匹配，类似Oracle的 like '%cccc%'
            Term t = new Term("lastName", "*" + term.getName() + "*");
            Query query = new WildcardQuery(t);
            //多个条件，用或查询
            booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        }
        TopDocs hits = is.search(booleanQuery.build(), 100, Sort.RELEVANCE);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            Employee emp = new Employee();
            emp.setGender(Integer.parseInt(doc.get(("gender"))));
            emp.setLastName(doc.get(("lastName")));
            emp.setEmail(doc.get(("email")));
            employees.add(emp);
        }
        } catch (Exception e) {
            e.printStackTrace();
            code="0";
            msg="异常错误：" + e.getMessage();
        }
        return  result(code,msg,employees);
    }


    /**
     * 根据Id更新索引
     */
    public void updateIndex(String strId,String lastName) throws Exception {

        Document doc = new Document();
        IndexWriter writer = getWriter(strId);

        /**
         * yes是会将数据存进索引，如果查询结果中需要将记录显示出来就要存进去，如果查询结果
         * 只是显示标题之类的就可以不用存，而且内容过长不建议存进去
         * 使用TextField类是可以用于查询的。
         */
        doc.add(new StringField("lastName", lastName, Field.Store.YES));

        writer.close();
    }

    /**
     * 删除指定Id的索引
     */
    public void deleteIndex(String strBusid, String cId) throws Exception {
        IndexWriter writer = getWriter(strBusid);
        writer.deleteDocuments(new Term("id", cId));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        writer.close();
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

    public static void main(String[] args) throws Exception {
        LuceneIndex l = new LuceneIndex();
//        l.getKeyWord("我要办理宽带");
        // 初始化索引
//        long sTime = System.currentTimeMillis();    //获取开始时间
        //l.initLuceneindexByBusid("1");
//        l.initLuceneindex();
//        long startTime = System.currentTimeMillis();    //获取开始时间
//        System.out.println("初始化时间：" + (startTime - sTime) + "ms");


//        System.out.println(l.getKeyWord("昨天阳光万里，而且长城很长"));
//        long endTime = System.currentTimeMillis();    //获取结束时间

        Result parse = IndexAnalysis.parse("昨天阳光万里，而且长城很长");

//        for (org.ansj.domain.Term term : parse.getTerms()) {
////
//            System.out.println(term.getName());
//            List<mccAnsEntity> mccAnsEntityList = l.searchRecord("1306", "*"+term.getName()+"*");
//
//            System.out.println(mccAnsEntityList.size());
//
//            for (mccAnsEntity ansEntity : mccAnsEntityList) {
//                System.out.println("cId:" + ansEntity.getcId());
//                System.out.println("cTitle:" + ansEntity.getcTitle());
//                System.out.println("cContent:" + ansEntity.getcContent());
//                System.out.println("========================================================");
//            }
//        }

//        List<mccAnsEntity> mccAnsEntityList = l.searchRecord("1330", "单位");
//
//        for (mccAnsEntity ansEntity : mccAnsEntityList) {
//            System.out.println("cId:" + ansEntity.getcId());
//            System.out.println("cTitle:" + ansEntity.getcTitle());
//            System.out.println("cContent:" + ansEntity.getcContent());
//            System.out.println("========================================================");
//        }

//        System.out.println("查询时间：" + (endTime - startTime) + "ms");
    }

}
