package com.example.bankcards.service.card;

import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CardBlockRequestService {

    CardBlockRequest makeRequest(Integer cardId);

    Map<String,String> blockByRequest(Integer cardBlockRequestId);

    Page<CardBlockRequest> getRequests(Pageable pageable);
}
