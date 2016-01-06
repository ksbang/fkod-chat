package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import client.ClientServiceImpl;
import member.MemberVO;

public class AddFriend extends JFrame implements ActionListener {			// 친구추가창
	private static final long serialVersionUID = 1L;
	
	MemberVO myVO;
	MemberVO target;
	
	public MemberVO getTarget() {
		return target;
	}

	public void setTarget(MemberVO target) {
		System.out.println("타겟도 들어온당");
		this.target = target;
	}
	JButton btnAddFriend, btnSearch, btnExit;
	JPanel menuPanel, uMenuPanel, dMenuPanel; // 메뉴, 위, 아래
	JPanel friendsPanel;
	JPanel southPanel;
	JTextField tfSearch;
	JLabel lbPhone;
	ClientServiceImpl client;

	public AddFriend(MemberVO myVO, ClientServiceImpl client) {
		this.myVO = myVO;
		this.client = client;
		init();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void init() {
		this.setTitle("친구추가");

		menuPanel = new JPanel();
		menuPanel.setBorder(LineBorder.createBlackLineBorder());

		friendsPanel = new JPanel(new GridLayout(5, 1)); // 친구수에 따라 행 바뀌어야 함
		friendsPanel.setBorder(LineBorder.createBlackLineBorder());
		JScrollPane scrollPane = new JScrollPane(friendsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane, BorderLayout.EAST);

		southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBorder(LineBorder.createBlackLineBorder());
		
		URL searchURL = getClass().getClassLoader().getResource("images/search.jpeg");
		ImageIcon searchIcon = new ImageIcon(searchURL);

		///////////////////////////////////////////////////////////////////////////////////////
		btnSearch = new JButton(searchIcon);
		btnSearch.setName("search"); // 친구이름을 넣고 검색하면
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchPhone = tfSearch.getText(); // 입력값
				if (searchPhone.equals("")) {
					JOptionPane.showMessageDialog(null, "친구를 먼저 입력해주세요.");
				} else {
					 // 검색한 폰을 입력 검색해오라고
					client.searchFriends(searchPhone); // 더할 친구를 찾아서 친구를 추가한다.
					System.out.println("타겟은 " + target);
				}
				tfSearch.setText("");
				JPanel fPanel = new JPanel(new GridLayout(1, 3));
				fPanel.setPreferredSize(new Dimension(MAXIMIZED_HORIZ, 50));
				fPanel.setBorder(LineBorder.createBlackLineBorder());
				System.out.println("친구가 들어있어야 정상" + searchPhone);
				JLabel fname = new JLabel(searchPhone);
				fPanel.add(fname);
				friendsPanel.removeAll();
				friendsPanel.add(fPanel);
				friendsPanel.repaint();
				init();
			}
		});
		
		
		btnAddFriend = new JButton("추가");
		btnExit = new JButton("나가기");

		tfSearch = new JTextField(20);

		lbPhone = new JLabel("Phone");

		// 조립단계 => 작은것부터 큰것 순으로

		btnAddFriend.addActionListener(this);
		btnExit.addActionListener(this);

		menuPanel.add(lbPhone);
		menuPanel.add(tfSearch);
		menuPanel.add(btnSearch);

		menuPanel.add(btnAddFriend);

		southPanel.add(btnAddFriend);
		southPanel.add(btnExit);

		this.add(menuPanel, "North");
		this.add(friendsPanel, "Center");
		this.add(southPanel, "South");

		this.setBounds(1250, 0, 350, 350); // 300,400은 좌표값, 1200,300길이
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "search":
			break;
		case "추가":
			System.out.println("이건 되냐 " + target);
			System.out.println("내껀 정상 " + myVO);
			client.addFriends(myVO.getPhone(), target.getPhone()); // 찾아온 타겟의 폰번호
			JOptionPane.showMessageDialog(null, "친구추가 성공");
			//JDialog dialog = new JDialog(this, service.addFriend(myVO, tempAddFriend));
			this.dispose();
		
			//dialog.setSize(250, 200);
			//dialog.setVisible(true);
			break;
		case "나가기":
			this.dispose();
			//MainUI.
			break;
		default:
			break;
		}
	}
}
