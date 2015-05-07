<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Sample Spring Interceptor</title>
<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen" title="default" />
<link rel="stylesheet" href="jquery-ui-1.9.2/css/smoothness/jquery-ui-1.9.2.custom.min.css" type="text/css" media="screen" title="default" />
<script src="jquery-ui-1.9.2/js/jquery-1.8.3.js" type="text/javascript"></script>
<script src="jquery-ui-1.9.2/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script>
</head>
<body id="login-bg"> 
 
<!-- Start: login-holder -->
<div id="login-holder">
	
	<div class="clear"></div>
	
	<!--  start loginbox ................................................................................. -->
	<div id="loginbox">
	
	<!--  start login-inner -->
	<div id="login-inner">
	<c:if test="${error}">
		<!--  start message-red -->
	 	<div class="clear"></div>
					<div id="message-red">
					<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="red-left">Wrong username or password !</td>
						<td class="red-right"><a class="close-red"><img src="images/table/icon_close_red.gif"   alt="" /></a></td>
					</tr>
					</table>
		</div>
		<!--  end message-red -->
	</c:if>
	
	<form:form action="login.do" method="post"  modelAttribute="loginAttribute">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<th>Username</th>
			<td><form:input type="text"  class="login-inp" path="username" /></td>
		</tr>
		<tr>
			<th>Password</th>
			<td><form:input type="password" value="************"  onfocus="this.value=''" class="login-inp" path="password"/></td>
		</tr>
		<tr>
			<th></th>
			<td><input type="submit" class="submit-login"  /></td>
		</tr>
		</table>
		</form:form>
	</div>
 	<!--  end login-inner -->
	<div class="clear"></div>
 </div>
 <!--  end loginbox -->

</div>
<!-- End: login-holder -->
</body>
</html>