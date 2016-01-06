package chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ClientServiceImpl;

public class ChatUI extends JFrame implements ActionListener, KeyListener {

//	public static void main(String[] args) {
//		ChatUI ui = new ChatUI();
//	}
	private static final long serialVersionUID = 1L;
	private ClientServiceImpl client;
	private JPanel inputField;
	private JButton jbOk, jbClear, jbExit;
	private JTextArea area;
	private JTextField field;
	private int myRoomNumber;
	private int roomNumber;
	public int getRoomNumber() {
		return roomNumber;
	}
	
	public void setArea(String msg) {
		area.append(msg);
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public ChatUI(ClientServiceImpl client, int roomNum) {
		super("Talk-Together");
		myRoomNumber = roomNum;
		this.client = client;
		// 부품준비
		init();
		// 조립
		assembly();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	private void init() {
		inputField = new JPanel();
		jbOk = new JButton("확인");
		jbClear = new JButton("지우개");
		jbExit = new JButton("종료");
		area = new JTextArea();
		field = new JTextField(25);
	}

	private void assembly() {
		field.addKeyListener(this);
		jbOk.addActionListener(this);
		jbClear.addActionListener(this);
		jbExit.addActionListener(this);
		inputField.add(field);
		inputField.add(jbOk);
		inputField.add(jbClear);
		inputField.add(jbExit);
		area.setEditable(false);
		area.setBackground(new Color(234, 153, 153));
		add(area, "Center");
		add(inputField, "South");
		pack();
		setBounds(100, 100, 550, 500);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "확인":
			//area.append(field.getText() + "\n");
			//서버로 전송
			client.sendMessage(field.getText(), myRoomNumber); 
			field.setText(""); //창 초기화
			break;
		case "지우개":
			area.setText("");
			break;
		case "종료":
			//종료신호를 서버로 보내고
			client.exitChatRoom(myRoomNumber);
			dispose();
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			jbOk.doClick();
			break;
		default:
			break;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
