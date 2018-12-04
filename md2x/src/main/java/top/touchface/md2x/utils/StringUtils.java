package top.touchface.md2x.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串处理工具
 * 
 * @author touchface
 * date 2018-09-26 21:42
 */
public class StringUtils {
	
	/**
	 * 将字符串数组以特定的字符连接成一个字符串
	 * example:
	 * 		String arr[]={"A","B","C"};
	 * 		String str=join(arr,"&")
	 * 		>> str="A&B&C"
	 * 
	 * @param arr 字符串数组
	 * @param join 连接符
	 * @return 拼接好的字符串
	 */
	public static String join(String[] arr,String join ) {

        StringBuffer sb=new StringBuffer();

        for(int i=0;i<arr.length;i++){

             if(i==(arr.length-1)){
            	 
                 sb.append(arr[i]);
             }else{

                 sb.append(arr[i]).append(join);

             }

        }
		return new String(sb);
	}

    /**
	 * 将字符串Array转换为List
	 * @param arr Array
	 * @return List
	 */
	public static List<String> arrayToList(String arr[]) {
		return new ArrayList<>(Arrays.asList(arr));
	}
}
