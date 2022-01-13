package com.github.flashvayne.dto;

/**
 * 分页的数据封装
 *
 * @author flashvayne
 */
public class PageInfo {
    private int start = 0;
    private int size = 10;

    private Long total;
    private Object list;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "start=" + start +
                ", size=" + size +
                ", total=" + total +
                ", list=" + list +
                '}';
    }
}
