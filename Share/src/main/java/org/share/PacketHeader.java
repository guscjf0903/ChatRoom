package org.share;

public class PacketHeader {
    private String sender;
    public PacketHeader(String sender){
        this.sender = sender;
    }
    public String getSender(){
        return sender;
    }
}
