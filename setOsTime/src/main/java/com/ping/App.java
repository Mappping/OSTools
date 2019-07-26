package com.ping;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 设置系统时间
 */
public class App {
    public static void main(String[] args) {
        System.out.println("正在从授时中心获取时间戳...");
        Long netTime = getNetTime();
        System.out.println("获取成功! 时间戳为: " + netTime);

        System.out.println("正在转换日期格式...");
        Date dateTime = new Date(netTime);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        //分别取得时间中的年月日,时分秒,并输出
        System.out.println(calendar.get(Calendar.YEAR) + "年" +
                (calendar.get(Calendar.MONTH) + 1) + "月" +
                calendar.get(Calendar.DAY_OF_MONTH) + "日" +
                calendar.get(Calendar.HOUR_OF_DAY) + "时" +
                calendar.get(Calendar.MINUTE) + "分" +
                calendar.get(Calendar.SECOND) + "秒");

        System.out.println("正在设置系统时间...");
        String[] timeArray = getTimeArray(dateTime);
        String date = timeArray[0];
        String time = timeArray[1];
        String osTime = setDatetime(date, time);
        System.out.println("系统时间设置成功!设置后的时间为: " + osTime);
    }

    /**
     * 获取年月日和时分秒数组
     *
     * @param date
     * @return
     */
    public static String[] getTimeArray(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat();
        sf.applyPattern("yyyy-MM-dd HH:mm:ss");
        String format = sf.format(date);
        return format.split(" ");
    }

    /**
     * 从网络中获取时间
     *
     * @return 时间戳 毫秒值
     */
    public static Long getNetTime() {
        try {
            // 时区设置
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            //取得资源对象
            URL url = new URL("http://api.m.taobao.com/rest/api3.do?api=mtop.common.getTimestamp");
            //生成连接对象
            URLConnection uc = url.openConnection();
            //发出连接
            uc.connect();
            //取得网站日期时间（时间戳）
            long ld = uc.getDate();
            return ld;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 给系统设置时间
     *
     * @param date
     * @param time
     * @return
     */
    public static String setDatetime(String date, String time) {
        String osName = System.getProperty("os.name");
        String dateTimeMessage = date + " " + time;
        try {
            if (osName.matches("^(?i)Windows.*$")) { // Window 系统
                String cmd;

                cmd = " cmd /C date " + date; // 格式：yyyy-MM-dd
                Runtime.getRuntime().exec(cmd);

                cmd = " cmd /C time " + time; // 格式 HH:mm:ss
                Runtime.getRuntime().exec(cmd);
            } else if (osName.matches("^(?i)Linux.*$")) {// Linux 系统
                String command = "date -s " + "\"" + date + " " + time + "\"";// 格式：yyyy-MM-dd HH:mm:ss
                Runtime.getRuntime().exec(command);
            } else {

            }
        } catch (IOException e) {
            return e.getMessage();
        }
        return dateTimeMessage;
    }
}
