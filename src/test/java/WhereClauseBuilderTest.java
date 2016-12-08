import com.manoharprabhu.wellrested.service.WhereClauseBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by manoharprabhu on 12/9/2016.
 */
@RunWith(JUnit4.class)
public class WhereClauseBuilderTest {

    @Test
    public void testSimpleEqExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$eq : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals(" ( testcolumn = 5 ) ", whereClauseBuilder.build());
    }


    @Test
    public void testSimpleLtExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$lt : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals(" ( testcolumn < 5 ) ", whereClauseBuilder.build());
    }


    @Test
    public void testSimpleGtExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$gt : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals(" ( testcolumn > 5 ) ", whereClauseBuilder.build());
    }
}
