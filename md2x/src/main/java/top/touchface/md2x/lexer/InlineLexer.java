package top.touchface.md2x.lexer;

import java.util.Map;
import java.util.regex.Pattern;

import top.touchface.md2x.entity.Link;
import top.touchface.md2x.entity.Options;
import top.touchface.md2x.render.Renderer;
import top.touchface.md2x.rule.InlineRules;
import top.touchface.md2x.rule.Rule;
import top.touchface.md2x.utils.Helper;
import top.touchface.md2x.utils.RegexUtils;

/**
 * 对行内元素进行分析和编译
 * 
 * @author touchface
 * @date 2018-09-26 20:56
 */
public class InlineLexer {
	private Options options;
	private Map<String, Link> links;
	private boolean inLink = false;
	private Renderer renderer = null;
	private InlineRules rules=null;
	/**
	 * 对行内元素解析器进行设置
	 * @param links 定义的链接
	 * @param options 设置
	 */
	public InlineLexer(Map<String, Link> links, Options options) {
		this.options = options;
		this.links = links;
		this.rules=new InlineRules();
		this.renderer = options.renderer;
		
		if (this.options.pedantic) {
			this.rules.setPedantic();
			
		} else if (this.options.gfm) {
			
			if (this.options.breaks) {
				this.rules.setGfmBreaks();
				
			} else {
				this.rules.setGfm();
			}
		}
	}
	/**
	 * 解析编译
	 * 
	 * @param src 行内元素
	 * @return 解析后得到的HTML
	 */
	public String output(String src) {

		String out = "", text = "", href, title;
		String cap[];
		while (src.length() > 0) {
			// escape
			if ((cap = this.rules.escape.exec(src)) != null) {
				src = src.substring(cap[0].length());
				out += cap[0];
				
				continue;
			}

			// autolink
			if ((cap = this.rules.autolink.exec(src)) != null) {

				src = src.substring(cap[0].length());
				if ("@".equals(cap[2])) {
					text = Helper.escape(this.mangle(cap[1]), false);
					href = "mailto:" + text;
				} else {
					text = Helper.escape(cap[1], false);
					href = text;
				}
				out += this.renderer.link(href, null, text);
				
				continue;
			}
			// url (gfm)
			if (this.rules.url!=null&&!this.inLink && (cap = this.rules.url.exec(src)) != null) {
				cap[0] = this.rules._gfm_backpedal.exec(cap[0])[0];
				src = src.substring(cap[0].length());
				if ("@".equals(cap[2])) {
					text = Helper.escape(cap[0], false);
					href = "mailto:" + text;
				} else {
					text = Helper.escape(cap[0], false);
					if ("www.".equals(cap[1])) {
						href = "http://" + text;
					} else {
						href = text;
					}
				}
				out += this.renderer.link(href, null, text);
				
				continue;
			}
			// tag
			if ((cap = this.rules.tag.exec(src)) != null) {

				if (!this.inLink && RegexUtils.test("^<a ", Pattern.CASE_INSENSITIVE, cap[0])) {
					this.inLink = true;
				} else if (this.inLink && RegexUtils.test("^<\\/a>", Pattern.CASE_INSENSITIVE, cap[0])) {
					this.inLink = false;
				}
				src = src.substring(cap[0].length());

				if (this.options.sanitize) {
					if (this.options.sanitizer != null) {
						
						out += this.options.sanitizer.sanitize(cap[0]);
					} else {
						out += Helper.escape(cap[0], false);
					}
				} else {
					out += cap[0];
				}
				
				continue;
			}
			// link
			if ((cap = this.rules.link.exec(src)) != null) {
				src = src.substring(cap[0].length());
				this.inLink = true;
				href = cap[2];
				if (this.options.pedantic) {
					String[] link = new Rule("^([^'\"]*[^\\s])\\s+(['\"])(.*)\\2").exec(href);

					if (link != null) {
						href = link[1];
						title = link[3];
					} else {
						title = "";
					}
				} else {
					title = cap[3] != null ? cap[3].substring(1, cap[3].length() - 1) : "";
				}
				
				href = href.trim().replaceFirst("^<([\\s\\S]*)>$", "$1");
				out += this.outputLink(cap, new Link(title, href));
				this.inLink = false;
				
				continue;
			}
			// reflink, nolink
			if ((cap = this.rules.reflink.exec(src)) != null || (cap = this.rules.nolink.exec(src)) != null) {
				src = src.substring(cap[0].length());

				String link_tag = "";
				if (cap.length > 2) {
					link_tag = cap[2].replaceAll("\\s+", " ");
				} else if (cap.length > 1) {
					link_tag = cap[1].replaceAll("\\s+", " ");
				}
				Link link = this.links.get(link_tag.toLowerCase());
				if (link == null || link.href == null) {

					out += "" + cap[0].charAt(0);
					src = cap[0].substring(1) + src;
					continue;
				}
				this.inLink = true;

				out += this.outputLink(cap, link);
				this.inLink = false;
				
				continue;
			}
			// strong
			if ((cap = this.rules.strong.exec(src)) != null) {
				src = src.substring(cap[0].length());

				if (cap.length>4&&cap[4] != null) {
					text = this.output(cap[4]);
				} else if (cap.length>3&&cap[3] != null) {
					text = this.output(cap[3]);
				} else if (cap.length>2&&cap[2] != null) {
					text = this.output(cap[2]);
				} else if (cap.length>1&&cap[1] != null) {
					text = this.output(cap[1]);
				}
				out += this.renderer.strong(text);
				
				continue;
			}
			// em
			if ((cap = this.rules.em.exec(src)) != null) {
				src = src.substring(cap[0].length());

				if (cap.length>6&&cap[6] != null) {
					text = this.output(cap[6]);
				} else if (cap.length>5&&cap[5] != null) {
					text = this.output(cap[5]);
				} else if (cap.length>4&&cap[4] != null) {
					text = this.output(cap[4]);
				} else if (cap.length>3&&cap[3] != null) {
					text = this.output(cap[3]);
				} else if (cap.length>2&&cap[2] != null) {
					text = this.output(cap[2]);
				} else if (cap.length>1&&cap[1] != null) {
					text = this.output(cap[1]);
				}
				out += this.renderer.em(text);
				
				continue;
			}
			// code
			if ((cap = this.rules.code.exec(src)) != null) {
				src = src.substring(cap[0].length());
				out += this.renderer.codespan(Helper.escape(cap[2].trim(), true));
				
				continue;
			}

			// br
			if ((cap = this.rules.br.exec(src)) != null) {
				src = src.substring(cap[0].length());
				out += this.renderer.br();
				
				continue;
			}

			// del (gfm)
			if (this.rules.del!=null&&(cap = this.rules.del.exec(src)) != null) {
				src = src.substring(cap[0].length());
				out += this.renderer.del(this.output(cap[1]));
			
				continue;
			}
			// text
			if ((cap = this.rules.text.exec(src)) != null) {
				src = src.substring(cap[0].length());
				out += this.renderer.text(Helper.escape(this.smartypants(cap[0]), false));
				
				continue;
			}

			if (src.length() > 0) {
				System.out.println("md2x:Infinite loop on byte: " + src.charAt(0));

				break;
			}
		}
		
		return out;
	}
	/**
	 * 编译链接(编译为a标签或img)
	 * 
	 * @param cap
	 * @param link
	 * @return rendered link(string)
	 */
	public String outputLink(String cap[], Link link) {
		String href = link.href, title = (link.title != null ? Helper.escape(link.title, false) : null);
		if (cap[0].charAt(0) != '!') {

			return this.renderer.link(href, title, this.output(cap[1]));
		} else {
			return this.renderer.image(href, title, Helper.escape(cap[1], false));
		}

	}
	
	/**
	 * Smartypants Transformations
	 * 
	 * @param text
	 * @return
	 */
	public String smartypants(String text) {
		
		if (!this.options.smartypants) {
			return text;
		} else {
			return text.replaceAll("---", "\\u2014").replaceAll("--", "\u2013")
					.replaceAll("(^|[-\\u2014/(\\[{\"\\s])'", "$1\\u2018").replaceAll("'", "\u2019")
					.replaceAll("(^|[-\\u2014/(\\[{\\u2018\\s])\"", "$1\\u201c").replaceAll("\"", "\u201d")
					.replaceAll("\\.{3}", "\u2026");
		}
	}
	/**
	 * Mangle Links
	 * 
	 * @param text
	 * @return
	 */
	public String mangle(String text) {
		if (!this.options.mangle) {
			return text;
		}
		String out = "";
		int length = text.length();
		int i = 0;
		int c;
		String ch;
		for (; i < length; i++) {
			c = text.charAt(i);
			if (Math.random() > 0.5) {
				ch = "x" + Integer.toHexString(c);
			} else {
				ch = "" + c;
			}
			out += "&#" + ch + ";";
		}
		return out;
	}
}
