package cliente;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaCliente extends JFrame{
	private static final long serialVersionUID = 1L;

	private static final int ANCHO = 420;
	private static final int ALTO = 360;
	
	private Cliente cliente;
	private VentanaCliente instancia;
	
	private JLabel tituloJL = new JLabel("Cliente Yet Another Chat Chat");
	private JPanel formularioJP = new JPanel();
	private JLabel puertoJL = new JLabel("Puerto:");
	private JTextField puertoJTF = new JTextField(); 
	private JLabel ipJL = new JLabel("IP:");
	private JTextField ipJTF = new JTextField("127.0.0.1"); 
	private JLabel nombreJL = new JLabel("Nombre:");
	private JTextField nombreJTF = new JTextField(); 
	private JButton conectarJB = new JButton("Conectar");
	
	public VentanaCliente() {
		setTitle("Cliente Yet Another Chat Chat");
		setSize(ANCHO, ALTO);
		setLayout(new BorderLayout(100, 20));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		this.instancia = this;
		
		tituloJL.setFont(new Font("Verdana", Font.BOLD, 22));
		
		formularioJP.setLayout(null);
		
		puertoJL.setBounds(ANCHO/4, 20, 100, 20);
	
		puertoJTF.setBounds(ANCHO/2, 20, 100, 20);
		
		ipJL.setBounds(ANCHO/3, 60, 100, 20);
	
		ipJTF.setBounds(ANCHO/2, 60, 100, 20);
		
		nombreJL.setBounds(ANCHO/4, 100, 100, 20);
		
		nombreJTF.setBounds(ANCHO/2, 100, 100, 20);
		
		
		conectarJB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ipJTF.getText().isBlank() || ipJTF.getText().isEmpty() ||
					puertoJTF.getText().isBlank() || puertoJTF.getText().isEmpty() ||
					nombreJTF.getText().isBlank() || nombreJTF.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Debe completar los campos.");
						return;
				}
					
				if(cliente == null) {
					cliente = new Cliente(instancia, ipJTF.getText(), Integer.parseInt(puertoJTF.getText()));
				}
				
				cliente.conectar(nombreJTF.getText());
			}
		});
		
		add(tituloJL, BorderLayout.NORTH);
		formularioJP.add(puertoJL);
		formularioJP.add(puertoJTF);
		formularioJP.add(ipJL);
		formularioJP.add(ipJTF);
		formularioJP.add(nombreJL);
		formularioJP.add(nombreJTF);
		add(formularioJP, BorderLayout.CENTER);
		add(conectarJB, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}
}
