<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
    <meta charset="ISO-8859-1">
    <title>My app</title>
</head>

<body>
<p>Welcome! </p>
${applicationScope['key']}
<form action="/bye" >
<button>Go</button>
</form>
</body>

</html>