package top.touchface.md2x.lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import top.touchface.md2x.entity.Link;
import top.touchface.md2x.entity.Tokens;
import top.touchface.md2x.entity.Options;
import top.touchface.md2x.entity.Token;
import top.touchface.md2x.entity.Type;
import top.touchface.md2x.rule.BlockRules;
import top.touchface.md2x.utils.Helper;
import top.touchface.md2x.utils.RegexUtils;
import top.touchface.md2x.utils.ReplaceCallback;
import top.touchface.md2x.utils.StringUtils;

/**
 * MARKDOWN块级元素分词器
 *
 * @author touchface
 * date 2018-09-26 20:50
 */
public class BlockLexer {

    private Options options;// 解析器的配置信息

    private Tokens doc;// 解析后得到的文档对象

    private BlockRules rules;

    /**
     * 对分词器进行设置
     *
     * @param options 对解析器的设置
     */
    public BlockLexer(Options options) {

        doc = new Tokens();
        this.rules = new BlockRules();
        this.options = options;
        if (this.options.pedantic) {
            this.rules.setPedantic();

        } else if (this.options.gfm) {
            if (this.options.tables) {
                this.rules.setGfm();
                this.rules.setGfmTables();
            } else {
                this.rules.setGfm();
            }
        }
    }

    /**
     * 对MARKDOWN文本进行分词处理，得到块级元素和链接
     *
     * @param src MARKDOWN文本设置
     * @return 结果
     */
    public Tokens lex(String src) {
        // 预处理文本
        src = src.replaceAll("\\r\\n|\\r", "\n").replaceAll("\\t", "    ").replaceAll("\\u00a0", " ")
                .replaceAll("\\u2424", "\n");
        // 执行分词，并返回值
        return this.token(src, true);
    }

    /**
     * 进行解析
     *
     * @param src MARKDOWN文本
     * @param top 是否为第一层级
     * @return 结果
     */
    private Tokens token(String src, boolean top) {

        String cap[];
        while (src.length() > 0) {

            // newline 换行
            if (this.rules.newline != null && (cap = this.rules.newline.exec(src)) != null) {
                src = src.substring(cap[0].length());
                if (cap[0].length() > 1) {
                    Token token = new Token();
                    token.type = Type.space;
                    this.doc.tokens.add(token);
                }
            }
            // code 代码块
            if (this.rules.code != null && (cap = this.rules.code.exec(src)) != null) {
                src = src.substring(cap[0].length());
                String text = cap[0].replaceAll("^ {4}", "");
                Token token = new Token();
                token.type = Type.code;
                token.text = text;
                this.doc.tokens.add(token);
                continue;
            }
            // fences 代码块
            if (this.rules.fences != null && (cap = this.rules.fences.exec(src)) != null) {

                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.code;
                token.lang = cap[2];
                token.text = cap.length < 3 ? "" : cap[3];
                this.doc.tokens.add(token);
                continue;

            }

            // heading 分级标题
            if (this.rules.heading != null && (cap = this.rules.heading.exec(src)) != null) {
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.heading;
                token.depth = cap[1].length();
                token.text = cap[2];
                this.doc.tokens.add(token);
                continue;
            }

            // table no leading pipe (gfm)
            if (this.rules.nptable != null && top && (cap = this.rules.nptable.exec(src)) != null) {

                Token item = new Token();
                item.type = Type.table;
                // 获取表头
                item.header = Helper.splitCells(cap[1].replaceAll("^ *| *\\| *$", ""));
                // 获取每列的文本对齐方式
                item.align = StringUtils.arrayToList(cap[2].replaceAll("^ *|\\| *$", "").split(" *\\| *"));
                // 创建表格对象
                item.cells = new ArrayList<>();
                // 获取表格的每一行
                List<String> rows;
                if (cap.length > 3) {
                    rows = StringUtils.arrayToList(cap[3].replaceAll("\\n$", "").split("\\n"));
                } else rows = new ArrayList<>();

                if (item.header.size() == item.align.size()) {
                    src = src.substring(cap[0].length());
                    // 解析表格居中方式
                    Helper.parseCellsAlign(item.align);
                    // 获取Cell中的文本
                    for (String row : rows){
                        item.cells.add(Helper.splitCells(row, item.header.size()));
                    }
                    this.doc.tokens.add(item);
                    continue;

                }

            }
            // hr 分割线
            if (this.rules.hr != null && (cap = this.rules.hr.exec(src)) != null) {
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.hr;
                this.doc.tokens.add(token);
                continue;
            }

            // blockquote 引用
            if (this.rules.blockquote != null && (cap = this.rules.blockquote.exec(src)) != null) {
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.blockquote_start;
                this.doc.tokens.add(token);

                String subsrc = RegexUtils.replaceAll(cap[0], "^ *> ?", Pattern.MULTILINE, 0, "");
                this.token(subsrc, top);
                token = new Token();
                token.type = Type.blockquote_end;
                this.doc.tokens.add(token);
                continue;
            }

            // list 列表
            if (this.rules.list != null && (cap = this.rules.list.exec(src)) != null) {

                src = src.substring(cap[0].length());
                String bull = cap[2];

                boolean isOrdered = bull.length() > 1;
                Token token = new Token();
                token.type = Type.list_start;
                token.ordered = isOrdered;
                token.start = isOrdered ? bull : "";
                this.doc.tokens.add(token);

                // 获取每一个列表项
                cap = this.rules.item.exec(cap[0]);
                boolean next = false;
                int l = cap.length;

                StringBuilder srcBuilder = new StringBuilder(src);
                for (int i = 0; i < l; i++) {

                    String item = cap[i];
                    int space = item.length();
                    //移除列表项的标记，避免被当作下一个列表项
                    item = item.replaceFirst("^ *([*+-]|\\d+\\.) +", "");
                    //移除嵌套列表前的空格，以免被识别为代码块
                    if (item.contains("\n ")) {
                        space -= item.length();

                        if (!this.options.pedantic) {

                            item = RegexUtils.replaceAll(item, "^ {1," + space + "}", Pattern.MULTILINE, 0,
                                    (index, text) -> {
                                        // 将空格替换为""避免被解析为code
                                        return "";
                                    });
                        } else {

                            item = RegexUtils.replaceAll(item, "^ {1,4}", Pattern.MULTILINE, 0,
                                    (index, text) -> {
                                        // 将空格替换为""避免被解析为code
                                        return "";
                                    });
                        }
                    }
                    // 确定下一个列表项是否在这里。
                    // 如果它不属于此列表则还原。
                    if (this.options.smartLists && (i != (l - 1))) {
                        String b = this.rules.bullet.exec(cap[i + 1])[0];
                        if (!bull.equals(b) && !(bull.length() > 1 && b.length() > 1)) {

                            String[] subcap = Arrays.copyOfRange(cap, i + 1, cap.length);
                            String str = StringUtils.join(subcap, "\n");
                            srcBuilder.insert(0, str);
                            i = l - 1;
                        }
                    }
                    boolean loose = (next || RegexUtils.test("\\n\\n(?!\\s*$)", item));
                    if (i != (l - 1)) {
                        if (item.length() > 0) {
                            next = (item.charAt(item.length() - 1) == '\n');
                        }
                        if (!loose) {

                            loose = next;
                        }
                    }
                    // Check for task list items
                    boolean isTask = RegexUtils.test("^\\[[ xX]\\] ", item);
                    boolean isChecked = false;
                    if (isTask) {
                        isChecked = item.charAt(1) != ' ';
                        item = item.replaceFirst("^\\[[ xX]] +", "");

                    }

                    token = new Token();
                    token.type = loose ? Type.loose_item_start : Type.list_item_start;
                    token.checked = isChecked;
                    token.task = isTask;
                    this.doc.tokens.add(token);

                    // Recurse.
                    this.token(item, false);
                    token = new Token();
                    token.type = Type.list_item_end;
                    this.doc.tokens.add(token);
                }
                src = srcBuilder.toString();
                token = new Token();
                token.type = Type.list_end;
                this.doc.tokens.add(token);
                continue;
            }

            // html
            if (this.rules.html != null && (cap = this.rules.html.exec(src)) != null) {
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.html;
                token.text = cap[0];
                this.doc.tokens.add(token);
                continue;
            }
            // def 定义的链接
            if (this.rules.def != null && top && (cap = this.rules.def.exec(src)) != null) {
                src = src.substring(cap[0].length());
                if (cap[3] != null) {
                    cap[3] = cap[3].substring(1, cap[3].length() - 1);
                }
                String tag = cap[1].toLowerCase().replace("\\s+", " ");
                if (this.doc.links.get(tag) == null) {
                    this.doc.links.put(tag, new Link(cap[3], cap[2]));
                }
                continue;
            }
            // table (gfm)
            if (this.rules.table != null && this.rules.table != null && top
                    && (cap = this.rules.table.exec(src)) != null) {

                Token item = new Token();
                item.type = Type.table;
                // 获取表头
                item.header = Helper.splitCells(cap[1].replaceAll("^ *| *\\| *$", ""));
                // 获取每列的文本对齐方式
                item.align = StringUtils.arrayToList(cap[2].replaceAll("^ *|\\| *$", "").split(" *\\| *"));
                // 创建表格对象
                item.cells = new ArrayList<>();
                // 获取表格的每一行
                List<String> rows;
                if (cap.length > 3) {
                    rows = StringUtils.arrayToList(cap[3].replaceAll("(?: *\\| *)?\\n$", "").split("\\n"));
                } else {
                    rows = new ArrayList<>();
                }

                if (item.header.size() == item.align.size()) {
                    src = src.substring(cap[0].length());
                    Helper.parseCellsAlign(item.align);
                    for (String row : rows) {
                        item.cells.add(Helper.splitCells(row.replaceAll("^ *\\| *| *\\| *$", ""), item.header.size()));
                    }

                    this.doc.tokens.add(item);

                    continue;
                }
            }
            // lheading 标题
            if (this.rules.lheading != null && (cap = this.rules.lheading.exec(src)) != null) {
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.heading;
                token.text = cap[1];
                token.depth = cap[2].equals("=") ? 1 : 2;
                this.doc.tokens.add(token);
                continue;
            }
            // top-level paragraph 一级段落
            if (this.rules.paragraph != null && top && (cap = this.rules.paragraph.exec(src)) != null) {
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.paragraph;
                char c = cap[1].charAt(cap[1].length() - 1);
                token.text = (c == '\n') ? cap[1].substring(0, cap[1].length() - 2) : cap[1];
                this.doc.tokens.add(token);
                continue;
            }
            // text 文本
            if (this.rules.text != null && (cap = this.rules.text.exec(src)) != null) {
                // 一级文本永远不会匹配到这里，因为会被匹配为段落
                src = src.substring(cap[0].length());
                Token token = new Token();
                token.type = Type.text;
                token.text = cap[0];
                this.doc.tokens.add(token);
                continue;
            }
            if (src.length() > 0) {
                System.out.println("md2x:Infinite loop on byte: " + src.charAt(0));
                break;
            }

        }
        return doc;
    }

}
