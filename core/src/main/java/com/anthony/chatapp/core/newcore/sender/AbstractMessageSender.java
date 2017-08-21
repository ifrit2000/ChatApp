package com.anthony.chatapp.core.newcore.sender;

import com.anthony.chatapp.core.message.entity.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by chend on 2017/7/16.
 */
public abstract class AbstractMessageSender implements MessageSender {
    protected int messageIntoSocketChannel(Message message, SocketChannel socketChannel) throws IOException {
        byte[] messageByte = message.encode();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(messageByte.length);
        byteBuffer.put(messageByte);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        return messageByte.length;
    }
}