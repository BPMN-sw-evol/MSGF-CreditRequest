$(document).ready(function () {
    // Captura el clic en el enlace de "Registrar Solicitud Credito"
    $("a.nav-link[href='register.jsp']").click(function (e) {
        e.preventDefault(); // Evita la navegación normal

        // Realiza una petición AJAX para cargar la vista register.jsp
        $.ajax({
            url: './pages/register.jsp',
            type: 'GET',
            success: function (data) {
                // Carga el contenido en el div con identificador contentContainer
                $('#contentContainer').html(data);
            }
        });
    });

    // Captura el clic en el enlace de "Visualizar Solicitud Credito"
    $("a.nav-link[href='view.jsp']").click(function (e) {
        e.preventDefault(); // Evita la navegación normal

        // Realiza una petición AJAX para cargar la vista view.jsp
        $.ajax({
            url: './pages/viewCredit.jsp',
            type: 'GET',
            success: function (data) {
                // Carga el contenido en el div con identificador contentContainer
                $('#contentContainer').html(data);
            }
        });
    });
});
