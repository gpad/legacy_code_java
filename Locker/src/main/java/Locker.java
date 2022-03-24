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
	String _code;
	HttpClient _http_client;
	String _inserted;
	LockerStatus _status = LockerStatus.open;
	public Locker(String code, HttpClient http_client)
	{
		this.code = code;
		this.http_client = http_client;
		http_client.post("www.LockerServer.com", "open");
	}

	public void add_digit(char digit) {
		_inserted += digit;
		_http_client.post("www.LockerServer.com", _inserted);
		if (_inserted.equals(_code)) {
			_status = LockerStatus.open;
			_http_client.post("www.LockerServer.com", "open");
		}
		else {
			_status = LockerStatus.closed;
			_http_client.post("www.LockerServer.com", "closed");
		}
	}
};



