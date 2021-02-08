package cn.com.bbut.iy.itemmaster.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换工具
 */
public class DateConvert {
    /**
     * TODO 默认格式化字符串 今后可能需要修改为参数配置 yyyy-MM-dd HH:mm:ss
     */
    public static String CONST_FORMAT = "yyyy/MM/dd";

    /**
     * 将当前时间按照默认格式格式化为字符串
     *
     * @return 格式化后字符串
     */
    public static String ToString() {
        return DateConvert.ToString(new Date());
    }

    /**
     * 将当前时间按照指定格式格式化为字符串
     *
     * @param pattern
     *            格式化
     * @return 格式化后字符串
     */
    public static String ToString(String pattern) {
        return DateConvert.ToString(new Date(), pattern);
    }

    /**
     * 将指定时间按照默认格式化模式格式化为字符串
     *
     * @param date
     *            指定时间
     * @return 格式化后字符串
     */
    public static String ToString(Date date) {
        return DateConvert.ToString(date, CONST_FORMAT);
    }

    /**
     * 将指定日期按照指定样式格式化为字符串
     *
     * @param date
     *            指定日期
     * @param pattern
     *            指定样式
     * @return 格式化后字符串
     */
    public static String ToString(Date date, String pattern) {
        if (StringUtil.IsBlank(pattern))
            pattern = CONST_FORMAT;
        SimpleDateFormat _dateFormat = new SimpleDateFormat(pattern);
        return _dateFormat.format(date);
    }

    /**
     * 从字符串转换为日期 如无，则返回一个默认日期
     *
     * @param str
     *            日期字符串
     * @param format
     *            格式化字符串
     * @return 日期
     */
    public static Date FromString(String str, String format) {
        Date _date = new Date();
        DateFormat _df = new SimpleDateFormat(format);
        try {
            _date = _df.parse(str);
        } catch (ParseException e) {
            // TODO 这里需要记录错误
            _df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                _date = _df.parse(str);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return _date;
    }

    /**
     * 从字符串转换为日期 如无，则返回一个默认日期
     *
     * @param str
     *            日期字符串
     * @return 日期
     */
    public static Date FromString(String str) {
        return FromString(str, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 比较两个日期时间的大小 返回较大的时间 如果相等返回传入的第一个时间 否则返回默认时间
     *
     * @param date1
     *            ，date2
     * @return 日期
     */
    public static Date Compare(String date1, String date2) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date _da1 = sdf.parse(date1);
            Date _da2 = sdf.parse(date2);
            if (_da1.getTime() > _da2.getTime()) {
                return _da1;
            } else if (_da1.getTime() < _da2.getTime()) {
                return _da2;
            } else {
                return _da1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * @Description: 对日期字符串进行格式化转换
     * @param dateValue
     *            日期值（文本字符串）
     * @param oldFormat
     *            当前的格式化方式
     * @param newFormat
     *            转换后的格式化方式
     * @return String 返回类型
     */
    public static String ChangeFormat4Date(String dateValue, String oldFormat,
                                           String newFormat) {
        String _dateString = dateValue;
        try {
            Date _date = DateConvert.FromString(dateValue, oldFormat);
            _dateString = DateConvert.ToString(_date, newFormat);
        } catch (Exception e) {

        }
        return _dateString;
    }

    /**
     *
     * @Description: 进行日期增减
     * @param oldDate
     *            历史日期
     * @param addDay
     *            增加日期
     * @return
     */
    public static Date AddDay(Date oldDate, int addDay) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTime(oldDate);
        // System.out.println(calendar.get(Calendar.DAY_OF_MONTH));// 今天的日期
        _calendar.set(Calendar.DAY_OF_MONTH,
                _calendar.get(Calendar.DAY_OF_MONTH) + 1);// 让日期加1
        // System.out.println(_calendar.get(Calendar.DATE));// 加1之后的日期Top
        return _calendar.getTime();
    }
}
