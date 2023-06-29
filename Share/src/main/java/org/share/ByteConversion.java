package org.share;
// 헤더 : 0 ~ 20바이트 - 0~9 패킷의 타입, 10 ~ 13 메세지의 길이
// 바디 : 14 ~ 39 닉네임 - 40 ~ 1024 메세지의 내용

public class ByteConversion {
    Packet packet = null;
    private byte[] convertedBytes = new byte[1024];
    public ByteConversion(Packet packet) {
        this.packet = packet;
    }
    public byte[] packetToByte(){
        byte[] lengthByte;
        int messageLength = packet.getHeader().getMessageLength();
        lengthByte = intToByteArray(messageLength);
        PacketType type = packet.getHeader().getType();
        byte[] nameBytes = packet.getBody().getNickname().getBytes();
        byte[] messageBytes = packet.getBody().getMessage().getBytes();

        System.arraycopy(type.name().getBytes(),0, convertedBytes,0,Math.min(type.name().getBytes().length,10)); // 0부터 10까지 패킷의 타입

        System.arraycopy(lengthByte,0,convertedBytes,11,Math.min(lengthByte.length,4)); // 11 부터 14까지 메세지의 길이
        System.arraycopy(nameBytes,0,convertedBytes,15,Math.min(nameBytes.length,25)); // 15부터 39까지 닉네임
        System.arraycopy(messageBytes,0,convertedBytes,60,Math.min(messageBytes.length,984)); // 60부터 1024까지 메세지

        return convertedBytes;
    }


    static byte[] intToByteArray(int number){
        byte[] byteArray = new byte[10];
        for(int i = 0; i < 4; i++){
            byteArray[i] = (byte) ((number >> (8 * i)) & 0xFF);
        }
        return byteArray;
    }
}
