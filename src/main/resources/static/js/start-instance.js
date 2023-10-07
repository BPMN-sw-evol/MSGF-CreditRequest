function enviarFilaConEstadoDraft() {
    // Obtén todas las filas de la tabla
    var filas = document.querySelectorAll('#creditTable tbody tr');

    // Obtén los valores de los campos ocultos
    var coupleSavings = document.querySelector('input[name="coupleSavings"]').value;
    var applicantCouple = document.querySelector('input[name="applicantCoupleId"]').value;
    var marriageYears = document.querySelector('input[name="marriageYears"]').value;
    var bothEmployees = document.querySelector('input[name="bothEmployees"]').value;


    // Itera a través de las filas y busca la fila con estado "DRAFT"
    for (var i = 0; i < filas.length; i++) {
        var estado = filas[i].querySelector('td:last-child span').textContent.trim();
        if (estado === "DRAFT") {
            // Encuentra la fila con estado "DRAFT", obtén los datos y envíalos al controlador
            var procesoId = filas[i].querySelector('td:first-child span').textContent.trim();
            var housePrices = filas[i].querySelector('td:nth-child(2) span').textContent.trim();
            var quotaValue = filas[i].querySelector('td:nth-child(3) span').textContent.trim();
            var requestDate = filas[i].querySelector('td:nth-child(4) span').textContent.trim();
            var status = filas[i].querySelector('td:last-child span').textContent.trim();


            console.log("procesoId: " + procesoId);
            console.log("housePrices: " + housePrices);
            console.log("quotaValue: " + quotaValue);
            console.log("requestDate: " + requestDate);
            console.log("status: " + status);
            console.log("coupleSavings: "+ coupleSavings);
            console.log("applicantCouple: "+applicantCouple);
            console.log("marriageYears: "+marriageYears);
            console.log("bothEmployees: "+bothEmployees);


            // Crear un formulario oculto y agregar los datos a enviar
            var form = document.createElement('form');
            form.setAttribute('method', 'post');
            form.setAttribute('action', '/startProcess');

            // Después de obtener los valores
            document.getElementById('processId').value = procesoId;
            document.getElementById('housePrices').value = housePrices;
            document.getElementById('quotaValue').value = quotaValue;
            document.getElementById('requestDate').value = requestDate;
            document.getElementById('status').value = status;

            // Asigna los valores de los campos ocultos adicionales
            document.getElementById('coupleSavings').value = coupleSavings;
            document.getElementById('applicantCoupleId').value = applicantCouple;
            document.getElementById('marriageYears').value = marriageYears;
            document.getElementById('bothEmployees').value = bothEmployees;

            document.body.appendChild(form);

            // Enviar el formulario
            form.submit();

            break; // Detener la iteración después de enviar la fila
        }
    }

}