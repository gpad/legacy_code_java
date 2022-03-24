// Algorithm for characterization tests :
// 1 - Use a piece of code in a test harness.
// 2 - Write an assertion that you know will fail.
// 3 - Let the failure tell you what the behavior is.
// 4 - Change the test so that it expects the behavior that the code produces.
// 5 - Repeat.

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

class UnknownClass
{
	Map<Integer, String> do_some_magic(List<Integer> values, List<Integer> discarded) {

        var uniqueValues = new HashSet<Integer>(values);
		Map<Integer, String> ret = new HashMap<>();
		for (var value : uniqueValues) {
			if (value % 2 == 0) {
				ret.put(value, String.valueOf(value));
			}
            else{
                discarded.add(value);
            }
		}
		return ret;
	}
};

// Algorithm for characterization tests :
// 1 - Use a piece of code in a test harness.
// 2 - Write an assertion that you know will fail.
// 3 - Let the failure tell you what the behavior is.
// 4 - Change the test so that it expects the behavior that the code produces.
// 5 - Repeat.


























//class UnknownClassTest {
//    @Test
//    void returnPairOfNumberAndString() {
//        var discarded = new ArrayList<Integer>();
//        var obj = new UnknownClass();
//
//        var ret = obj.do_some_magic(Arrays.asList(new Integer[]{1, 2}), discarded);
//
//        assertEquals(ret, Map.of(2, "2"));
//    }
//
//    @Test
//    void discardOddNumber() {
//        var discarded = new ArrayList<Integer>();
//        var obj = new UnknownClass();
//
//        var ret = obj.do_some_magic(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}), discarded);
//
//        assertArrayEquals(discarded.toArray(), new Integer[]{1, 3, 5});
//    }
//
//    @Test
//    void insertInMapOnlyEvenNumber() {
//        var discarded = new ArrayList<Integer>();
//        var obj = new UnknownClass();
//
//        var ret = obj.do_some_magic(Arrays.asList(new Integer[]{1, 2, 3, 4, 5}), discarded);
//
//        assertEquals(ret, Map.of(2, "2", 4, "4"));
//    }
//
//    @Test
//    void insertInDiscardedUniqueValues() {
//        var discarded = new ArrayList<Integer>();
//        var obj = new UnknownClass();
//
//        var ret = obj.do_some_magic(Arrays.asList(new Integer[]{1, 2, 3, 3, 5, 5, 5}), discarded);
//
//        assertArrayEquals(discarded.toArray(), new Integer[]{1, 3, 5});
//    }
//
//    @Test
//    void insertInDiscardedOrderedValues() {
//        var discarded = new ArrayList<Integer>();
//        var obj = new UnknownClass();
//
//        var ret = obj.do_some_magic(Arrays.asList(new Integer[]{5, 3, 1}), discarded);
//
//        assertArrayEquals(discarded.toArray(), new Integer[]{1, 3, 5});
//    }
//}
//
//
