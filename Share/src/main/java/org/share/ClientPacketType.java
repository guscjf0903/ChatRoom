package org.share;

public enum ClientPacketType {
    MESSAGE(0),
    CONNECT(1),
    DISCONNECT(2);
    private int value;

    ClientPacketType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static ClientPacketType clientFindByValue(int value){
        for(ClientPacketType type : values()){
            if(type.getValue() == value){
                return type;
            }
        }
        return null;
    }

    //서버와 클라이언트는 메세지를 필요로하고 커넥트와 디스커넥트는 메세지는 필요하지 않음
}
