package org.share;

import java.io.Serializable;

public class PacketHeader implements Serializable {
    private PacketType packetType;
    private int messageLength;

    public PacketHeader(PacketType packetType,int messageLength) {
        this.packetType = packetType;
        this.messageLength = messageLength;
    }

    public PacketType getType() {
        return packetType;
    }
    public int getMessageLength() {
        return messageLength;
    }
}