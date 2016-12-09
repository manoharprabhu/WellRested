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
        Assert.assertEquals("( testcolumn = 5 )", whereClauseBuilder.build());
    }


    @Test
    public void testSimpleLtExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$lt : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals("( testcolumn < 5 )", whereClauseBuilder.build());
    }


    @Test
    public void testSimpleGtExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$gt : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals("( testcolumn > 5 )", whereClauseBuilder.build());
    }

    @Test
    public void testTwoANDExpressions() {
        JSONObject object = new JSONObject("{$and: [{test1: {$gt: 6}},{test2: {$lt: 1}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals("( ( test1 > 6 ) AND ( test2 < 1 ) )", whereClauseBuilder.build());
    }

    @Test
    public void testTwoORExpressions() {
        JSONObject object = new JSONObject("{$or: [{test1: {$eq: 6}},{test2: {$lt: 1}},{test3: {$gt: 23}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals("( ( test1 = 6 ) OR ( test2 < 1 ) OR ( test3 > 23 ) )", whereClauseBuilder.build());
    }

    @Test
    public void testNestedExpressions() {
        JSONObject object = new JSONObject("{$and:[{$or: [{address:{$eq: 11}},{address: {$eq: 12}},{address: {$eq:13}}]},{phone: {$eq: 53}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object);
        Assert.assertEquals("( ( ( address = 11 ) OR ( address = 12 ) OR ( address = 13 ) ) AND ( phone = 53 ) )", whereClauseBuilder.build());
    }
}
