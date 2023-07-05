package org.share;

public class ServerMessagePacket extends HeaderPacket {
    private String message;
    private String name;
    public ServerMessagePacket(String message, String name) { //헤더내용 삽입
        this.message = message;
        this.name = name;
        ServerPacketType serverPacketType = ServerPacketType.MESSAGE;
        int messagelength = 0;
        int namelength = 0;
        if(message != null){
            messagelength = message.length();
        }
        if(name != null){
            namelength = name.length();
        } //타입마다 메세지가 필요할수도 안할수도 있기때문에 null이면 0으로 초기화
        super.makeServerHeaderPacket(serverPacketType.getValue(), messagelength, namelength);
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
