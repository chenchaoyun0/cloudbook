<%@ page contentType="text/html; charset=gbk"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>Cluster App Test</title>
</head>
<body>
	Server Info:
	<%
	out.println(request.getLocalAddr() + " : " + request.getLocalPort()
			+ "<br>");
	%>
	<%
		out.println("<br> ID " + session.getId() + "<br>");
		// ������µ� Session ��������
		String dataName = request.getParameter("dataName");
		if (dataName != null && dataName.length() > 0) {
			String dataValue = request.getParameter("dataValue");
			session.setAttribute(dataName, dataValue);
		}
		out.println("<b>Session �б�</b><br>");
		System.out.println("============================");
		Enumeration e = session.getAttributeNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = session.getAttribute(name).toString();
			out.println(name + " = " + value + "<br>");
			System.out.println(name + " = " + value);
		}
	%>
	
</body></html>
