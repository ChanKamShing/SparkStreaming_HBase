# SparkStreaming_HBase
将从Kafka收集过来的数据保存到HBase中
<p>数据来源：日志生成器。</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编写一个python工程，用于产生行为日志，每运行一次，产生所设定的数量数据，使用Linux的定时器，每隔60s执行一次，行为日志保存在文件中。
使用flume对新产生的行为日志进行收集，再用Kafka进行收集、存储，使用SparkStreaming实时处理，最终落到HBase上。</p>
<p>数据格式：</p>
<p>/**</p>
<p>&nbsp;&nbsp;  * 63.132.29.46	2019-10-15 00:36:16	"GET /class/131.html HTTP/1.1"	404	-</p>
<p>&nbsp;&nbsp;  * 46.98.10.132	2019-10-15 00:36:16	"GET /class/112.html HTTP/1.1"	200	-</p>
<p>&nbsp;&nbsp;  * 46.29.167.10	2019-10-15 00:36:16	"POST /course/list HTTP/1.1"	500	-</p>
<p>&nbsp;&nbsp;  * 63.72.55.156	2019-10-15 00:36:16	"GET /class/130.html HTTP/1.1"	404	-</p>
<p>&nbsp;&nbsp;  * 98.63.187.167	2019-10-15 00:36:16	"GET /learn/821 HTTP/1.1"	200	https://www.sogou.com/web?query=Hadoop基础</p>
<p>&nbsp;&nbsp;  */</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;另外，为了使代码容易管理，借鉴了MVC的设计模式，在工程代码使用了DAO、Model层，Scala编写为主，Java辅助</p>
