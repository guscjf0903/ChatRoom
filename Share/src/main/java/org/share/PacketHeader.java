package org.share;
import java.io.Serializable;

public class PacketHeader implements Serializable {
    private String sender;
    public PacketHeader(String sender){
        this.sender = sender;
    }
    public String getSender(){
        return sender;
    }
}
