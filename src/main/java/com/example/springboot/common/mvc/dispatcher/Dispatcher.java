package com.example.springboot.common.mvc.dispatcher;

import com.example.springboot.common.mvc.Holder;

public interface Dispatcher {

	void dispatch(Holder request, Holder response);
}
