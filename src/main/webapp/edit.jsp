<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Update</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>

<form name="editForm" action="edit" method="post">
    DateTime: <input required type="datetime-local" name="DateTime" value=${mealToEdit.getDateTime()} /> <br/>
    Description: <input required type="text" name="Description" value=${mealToEdit.getDescription()} /> <br/>
    Calories: <input required type="text" name="Calories" value=${mealToEdit.getCalories()} /> <br/>
    <input hidden type="text" name="Id" value=${mealToEdit.getId()} />
    <input type="submit" name="submit" value="Save"/>
</form>



</body>
</html>