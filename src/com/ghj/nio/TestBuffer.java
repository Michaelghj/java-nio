package com.ghj.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class TestBuffer {

    @Test
    public void test3(){
        //分配直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect());
    }
    @Test
    public void test2(){
        String str = "hello world";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        byte[] b = new byte[buffer.limit()];
        buffer.get(b,0,2);
        System.out.println(new String(b,0,2));

        System.out.println(buffer.position());
        //mark标记
        buffer.mark();
        buffer.get(b,2,2);
        System.out.println(new String(b,2,2));
        System.out.println(buffer.position());

        //reset()恢复到remark的位置
        buffer.reset();
        System.out.println(buffer.position());
    }
    @Test
    public void test(){
        String str = "hello";
        //分配一个指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        //2. 利用put()存入数据到缓冲区
        buffer.put(str.getBytes());
        System.out.println("========put()=========");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        //3.切换成读取数据的模式
        buffer.flip();
        System.out.println("========put()=========");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        //利用get()方法读取缓冲区的数据
        byte[] b = new byte[buffer.limit()];
        buffer.get(b);
        System.out.println(new String(b,0,b.length));
        System.out.println("========get()=========");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        //rewind()
        buffer.rewind();
        System.out.println("========rewind()=========");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        //clear()清空缓冲区  但是缓冲区中的数据依然存在，但是出于被遗忘状态
        buffer.clear();
        System.out.println("========clear()=========");
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        System.out.println((char) buffer.get());

    }
}
