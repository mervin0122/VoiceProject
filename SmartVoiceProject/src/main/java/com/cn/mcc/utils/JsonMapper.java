package com.cn.mcc.utils;

/**
 * Created by zhangqingjie on 15-5-15.
 */

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 *
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 *
 */
public class JsonMapper {

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private ObjectMapper mapper;

    public JsonMapper(JsonSerialize.Inclusion inclusion) {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(inclusion);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //禁止使用int代表Enum的order()來反序列化Enum,非常危險
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
    }

    /**
     * 创建输出全部属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.NON_DEFAULT);
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNonEmptyMapper() {
        return new JsonMapper(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object)throws IOException{

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
     * @see #constructParametricType(Class, Class...)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz)throws IOException{
        if (jsonString == null || jsonString.length() == 0) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
     * @see #constructParametricType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType)throws IOException{
        if (jsonString == null || jsonString.length() == 0) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses)throws IOException {
        return (T) this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> fromJsonToList(String jsonString, Class<T> classMeta)throws IOException{
        return (List<T>) this.fromJson(jsonString,constructParametricType(List.class, classMeta));
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(JsonNode node, Class<?> parametrized, Class<?>... parameterClasses)throws IOException{
        JavaType javaType = constructParametricType(parametrized, parameterClasses);
        try {
            return (T) mapper.readValue(node, javaType);
        } catch (IOException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T pathAtRoot(String json, String path, Class<?> parametrized, Class<?>... parameterClasses)throws IOException{
        JsonNode rootNode = parseNode(json);
        JsonNode node = rootNode.path(path);
        return (T) fromJson(node, parametrized, parameterClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T pathAtRoot(String json, String path, Class<T> clazz)throws IOException{
        JsonNode rootNode = parseNode(json);
        JsonNode node = rootNode.path(path);
        return (T) fromJson(node, clazz);
    }

    /**
     * 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
     *             Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    @SuppressWarnings("unchecked")
    public <T> T update(T object, String jsonString) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object)throws IOException {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum,
     * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
     * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public void setEnumUseToString(boolean value) {
        mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
        mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    public JsonNode parseNode(String json)throws IOException{
        try {
            return mapper.readValue(json, JsonNode.class);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 输出全部属性
     * @param object
     * @return
     */
    public static String toNormalJson(Object object)throws IOException{
        return new JsonMapper(JsonSerialize.Inclusion.ALWAYS).toJson(object);
    }

    /**
     * 输出非空属性
     * @param object
     * @return
     */
    public static String toNonNullJson(Object object)throws IOException{
        return new JsonMapper(JsonSerialize.Inclusion.NON_NULL).toJson(object);
    }

    /**
     * 输出初始值被改变部分的属性
     * @param object
     * @return
     */
    public static String toNonDefaultJson(Object object)throws IOException{
        return new JsonMapper(JsonSerialize.Inclusion.NON_DEFAULT).toJson(object);
    }

    /**
     * 输出非Null且非Empty(如List.isEmpty)的属性
     * @param object
     * @return
     */
    public static String toNonEmptyJson(Object object)throws IOException{
        return new JsonMapper(JsonSerialize.Inclusion.NON_EMPTY).toJson(object);
    }

    public void setDateFormat(String dateFormat){
        mapper.setDateFormat(new SimpleDateFormat(dateFormat));
    }



}
