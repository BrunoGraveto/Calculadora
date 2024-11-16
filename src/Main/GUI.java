/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author alcan
 */
public class GUI extends JFrame implements ActionListener {
	
	// Configurações Tela
	public final int SCREEN_WIDTH = 300;
	public final int SCREEN_HEIGHT = 350;
	
	// REGEX para verificar as expressões
	private static final String REGEX = "(\\-?\\d+\\.?\\d*)(\\+|\\-|\\*|\\/)(\\-?\\d+\\.?\\d*)";
	
	// Componentes
	JTextField txtVisor;
	JPanel panelNumpad;
	
	// Variavel para verificar se é a primeira vez apos iniciar
	boolean apagar = true;
	
	public GUI() {
		
		configurarFrame();
		configurarComponentes();
		mostrarFrame();
		
	}
	
	/*
		Configura o frame.
	*/
	public void configurarFrame() {
		setTitle("Calculadora");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	/*
		Configura os componentes.
	*/
	public void configurarComponentes() {
		
		// Visor
		txtVisor = new JTextField("0");
		txtVisor.setEditable(false);
		txtVisor.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT/5));
		txtVisor.setHorizontalAlignment(SwingConstants.RIGHT);
		txtVisor.setBorder(null);
		txtVisor.setBackground(Color.DARK_GRAY);
		txtVisor.setForeground(Color.WHITE);
		txtVisor.setFont(new Font("Arial", Font.PLAIN, 20));
		add(txtVisor, BorderLayout.NORTH);
		
		// Numpad
		panelNumpad = new JPanel();
		panelNumpad.setLayout(new GridLayout(5, 4, 4, 4));
		panelNumpad.setBackground(Color.DARK_GRAY);
		
		String numpad[] = {
			"EXIT", "AC", "C", " ",
			"1", "2", "3", "+",
			"4", "5", "6", "-",
			"7", "8", "9", "*",
			"0", ".", "=", "/"
		};
		
		for (String d : numpad) {
			JButton btnAdd = new JButton(d);
			btnAdd.setBorder(null);
			btnAdd.addActionListener(this);
			
			btnAdd.setForeground(Color.WHITE);
			if (d.equals("=")) {
				btnAdd.setBackground(Color.BLUE);
			} else if (!d.matches("\\d")) {
				btnAdd.setBackground(Color.GRAY);
			} else {
				btnAdd.setForeground(Color.DARK_GRAY);
				btnAdd.setBackground(Color.LIGHT_GRAY);
			}
			
			panelNumpad.add(btnAdd);
		}
		add(panelNumpad, BorderLayout.CENTER);
		
		// Foca no frame
		setFocusable(true);
	}
	
	/*
		Faz ultimos ajustes e mostra o frame.
	*/
	public void mostrarFrame() {
		txtVisor.setFocusable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/*
		Calcula de acordo com os numeros e operador passado.
	*/
	public String calcular(double num, String operador, double num2) {
		switch(operador) {
		case "+":
			return ""+(num + num2);
		case "-":
			return ""+(num - num2);
		case "*":
			return ""+(num * num2);
		case "/":
			if (num2 == 0) {
				apagar = true;
				return "Não é possivel dividir por zero!";
			} else {
				return ""+(num / num2);
			}
		default:
			return "0.0";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if (src instanceof JButton) {
			// Converte o objeto em botão
			JButton btn = (JButton) src;
			
			// Passa para duas string os textos do visor e do botão pressionado
			String digito = btn.getText();
			String visor = txtVisor.getText();
			
			// Regex para identificar se já da pra fazer conta
			Pattern pattern = Pattern.compile(REGEX);
			Matcher matcher = pattern.matcher(visor);
			
			// Variaveis para calcular
			double num = 0.0;
			String operador = null;
			double num2 = 0.0;
			
			// Se possivel adiciona as variaveis seus valores
			if (matcher.matches()) {
				num = Double.parseDouble(matcher.group(1));
				operador = matcher.group(2);
				num2 = Double.parseDouble(matcher.group(3));
			}
			
			if (apagar && !digito.matches("(\\+|\\-|\\*|\\/)")) {
				visor = "";
				apagar = false;
			}
			// Fecha o programa
			else if (digito.equals("EXIT")) {
				System.exit(0);
			}
			// Calcula
			else if (digito.equals("=")) {
				// Se for possivel faz a conta
				if (matcher.matches()) {
					visor = calcular(num, operador, num2);
				}
				digito = "";
			}
			// Caso seja algum outro botão pressionado
			else {
				// Apaga a expressão presente inteira
				if (digito.equals("C")) {
					visor = "";
					digito = "";
				}
				// Apaga digito por digito
				else if (digito.equals("AC")) {
					// Verifica se tem algo para apagar
					if (visor.length() > 0) {
						visor = visor.substring(0, visor.length()-1);
					}
					digito = "";
				}
				// Caso seja algum operador
				else if (digito.matches("(\\+|\\-|\\*|\\/)")) {
					// Obtem o ultimo digito da expressão
					String lastChar = visor.charAt(visor.length()-1)+"";
					
					// Se for possivel, antes de continuar, ele calcula
					if (matcher.matches()) {
						visor = calcular(num, operador, num2);
					}
					// Caso ja tenha um operador e o usuário digite novamente
					else if (lastChar.matches("(\\+|\\-|\\*|\\/)")) {
						visor = visor.substring(0, visor.length()-1);
					}
				}
			}
			
			// Retirar o "0.0"
			if (visor.matches(".*.0$")) {
				visor = visor.replace(".0","");
			}
			
			// Imprime no visor
			txtVisor.setText(visor+digito);
			
			// Solicita o foco para o frame
			requestFocusInWindow();
		}
	}
	
}
