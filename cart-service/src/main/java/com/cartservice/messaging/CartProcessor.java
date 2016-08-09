package com.cartservice.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by Flaviu Cicio on 09.08.2016.
 */
public interface CartProcessor {
    String INPUT_DELETE = "input_delete";
    String INPUT_UPDATE = "input_update";
    String INPUT_CREATE = "input_create";
    String INPUT_ADD_PRODUCT = "input_add_product";
    String INPUT_REMOVE_PRODUCT = "input_remove_product";
    String INPUT_CONFIRM_ORDER = "input_confirm_order";

    String OUTPUT_UPDATE = "output_update";


    @Input(INPUT_DELETE)
    SubscribableChannel input_delete();

    @Input(INPUT_UPDATE)
    SubscribableChannel input_update();

    @Input(INPUT_CREATE)
    SubscribableChannel input_create();

    @Input(INPUT_ADD_PRODUCT)
    SubscribableChannel input_add_product();

    @Input(INPUT_REMOVE_PRODUCT)
    SubscribableChannel input_remove_product();

    @Input(INPUT_CONFIRM_ORDER)
    SubscribableChannel input_confirm_order();



    @Output(OUTPUT_UPDATE)
    SubscribableChannel output_update();
}
