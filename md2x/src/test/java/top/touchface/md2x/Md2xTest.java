package top.touchface.md2x;

import java.io.*;

import org.junit.Test;

import top.touchface.md2x.entity.Options;

public class Md2xTest {

    @Test
    public void test1() {
        // 读取markdown文本
        String markdown=this.readMarkdown("md2x.md");
        System.out.println(markdown);
        System.out.println("----------------------");

        // 创建对解析器的设置对象
        Options option=new Options();

        // 创建解析器对象
        Md2x md2x = new Md2x(option);

        // 循环解析Markdown文本
        int num=1000;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            md2x.parse(markdown);
        }
        long end = System.currentTimeMillis();
        System.out.println("total millis:" + (end - begin));
        System.out.println("average millis:" + (end - begin)/num);
    }

    /**
     * 获取resource目录下的Markdown文件
     *
     * @param name MARKDOWN文件名
     * @return MARKDOWN文本
     */
    private String readMarkdown(String name) {
        ClassLoader classLoader=this.getClass().getClassLoader();
        File file=new File(classLoader.getResource(name).getFile());
        if (file.isFile() && file.exists()) {
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String line;
                StringBuilder sb = new StringBuilder();
                while (null != (line = bufferedReader.readLine())) {
                    sb.append(line).append("\n");
                }

                bufferedReader.close();
                return sb.toString();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {
            System.out.println("md2x:file not found!");
        }
        return null;
    }

}
