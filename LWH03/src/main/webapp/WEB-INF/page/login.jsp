<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>登录页</title>
<link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
	<style>
		.col-center-block {
			float: none;
			display: block;
			margin-left: auto;
			margin-right: auto;
		}
		.logo{
			position: absolute;
			/*background-position: 0 -96px;*/
			width: 25px;
			height: 25px;
			font-size: 0;
			background-image: url(${pageContext.request.contextPath}/static/logo.jpg);
		}
	</style>
</head>


<body>

<div class="container">
	<div class="row myCenter">
		<div class="col-xs-6 col-md-4 col-center-block">
			<form id="form1" class="form-signin" method="post" action="${pageContext.request.contextPath}/user/login">
				<h2 class="form-signin-heading ">请登录${param.msg}</h2>
				<input type="hidden" name="location" id="location">
				<label for="username" class="sr-only">用户名</label>
				<input type="text" name="username" id="username" class="form-control" placeholder="用户名" required autofocus>
				<label for="inputPassword" class="sr-only">密码</label>
				<input type="password" id="inputPassword" name="password" class="form-control" placeholder="密码" required>
				<div class="checkbox">
					<label>
						<input type="checkbox" value="remember-me">
						记住我 </label>
					<a class="pull-right" href="${pageContext.request.contextPath}/user/register" id="register">注册</a>
				</div>
				<button class="btn btn-lg btn-primary btn-block" type="submit" id="submitForm">登录</button>
			</form>
		</div>
	</div>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/jquery-2.1.4.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/jquery.form.min.js"></script>



</html>