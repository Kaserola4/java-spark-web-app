package com.pikolinc.services;

import com.pikolinc.domain.Item;
import com.pikolinc.dto.request.ItemCreateDto;

import java.util.List;

/**
 * Service interface for Item operations.
 *
 * @see com.pikolinc.services.impl.ItemServiceImpl
 */
public interface ItemService {
    long insert(ItemCreateDto dto);
    List<Item> findAll();
    Item findById(long id);
    long update(long id, ItemCreateDto dto);
    long delete(long id);
    Object options(long id);
}
