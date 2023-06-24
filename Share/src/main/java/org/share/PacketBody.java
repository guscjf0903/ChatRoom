package org.share;

import java.io.Serializable;

public class PacketBody implements Serializable {
    private String message;
    private String nickname;

    public PacketBody(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public String getNickname() {
        return nickname;
    }
}
