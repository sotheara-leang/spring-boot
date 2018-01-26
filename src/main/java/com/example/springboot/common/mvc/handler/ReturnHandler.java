package com.example.springboot.common.mvc.handler;

import com.example.springboot.common.mvc.Holder;

public interface ReturnHandler extends Handler {

	Object execute(Holder request);
}
