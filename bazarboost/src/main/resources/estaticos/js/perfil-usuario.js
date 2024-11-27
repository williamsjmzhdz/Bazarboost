import { mostrarMensajeExito, mostrarListaErrores, mostrarMensajeError, mostrarMensajeErrorDesaparece } from './mensajes-estado.js';

export class PerfilUsuario {
 constructor() {
   this.form = document.getElementById('formPerfilUsuario');
   this.form.dataset.usuarioId = '';
   this.campos = this.obtenerCamposFormulario();
   this.camposContrasenia = {
     contrasenia: document.getElementById('contrasenia'),
     confirmarContrasenia: document.getElementById('confirmarContrasenia')
   };
   this.inicializar();
 }

 obtenerCamposFormulario() {
   return {
     nombre: document.querySelector('[data-nombre]'),
     apellidoPaterno: document.querySelector('[data-apellido-paterno]'),
     apellidoMaterno: document.querySelector('[data-apellido-materno]'),
     telefono: document.querySelector('[data-telefono]'),
     correoElectronico: document.querySelector('[data-correo-electronico]')
   };
 }

 inicializar() {
   this.cargarDatosUsuario();
   setTimeout(() => this.limpiarCamposContrasenia(), 500);
   this.form.addEventListener('submit', (e) => this.manejarEnvioFormulario(e));
 }

 limpiarCamposContrasenia() {
   Object.values(this.camposContrasenia).forEach(campo => {
     campo.value = '';
     campo.removeAttribute('required');
     campo.setAttribute('autocomplete', 'new-password');
   });
 }

 async cargarDatosUsuario() {
   try {
     const response = await fetch('/api/usuarios/perfil');
     if (!response.ok) {
       if (response.status === 404) {
         const mensaje = await response.text();
         window.location.href = `/productos?mensajeError=${encodeURIComponent(mensaje)}`;
         return;
       }
       throw new Error('Error al cargar datos');
     }
     const datos = await response.json();
     this.form.dataset.usuarioId = datos.usuarioId;
     this.rellenarFormulario(datos);
   } catch (error) {
     console.error('Error:', error);
     const mensajeError = "Error al cargar la información del perfil";
     window.location.href = `/productos?mensajeError=${encodeURIComponent(mensajeError)}`;
   }
 }

 rellenarFormulario(datos) {
   Object.keys(this.campos).forEach(campo => {
     if (datos[campo]) {
       this.campos[campo].value = datos[campo];
     }
   });
 }

 async manejarEnvioFormulario(e) {
   e.preventDefault();

   const datosActualizacion = {
       nombre: this.campos.nombre.value,
       apellidoPaterno: this.campos.apellidoPaterno.value,
       apellidoMaterno: this.campos.apellidoMaterno.value,
       telefono: this.campos.telefono.value,
       correoElectronico: this.campos.correoElectronico.value
   };

   if (this.camposContrasenia.contrasenia.value) {
       datosActualizacion.contrasenia = this.camposContrasenia.contrasenia.value;
       datosActualizacion.confirmacionContrasenia = this.camposContrasenia.confirmarContrasenia.value;
   }

   try {
       const response = await fetch('/api/usuarios/actualizar', {
           method: 'PUT',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(datosActualizacion)
       });

       const mensaje = await response.text();

       // Limpiar mensajes previos
       document.querySelectorAll('.alert').forEach(alert => {
           alert.classList.add('d-none');
       });

       if (response.ok) {
           window.scrollTo({ top: 0, behavior: 'smooth' });
           mostrarMensajeExito(mensaje);
           return;
       }

       if (response.status === 400) {
           try {
               const errores = JSON.parse(mensaje);
               if (Array.isArray(errores)) {
                   mostrarListaErrores(errores);
               } else {
                   mostrarMensajeError(mensaje);
               }
           } catch {
               mostrarMensajeError(mensaje);
           }
           return;
       }

       if (response.status === 404 || response.status === 409) {
           mostrarMensajeErrorDesaparece(mensaje);
       }

   } catch (error) {
       console.error('Error:', error);
       mostrarMensajeError('Error al actualizar el perfil. Por favor, intente nuevamente.');
   }
 }
}

document.addEventListener('DOMContentLoaded', () => {
 new PerfilUsuario();
});