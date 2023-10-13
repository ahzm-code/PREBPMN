package fr.blind.anonymous.rbpmn.enforcement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.enforcement.Buffer;

class BufferTest {

	@Test
	void testBuffer() {
		
		Buffer<Integer> queue = new Buffer<>();
		queue.add(1);
		queue.add(2);
		queue.add(3);
		queue.poll();
		queue.peek();
		System.out.println(queue.size());
		System.out.println(queue.toString());
	}

}
