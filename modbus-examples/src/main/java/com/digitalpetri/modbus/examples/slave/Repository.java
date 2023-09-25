package com.digitalpetri.modbus.examples.slave;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;

public class Repository {


    private int[]  holdingRegister;
    
    public Repository(){
        holdingRegister = new int[2000];
        Arrays.fill(holdingRegister, 0);
    }

    public ByteBuf readHoldingRegisters(int address, int quantity){
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(quantity);
        for (int i = address; i < address+quantity; i++) {
            byteBuf.writeShort(holdingRegister[i]);
        }
        return byteBuf;
    }
    public void writeSingleRegister(int address, int value) {
        holdingRegister[address] = value;
    }

    public void writeMultipleRegister(int address, int quantity, ByteBuf values) {
        for (int i = address; i< address + quantity ;i++){
            holdingRegister[i] = values.readShort();
        }
    }
}
