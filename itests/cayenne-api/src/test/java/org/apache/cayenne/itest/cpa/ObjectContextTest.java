begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|itest
operator|.
name|cpa
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_class
specifier|public
class|class
name|ObjectContextTest
extends|extends
name|CPAContextCase
block|{
specifier|public
name|void
name|testPerformQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"entity1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"name"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"X"
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"entity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"name"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|"Y"
block|}
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Entity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|getContext
argument_list|()
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

