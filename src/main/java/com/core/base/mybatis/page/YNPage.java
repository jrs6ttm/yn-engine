package com.core.base.mybatis.page;

import java.util.List;

/**
 * 
 * @ClassName: YNPage 
 * @Description: 分页处理类
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:25:18 
 */
public class YNPage<T> {
    public static final int DEFAULT_PAGE_SIZE = 20;
    private Integer pageSize = 20;
    private Integer pageIndex = 0;
    private Integer pageTotal;
    private Integer totalCount;
    private List<T> data;

    private Integer start;

    public YNPage() {
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageTotal() {
        return this.pageTotal;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if (this.pageIndex != null && this.pageSize != null) {
            start = pageIndex * pageSize;
        }
    }

    public Integer getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        this.caculate();
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    private void caculate() {
        if (this.pageSize != 0) {
            this.pageTotal = this.totalCount / this.pageSize + (this.totalCount % this.pageSize > 0 ? 1 : 0);
        }

        if (this.pageTotal < 1) {
            this.pageTotal = 1;
        }

    }
}


