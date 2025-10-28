package com.pikolinc.services;


import com.pikolinc.domain.Offer;
import com.pikolinc.dto.request.OfferCreateDto;

import java.util.List;

public interface OfferService {
    long insert(OfferCreateDto offerCreateDto);
    List<Offer> findAll();
    Offer findById(long id);
    long update(long id, OfferUpdateDto offerUpdateDto);
    long delete(long id);
    Object options(long id);
}
