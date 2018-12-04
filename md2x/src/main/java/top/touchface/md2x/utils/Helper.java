package top.touchface.md2x.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author touchface
 * date 2018-09-26 21:38
 */
public class Helper {

    /**
     * 解析表格
     *
     * @param tableRow 表格一行的Markdown标记
     * @param count    表格的列数
     * @return 分割好的表格内容
     */
    public static List<String> splitCells(String tableRow, int count) {

        String cells[] = tableRow.replaceAll("([^\\\\])\\|", "$1 |").split(" +\\| *");
        for (int i = 0; i < cells.length; i++) {
            cells[i] = cells[i].replaceAll("\\\\\\|", "|");
        }
        List<String> lcells = StringUtils.arrayToList(cells);
        if (lcells.size() > count) {

            lcells = lcells.subList(0, count);
        } else {
            while (lcells.size() < count) {
                lcells.add("");
            }
        }

        return lcells;
    }

    /**
     * 分割单元格
     *
     * @param tableRow 表格一行的Markdown标记
     * @return 分割好的表格内容
     */
    public static List<String> splitCells(String tableRow) {
        String cells[] = tableRow.replaceAll("([^\\\\])\\|", "$1 |").split(" +\\| *");
        for (int i = 0; i < cells.length; i++) {
            cells[i] = cells[i].replaceAll("\\\\\\|", "|");
        }
        return StringUtils.arrayToList(cells);
    }

    /**
     * 解析单元格的居中方式，将单元格中的居中方式转换为left center right
     *
     * @param align 控制单元格居中方式的表格项目
     */
    public static void parseCellsAlign(List<String> align) {
        if (align == null) {
            return;
        }
        for (int i = 0; i < align.size(); i++) {

            if (RegexUtils.test("^ *-+: *$", align.get(i))) {
                align.set(i, "right");
            } else if (RegexUtils.test("^ *:-+: *$", align.get(i))) {
                align.set(i, "center");
            } else if (RegexUtils.test("^ *:-+ *$", align.get(i))) {
                align.set(i, "left");
            } else {
                align.set(i, null);
            }
        }
    }


    /**
     * @param html   html文本
     * @param encode 是否编码
     * @return 编码后的字符串
     */
    public static String escape(String html, boolean encode) {
        return html.replaceAll(!encode ? "&(?!#?\\w+;)" : "&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#39;");
    }

    /**
     * 解码操作
     *
     * @param html 需要进行解码的html文本
     * @return 解码后的文本
     */
    public static String unescape(String html) {
        html = RegexUtils.replaceAll(html,
                "&(#(?:\\d+)|(?:#x[0-9A-Fa-f]+)|(?:\\w+));?",
                Pattern.CASE_INSENSITIVE, 1, (index, text) -> {
                    text = text.toLowerCase();
                    if (text.equals("colon")) {
                        return "|";
                    }
                    if (text.charAt(0) == '#') {
                        return text.charAt(1) == 'x' ? "" + (char) (int) Integer.valueOf(text.substring(2), 16)
                                : "" + (char) (int) Integer.valueOf(text.substring(1));
                    }
                    return "";
                });
        return html;
    }

    // 创建一个Map对象用于存放对象正确的根路径
    private static Map<String, String> baseUrls;

    static {
        baseUrls = new HashMap<>();
    }

    /**
     * 判断路径是否为完整的链接
     * 如果是完整的链接返回true
     * 否则返回false
     *
     * @param href 测试的路径
     * @return 结果
     */
    public static boolean originIndependentUrl(String href) {
        return RegexUtils.test("^$|^[a-z][a-z0-9+.-]*:|^[?#]", Pattern.CASE_INSENSITIVE, href);
    }

    /**
     * 生成完整的路径
     *
     * @param base 根路径
     * @param href 路径
     * @return 完整路径
     */
    public static String resolveUrl(String base, String href) {
        if (!baseUrls.containsKey(base)) {
            if (RegexUtils.test("^[^:]+:\\/*[^/]*$", base)) {
                baseUrls.put(base, base + "/");
            } else {
                baseUrls.put(base, base.replaceAll("[^/]*$", ""));
            }
        }
        base = baseUrls.get(base);
        href = href.replace("\\", "/");
        if (href.substring(0, 2).equals("//")) {
            return base.replaceAll(":[\\s\\S]*", ":") + href;
        } else if (href.charAt(0) == '/') {
            return base.replaceAll("(:/*[^/]*)[\\s\\S]*", "$1") + href;
        } else {
            return base + href;
        }
    }
}
