<%--
  Created by IntelliJ IDEA.
  User: jy
  Date: 2019/12/23
  Time: 12:36 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h1>登录成功页面</h1>
<hr>

${user}
<a href="${pageContext.request.contextPath}/soso/logout">注销</a>
</body>
</html>