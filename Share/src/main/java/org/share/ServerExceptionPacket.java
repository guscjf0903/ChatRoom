package org.share;

public class ServerExceptionPacket extends HeaderPacket{
    private String message;
    public ServerExceptionPacket(String message) { //헤더내용 삽입
        this.message = message;
        ServerPacketType serverPacketType = ServerPacketType.EXCEPTION;
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
