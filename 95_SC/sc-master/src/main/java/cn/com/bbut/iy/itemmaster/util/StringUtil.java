package cn.com.bbut.iy.itemmaster.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * 字符串工具類
 */
public class StringUtil extends StringUtils {

    /**
     * 空字符串 <code>""</code>.
     * @since 2.0
     */
    public static final String EMPTY = "";
    
    
    /**
     * <p>如果傳入的參數為 <code>null</code>, 返回一個空字符串("").</p>
     *
     * <pre>
     * StringUtils.defaultString(null)  = ""
     * StringUtils.defaultString("")    = ""
     * StringUtils.defaultString("bat") = "bat"
     * </pre>
     *
     * @see ObjectUtils#toString(Object)
     * @see String#valueOf(Object)
     * @param str  传入的参数，可以为null
     * @return 转换后的字符串，如果传入为 <code>null</code>，则为空字符串
     */
    public static String DefaultString(String str) {
        return str == null ? EMPTY : str;
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
     * 将concatText拼接到oldText，以separator为分隔符(首位不添加该分隔符)
     * @param oldText 原有字符串
     * @param concatText 待拼接字符串
     * @param separator 分隔符
     * @return 拼接后的字符串
     */
    public static String Join(String oldText,String concatText,String separator)
    {
    	return StringUtil.IsBlank(oldText)? concatText : (oldText + separator + concatText);
    }
    
    /**
     * 将concatText拼接到oldText，以separator为分隔符(首位不添加该分隔符)
     * @param oldText 原有字符串
     * @param concatObj 待拼接对象(直接调用.toString()转换为字符串)
     * @param separator 分隔符
     * @return 拼接后的字符串
     */
    public static String Join(String oldText,Object concatObj,String separator)
    {
    	return StringUtil.IsBlank(oldText)? concatObj.toString() : (oldText + separator + concatObj.toString());
    }
    
    /**
     * 将strList列表中每一个字符串按照separator分割，拼接到oldText(首位不添加该分隔符)
     * @param strList 待拼接字符串列表
     * @param separator 分隔符
     * @return
     */
    public static String Join(Collection<String> strList,String separator)
    {
    	String _text = StringUtil.EMPTY;
    	Iterator<String> _strItr = strList.iterator();
    	while(_strItr.hasNext())
    	{
    		_text = Join(_text,_strItr.next(),separator);
    	}
    	return _text;
    }
    
    /**
     * 首字母大写
     * @param text 将首字母转为大写
     * @return 首字母大写字符串
     */
    public static String Capitalize(String text)
    {
    	String _text = text;
    	
    	_text = _text.substring(0, 1).toUpperCase()+_text.substring(1, _text.length() - 1);
    	
    	return _text;
    }
	
    /**
     * <p>Removes all occurrences of a substring from within the source string.</p>
     *
     * <p>A <code>null</code> source string will return <code>null</code>.
     * An empty ("") source string will return the empty string.
     * A <code>null</code> remove string will return the source string.
     * An empty ("") remove string will return the source string.</p>
     *
     * <pre>
     * StringUtils.remove(null, *)        = null
     * StringUtils.remove("", *)          = ""
     * StringUtils.remove(*, null)        = *
     * StringUtils.remove(*, "")          = *
     * StringUtils.remove("queued", "ue") = "qd"
     * StringUtils.remove("queued", "zz") = "queued"
     * </pre>
     *
     * @param str  the source String to search, may be null
     * @param remove  the String to search for and remove, may be null
     * @return the substring with the string removed if found,
     *  <code>null</code> if null String input
     * @since 2.1
     */
    public static String Remove(String str,String remove) {
        if (IsBlank(str) || IsBlank(remove)) {
            return str;
        }
        return Replace(str, remove, EMPTY, -1);
    }
    

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first <code>max</code> values of the search String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or <code>-1</code> if no maximum
     * @return the text with any replacements processed,
     *  <code>null</code> if null String input
     */
    public static String Replace(String text, String searchString, String replacement, int max) {
        if (IsBlank(text) || IsBlank(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
    
    /**
     * 去除数组中重复和为空的字符串
     * @param array 传入数组
     * @return 返回数组(不重复)
     */
    public static String[] DistinctArray(String[] array)
    {
    	if(array == null || array.length < 1)return array;
    	List<String> _list = new ArrayList<String>();
		for(int i = 0 ; i < array.length; i++)
		{
			String _tmp = array[i];
			if(_list.contains(_tmp) && !StringUtil.IsBlank(_tmp))
			{
				_list.add(_tmp);
			}
		}
		array = (String[]) _list.toArray();
		return array;
    }
    
    /**
     * 字符串左补齐
     * @Description:
     * @param oldText
     * @param length
     * @param parse
     * @return 参数类型
     */
    public static String Lpad(String oldText,int length,char parse)
    {
    	return StringUtil.leftPad(oldText, length, parse);
    }
    
    /**
     * 字符串右补齐
     * @Description:
     * @param oldText
     * @param length
     * @param parse
     * @return 参数类型
     */
    public static String Rpad(String oldText,int length,char parse)
    {
    	return StringUtil.rightPad(oldText, length, parse);
    }

    /**
     * yyyymmdd 格式转换为 dd/mm/yyyy格式字符串
     * @Description:
     * @param @param date
     * @param @return 参数类型
     * @return String 返回类型
     * @param date
     * @return
     */
    public static String getFormatDate(String date) {
        date = StringUtil.trimToNull(date);
        if (date == null) {
            return "";
        }
        if (date.length() != 8) {
            return date;
        }
        return date.substring(6, 8)+"/"+date.substring(4, 6)+"/"+date.substring(0, 4);
    }
}
