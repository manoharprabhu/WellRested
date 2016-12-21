import com.manoharprabhu.wellrested.DatabaseType;
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
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( `testcolumn` = 5 )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( [testcolumn] = 5 )", whereClauseBuilder.build());
    }


    @Test
    public void testSimpleLtExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$lt : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( `testcolumn` < 5 )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( [testcolumn] < 5 )", whereClauseBuilder.build());
    }


    @Test
    public void testSimpleGtExpression() {
        JSONObject object = new JSONObject("{testcolumn: {$gt : 5}}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( `testcolumn` > 5 )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( [testcolumn] > 5 )", whereClauseBuilder.build());
    }

    @Test
    public void testTwoANDExpressions() {
        JSONObject object = new JSONObject("{$and: [{test1: {$gt: 6}},{test2: {$lt: 1}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( ( `test1` > 6 ) AND ( `test2` < 1 ) )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( ( [test1] > 6 ) AND ( [test2] < 1 ) )", whereClauseBuilder.build());
    }

    @Test
    public void testTwoORExpressions() {
        JSONObject object = new JSONObject("{$or: [{test1: {$eq: 6}},{test2: {$lt: 1}},{test3: {$gt: 23}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( ( `test1` = 6 ) OR ( `test2` < 1 ) OR ( `test3` > 23 ) )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( ( [test1] = 6 ) OR ( [test2] < 1 ) OR ( [test3] > 23 ) )", whereClauseBuilder.build());
    }

    @Test
    public void testNestedExpressions() {
        JSONObject object = new JSONObject("{$and:[{$or: [{address:{$eq: \"mordor\"}},{address: {$eq: 12}},{address: {$eq:\"address with space\"}}]},{phone: {$eq: 53}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( ( ( `address` = 'mordor' ) OR ( `address` = 12 ) OR ( `address` = 'address with space' ) ) AND ( `phone` = 53 ) )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( ( ( [address] = 'mordor' ) OR ( [address] = 12 ) OR ( [address] = 'address with space' ) ) AND ( [phone] = 53 ) )", whereClauseBuilder.build());
    }


    @Test
    public void testNoClauseExpressions() {
        JSONObject object = new JSONObject("{}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( 1 = 1 )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( 1 = 1 )", whereClauseBuilder.build());
    }

    @Test
    public void testMultipleRootExpressions() {
        JSONObject object = new JSONObject("{$and:[{a: {$eq: 1}}],$or:[{b: {$eq: 2}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( 1 = 0 )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( 1 = 0 )", whereClauseBuilder.build());
    }

    @Test
    public void testMultipleInnerExpressions() {
        JSONObject object = new JSONObject("{$and:[{a: {$eq: 1, $gt: 2}},{c: {$eq: 1}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( ( 1 = 0 ) AND ( `c` = 1 ) )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( ( 1 = 0 ) AND ( [c] = 1 ) )", whereClauseBuilder.build());
    }

    @Test
    public void testUndefinedOperatorExpressions() {
        JSONObject object = new JSONObject("{$and:[{a: {$lol: 1}},{c: {$eq: 1}}]}");
        WhereClauseBuilder whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.MYSQL);
        Assert.assertEquals("( ( 1 = 0 ) AND ( `c` = 1 ) )", whereClauseBuilder.build());
        whereClauseBuilder = new WhereClauseBuilder(object, DatabaseType.SQLSERVER);
        Assert.assertEquals("( ( 1 = 0 ) AND ( [c] = 1 ) )", whereClauseBuilder.build());
    }
}
