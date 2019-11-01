package Proj_assgn.Log;

public class MyLogClass {
	 
    private String date;
    private String timeStamp;
    private String loglevel;
    private String lineNumber;
    private String message;
    private String logId;
    private String className;
 

	public String getDate() {
		return date;
	}

	public void setDate(String dt) {
		date = dt;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStmp) {
		timeStamp = timeStmp;
	}

	public String getloglevel() {
		return loglevel;
	}

	public void setloglevel(String loglvl) {
		loglevel = loglvl;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lNumber) {
		lineNumber = lNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		message = msg;
	}

	 @Override
	    public String toString() {
	        return String.format("Log1{date='%s', timeStamp='%s', loglevel='%s', lineNumber='%s', message='%s'}", 
	        		date, timeStamp,loglevel,lineNumber,message);
	    }

	public String getLogId() {
		return logId;
	}

	public void setLogId(String lgId) {
		logId = lgId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String clsName) {
		className = clsName;
	}
}