代码实现：基于 Filter + MDC + AOP 构建全链路日志体系，实现 traceId 贯穿、接口日志采集、异步入库及高性能日志处理
使用方式：
1、本地项目根目录下创建libs
2、将当前项目打包，放到libs下
3、pom文件增加配置
<!--引入自定义sdk-->
<dependency>
<groupId>com.logging</groupId>
<artifactId>logging-starter</artifactId>
<version>0.0.1-SNAPSHOT</version>
<scope>system</scope>
<systemPath>${project.basedir}/libs/logging-starter.jar</systemPath>
</dependency>