[中文版文档](https://github.com/flashvayne/mysql-mybatis-page/blob/master/README_zh.md)

# mysql-mybatis-page
A very lightweight pagination interceptor based MySQL Dialect "SQL_CALC_FOUND_ROWS & FOUND_ROWS()"  
The release "mysql-mybatis-page-1.0.0.jar" package size is only 10kb.
## Usage
1.Pull project in master branch and build by "maven install".  
2.Load maven dependency.  
For SpringBoot project, interceptor will be autowired on startup, no additional configuration required.
```pom
<dependency>
    <groupId>com.github.flashvayne</groupId>
    <artifactId>mysql-mybatis-page</artifactId>
    <version>1.0.0</version>
</dependency>
```
3.Use the following code where pagination is required
```java
Page.start(pageNum,pageSize);
List<User> users = userMapper.select();
PageInfo pageInfo = Page.end(users);
logger.info("pageInfo: {}", pageInfo);
```
*Notice：
Pagination is only valid from "Page.start()" to "Page.end()".
The query after "Page.end()" that was executed will not be paged again unless "Page.start()" is called again to start a new paging process.
# Author Info
Email: flashvayne@gmail.com

Blog: https://blog.vayne.ink
