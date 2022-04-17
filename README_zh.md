[![Maven central](https://maven-badges.herokuapp.com/maven-central/io.github.flashvayne/mysql-mybatis-pagination/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.flashvayne/mysql-mybatis-pagination)

# mysql-mybatis-pagination
基于MySQL方言 SQL_CALC_FOUND_ROWS & FOUND_ROWS() 的轻量级分页组件  
## 使用方法
1.maven加载依赖。对于SpringBoot项目，启动时会自动装配组件，无需其他配置
```pom
<dependency>
    <groupId>io.github.flashvayne</groupId>
    <artifactId>mysql-mybatis-pagination</artifactId>
    <version>2.0.0</version>
</dependency>
```
2.在需要分页的地方使用如下代码
```java
Page.start(pageNum,pageSize);
List<User> users = userMapper.select();
PageInfo pageInfo = Page.end(users);
logger.info("pageInfo: {}", pageInfo);
```
*注意：
分页仅生效于 "Page.start()" 后的第一次查询。  
"Page.end()" 之后的查询也不会再次分页,除非再次调用 "Page.start()" 开始新的分页处理。  
当你使用线程池且 "Page.start()" 之后没有查询被执行时，请手动调用  "Page.clear()" 去结束当前的分页处理，以至于当前线程再次被调起并执行查询时不会莫名地被分页处理。（因为分页处理是根据ThreadLocal变量生效的）
