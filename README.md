# md2x
A Java markdown parser
> **前言**：**Md2x** 是一款运行于Java平台上的轻量级markdown解析器，能够通过简单的调用方式将markdown标记解析为HTML标记。在代码的设计与实现参考了marked.js 的源码，使用了marked.js中的 正则表达式来分析markdown文档进行，以及相似的方法进行解析渲染，编写它的最初目的就是为了在Java中还原一个marked.js。支持标准的语法，GFM的语法和Pedantic语法。

## 1. 如何使用Md2x

通过引入Md2x的jar包到你项目当中，你可以通过简单的调用将markdown转换为html

### 引入md2x的jar包
1. 下载md2x的jar包，放入你的项目。
2. 通过Maven添加md2x依赖。

### 如何调用
#### 1. 创建Md2x的实例，调用实例的parse方法
- 解析markdown字符串
~~~
Md2x md2x=new Md2x();
md2x.parse(src);
~~~

- 解析markdown文件
~~~
File file=new File(“test.md”)
Md2x md2x=new Md2x();
md2x.parse(file);
~~~

- 你可以对解析器进行一些设置，通过向构造函数中传入Options设置对象。Options对象的默认值与marked.js的默认设置一致。
比如，为解析后的分级标题添加id前缀
~~~
Options options=new Opitions();
option.headerPrefix="md2x";
Md2x md2x=new Md2x();
md2x.parse(src);
~~~


#### 2. 通过标签`<md2x:md2html>`，在JSP页面中调用
- 在标签体中输入markdown文本
~~~
<%@ taglib prefix="md2x" uri="http://www.touchface.top/md2x" %>
<md2x:md2html>
# Hello World!
</md2x:md2html>
~~~

- 通过`value`属性，使用EL表达式赋值进行解析
~~~
<%@ taglib prefix="md2x" uri="http://www.touchface.top/md2x" %>
<md2x:md2html value="${markdown}"/>
~~~
**在使用`<md2x:html/>` 标签时需要添加servlet.jsp-api**


