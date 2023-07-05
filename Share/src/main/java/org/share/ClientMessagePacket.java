package org.share;

public class ClientMessagePacket extends HeaderPacket {
    private String message;
    private String name;
    public ClientMessagePacket(String message, String name) {
        this.message = message;
        this.name = name;
        ClientPacketType clientPacketType = ClientPacketType.MESSAGE;
        int messagelength = 0;
        int namelength = 0;
        if(message != null){
            messagelength = message.length();
        }
        if(name != null){
            namelength = name.length();
        } //타입마다 메세지가 필요할수도 안할수도 있기때문에 null이면 0으로 초기화
        super.makeClientHeaderPacket(clientPacketType.getValue(), messagelength, namelength); //헤더 내용삽입
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
