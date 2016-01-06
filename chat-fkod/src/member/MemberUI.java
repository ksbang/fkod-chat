package member;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientServiceImpl;

public class MemberUI extends JFrame implements ActionListener, KeyListener{
	private static final long serialVersionUID = 1L;
	private JTextField fieldPhone, fieldPass;
	private JButton b1, b2;
	private JPanel panel;
	private ClientServiceImpl client;
	public MemberUI(final ClientServiceImpl client) {
		super("로그인");
		this.client = client;
		panel = new JPanel();
		b1 = new JButton("로그인");
		b2 = new JButton("회원가입");
		panel.setLayout(new GridLayout(7, 1));
		b1.setLayout(new GridLayout(2, 1));
		panel.add(new JLabel("    핸드폰 번호:")); //판넬에다 "핸드폰 번호" 라벨 in 
		fieldPhone = new JTextField(10);
		panel.add(fieldPhone);
		panel.add(new JLabel("    패스워드: ")); //판넬에다 "패스워드" 라벨 in
		fieldPass = new JTextField(10);
		panel.add(fieldPass);
		panel.add(new Label("   "));
		
		fieldPass.addKeyListener(new KeyListener() { //텍스트 필드에서 엔터 눌렀을 때 이벤트 발생.
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					client.requestLogin(fieldPhone.getText(), fieldPass.getText());
					fieldPhone.setText("");
					fieldPass.setText("");
					break;

				default:
					break;
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		
		});
		
		
		panel.add(b1); //판넬에다 버튼1 붙임.
		panel.add(b2); //판넬에다 버튼2 붙임.
		add(panel); //프레임에다 판넬 붙임.
		b1.addActionListener(this);
		b2.addActionListener(this);
		
		b1.addKeyListener(new KeyListener() { //로그인 버튼 엔터 눌렀을 때 이벤트 발생
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					client.requestLogin(fieldPhone.getText(), fieldPass.getText());
					break;
				default:
					break;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		b2.addKeyListener(this); //회원가입 버튼 엔터 눌렀을 때 이벤트 발생.
		
		pack();
		setBounds(700, 350, 270, 270);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) { // 버튼 마우스로 클릭 했을 때의 이벤트
		switch (e.getActionCommand()) {
		case "로그인":
			client.requestLogin(fieldPhone.getText(), fieldPass.getText());
			break;
		case "회원가입":
			new JoinUsUI(client);
			break;
		default:
			break;
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			new JoinUsUI(client);
			this.dispose();
			break;

		default:
			break;
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}


