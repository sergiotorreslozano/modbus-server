package com.digitalpetri.modbus.slave;

import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.requests.WriteMultipleRegistersRequest;
import com.digitalpetri.modbus.requests.WriteSingleRegisterRequest;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;
import com.digitalpetri.modbus.responses.WriteMultipleRegistersResponse;
import com.digitalpetri.modbus.responses.WriteSingleRegisterResponse;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlaveServiceRequestHandler implements ServiceRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Repository repository = new Repository();
    @Override
    public void onReadHoldingRegisters(ServiceRequest<ReadHoldingRegistersRequest, ReadHoldingRegistersResponse> service) {
        logClientInformation(service);
        ReadHoldingRegistersRequest request = service.getRequest();
        ByteBuf holdingRegisters = repository.readHoldingRegisters(request.getAddress(),request.getQuantity());
        service.sendResponse(new ReadHoldingRegistersResponse(holdingRegisters));
    }

    @Override
    public void onWriteMultipleRegisters(ServiceRequest<WriteMultipleRegistersRequest, WriteMultipleRegistersResponse> service) {
        logClientInformation(service);
        WriteMultipleRegistersRequest request = service.getRequest();
        repository.writeMultipleRegister(request.getAddress(), request.getQuantity(), request.getValues() );
        service.sendResponse(new WriteMultipleRegistersResponse(request.getAddress(), request.getQuantity()));
    }


    @Override
    public void onWriteSingleRegister(ServiceRequest<WriteSingleRegisterRequest, WriteSingleRegisterResponse> service) {
        logClientInformation(service);
        WriteSingleRegisterRequest request = service.getRequest();
        repository.writeSingleRegister(request.getAddress(), request.getValue());
        service.sendResponse(new WriteSingleRegisterResponse(request.getAddress(), request.getValue()));
    }

    private void logClientInformation(ServiceRequest service){
        String clientRemoteAddress = service.getChannel().remoteAddress().toString();
        String clientIp = clientRemoteAddress.replaceAll(".*/(.*):.*", "$1");
        String clientPort = clientRemoteAddress.replaceAll(".*:(.*)", "$1");
        logger.debug("clientIp:{} clientPort:{}", clientIp, clientPort);
        logger.debug("transactionId:{} unitId:{}" ,  service.getTransactionId(), service.getUnitId());
    }
}
