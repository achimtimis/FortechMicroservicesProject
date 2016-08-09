package com.productservice.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


/**
 * Created by Flaviu Cicio on 02.08.2016.
 */
public interface ProductProcessor {
    String INPUT_DELETE = "input_delete";
    String INPUT_UPDATE = "input_update";
    String INPUT_CREATE = "input_create";

    @Input(INPUT_DELETE)
    SubscribableChannel input_delete();

    @Input(INPUT_UPDATE)
    SubscribableChannel input_update();

    @Input(INPUT_CREATE)
    SubscribableChannel input_create();
}
