package com.ays.kardex.service;

import com.ays.kardex.dto.transfer.TransferInitiationRequest;
import com.ays.kardex.dto.transfer.TransferResponse;

public interface TransferService {

    TransferResponse iniciarTransferencia(TransferInitiationRequest request);

    TransferResponse confirmarTransferencia(Long transferId);
}
