import java.util.Collection;
import java.util.List;

class ExternalService {

    public void sendValues(List<Integer> v) {
        /* send email */
    }
}

class TooComplexToPutUnderTest {
    ExternalService _service;
    List<Integer> _alreadySent;

    public void veryComplexMethod(List<Integer> v) {
        _service.sendValues(v);
        _alreadySent.addAll(v);
    }
}

//
// NEW FEATURE - Verify that vector v contains only uniques
// values before to send
//

class TooComplexToPutUnderTestSproutMethod {
    ExternalService _service;
    List<Integer> _alreadySent;

    // Develop it in TDD
    static public List<Integer> uniqueValue(List<Integer> v) {
        // create uniq vector ...
        return v;
    }

    public void veryComplexMethod(List<Integer> v) {
        _service.sendValues(v);
        _alreadySent.addAll(uniqueValue(v));
    }
}


//
// NEW FEATURE verify that v contains only valid values
// otherwise ignore it
//

class TooComplexToPutUnderTestSproutClass {
    ExternalService _service;
    List<Integer> _alreadySent;
    Validator validator = new Validator();

    public void veryComplexMethod(List<Integer> v) {
        if (!validator.isValid(v)) return;
        _service.sendValues(v);
        _alreadySent.addAll(v);
    }
}

// DEVELOP THIS in TDD
class Validator {
    public boolean isValid(List<Integer> v) {
        return false;
    }
}
