package cn.com.bbut.iy.itemmaster.util;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TimeUtilTest {

    @Test
    public void testTransferFromStringToDate() throws Exception {
        String dateStr = "2019-10-11 12:12:12";
        Date date = TimeUtil.transferFromStringToDate(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ss = sdf.format(date);
        Assert.assertEquals(dateStr, ss);

    }

    @Test
    public void testTransferFromStringToDate1() throws Exception {
        String dateStr = "2019-10-11";
        Date date = TimeUtil.transferFromStringToDate(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sdf.format(date);
        Assert.assertEquals(dateStr, ss);

    }

    @Test
    public void testIsInTime() {

        LocalTime nowTime = LocalTime.of(16, 0);
        Assert.assertTrue(TimeUtil.isInTime(nowTime, "1600", "2359", false));

        nowTime = LocalTime.of(23, 59);
        Assert.assertTrue(TimeUtil.isInTime(nowTime, "1600", "2359", false));

        nowTime = LocalTime.of(22, 00);
        Assert.assertTrue(TimeUtil.isInTime(nowTime, "2200", "0200", true));

        nowTime = LocalTime.of(2, 00);
        Assert.assertTrue(TimeUtil.isInTime(nowTime, "2200", "0200", true));

        nowTime = LocalTime.of(21, 59);
        Assert.assertFalse(TimeUtil.isInTime(nowTime, "2200", "0200", true));

        nowTime = LocalTime.of(2, 1);
        Assert.assertFalse(TimeUtil.isInTime(nowTime, "2200", "0200", true));

    }
}