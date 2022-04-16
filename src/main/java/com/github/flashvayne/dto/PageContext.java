package com.github.flashvayne.dto;

import lombok.Data;

/**
 * 分页上下文
 *
 * @author flashvayne
 */
@Data
public class PageContext {

    //分页信息
    private PageInfo pageInfo;

    /**
     * 分页操作是否有效
     *
     * 减少发生不安全的分页：执行一次分页查询后将active设置false
     * 这样，一次查询后尽管忘记清除ThreadLocal，再次查询也不会分页
     */
    private boolean active;

}
