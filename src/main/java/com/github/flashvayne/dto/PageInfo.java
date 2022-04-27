package com.github.flashvayne.dto;

import lombok.Data;

/**
 * 分页的数据封装
 *
 * @author flashvayne
 */
@Data
public class PageInfo<T> {
    //页码
    private int page;

    //每页记录数
    private int size;

    //总记录数
    private Long total;

    //当页列表
    private T list;
}
