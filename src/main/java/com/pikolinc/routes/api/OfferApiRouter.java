package com.pikolinc.routes.api;

import com.pikolinc.controllers.api.OfferApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class OfferApiRouter implements Router {
    private static final Logger logger = LoggerFactory.getLogger(OfferApiRouter.class);
    private final OfferApiController offerController;

    public OfferApiRouter(OfferApiController offerController) {
        this.offerController = offerController;
    }

    @Override
    public void registerRoutes() {
        logger.info("Registering routes for OfferApiController");

        Spark.path("/api/v1", () -> {
            // Basic operations
            Spark.get("/offers", offerController::findAll);
            Spark.get("/offers/:id", offerController::findById);
            Spark.post("/offers", offerController::insert);
            Spark.put("/offers/:id", offerController::update);
            Spark.delete("/offers/:id", offerController::delete);
            Spark.options("/offers/:id", offerController::options);
            // Filters per item
            Spark.get("/offers/item/:itemId", offerController::findByItemId);
            Spark.get("/offers/item/:itemId/active", offerController::findByItemIdAndActive);

            // Filters per user
            Spark.get("/offers/user/:userId", offerController::findByUserId);
            Spark.get("/offers/user/:userId/active", offerController::findByUserIdAndActive);

            // Filter per status
            Spark.get("/offers/status/:status", offerController::findByStatus);

            // Status changes
            Spark.put("/offers/:id/accept", offerController::acceptOffer);
            Spark.put("/offers/:id/reject", offerController::rejectOffer);
            Spark.put("/offers/:id/complete", offerController::completeOffer);
            Spark.put("/offers/:id/cancel", offerController::cancelOffer);

            // Rebid

            Spark.patch("/offers/:id/amount", offerController::updateAmount);
        });


    }
}
