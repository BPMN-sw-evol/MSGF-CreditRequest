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

            // Marcar que se envió el formulario
            formularioEnviado = true;


            break; // Detener la iteración después de enviar la fila
        }
    }

    // Mostrar la alerta de éxito o error después de enviar el formulario
    if (formularioEnviado) {
        // Muestra la alerta de éxito
        Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'Success!',
            text: 'The application has been sent successfully',
            showConfirmButton: false,
            timer: 4500
        });
    } else {
        // Muestra la alerta de error
        Swal.fire({
            position: 'center',
            icon: 'error',
            title: 'Error!',
            text: 'Please try to send the application again',
            showConfirmButton: false,
            timer: 4500
        });
    }

}