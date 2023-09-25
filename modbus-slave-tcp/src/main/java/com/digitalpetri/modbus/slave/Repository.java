package com.digitalpetri.modbus.slave;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Repository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int[]  holdingRegister;
    
    public Repository(){
        logger.info("Initializing repository");
        holdingRegister = new int[2000];
        Arrays.fill(holdingRegister, 0);
        logger.info("Repository initialized");
    }

    public ByteBuf readHoldingRegisters(int address, int quantity){
        logger.info("Reading repository address:{} , quantity:{}" , address, quantity);
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(quantity);
        for (int i = address; i < address+quantity; i++) {
            byteBuf.writeShort(holdingRegister[i]);
        }
        return byteBuf;
    }
    public void writeSingleRegister(int address, int value) {
        logger.info("Writing repository address:{} , value:{}" , address, value);
        holdingRegister[address] = value;
    }

    public void writeMultipleRegister(int address, int quantity, ByteBuf values) {
        logger.info("Writing repository address:{} , quantity:{}" , address, quantity);
        for (int i = address; i< address + quantity ;i++){
            holdingRegister[i] = values.readShort();
        }
    }
}
