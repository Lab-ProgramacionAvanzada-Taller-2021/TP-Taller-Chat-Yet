package cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Cliente {
	private Socket socket;
	private String nombre;
	private VentanaCliente ventana;
	private VentanaLooby ventanaLobby;
	private HashMap<String, VentanaSala> salas = new HashMap<String, VentanaSala>();
	
	public Cliente(VentanaCliente ventana, String ip, int host) {
		try {
			socket = new Socket(ip, host);
		
			this.ventana = ventana;
			
			new HiloCliente(this, socket).start();
	
		}catch(Exception e) {
			ventana.mostrarMensaje("Error al querer crear la conexion.");
		}
	}
	
	public void cargarNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void conectar(String nombre) {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF("LogiN|"+nombre);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void crearSala(String nombre) {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF("CrearSalA|"+nombre);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unirseSala(String nombre) {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF("UnirSalA|"+nombre);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensajePrivado(String nombreSala, String nombreUsuario, String fecha, String mensaje) {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF(
					"MensajeP|"+
					nombreSala+"|"+
					this.nombre+"|"+
					nombreUsuario+"|"+
					fecha+"|"+
					mensaje);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensajeSala(String nombreSala, String fecha, String mensaje) {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF(
					"MensajeS|"+
					nombreSala+"|"+
					this.nombre+"|"+
					fecha+"|"+
					mensaje);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarConexion() {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF("DesconectaR");
			new DataOutputStream(socket.getOutputStream()).writeUTF("SaliR");

			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarConexionSala(String nombreSala) {
		try {
			new DataOutputStream(socket.getOutputStream()).writeUTF("DesconectarSalA|"+nombreSala);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarMensajeError(String mensaje) {
		if(ventana != null)
			ventana.mostrarMensaje(mensaje);
		else
			ventanaLobby.mostrarMensaje(mensaje);
	}
	
	public void cargarSalas(String[] salas) {
		ventanaLobby.cargarSalas(salas);
	}
	
	public void abrirSala(String nombreSala) {
		this.salas.put(nombreSala, new VentanaSala(this, nombreSala));
		this.salas.get(nombreSala).setVisible(true);
	}
	
	public void cerrarSala(String nombreSala) {
		this.salas.remove(nombreSala);
	}
	
	public int obtenerCantidadSalas() {
		return salas.size();
	}
	
	public void cargarUsuariosSala(String nombreSala, String[] usuarios) {
		if(this.salas.containsKey(nombreSala)) {
			this.salas.get(nombreSala).cargarUsuarios(usuarios);
		}else {
			ventanaLobby.mostrarMensaje("Error al querer cargar los usuarios de la sala " + nombreSala + ".");
		}
	}
	
	public void agregarMensajePrivado(String nombreSala, String usuarioPropio, String usuarioPrivado, String fecha, String mensaje) {
		if(this.salas.containsKey(nombreSala)) {
			this.salas.get(nombreSala).agregarMensaje(
					usuarioPropio + " [Para " + usuarioPrivado + "] {" +fecha+ "} > " + mensaje
				);
		}else {
			ventanaLobby.mostrarMensaje("Error al querer agregar un mensaje privado.");
		}
	}
	
	public void agregarMensajeSala(String nombreSala, String usuario, String fecha, String mensaje) {
		if(this.salas.containsKey(nombreSala)) {
			this.salas.get(nombreSala).agregarMensaje(
					usuario + " {"+ fecha + "} > " + mensaje
				);
		}else {
			ventanaLobby.mostrarMensaje("Error al querer cargar un mensaje a la sala.");
		}
	}
		
	public boolean seleccioneAMiMismo(String nombre) {
		return this.nombre.equals(nombre);
	}
	
	public boolean tieneSalas() {
		return (this.salas.size() > 0) ? true : false;
	}
	
	public void cerrarVentana() {
		this.ventanaLobby = new VentanaLooby(this);
		this.ventanaLobby.setVisible(true);
		ventana.dispose();
		ventana = null;
	}
}
