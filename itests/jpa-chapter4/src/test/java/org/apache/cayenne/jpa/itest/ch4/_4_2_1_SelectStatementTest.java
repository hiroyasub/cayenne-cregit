begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|itest
operator|.
name|ch4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|itest
operator|.
name|jpa
operator|.
name|EntityManagerCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|itest
operator|.
name|ch4
operator|.
name|entity
operator|.
name|SimpleEntity
import|;
end_import

begin_class
specifier|public
class|class
name|_4_2_1_SelectStatementTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testSelectFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"SimpleEntity"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"SimpleEntity"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"property1"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|15
block|,
literal|"XXX"
block|}
argument_list|)
expr_stmt|;
name|Query
name|query
init|=
name|getEntityManager
argument_list|()
operator|.
name|createQuery
argument_list|(
literal|"select x from SimpleEntity x"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|List
name|result
init|=
name|query
operator|.
name|getResultList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|SimpleEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
operator|(
operator|(
name|SimpleEntity
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getProperty1
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: andrus, 3/11/2007 - this fails.
specifier|public
name|void
name|testSelectFromWhere
parameter_list|()
throws|throws
name|Exception
block|{
comment|// getDbHelper().deleteAll("SimpleEntity");
comment|//
comment|// getDbHelper().insert("SimpleEntity",
comment|// new String[] { "id", "property1" }, new Object[] { 15, "XXX" });
comment|// getDbHelper().insert("SimpleEntity",
comment|// new String[] { "id", "property1" }, new Object[] { 16, "YYY" });
comment|//
comment|// Query query = getEntityManager().createQuery(
comment|// "select x from SimpleEntity x where x.property1 = 'YYY'");
comment|// assertNotNull(query);
comment|// List result = query.getResultList();
comment|// assertNotNull(result);
comment|// assertEquals(1, result.size());
comment|// assertTrue(result.get(0) instanceof SimpleEntity);
comment|// assertEquals("YYY", ((SimpleEntity) result.get(0)).getProperty1());
block|}
block|}
end_class

end_unit

