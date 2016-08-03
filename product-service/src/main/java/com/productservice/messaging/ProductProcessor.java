package com.productservice.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


/**
 * Created by Flaviu Cicio on 02.08.2016.
 */
public interface ProductProcessor {
    String OUTPUT_GETALL = "output_getAll";
    String OUTPUT_GET = "output_get";
    String INPUT_DELETE = "input_delete";
    String INPUT_UPDATE = "input_update";
    String INPUT_CREATE = "input_create";

    @Output(OUTPUT_GETALL)
    MessageChannel output_getAll();

    @Output(OUTPUT_GET)
    MessageChannel output_get();

    @Input(INPUT_DELETE)
    SubscribableChannel input_delete();

    @Input(INPUT_UPDATE)
    SubscribableChannel input_update();

    @Input(INPUT_CREATE)
    SubscribableChannel input_create();
}
