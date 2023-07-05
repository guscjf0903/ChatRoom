package org.share;

public class ClientDisconnectPacket extends HeaderPacket {
    private String name;
    public ClientDisconnectPacket(String name) { //디스커넥트는 message가 필요없음
        ClientPacketType clientPacketType = ClientPacketType.DISCONNECT;
        this.name = name;
        super.makeClientHeaderPacket(clientPacketType.getValue(), 0, name.length()); //헤더 내용삽입
    }

    public String getName() {
        return name;
    }
}
