package com.choc.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EmailSenderPool extends ThreadPoolExecutor {

	private static EmailSenderPool pool;
	
	private static final int DEFAULT_CORE_SIZE = 5;
	private static final int DEFAULT_MAX_SIZE = 5;
	private static final long DEFAULT_TIMEOUT = 2000;
	
	public static EmailSenderPool getInstance() {
		if(pool == null) {
			pool = new EmailSenderPool();
		}
		return pool;
	}
	
	private EmailSenderPool() {
		super(DEFAULT_CORE_SIZE,DEFAULT_MAX_SIZE,DEFAULT_TIMEOUT,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
	}

}
