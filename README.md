[![Maven central](https://maven-badges.herokuapp.com/maven-central/io.github.flashvayne/mysql-mybatis-pagination/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.flashvayne/mysql-mybatis-pagination)

[中文版文档](https://github.com/flashvayne/mysql-mybatis-page/blob/master/README_zh.md)

# mysql-mybatis-pagination
A very lightweight pagination interceptor based on MySQL Dialect "SQL_CALC_FOUND_ROWS & FOUND_ROWS()"  
## Usage
1.Load maven dependency.  
For SpringBoot project, interceptor will be autowired on startup, no additional configuration required.
```pom
<dependency>
    <groupId>io.github.flashvayne</groupId>
    <artifactId>mysql-mybatis-pagination</artifactId>
    <version>2.0.0</version>
</dependency>
```
2.Use the following code where pagination is required
```java
Page.start(pageNum,pageSize);
List<User> users = userMapper.select();
PageInfo pageInfo = Page.end(users);
logger.info("pageInfo: {}", pageInfo);
```
*Notice：
Pagination only takes effect at the first query after "Page.start()".  
Pagination will not be applied to any query executed after line "Page.end()", unless "Page.start()" is called again to start a new paging process.  

When you are using a thread poll and there is no query was invoked after "Page.start()", notice that invoke "Page.clear()" manually to end current paging process. So that when the current thread is invoked next time,the pagination will not take effect inexplicably.  
(Because pagination is using ThreadLocal to take effect)

# Author Info
Email: flashvayne@gmail.com

Blog: https://vayne.cc
