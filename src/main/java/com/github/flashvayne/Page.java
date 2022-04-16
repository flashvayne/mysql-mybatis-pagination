package com.github.flashvayne;

import com.github.flashvayne.dto.PageContext;
import com.github.flashvayne.dto.PageInfo;

/**
 * MysqlPage工具类
 *
 * @author flashvayne
 */
public class Page {
    private static final ThreadLocal<PageContext> PageInfoThreadLocal = new ThreadLocal<>();

    public static void set(PageContext pageContext) {
        PageInfoThreadLocal.set(pageContext);
    }

    public static PageContext get() {
        return PageInfoThreadLocal.get();
    }

    public static void start(int pageNum, int pageSize) {
        PageContext pageContext = PageInfoThreadLocal.get();
        if(pageContext == null){
            pageContext = new PageContext();
            pageContext.setActive(true);
            pageContext.setPageInfo(new PageInfo());
        }
        PageInfo pageInfo = pageContext.getPageInfo();
        if (pageInfo == null) {
            pageInfo = new PageInfo();
            pageContext.setPageInfo(pageInfo);
        }
        pageInfo.setPage(pageNum);
        pageInfo.setSize(pageSize);
        PageInfoThreadLocal.set(pageContext);
    }

    public static PageInfo end(Object o){
        PageContext pageContext = PageInfoThreadLocal.get();
        clear();
        if(pageContext == null){
            return null;
        }
        PageInfo pageInfo = pageContext.getPageInfo();
        if(pageInfo == null){
            return null;
        }
        pageInfo.setList(o);
        return pageInfo;
    }

    public static void clear(){
        PageInfoThreadLocal.remove();
    }

}
