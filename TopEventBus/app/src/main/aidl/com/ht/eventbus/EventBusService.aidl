package com.ht.eventbus;

import com.ht.eventbus.Request;
import com.ht.eventbus.Response;

interface EventBusService {
    Response send(in Request request);
}
