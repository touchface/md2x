package top.touchface.md2x.render;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import top.touchface.md2x.entity.Options;
import top.touchface.md2x.utils.Helper;
/**
 * Html render
 * 
 * @author touchface
 * @date 2018-09-26 21:00
 */
public class HtmlRenderer extends Renderer {


	public HtmlRenderer(Options options) {
		super(options);
	}
	
	@Override
	public String code(String code, String lang, boolean escaped) {
		if (this.options.highlight != null) {
		    String out = this.options.highlight.highlight(code, lang);
		    if (out != null && out != code) {
		      escaped = true;
		      code = out;
		    }
		}
		if (lang == null) {
			return "<pre><code>" + (escaped ? code : Helper.escape(code, true)) + "</code></pre>";
		}
		return "<pre><code class=\"" + this.options.langPrefix + Helper.escape(lang, true) + "\">"
				+ (escaped ? code : Helper.escape(code, true)) + "\n</code></pre>\n";
	}
	
	@Override
	public String blockquote(String quote) {
		return "<blockquote>\n" + quote + "</blockquote>\n";
	}
	
	@Override
	public String html(String html) {
		return html;
	}
	
	@Override
	public String heading(String text, int depth, String raw) {
		if (this.options.headerIds) {
			return "<h" + depth + " id=\"" + this.options.headerPrefix + raw.toLowerCase().replaceAll("[^\\w]+", "-")
					+ "\">" + text + "</h" + depth + ">\n";
		}
		// ignore IDs
		return "<h" + depth + ">" + text + "</h" + depth + ">\n";
	}
	
	@Override
	public String hr() {

		return this.options.xhtml ? "<hr/>\n" : "<hr>\n";
	}

	@Override
	public String list(String body, boolean ordered, String start) {
		String type = ordered ? "ol" : "ul",
				startatt = (ordered && !start.equals("1")) ? (" start=\"" + start + "\"") : "";
		return "<" + type + startatt + ">\n" + body + "</" + type + ">\n";
	}

	@Override
	public String listitem(String text) {
		return "<li>" + text + "</li>\n";
	}

	@Override
	public String checkbox(boolean checked) {
		return "<input " + (checked ? "checked=\"\"" : "") + "disabled=\"\" type=\"checkbox\""
				+ (this.options.xhtml ? " /" : "") + "> ";
	}

	@Override
	public String paragraph(String text) {
		return "<p>" + text + "</p>\n";
	}

	@Override
	public String table(String header, String body) {
		if (body != null) {
			body = "<tbody>" + body + "</tbody>";
		}

		return "<table>\n" + "<thead>\n" + header + "</thead>\n" + body + "</table>\n";
	}

	@Override
	public String tablerow(String content) {

		return "<tr>\n" + content + "</tr>\n";
	}

	@Override
	public String tablecell(String content, boolean flags_header, String flags_align) {
		String type = flags_header ? "th" : "td";
		String tag = flags_align != null ? "<" + type + " align=\"" + flags_align + "\">" : "<" + type + ">";
		return tag + content + "</" + type + ">\n";
	};

	@Override
	public String strong(String text) {

		return "<strong>" + text + "</strong>";
	}

	@Override
	public String em(String text) {

		return "<em>" + text + "</em>";
	}

	@Override
	public String codespan(String text) {

		return "<code>" + text + "</code>";
	}

	@Override
	public String br() {
		return this.options.xhtml ? "<br/>" : "<br>";
	}

	@Override
	public String del(String text) {
		return "<del>" + text + "</del>";
	}

	@Override
	public String link(String href, String title, String text) {
		if (this.options.sanitize) {
			String prot;
			try {

				prot = URLDecoder.decode(Helper.unescape(href), "UTF-8");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
				return text;
			}
			prot = prot.replaceAll("[^\\w:]", "").toLowerCase();
			if (prot.indexOf("javascript:") == 0 || prot.indexOf("vbscript:") == 0 || prot.indexOf("data:") == 0) {
				return text;
			}
		}
		if (this.options.baseUrl != null && !Helper.originIndependentUrl(href)) {
			href = Helper.resolveUrl(this.options.baseUrl, href);
		}

		try {
			// 还原URI保留字符
			href = URLEncoder.encode(href, "UTF-8").replace("%2B", "+").replace("%2F", "/").replace("%3F", "?")
					.replace("%25", "%").replace("%23", "#").replace("%36", "&").replace("%3D", "=")
					.replace("%3A", ":");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return text;
		}
		String out = "<a href=\"" + Helper.escape(href, false) + "\"";
		if (title != null) {
			out += " title=\"" + title + "\"";
		}
		out += ">" + text + "</a>";
		return out;
	};

	@Override
	public String image(String href, String title, String text) {

		if (this.options.baseUrl != null && !Helper.originIndependentUrl(href)) {
			href = Helper.resolveUrl(this.options.baseUrl, href);
		}
		String out = "<img src=\"" + href + "\" alt=\"" + text + "\"";
		if (title != null) {
			out += " title=\"" + title + "\"";
		}
		out += this.options.xhtml ? "/>" : ">";
		return out;
	}

	@Override
	public String text(String text) {
		return text;
	}

}
