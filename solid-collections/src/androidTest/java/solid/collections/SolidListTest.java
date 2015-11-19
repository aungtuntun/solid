package solid.collections;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import testkit.ParcelFn;

import static org.junit.Assert.assertEquals;
import static solid.stream.Stream.of;

@RunWith(AndroidJUnit4.class)
public class SolidListTest {
    @Test
    public void testParcelable() throws Exception {
        SolidList<Integer> list = of(1, 2, 3).collect(SolidList::new);
        assertEquals(list, ParcelFn.unmarshall(ParcelFn.marshall(list)));
    }
}