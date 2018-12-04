## Md2x Markdown 解析器

Md2x是一款轻量级的Java Markdown解析器，
它的源码设计参考了marked.js。

### 简单使用

#### 1. 在你的项目中引入md2x的jar包。

通过以下方式将jar包导入到你的项目中

+ 下载jar包 [Download]()

+ Maven中央仓库引入(暂不支持)


#### 2. 进行Markdown解析

你可以通过简单的方式使用md2x。
```
Md2x md2x=new Md2x();
html=md2x.parse(markdown);

```




