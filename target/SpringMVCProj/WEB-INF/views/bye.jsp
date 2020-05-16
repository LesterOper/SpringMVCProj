<jsp:useBean id="key" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">

<head>
    <meta charset="ISO-8859-1">
    <title>My app</title>
</head>

<body>
<p> ${requestScope.get("key")}</p>
<p>Goodbye!</p>
</body>

</html>