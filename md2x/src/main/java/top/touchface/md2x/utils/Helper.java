package top.touchface.md2x.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Helpers
 * 
 * @author touchface
 * @date 2018-09-26 21:38
 */
public class Helper {
	

	public static List<String> splitCells(String tableRow, int count) {

		String cells[] = tableRow.replaceAll("([^\\\\])\\|", "$1 |").split(" +\\| *");
		List<String> lcells = StringUtils.arrayToList(cells);

		if (lcells.size() > count) {
			lcells.remove(count);
		} else {
			while (lcells.size() < count) {
				lcells.add("");
			}
		}
		return lcells;
	}

	public static List<String> splitCells(String tableRow) {
		String cells[] = tableRow.replaceAll("([^\\\\])\\|", "$1 |").split(" +\\| *");
		for (int i = 0; i < cells.length; i++) {
			cells[i] = cells[i].replaceAll("\\\\\\|", "|");
		}
		return StringUtils.arrayToList(cells);
	}
	
	public static String escape(String html,boolean encode){
		return html
				.replaceAll(!encode?"&(?!#?\\w+;)":"&","&amp;")
				.replaceAll("<","&lt;")
				.replaceAll(">","&gt;")
				.replaceAll("\"","&quot;")
				.replaceAll("'","&#39;");
	}
	public static String unescape(String html) {
		html=RegexUtils.replaceAll(html,"&(#(?:\\d+)|(?:#x[0-9A-Fa-f]+)|(?:\\w+));?",Pattern.CASE_INSENSITIVE,1,new ReplaceCallback() {
			
			@Override
			public String replace(int index, String text) {
			    text = text.toLowerCase();
			    if (text.equals("colon")) {
			    	return "|";
			    }
			    if (text.charAt(0) == '#') {
			      return text.charAt(1) == 'x'
			        ?""+(char)(int)Integer.valueOf(text.substring(2),16)
			        :""+(char)(int)Integer.valueOf(text.substring(1));
			    }
			    return "";
			}
		});
		return html;
		
	}
	public static boolean originIndependentUrl(String href) {
		return RegexUtils.test("^$|^[a-z][a-z0-9+.-]*:|^[?#]",Pattern.CASE_INSENSITIVE, href);
	}
	
	public static Map<String,String> baseUrls=new HashMap<String,String>();
	
	public static String resolveUrl(String base,String href) {
		if(!baseUrls.containsKey(base)) {
			if(RegexUtils.test("^[^:]+:\\/*[^/]*$",base)) {
				baseUrls.put(base,base+"/");
			}else {
				baseUrls.put(base,base.replaceAll("[^/]*$",""));
			}
		}
		base=baseUrls.get(base);
		if(href.substring(0,2).equals("//")) {
			return base.replaceAll(":[\\s\\S]*",":")+href;
		}else if(href.charAt(0)=='/') {
			return base.replaceAll("(:\\/*[^/]*)[\\s\\S]*", "$1") + href;
		}else {
			return base+href;
		}
	}
}
