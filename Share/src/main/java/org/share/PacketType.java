package org.share;

public enum PacketType {
    SERVER, CLIENT, CONNECT, DISCONNECT
    //서버와 클라이언트는 메세지를 필요로하고 커넥트와 디스커넥트는 메세지는 필요하지 않음
}