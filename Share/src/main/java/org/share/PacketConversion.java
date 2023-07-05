package org.share;
// 헤더 : 0 ~ 20바이트 - 0~9 패킷의 타입, 10 ~ 13 메세지의 길이
// 바디 : 14 ~ 39 닉네임 - 40 ~ 1024 메세지의 내용

public class PacketConversion {

    byte[] bytes = new byte[1024];
    Packet convertedPacket = null;
    public PacketConversion(byte[] bytes){
        this.bytes = bytes;;
    }

    public Packet byteToPacket(){
        int type = byteArrayToInt(bytes,0,3);
        int length = byteArrayToInt(bytes,4,4);
        String name = new String(bytes,8,32).trim();
        String message = new String(bytes,40,length);
        convertedPacket = new Packet(ClientPacketType.clientFindByValue(type),message,name);
        return convertedPacket;
    }

    static int byteArrayToInt(byte[] byteArray,int startIdx, int endIdx){
        int value = 0;
        for (int i = startIdx; i <= endIdx; i++) {
            value += ((int) byteArray[i] & 0xFF) << (8 * (i - startIdx));
        }
        return value;
    }


}
