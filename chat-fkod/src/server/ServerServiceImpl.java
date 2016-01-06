package server;

/**
 * 서버 서비스에서 구현할 내용
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import global.Command;
import member.MemberVO;
/////
public class ServerServiceImpl implements Runnable {
	private Socket client;
	private DataInputStream in;
	private DataOutputStream out;
	private StringBuffer buffer;
	private ServerDAO dao;
	private Thread thisThread;
	private static int roomNumber = 0;
	private static Map<Integer, ChatRoomVO> rooms;
	private String phone;
	private static List<ServerServiceImpl> users = new Vector<ServerServiceImpl>();
	private boolean flag;
	private String user;

	
	public void setThisThread(Thread thisThread) {			//지금 쓰레드 지정
		this.thisThread = thisThread;

	}
	public ServerServiceImpl() {
	}
	public ServerServiceImpl(Socket clientSocket) {			//초기화 (소켓과  input, output stream set)
		flag = true;
		user = "";
		rooms = new HashMap<Integer, ChatRoomVO>();
		dao = new ServerDAO();
		client = clientSocket;
		try {
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
			buffer = new StringBuffer(4096);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {				//실행
		try {
			while (true) {
				String command = in.readUTF();
				if (command.equals("")) {
					continue;
				}
				StringTokenizer token = new StringTokenizer(command, Command.COMMAND_DELIMITER);
				switch (token.nextToken()) {
				case Command.REQUEST_LOGIN:
					System.out.println("로그인 요청이 들어왔습니다.");
					phone = token.nextToken(); // 폰번
					String password = token.nextToken(); // 비번
					for (int i = 0; i < users.size(); i++) {
						if (users.get(i).phone.equals(phone)) { // 이미 로그인상태이면
							flag = false;
							buffer.setLength(0);
							buffer.append(Command.DENY_LOGIN);
							send(buffer.toString());
							break;
						}
					} //로그인이 되지 않은상태면
					if (flag) {
						System.out.println("폰번 : " + phone + " 패스워드 " + password);
						List<MemberVO> temp = dao.confirmLogin(phone, password);
						System.out.println("템프수 : " + temp.size());
						System.out.println(temp);
						if (!temp.isEmpty()) {
							users.add(this); // 해당 유저를 유저목록에 추가함
							respondLogin(temp.toString());
						} else {
							System.out.println("비어있는데");
							respondLogin(null);
						}
					}
					flag = true;
					break;
				case Command.SEND_MESSAGE: // 명령어|방번호|내이름>>메시지
					int msgtempNum = Integer.parseInt(token.nextToken()); // 방번호
					String msgMsg = token.nextToken(); // 메시지
					System.out.println("방번호가 뭐길래 " + msgtempNum);
					String msgNames = rooms.get(msgtempNum).getClients();
					System.out.println("명단 " + msgNames);
					StringTokenizer msgToken = new StringTokenizer(msgNames, Command.CONTENT_DELIMITER); // 친구목록
					int msgLength = msgToken.countTokens();
					for (int i = 0; i < msgLength; i++) {
						String msgUser = msgToken.nextToken();
						System.out.println("유저는 " + msgUser);
						for (int j = 0; j < users.size(); j++) {
							if (msgUser.equals(users.get(j).phone)) {
								System.out.println(user + " 같습니다.");
								buffer.setLength(0);
								buffer.append(Command.DEFFUSION_MESSAGE + "|" + msgtempNum + "|" + msgMsg);
								users.get(j).send(buffer.toString());
							}
						}
					}
					break;
				case Command.SEND_SEVER: // 명령어|방번호|내이름>>메시지
					int sendNum = Integer.parseInt(token.nextToken()); // 방번호
					String sendMsg = token.nextToken(); // 메시지
					StringTokenizer realmsg = new StringTokenizer(sendMsg, ">>");
					int sendLength = realmsg.countTokens();
					for (int i = 0; i < sendLength; i++) {
						sendMsg = realmsg.nextToken();
					}
					System.out.println("방번호가 뭐길래 " + sendNum);
					String sendNames = rooms.get(sendNum).getClients();
					System.out.println("명단 " + sendNames);
					StringTokenizer sendToken = new StringTokenizer(sendNames, Command.CONTENT_DELIMITER); // 친구목록
					sendLength = sendToken.countTokens();
					for (int i = 0; i < sendLength; i++) {
						String sendUser = sendToken.nextToken();
						System.out.println("유저는 " + sendUser);
						for (int j = 0; j < users.size(); j++) {
							if (sendUser.equals(users.get(j).phone)) {
								System.out.println(user + " 같습니다.");
								buffer.setLength(0);
								buffer.append(Command.DEFFUSION_MESSAGE + "|" + sendNum + "|" + "<서버> " + sendMsg);
								users.get(j).send(buffer.toString());
							}
						}
					}
					break;
				case Command.DEL_FRIEND: //명령어|내폰|친구폰들
					String myPhone = token.nextToken();
					StringTokenizer delToken = new StringTokenizer(token.nextToken(), Command.CONTENT_DELIMITER);
					int delLength = delToken.countTokens();
					int temp = 0;
					for (int i = 0; i < delLength; i++) {
						 temp = dao.deleteFriend(myPhone, delToken.nextToken());
					}
					if (temp != 0) {
						buffer.setLength(0);
						buffer.append(Command.ALLOW_DEL);
						send(buffer.toString());
					} else {
						buffer.setLength(0);
						buffer.append(Command.DENY_DEL);
						send(buffer.toString());
					}
					break;
				case Command.SIGN_UP:
					StringTokenizer signToken = new StringTokenizer(token.nextToken(), Command.CONTENT_DELIMITER);
					int result = dao.confirmSignUp(signToken.nextToken(), signToken.nextToken(),
							signToken.nextToken(), signToken.nextToken());
					if (result != 0) {
						buffer.setLength(0);
						buffer.append(Command.ALLOW_SIGN_UP);
						send(buffer.toString());
					} else {
						buffer.setLength(0);
						buffer.append(Command.DENY_SIGN_UP);
						send(buffer.toString());
					}
					break;
				case Command.SEARCH_FRIENDS: // 친구검색 명령어 | 친구번호
					MemberVO target = dao.searchFriend(token.nextToken());
					// System.out.println("디비찾아온친구 " + target.toString());
					if (target != null) {
						buffer.setLength(0);
						buffer.append(Command.ALLOW_SEARCH + "|" + target.toString());
						send(buffer.toString());
					} else {
						buffer.setLength(0);
						buffer.append(Command.DENY_SEARCH);
						send(buffer.toString());
					}
					break;
				case Command.ADD_FRIENDS:
					// 내전화번호 , 친구전화번호
					System.out.println("친구추가하고싶어 죽겄당!!!");
					int addtempNum = dao.addFriend(token.nextToken().toString(), token.nextToken().toString());
					if (addtempNum != 0) {
						buffer.setLength(0);
						buffer.append(Command.ALLOW_FRIENDS);
						send(buffer.toString());
					} else {
						buffer.setLength(0);
						buffer.append(Command.DENY_FRIENDS);
						send(buffer.toString());
					}
					break;
				case Command.CREATE_CHATROOM: // 명령어|사람목록
					int createNum = roomNumber;
					String createNames = token.nextToken(); // 사람들 목록
					StringTokenizer createToken = new StringTokenizer(createNames, Command.CONTENT_DELIMITER);
					int createLength = createToken.countTokens();
					int count = 0;
					for (int i = 0; i < createLength; i++) {
						System.out.println("수행함");
						String createUser = createToken.nextToken();
						System.out.println("방을만들 이름은 " + createUser); // 친구 스레드명
						for (int j = 0; j < users.size(); j++) {
							if (createUser.equals(users.get(j).phone)) {
								count++;
							}
						}
					}
					StringTokenizer createSecondToken = new StringTokenizer(createNames, Command.CONTENT_DELIMITER);
					createLength = createSecondToken.countTokens();
					if (count < 2) {
						buffer.setLength(0);
						buffer.append(Command.DENY_CHATROOM + "|");
						send(buffer.toString());
					} else {
						for (int i = 0; i < createLength; i++) {
							String createUser = createSecondToken.nextToken();
							System.out.println("카운트수 " + count);
							System.out.println("유저수는 : " + users.size());
							for (int j = 0; j < users.size(); j++) { // 유저목록에서
								System.out.println("유저 " + users.get(j).phone);
								if (createUser.equals(users.get(j).phone)) {
									System.out.println("같아서 수행 " + users.get(j).phone);
									buffer.setLength(0);
									buffer.append(Command.DEFFUSION_CHATROOM + "|" + createNum); // 방번호를
									users.get(j).send(buffer.toString());
									break;
								}
							}
						}
						rooms.put(createNum, new ChatRoomVO(createNames, createLength)); // 방을
						roomNumber++;
					}
					// 해당방의 정보를 가지고 있는 채트룸을 만듦
					break;
				case Command.EXIT_CHATROOM: // 명령어 | 방번호 | 폰번 | 내이름

					int exittempNum = Integer.parseInt(token.nextToken()); // 방번호
					String exitphone = token.nextToken(); // 폰번호
					String myName = token.nextToken(); // 이름
					rooms.get(exittempNum).delClients(exitphone); // 방번호의 명단에서 해당폰번호 삭제
					if (rooms.get(exittempNum).getNumOfUser() == 0) {
						rooms.remove(exittempNum);
						System.out.println("방이 폭파되었습니다.");
					} else {
						System.out.println("방번호가 뭐길래 " + exittempNum);
						String exitNames = rooms.get(exittempNum).getClients(); // 방번호의 명단을 가져옴
						System.out.println("명단 " + exitNames);
						StringTokenizer exitToken = new StringTokenizer(exitNames, Command.CONTENT_DELIMITER); // 친구목록
						int exitlength = exitToken.countTokens();
						for (int i = 0; i < exitlength; i++) {
							String exitUser = exitToken.nextToken();
							System.out.println("유저는 " + exitUser);
							for (int j = 0; j < users.size(); j++) {
								if (exitUser.equals(users.get(j).phone)) {
									System.out.println(user + " 같습니다.");
									buffer.setLength(0);
									buffer.append(Command.DEFFUSION_MESSAGE + "|" + exittempNum + "|" + "<서버> " + myName
											+ "님이 퇴실하셨습니다.");
									users.get(j).send(buffer.toString());
								}
							}
						}
					}
					break;
				case Command.LOGOUT:
					String delphone = token.nextToken();
					System.out.println("삭제할 폰번");
					buffer.setLength(0);
					buffer.append(Command.LOGOUT);
					send(buffer.toString());
					int outLength = users.size();
					for (int i = 0; i < outLength; i++) {
						if (users.get(i).phone.equals(delphone)) {
							release();
							users.remove(i);
							break;
						}
					}
					break;
				default:
					break;
				}
				Thread.sleep(200);
			}
		} catch (Exception e) {
			System.out.println("무슨문제냥 : " + e);
			e.printStackTrace();
		} finally {
			return;
		}
	}

	private StringTokenizer StringTokenizer(String nextToken, String contentDelimiter) {
		// TODO Auto-generated method stub
		return null;
	}

	public void respondLogin(String str) {
		if (str != null) {
			buffer.setLength(0);
			buffer.append(Command.ALLOW_LOGIN + "|" + str); // 로그인 허가
			send(buffer.toString());
		} else {
			send(Command.CANT_LOGIN); // 로그인 거부 전송
		}
	}

	public void send(String sendData) {
		try {
			out.writeUTF(sendData);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void release() {
		if (thisThread != null) {
			thisThread = null;
		}
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (client != null) {
				client.close();
			}
		} catch (Exception e) {
		} finally {
			out = null;
			in = null;
			client = null;
		}
	}
}
