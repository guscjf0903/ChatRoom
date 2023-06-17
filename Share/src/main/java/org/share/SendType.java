package org.share;

public class SendType extends Packet {
    MessageType type = null;
    String message = "";

    @Override
    void packetType() {
        type = MessageType.SEND;
    }

    public void makeMessage(String message) { // 보내야할 메세지를 받아서 저장.
        this.message = message;
    }

    public String sendMessage() {
        return message;
    }
}
