package org.share;

import java.io.*;

public class ToConvert {
    public static byte[] toByteArray(Packet packet){
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(packet);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        }catch (IOException e){}
        return bytes;
    }

    public static Packet toPacket(byte[] bytes){
        Packet packet = null;
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            packet = (Packet) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return packet;
    }
}
