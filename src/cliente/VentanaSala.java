package cliente;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VentanaSala extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int ANCHO = 640;
	private static final int ALTO = 480;
	
	private JTextArea chatJTA = new JTextArea(10, 37);
	private JScrollPane chatJSP = new JScrollPane(chatJTA);
	private JPanel usuariosJP = new JPanel(new BorderLayout());
	private JList<String> usuariosJL = new JList<String>();
	private JRadioButton privadoJRB = new JRadioButton();
	private JPanel mensajeJP = new JPanel();
	private JTextField mensajeJTF = new JTextField();
	private JButton enviarJB = new JButton("Enviar");
	private JButton historialJB = new JButton("Historial");
	
	public VentanaSala(Cliente cliente, String nombre) {

		setTitle("Sala de chat: " + nombre);
		setSize(ANCHO, ALTO);
		setLayout(new BorderLayout());
		setResizable(false);
		setLocationRelativeTo(null);
		
		chatJTA.setEnabled(false);
		chatJTA.setFont(new Font("Verdana", Font.BOLD, 12));
		chatJTA.setLineWrap(true);
		chatJTA.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		usuariosJL.setEnabled(false);
		usuariosJL.setPreferredSize(new Dimension(180, ALTO-100));
		usuariosJL.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mensajeJTF.setPreferredSize(new Dimension(ANCHO-200, 30));
		
		privadoJRB.setPreferredSize(new Dimension(180, 30));
		privadoJRB.setText("Privado");
		
		privadoJRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(privadoJRB.isSelected()) {
					usuariosJL.setEnabled(true);
				}else {
					usuariosJL.setEnabled(false);
				}
			}
		});
		
		enviarJB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String mensaje = mensajeJTF.getText();
				
				mensaje = mensaje.replace('|', ' ');
				
				if(mensaje == null || mensaje.isBlank() || mensaje.isEmpty()) {
					return;
				}
				
				String fechaActual = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss").format(LocalDateTime.now());
				
				if(privadoJRB.isSelected()) {
					String usuario = usuariosJL.getSelectedValue();
					
					if(usuario == null || usuario.isBlank() || usuario.isEmpty() || 
						cliente.seleccioneAMiMismo(usuario)) {
							return;
					}
					
					cliente.enviarMensajePrivado(nombre, usuario, fechaActual,mensaje);
				}else {
					cliente.enviarMensajeSala(nombre, fechaActual, mensaje);
				}
			
				mensajeJTF.setText("");
			}
		});
		
		historialJB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					FileWriter archivo = new FileWriter(nombre+".txt");
					
					archivo.write(chatJTA.getText());
					
					JOptionPane.showMessageDialog(null, "Historial descargado correctamente.");
					
					archivo.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "No se pudo descargar el historial correctamente.");
				}
			}
		});
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				dispose();
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				cliente.cerrarConexionSala(nombre);
				cliente.cerrarSala(nombre);
				dispose();
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
		
		usuariosJP.add(usuariosJL, BorderLayout.NORTH);
		usuariosJP.add(privadoJRB, BorderLayout.SOUTH);
		
		mensajeJP.add(mensajeJTF);
		mensajeJP.add(enviarJB);
		mensajeJP.add(historialJB);
		
		add(chatJSP, BorderLayout.WEST);
		add(usuariosJP, BorderLayout.EAST);
		add(mensajeJP, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public void cargarUsuarios(String[] usuarios) {
		usuariosJL.setListData(usuarios);
	}
	
	public void agregarMensaje(String mensaje) {
		chatJTA.append(mensaje + "\n");
		chatJTA.setCaretPosition(chatJTA.getDocument().getLength());
	}
}
