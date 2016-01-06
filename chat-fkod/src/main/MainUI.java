package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import chat.ChatUI;
import client.ClientServiceImpl;
import member.MemberVO;

public class MainUI extends JFrame implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	JButton btnAddFriend, btnChat, btnDelFriend, btnExit;
	JPanel menuPanel, uMenuPanel, dMenuPanel; // 메뉴, 위, 아래
	JPanel friendsPanel; // 친구목록
	JPanel southPanel;
	JLabel me, myName, myMail, logo;
	ClientServiceImpl client;
	List<MemberVO> vec;
	MemberVO myInfo;
	boolean flag;
	private StringBuffer friends;
	Map<Integer, ChatUI> rooms;
	
	public void setMyInfo(MemberVO myInfo) {
		this.myInfo = myInfo;
	}
	
	public List<MemberVO> getVec() {
		return vec;
	}

	public void setVec(MemberVO vec) {
	//	this.vec.add(0, element);
	}

	private AddFriend  addFriend;
	
	public AddFriend getAddFriend() {
		return addFriend;
	}

	public void setAddFriend(AddFriend addFriend) {
		this.addFriend = addFriend;
	}

	
	
	public StringBuffer getFriends() {
		return friends;
	}

	

	public HashMap<Integer, ChatUI> getRooms() {
		return (HashMap<Integer, ChatUI>) rooms;
	}

	public void setRooms(int index, ChatUI rooms) {
		this.rooms.put(index, rooms); // 해당 인덱스에 채팅방을 생성함
	}

	
	int[] check;

	public MainUI(ClientServiceImpl client) {
		flag = false;
		rooms = new HashMap<Integer, ChatUI>();
		vec = new Vector<MemberVO>();
		friends = new StringBuffer();
		this.client = client;
		this.init();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	public MainUI(ClientServiceImpl client, MemberVO myInfo) {
		flag = false;
		rooms = new HashMap<Integer, ChatUI>();
		vec = new Vector<MemberVO>();
		friends = new StringBuffer();
		this.client = client;
		this.setMyInfo(myInfo);
		this.init();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	public void init() {

		this.setTitle("Talk-Together");

		menuPanel = new JPanel(new GridLayout(2, 1));
		menuPanel.setBorder(LineBorder.createBlackLineBorder());
		uMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		uMenuPanel.setBorder(LineBorder.createBlackLineBorder());
		dMenuPanel = new JPanel(new GridLayout(1, 3));
		dMenuPanel.setBorder(LineBorder.createBlackLineBorder());
		friendsPanel = new JPanel(new GridLayout(10, 1)); // 친구수에 따라 행 바뀌어야 함	
		friendsPanel.setBorder(LineBorder.createBlackLineBorder());
		uMenuPanel.setBackground(new Color(255, 255, 255)); // 타이틀 색상
		dMenuPanel.setBackground(new Color(255, 255, 255)); //
		friendsPanel.setBackground(new Color(255, 255, 255));
		
		JScrollPane scrollPane = new JScrollPane(friendsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane, BorderLayout.EAST);

		friendsPanel.setAutoscrolls(true); // 자동스크롤생성

		southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBorder(LineBorder.createBlackLineBorder());
		
//		URL imageURL = getClass().getClassLoader().getResource("image/" + temp[i] + ".gif");
//		btns.get(i).setIcon(new ImageIcon(imageURL));
		URL friendURL = getClass().getClassLoader().getResource("images/addFriend.png");
		URL logoURL = getClass().getClassLoader().getResource("images/logo.png");
		ImageIcon addFriendIcon = new ImageIcon(friendURL);
		ImageIcon addLogoIcon = new ImageIcon(logoURL);
		logo = new JLabel(addLogoIcon); //로고 라벨 제작
		
		
		btnAddFriend = new JButton(addFriendIcon);
		btnAddFriend.setName("addFrined");
		btnAddFriend.addActionListener(new ActionListener() { // 친구추가 아이콘
			@Override
			public void actionPerformed(ActionEvent e) {
				addFriend = new AddFriend(myInfo, client);
			}
		});

		
		btnChat = new JButton("채팅하기");
		btnDelFriend = new JButton("친구삭제");
		btnExit = new JButton("종료");

		me = new JLabel("  나(" + myInfo.getName() +")" );
		myName = new JLabel(myInfo.getEmail());
		myMail = new JLabel(myInfo.getPhone());

		// 조립단계 => 작은것부터 큰것 순으로

		btnChat.addActionListener(this);
		btnDelFriend.addActionListener(this);
		btnExit.addActionListener(this);

		uMenuPanel.add(btnAddFriend);
		uMenuPanel.add(logo); //로고 메뉴 판넬에 붙임.
		dMenuPanel.add(me);
		dMenuPanel.add(myName);
		dMenuPanel.add(myMail);

		menuPanel.add(uMenuPanel);
		menuPanel.add(dMenuPanel);

		southPanel.add(btnChat);
		southPanel.add(btnDelFriend);
		southPanel.add(btnExit);

		vec = client.getVec(); // 친구목록 맨마지막은 나

		// 디비정보 갱신
		check = new int[vec.size()]; // 체크박스목록
		int size = vec.size();
		System.out.println("사이즈는 " + size);
		if (size != 0) {
			for (int i = 0; i < size; i++) { // 친구정보받아와서 실행
				MemberVO mem = vec.get(i);
				JPanel fPanel = new JPanel(new GridLayout(1, 3));
				fPanel.setPreferredSize(new Dimension(MAXIMIZED_HORIZ, 50));
				fPanel.setBorder(LineBorder.createBlackLineBorder());
				JLabel fname = new JLabel("  "+mem.getName());
				JLabel femail = new JLabel(mem.getEmail());
				JCheckBox ckFriend = new JCheckBox(fname.getName());
				ckFriend.setText(mem.getPhone());
				ckFriend.addItemListener(this);
				fPanel.add(fname);
				fPanel.add(femail);
				fPanel.add(ckFriend);
				friendsPanel.add(fPanel);
			}
		} else {
			JPanel fPanel = new JPanel();
			JLabel no = new JLabel("친구를 등록해주세요.");
			fPanel.add(no);
			friendsPanel.add(fPanel);
		}

		this.add(menuPanel, "North");
		this.add(friendsPanel, "Center");
		this.add(southPanel, "South");
		this.setBounds(1250, 0, 350, 700);

		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "채팅하기":
			friends.setLength(0);
			for (int i = 0; i < vec.size(); i++) {
				if (check[i] == 1) {
					friends.append(vec.get(i).getPhone() + "`");
					flag = true;
				}
			}
			if (flag) {
				friends.append(myInfo.getPhone());
				System.out.println("친구친구 " + friends);
				client.creatChatRoom(friends.toString());
				flag = false;
			} else {
				JOptionPane.showMessageDialog(null, "대화상대를 선택해주세요.");
			}
			break;
		case "친구삭제":
			friends.setLength(0);
			for (int i = 0; i < vec.size(); i++) {
				if (check[i] == 1) {
					friends.append(vec.get(i).getPhone() + "`");
					vec.remove(i);
				}
			}
			client.deleteFriend(friends.toString());
			break;
		case "종료":
			client.logOut();
			break;
		default:
			break;
		}

	}

	public MemberVO getMyInfo() {
		return myInfo;
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		String source = e.paramString();
		for (int i = 0; i < vec.size(); i++) {
			if (vec.get(i).getPhone().equals(getSource(source)) && check[i] == 0) {
				check[i] = 1;
			} else if (vec.get(i).getPhone().equals(getSource(source)) && check[i] == 1){
				check[i] = 0;
			}
		}
	}
	
	public String getSource(String resources) {
		return resources.substring(457).substring(resources.substring(457).indexOf("=")+1, resources.substring(457).indexOf("]"));
	}
	
}