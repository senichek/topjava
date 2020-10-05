<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Update</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form name="createForm" action="create" method="post">
    DateTime: <input required type="datetime-local" name="DateTime" > <br/>
    Description: <input required type="text" name="Description" > <br/>
    Calories: <input required type="text" name="Calories" > <br/>
    <input type="submit" name="submit" value="Create"/>
</form>
</body>
</html>