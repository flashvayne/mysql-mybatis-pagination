package com.github.flashvayne;

import com.github.flashvayne.dto.PageInfo;

/**
 * MysqlPage工具类
 *
 * @author flashvayne
 */
public class Page {
    private static final ThreadLocal<PageInfo> PageInfoThreadLocal = new ThreadLocal<>();

    public static void set(PageInfo PageInfo) {
        PageInfoThreadLocal.set(PageInfo);
    }

    public static PageInfo get() {
        return PageInfoThreadLocal.get();
    }

    public static void start(int pageNum, int pageSize) {
        PageInfo pageInfo = PageInfoThreadLocal.get();
        if (pageInfo == null) {
            pageInfo = new PageInfo();
        }
        pageInfo.setPage(pageNum);
        pageInfo.setSize(pageSize);
        PageInfoThreadLocal.set(pageInfo);
    }

    public static PageInfo end(Object o){
        PageInfo pageInfo = PageInfoThreadLocal.get();
        if(pageInfo == null){
            return null;
        }
        pageInfo.setList(o);
        try {
            return pageInfo;
        }finally {
            PageInfoThreadLocal.remove();
        }
    }

}
