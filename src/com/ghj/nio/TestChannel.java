package com.ghj.nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestChannel {

    @Test
    public void test4() throws IOException {

        RandomAccessFile random = new RandomAccessFile("1.txt", "rw");
        //获取通道
        FileChannel channel1 = random.getChannel();
        //分配大小指定的缓冲区
        ByteBuffer buffer1 = ByteBuffer.allocate(100);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);

        //分散读取
        ByteBuffer[] bytes = {buffer1, buffer2};
        channel1.read(bytes);
        for (ByteBuffer byteBuffer : bytes) {
            byteBuffer.flip();
        }
        System.out.println(new String(bytes[0].array(), 0, bytes[0].limit()));

        System.out.println(new String(bytes[1].array(), 0, bytes[1].limit()));

        //聚集写入
        RandomAccessFile random1 = new RandomAccessFile("2.txt", "rw");
        FileChannel fileChannel = random1.getChannel();

        fileChannel.write(bytes);
        random.close();
        random1.close();
    }
    @Test
    public void test3() throws IOException {
        FileChannel inchannel =  FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outchannel = FileChannel.open(Paths.get("2.jpg"),StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE);

        inchannel.transferTo(0,inchannel.size(),outchannel);

        inchannel.close();
        outchannel.close();
    }
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
