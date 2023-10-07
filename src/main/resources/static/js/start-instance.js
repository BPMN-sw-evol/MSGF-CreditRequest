$(document).ready(function () {
    // Escuchar el clic en el botón "Confirmar solicitud"
    $("#cualquiernombre").click(function () {
        // Inicializar un objeto de datos para almacenar la información de la fila
        var rowData = {};

        // Iterar a través de las filas de la tabla
        $("#creditTable tbody tr").each(function () {
            // Obtener el estado de la fila actual
            var estado = $(this).find("td:last").text(); // Suponiendo que el estado está en la última columna

            // Comprobar si el estado es "draft"
            if (estado === "DRAFT") {
                // Si es "draft", obtener la información de esa fila y guardarla en el objeto rowData
                rowData.creditCod = $(this).find("td:first").text();
                rowData.housePrice = $(this).find("td:eq(1)").text();
                rowData.quotaValue = $(this).find("td:eq(2)").text();
                rowData.requestDate = $(this).find("td:eq(3)").text();
            }
        });

        // Realizar una solicitud POST al endpoint /startProcess con los datos de la fila
        $.ajax({
            type: "POST",
            url: "http://localhost:9001/startProcess",
            data: JSON.stringify(rowData), // Convierte los datos a formato JSON
            contentType: "application/json",
            success: function (response) {
                // Manejar la respuesta del servidor si es necesario
                console.log("Solicitud enviada con éxito:", response);
            },
            error: function (err) {
                console.error("Error al enviar la solicitud:", err);
            }
        });
    });
});

