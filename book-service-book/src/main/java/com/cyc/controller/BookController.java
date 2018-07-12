package com.cyc.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.cyc.common.base.ErrorCode;
import com.cyc.common.dictionary.ImgLinkType;
import com.cyc.common.po.Book;
import com.cyc.common.po.GridfsImg;
import com.cyc.common.po.TImg;
import com.cyc.common.po.User;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.file.BookFileUtils;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.SelectBookDetailReq;
import com.cyc.common.vo.SelectBookDetailResp;
import com.cyc.common.vo.SelectBookPagesReq;
import com.cyc.common.vo.SelectBookPagesResp;
import com.cyc.common.vo.UploadBookSubmitReq;
import com.cyc.common.vo.UploadBookSubmitResp;
import com.cyc.service.IBaseMongoRepository;
import com.cyc.service.IBookService;
import com.cyc.service.IImgService;

import cn.itcast.commons.CommonUtils;

@RestController
public class BookController {
  private static final Logger log = LoggerFactory.getLogger(BookController.class);
  @Autowired
  private IBookService bookService;
  @Autowired
  private IImgService imgService;

  @Autowired
  private IBaseMongoRepository<?> baseMongoRepository;

  @Value("${server.port}")
  private String port;

  @RequestMapping("/info")
  public String info() {
    PagedResult<Book> pages = bookService.selectBookPages(new Book(), 1, 10);
    String jsonString = JSONObject.toJSONString(pages);
    return "测试 book-book" + ",port:" + port+";测试响应:"+jsonString;
  }

  @RequestMapping(value = "/uploadBookSubmit", method = RequestMethod.POST)
  public UploadBookSubmitResp uploadBookSubmit(@RequestParam("req") UploadBookSubmitReq req,
    @RequestParam("bookFile") MultipartFile[] bookFile) {
    
    log.info(">>>>>>>>>uploadBookSubmit 请求:{}", JSONObject.toJSONString(req));
    
    UploadBookSubmitResp resp = new UploadBookSubmitResp();
    Book book = req.getBook();
    User user = req.getUser();
    String bookId = book.getBookId();
    /**
     * 图片处理
     */
    log.info("bookFile:{}", bookFile.length);
    List<TImg> imgList = new ArrayList<>();
    try {
      for (int i = 0; i < bookFile.length; i++) {
        MultipartFile myfile = bookFile[i];
        if (myfile.isEmpty()) {
          TImg tImg = new TImg();
          tImg.setCreateTime(new Date());
          tImg.setCreateUser(user.getLoginName());
          tImg.setImgId(CommonUtils.uuid());
          // 未选择图片择读取默认图片
          InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("defaultBookImg.jpg");
          //
          GridfsImg gridfsImg = new GridfsImg();
          gridfsImg.setIn(inputStream);
          gridfsImg.setAliases(tImg.getImgId());
          gridfsImg.setFileName("defaultBookImg.jpg");
          //
          String saveImg = baseMongoRepository.saveImg(gridfsImg);
          tImg.setImgPath(saveImg);
          tImg.setLastModifyTime(new Date());
          tImg.setLastModifyUser(user.getLoginName());
          tImg.setLinkId(bookId);
          tImg.setLinkType(ImgLinkType.BookImg.getCode());
          log.info("保存图片信息begin...tImg:{}", JSONObject.toJSON(tImg));
          imgList.add(tImg);
        } else {
          // 格式错误
          String originalFilename = myfile.getOriginalFilename();
          String imgEnd = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
          if (!BookFileUtils.isImgFile(imgEnd)) {
            resp.setBook(book);
            resp.setMsg("请上传jpg格式的图片");
            return resp;
          }

          log.info("文件长度: " + myfile.getSize());
          log.info("文件类型: " + myfile.getContentType());
          log.info("文件名称: " + myfile.getName());
          log.info("文件原名: " + myfile.getOriginalFilename());
          log.info("========================================");
          // 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
          /* 保存文件夹 */
          /* 真实路径 */
          TImg tImg = new TImg();
          tImg.setCreateTime(new Date());
          tImg.setCreateUser(user.getLoginName());
          tImg.setImgId(CommonUtils.uuid());
          //
          GridfsImg gridfsImg = new GridfsImg();
          gridfsImg.setIn(myfile.getInputStream());
          gridfsImg.setAliases(tImg.getImgId());
          gridfsImg.setFileName(originalFilename);
          //
          String saveImg = baseMongoRepository.saveImg(gridfsImg);
          tImg.setImgPath(saveImg);
          tImg.setLastModifyTime(new Date());
          tImg.setLastModifyUser(user.getLoginName());
          tImg.setLinkId(bookId);
          tImg.setLinkType(ImgLinkType.BookImg.getCode());
          log.info("保存图片信息begin...tImg:{}", JSONObject.toJSON(tImg));
          imgList.add(tImg);
        }
      }
    } catch (Exception e) {
      resp.setBook(book);
      resp.setMsg(e.getMessage());
      return resp;
    }
    /**
     * 插入数据库
     */
    try {
      book.setBookImg(imgList.size() + "");
      log.info("imgList size:{}条", imgList.size());
      int j = bookService.insertSelective(book);
      log.info("保存图书:{}条", j);
      log.info("imgList:{}", JSONObject.toJSON(imgList));
      int i = imgService.batchSaveImg(imgList);
      log.info("保存图片:{}条", i);
    } catch (UserException e) {
      resp.setBook(book);
      resp.setMsg(e.getMessage());
      return resp;
    }

    List<String> imageBase64StrList = BookFileUtils.getImageBase64StrList(imgList);
    resp.setImgList(imageBase64StrList);
    resp.setBook(book);
    resp.setMsg("上传成功");
    log.info(">>>>>>>>>uploadBookSubmit 响应:{}", JSONObject.toJSONString(resp));
    return resp;
  }

  @RequestMapping(value = "/selectBookPages", method = {RequestMethod.POST})
  public SelectBookPagesResp selectBookPages(@RequestBody SelectBookPagesReq req) {

    log.info(">>>>>>>>>selectBookPages 请求:{}", JSONObject.toJSONString(req));

    String loginName = req.getLoginName();
    Book book = req.getBook();
    String userId = req.getUserId();
    Integer pageNo = req.getPageNo();
    Integer pageSize = req.getPageSize();
    //
    SelectBookPagesResp resp = new SelectBookPagesResp();
    try {
      User user = new User();
      user.setLoginName(loginName);
      book.setUser(user);
      if (!"".equals(userId) && userId != null) {
        book.setUserId(userId);
      }
      /* 分页 */
      PagedResult<Book> pages = bookService.selectBookPages(book, pageNo, pageSize);
      resp.setPages(pages);
    } catch (Exception e) {
      resp.setMsg(e.getMessage());
    }
    //
    log.info(">>>>>>>>>selectBookPages 响应:{}", JSONObject.toJSONString(resp));
    return resp;
  }

  @RequestMapping(value = "/selectBookDetail", method = {RequestMethod.POST})
  public SelectBookDetailResp selectBookDetail(@RequestBody SelectBookDetailReq req) {
    log.info(">>>>>>>>>selectBookDetail 请求:{}", JSONObject.toJSONString(req));
    SelectBookDetailResp resp = new SelectBookDetailResp();
    String bookId = req.getBookId();
    try {
      Book book = bookService.selectByPrimaryKey(bookId);
      TImg tImg = new TImg();
      tImg.setLinkId(bookId);
      log.info("查询图书图片begin...");
      List<TImg> imgList = imgService.selectList(tImg);
      log.info("查询图书图片end...imgList:{}", JSONObject.toJSON(imgList));
      List<String> imageBase64StrList = BookFileUtils.getImageBase64StrList(imgList);
      resp.setBook(book);
      resp.setImgList(imageBase64StrList);
    } catch (Exception e) {
      resp.setCode(ErrorCode.ERROR_CODE);
      resp.setMsg(e.getMessage());
    }
    log.info(">>>>>>>>>selectBookDetail 响应:{}", JSONObject.toJSONString(resp));
    return  resp;
  }

  @RequestMapping(value = "updateBookInput/{bookId}", method = {RequestMethod.GET})
  public String updateBook(@PathVariable("bookId") String bookId, Model model) {
    Book book = bookService.selectByPrimaryKey(bookId);
    model.addAttribute("book", book);
    //
    TImg tImg = new TImg();
    tImg.setLinkId(bookId);
    log.info("查询图书图片begin...");
    List<TImg> imgList = imgService.selectList(tImg);
    log.info("查询图书图片end...imgList:{}", JSONObject.toJSON(imgList));
    List<String> imageBase64StrList = BookFileUtils.getImageBase64StrList(imgList);
    model.addAttribute("imgList", imageBase64StrList);
    return "book/updateBook";
  }

  @RequestMapping(value = "updateBookSubmit", method = {RequestMethod.POST})
  public String updateBookSubmit(@ModelAttribute("book") Book book, Model model, HttpServletRequest request) {
    // ...方便测试暂时不做校验
    String sessionCode = (String)request.getSession().getAttribute("session_vcode");
    String paramCode = request.getParameter("verifyCode");
    if (!paramCode.equalsIgnoreCase(sessionCode)) {// 验证码
      model.addAttribute("book", book);
      request.setAttribute("error_code", "验证码错误");
      return "book/updateBook";
    }
    bookService.updateByPrimaryKeySelective(book);
    model.addAttribute("book", book);
    model.addAttribute("successMsg", "更新成功");
    return "book/updateBook";
  }

  @RequestMapping("/admin")
  public String admin() {
    return "/admin/bookadmin";
  }

  @RequestMapping("/adminData")
  @ResponseBody
  public PagedResult<Book> adminData(Book book, Integer pageNo, Integer pageSize) {
    User user = new User();
    book.setUser(user);
    PagedResult<Book> list = bookService.selectBookPages(book, pageNo, pageSize);
    return list;
  }

  @RequestMapping("/unmountBook/{bookId}")
  @ResponseBody
  public int unmountBook(@PathVariable("bookId") String bookId) {

    return bookService.unmountBook(bookId);
  }

  @RequestMapping("/mountBook/{bookId}")
  @ResponseBody
  public int mountBook(@PathVariable("bookId") String bookId) {

    return bookService.mountBook(bookId);
  }
}
