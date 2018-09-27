package top.touchface.md2x.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * String tools
 * 
 * @author touchface
 * @date 2018-09-26 21:42
 */
public class StringUtils {
	
	/**
	 * Splits the string array into a string with the specified connector.
	 * example:
	 * 		String arr[]={"A","B","C"};
	 * 		String str=join(arr,"&")
	 * 		>> str="A&B&C"
	 * 
	 * @param arr
	 * @param join
	 * @return
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
	 * Parse List To Array
	 * @param list
	 * @return
	 */
	public static String[] listToArray(List<String> list) {
		String arr[]=new String[list.size()];
		for (int i = 0; i <list.size(); i++) {
			arr[i]=list.get(i);
		}
		return arr;
		
	}
	/**
	 * Parse Array To List
	 * @param arr
	 * @return
	 */
	public static List<String> arrayToList(String arr[]) {
		List<String> list=new ArrayList<String>();
		for(String a:arr) {
			list.add(a);
		}
		return list;
		
	}
}
