package comp533.joiner;

public interface Joiner {
	void finished();
	void join();	
	void resetThreadCount();
	int getFinishedThreads();
}
