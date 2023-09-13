<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="./styles/dashboard.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title>Solicitud Credito</title>
</head>
<body>
<div class="col">
    <div class="dashboard">
        <div class="d-flex ms-3 rounded-3">
            <nav class="navbar navbar-dark bg-primary navbar-vertical h-100vh justify-content-center rounded-3">
                <h2>CREDIT REQUEST  </h2>
                <ul class="navbar-nav align-self-start">
                    <li class="nav-item">
                        <a href="register.jsp" class="nav-link">
                            <h5 class="fa fa-dashboard fa-fw">Registrar Solicitud Credito</h5>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="view.jsp" class="nav-link">
                            <h5 class="fa fa-envelope fa-fw">Visualizar Solicitud Credito</h5>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
        <div class="content pt-3 pb-3" id="contentContainer">
            <jsp:include page="./pages/register.jsp" flush="true"/>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="./scripts/routing.js"></script> <!-- Incluye tu archivo JavaScript aquí -->
</body>
</html>