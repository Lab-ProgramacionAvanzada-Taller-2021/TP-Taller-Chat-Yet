package cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class VentanaLooby extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private static final int ANCHO = 420;
	private static final int ALTO = 360;
	
	private JList<String> salasJL = new JList<String>();
	private JPanel botonesJP = new JPanel();
	private JButton crearSalaJB = new JButton("Crear sala");
	private JButton unirSalaJB = new JButton("Unirse sala");
	
	public VentanaLooby(Cliente cliente) {
		setTitle("Looby Yet Another Chat Chat");
		setSize(ANCHO, ALTO);
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		crearSalaJB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la sala");
				
				if(nombre == null || nombre.isBlank() || nombre.isEmpty()) {
					return;
				}
				
				cliente.crearSala(nombre);
			}
		});
		
		unirSalaJB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cliente.obtenerCantidadSalas() > 2) {
					JOptionPane.showMessageDialog(null, "No puedes unirte a la sala.");
					return;
				}
				
				String nombre = salasJL.getSelectedValue();
				
				if(nombre == null || nombre.isBlank() || nombre.isEmpty()) {
					return;
				}
				
				int pos = nombre.indexOf('-');
				nombre = nombre.substring(0, pos-1);
				
				cliente.unirseSala(nombre);
			}
			
		});
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				if(!cliente.tieneSalas()) {
					cliente.cerrarConexion();
					dispose();
				}else {
					JOptionPane.showMessageDialog(null, "Debe salir de las salas antes de cerrar el Looby");
				}
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		
		botonesJP.add(crearSalaJB);
		botonesJP.add(unirSalaJB);
		
		add(salasJL, BorderLayout.CENTER);
		add(botonesJP, BorderLayout.SOUTH);;
		
		setVisible(true);
	}
	
	public void cargarSalas(String[] salas) {
		salasJL.setListData(salas);
	}
	
	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}
}
