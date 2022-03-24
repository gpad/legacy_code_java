//
// Adapt Parameter && Reveal Interface
//

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

interface ValueExtractor {
    int getValue();
}

interface NamesStore {
    void save_name(int k, String s);
}

// After Break

class ImpossibleToInstantiate {

    ImpossibleToInstantiate() {
        throw new RuntimeException("destroy!!");
    }

    int getValue() {
        return 666;
    }

    void setValue(int value) {
    }

    // a lot of other methods!!!
}

class ObjectUnderTest {
    boolean action(ImpossibleToInstantiate arg1) {
        return arg1.getValue() == 42;
    }
}

class ImpossibleToInstantiateValueExtractor implements ValueExtractor {
    private final ImpossibleToInstantiate obj;

    ImpossibleToInstantiateValueExtractor(ImpossibleToInstantiate obj) {
        this.obj = obj;
    }

    public int getValue() {
        return obj.getValue();
    }
}

class ObjectUnderTest2 {
    boolean action(ValueExtractor arg1) {
        return arg1.getValue() == 42;
    }
}

//
// Pull Up Feature (Opposite of Push Down Dependency)
//

class BreakDepTechPartIIAdaptParameter {
    @Test
    public void AdaptParameter() {
        var obj = new ObjectUnderTest();
        ImpossibleToInstantiate dep = new ImpossibleToInstantiate();

        var ret = obj.action(dep);

        assertTrue(ret);
    }

    @Test
    public void AdaptParameter2() {
        var obj = new ObjectUnderTest2();
        var dep = mock(ValueExtractor.class);
        when(dep.getValue()).thenReturn(42);

        var ret = obj.action(dep);

        assertTrue(ret);
    }
}

class ObjToTest {
    private final List<Integer> items = new ArrayList<>();

    private void saveOnDB(int v) {
        throw new RuntimeException("save on DB");
    }

    public ObjToTest add(int v) {
        items.add(v);
        saveOnDB(v);
        return this;
    }

    List<Integer> getCurrentValues() {
        return items;
    }
}

//class ObjToTest extends ObjToTestService
//{
//	void saveOnDB(int v)
//	{ /* SAVE ON DB */	}
//
//	ObjToTest add(int v)
//	{
//		super.add(v);
//		saveOnDB(v);
//		return this;
//	}
//};

class ObjToTestService {
    private final List<Integer> items = new ArrayList<>();

    ObjToTestService add(int v) {
        items.add(v);
        return this;
    }

    List<Integer> getCurrentValues() {
        return items;
    }
}

//
// Introduce Instance Delegator
//

class BreakDepTechPartIIPullUpFeature {

    @Test
    void PullUpFeature() {
        ObjToTest obj = new ObjToTest();
        obj.add(1).add(2).add(3);

        var items = obj.getCurrentValues();

        assertArrayEquals(items.toArray(), new Integer[]{1, 2, 3});
    }
}


class ExternalService {

    public static void service1(String name, int value) {
        throw new RuntimeException("call real service");
    }

    void service1_new(String name, int value) {
        ExternalService.service1(name, value);
    }
}

class SomeClass {
    private final String name;

    SomeClass(String name) {
        this.name = name;
    }

    void function(int v) {
        ExternalService.service1(name, v * 2);
    }

    void function(int v, ExternalService service) {
        service.service1_new(name, v * 2);
    }
}

class MockExternalService extends ExternalService {
    @Override
    void service1_new(String name, int value) {
        //do nothing
    }
}

class BreakDepTechPartIIIntroduceInstanceDelegator {

    @Test
    void IntroduceInstanceDelegator() {
        SomeClass c = new SomeClass("xyz");
        int v = 42;

        c.function(v);
//        var srv = new MockExternalService();
//        c.function(v, srv);

    }
}

//
// Replace Global Reference With Getter
//

class ObjectThatUseGlobal {
    private final int id;

    ObjectThatUseGlobal(int id) {
        this.id = id;
    }

    ObjectThatUseGlobal add(String s) {
        GlobalClass.GetInstance().save(id, s);
        return this;
    }

    List<String> build() {
        return GlobalClass.GetInstance().load(id);
    }
}

class GlobalClass {
    static GlobalClass _This;

    protected GlobalClass() {
    }

    public static GlobalClass GetInstance() {
        return _This;
    }

    void save(int id, String name) {
        throw new RuntimeException("save on DB");
    }

    List<String> load(int id) {
        throw new RuntimeException("load on DB");
    }
}

//class ObjectThatUseGlobal {
//    private final int id;
//
//    public ObjectThatUseGlobal(int id) {
//        this.id = id;
//    }
//
//    protected GlobalClass GetGlobalInstance() {
//        return GlobalClass.GetInstance();
//    }
//
//    public ObjectThatUseGlobal add(String s) {
//        GetGlobalInstance().save(id, s);
//        return this;
//    }
//
//    List<String> build() {
//        return GetGlobalInstance().load(id);
//    }
//}

class FakeGlobalClass extends GlobalClass {
    private final Map<Integer, List<String>> values = new HashMap<>();

    @Override
    void save(int id, String name) {
        var it = values.get(id);
        if (it == null) {
            values.put(id, new ArrayList<>(Arrays.asList(name)));
        } else {
            it.add(name);
        }
    }

    @Override
    List<String> load(int id) {
        var it = values.get(id);
        if (it == null) {
            return new ArrayList<>();
        }
        return it;
    }
}

class TestingObjectThatUseGlobal extends ObjectThatUseGlobal {
    GlobalClass _global;

    TestingObjectThatUseGlobal(int id) {

        super(id);
        _global = GlobalClass.GetInstance();

    }

    void SetGlobalInstance(GlobalClass global) {
        _global = global;
    }

//    @Override
//    protected GlobalClass GetGlobalInstance() {
//        return _global;
//    }
}

class BreakDepTechPartIIRepleaceGlobalReferenceWithGetter {

    @Test
    void ReplaceGlobalReferenceWithGetter() {
//        ObjectThatUseGlobal obj = new ObjectThatUseGlobal(666);
        FakeGlobalClass global = new FakeGlobalClass();
        TestingObjectThatUseGlobal obj = new TestingObjectThatUseGlobal(666);
        obj.SetGlobalInstance(global);

        var values = obj.add("a").add("a").add("b").build();

        assertArrayEquals(values.toArray(), new String[]{"a", "a", "b"});
    }
}

//
// Push Down Feature
//

//
// Break Out Method Object
// If your method is large or does use instance data and methods, consider using Break Out Method Object
//

class ObjectToBreak {
    void save_name(int k, String s) {
        throw new RuntimeException("do something strange!!!");
    }

    public void execute(List<Integer> values, List<String> names) {
        for (var v : values) {
            for (int i = 0; i < Math.min(v, names.size()); i++) {
                save_name(v + i, names.get(i));
            }
        }
    }
}

class Calculator {
    private final NamesStore obj;

    Calculator(NamesStore obj) {
        this.obj = obj;
    }

    void execute(List<Integer> values, List<String> names) {
        for (var v : values) {
            for (int i = 0; i < Math.min(v, names.size()); i++) {
                obj.save_name(v + i, names.get(i));
            }
        }
    }

}

//class ObjectToBreak implements NamesStore {
//    public void save_name(int k, String s) {
//        throw new RuntimeException("do something strange!!!");
//    }
//
//    void execute(List<Integer> values, List<String> names) {
//        Calculator calc = new Calculator(this);
//        calc.execute(values, names);
//    }
//}

class BreakDepTechPartIIBreakOutMethodObject {
    @Test
    void BreakOutMethodObject() {
        ObjectToBreak obj = new ObjectToBreak();
        obj.execute(Arrays.asList(1, 2, 3, 4), Arrays.asList("a", "b", "c"));

        // vs

//		var store = mock(NamesStore.class);
//		Calculator calc = new Calculator(store);
//
//		calc.execute(Arrays.asList(1, 2, 3, 4), Arrays.asList("a", "b", "c"));
//
//		verify(store).save_name(1, "a");
//
//		verify(store).save_name(2, "a");
//		verify(store).save_name(3, "b");
//
//		verify(store).save_name(3, "a");
//		verify(store).save_name(4, "b");
//		verify(store).save_name(5, "c");
//
//		verify(store).save_name(4, "a");
//		verify(store).save_name(5, "b");
//		verify(store).save_name(6, "c");
    }
}
