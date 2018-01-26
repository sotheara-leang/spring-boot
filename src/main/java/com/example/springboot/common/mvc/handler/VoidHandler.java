package com.example.springboot.common.mvc.handler;

import com.example.springboot.common.mvc.Holder;

public interface VoidHandler extends Handler {

	void execute(Holder request);
}
