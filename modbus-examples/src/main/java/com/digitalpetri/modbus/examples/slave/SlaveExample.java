/*
 * Copyright 2016 Kevin Herron
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitalpetri.modbus.examples.slave;

import java.util.concurrent.ExecutionException;

import com.digitalpetri.modbus.slave.ModbusTcpSlave;
import com.digitalpetri.modbus.slave.ModbusTcpSlaveConfig;
import com.digitalpetri.modbus.slave.SlaveServiceRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlaveExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final SlaveExample slaveExample = new SlaveExample();
        int port = 50200;
        if (args.length ==1 ){
            System.out.printf("Setting port: " + args[0]);
            port = Integer.parseInt(args[0]);
        }
        slaveExample.start(port);

        Runtime.getRuntime().addShutdownHook(new Thread("modbus-slave-shutdown-hook") {
            @Override
            public void run() {
                slaveExample.stop();
            }
        });

        Thread.sleep(Integer.MAX_VALUE);
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ModbusTcpSlaveConfig config = new ModbusTcpSlaveConfig.Builder().build();
    private final ModbusTcpSlave slave = new ModbusTcpSlave(config);

    public SlaveExample() {}

    public void start(int port) throws ExecutionException, InterruptedException {
        logger.info("Starting slave");
        SlaveServiceRequestHandler serviceRequestHandler = new SlaveServiceRequestHandler();
        slave.setRequestHandler(serviceRequestHandler);
        slave.bind("localhost", port).get();
    }

    public void stop() {
        slave.shutdown();
    }

}
