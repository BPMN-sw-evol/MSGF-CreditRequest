function validateMarriageYears(inputElement) {
    const Years = inputElement.value;
    if (!inputElement.value.trim()) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
        return false;
    } else {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
        return true;
    }
}

function validatebothEmployees(selectElement) {
    // Obtén el valor seleccionado
    const selectedValue = selectElement.value;

    // Verifica si se seleccionó una opción válida
    if (selectedValue === "") {
        selectElement.classList.remove("is-valid");
        selectElement.classList.add("is-invalid");
    } else {
        selectElement.classList.remove("is-invalid");
        selectElement.classList.add("is-valid");
    }
}

function validateHousePrices(inputElement) {
    const Price = inputElement.value;
    if (!inputElement.value.trim()) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
        return false;
    } else {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
        return true;
    }
}

function updateCoupleSavingsMax() {
    const housePricesInput = document.getElementById('housePrices');
    const coupleSavingsInput = document.getElementById('coupleSavings');

    // Obtener el valor actual de housePrices
    const housePricesValue = parseFloat(housePricesInput.value);

    // Establecer el valor máximo de coupleSavings
    coupleSavingsInput.max = housePricesValue * 0.10;

    // Establecer el valor del paso (step) como el 10% del máximo
    coupleSavingsInput.step = housePricesValue * 0.01; // Por ejemplo, 1% del máximo
}


function validateQuotaValue(inputElement) {
    const value = inputElement.value;
    if (!inputElement.value.trim()) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
        return false;
    } else {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
        return true;
    }
}

function validateCoupleSavings(inputElement) {
    const Savings = inputElement.value;
    if (!inputElement.value.trim()) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
        return false;
    } else {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
        return true;
    }
}

function validateApplicantCouple(inputElement) {
    const IdCouple = inputElement.value;
    // Aquí puedes realizar una validación personalizada según tus requisitos.
    // Si se cumple la validación, marca el campo como válido.
    // Si no se cumple, marca el campo como inválido y muestra un mensaje de error.
    if (!(/^[0-9]/).test(inputElement.value.trim())) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
        return false;
    } else {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
        return true;
    }
}

function validateFormCredit(form) {
    const validElementsCredit = form.querySelectorAll('.is-valid');
    const formElements = form.elements;

    // Verifica si todos los campos tienen la clase 'is-valid'
    if (validElementsCredit.length === formElements.length - 1) {
        Swal.fire({
            icon: 'success',
            title: 'success!',
            text: 'the couple has been successfully registered',
        });
        return true; // Envía el formulario si todos los campos son válidos
    } else {
        // Muestra una alerta si algún campo no es válido
        Swal.fire({
            icon: 'error',
            title: 'Error!',
            text: 'please enter correct information',
        });
        return false; // Evita el envío del formulario
    }
}