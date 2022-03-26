import java.util.Map;

interface LogService {
	void appendTo(String filename, String content);
};

class HttpClient
{
	public void post(String url, String body) {
		throw new RuntimeException("not implemented");
	}

	public void post(String url, Map<String, String> values) {
		throw new RuntimeException("not implemented");
	}
};

enum LockerStatus { open, closed };

public class Locker
{
	private final String code;
	private final HttpClient http_client;
	private String inserted;
	private LockerStatus status = LockerStatus.open;

	public Locker(String code, HttpClient http_client)
	{
		this.code = code;
		this.http_client = http_client;
		http_client.post("www.LockerServer.com", "open");
	}

	public void add_digit(char digit) {
		this.inserted += digit;
		this.http_client.post("www.LockerServer.com", this.inserted);
		if (this.inserted.equals(this.code)) {
			this.status = LockerStatus.open;
			this.http_client.post("www.LockerServer.com", "open");
		}
		else {
			this.status = LockerStatus.closed;
			this.http_client.post("www.LockerServer.com", "closed");
		}
	}
};



