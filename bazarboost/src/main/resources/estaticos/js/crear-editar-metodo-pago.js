import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';

/**
 * Clase para gestionar el formulario de creación o edición de métodos de pago
 */
class CrearMetodoPago {
    constructor() {
        this.formulario = document.getElementById('metodo-pago-form');
        this.inicializarEventos();
    }

    /**
     * Inicializa los eventos del formulario
     */
    inicializarEventos() {
        this.formulario.addEventListener('submit', (event) => this.enviarFormulario(event));
    }

    /**
     * Envía el formulario al backend para crear o editar un método de pago
     * @param {Event} event Evento de envío del formulario
     */
    async enviarFormulario(event) {
        event.preventDefault(); // Prevenir el comportamiento por defecto

        // Recopilar los datos del formulario
        const metodoPagoCreacionDTO = this.obtenerDatosFormulario();

        // Validación básica de campos
        if (!metodoPagoCreacionDTO) {
            mostrarMensajeError('Por favor, completa todos los campos requeridos.');
            return;
        }

        try {
            // Enviar los datos al backend
            const response = await fetch('/api/metodos-pago', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(metodoPagoCreacionDTO)
            });

            if (!response.ok) {
                const errorData = await response.text(); // Obtener el mensaje de error como texto

                if (response.status === 400 && Array.isArray(JSON.parse(errorData))) {
                    // Mostrar lista de errores si el status es 400 y el errorData es un arreglo de mensajes de error
                    mostrarListaErrores(JSON.parse(errorData));
                } else if (response.status === 404) {
                    // Log para debugging y mostrar un mensaje amigable al usuario
                    console.error(`Error 404: ${errorData}`); // Mostrar el mensaje original en la consola
                    mostrarMensajeError("No se encontró la información del usuario. Por favor, verifica y vuelve a intentar.");
                } else if (response.status === 409) {
                    // Log para debugging y mostrar un mensaje amigable para duplicidad de número de tarjeta
                    console.error(`Error 409: ${errorData}`); // Mostrar el mensaje original en la consola
                    mostrarMensajeError("El número de tarjeta ya está registrado. Use un número diferente.");
                } else {
                    // Mostrar mensaje de error genérico para otros casos
                    mostrarMensajeError('Error al crear el método de pago');
                }
                return;
            }


            // Redirigir al usuario al endpoint /metodos-pago con el mensaje de éxito en la URL
            const mensajeExito = encodeURIComponent('Método de pago creado exitosamente.');
            window.location.href = `/metodos-pago?mensajeExito=${mensajeExito}`;

        } catch (error) {
            mostrarMensajeError(error.message || 'Error de conexión. Intenta nuevamente.');
        }
    }

    /**
     * Obtiene y valida los datos del formulario
     * @returns {Object|null} Datos del formulario o null si hay errores
     */
    obtenerDatosFormulario() {
        const nombreTitular = document.getElementById('nombreTitular').value.trim();
        const numeroTarjeta = document.getElementById('numeroTarjeta').value.trim();
        const fechaExpiracionInput = document.getElementById('fechaExpiracion').value;
        const tipoTarjeta = document.getElementById('tipoTarjeta').value;
        const monto = parseFloat(document.getElementById('montoDisponible').value);

        if (!nombreTitular || !numeroTarjeta || !fechaExpiracionInput || !tipoTarjeta || isNaN(monto)) {
            return null;
        }

        // Convertir la fecha de expiración al formato YYYY-MM-DD
        const fechaExpiracion = `${fechaExpiracionInput}-01`; // Agregar día 1

        return {
            nombreTitular,
            numeroTarjeta,
            fechaExpiracion,
            tipoTarjeta,
            monto
        };
    }
}

// Inicializar la clase cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new CrearMetodoPago();
});
