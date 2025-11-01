package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.domain.OfferStatus;
import com.pikolinc.dto.request.OfferCreateDto;
import com.pikolinc.dto.request.OfferUpdateDto;
import com.pikolinc.exceptions.ValidationException;
import com.pikolinc.services.OfferService;
import com.pikolinc.util.ValidationUtil;
import spark.Request;
import spark.Response;

import java.util.Map;

public class OfferApiController {
    private final OfferService offerService;
    private final Gson gson;

    public OfferApiController(OfferService offerService) {
        this.gson = new Gson();
        this.offerService = offerService;
    }

    public Object findAll(Request request, Response response) {
        return this.gson.toJson(this.offerService.findAll());
    }

    public Object findById(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.gson.toJson(this.offerService.findById(id));
    }

    public Object insert(Request request, Response response) {
        ValidationUtil.validateNotEmptyBody(request.body());
        return this.offerService.insert(gson.fromJson(request.body(), OfferCreateDto.class));
    }

    public Object update(Request request, Response response) {
        ValidationUtil.validateNotEmptyBody(request.body());
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.offerService.update(id, gson.fromJson(request.body(), OfferUpdateDto.class));
    }

    public Object delete(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.offerService.delete(id);
    }

    public Object options(Request request, Response response) {
        response.status(200);
        return "";
    }

    // Filters per item
    public Object findByItemId(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.gson.toJson(this.offerService.findByItemId(id));
    }

    public Object findByItemIdAndActive(Request request, Response response) {
        long itemId = ValidationUtil.validateParamFormat(":itemId", request.params(":itemId"), Long.class);
        return this.gson.toJson(this.offerService.findByItemIdAndStatus(itemId, OfferStatus.OPEN));
    }

    // Filters per user
    public Object findByUserId(Request request, Response response) {
        long userId = ValidationUtil.validateParamFormat(":userId", request.params(":userId"), Long.class);
        return this.gson.toJson(this.offerService.findByUserId(userId));
    }

    public Object findByUserIdAndActive(Request request, Response response) {
        long userId = ValidationUtil.validateParamFormat(":userId", request.params(":userId"), Long.class);
        return this.gson.toJson(this.offerService.findByUserIdAndStatus(userId, OfferStatus.OPEN));
    }

    // Filter per status
    public Object findByStatus(Request request, Response response) {
        String statusParam = request.params(":status").toUpperCase();

        ValidationUtil.validateParamFormat(":status", statusParam, OfferStatus.class);

        OfferStatus status;
        try {
            status = OfferStatus.valueOf(statusParam);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status. Valid values: OPEN, ACCEPTED, REJECTED, COMPLETED, CANCELLED");
        }

        return this.gson.toJson(this.offerService.findByStatus(status));
    }

    // Status changes
    public Object acceptOffer(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.offerService.acceptOffer(id);
    }

    public Object rejectOffer(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.offerService.rejectOffer(id);
    }

    public Object completeOffer(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.offerService.completeOffer(id);
    }

    public Object cancelOffer(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.offerService.cancelOffer(id);
    }

    public Object updateAmount(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        ValidationUtil.validateNotEmptyBody(request.body());
        return this.offerService.updateAmount(id, gson.fromJson(request.body(), OfferUpdateDto.class));
    }
}