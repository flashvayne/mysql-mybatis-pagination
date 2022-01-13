# mysql-mybatis-page
基于MySQL方言 SQL_CALC_FOUND_ROWS & FOUND_ROWS() 的轻量级分页组件
## 使用方法
1.拉取master代码，使用maven install  
2.maven加载依赖。对于SpringBoot项目，启动时会自动装配组件，无需其他配置
```pom
<dependency>
    <groupId>com.github.flashvayne</groupId>
    <artifactId>mysql-mybatis-page</artifactId>
    <version>1.0.0</version>
</dependency>
```
3.在需要分页的地方使用如下代码
```java
Page.start(pageNum,pageSize);
List<User> users = userMapper.select();
PageInfo pageInfo = Page.end(users);
logger.info("pageInfo: {}", pageInfo);
```
*注意：
分页从Page.start()开始，到Page.end()结束，只在二者之间有效。  
Page.end()执行后的查询不会再次分页，除非再次调用Page.start()开始新的分页流程。
