package org.share;


import static org.share.ClientPacketType.*;
import static org.share.ServerPacketType.serverFindByValue;

abstract class HeaderPacket{
    private int clientIntPacketType;
    private int serverIntPacketType;
    private int messageLength;
    private int nameLength;


    final void makeClientHeaderPacket(int clientPacketType, int messageLength, int nameLength){
        this.clientIntPacketType = clientPacketType;
        this.messageLength = messageLength;
        this.nameLength = nameLength;
    }

    final void makeServerHeaderPacket(int serverPacketType, int messageLength, int nameLength){
        this.serverIntPacketType = serverPacketType;
        this.messageLength = messageLength;
        this.nameLength = nameLength;
    }

    public ClientPacketType getClientIntPacketType(){
        return clientFindByValue(clientIntPacketType);
    }
    public ServerPacketType getServerIntPacketType(){
        return serverFindByValue(serverIntPacketType);
    }
    public int getMessageLength(){
        return messageLength;
    }
    public int getNameLength(){
        return nameLength;
    }


}
