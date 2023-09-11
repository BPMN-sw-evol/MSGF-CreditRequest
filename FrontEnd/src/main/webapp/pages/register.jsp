<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-md-10">
    <div class="card">
        <div class="card-title m-4">
            <h1 class="text-center">¡Ingresar los nombres completos de los solicitantes!</h1>
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
