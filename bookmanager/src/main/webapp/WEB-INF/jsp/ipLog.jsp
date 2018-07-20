<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//Dth HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dth">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网站访问IP日志 by 陈超允</title>
</head>
<body style="background-color: black;">
<!-- 顶部导航 start -->
    <jsp:include page="public/top.jsp"></jsp:include>
<div align="center" style="margin-top: 20px" >
                <a href="<c:url value='/book/selectBookPages'/>"><img src="<c:url value='img/myheader.jpeg'/>" style="width: 150px;height: 100px;" alt="数通图书"/>
                </a>
            <%-- <object height="100" width="100" data="${pageContext.request.contextPath}/music/jj-qt.mp3"></object> --%>
            <%-- <EMBED src="${pageContext.request.contextPath}/music/jj-qt.mp3" width="300" height="45" type="audio/mpeg" loop="true" autostart="true" volume="100"></EMBED> --%>
            </div>
        <div align="center" style="margin-top:10px">
<p style="color:aqua;font-weight: bolder;font-size: 20px;font-family:cursive;">今日总访问人数：${todayCount.todayVisitorCount} 人，今日总访问量：${todayCount.todayCount} 次</p>
<br/>
<p style="color: aqua;font-weight: bolder;font-size: 20px;font-family:cursive;">总访问人数：${pages.total} 人，总访问量：${totalcount} 次，网站爬虫量：${totalBlackList} 次</p>
<br/>
<a style="color: lime;font-weight: bold;font-family:cursive;" href="http://www.shopbop.ink" target="_bank">点此查看他在线简历</a>
<br/>
</div>

<div align="center" style="margin-top: 20px" >
<div style="color:activecaption;">
当前主机:
<%
String dataName = request.getParameter("dataName");
if (dataName != null && dataName.length() > 0) {
    String dataValue = request.getParameter("dataValue");
    session.setAttribute(dataName, dataValue);
}
%>
    <%
    out.println(request.getLocalAddr() + " : " + request.getLocalPort()
            + "<br>");
    %>
    <%
        out.println("<br> SESSIONID :" + session.getId() + "<br>");
        %>
</div>
<br/>
<hr/>
<c:if test="${pages.total le 0 }">
            <div align="center" style="margin-top: 30px;">目前没有数据!</div>
</c:if>
<c:if test="${pages.total gt 0 }">
        <table align="center" width="100%" >
                <tr style="color:red; font-family:fantasy;">
                        <th >用户名</th>
                        <th >用户IP</th>
                        <th >访问时间</th>
                        <th >访问来源</th>
                        <th >浏览器</th>
                        <th >系统名称</th>
                        <th >设备类型</th>
                        <th >经纬度</th>
                        <th >访问内容</th>
                        <th >执行时间</th>
                        <th >访问次数</th>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
<c:forEach items="${pages.dataList}" var="log">
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <th style="color: orange;">${log.userName}</th>
                    <th style="color: orange"><a href="<c:url value='indexHomeForIp?userIp=${log.userIp}'/>" style="color:lime; text-decoration:underline;">${log.userIp}</a></th>
                    <th style="color: orange">${log.operTime}</th>
                    <th style="color: orange">${log.userAddress}</th>
                    <th style="color: orange">${log.browserAndVersion}</th>
                    <th style="color: orange">${log.sysManufacturer}-${log.sysName}</th>
                    <th style="color: orange">${log.deviceType}</th>
                    <th style="color: orange">${log.userJwd}</th>
                    <th style="color: orange">${log.action}</th>
                    <th style="color: orange">${log.actionTime}</th>
                    <th style="color: orange">${log.count}</th>
                </tr>
            </c:forEach>
        </table>
</c:if>
</div>
<hr/>
<div style="margin-top: 20px">
    <jsp:include page="public/page.jsp"></jsp:include>
    </div>
	<div align="center" style="color: white;">
	<br/>
	项目gitHub:<a style="color: lime;" href="https://github.com/chenchaoyun0/cloudbook" target="_bank">https://github.com/chenchaoyun0/cloudbook</a>
	，Jenkins:<a style="color: lime;" href="http://www.lilinjue.cn:8081" target="_bank">http://www.longge1987.cn/jenkins/</a>
	，Zookeeper节点管理:<a style="color: lime;" href="http://www.shopbop.ink/zkWeb/" target="_bank">http://www.shopbop.ink/zkWeb/</a>
	<br/>
	<br/>
	私服库:<a style="color: lime;" href="http://www.lilinjue.cn:8080/nexus" target="_bank">http://www.longge1987.cn:8080/nexus/</a>
	，Kafka-offset-monitor:<a style="color: lime;" href="http://39.105.33.58:8086/#/" target="_bank">http://39.105.33.58:8086/#/</a>
	<br/>
	<br/>
 Eureka:<a style="color: lime;" href="http://123.207.168.248:9099/" target="_bank">http://123.207.168.248:9099/</a>
	，Dubbo:<a style="color: lime;" href="http://39.105.33.58:8081/dubbo-admin" target="_bank">http://www.ccy123.cn/dubbo-admin/</a>
	<br/>
	<br/>
Gerrit:<a style="color: lime;" href="http://www.shopbop.ink/gerrit" target="_bank">http://www.shopbop.ink/gerrit</a>，	
博客:<a style="color: lime;" href="http://blog.csdn.net/sinat_22767969" target="_bank">http://blog.csdn.net/sinat_22767969</a>
	<br/>
	<br/>
	备案/许可证编号:<a style="color: lime;" href="http://www.miitbeian.gov.cn" target="_bank">桂ICP备16001571</a>
</div>
</body>
</html>
