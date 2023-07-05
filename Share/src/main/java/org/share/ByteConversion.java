package org.share;
// 헤더 -> 0 ~ 7바이트 - 0~3 패킷의 타입, 4 ~ 7 메세지의 길이
// 바디 -> 8 ~ 39 닉네임 - 40 ~ 1024 메세지의 내용

import java.io.ByteArrayOutputStream;

public class ByteConversion {
    Packet packet = null;
    private byte[] convertedBytes = new byte[1024];
    public ByteConversion(Packet packet) {
        this.packet = packet;
    }
    public byte[] packetToByte(){
        ByteArrayOutputStream testBytes = new ByteArrayOutputStream(1024);
        //헤더 내용
        ClientPacketType type = packet.getHeader().getType();
        byte[] typeByte = intToByteArray(type.getValue()); //enum에 있는 타입을 int로 바꾸고 byte로 변환
        int messageLength = packet.getHeader().getMessageLength();
        byte[] lengthByte = intToByteArray(messageLength); //int로 받은 메세지길이 byte로 변환.
        //바디 내용
        byte[] nameBytes = packet.getBody().getNickname().getBytes();
        byte[] messageBytes = packet.getBody().getMessage().getBytes();

        testBytes.write(typeByte,0,4); // 0부터 3까지 패킷의 타입
        testBytes.write(lengthByte,0,4); // 4부터 7까지 메세지의 길이
        testBytes.write(nameBytes,0,Math.min(nameBytes.length,32)); // 8부터 39까지 닉네임
        testBytes.write(messageBytes,0,Math.min(messageBytes.length,984)); // 40부터 1024까지 메세지



//        System.arraycopy(type.name().getBytes(),0, convertedBytes,0,Math.min(type.name().getBytes().length,10)); // 0부터 10까지 패킷의 타입
//        System.arraycopy(lengthByte,0,convertedBytes,11,Math.min(lengthByte.length,4)); // 11 부터 14까지 메세지의 길이
//        System.arraycopy(nameBytes,0,convertedBytes,15,Math.min(nameBytes.length,25)); // 15부터 39까지 닉네임
//        System.arraycopy(messageBytes,0,convertedBytes,40,Math.min(messageBytes.length,984)); // 40부터 1024까지 메세지

        return testBytes.toByteArray();
    }


    static byte[] intToByteArray(int number){
        byte[] byteArray = new byte[10];
        for(int i = 0; i < 4; i++){
            byteArray[i] = (byte) ((number >> (8 * i)) & 0xFF);
        }
        return byteArray;
    }
}
