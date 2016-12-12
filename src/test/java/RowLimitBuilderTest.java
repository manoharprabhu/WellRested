import com.manoharprabhu.wellrested.DatabaseType;
import com.manoharprabhu.wellrested.service.RowsLimitBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by manoharprabhu on 12/12/2016.
 */
@RunWith(JUnit4.class)
public class RowLimitBuilderTest {

    @Test
    public void testEmpty() {
        JSONObject rowLimit = new JSONObject("{}");
        RowsLimitBuilder rowsLimitBuilder = new RowsLimitBuilder(rowLimit, DatabaseType.MYSQL);
        Assert.assertEquals("", rowsLimitBuilder.build());
    }

    @Test
    public void testLimitZeroTen() {
        JSONObject rowLimit = new JSONObject("{fromRow: 0, count: 10}");
        RowsLimitBuilder rowsLimitBuilder = new RowsLimitBuilder(rowLimit, DatabaseType.MYSQL);
        Assert.assertEquals("LIMIT 0 ,10", rowsLimitBuilder.build());
    }
}
