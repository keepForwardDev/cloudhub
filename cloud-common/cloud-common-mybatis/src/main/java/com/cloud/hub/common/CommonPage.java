package com.cloud.hub.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class CommonPage {
    private long totalCount = 0;

    private long pageCount = 0;

    private long pageSize = 20;

    private long currentPage = 1;

    private Object list = new String();

    public CommonPage() {

    }

    public CommonPage(Page page) {
        this.totalCount = page.getTotal();
        this.pageCount = page.getPages();
        this.pageSize = page.getSize();
        this.currentPage = page.getCurrent();
        this.list = page.getRecords();
    }

    public CommonPage(Page page, Object list) {
        this.totalCount = page.getTotal();
        this.pageCount = page.getPages();
        this.pageSize = page.getSize();
        this.currentPage = page.getCurrent();
        this.list = list;
    }

    public CommonPage(long totalCount, int pageCount, int pageSize, int currentPage, Object list) {
        super();
        this.totalCount = totalCount;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.list = list;
    }

    public CommonPage(long totalCount, int currentPage, int pageSize, Object list) {
        super();
        this.totalCount = totalCount;
        //计算总页数
        int pageCount = (int) (totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            pageCount++;
        }
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.list = list;

    }

    public long getTotalCount() {
        if (this.totalCount < 0) {
            return 0;
        }
        if (this.totalCount > Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        //计算总页数
        int pageCount = (int) (totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            pageCount++;
        }
        this.pageCount = pageCount;
        this.totalCount = totalCount;

    }

    public long getPageCount() {
        if (this.pageCount < 0) {
            this.pageCount = 0;
        }
        if (this.pageCount > Integer.MAX_VALUE) {
            this.pageCount = Integer.MAX_VALUE;
        }
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getPageSize() {
        if (this.pageSize < 0) {
            this.pageSize = 0;
        }
        if (this.pageSize > Integer.MAX_VALUE) {
            this.pageSize = Integer.MAX_VALUE;
        }
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getCurrentPage() {
		/*if(this.pageCount>0&&this.currentPage>this.pageCount){
			return this.pageCount;
		}*/
        if (this.currentPage <= 0 || this.currentPage > Integer.MAX_VALUE) {
            this.currentPage = 1;
        }
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CommonPage [totalCount=" + totalCount + ", pageCount=" + pageCount + ", pageSize=" + pageSize
                + ", currentPage=" + currentPage + ", list=" + list + "]";
    }

}
