/**
 * ClassName  Utils
 *
 * History
 * Create User: Shiy
 * Create Date: 2014年1月13日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * 
 * @author songxz
 */
@Slf4j
public final class TimeUtil {
    private static final String FORMAT_SHORTDATE = "yyyy-MM-dd";
    private static final String FORMAT_SHORTDATE_CN = "yyyy年MM月dd日";
    private static final String FORMAT_SHORTDATE2 = "yyyy/MM/dd";
    private static final String FORMAT_ONLINE_DATE = "yyyyMMdd";
    private static final String FORMAT_LONGTDATE = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_LONGTDATE_SLANTING = "yyyy/MM/dd HH:mm:ss";
    private static final String LONGDATEFMT = "yyyyMMddHHmmssSSS";
    private static final String FORMAT_LONGTDATE_SHORT = "yyyy-MM-dd HH:mm";
    private static final FastDateFormat DATE_FORMATER_SHORT = FastDateFormat
            .getInstance(FORMAT_SHORTDATE);
    private static final FastDateFormat DATE_FORMATER_SHORT_CN = FastDateFormat
            .getInstance(FORMAT_SHORTDATE_CN);
    private static final FastDateFormat DATE_FORMATER_ONLINE_DATE = FastDateFormat
            .getInstance(FORMAT_ONLINE_DATE);
    private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HHmm");

    /** 20180101 **/
    public static final String FORMATSTRDATETOMAP_KEY_1 = "1";
    /** 2018/01/01 */
    public static final String FORMATSTRDATETOMAP_KEY_2 = "2";
    /** 得到年份2018 */
    public static final String FORMATSTRDATETOMAP_KEY_3 = "3";
    /** 得到月份8 */
    public static final String FORMATSTRDATETOMAP_KEY_4 = "4";

    private static final ZoneId zoneId = ZoneId.systemDefault();;

    public static Date getDate() {
        return new Date();
    }

    /**
     * 得到当前日期所在月的最后一天
     * 
     * @param date
     *            字符串：yyyyMMdd
     * @return yyyyMMdd
     */
    public static String getNowDateLastDay(String str_date) {
        Date nowdate = foormatOnlineDateTo(str_date);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar ca = Calendar.getInstance();
        ca.setTime(nowdate);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last;
    }

    /**
     * 得到当前年份的最后一天
     * 
     * @param date
     *            int：yyyy
     * @return yyyyMMdd
     */
    public static String getNowYearLastDay(Integer year) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        String last = format.format(currYearLast.getTime());
        return last;
    }

    /**
     * 格式化字符串日期返回各种类型
     * 
     * @param date
     *            20180101
     * @return key=1 20180101 ,key=2 2018/01/01, key=3 yyyy ,key=4 MM
     */
    public static Map<String, String> formatStrDateToMap(String date) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(FORMATSTRDATETOMAP_KEY_1, date);
        map.put(FORMATSTRDATETOMAP_KEY_2, getStrDateByIntDate(date));
        map.put(FORMATSTRDATETOMAP_KEY_3, date.substring(0, 4));
        map.put(FORMATSTRDATETOMAP_KEY_4, date.substring(4, 6));
        return map;
    }

    /**
     * 将字符串 yyyyMMdd格式的日期转为 MM/dd
     * 
     * @param yyyMMdd
     * @return
     */
    public static String formatStrDateToMMdd(String yyyMMdd) {
        return yyyMMdd.substring(4, 6) + "/" + yyyMMdd.substring(6, 8);
    }

    /**
     * 日期格式化为八位数字
     * 
     * @param dateDate
     *            yyyy/mm/dd
     * @return yyyymmdd
     */
    public static Integer foormatOnlineDate(Date dateDate) {
        String strDate = DATE_FORMATER_ONLINE_DATE.format(dateDate);
        return Integer.parseInt(strDate);
    }

    /**
     * 返回日期是星期几
     * 
     * @param date
     *            (yyyy/MM/dd)
     * @return
     */
    public static String getTheWeek(String dates) {
        Date date = null;
        String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
        Calendar cal = Calendar.getInstance();
        try {
            date = new SimpleDateFormat(FORMAT_SHORTDATE2).parse(dates);
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            return weekDays[w];
        } catch (ParseException e) {
            log.error("日期转型错误", e);
            return null;
        }
    }

    /**
     * 返回日期是星期几
     * 
     * @param dates
     *            yyyyMMdd
     * @return
     */
    public static String getTheWeekNotFormat(String dates) {
        Date date = null;
        String[] weekDays = { "日", "一", "二", "三", "四", "五", "六" };
        Calendar cal = Calendar.getInstance();
        try {
            date = new SimpleDateFormat(FORMAT_ONLINE_DATE).parse(dates);
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            return weekDays[w];
        } catch (ParseException e) {
            log.error("日期转型错误", e);
            return null;
        }
    }

    /**
     * 8位数字格式化为日期
     * 
     * @param int_date
     *            格式：yyyyMMdd
     * @return Date()
     */
    public static Date foormatOnlineDateTo(String int_date) {
        Date date = null;
        try {
            date = new SimpleDateFormat(FORMAT_ONLINE_DATE).parse(int_date);
        } catch (ParseException e) {
            log.error("日期转型错误", e);
            return null;
        }
        return date;
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     *            格式：yyyy-MM-dd HH:mm:ss
     * @return String类型：yyyy-MM-dd
     */
    public static String formatShortDate(String str_date) {
        Date date = null;
        if (str_date != null) {
            try {
                date = new SimpleDateFormat(FORMAT_SHORTDATE).parse(str_date);
            } catch (ParseException e) {
                log.error("日期转型错误", e);
                return null;
            }
        }
        return DATE_FORMATER_SHORT.format(date);
    }

    /**
     * 日期格式化 注：yyyy/MM/dd
     * 
     * @param String类型
     *            ：yyyy/MM/dd
     * @return date
     */
    public static Date getShortDate2(String str_date) {
        Date date = null;
        if (str_date != null) {
            try {
                date = new SimpleDateFormat(FORMAT_SHORTDATE2).parse(str_date);
            } catch (ParseException e) {
                log.error("日期转型错误", e);
                return null;
            }
        }
        return date;
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     *            格式：yyyy-MM-dd HH:mm:ss
     * @return String类型：yyyy-MM-dd
     */
    public static String formatShortDate2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SHORTDATE2);
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 将Date 类型转换成 yyyymmdd字符串类型
     * 
     * @param date
     * @return yyyymmdd
     */
    public static String formatShortDate3(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_ONLINE_DATE);
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     * @return yyyy-MM-dd
     */
    public static String formatShortDate(Date date) {
        return DATE_FORMATER_SHORT.format(date);
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     * @return yyyy年MM月dd日
     */
    public static String formatShortDateCN(Date date) {
        return DATE_FORMATER_SHORT_CN.format(date);
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     *            yyyy-MM-dd HH:mm:ss
     * @return String类型 yyyy-MM-dd HH:mm:ss
     */
    public static String formatLongDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_LONGTDATE);
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     *            yyyy-MM-dd HH:mm:ss
     * @return String类型 yyyy-MM-dd HH:mm:ss
     */
    public static String formatSlantingDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_LONGTDATE_SLANTING);
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 文件名 年月日时分秒毫秒 注：yyMMdd
     * 
     * @param date
     * 
     * @return String类型 yyyyMMddHHmmssSSS
     */
    public static String formatFileNameByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(LONGDATEFMT);
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 日期格式化 注：yyMMdd
     * 
     * @param date
     *            yyyy-MM-dd HH:mm:ss
     * @return String类型 yyyy-MM-dd HH:mm
     */
    public static String formatLongDateShort(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_LONGTDATE_SHORT);
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    /**
     * 
     * @param dateStr
     *            日期字符串格式： yyyy-MM-dd HH:mm:ss
     * @return Date类型：yyyy-MM-dd格式的日期
     */
    public static Date getShortDate(String dateStr) {
        Date date = null;
        if (dateStr != null) {
            try {
                date = new SimpleDateFormat(FORMAT_SHORTDATE).parse(dateStr);
            } catch (ParseException e) {
                log.error("日期转型错误", e);
                return null;
            }
        }
        return date;
    }

    /**
     * 转换字符串日期格式
     * 
     * @param dateStr
     *            yyyyMMdd
     * @return yyyy/MM/dd
     */
    public static String getStrDateByIntDate(String dateStr) {
        return dateStr.substring(0, 4) + "/" + dateStr.substring(4, 6) + "/"
                + dateStr.substring(6, 8);
    }

    /**
     * 转换字符串日期格式
     * 
     * @param dateStr
     *            yyyyMMdd
     * @return yyyy/MM/dd
     */
    public static String getStrDateByIntDayCn(String dateStr) {
        return dateStr.substring(4, 6) + "月" + dateStr.substring(6, 8) + "日";
    }

    /**
     * 
     * @param dateStr
     *            日期字符串格式： yyyy-MM-dd HH:mm:ss
     * @return Date类型：yyyy-MM-dd HH:mm:ss 格式的日期
     */
    public static Date getLongDate(String dateStr) {
        Date date = null;
        if (dateStr != null) {
            try {
                date = new SimpleDateFormat(FORMAT_LONGTDATE).parse(dateStr);
            } catch (ParseException e) {
                log.error("日期转型错误", e);
                return null;
            }
        }
        return date;
    }

    /**
     * 根据日期字符串返回小时
     * 
     * @param dateStr
     *            格式：日期字符串格式： yyyy-MM-dd HH:mm:ss
     * @return 返回数字类型的HH,如09返回9
     */
    public static int convertDateStrtoHour(String dateStr) {
        String hourStr = null;
        if (dateStr != null) {
            String dateShort = dateStr.substring(0, 13);
            hourStr = dateShort.substring(dateShort.length() - 2);
        }
        return Integer.valueOf(hourStr);
    }

    /**
     * 根据日期返回小时
     * 
     * @param inputDate
     *            格式：Date类型 yyyy-MM-dd HH:mm:ss
     * @return 返回数字类型的HH,如09返回9
     */
    public static int convertDateStrtoHour(Date inputDate) {
        String hourStr = null;
        String dateStr = null;
        if (inputDate != null) {
            dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inputDate);
        } else {
            return 0;
        }
        String dateShort = dateStr.substring(0, 13);
        hourStr = dateShort.substring(dateShort.length() - 2);
        return Integer.valueOf(hourStr);
    }

    /**
     * 根据日期字符串返回天
     * 
     * @param dateStr
     *            格式：2014-11-25 16:11:12
     * @return 返回数字类型的dd,如09返回9
     */
    public static int convertDateStrtoDay(String dateStr) {
        String dayStr = null;
        if (dateStr != null) {
            String dateShort = dateStr.substring(0, 10);
            dayStr = dateShort.substring(dateShort.length() - 2);
        }
        return Integer.valueOf(dayStr);
    }

    /**
     * 根据日期字符串返回月
     * 
     * @param dateStr
     *            格式：2014-11-25 16:11:12
     * @return 返回数字类型的MM,如09返回9
     */
    public static int convertDateStrtoMonth(String dateStr) {
        String monthStr = null;
        if (dateStr != null) {
            String dateShort = dateStr.substring(0, 7);
            monthStr = dateShort.substring(dateShort.length() - 2);
        }
        return Integer.valueOf(monthStr);
    }

    /**
     * 根据日期字符串返回年
     * 
     * @param dateStr
     *            格式：2014-11-25
     * @return int类型：数字类型的yyyy
     */
    public static int convertDateStrtoYear(String dateStr) {
        String yearStr = null;
        if (dateStr != null) {
            yearStr = dateStr.substring(0, 4);
            // yearStr = dateShort.substring(dateShort.length()-2) ;
        }
        return Integer.valueOf(yearStr);
    }

    /**
     * 根据日期，返回这一天在一年中的周数
     * 
     * @param source
     */
    public static int getWeekOfYearByshortDate(String source) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long time = 0;
        try {
            time = sdf.parse(source).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 根据指定日期，返回该日期所在周的周日的日期。格式：
     * 
     * @param dateStr
     *            格式:yyyy-MM-dd
     * @return String类型：yyyy-MM-dd
     */
    public static String convertWeekByDate(String dateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Date time;
        String imptimeEnd = null;
        try {
            time = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); //
            // 输出要计算日期
            cal.setFirstDayOfWeek(Calendar.MONDAY);// 按中国的习惯每周第一天是星期一
            int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天

            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            // String imptimeBegin = sdf.format(cal.getTime());
            // System.out.println("所在周星期一的日期：" + imptimeBegin);

            cal.add(Calendar.DATE, 6);
            imptimeEnd = sdf.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return imptimeEnd;

    }

    /**
     * 格式化字符串（去掉换行符号\n)
     * 
     * @param str
     * @return
     */
    public static String formatLogString(String str) {
        boolean exists = str.contains("\\n");
        while (exists) {
            str.replace("\\n", "");
            exists = str.contains("\\n");
        }
        return str;
    }

    /**
     * 计算加N天的日期
     * 
     * @param day
     * @param n
     * 
     * @return 加N天的日期
     * 
     */
    public static Date daysBeforen(Date day, int n) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(day);
        rightNow.add(Calendar.DAY_OF_YEAR, n);// 日期加n天
        Date date = rightNow.getTime();
        return date;
    }

    /**
     * 计算两个日期相差的天数
     * 
     * @param d1
     *            日期1 不可为null
     * @param d2
     *            日期2 不可为null
     * 
     * @return d1与d2的日期差，可能为负数
     * 
     */
    // public static int daysBetween(Date d1, Date d2) {
    //
    // LocalDate date1 = LocalDate.fromDateFields(d1);
    // LocalDate date2 = LocalDate.fromDateFields(d2);
    // Period p = new Period(date1, date2, PeriodType.days());
    // return p.getDays();
    // }

    /**
     * 判断指定日期距离今日是否在指定天数内
     * <p>
     * 如： 指定日期为 2015.6.1<br/>
     * 限定日数为 7 <br/>
     * 则 当日 < 2015.6.7 时 返回true 当日为 2015.6.8 时 返回true 当日 > 2015.6.9 时 返回false
     * </p>
     * 
     * @param d
     *            指定日期
     * @param days
     *            限定日数
     * @return
     */
    // public static boolean isWithInDays(Date d, int days) {
    // LocalDate date = LocalDate.fromDateFields(d);
    // Period p = new Period(LocalDate.now(date.getChronology()),
    // date.plusDays(days),
    // PeriodType.days());
    //
    // return p.getDays() >= 0 ? true : false;
    // }

    /**
     * 根据日期得到当前日期所在月的所有日期
     * 
     * @param date
     *            yyyyMMdd 字符串
     * @return 集合yyyyMMdd int类型
     */
    public static List<Integer> getMonthEachDayByDate(String date) {
        // 月初
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(date));
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startDay = format.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            String endDay = format.format(calendar.getTime());
            // calendar.getminmo
            // 月末
            return getDateListByStartAndEnd(startDay, endDay);
        } catch (ParseException e) {
            log.error("日期转型错误", e);
            return null;
        }
    }

    /**
     * 根据起始时间与结束时间得到期间的所有日期
     * 
     * @param start
     *            起始（20160101）
     * @param end
     *            结束（20160107）
     * @return
     */
    public static List<Integer> getDateListByStartAndEnd(String start, String end) {
        List<Integer> dates = new ArrayList<>();
        // 格式化起始与结束时间
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date startDate;
        try {
            startDate = df.parse(start);
            startCalendar.setTime(startDate);
            dates.add(Integer.parseInt(df.format(startCalendar.getTime())));
            Date endDate = df.parse(end);
            endCalendar.setTime(endDate);
            while (true) {
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                if (startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()) {
                    dates.add(Integer.parseInt(df.format(startCalendar.getTime())));
                } else {
                    break;
                }
            }
            return dates;
        } catch (ParseException e) {
            log.error("日期转型错误", e);
            return null;
        }
    }

    /**
     * 计算日期，并返回当前日期的显示格式
     * 
     * @param
     * @return 上午/下午/星期/月日
     */
    public static String getDateByParam(Date d) {
        Date now = new Date();// 取得系统时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 设置时间格式
        SimpleDateFormat sdf_m_d = new SimpleDateFormat("MM月dd日"); // 设置时间格式
        Calendar cal = Calendar.getInstance();

        // 格式化 当前系统时间与传入的事时间
        Integer nowStr = Integer.parseInt(sdf.format(now));
        Integer inStr = Integer.parseInt(sdf.format(d));

        // 判断传入的日期是否与当前的日期相等，如果相等，返回当前日期时间是上午或下午。
        if (inStr.equals(nowStr)) {
            cal.setTime(d);
            // 取得时间段编号，0上午，1下午
            int am_pm = cal.get(GregorianCalendar.AM_PM);
            switch (am_pm) {
            case 0:
                return "上午";
            case 1:
                return "下午";
            default:
                return "今天";
            }
        } else {
            // 对比日期不相等
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Integer yesterday = Integer.parseInt(sdf.format(cal.getTime()));
            // 判断当前日期是否为昨天的日期。
            if (inStr.equals(yesterday)) {
                return "昨天";
            } else {
                // 与昨天的日期不相等
                cal.setTime(now);
                cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
                int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
                cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
                Integer monday = Integer.parseInt(sdf.format(cal.getTime()));
                // 判断当前输入的日期是否小于系统当前时间，如果小于则使用“月日”
                if (inStr < monday) {
                    String md = sdf_m_d.format(d);
                    return md;
                } else {
                    // 不小于当前系统时间，使用星期表示
                    String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
                    cal.setTime(d);
                    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
                    if (w < 0) {
                        w = 0;
                    }
                    return weekDays[w];
                }
            }
        }
    }

    /**
     * 得到指定日期的上个月1日
     * 
     * @param d
     * @return date
     */
    public static Date getLastMonthDateByDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        return cal.getTime();
    }

    /**
     * 获取前n年同期
     *
     * @param day
     * @param n
     *
     * @return 加N年的日期
     *
     */
    public static Date yearsBeforen(Date day, int n) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(day);
        rightNow.add(Calendar.YEAR, n);// 日期加n天
        Date date = rightNow.getTime();
        return date;
    }

    /**
     * 
     * @param dateStr
     * @return
     */
    public static Date transferFromStringToDate(String dateStr, DateTimeFormatter formatter) {
        ZonedDateTime zonedDateTime = null;
        if (DateTimeFormatter.ISO_LOCAL_DATE.equals(formatter)) {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            zonedDateTime = localDate.atStartOfDay(zoneId);
        } else if (DateTimeFormatter.ISO_DATE_TIME.equals(formatter)) {
            LocalDateTime localTime = LocalDateTime.parse(dateStr, formatter);
            zonedDateTime = localTime.atZone(zoneId);
        }
        if (zonedDateTime == null) {
            return null;
        }
        Date date = Date.from(zonedDateTime.toInstant());
        return date;
    }

    /**
     * 验证给定时间是否在限定时间段内
     * 
     * @param time
     *            LocalTime 要验证的时间
     * @param from
     *            LocalTime HHmm格式的开始时间
     * @param to
     *            LocalTime HHmm格式的结束时间
     * @param isNextDay
     *            是否跨天, true 跨天 false 同天
     * @return boolean from <= time <= to 返回 true 否则 返回 false
     */
    public static boolean isInTime(LocalTime time, String from, String to, boolean isNextDay) {
        LocalTime fromTime = LocalTime.parse(from, HHMM);
        LocalTime toTime = LocalTime.parse(to, HHMM);

        if (isNextDay) {
            return !time.isBefore(fromTime) || !time.isAfter(toTime);
        } else {
            return !time.isBefore(fromTime) && !time.isAfter(toTime);
        }
    }

    /**
     * 验证当前时间是否在限定时间段内
     * 
     * @see {@link #isInTime(LocalTime, String, String, boolean)}
     */
    public static boolean isInTime(String from, String to, boolean isNextDay) {
        return isInTime(LocalTime.now(), from, to, isNextDay);
    }

    /**
     * 验证当前时间是否在限定时间段内(不跨天)
     *
     * @see {@link #isInTime(LocalTime, String, String, boolean)}
     */
    public static boolean isInTime(String from, String to) {
        return isInTime(from, to, false);
    }
}
