package com.github.flashvayne.interceptor;

import com.github.flashvayne.Page;
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
    private static final String countSql = "select FOUND_ROWS()";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        PageInfo pageInfo = Page.get();
        if(pageInfo == null) return invocation.proceed();
        int start = pageInfo.getStart();
        int size = pageInfo.getSize();
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
        metaObject.setValue("sql","select SQL_CALC_FOUND_ROWS" + sql.substring(sql.indexOf("select")+6) + " limit "+ start+","+size);
        List rsList = executor.query(ms,parameter,rowBounds,resultHandler,cacheKey,boundSql);
        if(countMs == null) countMs = newCountMappedStatement(ms,"mysqlpage.count");
        Object countResultList = executor.query(countMs,null,new RowBounds(),null,null,
                new BoundSql(countMs.getConfiguration(), countSql, null, null));
        pageInfo.setTotal(((Number) ((List) countResultList).get(0)).longValue());
        Page.set(pageInfo);
        return rsList;
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
        builder.fetchSize(1);
        builder.fetchSize(1);
        builder.statementType(ms.getStatementType());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(new ParameterMap.Builder(ms.getConfiguration(),msId,Object.class,new ArrayList<>(0)).build());
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(new ResultMap.Builder(ms.getConfiguration(), msId, Long.class, new ArrayList<>(0)).build());
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(null);
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(false);
        return builder.build();
    }

}
