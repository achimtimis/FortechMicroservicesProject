package com.uiservice.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by Flaviu Cicio on 09.08.2016.
 */
public interface CartProcessor {
    String OUTPUT_DELETE = "output_delete";
    String OUTPUT_UPDATE = "output_update";
    String OUTPUT_CREATE = "output_create";
    String OUTPUT_ADD_PRODUCT = "output_add_product";
    String OUTPUT_REMOVE_PRODUCT = "output_remove_product";
    String OUTPUT_CONFIRM_ORDER = "output_confirm_order";

    @Output(OUTPUT_DELETE)
    SubscribableChannel output_delete();

    @Output(OUTPUT_UPDATE)
    SubscribableChannel output_update();

    @Output(OUTPUT_CREATE)
    SubscribableChannel output_create();

    @Output(OUTPUT_ADD_PRODUCT)
    SubscribableChannel output_add_product();

    @Output(OUTPUT_REMOVE_PRODUCT)
    SubscribableChannel output_remove_product();

    @Output(OUTPUT_CONFIRM_ORDER)
    SubscribableChannel output_confirm_order();
}
