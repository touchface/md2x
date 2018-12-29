package top.touchface.md2x;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import top.touchface.md2x.entity.Link;
import top.touchface.md2x.entity.Options;
import top.touchface.md2x.entity.Token;
import top.touchface.md2x.entity.Tokens;
import top.touchface.md2x.entity.Type;
import top.touchface.md2x.lexer.InlineLexer;
import top.touchface.md2x.render.HtmlRenderer;
import top.touchface.md2x.render.Renderer;
import top.touchface.md2x.render.TextRender;
import top.touchface.md2x.utils.Helper;

import static java.util.Objects.*;

/**
 * 解析MARKDOWN行内标记，并编译为HTML
 *
 * @author touchface
 * date 2018-09-26 20:28
 */
class Parser {

    private LinkedList<Token> tokens;// BlockLexer得到的块级元素的标记
    private Token token;// 当前解析的Token
    private Renderer renderer;// 渲染器对象，对Token进行渲染
    private InlineLexer inlineLexer;// 行内元素解析器
    private InlineLexer textLexer;// 行内元素解析器

    /**
     * 对解析器进行设置
     *
     * @param options 设置
     */
    Parser(Options options) {
        if (options != null) {
            if (options.renderer == null) {
                options.renderer = new HtmlRenderer(options);
            }
        } else {
            options = new Options();
            options.renderer = new HtmlRenderer(options);
        }
        this.renderer=options.renderer;
        this.inlineLexer = new InlineLexer(options);
        Options opt = options.clone();
        opt.renderer = new TextRender(options);

        this.textLexer = new InlineLexer(opt);

    }

    /**
     * 解析循环
     *
     * @param tokens MARKDOWN标记的块级元素和链接
     * @return HTML
     */
    String parse(Tokens tokens) {

        // BlockLexer得到的链接
        Map<String, Link> links = tokens.links;
        this.tokens = tokens.tokens;
        inlineLexer.setLinks(links);
        inlineLexer.setLinks(links);
        StringBuilder out = new StringBuilder();
        while (this.next() != null) {
            out.append(this.parseToken());
        }
        return out.toString();
    }

    /**
     * 解析当前的标记
     *
     * @return 解析结果
     */
    private String parseToken() {

        switch (this.token.type) {

            case space:
                return "";

            case hr:
                return this.renderer.hr();

            case heading:

                return this.renderer.heading(this.inlineLexer.output(this.token.text), this.token.depth,
                        Helper.unescape(this.textLexer.output(this.token.text)));

            case code: {
                return this.renderer.code(this.token.text, this.token.lang, this.token.escaped);
            }

            case table: {

                String header = "";
                StringBuilder body = new StringBuilder();
                // header
                StringBuilder cell = new StringBuilder();
                for (int i = 0; i < this.token.header.size(); i++) {
                    cell.append(this.renderer.tablecell(this.inlineLexer.output(this.token.header.get(i)), true,
                            token.align.get(i)));
                }
                header += this.renderer.tablerow(cell.toString());

                for (int i = 0; i < this.token.cells.size(); i++) {

                    List<String> row = this.token.cells.get(i);
                    cell = new StringBuilder();
                    for (int j = 0; j < row.size(); j++) {
                        cell.append(this.renderer.tablecell(this.inlineLexer.output(row.get(j)), false,
                                this.token.align.get(j)));
                    }
                    body.append(this.renderer.tablerow(cell.toString()));
                }
                return this.renderer.table(header, body.toString());
            }

            case blockquote_start: {
                StringBuilder body = new StringBuilder();

                while (requireNonNull(this.next()).type != Type.blockquote_end) {
                    body.append(this.parseToken());
                }
                return this.renderer.blockquote(body.toString());
            }
            case list_start: {
                StringBuilder body = new StringBuilder();
                boolean ordered = this.token.ordered;
                String start = this.token.start;

                while (requireNonNull(this.next()).type != Type.list_end) {
                    body.append(this.parseToken());
                }
                return this.renderer.list(body.toString(), ordered, start);
            }
            case list_item_start: {
                StringBuilder body = new StringBuilder();

                if (this.token.task) {
                    body.append(this.renderer.checkbox(this.token.checked));
                }

                while (requireNonNull(this.next()).type != Type.list_item_end) {
                    body.append(this.token.type == Type.text ? this.parseText() : this.parseToken());
                }
                return this.renderer.listitem(body.toString());
            }

            case loose_item_start: {
                StringBuilder body = new StringBuilder();

                while (requireNonNull(this.next()).type != Type.list_item_end) {
                    body.append(this.parseToken());
                }
                return this.renderer.listitem(body.toString());
            }

            case html: {

                return this.renderer.html(this.token.text);
            }

            case paragraph: {
                return this.renderer.paragraph(this.inlineLexer.output(this.token.text));
            }

            case text: {
                return this.renderer.paragraph(this.parseText());
            }
            default:
                break;
        }
        return null;
    }

    /**
     * 解析文本标记
     *
     * @return 解析结果
     */
    private String parseText() {

        StringBuilder body = new StringBuilder(this.token.text);
        while (Objects.requireNonNull(this.peek()).type == Type.text) {
            body.append("\n").append(Objects.requireNonNull(this.next()).text);
        }

        return this.inlineLexer.output(body.toString());
    }

    /**
     * 切换到下一个标记
     *
     * @return 下一个标记
     */
    private Token next() {
        if (this.tokens.size() > 0) {
            return this.token = this.tokens.removeFirst();
        } else {
            return null;
        }
    }

    /**
     * 预览下一个标记
     *
     * @return 下一个标记
     */
    private Token peek() {
        if (tokens.size() == 0) {
            return null;
        }
        if (tokens.size() > 1) {
            return this.tokens.get(1);
        } else {
            return this.tokens.get(0);
        }
    }

}
