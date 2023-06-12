package org.example;

enum MessageType{
    CONNECT,
    DISCONNECT,
    MESSAGE
}

public class Message {
    private MessageType type;
    private String content;
    private String sender;

    public Message(String content, String sender){ // 주고받는 일반적인 메세지 생성자
        this.type = MessageType.MESSAGE;
        this.content = content;
        this.sender = sender;
    }
    public Message(MessageType type, String sender){ // 연결이나, 연결끊기 할때 사용하는 생성자
        this.type = type;
        this.sender = sender;
    }

    public String getSender(){
        return sender;
    }
    public String getContent(){
        return content;
    }
    public MessageType getType(){
        return type;
    }
}
