function enviarFilaConEstadoDraft() {
    // Obtén todas las filas de la tabla
    var filas = document.querySelectorAll('#creditTable tbody tr');

    // Itera a través de las filas y busca la fila con estado "DRAFT"
    for (var i = 0; i < filas.length; i++) {
        var estado = filas[i].querySelector('td:last-child span').textContent.trim();
        if (estado === "DRAFT") {
            // Encuentra la fila con estado "DRAFT", obtén los datos y envíalos al controlador
            var taskId = filas[i].querySelector('td:first-child span').textContent.trim();


            console.log("procesoId: " + taskId);

            // Crear un formulario oculto y agregar los datos a enviar
            var form = document.createElement('form');
            form.setAttribute('method', 'get');
            form.setAttribute('action', '/complete');

            // Después de obtener los valores
            document.getElementById('taskId').value = taskId;

            document.body.appendChild(form);

            // Enviar el formulario
            form.submit();

            break; // Detener la iteración después de enviar la fila
        }
    }

}