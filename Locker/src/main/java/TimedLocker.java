import java.time.Duration;
import java.time.LocalDateTime;

interface TimedLockerStatus {
    void addDigit(char digit);

    void close();
}

class TimedLockerOpen implements TimedLockerStatus {
    public static final int TIME_BEFORE_CLOSE = 3;
    TimedLocker _timedLocker;

    public TimedLockerOpen(TimedLocker timedLocker) {
        this._timedLocker = timedLocker;
    }

    public void addDigit(char digit) {
        System.out.println(String.format("Ignore the character %c", digit));
    }

    public void close() {
        _timedLocker.setClose(Duration.ofSeconds(TIME_BEFORE_CLOSE));
    }
}

class TimedLockerClose implements TimedLockerStatus {
    public static final int MAX_INSERTED_LENGHT = 255;
    TimedLocker _timedLocker;

    public TimedLockerClose(TimedLocker timedLocker) {
        this._timedLocker = timedLocker;
    }

    private boolean isTooLong() {
        return _timedLocker._inserted.length() > Math.max(_timedLocker._code.length(), MAX_INSERTED_LENGHT);
    }

    public void addDigit(char digit) {

        _timedLocker._inserted += digit;
        if (_timedLocker._inserted.equals(_timedLocker._code)) {
            _timedLocker.setOpen();
        } else {
            if (isTooLong()) {
                System.out.println("Too many characters !!!");
                _timedLocker._inserted = "";
            }
        }
    }

    public void close() {
        System.out.println("Locker already closed");
    }
}


public class TimedLocker {
    String _code;
    String _inserted;
    LocalDateTime _open_at;
    TimedLockerStatus _state = new TimedLockerOpen(this);

    public TimedLocker(String code) {
        this._code = code;
    }

    void setOpen() {
        _inserted = "";
        System.out.println("Locker is open");
        _open_at = LocalDateTime.now();
        _state = new TimedLockerOpen(this);
    }

    void setClose(Duration wait) {
        var elapsed = Duration.between(LocalDateTime.now(), _open_at);
        if (elapsed.compareTo(wait) == -1) {
            _state = new TimedLockerClose(this);
            System.out.println("Locker is closed");
        } else {
            System.out.println("You have to wait 10 secs before to close the locker");
        }
    }

    public TimedLocker addDigit(char digit) {
        this._state.addDigit(digit);
        return this;
    }

    public void close() {
        _state.close();
    }
}
