package com.uiservice.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;


/**
 * Created by Flaviu Cicio on 02.08.2016.
 */
public interface ProductProcessor {
    String OUTPUT_DELETE = "output_delete";
    String OUTPUT_UPDATE = "output_update";
    String OUTPUT_CREATE = "output_create";

    @Output(OUTPUT_DELETE)
    SubscribableChannel output_delete();

    @Output(OUTPUT_UPDATE)
    SubscribableChannel output_update();

    @Output(OUTPUT_CREATE)
    SubscribableChannel output_create();
}
