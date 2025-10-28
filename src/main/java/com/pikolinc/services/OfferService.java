package com.pikolinc.services;


import com.pikolinc.domain.Offer;
import com.pikolinc.domain.OfferStatus;
import com.pikolinc.dto.request.OfferCreateDto;
import com.pikolinc.dto.request.OfferUpdateDto;

import java.util.List;

public interface OfferService {
    long insert(OfferCreateDto offerCreateDto);
    List<Offer> findAll();
    Offer findById(long id);
    long update(long id, OfferUpdateDto offerUpdateDto);
    long delete(long id);

    // Filters
    List<Offer> findByItemId(long itemId);
    List<Offer> findByItemIdAndStatus(long itemId, OfferStatus status);
    List<Offer> findByUserId(long userId);
    List<Offer> findByUserIdAndStatus(long userId, OfferStatus status);
    List<Offer> findByStatus(OfferStatus status);
    
    // State changes
    long acceptOffer(long id);
    long rejectOffer(long id);
    long completeOffer(long id);
    long cancelOffer(long id);

    // Rebidding if status = OPEN
    long updateAmount(long id, double amount);
}
