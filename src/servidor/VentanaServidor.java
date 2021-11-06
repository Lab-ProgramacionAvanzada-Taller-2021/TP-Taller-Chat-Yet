package servidor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class VentanaServidor extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private static final int ANCHO = 480;
	private static final int ALTO = 360;
	
	private VentanaServidor ventana;
	private Servidor servidor;
	
	private JLabel tituloJL = new JLabel();
	private JLabel puertoJL = new JLabel();
	private JTextField puertoJTF = new JTextField();
	private JLabel estadoJL = new JLabel();
	private JLabel estadoUsuarios = new JLabel();
	private JLabel cantidadUsuarios = new JLabel();
	private JLabel estadoSalas = new JLabel();
	private JLabel cantidadSalas = new JLabel();
	private JButton conectarJB = new JButton();
	
	public VentanaServidor() {
		setTitle("Servidor Yet Another Chat Chat");
		setLayout(null);
	
		this.ventana = this;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(ANCHO, ALTO);
		setResizable(false);
	
		tituloJL.setFont(new Font("Verdana", Font.BOLD, 20));
		tituloJL.setText("Servidor Yet Another Chat Chat");
		tituloJL.setBounds(ANCHO/6, 20, 360, 20);
		
		puertoJL.setText("Puerto:");
		puertoJL.setBounds(ANCHO/3, 60, 60, 20);
	
		puertoJTF.setBounds(ANCHO/2, 60, 60, 20);
		
		estadoJL.setForeground(Color.RED);
		estadoJL.setText("¡Servidor Cerrado!");
		estadoJL.setBounds(ANCHO/3, 140, 140, 20);
		
		estadoUsuarios.setText("Usuarios conectados:");
		estadoUsuarios.setBounds(ANCHO/4, 180, 160, 20);
		
		cantidadUsuarios.setText("0");
		cantidadUsuarios.setBounds(ANCHO/2 + 60, 180, 20, 20);
		
		estadoSalas.setText("Salas creadas:");
		estadoSalas.setBounds(ANCHO/4, 220, 160, 20);
		
		cantidadSalas.setText("0");
		cantidadSalas.setBounds(ANCHO/2, 220, 20, 20);

		conectarJB.setText("Iniciar");
		conectarJB.setBounds(ANCHO/3, 100, 140, 20);
		conectarJB.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (servidor == null) {
						int puerto = Integer.parseInt(puertoJTF.getText());
						
						if(puerto < 1000 || puerto > 60000) {
							JOptionPane.showMessageDialog(null, "Utilice un puerto que este en un rango seguro de conflicto.");
							return;
						}
						
						servidor = new Servidor(puerto, ventana);
					}
					
					if(!servidor.estaAbierto()) {
						servidor.abrirServidor();
						estadoJL.setForeground(Color.GREEN);
						estadoJL.setText("¡Servidor Abierto!");
						puertoJTF.setEnabled(false);
						conectarJB.setText("Cerrar");
						puertoJTF.setBorder(BorderFactory.createLineBorder(Color.GRAY));
					}else{
						servidor.cerrarServidor();
						servidor = null;
						estadoJL.setForeground(Color.RED);
						estadoJL.setText("¡Servidor Cerrado!");
						puertoJTF.setEnabled(true);
						puertoJTF.setText("");
						conectarJB.setText("Iniciar");
					}
				} catch (NumberFormatException | IOException  e) {
					puertoJTF.setBorder(BorderFactory.createLineBorder(Color.RED));
				}
			}
		});
		
		add(tituloJL);
		add(puertoJL);
		add(puertoJTF);
		add(conectarJB);
		add(estadoJL);
		add(estadoUsuarios);
		add(cantidadUsuarios);
		add(estadoSalas);
		add(cantidadSalas);

		setVisible(true);
	}
	
	public void actualizarCantidadUsuarios(int cantidad) {
		cantidadUsuarios.setText(String.valueOf(cantidad));
	}
	
	public void actualizarCantidadSalas(int cantidad) {
		cantidadSalas.setText(String.valueOf(cantidad));
	}
}
