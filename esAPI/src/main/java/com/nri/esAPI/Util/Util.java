package com.nri.esAPI.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.nri.esAPI.DTO.RealTimeDto;
import com.nri.esAPI.ServiceImpl.RealTimeInventoryServiceImpl;

public class Util{

    private static Connection connection = null;
    private static PreparedStatement statement = null;
    private static ResultSet query = null;
    private static ResultSetMetaData metaData = null;
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private static Properties props;
    static{
        loadProps();
    }
    /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     * @param time
     * @return
     */
    public static Integer StringToTimestamp(String time){
           int times = 0;
           try {  
               times = (int) ((Timestamp.valueOf(time).getTime())/1000);  
           } catch (Exception e) {  
               e.printStackTrace();  
           }
           if(times==0){
            System.out.println("String转10位时间戳失败");
           }
           return times; 
    }
    
    /**
     * Date转为String(yyyy-MM-dd HH:mm:ss)
     * 
     * @param inesDate
     * @return
     */
    public static String DateToString(Date inesDate) {
         String dateStr = "";
         //Date date = new Date();
         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         try {
          dateStr = dateFormat.format(inesDate);
          System.out.println(dateStr);
         } catch (Exception e) {
          e.printStackTrace();
         }
         return dateStr;
    }
    
    synchronized static private void loadProps(){
        props = new Properties();
        InputStream in = null;
        try {
            in = Util.class.getResourceAsStream("/property.properties");
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            props.load(inputStreamReader);
        } catch (FileNotFoundException e) {
            logger.error("property.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("property.properties文件流关闭出现异常");
            }
        }
    }
    
    public static String getProperty(String key){
        loadProps();
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
    
    /**
     * <p>检查字符串是否为空.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  需要进行检查的字符串
     * @return 如果传入的str为null、空、空格，则返回<code>true</code> , 
     * @since 2.0
     */
    public static boolean IsBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    /**
             *   通过反射吧dto转为map
     * @return
     */
    public static <T> Map<String, Object> dtoToMap(T data) {
        Map<String, Object> result = new HashMap<String, Object>();
        Class<?> dataClass = data.getClass();
        //得到属性
        Field[] field = null;
        try {
            field = dataClass.getDeclaredFields();
            for (Field field2 : field) {
                //打开私有访问
                field2.setAccessible(true);
                //获取属性
                String name = field2.getName();
                if (name.equals("createYmdHms")) {
                    continue;
                }
                //获取属性值
                Object subjectData = field2.get(data);
                if (field2.getType() == Float.class && subjectData == null) {
                    subjectData = 0f;
                }
                if (field2.getType() == String.class && subjectData == null) {
                    subjectData = "";
                }
                if (name.equals("inEsTime")) {
                    subjectData = Util.DateToString((Date)subjectData);
                    subjectData = Util.StringToTimestamp((String)subjectData);
                }else if (name.equals("impFlag")) {
                    subjectData = "0";
                }else if (name.equals("posFlg")) {
                    subjectData = "N";
                }
                char[] charArray = name.toCharArray();
                for (char char1 : charArray) {
                    if(Character.isUpperCase(char1)) {
                        name = name.replaceFirst(char1+"","_"+Character.toLowerCase(char1));
                    }
                }
                result.put(name,subjectData);
            }
        } catch (Exception e) {
            logger.info("DTO 转换 Map 报错");
            e.printStackTrace();
        }
        
        return result;
    }

    public static Connection getConn() {
        String driver = "org.postgresql.Driver";
        String url = Util.getProperty("jdbc.url");
        String username = Util.getProperty("jdbc.username");
        String password = Util.getProperty("jdbc.password");
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获取集合
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public <T> List<T> getList(String sql, Class<T> clazz, Object...params){
        List<T> list = new ArrayList<>();
        try {
            connection = getConn();
            statement = connection.prepareStatement(sql);
            for(int i = 0;i<params.length;i++){
                statement.setObject(i+1, params[i]);
            }
            query = statement.executeQuery();
            metaData = query.getMetaData();
            int column = metaData.getColumnCount();
            while(query.next()){
                T t = clazz.newInstance();
                for(int i = 1;i<=column;i++){
                    String name = metaData.getColumnName(i);
                    Object value = query.getObject(i);
                    Field field = clazz.getDeclaredField(name);
                    field.setAccessible(true);
                    String colType=field.getType().toString();
                    if(colType.equals("class java.lang.Float")) {
                        field.set(t, DecimalConvertToFloat(value));
                    }
                    else {
                        field.set(t, value);
                    }
                }
                list.add(t);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;
    }

    private float DecimalConvertToFloat(Object object) {
        BigDecimal tt = new BigDecimal(object.toString()) ;

        float kk = tt.floatValue();
        return kk;
    }

    /**
     * 增删改 操作
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int operateDB(String sql,Object...params) throws SQLException{
        int value = -1;
        try {
            connection = getConn();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);

            for(int i = 0;i<params.length;i++){
                statement.setObject(i+1, params[i]);
            }
            //System.out.println(sql);
            value = statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if(connection!=null){
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            connection.close();
        }
        return value;
    }
}
