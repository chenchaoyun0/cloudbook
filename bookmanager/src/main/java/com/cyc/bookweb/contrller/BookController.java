package com.cyc.bookweb.contrller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.bookweb.feignclient.IBookBookClient;
import com.cyc.common.po.Book;
import com.cyc.common.po.TImg;
import com.cyc.common.po.User;
import com.cyc.common.utils.CommonUtils;
import com.cyc.common.utils.UUID2NO;
import com.cyc.common.utils.file.BookFileUtils;
import com.cyc.common.utils.pages.PagedResult;
import com.cyc.common.vo.SelectBookDetailReq;
import com.cyc.common.vo.SelectBookDetailResp;
import com.cyc.common.vo.SelectBookPagesReq;
import com.cyc.common.vo.SelectBookPagesResp;
import com.cyc.common.vo.UploadBookSubmitReq;
import com.cyc.common.vo.UploadBookSubmitResp;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/book")
public class BookController {

  @Autowired
  private IBookBookClient bookBookClient;

  @Value("${server.port}")
  private String port;

  @RequestMapping("/info")
  public String info() {
    return "测试 book-book" + ",port:" + port;
  }

  @RequestMapping(value = "/uploadBookInput", method = {RequestMethod.GET, RequestMethod.POST})
  public String uploadBookInput() {
    return "book/uploadBook";
  }

  @RequestMapping(value = "/uploadBookSubmit", method = RequestMethod.POST)
  public String uploadBookSubmit(@ModelAttribute("book") Book book, HttpServletResponse response,
    HttpServletRequest request, Model model, @RequestParam("bookFile") MultipartFile[] bookFile) throws IOException {
    /* 验证码 */
    // ...方便测试暂时不做校验
    String sessionCode = (String)request.getSession().getAttribute("session_vcode");
    String paramCode = request.getParameter("verifyCode");
    if (!paramCode.equalsIgnoreCase(sessionCode)) {// 验证码
      request.setAttribute("error_code", "验证码错误");
      return "book/uploadBook";
    }
    /* 获取登陆用户 */
    User user = (User)request.getSession().getAttribute("userLogin");
    String bookId = CommonUtils.uuid();
    book.setBookId(bookId);
    book.setBookNo(UUID2NO.getUUID2NO());
    book.setBookStatus(1);
    book.setBookRemain(book.getBookCount());
    book.setUserId(user.getUserId());
    book.setBookUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    book.setBookFlag(1);

    //
    UploadBookSubmitReq req = new UploadBookSubmitReq();
    req.setBook(book);
    req.setUser(user);
    // excut
    UploadBookSubmitResp resp = bookBookClient.uploadBookSubmit(req, bookFile);

    List<String> imgList = resp.getImgList();
    Book bookModel = resp.getBook();

    //

    model.addAttribute("successMsg", "上传成功");
    model.addAttribute("imgList", imgList);
    model.addAttribute("book", bookModel);
    return "book/uploadBook";
  }

  @RequestMapping(value = "selectBookPages", method = {RequestMethod.GET, RequestMethod.POST})
  public String selectBookPages(Book book, String loginName, Model model, HttpServletRequest request,
    ModelAndView modelAndView, Integer pageNo, Integer pageSize,
    @RequestParam(value = "userId", required = false) String userId) {
    User user = new User();
    user.setLoginName(loginName);
    book.setUser(user);
    if (!"".equals(userId) && userId != null) {
      book.setUserId(userId);
    }

    SelectBookPagesReq req = new SelectBookPagesReq();
    req.setBook(book);
    req.setUserId(userId);
    req.setPageNo(pageNo);
    req.setPageSize(pageSize);
    req.setLoginName(loginName);
    /* 分页 */
    SelectBookPagesResp resp = bookBookClient.selectBookPages(req);
    PagedResult<Book> pages = resp.getPages();
    String url = request.getRequestURI();
    pages.setUrl(url);
    //
    model.addAttribute("pages", pages);

    return "indexHome";
  }

  @RequestMapping(value = "selectBookDetail/{bookId}", method = {RequestMethod.GET})
  public String selectBookDetail(@PathVariable("bookId") String bookId, Model model) {
    SelectBookDetailReq req = new SelectBookDetailReq();
    req.setBookId(bookId);
    SelectBookDetailResp resp = bookBookClient.selectBookDetail(req);
    Book book = resp.getBook();
    List<String> imgList = resp.getImgList();
    model.addAttribute("book", book);
    model.addAttribute("imgList", imgList);
    return "book/bookDetail";
  }
  
  @RequestMapping(value = "updateBookInput/{bookId}", method = { RequestMethod.GET })
  public String updateBook(@PathVariable("bookId") String bookId, Model model) {
      Book book = bookBookClient.selectByPrimaryKey(bookId);
      model.addAttribute("book", book);
      //
      TImg tImg = new TImg();
      tImg.setLinkId(bookId);
      log.info("查询图书图片begin...");
      List<TImg> imgList = bookBookClient.selectList(tImg);
      log.info("查询图书图片end...imgList:{}", JSONObject.toJSON(imgList));
      List<String> imageBase64StrList =BookFileUtils.getImageBase64StrList(imgList);
      model.addAttribute("imgList", imageBase64StrList);
      return "book/updateBook";
  }

  @RequestMapping(value = "updateBookSubmit", method = { RequestMethod.POST })
  public String updateBookSubmit(@ModelAttribute("book") Book book, Model model, HttpServletRequest request) {
      // ...方便测试暂时不做校验
      String sessionCode = (String) request.getSession().getAttribute("session_vcode");
      String paramCode = request.getParameter("verifyCode");
      if (!paramCode.equalsIgnoreCase(sessionCode)) {// 验证码
          model.addAttribute("book", book);
          request.setAttribute("error_code", "验证码错误");
          return "book/updateBook";
      }
      bookBookClient.updateByPrimaryKeySelective(book);
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
      SelectBookPagesReq req = new SelectBookPagesReq(book, pageNo, pageSize);
      SelectBookPagesResp resp = bookBookClient.selectBookPages(req);
      PagedResult<Book> list = resp.getPages();
      return list;
  }

  @RequestMapping("/unmountBook/{bookId}")
  @ResponseBody
  public int unmountBook(@PathVariable("bookId") String bookId) {

      return bookBookClient.unmountBook(bookId);
  }

  @RequestMapping("/mountBook/{bookId}")
  @ResponseBody
  public int mountBook(@PathVariable("bookId") String bookId) {

      return bookBookClient.mountBook(bookId);
  }
}
