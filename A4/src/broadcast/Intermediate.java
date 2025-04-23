package broadcast;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Intermediate {
	private ByteBuffer byteBuffer;
	private SocketChannel socketChannel;
	
	public Intermediate(ByteBuffer byteBuffer, SocketChannel socketChannel) {
		this.byteBuffer = byteBuffer;
		this.socketChannel = socketChannel;
	}
	
	public ByteBuffer getByteBuffer() {
		return byteBuffer; 
	}
	
	public SocketChannel getSockerChannel() {
		return socketChannel;
	}
}
