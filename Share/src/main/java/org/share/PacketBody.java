package org.share;
import java.io.Serializable;

public class PacketBody implements Serializable {
    private String message;
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
