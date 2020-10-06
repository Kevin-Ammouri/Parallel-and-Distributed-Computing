// Class object that contains all data needed for logging the 
// linearizable points.
// Contains: A time stamp in nanosec, a keyvalue and 
// which method call that was used.
public class LogContent<T> {
	public String call;
	public T value;
	public long timestamp;
	
	public LogContent(String call, T value, long timestamp) {
		this.call = call;
		this.value = value;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "{Call: " + call + ", value: " + value + ", timestamp: " + timestamp + " ns}";
    }
}