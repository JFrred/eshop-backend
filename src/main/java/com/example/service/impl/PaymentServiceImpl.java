package com.example.service.impl;

import com.example.dto.PaymentRepresentation;
import com.example.exception.PaymentNotFound;
import com.example.mapper.PaymentMapper;
import com.example.repository.PaymentDetailsRepository;
import com.example.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentDetailsRepository repository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentRepresentation get(int id) {
        return paymentMapper.mapSourceToDto(repository.findById(id).orElseThrow(
                () -> new PaymentNotFound("Could not find payment details with id = " + id)));
    }
}
