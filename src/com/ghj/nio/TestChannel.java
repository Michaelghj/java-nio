package com.ghj.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestChannel {
    //2. 利用通道完成文件的复制(利用内存映射文件)
    //只有byteBuffer支持 其他不支持
    @Test
    public void test2() throws IOException {
       FileChannel inchannel =  FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
       FileChannel outchannel = FileChannel.open(Paths.get("2.jpg"),StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);
       //内存映射文件
        /**
         * 好处：不用操作通道，直接操作缓冲区进行数据的读写
         */
        MappedByteBuffer inMapperbuf = inchannel.map(FileChannel.MapMode.READ_ONLY,0,inchannel.size());
        MappedByteBuffer outMapperbuf = outchannel.map(FileChannel.MapMode.READ_WRITE,0,inchannel.size());

        //直接对缓冲区的数据进行读写
        byte[] bytes = new byte[inMapperbuf.limit()];
        inMapperbuf.get(bytes);
        outMapperbuf.put(bytes);

        inchannel.close();
        outchannel.close();

    }
    //1. 利用通道完成文件的复制(非直接缓冲区)
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
