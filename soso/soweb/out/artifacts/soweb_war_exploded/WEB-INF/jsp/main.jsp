<%--
  Created by IntelliJ IDEA.
  User: jy
  Date: 2019/12/24
  Time: 12:21 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <style>body {
        padding-top: 60px;
    }</style>

    <link href="${pageContext.request.contextPath}/statics/bootstrap3/css/bootstrap.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/statics/css/login-register.css" rel="stylesheet"/>
    <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-select/1.13.11/css/bootstrap-select.css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/statics/js/jquery-3.4.1.js"></script>
    <script src="${pageContext.request.contextPath}/statics/bootstrap3/js/bootstrap.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/statics/js/login-register.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.13.11/js/bootstrap-select.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-select/1.13.11/js/i18n/defaults-zh_CN.js"></script>
    <script>
        let flag = false;
        let result = '';

        function a1() {
            $.post({
                url: "${pageContext.request.contextPath}/login/a3",
                data: {'cardNumber': $("#cardNumber").val()},
                success: function (data) {
                    console.log(data.toString());
                    if (data.toString() === 'OK') {
                        $("#userInfo").css("color", "green");
                    } else {
                        $("#userInfo").css("color", "red");
                    }
                    $("#userInfo").html(data);
                }
            });
        }

        function a2() {
            $.post({
                url: "${pageContext.request.contextPath}/login/a3",
                data: {'cardNumber': $("#cardNumber").val(), 'password': $("#pwd").val()},
                success: function (data) {
                    console.log(data.toString());
                    if (data.toString() === 'OK') {
                        $("#pwdInfo").css("color", "green");
                        result = 'OK';
                    } else {
                        $("#pwdInfo").css("color", "red");
                        result = 'false';
                    }
                    $("#pwdInfo").html(data);
                }
            });
        }

        $.ajax({
            type: 'get',
            url: "/register/phoneNum",
            dataType: "json",
            success: function (data) {
                let str = "";
                for (let i = 0; i < data.length; i++) {
                    str += '<option value=' + data[i] + '>' + data[i] + '</option>';
                    console.log(data[i]);
                }
                $('#Number').append(str);
                $('.selectpicker').selectpicker('refresh');
                $('.selectpicker').selectpicker('render');
            }
        });

        function passCheck() {
            let data = $("#password").val();
            if (data.length < 6) {
                $("#passInfo").css("color", "red");
                $("#passInfo").html("密码需要6位以上！");
                flag = false;
            } else {
                $("#passInfo").css("color", "green");
                $("#passInfo").html("OK");
                flag = true;
            }
        }

        function RepassCheck() {
            console.log($('#password'));
            if ($('#password').val() !== $('#repassword').val()) {
                $('#repassInfo').css("color", "red");
                $('#repassInfo').html("两次密码输入需要一致！");
                flag = false;
            } else {
                $('#repassInfo').css("color", "green");
                $('#repassInfo').html("OK");
                flag = true;
            }
        }

        function checkMoney() {
            let Money = $("#Money").val();
            let Package = $("#package").val();
            $.post({
                url: "/register/money",
                data: {'Money': Money.toString(), 'Package': Package.toString()},
                success: function (data) {
                    if (data.toString() === 'OK') {
                        $('#monInfo').css("color", "green");
                        flag = true;
                    } else {
                        $('#monInfo').css("color", "red");
                        flag = false;
                    }
                    $('#monInfo').html(data);
                }
            });
        }

        function sub() {
            return flag;
        }

        function success() {
            let Money = $("#Money").val();
            let Package = $("#package").val();
            $.post({
                url: "${pageContext.request.contextPath}/register/commit",
                data: {
                    'cardNumber': $("#Number").val(),
                    'userName': $('#userName').val(),
                    'passWord': $("#password").val(),
                    'serPackage': Package.toString(),
                    'money': Money.toString()
                },
                success: function (data) {
                    if (data === 'success') {
                        alert("注册成功！");
                    } else {
                        alert("注册失败！");
                    }
                },
                error: function (data) {
                    console.log(data);
                }
            });
        }
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <a class="btn big-login" data-toggle="modal" href="javascript:void(0)" onclick="openLoginModal();">Log
                in</a>
            <a class="btn big-register" data-toggle="modal" href="javascript:void(0)" onclick="openRegisterModal();">Register</a>
        </div>
        <div class="col-sm-4"></div>
    </div>


    <div class="modal fade login" id="loginModal">
        <div class="modal-dialog login animated">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">登录</h4>
                </div>
                <div class="modal-body">
                    <div class="box">
                        <div class="content">
                            <div class="form loginBox">
                                <form method="post" action="${pageContext.request.contextPath}/soso/home"
                                      accept-charset="UTF-8">
                                    <p>
                                        <input id="cardNumber" class="form-control" type="text" placeholder="cardNumber"
                                               name="cardNumber" onblur="a1()" required>
                                        <span id="userInfo"></span>
                                    </p>
                                    <p>
                                        <input id="pwd" class="form-control" type="password" placeholder="Password"
                                               name="password" onblur="a2()" required>
                                        <span id="pwdInfo"></span>
                                    </p>
                                    <input class="btn btn-default btn-login" type="button" value="Login"
                                           onclick="loginAjax()">
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="box">
                        <div class="content registerBox" style="display:none;">
                            <div class="form">
                                <form method="post" html="{:multipart=>true}" data-remote="true" onsubmit="flag"
                                      action="${pageContext.request.contextPath}/soso/home" accept-charset="UTF-8">
                                    <p>
                                        卡号: <select id="Number" class="form-control selectpicker" title="请选择"
                                                    data-live-search="true"></select>
                                    </p>
                                    <p>
                                        用户名: <input id="userName" class="text" placeholder="userName" name="userName"
                                                    maxlength="10" required>
                                    </p>
                                    <p>
                                        密码: <input id="password" class="form-control" type="password"
                                                   placeholder="password" name="password" onchange="passCheck()"
                                                   maxlength="20" required>
                                        <span id="passInfo"></span>
                                    </p>
                                    <p>
                                        重复密码: <input id="repassword" class="form-control" type="password"
                                                     placeholder="repassword" name="repassword" onchange="RepassCheck()"
                                                     required>
                                        <span id="repassInfo"></span>
                                    </p>
                                    <p>
                                        套餐: <select id="package" class="form-control selectpicker" title="请选择"
                                                    data-live-search="true">
                                        <option></option>
                                        <option value="1">话痨套餐</option>
                                        <option value="2">网虫套餐</option>
                                        <option value="3">超人套餐</option>
                                    </select>
                                    </p>
                                    <p>
                                        充值金额: <input id="Money" class="form-control" type="number" placeholder="money"
                                                     name="money" onblur="checkMoney()" required>
                                        <span id="monInfo"></span>
                                    </p>
                                    <input class="btn btn-default btn-register" type="submit" onclick="success()"
                                           value="创建用户" name="commit">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="forgot login-footer">
                            <span>点击
                                 <a href="javascript: showRegisterForm();">注册账号</a>
                            </span>
                    </div>
                    <div class="forgot register-footer" style="display:none">
                        <span>已经有soso卡号？</span>
                        <a href="javascript: showLoginForm();">登录</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

