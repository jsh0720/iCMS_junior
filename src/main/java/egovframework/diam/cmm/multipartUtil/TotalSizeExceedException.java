package egovframework.diam.cmm.multipartUtil;

public class TotalSizeExceedException extends RuntimeException {
	
	private int maxUploadSize;
	private int requestSize;
	
	public long getMaxUploadSize() {
		return this.maxUploadSize;
	}
	
	public long getRequestSize() {
		return this.requestSize;
	}
	
	public void setMaxUploadSize(long maxUploadSize) {
		this.maxUploadSize = (int) (maxUploadSize / 1024 / 1024);
	}
	
	public void setRequestSize(long requestSize) {
		this.requestSize = (int) (requestSize / 1024 / 1024);
	}
	
	TotalSizeExceedException() {
		
	}
	
	TotalSizeExceedException(String message) {
		super(message);
	}
	
	TotalSizeExceedException(long maxUploadSize, long requestSize) {
		setMaxUploadSize(maxUploadSize);
		setRequestSize(requestSize);
	}
}
