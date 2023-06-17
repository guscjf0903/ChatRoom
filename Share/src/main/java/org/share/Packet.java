package org.share;

enum MessageType{
    CONNECT,
    DISCONNECT,
    SEND
}

public abstract class Packet{
    String name;
    final public void makePacket(String name){
        makename(name);
        packetType();
    }
    abstract void packetType();
    private void makename(String name){
        this.name = name;
    }
    public String returnname(){
        return name;
    }
}
