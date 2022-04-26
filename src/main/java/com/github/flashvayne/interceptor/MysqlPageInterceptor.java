package com.github.flashvayne.interceptor;

import com.github.flashvayne.Page;
import com.github.flashvayne.dto.PageContext;
import com.github.flashvayne.dto.PageInfo;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * MysqlPage插件
 *
 * @author flashvayne
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class MysqlPageInterceptor implements Interceptor {

    private MappedStatement countMs;
    private BoundSql countBoundSql;
    private static final String countSql = "select FOUND_ROWS()";
    private static final String tmpTable = "tmp_pagination_table";
    private static final String countMsId = "page.count";
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PageContext pageContext = Page.get();
        if(pageContext == null){
            return invocation.proceed();
        }
        if(!pageContext.isActive()){
            Page.clear();
            return invocation.proceed();
        }
        PageInfo pageInfo = pageContext.getPageInfo();
        if(pageInfo == null) return invocation.proceed();
        int size = pageInfo.getSize();
        int start = (pageInfo.getPage() - 1) * size;
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        if (args.length == 4) {
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }
        MetaObject metaObject = SystemMetaObject.forObject(boundSql);
        String sql = (String) metaObject.getValue("sql");
        StringBuilder calcSqlBuild = new StringBuilder();
        calcSqlBuild.append("select SQL_CALC_FOUND_ROWS * from (");
        calcSqlBuild.append(sql).append(") ").append(tmpTable).append(" limit ").append(start).append(",").append(size);
        metaObject.setValue("sql", calcSqlBuild.toString());
        try {
            List rsList = executor.query(ms,parameter,rowBounds,resultHandler,cacheKey,boundSql);
            if(countMs == null) {
                countMs = newCountMappedStatement(ms,countMsId);
                countBoundSql = new BoundSql(countMs.getConfiguration(), countSql, new ArrayList<>(), null);
            }
            Object countResultList = executor.query(countMs,null,new RowBounds(),null,null,countBoundSql);
            pageInfo.setTotal(((Number) ((List) countResultList).get(0)).longValue());
            return rsList;
        }finally {
            pageContext.setActive(false);
        }

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    public static MappedStatement newCountMappedStatement(MappedStatement ms, String msId) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), msId, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(1);
        builder.statementType(ms.getStatementType());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(new ParameterMap.Builder(ms.getConfiguration(),msId,Object.class,new ArrayList<>(0)).build());
        List<ResultMap> resultMaps = new ArrayList<>();
        resultMaps.add(new ResultMap.Builder(ms.getConfiguration(), msId, Long.class, new ArrayList<>(0)).build());
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(null);
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(false);
        return builder.build();
    }

}
