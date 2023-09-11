<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="./styles/dashboard.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title>Solicitud Credito</title>
</head>
<body>

<div class="dashboard">
    <div class="d-flex">
        <nav class="navbar navbar-dark bg-primary navbar-vertical h-100vh justify-content-center">
            <span>CREDIT REQUEST  </span>
            <ul class="navbar-nav align-self-start">
                <li class="nav-item">
                    <a href="." class="nav-link">
                        <i class="fa fa-dashboard fa-fw">Registrar Solicitud Credito</i>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="./pages/register.jsp" class="nav-link">
                        <i class="fa fa-envelope fa-fw">Visualizar Solicitud Credito</i>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
    <div class="content">
        <jsp:include page="./pages/register.jsp" flush="true"/>
    </div>

</div>

</body>
</html>