<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>New WorkOrder</title>
    </head>
    <body>
        
        <h2>New WorkOrder:</h2>

<form action="create.jsp" method=POST>
<table width="25%" border="0" bordercolordark="#000000" bordercolorlight="#FFFFFF" cellpadding="3" cellspacing="0">
<tr>
<td>Title:</td>
<td><input type=text name=title value="" size=10></td>
</tr>

<tr>
<td>Details:</td>
<td><input type=text name=details value="" size=10></td>
</tr>
</table>

<input type=submit name=send value=Send>
</form>

</body>
</html>