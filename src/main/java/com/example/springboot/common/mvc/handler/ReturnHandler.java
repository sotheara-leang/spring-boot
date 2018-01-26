package com.example.springboot.common.invocation;

public interface ReturnHandler extends Handler {

	Object execute(Holder request);
}
