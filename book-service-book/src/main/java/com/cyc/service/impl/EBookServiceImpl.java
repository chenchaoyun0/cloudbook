package com.cyc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.po.EBook;
import com.cyc.common.po.TImg;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.pages.BeanUtil;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.mapper.EBookMapper;
import com.cyc.service.IEBookService;
import com.cyc.service.IImgService;
import com.github.pagehelper.PageHelper;

@Service("eBookService")
public class EBookServiceImpl implements IEBookService {
    @Autowired
    private EBookMapper eBookMapper;
    private static final Logger log = LoggerFactory.getLogger(EBookServiceImpl.class);
    @Autowired
    private IImgService imgService;
    public int insertSelective(EBook eBook) {
        try {
            return eBookMapper.insertSelective(eBook);
        } catch (Exception e) {
            throw new UserException("上传失败");
        }
    }

    public EBook selectByPrimaryKey(String ebookId) {

        try {
            return eBookMapper.selectByPrimaryKey(ebookId);
        } catch (Exception e) {
      log.error("selectByPrimaryKey error:{}", e);
            throw new UserException("查询失败");
        }
    }

    public PagedResult<EBook> selectEBookPages(EBook eBook, Integer pageNo, Integer pageSize) {
        pageNo = (pageNo == null) ? (Integer) 1 : pageNo;
        pageSize = (pageSize == null) ? (Integer) 5 : pageSize;
        PageHelper.startPage(pageNo, pageSize);

        List<EBook> bookList = eBookMapper.selectEBookPages(eBook);
        log.info("bookList:{}", JSONObject.toJSON(bookList));
        List<EBook> arrayList = new ArrayList<EBook>();
        for (EBook eBook2 : bookList) {
            TImg tImg = new TImg();
            tImg.setLinkId(eBook2.getEbookId());
            log.info("查询图书图片begin...");
            List<TImg> imgList = imgService.selectList(tImg);
            log.info("查询图书图片end...imgList:{}", JSONObject.toJSON(imgList));
            TImg tImg2 = imgList.get(0);
            String imgPath = tImg2.getImgPath();
            eBook2.setEbookImg(imgPath);
            arrayList.add(eBook2);
        }

        PagedResult<EBook> bookPagedResult = BeanUtil.toPagedResult(arrayList);

        bookPagedResult.setPageNo(pageNo);
        bookPagedResult.setPageSize(pageSize);

        return bookPagedResult;
    }

    public int updateDownloadCount(String ebookId) {
        return eBookMapper.updateDownloadCount(ebookId);
    }

    public int deleteByPrimaryKey(String ebookId) {
        return eBookMapper.deleteByPrimaryKey(ebookId);
    }
}
