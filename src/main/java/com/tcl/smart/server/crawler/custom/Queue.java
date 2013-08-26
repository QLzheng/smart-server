package com.tcl.smart.server.crawler.custom;

import java.util.LinkedList;

/**
 * ���ݽṹ����
 * 
 * @author fanjie
 * @date 2013-4-26
 * @param <T>
 */
public class Queue<T> {

	private LinkedList<T> queue = new LinkedList<T>();

	public void enQueue(T t) {
		queue.addLast(t);
	}

	public T deQueue() {
		return queue.removeFirst();
	}

	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	public boolean contians(T t) {
		return queue.contains(t);
	}

	public boolean empty() {
		return queue.isEmpty();
	}
}