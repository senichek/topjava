<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="mealServlet" class="ru.javawebinar.topjava.web.MealServlet"/>

<html lang="ru">
<head>
    <style>
        .green {
            color: green;
        }

        .red {
            color: red;
        }
    </style>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<li><a href="create">Create new meal</a></li>
<br>
<table border="1" cellpadding="5">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="mealVar" items="${listOfMeal}">
        <c:choose>
            <c:when test="${mealVar.excess == false}">
                <tr>
                    <td class="green"> ${mealVar.getFormattedDate()} </td>
                    <td class="green"> ${mealVar.description} </td>
                    <td class="green"> ${mealVar.calories} </td>
                    <td><a href="/topjava/edit?id=<c:out value="${mealVar.getId()}"/>">Update</a></td>
                    <td><a href="/topjava/delete?id=<c:out value="${mealVar.getId()}"/>">Delete</a></td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <td class="red"> ${mealVar.getFormattedDate()} </td>
                    <td class="red"> ${mealVar.description} </td>
                    <td class="red"> ${mealVar.calories} </td>
                    <td><a href="/topjava/edit?id=<c:out value="${mealVar.getId()}"/>">Update</a></td>
                    <td><a href="/topjava/delete?id=<c:out value="${mealVar.getId()}"/>">Delete</a></td>
                </tr>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</table>
</body>
</html>