package top.touchface.md2x.entity;
import top.touchface.md2x.expand.Highlight;
import top.touchface.md2x.expand.Sanitizer;
import top.touchface.md2x.render.Renderer;
/**
 * 解析器设置信息类，包含对解析器的设置参数。
 * 
 * @author touchface
 * @date 2018-09-26 20:35
 */
public class Options implements Cloneable{
	
    public String baseUrl=null;
    public boolean breaks=false;
    
    public boolean gfm=true;
    public boolean headerIds=true;
    public String headerPrefix="";
    public Highlight highlight=null;//unsupported option
    public String langPrefix="language-";
    public boolean mangle=true;
    public boolean pedantic=false;
    public Renderer renderer=null;
    public boolean sanitize=false;
    public Sanitizer sanitizer=null;//unsupported option
    //public boolean silent=false;
    public boolean smartLists=false;
    public boolean smartypants=false;
    public boolean tables=true;
    public boolean xhtml=false;
	@Override
	public Options clone(){
		
		try {
			return (Options)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    

}
