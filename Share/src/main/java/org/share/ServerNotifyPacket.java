package org.share;

public class ServerNotifyPacket extends HeaderPacket{
    private String message;
    public ServerNotifyPacket(String message) { //헤더내용 삽입
        this.message = message;
        ServerPacketType serverPacketType = ServerPacketType.NOTIFY;
        int messagelength = 0;
        if(message != null){
            messagelength = message.length();
        }
        super.makeServerHeaderPacket(serverPacketType.getValue(), messagelength, 0);
    }

    public String getMessage() {
        return message;
    }
}
