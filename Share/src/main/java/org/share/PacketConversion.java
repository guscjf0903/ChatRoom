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
        String type = new String(bytes,0,10).trim();
        int length = byteArrayToInt(bytes,11,14);
        String name = new String(bytes,15,40).trim();
        String message = new String(bytes,60,length);
        convertedPacket = new Packet(PacketType.valueOf(type),message,name);
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
