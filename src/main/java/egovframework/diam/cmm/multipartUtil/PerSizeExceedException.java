package egovframework.diam.cmm.multipartUtil;

public class PerSizeExceedException extends RuntimeException {
	
	private int maxUploadSizePerFile;
	
	public long getMaxUploadSizePerFile() {
		return this.maxUploadSizePerFile;
	}


	public void setMaxUploadSizePerFile(long maxUploadSizePerFile) {
		this.maxUploadSizePerFile = (int) (maxUploadSizePerFile / 1024 / 1024);
	}


	PerSizeExceedException() {
		
	}

	PerSizeExceedException(String message) {
		super(message);
	}

	PerSizeExceedException(long maxUploadSizePerFile) {
		setMaxUploadSizePerFile(maxUploadSizePerFile);
	}
}
