package com.cyc.common.utils.pages;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Data;
@Data
public class PagedResult<T> extends BaseEntity {

    /* serialVersionUID */
    private static final long serialVersionUID = 1L;
    private int pageNo;// 当前页

    private int pageSize;// 一页条数

    private long total;// 总条数

    private long pages;// 总页数
    private int pageOffset;
    private List<T> dataList;// 数据
    private String url;
    private String strWhere;
    @Override
    public int getPageNumber() {
      // TODO Auto-generated method stub
       return 0;
    }
    @Override
    public int getOffset() {
      // TODO Auto-generated method stub
       return 0;
    }
    @Override
    public Sort getSort() {
      // TODO Auto-generated method stub
       return null;
    }
    @Override
    public Pageable next() {
      // TODO Auto-generated method stub
       return null;
    }
    @Override
    public Pageable previousOrFirst() {
      // TODO Auto-generated method stub
       return null;
    }
    @Override
    public Pageable first() {
      // TODO Auto-generated method stub
       return null;
    }
    @Override
    public boolean hasPrevious() {
      // TODO Auto-generated method stub
       return false;
    }
    

}
