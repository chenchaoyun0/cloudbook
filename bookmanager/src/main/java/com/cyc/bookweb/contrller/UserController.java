package com.cyc.bookweb.contrller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cyc.bookweb.feignclient.IBookUserClient;
import com.cyc.common.po.GridfsImg;
import com.cyc.common.po.User;
import com.cyc.common.utils.CommonUtils;
import com.cyc.common.utils.SHA;
import com.cyc.common.utils.TelUtil;
import com.cyc.common.utils.exception.UserException;
import com.cyc.common.utils.mail.Mail;
import com.cyc.common.utils.mail.MailUtils;
import com.cyc.common.utils.pages.PagedResult;

@Controller
@RequestMapping("/user")
public class UserController {
  private static final Logger log = LoggerFactory.getLogger(UserController.class);
//  @Autowired
  private IBookUserClient userService;

  @RequestMapping("/indexHome")
  public String indexHome() {
    return "indexHome";
  }

  @RequestMapping("/xcConfirmDemo")
  public String xcConfirmDemo() {
    return "xcConfirmDemo";
  }

  @RequestMapping("/indextest")
  public String indextest() {
    return "index";
  }

  @RequestMapping("/userRegistInput")
  public String userRegistInput() {
    return "user/userRegist";
  }

  @RequestMapping("/userLoginInput")
  public String userLoginInput() {
    return "user/userLogin";
    // return "redirect:user/userLogin";
  }

  @RequestMapping(value = "/existUserEmail", method = RequestMethod.POST)
  public @ResponseBody String existUserEmail(String userEmail) {
    boolean b = userService.existUserEmail(userEmail);
    if (b) {
      return "true";
    }
    return "false";
  }

  @RequestMapping(value = "/existLoginName", method = RequestMethod.POST)
  public @ResponseBody String existLoginName(String loginName) {
    boolean b = userService.existUserName(loginName);
    if (b) {
      return "true";
    }
    return "false";
  }

  @RequestMapping(value = "/userTelHome", method = RequestMethod.POST)
  public @ResponseBody String userTelHome(HttpServletRequest request, String userTel)
    throws UnsupportedEncodingException {
    String telHome = TelUtil.getTelHome(userTel);

    return telHome;
  }

  @RequestMapping(value = "/userRegistSubmit", method = RequestMethod.POST)
  public String userRegistSubmit(HttpServletRequest request, HttpServletResponse response, User user)
    throws FileNotFoundException, IOException {
    /**
     * 后端验证
     */
    String sessionCode = (String)request.getSession().getAttribute("session_vcode");
    String paramCode = request.getParameter("verifyCode");
    if (!paramCode.equalsIgnoreCase(sessionCode)) {// 验证码
      request.setAttribute("error_code", "验证码错误");
      return "forward:/user/userRegistInput.action";
    }
    if (userService.existUserEmail(user.getUserEmail())) {
      response.getWriter().print("<script>alert('the email has been registered');history.back();</script>");
      return null;
    }
    if (userService.existUserName(user.getLoginName())) {
      response.getWriter().print("<script>alert('the loginName has been registered');history.back();</script>");
      return null;
    }
    user.setUserPwd(SHA.getSHA256(user.getUserPwd()));
    user.setUserId(CommonUtils.uuid());
    /*
     * 默认头像
     */
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("userhead.jpg");
    GridfsImg gridfsImg = new GridfsImg();
    gridfsImg.setIn(inputStream);
    gridfsImg.setAliases(user.getUserId());
    gridfsImg.setFileName("userhead.jpg");
    //
    // String url = baseMongoRepository.saveImg(gridfsImg);
    String url = "http://39.107.126.75/gridfs/5afad7bc6143a025104bd48d";
    user.setUserHead(url);
    user.setUserCode(CommonUtils.uuid() + CommonUtils.uuid());
    user.setUserStatus(0);
    user.setUserRegisttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    /**
     * 第一个注册用户为管理员
     */
    PagedResult<User> selectUserPages = userService.selectUserPages(null, null, null);
    if (selectUserPages.getTotal() == 0) {
      user.setUserRole(1);
    } else {
      user.setUserRole(0);
    }
    try {
      userService.insertSelective(user);
    } catch (Exception e) {
      request.setAttribute("msg", e.getMessage());
      request.setAttribute("user", user);
      return "forward:/user/userRegistInput.action";
    }

    /**
     * 发邮件 准备配置文件！
     */
    log.info("发邮件 准备配置文件...");
    Properties props = new Properties();
    props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));// 获取配置文件内容
    String host = props.getProperty("host");// 获取服务器主机
    String port = props.getProperty("port");
    String uname = props.getProperty("uname");// 获取用户名
    String pwd = props.getProperty("pwd");// 获取密码
    String from = props.getProperty("from");// 获取发件人
    String to = user.getUserEmail();// 获取收件人
    log.info("获取收件人...userEmail:{}", to);
    // String to = "873692191@qq.com";// 获取收件人
    String subject = props.getProperty("subject");// 获取主题
    log.info("获取主题subject:{}", subject);
    String content = props.getProperty("content");// 获取邮件内容
    log.info("获取邮件内容content:{}", content);
    int serverPort = request.getServerPort();
    log.info("serverPort:{}", serverPort);
    String serverName = request.getServerName();
    log.info("serverName:{}", serverName);
    content = MessageFormat.format(content, serverName + ":" + serverPort, user.getUserCode());// 替换{0}
    log.info(" 替换{0},content:{}", content);

    // Session session = MailUtils.createSession(host, uname, pwd);//
    // 得到session
    /**
     * add session
     */
    // 创建验证器
    Authenticator auth = new Authenticator() {
      public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(uname, pwd);
      }
    };
    Properties prop = new Properties();
    prop.setProperty("mail.transport.protocol", "smtp");// 邮件发送协议
    prop.setProperty("mail.host", host);// 指定主机
    prop.setProperty("mail.smtp.port", port);
    log.info("port:{}", port);
    prop.setProperty("mail.smtp.auth", "true");// 指定验证为true
    prop.setProperty("mail.debug", "true");// 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    log.info("prop:{}", JSONObject.toJSON(prop));
    Session session = Session.getDefaultInstance(prop, auth);
    //
    Mail mail = new Mail(from, to, subject, content);// 创建邮件对象
    log.info("邮件对象mail:{}", JSONObject.toJSON(mail));
    try {
      log.info("发邮件begin..");
      MailUtils.send(session, mail);// 发邮件
      log.info("发邮件end..");
    } catch (MessagingException e) {
      log.info("发邮件异常,{}", e);
      request.setAttribute("msg", "当前网络异常，请检查您的网络后到您注册的邮箱激活");
      request.setAttribute("user", user);
      request.setAttribute("tz", "user/userLoginInput");
      return "forward:/error/msg.jsp";
    }
    log.info("发送邮件成功");
    /**
     * 网络正常，发送邮件成功了 1. 保存成功信息 2. 转发到msg.jsp <meta http-equiv="Refresh" content="3;url=http://${requestScope.tz}">
     * 设置响应头，3秒后跳转
     */
    request.setAttribute("msg", "注册成功！请前往您的邮箱点击激活链接激活。3秒后自动跳转到邮箱登录页面......");

    String email = user.getUserEmail();
    request.setAttribute("user", user);
    request.setAttribute("tz", "mail." + email.substring(email.lastIndexOf("@") + 1));

    // return "forward:indexHome";
    // return "redirect:user/user/userLogin";
    return "forward:/error/redirect.jsp";
  }

  @RequestMapping(value = "/activeUserStatus/{userCode}", method = RequestMethod.GET)
  public ModelAndView activeUserStatus(@PathVariable("userCode") String userCode, HttpServletRequest request,
    HttpServletResponse response, Model model) throws IOException {
    ModelAndView modelAndView = new ModelAndView();
    try {
      userService.activeUserStatus(1, userCode);
      request.setAttribute("msg", "恭喜您，激活成功，请登录。3秒后自动跳转到登录页面......");
      modelAndView.setViewName("forward:/error/msg.jsp");
      int serverPort = request.getServerPort();
      String serverName = request.getServerName();
      request.setAttribute("serverHost", serverName + ":" + serverPort);
      request.setAttribute("tz", "user/userLoginInput");
      return modelAndView;
    } catch (Exception e) {
      request.setAttribute("msg", e.getMessage());
      request.setAttribute("tz", "user/userLoginInput");
      modelAndView.setViewName("forward:/error/msg.jsp");
      return modelAndView;
    }
  }

  @RequestMapping(value = "/userLoginSubmit", method = RequestMethod.POST)
  public String userLoginSubmit(String loginName, String userPwd, HttpServletRequest request) {
    User user = null;
    try {
      user = userService.userLoginSubmit(loginName, userPwd);
    } catch (UserException e) {
      request.setAttribute("errorMsg", e.getMessage());
      return "forward:/user/userLoginInput.action";
    }
    request.getSession().setAttribute("userLogin", user);
    return "redirect:/book/selectBookPages.action";
  }

  @RequestMapping(value = "/userLoginOut", method = RequestMethod.GET)
  public String userLoginOut(HttpServletRequest request) {
    request.getSession().removeAttribute("userLogin");
    return "forward:/book/selectBookPages.action";
  }

  @RequestMapping("/updateUpdatePassword")
  public String updateDetail() {
    return "/user/userUpdatePassword";
  }

  @RequestMapping("/getThisUser")
  @ResponseBody
  public User getThisUser(String userId) {
    return userService.selectByPrimaryKey(userId);
  }

  @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
  @ResponseBody
  public int updatePassword(HttpServletRequest request, String userId, String loginName, String oripassword,
    String nowpassword, String verifyCode) {
    String sessionCode = (String)request.getSession().getAttribute("session_vcode");
    if (sessionCode.equals(verifyCode)) {
      return -1;
    }
    // 验证密码是否正确
    User user = null;
    try {
      user = userService.userLoginSubmit(loginName, oripassword);
    } catch (UserException e) {
      return -2;
    }
    // 对密码经行加密后加入
    user.setUserId(userId);
    user.setUserPwd(SHA.getSHA256(nowpassword));
    int i = userService.updatePwd(user);
    return i;
  }

  @RequestMapping("/admin")
  public String admin() {
    return "/admin/adminHome";
  }

  @RequestMapping("/adminData")
  @ResponseBody
  public PagedResult<User> adminData(User user, Integer pageNo, Integer pageSize) {
    PagedResult<User> list = userService.selectUserPages(user, pageNo, pageSize);
    return list;
  }

  @RequestMapping("/updatePermission/{userId}")
  @ResponseBody
  public int updatePermission(@PathVariable("userId") String userId) {
    return userService.updatePermission(userId);
  }

  @RequestMapping("/chatWithRobot")
  @ResponseBody
  public String chatWithRobot(@RequestParam("user_say") String user_say) {
    String robotRes = userService.chatWithRobot(user_say);
    log.info("chatWithRobot:req:{},res:{}", user_say, robotRes);
    return robotRes;
  }

}
