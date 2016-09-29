package com.wxy.customcalendar.utils;

import android.text.TextUtils;

/**
 * 字符串工具类
 */
public class StringUtil {


    /**
     * 得到文件url前缀
     *
     * @param type 1 from web; 2 from sdcard; 3 from content; 4 from assets; 5 from drawable
     * @return String url前缀
     */
    public static String getFileUrlHead(int type) {
        switch (type) {
            case 1:
                return "http://";
            case 2:
                return "file://";
            case 3:
                return "content://";
            case 4:
                return "assets://";
            case 5:
                return "drawable://";
        }
        return "";
    }

    /**
     * 解析浮点型数据
     *
     * @param string 待解析的数据
     * @return float 数据解析结果
     */
    public static float parseFloat(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 解析Int类型数据
     *
     * @param string 待解析的数据
     * @return int 数据解析结果
     */
    public static int parseInt(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析Double类型数据
     *
     * @param string 待解析数据
     * @return double 返回结果
     */
    public static double parseDouble(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析长整型数据
     *
     * @param string 待解析数据
     * @return long 返回结果
     */
    public static long parseLong(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解析Boolean类型数据
     *
     * @param string 待解析数据
     * @return boolean 返回结果
     */
    public static boolean parseBoolean(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(string);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 截取字符串
     *
     * @param str   待截取字符串
     * @param start 开始位置
     * @return String 截取结果
     */
    public static String subString(String str, int start) {
        if (start >= str.length())
            return str;
        else
            return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   待截取字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return String 截取结果
     */
    public static String subString(String str, int start, int end) {
        if (TextUtils.isEmpty(str) || start >= str.length() || end >= str.length())
            return str;
        else
            return str.substring(start, end);
    }

    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }


}
