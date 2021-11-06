package cliente;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class HiloCliente extends Thread{

	private Cliente cliente;
	private Socket socket;

	public HiloCliente(Cliente cliente, Socket socket) {
		this.cliente = cliente;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			String mensaje = new DataInputStream(socket.getInputStream()).readUTF();
			
			while(!mensaje.equals("SaliR")) {
				procesarMensaje(mensaje);
				mensaje = new DataInputStream(socket.getInputStream()).readUTF();
			}
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void procesarMensaje(String mensaje) {
		String[] cadena = mensaje.split("\\|");
		
		switch(cadena[0]) {
			case "LogiNOK":
				cliente.cargarNombre(cadena[1]);
				cliente.cerrarVentana();
				break;
				
			case "LogiNErroR":
				cliente.mostrarMensajeError("Nombre de usuario existente.");
				break;
				
			case "CrearSalAOK":
				break;
				
			case "CrearSalAErroR":
				cliente.mostrarMensajeError("Nombre de la sala existente.");
				break;
				
			case "CargarSalaS":
				if(cadena.length > 1)
					cliente.cargarSalas(Arrays.copyOfRange(cadena, 1, cadena.length));
				break;
				
			case "UnirseSalaOK":
				cliente.abrirSala(cadena[1]);
				break;
				
			case "UnirseSalaErroR":
				cliente.mostrarMensajeError("Error al querer unir a la sala.");
				break;
				
			case "MensajeP":
				// Sala - nombrePropio - nombreUsuario - Fecha - Mensaje
				cliente.agregarMensajePrivado(cadena[1], cadena[2], cadena[3], cadena[4], cadena[5]);
				break;
				
			case "MensajeS":
				// Sala - nombrePropio - Fecha - Mensaje
				cliente.agregarMensajeSala(cadena[1], cadena[2], cadena[3], cadena[4]);
				break;
				
			case "MensajePErroR":
				cliente.mostrarMensajeError("Error al querer agregar un mensaje privado.");
				break;
				
			case "MensajeSErroR":
				cliente.mostrarMensajeError("Error al querer agregar un mensaje a la sala.");
				break;
			
				
			case "CargarUsuarios":
				cliente.cargarUsuariosSala(cadena[1], Arrays.copyOfRange(cadena, 2, cadena.length));
				break;
		}
	}
}
