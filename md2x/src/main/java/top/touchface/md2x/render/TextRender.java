package top.touchface.md2x.render;

import top.touchface.md2x.entity.Options;
/**
 * 文本渲染器
 * 
 * @author touchface
 * date 2018-09-26 21:01
 */
public class TextRender extends HtmlRenderer{

	public TextRender(Options options) {
		super(options);
	}
	@Override
	public String text(String text) {
		return text;
	}
	@Override
	public String br() {
		
		return "";
	}
	@Override
	public String image(String href, String title, String text) {
		
		return text;
	}
	
}
