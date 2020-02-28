# Host: 127.0.0.1  (Version: 5.5.28)
# Date: 2020-02-27 22:12:08
# Generator: MySQL-Front 5.3  (Build 4.214)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "t_attachment"
#

DROP TABLE IF EXISTS `t_attachment`;
CREATE TABLE `t_attachment` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `attachname` varchar(255) DEFAULT NULL,
  `attachpath` varchar(255) DEFAULT NULL,
  `attachsmallpath` varchar(255) DEFAULT NULL,
  `attachtype` varchar(255) DEFAULT NULL,
  `attachsuffix` varchar(255) DEFAULT NULL,
  `attachsize` varchar(255) DEFAULT NULL,
  `attachwh` varchar(255) DEFAULT NULL,
  `attachorigin` int(11) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `rawsize` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

#
# Data for table "t_attachment"
#

INSERT INTO `t_attachment` VALUES (27,'20200227_203053_480.jpg','/upload/2020/2/20200227_203053_480.jpg','/upload/2020/2/20200227_203053_480_small.jpg','image/jpeg','jpg','22KB','1006x463',0,'2020-02-27 20:30:54',23226),(28,'20200227_203102_810.jpg','/upload/2020/2/20200227_203102_810.jpg','/upload/2020/2/20200227_203102_810_small.jpg','image/jpeg','jpg','17KB','485x175',0,'2020-02-27 20:31:02',17657),(29,'20200227_203110_173.jpg','/upload/2020/2/20200227_203110_173.jpg','/upload/2020/2/20200227_203110_173_small.jpg','image/jpeg','jpg','42KB','665x300',0,'2020-02-27 20:31:10',43418),(30,'20200227_203119_83.jpg','/upload/2020/2/20200227_203119_83.jpg','/upload/2020/2/20200227_203119_83_small.jpg','image/jpeg','jpg','10KB','554x300',0,'2020-02-27 20:31:19',11192),(31,'20200227_203127_534.jpg','/upload/2020/2/20200227_203127_534.jpg','/upload/2020/2/20200227_203127_534_small.jpg','image/jpeg','jpg','108KB','550x380',0,'2020-02-27 20:31:27',111570),(32,'20200227_203135_282.jpg','/upload/2020/2/20200227_203135_282.jpg','/upload/2020/2/20200227_203135_282_small.jpg','image/jpeg','jpg','26KB','522x300',0,'2020-02-27 20:31:35',27237),(33,'20200227_203141_72.jpg','/upload/2020/2/20200227_203141_72.jpg','/upload/2020/2/20200227_203141_72_small.jpg','image/jpeg','jpg','20KB','605x300',0,'2020-02-27 20:31:41',21232),(34,'20200227_203508_953.jpg','/upload/2020/2/20200227_203508_953.jpg','/upload/2020/2/20200227_203508_953_small.jpg','image/jpeg','jpg','115KB','760x300',0,'2020-02-27 20:35:08',118089),(35,'20200227_220205_737.jpg','/upload/2020/2/20200227_220205_737.jpg','/upload/2020/2/20200227_220205_737_small.jpg','image/jpeg','jpg','244KB','640x640',0,'2020-02-27 22:02:05',250269);

#
# Structure for table "t_blog_t_tag"
#

DROP TABLE IF EXISTS `t_blog_t_tag`;
CREATE TABLE `t_blog_t_tag` (
  `blogs_id` bigint(20) NOT NULL,
  `tags_id` bigint(20) NOT NULL,
  KEY `FKhl5bn19gf2sq5o31s7amn0l1t` (`tags_id`),
  KEY `FKk1nf67s05jh2pbvf45gmhvhja` (`blogs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "t_blog_t_tag"
#

INSERT INTO `t_blog_t_tag` VALUES (149,30),(149,31);

#
# Structure for table "t_comment"
#

DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `blog_id` bigint(20) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `pass` int(2) DEFAULT '0' COMMENT '0:审核中1:成功2:失败',
  `admin_comment` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime DEFAULT NULL,
  `parent_comment_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKke3uogd04j4jx316m1p51e05u` (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8;

#
# Data for table "t_comment"
#


#
# Structure for table "t_link"
#

DROP TABLE IF EXISTS `t_link`;
CREATE TABLE `t_link` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `linkname` varchar(255) DEFAULT NULL,
  `linkurl` varchar(255) DEFAULT NULL,
  `linkpic` varchar(255) DEFAULT NULL,
  `linkdesc` varchar(255) DEFAULT NULL,
  `linkcreatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Data for table "t_link"
#

INSERT INTO `t_link` VALUES (4,'Zephery','http://www.wenzhihuai.com/','/upload/2020/2/20200227_220205_737.jpg','专注高并发、高可用、分布式计算、大数据开发','2020-02-27 22:03:09');

#
# Structure for table "t_log"
#

DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `logtype` varchar(255) DEFAULT NULL,
  `requesturl` varchar(255) DEFAULT NULL,
  `requesttype` varchar(255) DEFAULT NULL,
  `requestparam` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `ipinfo` varchar(255) DEFAULT NULL,
  `costtime` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

#
# Data for table "t_log"
#

INSERT INTO `t_log` VALUES (115,'修改个人密码','operation','/admin/security','PUT','忽略','admin','127.0.0.1',NULL,'8','2020-02-27 21:58:09'),(116,'用户登录','login','/loginVerify','POST','{\"password\":\"密码隐藏\",\"verifyCode\":\"16\",\"rememberme\":\"on\",\"username\":\"admin\"}','admin','127.0.0.1',NULL,'20','2020-02-27 21:58:25'),(117,'上传文件','attachment','/admin/attachments/upload','POST','{}','admin','127.0.0.1',NULL,'312','2020-02-27 22:02:05'),(118,'更新页面','operation','/admin/pages','POST','忽略','admin','127.0.0.1',NULL,'17','2020-02-27 22:05:42');

#
# Structure for table "t_slide"
#

DROP TABLE IF EXISTS `t_slide`;
CREATE TABLE `t_slide` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `slide_title` varchar(255) DEFAULT NULL,
  `slide_url` varchar(255) DEFAULT NULL,
  `slide_picture_url` varchar(255) DEFAULT NULL,
  `slide_sort` int(11) DEFAULT NULL,
  `slide_target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

#
# Data for table "t_slide"
#

INSERT INTO `t_slide` VALUES (6,'TCP和UDP总结','/article/149.html','/upload/2020/2/20200227_203508_953.jpg',1,'新窗口');

#
# Structure for table "t_tag"
#

DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

#
# Data for table "t_tag"
#

INSERT INTO `t_tag` VALUES (30,'TCP',''),(31,'UDP','');

#
# Structure for table "t_type"
#

DROP TABLE IF EXISTS `t_type`;
CREATE TABLE `t_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

#
# Data for table "t_type"
#

INSERT INTO `t_type` VALUES (53,'计算机网络','','');

#
# Structure for table "t_user"
#

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  `type` bit(1) DEFAULT b'0',
  `username` varchar(255) NOT NULL DEFAULT '',
  `url` varchar(100) DEFAULT NULL,
  `last_login_ip` varchar(255) DEFAULT NULL,
  `register_time` datetime DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `status` bit(1) NOT NULL DEFAULT b'0',
  `update_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

#
# Data for table "t_user"
#

INSERT INTO `t_user` VALUES (2,'/images/islizx.jpg','islizx@163.com','RAYMOND','8bd6266de733d121f4f0fee649ab5357',b'0','admin','hello','127.0.0.1','2020-02-04 11:42:36','2020-02-27 21:58:25',b'0','2020-02-27 21:58:09','hello ');

#
# Structure for table "t_blog"
#

DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE `t_blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appreciation` bit(1) NOT NULL DEFAULT b'0',
  `commentabled` bit(1) DEFAULT b'0',
  `content` text,
  `createTime` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `firstPicture` varchar(255) DEFAULT NULL,
  `flag` varchar(255) DEFAULT NULL,
  `published` int(1) NOT NULL DEFAULT '0',
  `recommend` bit(1) DEFAULT b'0',
  `shareStatement` bit(1) DEFAULT b'0',
  `title` varchar(255) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `views` int(11) DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `comment_count` int(11) DEFAULT '0',
  `like_count` int(11) DEFAULT '0',
  `posttype` int(11) NOT NULL DEFAULT '0',
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK292449gwg5yf7ocdlmswv9w4j` (`type_id`),
  KEY `FK8ky5rrsxh01nkhctmo7d48p82` (`user_id`),
  CONSTRAINT `FK292449gwg5yf7ocdlmswv9w4j` FOREIGN KEY (`type_id`) REFERENCES `t_type` (`id`),
  CONSTRAINT `FK8ky5rrsxh01nkhctmo7d48p82` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;

#
# Data for table "t_blog"
#

INSERT INTO `t_blog` VALUES (149,b'0',b'1','# 前言\r\n\r\n​\t在面试中经常被问 TCP 和 UDP 的区别，以及 三次握手 和 四次挥手 。回答的时候参考着网上的内容。TCP 和 UDP 的区别：\r\n\r\n- ​\tTCP 是面向连接的，UDP 是面向无连接的\r\n- ​\tUDP程序结构较简单\r\n- ​\tTCP 是面向字节流的，UDP 是基于数据报的\r\n- ​\tTCP 保证数据正确性，UDP 可能丢包\r\n- ​\tTCP 保证数据顺序，UDP 不保证\r\n\r\n​\t作为一名计算机专业的学生想起面试时经常被问到这方面，既是对知识的好奇，又好奇于面试官的心理。换句话说，我们了解 TCP/UDP、TCP三次握手四次挥手 的现实意义是什么。而查阅网上资料，大部分都是在解释 TCP 和 UDP 是什么。因此，本篇文章主要基于网络资料和自己的理解。\r\n​\t要想理解 TCP 和 UDP，我们得先了解它们处于哪一层？\r\n\r\n# 一. 计算机网络体系结构\r\n\r\n![](/upload/2020/2/20200227_203135_282.jpg)\r\n\r\n​\tOSI模型(Open System Interconnection Reference Model):即开放式系统互联通信参考模型。由国际标准化组织提出，一个试图使各种计算机在世界范围内互连为网络的标准框架。\r\n\r\n​\t五层协议是教科书版的分法，便于学习和研究而分成五层的。\r\n\r\n​\t四层协议是现在实际运用的，就是商业应用的。\r\n\r\n## 1.1 不同层对应的协议\r\n\r\n![](/upload/2020/2/20200227_203110_173.jpg)\r\n\r\n# 二. UDP\r\n![](/upload/2020/2/20200227_203102_810.jpg)\r\n\r\n\r\n​\t上图是 UDP 的包头格式，可以看出 UDP 格式非常简单，只有源端口和目的端口，端口的功能是为了将数据发个某个应用。\r\n\r\n\r\n## 2.1 UDP 的特点\r\n\r\n- ​\t不需要大量的数据结构，处理逻辑和包头字段\r\n- ​\t不会建立连接，但是会监听这个地方，谁都可以传给它数据，它也可以传给任何人数据，甚至可以同时传给多个人数据。\r\n- ​\t不会根据网络的情况进行拥塞控制，无论是否丢包，它该怎么发还是怎么发\r\n\r\n## 2.2 UDP 的主要应用场景\r\n\r\n​\t因为 UDP 的这些特点，所以处理的是一些没那么难的功能，并且就算失败的也能接收。基于这些特点的话，UDP 可以使用在如下场景中：\r\n- ​\t需要资源少，网络情况稳定的内网，或者对于丢包不敏感的应用，比如 DHCP 就是基于 UDP 协议的。\r\n- ​\t不需要一对一沟通，建立连接，而是可以广播的应用。因为它不面向连接，所以可以做到一对多，承担广播或者多播的协议。\r\n- ​\t需要处理速度快，可以容忍丢包，但是即使网络拥塞，也毫不退缩，一往无前的时候\r\n## 2.3 UDP 的几个例子\r\n\r\n- ​\t直播。直播对实时性的要求比较高，宁可丢包，也不要卡顿的，所以很多直播应用都基于 UDP 实现了自己的视频传输协议\r\n- ​\t实时游戏。游戏的特点也是实时性比较高，在这种情况下，采用自定义的可靠的 UDP 协议，自定义重传策略，能够把产生的延迟降到最低，减少网络问题对游戏造成的影响\r\n- ​\t物联网。一方面，物联网领域中断资源少，很可能知识个很小的嵌入式系统，而维护 TCP 协议的代价太大了；另一方面，物联网对实时性的要求也特别高。比如 Google 旗下的 Nest 简历 Thread Group，推出了物联网通信协议 Thread，就是基于 UDP 协议的\r\n\r\n# 三. TCP\r\n![](/upload/2020/2/20200227_203053_480.jpg)\r\n\r\n​\tTCP 的包头有哪些内容，分别有什么用：\r\n\r\n- ​\t首先，源端口和目标端口是不可少的。\r\n- ​\t接下来是包的序号。主要是为了解决乱序问题。不编好号怎么知道哪个先来，哪个后到\r\n- ​\t确认序号。发出去的包应该有确认，这样能知道对方是否收到，如果没收到就应该重新发送，这个解决的是不丢包的问题\r\n- ​\t状态位。SYN 是发起一个链接，ACK 是回复，RST 是重新连接，FIN 是结束连接。因为 TCP 是面向连接的，因此需要双方维护连接的状态，这些状态位的包会引起双方的状态变更\r\n- ​\t窗口大小，TCP 要做流量控制，需要通信双方各声明一个窗口，标识自己当前的处理能力。\r\n\r\n​\t通过对 TCP 头的解析，我们知道要掌握 TCP 协议，应该重点关注以下问题：\r\n\r\n- ​\t顺序问题\r\n- ​\t丢包问题\r\n- ​\t连接维护\r\n- ​\t流量控制\r\n- ​\t拥塞控制\r\n\r\n## 3.1 TCP 的三次握手\r\n\r\n![](/upload/2020/2/20200227_203141_72.jpg)\r\n\r\n​\t这是网上经常见到的一张图，刚开始的时候，客户端和服务器都处于 CLOSED 状态，先是服务端主动监听某个端口，处于 LISTEN 状态。然后客户端主动发起连接 SYN，之后处于 SYN-SENT 状态。服务端接收了发起的连接，返回 SYN，并且 ACK ( 确认 ) 客户端的 SYN，之后处于 SYN-SENT 状态。客户端接收到服务端发送的 SYN 和 ACK 之后，发送 ACK 的 ACK，之后就处于 ESTAVLISHED 状态，因为它一发一收成功了。服务端收到 ACK 的 ACK 之后，也处于 ESTABLISHED 状态，因为它也一发一收了\r\n## 3.2 TCP 的四次挥手\r\n\r\n![](/upload/2020/2/20200227_203127_534.jpg)\r\n\r\n​\t说完建立连接，再说下断开连接，也被称为四次挥手，可以简单理解如下：\r\n- ​\t第一次挥手：双方数据传输的差不多，此时客户端也已经结束传输了，接下来要断开通信连接，所以告诉服务端客户端数据传输完毕（FIN），此时客户端进入等待结束连接的状态。\r\n- ​\t第二次挥手：，服务端知道客户端数据传输完毕（ACK），但服务端可能还有数据未传输，接着继续传输。\r\n- ​\t第三次挥手：此时客户端接收数据继续处于等待结束的状态，然后服务器端也传输完毕，自身此时处于等待关闭连接的状态，并对告诉客户端，自己的数据也传输完毕（FIN）”\r\n- ​\t第四次挥手：客户端知道服务端也传输完毕，也要告诉服务端一声（ACK），因为连接和断开要双方都按下关闭操作才能断开，客户端同时又为自己定义一个定时器，因为不知道刚才说的这句话能不能准确到达服务端（网络不稳定或者其他因素引起的网络原因），默认时间定为两个通信的最大时间之和，超出这个时间就默认服务器端已经接收到了自己的确认信息，此时客户端就关闭自身连接，服务器端一旦接收到客户端发来的确定通知就立刻关闭服务器端的连接\r\n\r\n​\t到此为止双方整个通信过程就此终结。但断开链接不一定就是客户端，谁都可以先发起断开指令，另外客户端和服务端是没有固定标准的，谁先发起请求谁就是客户端。\r\n## 3.3 TCP 累计确认\r\n\r\n​\t首先为了保证顺序性，每个包都有一个 ID。在建立连接的时候会商定起始 ID 是什么，然后按照 ID 一个个发送，为了保证不丢包，需要对发送的包都要进行应答，当然，这个应答不是一个一个来的，而是会应答某个之前的 ID，表示都收到了，这种模式成为累计应答或累计确认。\r\n\r\n​\t为了记录所有发送的包和接收的包，TCP 需要发送端和接收端分别来缓存这些记录，发送端的缓存里是按照包的 ID 一个个排列，根据处理的情况分成四个部分\r\n\r\n- ​\t发送并且确认的\r\n- ​\t发送尚未确认的\r\n- ​\t没有发送等待发送的\r\n- ​\t没有发送并且暂时不会发送的\r\n\r\n​\t这里的第三部分和第四部分就属于流量控制的内容\r\n\r\n​\t在 TCP 里，接收端会给发送端报一个窗口大小，叫 Advertised window。这个窗口应该等于上面的第二部分加上第三部分，超过这个窗口，接收端做不过来，就不能发送了，于是，发送端要保持下面的数据结构：\r\n​\t对于接收端来讲，它的缓存里面的内容要简单一些\r\n\r\n- ​\t接收并且确认过的\r\n- ​\t还没接收，但是马上就能接收的\r\n- ​\t还没接收，但也无法接收的\r\n\r\n## 3.4 TCP 顺序问题和丢包问题\r\n\r\n​\t举个例子：在发送端，1、2、3 已发送并确认；4、5、6、7、8、9 都是发送了还没确认；10、11、12 是还没发出的；13、14、15 是接收方没有空间，不准备发的。\r\n\r\n​\t在接收端来看，1、2、3、4、5 是已经完成 ACK 但是还没读取的；6、7 是等待接收的；8、9 是已经接收还没有 ACK 的。\r\n\r\n​\t发送端和接收端当前的状态如下：\r\n\r\n- ​\t1、2、3 没有问题，双方达成了一致\r\n\r\n- ​\t4、5 接收方说 ACK 了，但是发送方还没收到\r\n\r\n- ​\t6、7、8、9 肯定都发了，但是 8、9 已经到了，6、7 没到，出现了乱序，缓存着但是没办法 ACK。\r\n\r\n​\t根据这个例子可以知道顺序问题和丢包问题都有可能存在，所以我们先来看确认与重传机制。\r\n\r\n​\t假设 4 的确认收到了，5 的 ACK 丢了，6、7 的数据包丢了，该怎么办？\r\n\r\n​\t一种方法是超时重试，即对每一个发送了但是没有 ACK 的包设定一个定时器，超过了一定的事件就重新尝试。这个时间必须大于往返时间，但也不宜过长，否则超时时间变长，访问就变慢了。\r\n​\t如果过一段时间，5、6、7 都超时了就会重新发送。接收方发现 5 原来接收过，于是丢弃 5；6 收到了，发送 ACK，要求下一个是 7，7 不幸又丢了。当 7 再次超时的时候，TCP 的策略是超时间隔加倍。每当遇到一次超时重传的时候，都会讲下一次超时时间间隔设为先前值的两倍。\r\n\r\n​\t超时重传的机制是超时周期可能相对较长，是否有更快的方式呢？\r\n\r\n​\t有一个快速重传的机制，即当接收方接收到一个序号大于期望的报文段时，就检测到了数据流之间的间隔，于是发送三个冗余的 ACK，客户端接收到之后，知道数据报丢失，于是重传丢失的报文段。\r\n​\t例如，接收方发现 6、8、9 都接收了，但是 7 没来，所以肯定丢了，于是发送三个 6 的 ACK，要求下一个是 7。客户端接收到 3 个，就会发现 7 的确又丢了，不等超时，马上重发。\r\n\r\n## 3.4 TCP 流量控制的问题\r\n\r\n​\t在流量控制的机制里面，在对于包的确认中，会携带一个窗口的大小\r\n\r\n​\t简单的说一下就是接收端在发送 ACK 的时候会带上缓冲区的窗口大小，但是一般在窗口达到一定大小才会更新窗口，因为每次都更新的话，刚空下来就又被填满了\r\n\r\n## 3.5 TCP 拥塞控制的问题\r\n\r\n​\t也是通过窗口的大小来控制的，但是检测网络满不满是个挺难的事情，所以 TCP 发送包经常被比喻成往谁管理灌水，所以拥塞控制就是在不堵塞，不丢包的情况下尽可能的发挥带宽。\r\n\r\n​\t水管有粗细，网络有带宽，即每秒钟能发送多少数据；水管有长度，端到端有时延。理想状态下，水管里面的水 = 水管粗细 * 水管长度。对于网络上，通道的容量 = 带宽 * 往返时延。\r\n\r\n​\t如果我们设置发送窗口，使得发送但未确认的包为通道的容量，就能撑满整个管道。\r\n\r\n\r\n\r\n​\t假设往返时间为 8 秒，去 4 秒，回 4 秒，每秒发送一个包，已经过去了 8 秒，则 8 个包都发出去了，其中前四个已经到达接收端，但是 ACK 还没返回，不能算发送成功，5-8 后四个包还在路上，还没被接收，这个时候，管道正好撑满，在发送端，已发送未确认的 8 个包，正好等于带宽，也即每秒发送一个包，也即每秒发送一个包，乘以来回时间 8 秒。\r\n\r\n​\t如果在这个基础上调大窗口，使得单位时间可以发送更多的包，那么会出现接收端处理不过来，多出来的包会被丢弃，这个时候，我们可以增加一个缓存，但是缓存里面的包 4 秒内肯定达不到接收端课，它的缺点会增加时延，如果时延达到一定程度就会超时重传\r\n\r\n​\tTCP 拥塞控制主要来避免两种现象，包丢失和超时重传，一旦出现了这些现象说明发送的太快了，要慢一点。\r\n\r\n​\t具体的方法就是发送端慢启动，比如倒水，刚开始倒的很慢，渐渐变快。然后设置一个阈值，当超过这个值的时候就要慢下来\r\n\r\n​\t慢下来还是在增长，这时候就可能水满则溢，出现拥塞，需要降低倒水的速度，等水慢慢渗下去。\r\n\r\n​\t拥塞的一种表现是丢包，需要超时重传，这个时候，采用快速重传算法，将当前速度变为一半。所以速度还是在比较高的值，也没有一夜回到解放前。\r\n\r\n# 四. 总结\r\n## 4.1 TCP 和 UDP 的区别\r\n\r\n- ​\tTCP 是面向连接的，UDP 是面向无连接的\r\n- ​\tUDP程序结构较简单\r\n- ​\t- ​\tTCP 是面向字节流的，UDP 是基于数据报的\r\n- ​\tTCP 保证数据正确性，UDP 可能丢包\r\n- ​\tTCP 保证数据顺序，UDP 不保证\r\n\r\n## 4.2 什么是面向连接，什么是面向无连接\r\n\r\n- ​\t在互通之前，面向连接的协议会先建立连接，如 TCP 有三次握手，而 UDP 不会\r\n\r\n## 4.3 TCP 为什么是可靠连接\r\n\r\n- ​\t通过 TCP 连接传输的数据无差错，不丢失，不重复，且按顺序到达。\r\n- ​\tTCP 报文头里面的序号能使 TCP 的数据按序到达\r\n- ​\t报文头里面的确认序号能保证不丢包，累计确认及超时重传机制\r\n- ​\tTCP 拥有流量控制及拥塞控制的机制\r\n- ​\t 的顺序问题，丢包问题，流量控制都是通过滑动窗口来解决的\r\n- ​\t拥塞控制时通过拥塞窗口来解决的\r\n\r\n​\t TCP/IP并不是一个完美的协议,但是它却能在那么多的网络协议中脱颖而出,至少表明它是出色的,至少在目前还没有任何协议可以取代它。\r\n\r\n​\t 文章主要内容摘自https://www.cnblogs.com/reload-sun/p/12216936.html','2020-02-24 21:36:13','在面试中经常被问 TCP 和 UDP 的区别，以及 三次握手 和 四次挥手 。回答的时候参考着网上的内容。作为一名计算机专业的学生想起面试时经常被问到这方面，既是对知识的好奇，又好奇于面试官的心理。换句话说','/upload/2020/2/20200227_203119_83.jpg','转载',0,b'1',b'0','TCP和UDP总结','2020-02-27 20:33:46',16,53,2,0,0,0,NULL),(150,b'0',b'1','##### 请在申请友链之前请在贵站加好本站的链接\r\n\r\n- ​\t名称：islizx -Blog\r\n- ​\t网址：https://islizx.cn\r\n- ​\t头像：<img src=\"/images/islizx.jpg\" style=\"zoom:25%;\" />\r\n\r\n##### 申请方法\r\n\r\n- ​\t在下方留言或者任意其他位置留言\r\n- ​\t如需设置友链头像可通过QQ、微信或者Email联系博主\r\n\r\n##### 备注\r\n\r\n- ​\t不健康站、未备案站、非常见域名后缀等 **不友链**\r\n- ​\t欢迎加好友交流学习','2020-02-27 21:17:48',NULL,NULL,NULL,0,b'0',b'0','友链申请','2020-02-27 21:53:24',8,NULL,2,0,0,2,'apply-links'),(151,b'0',b'1','##### 有什么问题欢迎在此处留言\r\n##### 您的支持和鼓励是我最大的动力！','2020-02-27 22:05:42',NULL,NULL,NULL,0,b'0',b'0','留言板','2020-02-27 22:05:42',1,NULL,2,0,0,2,'message');

#
# Structure for table "t_widget"
#

DROP TABLE IF EXISTS `t_widget`;
CREATE TABLE `t_widget` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `widget_title` varchar(255) DEFAULT NULL,
  `widget_content` text,
  `is_display` int(11) NOT NULL DEFAULT '1',
  `widget_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

#
# Data for table "t_widget"
#

INSERT INTO `t_widget` VALUES (4,'a','<div class=\"ui inverted link list\">\r\n  <div class=\"item\">\r\n    <img  src=\"/images/wechat.jpg\"  class=\"ui rounded image\" alt=\"\" style=\"width: 110px\">\r\n  </div>\r\n</div>\r\n\r\n',0,0),(5,'二维码','<div class=\"ui inverted link list\">\r\n  <div class=\"item\">\r\n    <img  src=\"/images/wechat.jpg\"  class=\"ui rounded image\" alt=\"\" style=\"width: 110px\">\r\n  </div>\r\n</div>\r\n\r\n',1,1),(17,'联系方式','<h4 class=\"ui inverted header m-text-thin m-text-spaced \">联系我</h4>\r\n        <div class=\"ui inverted link list\">\r\n          <a href=\"#\" class=\"item m-text-thin\">Email：islizx@163.com</a>\r\n          <a href=\"#\" class=\"item m-text-thin\">QQ：3022823354</a>\r\n        </div>',1,1),(18,'介绍','<h4 class=\"ui inverted header m-text-thin m-text-spaced \">Blog</h4>\r\n        <p class=\"m-text-thin m-text-spaced m-opacity-mini\">这是我的个人博客、会分享关于编程、写作、思考相关的任何内容，希望可以给来到这儿的人有所帮助...</p>',1,1),(19,'关于本站','<h4 class=\"ui inverted header m-text-thin m-text-spaced \">关于本站</h4>\r\n        <p class=\"m-text-thin m-text-spaced m-opacity-mini\">本站基于 Spring、SpringMVC、MyBatis、Thmeleaf 实现。</p>',1,1),(20,'ssss','sssss',0,0);
