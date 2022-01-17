[![Maven central](https://maven-badges.herokuapp.com/maven-central/io.github.flashvayne/mysql-mybatis-pagination/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.flashvayne/mysql-mybatis-pagination)

# mysql-mybatis-pagination
基于MySQL方言 SQL_CALC_FOUND_ROWS & FOUND_ROWS() 的轻量级分页组件  
"mysql-mybatis-page-1.0.0.jar" Release Package 大小仅10kb
## 使用方法
1.maven加载依赖。对于SpringBoot项目，启动时会自动装配组件，无需其他配置
```pom
<dependency>
    <groupId>io.github.flashvayne</groupId>
    <artifactId>mysql-mybatis-pagination</artifactId>
    <version>1.0.1</version>
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
分页从Page.start()开始，到Page.end()结束，只在二者之间有效。  
Page.end()执行后的查询不会再次分页，除非再次调用Page.start()开始新的分页流程。
