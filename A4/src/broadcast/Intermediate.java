package broadcast;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Intermediate {
	private ByteBuffer byteBuffer;
	private SocketChannel socketChannel;
	private int aLength;
	
	public Intermediate(ByteBuffer byteBuffer, SocketChannel socketChannel, int aLength) {
		this.byteBuffer = byteBuffer;
		this.socketChannel = socketChannel;
		this.aLength = aLength;
	}
	
	public ByteBuffer getByteBuffer() {
		return byteBuffer; 
	}
	
	public SocketChannel getSockerChannel() {
		return socketChannel;
	}
	
	public int getALength() {
		return aLength;
	}
}
