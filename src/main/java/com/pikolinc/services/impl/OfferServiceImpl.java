package com.pikolinc.services.impl;

import com.pikolinc.dao.OfferDao;
import com.pikolinc.domain.Offer;
import com.pikolinc.domain.OfferStatus;
import com.pikolinc.dto.request.OfferCreateDto;
import com.pikolinc.dto.request.OfferUpdateDto;
import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.exceptions.ValidationException;
import com.pikolinc.exceptions.api.ApiResourceNotFoundException;
import com.pikolinc.exceptions.api.DuplicateResourceException;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.infraestructure.events.OfferCreatedEvent;
import com.pikolinc.services.ItemService;
import com.pikolinc.services.OfferService;
import com.pikolinc.services.UserService;
import com.pikolinc.services.base.BaseService;
import com.pikolinc.util.ValidationUtil;
import com.pikolinc.ws.ItemOfferWebSocketHandler;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OfferServiceImpl extends BaseService implements OfferService {
    private static final Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);
    private final ItemService itemService;
    private final UserService userService;

    public OfferServiceImpl(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public long insert(OfferCreateDto offerCreateDto) {
        ValidationUtil.validate(offerCreateDto);

        itemService.findById(offerCreateDto.itemId());
        userService.findById(offerCreateDto.userId());

        return withDao(OfferDao.class, dao -> {
            final List<OfferResponseDto> offersWithSameItemId = dao.findByItemId(offerCreateDto.itemId());

            offersWithSameItemId.forEach(offer -> {
                if (offer.getUserId().equals(offerCreateDto.userId()))
                    throw new DuplicateResourceException("Offer already exists");
            });

            Offer offer = new Offer();
            offer.setUserId(offerCreateDto.userId());
            offer.setItemId(offerCreateDto.itemId());
            offer.setAmount(offerCreateDto.finalPrice());

            long offerId = dao.insert(offer);

            logger.info("Insert offer with id {}", offerId);

            OfferResponseDto offerResponseDto = findById(offerId);

            EventBus.publish(new OfferCreatedEvent(offerResponseDto));
            return offerId;
        });
    }

    @Override
    public List<OfferResponseDto> findAll() {
        logger.info("Find all offers");
        return withDao(OfferDao.class, OfferDao::findAll);
    }

    @Override
    public OfferResponseDto findById(long id) {
        logger.info("Find offer with id {}", id);

        return withDao(OfferDao.class, dao ->
                dao.findById(id).orElseThrow(() -> {
                    logger.warn("Offer with id {} not found", id);
                    return new ApiResourceNotFoundException("Offer", id);
                })
        );
    }

    @Override
    public long update(long id, OfferUpdateDto offerUpdateDto) {
        ValidationUtil.validate(offerUpdateDto);

        return withDao(OfferDao.class, dao -> {
            OfferResponseDto existingOffer = dao.findById(id).orElseThrow(() -> new ApiResourceNotFoundException("Offer", id));

            Offer offer = Offer.builder()
                    .id(existingOffer.getId())
                    .userId(offerUpdateDto.getUserId())
                    .amount(offerUpdateDto.getAmount())
                    .status(offerUpdateDto.getStatus())
                    .build();

            long updated = dao.update(offer);

            logger.info("Offer updated with id: {}", id);

            return updated;
        });
    }

    @Override
    public long delete(long id) {
        logger.info("Deleting offer with id: {}", id);

        return withDao(OfferDao.class, dao -> {
            if (dao.findById(id).isEmpty()) {
                logger.warn("Attempted to delete non-existent offer with id: {}", id);
                throw new ApiResourceNotFoundException("Offer", id);
            }

            long deleted = dao.deleteById(id);

            logger.info("Deleted offer with id: {}", id);

            return deleted;
        });
    }

    @Override
    public List<OfferResponseDto> findByItemId(long itemId) {
        logger.info("Find by offers with item id: {}", itemId);
        return withDao(OfferDao.class, dao -> dao.findByItemId(itemId));
    }

    @Override
    public List<OfferResponseDto> findByItemIdAndStatus(long itemId, OfferStatus status) {
        logger.info("Find offers with item id: {} and status: {}", itemId, status);
        return withDao(OfferDao.class, dao -> dao.findByItemIdAndStatus(itemId, status.name()));
    }

    @Override
    public List<OfferResponseDto> findByUserId(long userId) {
        logger.info("Find offers by user id: {}", userId);
        return withDao(OfferDao.class, dao -> dao.findByUserId(userId));
    }

    @Override
    public List<OfferResponseDto> findByUserIdAndStatus(long userId, OfferStatus status) {
        logger.info("Find offers by user id: {} and status: {}", userId, status);
        return withDao(OfferDao.class, dao -> dao.findByUserIdAndStatus(userId, status.name()));
    }

    @Override
    public List<OfferResponseDto> findByStatus(OfferStatus status) {
        logger.info("Find offers by status: {}", status);
        return withDao(OfferDao.class, dao -> dao.findByStatus(status.name()));
    }

    @Override
    public long acceptOffer(long id) {
        validateOfferStatus(id, OfferStatus.OPEN);
        return withDao(OfferDao.class, dao -> dao.updateStatus(id, OfferStatus.ACCEPTED.name()));
    }

    @Override
    public long rejectOffer(long id) {
        validateOfferStatus(id, OfferStatus.OPEN);
        return withDao(OfferDao.class, dao -> dao.updateStatus(id, OfferStatus.REJECTED.name()));
    }

    @Override
    public long completeOffer(long id) {
        validateOfferStatus(id, OfferStatus.ACCEPTED);
        return withDao(OfferDao.class, dao -> dao.updateStatus(id, OfferStatus.COMPLETED.name()));
    }

    @Override
    public long cancelOffer(long id) {
        OfferResponseDto offer = findById(id);

        if (offer.getStatus() != OfferStatus.OPEN && offer.getStatus() != OfferStatus.ACCEPTED)
            throw new ValidationException("Cannot cancel offer with status: " + offer.getStatus());

        return withDao(OfferDao.class, dao -> dao.updateStatus(id, OfferStatus.CANCELLED.name()));
    }

    @Override
    public long updateAmount(long id, OfferUpdateDto updateDto) {
        ValidationUtil.validate(updateDto);
        validateOfferStatus(id, OfferStatus.OPEN);
        return withDao(OfferDao.class, dao -> dao.updateAmount(id, updateDto.getAmount()));
    }

    private void validateOfferStatus(long id, OfferStatus expectedStatus) {
        OfferResponseDto offer = findById(id);

        if (offer.getStatus() != expectedStatus) {
            throw new ValidationException(
                    String.format("Cannot perform this action. Expected status: %s, but found: %s",
                            expectedStatus, offer.getStatus())
            );
        }
    }
}