package com.uiservice.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


/**
 * Created by Flaviu Cicio on 02.08.2016.
 */
public interface ProductProcessor {
    String INPUT_GETALL = "input_getAll";
    String INPUT_GET = "input_get";
    String OUTPUT_DELETE = "output_delete";
    String OUTPUT_UPDATE = "output_update";
    String OUTPUT_CREATE = "output_create";

    @Input(INPUT_GETALL)
    MessageChannel input_getAll();

    @Input(INPUT_GET)
    MessageChannel input_get();

    @Output(OUTPUT_DELETE)
    SubscribableChannel output_delete();

    @Output(OUTPUT_UPDATE)
    SubscribableChannel output_update();

    @Output(OUTPUT_CREATE)
    SubscribableChannel output_create();
}
