package com.ghj.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestChannel {
    //利用通道完成文件的复制
    @Test
    public void test1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("1.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("2.jpg");

        //1.获取通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outchannel = fileOutputStream.getChannel();
        //2. 获取指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //3. 将通道中的数据存入缓冲区中
        while (inChannel.read(buffer)!=-1){
            //切换成读数据模式
            buffer.flip();
            //4. 将缓冲区的数据写入通道
            outchannel.write(buffer);
            buffer.clear();
        }
        outchannel.close();
        inChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
