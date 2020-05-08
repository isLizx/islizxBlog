# islizxBlog
基于SpringMVC+Spring+MyBatis开发的个人博客网站，使用IDEA工具开发，毕业设计
## 一、关于项目

1. 该博客是基于SSM实现的个人博客系统，适合初学SSM和个人博客制作的同学学习。主要技术架构包括Maven、SpringMVC、Spring、MyBatis、Thymeleaf等。前端采用Bootstarp和Semantic UI。
2. 详细介绍：https://islizx.cn/article/153.html

## 二、效果预览

1. 预览地址：https://islizx.cn

2. 前台效果图就不展示了，可前往网站浏览

3. 介绍几张后台的页面

   1. 后台首页 DashBoard

      ![](https://www.islizx.cn/upload/2020/2/20200229_170208_63.png)

   2. 文章列表

      ![](https://www.islizx.cn/upload/2020/2/20200229_170208_313.png)

   3. 编辑文章（MarkDown编辑器）

      ![](https://www.islizx.cn/upload/2020/2/20200229_170112_354.png)

   4. 页面管理（可以自定义页面，申请友链和留言板即为自定义页面）

      ![](https://www.islizx.cn/upload/2020/2/20200229_170209_828.png)

   5. 附件管理（点击附件可以查看详细信息以及删除操作）

      ![](https://www.islizx.cn/upload/2020/2/20200229_170112_201.png)

   6. 评论管理（管理员回复回收站和待审核的评论后直接通过审核并发送邮件给评论者）

      ![](https://www.islizx.cn/upload/2020/2/20200229_170208_899.png)

   7. 日志管理

      ![](https://www.islizx.cn/upload/2020/2/20200229_170208_909.png)

## 三、使用注意

1. 开发工具的选择

   请使用 IntelliJ IDEA, 尽量不要用 Eclipse/MyEclipse。后者可能要折腾一会儿

2. 确保你安装了 Maven

3. 请给你的IDE安装Lombok插件

   实体类中多次使用到 @Data 注解，请确保你的 IDE 安装了 Lombok 插件，否则找不到 getter/setter 方法

## 四、使用步骤

1. Fork项目

   fork或者下载项目到本地（建议先fork到自己仓库，在通过码云导入仓库下载，实测下载速度可以）。完整项目源码，可以使用IDEA导入。数据库文件请先创建数据库，然后以运行sql文件方式导入

2. 导入数据库

   新建数据库**blog**,导入数据库blog.sql。注意，数据库的编码和排序规则是utf-8和utf-8_general_ci。数据库默认用户名 root，密码 123456

3. 修改项目中的数据库连接信息

   修改 db.properties 文件，该文件很容易找到，在 src/main/resources 中。里面有 MySQL 数据库连接信息，请确保已安装和启动 MySQL。注意修改数据库地址、表名、用户名和密码。

## 五、下载地址

​	GitHub地址：https://github.com/isLizx/islizxBlog  （如果可以帮忙点一次Star和Fork）
