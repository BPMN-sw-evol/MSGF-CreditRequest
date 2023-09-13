<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-10">
    <div class="card-group">
        <div class="card-title mt-4">
            <h1 class="text-center">¡Registro de Solicitud de Credito!</h1>
        </div>
        <div class="card-body">
            <div class="card">
                <div class="card-header card-title m-4">
                    <h1 class="text-center">¡Informacion Personal!</h1>
                </div>
                    <div class="row">
                        <div class="col-sm-6 card-body">
                            <div class="card-title">
                                <h3 class="text-center">Pareja 1</h3>
                            </div>
                            <form action="index.jsp" method="post">
                                    <div class="form-floating mb-1 p-1">
                                        <input type="text" class="form-control" id="floatingFullName1" >
                                        <label for="floatingFullName1">Nombre completo</label>
                                    </div>
                                    <div class="form-floating mb-1 p-1">
                                        <input type="number" class="form-control" id="floatingPhone1">
                                        <label for="floatingPhone1">Numero de telefono</label>
                                    </div>
                                    <div class="form-floating mb-1 p-1">
                                        <input type="email" class="form-control" id="floatingEmail1">
                                        <label for="floatingPhone1">Correo electronico</label>
                                    </div>
                                    <div class="form-floating mb-1 p-1">
                                        <input type="date" class="form-control" id="floatingBirthdate1">
                                        <label for="floatingBirthdate1">Año de nacimiento</label>
                                    </div>
                                    <div class="d-flex justify-content-center mb-1 p-1">
                                        <input class="form-check-input mt-0" type="checkbox" id="masculino" value="masculino" aria-label="Checkbox for following text input">
                                        <label for="masculino">Masculino</label>
                                        <input class="form-check-input mt-0" type="checkbox" id="femenino" aria-label="Checkbox for following text input">
                                        <label for="femenino">Femenino</label>
                                    </div>
                            </form>
                        </div>

                        <div class="col-sm-6 card-body">
                            <div class="card-title">
                                <h3 class="text-center">Pareja 2</h3>
                            </div>
                            <form action="index.jsp" method="post">
                                <div class="form-floating mb-1 p-1">
                                    <input type="text" class="form-control" id="floatingFullName2" >
                                    <label for="floatingFullName2">Nombre completo</label>
                                </div>
                                <div class="form-floating mb-1 p-1">
                                    <input type="number" class="form-control" id="floatingPhone2">
                                    <label for="floatingPhone2">Numero de telefono</label>
                                </div>
                                <div class="form-floating mb-1 p-1">
                                    <input type="email" class="form-control" id="floatingEmail2">
                                    <label for="floatingPhone2">Correo electronico</label>
                                </div>
                                <div class="form-floating mb-1 p-1">
                                    <input type="date" class="form-control" id="floatingBirthdate2">
                                    <label for="floatingBirthdate2">Año de nacimiento</label>
                                </div>
                                <div class="d-flex justify-content-center mb-1 p-1">
                                    <input class="form-check-input mt-0" type="checkbox" id="masculino" value="masculino" aria-label="Checkbox for following text input">
                                    <label for="masculino">Masculino</label>
                                    <input class="form-check-input mt-0" type="checkbox" id="femenino" aria-label="Checkbox for following text input">
                                    <label for="femenino">Femenino</label>
                                </div>
                            </form>
                        </div>
                    </div>
            </div>


            <div class="card">
                <div class="card-header card-title m-4">
                    <h1 class="text-center">¡Informacion Financiera!</h1>
                </div>
                <div class="card-body">
                    <form action="index.jsp" method="post">
                        <div class="row">
                            <div class="form-floating mb-3 p-1">
                                <input type="text" class="form-control" id="floatingMr" >
                                <label for="floatingMr">Nombre del señor</label>
                            </div>
                            <div class="form-floating mb-3 p-1">
                                <input type="text" class="form-control" id="floatingMs">
                                <label for="floatingMs">Nombre de la señora</label>
                            </div>
                            <button class="btn btn-primary" type="submit">submit</button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
</div>
