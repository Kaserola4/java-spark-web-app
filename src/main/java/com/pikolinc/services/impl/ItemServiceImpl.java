package com.pikolinc.services.impl;

import com.pikolinc.dao.ItemDao;
import com.pikolinc.dao.UserDao;
import com.pikolinc.domain.Item;
import com.pikolinc.dto.request.ItemCreateDto;
import com.pikolinc.exceptions.api.ApiResourceNotFoundException;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.ItemCreatedEvent;
import com.pikolinc.services.ItemService;
import com.pikolinc.services.base.BaseService;
import com.pikolinc.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class ItemServiceImpl extends BaseService implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Override
    public long insert(ItemCreateDto dto) {
        ValidationUtil.validate(dto);

        return withDao(ItemDao.class, dao -> {
            Item itemEntity = Item.builder()
                    .name(dto.name())
                    .price(dto.price())
                    .description(dto.description())
                    .build();

            long itemId = dao.insert(itemEntity);

            logger.info("insert item id is {}", itemId);

            EventBus.publish(new ItemCreatedEvent(itemEntity));
            return itemId;
        });
    }

    @Override
    public List<Item> findAll() {
        logger.info("find all items");
        return withDao(ItemDao.class, ItemDao::findAll);
    }

    @Override
    public Item findById(long id) {
        logger.info("find item by id {}", id);

        return withDao(ItemDao.class, dao ->
                dao.findById(id).orElseThrow(() -> {
                    logger.warn("Item not found with id: {}", id);
                    return new ApiResourceNotFoundException("Item", id);
                })
        );
    }

    @Override
    public long update(long id, ItemCreateDto dto) {
        ValidationUtil.validate(dto);

        return withDao(ItemDao.class, dao -> {
            Item existingItem = dao.findById(id).orElseThrow(() -> new ApiResourceNotFoundException("Item", id));

            existingItem.setName(dto.name());
            existingItem.setPrice(dto.price());
            existingItem.setDescription(dto.description());

            long updated = dao.update(existingItem);
            logger.info("Item updated with id: {}", id);

            return updated;
        });
    }

    @Override
    public long delete(long id) {
        logger.info("Deleting item with id {}", id);

        return withDao(ItemDao.class, dao -> {
            if (dao.findById(id).isEmpty()){
                logger.warn("Attempted to delete non-existent item with id {}", id);
                throw new ApiResourceNotFoundException("Item", id);
            }

            long deleted = dao.deleteById(id);

            logger.info("Item deleted with id: {}", id);

            return deleted;
        });
    }

    @Override
    public Map<String, Boolean> options(long id) {
        logger.info("Checking existence of item with id: {}", id);

        return withDao(UserDao.class, dao -> {
            boolean exists = dao.findById(id).isPresent();
            logger.info("Item with id {} exists: {}", id, exists);
            return Map.of("exists", exists);
        });
    }
}
