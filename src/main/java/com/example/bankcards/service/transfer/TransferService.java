package com.example.bankcards.service.transfer;

import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;

import java.util.Map;

public interface TransferService {

    Map<String,String> transferBetweenOwnCards(TransferRequest transferRequest);
}
