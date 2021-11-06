package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Servidor extends Thread{
	private ServerSocket servidor;
	private Servidor instancia;
	private HashMap<String, HiloServidor> usuarios;
	private HashMap<String, Sala> salas;
	private boolean abierto = false;
	private VentanaServidor ventana;
	
	public Servidor(int puerto, VentanaServidor ventana) throws IOException {
		servidor = new ServerSocket(puerto);
		usuarios = new HashMap<String, HiloServidor>();
		salas = new HashMap<String, Sala>();
		this.ventana = ventana;
		this.instancia = this;
	}

	@Override
	public void run() {
		abierto = true;
		
		while(abierto) {
			try {
				Socket usuario = servidor.accept();
				
				new HiloServidor(instancia, usuario).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean estaAbierto() {
		return abierto;
	}

	public void abrirServidor() {
		this.start();
	}
	
	public void cerrarServidor() {
		abierto = false;
		servidor = null;
		usuarios = null;
		salas = null;
		actualizarCantidadUsuarios();
		actualizarCantidadSalas();
	}
	
	public boolean existeUsuario(String nombre) {
		return this.usuarios.containsKey(nombre);
	}
	
	public boolean existeUsuarioEnSala(String nombreSala, String nombreUsuario) {
		if( this.salas.containsKey(nombreSala))
			return this.salas.get(nombreSala).existeUsuario(nombreUsuario);
		
		return false;
	}
	
	public void agregarUsuario(String nombre, HiloServidor hilo) {
		this.usuarios.put(nombre, hilo);
	}
	
	public void agregarUsuarioSala(String nombreSala, String nombreUsuario, HiloServidor hilo) {
		this.salas.get(nombreSala).agregarUsuario(nombreUsuario, hilo);
	}
	
	public void enviarSalas() throws IOException {
		String mensaje = "CargarSalaS|";
		
		for(String sala : this.salas.keySet()) {
			mensaje+=
					sala+ 
					" - Usuarios: " + 
					this.salas.get(sala).cantidadUsuarios()+
					"|";
		}
		
		enviarMensajeATodos(mensaje);
	}
	
	public void enviarUsuariosSala(String nombreSala) {
		String mensaje = "CargarUsuarios|"+nombreSala+"|";
		
		for(String nombre : obtenerUsuariosSala(nombreSala)) {
			mensaje+=
					nombre+
					"|";
		}
		
		enviarMensajeASala(nombreSala, mensaje);
	}
	
	public Set<String> obtenerUsuariosSala(String nombre){
		return this.salas.get(nombre).obtenerUsuarios();
	}
	
	public Collection<HiloServidor> obtenerHilosUsuarios(){
		return this.usuarios.values();
	}
	
	public Collection<HiloServidor> obtenerHilosUsuariosSala(String nombre){
		return this.salas.get(nombre).obtenerHilosUsuarios();
	}
	
	public void agregarSala(String nombre) {
		this.salas.put(nombre, new Sala());
	}
	
	public boolean existeSala(String nombre) {
		return this.salas.containsKey(nombre);
	}
	
	public void eliminarUsuario(String nombre) {
		this.usuarios.remove(nombre);
	}
	
	public void eliminarUsuarioSala(String nombreSala, String nombre) {
		this.salas.get(nombreSala).eliminarUsuario(nombre);
	}
	
	public void actualizarCantidadUsuarios() {
		ventana.actualizarCantidadUsuarios((usuarios != null) ? usuarios.size() : 0);
	}
	
	public void actualizarCantidadSalas() {
		ventana.actualizarCantidadSalas((salas != null) ? salas.size() : 0);
	}
	
	public void enviarMensajeAPrivado(String nombreUsuario, String mensaje) {
		this.usuarios.get(nombreUsuario).enviarMensaje(mensaje);
	}
	
	public void enviarMensajeASala(String nombreSala, String mensaje) {
		for(HiloServidor hilo : obtenerHilosUsuariosSala(nombreSala)) {
			hilo.enviarMensaje(mensaje);
		}
	}
	
	public void enviarMensajeATodos(String mensaje) {
		for(HiloServidor hilo : obtenerHilosUsuarios()) {
			hilo.enviarMensaje(mensaje);
		}
	}
}
