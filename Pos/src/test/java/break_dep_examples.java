import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


// 1 - Pass Null Ptr

// 2 - Null Pattern / Stub Object

class Object1 {
    void f(int v) {
        throw new RuntimeException("xxx");
    }
}

class Object2 {
    int f(int v) {
        throw new RuntimeException("xxx");
    }
}

class ObjectToTest {
    private final Object1 obj1;
    private final Object2 obj2;

    ObjectToTest(Object1 obj1, Object2 obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    int makeSomeComplexCalc(int v) {
        // don't use _obj2;
        return v * 2;
    }

    protected void connectToDb() {
        throw new RuntimeException("connect to DB");
    }

    protected int getFromDb() {
        throw new RuntimeException("get from DB");
    }

    int funcToTest(int v) {
        obj1.f(v);

        // make some complex code that you want test
        int res = makeSomeComplexCalc(v);

        return v * 2;
    }

    int otherFuncToTest(int v) {
        obj1.f(v);

        connectToDb();
        return getFromDb() * v;
    }

    int f3(int v) {
        return obj2.f(v * 2);
    }
}

class NullObject1 extends Object1 {
    void f(int v) {
    }
}

//// Rename class Object2 -> Object2Impl and extract interface Object2
//interface Object2 {
//	 int f(int v);
//};
//
//class Object2Impl implements Object2 {
//	public int f(int v) {
//		throw new RuntimeException("xxx");
//	}
//};
//
//class NullObject2 implements Object2{
//
//    @Override
//    public int f(int v) {
//        return v;
//    }
//}

class BreakDepTechNull {
    @Test
    void testNullObj1() {
        NullObject1 obj1 = new NullObject1();
        ObjectToTest obj = new ObjectToTest(obj1, null);

        assertEquals(obj.funcToTest(1), 2);
    }

    @Test
    void testNullObj2() {
        NullObject1 obj1 = new NullObject1();
        ObjectToTest obj = new ObjectToTest(obj1, new Object2());
//        ObjectToTest obj = new ObjectToTest(obj1, new NullObject2());

        assertEquals(obj.f3(1), 2);
    }

}


// Extract and Override Call

class TestingObjectTotest extends ObjectToTest {
    private int v = -1;

    TestingObjectTotest(Object1 obj1, Object2 obj2, int v) {
        super(obj1, obj2);
        this.v = v;
    }

    protected void connectToDb() {

    }

    protected int getFromDb() {
        return this.v;
    }
}

class BreakDepTechExtractAndOverride {
    @Test
    public void ExtractAnOverride() {
        NullObject1 obj1 = new NullObject1();
        TestingObjectTotest obj = new TestingObjectTotest(obj1, null, 42);

        assertEquals(obj.otherFuncToTest(2), 84);
    }
}

// Extract Interface 

class BreakDepTechExtractInterface {
    @Test
    void ExtractInterface() {
        int v = 4;
        var mockObj2 = mock(Object2.class);
        when(mockObj2.f(v * 2)).thenReturn(v * 2 * 2);
        ObjectToTest obj = new ObjectToTest(null, mockObj2);

        var ret = obj.f3(v);

        assertEquals(ret, v * 2 * 2);
        verify(mockObj2).f(v * 2);
    }
}

// Exposing Sensing Variable

class AnotherToTest {
    private int _even = 0;
    private int _odd = 0;

    void longAndComplexMethod(int value) {
        if ((value % 2) != 0) {
            // do a lot of thing
            ++_even;
        } else {
            //
            // do a lot of thing
            //
            ++_odd;

        }
    }

    void exec() {
        // make some work with _odd _even
    }
}

class BreakDepTechExposeSensingVariable {
    @Test
    void ExposeSensingVariable() {
        AnotherToTest obj = new AnotherToTest();

        obj.longAndComplexMethod(44);

//		assertEquals(obj._odd, 1);
//		assertEquals(obj._even, 0);
    }
}