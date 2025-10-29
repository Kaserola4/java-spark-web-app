package com.pikolinc.services;


import com.pikolinc.domain.Offer;
import com.pikolinc.domain.OfferStatus;
import com.pikolinc.dto.request.OfferCreateDto;
import com.pikolinc.dto.request.OfferUpdateDto;
import com.pikolinc.dto.response.OfferResponseDto;

import java.util.List;

public interface OfferService {
    long insert(OfferCreateDto offerCreateDto);
    List<OfferResponseDto> findAll();
    OfferResponseDto findById(long id);
    long update(long id, OfferUpdateDto offerUpdateDto);
    long delete(long id);

    // Filters
    List<OfferResponseDto> findByItemId(long itemId);
    List<OfferResponseDto> findByItemIdAndStatus(long itemId, OfferStatus status);
    List<OfferResponseDto> findByUserId(long userId);
    List<OfferResponseDto> findByUserIdAndStatus(long userId, OfferStatus status);
    List<OfferResponseDto> findByStatus(OfferStatus status);
    
    // State changes
    long acceptOffer(long id);
    long rejectOffer(long id);
    long completeOffer(long id);
    long cancelOffer(long id);

    // Rebidding if status = OPEN
    long updateAmount(long id, double amount);
}
