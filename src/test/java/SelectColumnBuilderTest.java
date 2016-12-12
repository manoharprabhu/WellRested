import com.manoharprabhu.wellrested.service.SelectColumnBuilder;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by manoharprabhu on 12/10/2016.
 */
@RunWith(JUnit4.class)
public class SelectColumnBuilderTest {

    @Test
    public void testEmpty() {
        JSONArray jsonArray = new JSONArray("[]");
        SelectColumnBuilder selectColumnBuilder = new SelectColumnBuilder(jsonArray);
        Assert.assertEquals("*", selectColumnBuilder.build());
    }

    @Test
    public void testStar() {
        JSONArray jsonArray = new JSONArray("[\"*\"]");
        SelectColumnBuilder selectColumnBuilder = new SelectColumnBuilder(jsonArray);
        Assert.assertEquals("*", selectColumnBuilder.build());
    }

    @Test
    public void testSingleColumn() {
        JSONArray jsonArray = new JSONArray("[\"name\"]");
        SelectColumnBuilder selectColumnBuilder = new SelectColumnBuilder(jsonArray);
        Assert.assertEquals("`name`", selectColumnBuilder.build());
    }

    @Test
    public void testMultipleColumn() {
        JSONArray jsonArray = new JSONArray("[\"name\", \"age\", \"address\"]");
        SelectColumnBuilder selectColumnBuilder = new SelectColumnBuilder(jsonArray);
        Assert.assertEquals("`name`,`age`,`address`", selectColumnBuilder.build());
    }

    @Test
    public void testMultipleColumnWithStar() {
        JSONArray jsonArray = new JSONArray("[\"name\", \"age\", \"address\",\"*\"]");
        SelectColumnBuilder selectColumnBuilder = new SelectColumnBuilder(jsonArray);
        Assert.assertEquals("*", selectColumnBuilder.build());
    }
}
