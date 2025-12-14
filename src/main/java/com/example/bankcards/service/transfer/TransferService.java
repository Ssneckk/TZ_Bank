package com.example.bankcards.service.transfer;

import com.example.bankcards.util.auxiliaryclasses.request.TransferRequest;

import java.util.Map;

/**
 * Сервис для проведения денежных переводов между картами.
 * <p>
 * Предоставляет метод для перевода средств между своими картами.
 * </p>
 */
public interface TransferService {

    /**
     * Выполняет перевод между картами текущего пользователя.
     *
     * @param transferRequest объект {@link TransferRequest} с данными перевода
     * @return {@link Map} с информацией о результате транзакции
     */
    Map<String,String> transferBetweenOwnCards(TransferRequest transferRequest);
}
