// Función para validar el DNI
function validateDNI(inputElement) {

    if (/^\d{1,16}$/.test(inputElement.value)) {
        // La expresión regular /\d{1,16}/ verifica que haya de 1 a 16 dígitos.
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
    } else {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
    }
}

function validateFullName(inputElement) {

    // Utiliza una expresión regular para verificar si el valor no contiene números
    if (/^[^0-9]+$/.test(inputElement.value.trim())) {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
    } else {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
    }
}

function validateEmail(inputElement) {
    // Expresión regular para validar el formato del correo electrónico
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

    // Dominios permitidos
    const allowedDomains = ["gmail.com", "hotmail.com", "unillanos.edu.co"];

    // Obtén el valor del campo de correo electrónico
    const email = inputElement.value.trim();

    // Verifica si el correo electrónico tiene el formato correcto
    if (!emailPattern.test(email)) {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");

        // Elimina el atributo 'required' si no se cumple la validación
        inputElement.removeAttribute("required");

        return;
    }

    // Verifica si el correo electrónico contiene uno de los dominios permitidos
    const domain = email.split("@")[1];
    if (allowedDomains.includes(domain)) {
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");

        // Restablece el atributo 'required' si se cumple la validación
        inputElement.setAttribute("required", "required");
    } else {
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");

        // Elimina el atributo 'required' si no se cumple la validación
        inputElement.removeAttribute("required");
    }
}


function validateGender(selectElement) {
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

function validateBirthDate(inputElement) {
    const inputDate = new Date(inputElement.value);
    const currentDate = new Date();

    // Define la edad mínima permitida (por ejemplo, 18 años)
    const minAge = 18;

    // Calcula la fecha hace 18 años a partir de la fecha actual
    const minAgeDate = new Date(currentDate.getFullYear() - minAge, currentDate.getMonth(), currentDate.getDate());

    if (inputDate < minAgeDate) {
        inputElement.setCustomValidity("");
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
    } else {
        inputElement.setCustomValidity("You must be at least 18 years old.");
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
    }
}

function validatePhoneNumber(inputElement) {
    // Obtén el valor del campo de teléfono
    const phoneNumber = inputElement.value.trim();

    // Define una expresión regular para un formato de número de teléfono deseado
    const phonePattern = /^[0-9]{10}$/;

    // Realiza la validación
    if (!phonePattern.test(phoneNumber)) {
        inputElement.setCustomValidity("Please enter a valid 10-digit phone number.");
        inputElement.classList.remove("is-valid");
        inputElement.classList.add("is-invalid");
    } else {
        inputElement.setCustomValidity("");
        inputElement.classList.remove("is-invalid");
        inputElement.classList.add("is-valid");
    }
}

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

function validateFormField(form) {
    const validElementsCouple = form.querySelectorAll('.is-valid');
    const formElements = form.elements;
    // Verifica si todos los campos tienen la clase 'is-valid'
    if (validElementsCouple.length === formElements.length - 1) {
        Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'success!',
            text: 'the couple has been successfully registered',
            showConfirmButton: false,
            timer: 1500
        })
        return true; // Envía el formulario si todos los campos son válidos
    } else {
        // Muestra una alerta si algún campo no es válido
        Swal.fire({
            position: 'center',
            icon: 'error',
            title: 'Error!',
            text: 'please enter correct information',
            showConfirmButton: false,
            timer: 1500
        })
        return false; // Evita el envío del formulario
    }
}
