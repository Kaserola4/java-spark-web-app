package com.pikolinc.services;

import com.pikolinc.domain.Item;
import com.pikolinc.dto.request.ItemCreateDto;

import java.util.List;

public interface ItemService {
    long insert(ItemCreateDto dto);
    List<Item> findAll();
    Item findById(long id);
    long update(long id, ItemCreateDto dto);
    long delete(long id);
    Object options(long id);
}
