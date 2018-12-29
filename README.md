## Md2x --A Java Markdown Parser

**Md2x** 是一款运行于Java平台上的轻量级markdown解析器，
能够通过简单的调用方式将markdown标记解析为HTML标记。
在代码的设计与实现参考了marked.js 的源码，使用了marked.js中的
正则表达式来分析markdown文档进行，以及相似的方法进行解析渲染，
编写它的最初目的就是为了在Java中还原一个marked.js。

### 如何使用Md2x

通过引入Md2x的jar包到你项目当中，你可以通过简单的调用将markdown标记转换为html标记。
使用本解析器需要Java8及以上的环境。

#### 1.添加md2x的jar包到项目当中
下载地址:[download](https://github.com/touchface/md2x/releases)

#### 2.创建Md2x对象，调用parse方法
##### 例1:
~~~
Md2x md2x=new Md2x();
String html=md2x.parse("## Hello,World!");
// <h2 id="hello-world">Hello,World</h2>
~~~


在创建Md2x对象时可以传入设置(Options)对象对解析器进行设置。
支持设置的参数与marked.js支持的参数保持一致，但是代码语法
高亮功能未进行实现，所以不支持该设置。

##### 例2:
~~~
Options options=new Options();
options.headerPrefix="md2x-";
options.baseUrl="http://localhost:80/files";
Md2x md2x=new Md2x(options);
String html=md2x.parse("## Hello,World\n![图片](/img/a.png)");
System.out.println(html);
        
// <h2 id="md2x-hello-world">Hello,World</h2>
// <p><img src="http://localhost:80/img/a.png" alt="图片" title=""></p>
~~~

以下为支持的设置以及默认值
~~~
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
    public boolean smartLists=false;
    public boolean smartypants=false;
    public boolean tables=true;
    public boolean xhtml=false;
~~~

#### 3.在JSP页面中使用
在md2x中已经实现实现了一个自定义jsp标签类`Md2xTag`,该类继承于SimpleTagSupport类。
当然，你也可以通过自定义一个jsp标签来使用md2x。

在使用之前，需要将md2x的jar包放到`WebContent/WEB-INF/lib`或`webapp/WEB-INF/lib`目录下。

在使用的jsp页面中需要通过以下方式引入md2x中定义的jsp标签：
~~~
<%@ taglib prefix="md2x" uri="http://www.touchface.top/md2x" %>
~~~

然后在jsp页面中通过<md2x:md2html>标签中进行使用。

##### 例1:标签体赋值
~~~
<md2x:md2html>
## Hello,World!
</md2x:md2html>
~~~


##### 例2:通过标签属性赋值
~~~
<md2x:md2html value="## Hello,World!"/>
~~~

需要注意的是当通过value属性进行赋值的时，不能包含标签体，
否则标签将会解析标签体内的文本而忽略通过value属性传入的
文本。

##### 例3:通过标签属性对解析器进行设置
~~~
<md2x:md2html baseUrl="http://localhost:8080/files"  value="![图片](img.jpg)"/>
~~~




