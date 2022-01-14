[中文版文档](https://github.com/flashvayne/mysql-mybatis-page/blob/master/README_zh.md)

# mysql-mybatis-pagination
A very lightweight pagination interceptor based on MySQL Dialect "SQL_CALC_FOUND_ROWS & FOUND_ROWS()"  
The released package ("mysql-mybatis-page-1.0.0.jar") size is only 10KB.
## Usage
1.Load maven dependency.  
For SpringBoot project, interceptor will be autowired on startup, no additional configuration required.
```pom
<dependency>
    <groupId>io.github.flashvayne</groupId>
    <artifactId>mysql-mybatis-pagination</artifactId>
    <version>1.0.0</version>
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
Pagination only takes effect between "Page.start()" and "Page.end()".
Pagination will not be applied to any query executed after line "Page.end()", unless "Page.start()" is called again to start a new paging process.
# Author Info
Email: flashvayne@gmail.com

Blog: https://blog.vayne.ink
