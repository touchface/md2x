package top.touchface.md2x.render;

import top.touchface.md2x.entity.Options;

/**
 * Abstract render
 * 
 * @author touchface
 * @date 2018-09-26 21:00
 */
public abstract class Renderer {
	protected Options options;

	public Renderer(Options options) {
		this.options = options;
	}

	public abstract String code(String code, String lang, boolean escaped);

	public abstract String blockquote(String quote);

	public abstract String html(String html);

	public abstract String heading(String text, int depth, String raw);

	public abstract String hr();

	public abstract String list(String body, boolean ordered, String start);

	public abstract String listitem(String text);

	public abstract String checkbox(boolean checked);

	public abstract String paragraph(String text);

	public abstract String table(String header, String body);

	public abstract String tablerow(String content);

	public abstract String tablecell(String content, boolean flags_header, String flags_align);

	public abstract String strong(String text);

	public abstract String em(String text);

	public abstract String codespan(String text);

	public abstract String br();

	public abstract String del(String text);

	public abstract String link(String href, String title, String text);

	public abstract String image(String href, String title, String text);

	public abstract String text(String text);
}
