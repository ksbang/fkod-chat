package member;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientServiceImpl;
public class JoinUsUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanel;
	private JButton b1;
	private JTextField fieldName, fieldPhone, fieldPass, fieldEmail;
	ClientServiceImpl client;
	public JoinUsUI(ClientServiceImpl client) {
		super("회원가입");
		this.client = client;
		jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(10, 1));
		b1 = new JButton("회원가입");
		jPanel.add(new JLabel("  이름     ")); //이름 라벨 붙임.
		fieldName = new JTextField(10);
		jPanel.add(fieldName);
		jPanel.add(new JLabel("   핸드폰 번호     "));
		fieldPhone = new JTextField(10);
		jPanel.add(fieldPhone);
		
		
		jPanel.add(new JLabel("   비밀번호    "));
		fieldPass = new JTextField(10);
		jPanel.add(fieldPass);
		
		jPanel.add(new JLabel("   이메일 "));
		fieldEmail = new JTextField(20);
		jPanel.add(fieldEmail);
		
		jPanel.add(new Label("     ")); // 변경
		
		jPanel.add(b1);
		b1.addActionListener(this);
		add(jPanel);
		pack();
		setBounds(700, 350, 350, 350);
		this.setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "회원가입":
			dispose();
			client.requestSignUp(fieldName.getText(), fieldPhone.getText(), fieldPass.getText(), fieldEmail.getText());
			fieldEmail.setText("");
			fieldName.setText("");
			fieldPhone.setText("");
			fieldPass.setText("");
			break;

		default:
			break;
		}
	}
}

