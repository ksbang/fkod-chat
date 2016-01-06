package server;

import java.io.Serializable;
public class ChatRoomVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private StringBuffer clients; // 접속한 유저목록
	private int numOfUser;
	//private StringBuffer roomBuffer; // 채팅로그 만들경우 사용
	public int getNumOfUser() {
		return numOfUser;
	}
	public void setNumOfUser(int numOfUser) {
		this.numOfUser = numOfUser;
	}
	public ChatRoomVO(String clients, int length) {
		numOfUser = length;
		this.clients = new StringBuffer();
		this.clients.append(clients);
	}
	public String getClients() {
		return clients.toString();
	}

	public void setClients(String clients) {
		this.clients.append("`" + clients);
	}

	public void delClients(String clients) {
		String temp = this.clients.toString().replace(clients, ""); //명단에서 지워버림
		temp.replace("``", "`");
		this.clients.setLength(0);
		this.clients.append(temp);
		numOfUser--;
	}

//	public StringBuffer getRoomBuffer() {
//		return roomBuffer;
//	}
//
//	public void setRoomBuffer(StringBuffer roomBuffer) {
//		this.roomBuffer = roomBuffer;
//	}
//	
}
