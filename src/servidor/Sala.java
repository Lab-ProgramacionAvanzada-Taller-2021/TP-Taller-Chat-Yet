package servidor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Sala {
	private HashMap<String, HiloServidor> usuarios;
	
	public Sala() {
		this.usuarios = new HashMap<String, HiloServidor>();
	}
	
	public boolean existeUsuario(String nombre) {
		return this.usuarios.containsKey(nombre);
	}
	
	public void agregarUsuario(String nombre, HiloServidor hilo) {
		this.usuarios.put(nombre, hilo);
	}
	
	public int cantidadUsuarios() {
		return this.usuarios.size();
	}
	
	public Set<String> obtenerUsuarios() {
		return this.usuarios.keySet();
	}
	
	public Collection<HiloServidor> obtenerHilosUsuarios() {
		return this.usuarios.values();
	}
	
	public void eliminarUsuario(String nombre) {
		this.usuarios.remove(nombre);
	}
	
	
	
}
