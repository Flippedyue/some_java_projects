<%--
  Created by IntelliJ IDEA.
  User: jy
  Date: 2019/12/24
  Time: 6:17 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.kuang.pojo.MobileCard" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SOSO</title>
    <link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/statics/css/demo.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/statics/assets/css/jquery.mCustomScrollbar.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/statics/assets/css/custom.css">

    <script src="${pageContext.request.contextPath}/statics/js/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/statics/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/statics/assets/js/jquery.mCustomScrollbar.concat.min.js"></script>
    <script src="${pageContext.request.contextPath}/statics/assets/js/custom.js"></script>
</head>
<body>

<div class="page-wrapper">
    <nav id="sidebar" class="sidebar-wrapper">
        <div class="sidebar-content">
            <a href="#" id="toggle-sidebar"><i class="fa fa-bars"></i></a>
            <div class="sidebar-brand">
                <a href="#">功能栏</a>
            </div>
            <div class="sidebar-header">
                <div class="user-pic">
                    <img class="img-responsive img-rounded"
                         src="${pageContext.request.contextPath}/statics/assets/img/user.jpg" alt="">
                </div>
                <div class="user-info">
                    <span class="user-name">
                        <strong>
                            <%
                                MobileCard card = (MobileCard) session.getAttribute("cardUser");
                                String username = card.getUserName();
                                String cardNumber = card.getCardNumber();
                            %>
                            <%=username%><br/>
                        </strong>
                    </span>
                    <span class="user-role">
                        <strong>
                            <%=cardNumber%>
                        </strong>
                    </span>
                    <div class="user-status">
                        <a href="#"><span class="label label-success">在线</span></a>
                    </div>
                </div>
            </div><!-- sidebar-header  -->
            <div class="sidebar-search">
                <div>
                    <div class="input-group">
                        <input type="text" class="form-control search-menu" placeholder="Search for...">
                        <span class="input-group-addon"><i class="fa fa-search"></i></span>
                    </div>
                </div>
            </div><!-- sidebar-search  -->
            <div class="sidebar-menu">
                <ul>
                    <li class="header-menu"><span>用户菜单</span></li>
                    <li class="sidebar-dropdown">
                        <a href="#"><i class="fa fa-tv"></i><span>用户信息</span><span class="label label-danger">New</span></a>
                        <div class="sidebar-submenu">
                            <ul>
                                <li><a href="${pageContext.request.contextPath}/soso/<%=cardNumber%>">查看用户信息</a></li>
                                <li><a href="${pageContext.request.contextPath}/soso/toUpdateUser/<%=cardNumber%>">修改用户信息</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li class="sidebar-dropdown">
                        <a href="#"><i class="fa fa-photo"></i><span>使用soso</span></a>
                        <div class="sidebar-submenu">
                            <ul>
                                <li><a href="${pageContext.request.contextPath}/soso/use/通话/<%=cardNumber%>">通话</a></li>
                                <li><a href="${pageContext.request.contextPath}/soso/use/短信/<%=cardNumber%>">短信</a></li>
                                <li><a href="${pageContext.request.contextPath}/soso/use/上网/<%=cardNumber%>">上网</a></li>
                            </ul>
                        </div>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/soso/charge"><i class="fa fa-bar-chart-o"></i><span>充值</span></a>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/soso/changePkg"><i class="fa fa-diamond"></i><span>套餐变更</span></a>
                    </li>
                    <li class="sidebar-dropdown">
                        <a href="#"><i class="fa fa-photo"></i><span>用户消费记录</span></a>
                        <div class="sidebar-submenu">
                            <ul>
                                <li><a href="${pageContext.request.contextPath}/soso/showAmountDetail/<%=cardNumber%>">本月账单查询</a>
                                </li>
                                <li><a href="${pageContext.request.contextPath}/soso/showRemainDetail/<%=cardNumber%>">套餐余量查询</a>
                                </li>
                                <li><a href="${pageContext.request.contextPath}/consum/info/<%=cardNumber%>">查看消费详单</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/soso/quit/<%=cardNumber%>"><i
                            class="fa fa-diamond"></i><span>办理退网</span></a></li>
                </ul>
            </div><!-- sidebar-menu  -->
        </div><!-- sidebar-content  -->
        <div class="sidebar-footer">
            <a href="#"><i class="fa fa-bell"></i><span class="label label-warning notification">3</span></a>
            <a href="#"><i class="fa fa-envelope"></i><span class="label label-success notification">7</span></a>
        </div>
    </nav><!-- sidebar-wrapper  -->
    <main class="page-content">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="page-header">
                        <h1>
                            <small>修改用户信息</small>
                        </h1>
                    </div>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/soso/updateUser/<%=cardNumber%>" method="post">
                用户名：<input type="text" name="userName" value="${cardUser.getUserName()}" required maxlength="10"/>
                密码： <input type="password" name="passWord" value="${cardUser.getPassWord()}" required minlength="6"
                           maxlength="20"/>
                <input type="submit" value="提交"/>
            </form>
        </div>
    </main><!-- page-content" -->
</div><!-- page-wrapper -->

