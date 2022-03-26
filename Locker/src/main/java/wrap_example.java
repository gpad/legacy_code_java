import java.util.ArrayList;
import java.util.List;

class PayRecord {
	public int amount;
};

class ComplexStruct {
	private int amount;

    ComplexStruct(int amount) {
        this.amount = amount;
    }
};

class ComplexService{

    public void send(List<ComplexStruct> payments) {
		/* send payments */
	}
};

class PaymentService{
	private ComplexService _complexService;

	ComplexStruct toComplexStruct(PayRecord r) {
		return new ComplexStruct(r.amount);
	}

    public void pay(List<PayRecord> payments) {
		List<ComplexStruct> v = new ArrayList<>();
		for (var p : payments)
		{
			var cs = toComplexStruct(p);
			v.add(cs);
		}
		_complexService.send(v);
	}
};

//
// NEW FEATURE - Save on file the PayRecord before to send!!!
//

class PaymentServiceWrapMethod {
	private ComplexService _complexService;

	ComplexStruct toComplexStruct(PayRecord r) {
		return new ComplexStruct(r.amount);
	}

    public void pay(List<PayRecord> payments) {
		saveOnFile(payments);
		dispatchToExternal(payments);
	}

	void dispatchToExternal(List<PayRecord> payments) {
		List<ComplexStruct> v = new ArrayList<>();
		for (var p : payments)
		{
			var cs = toComplexStruct(p);
			v.add(cs);
		}
		_complexService.send(v);
	}

	// develop this in TDD!!!
	void saveOnFile(List<PayRecord> payments) {
	}
};

//
// NEW FEATURE - Save on file the PayRecord before to send!!!
//

class PaymentServiceWrapClass extends PaymentService {

    public void pay(List<PayRecord> payments)  {
		saveOnFile(payments);
		super.pay(payments);
	}

	// Develop This in TDD !!!
	void saveOnFile(List<PayRecord> payments) {
	}
};

