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
operator|.
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|DataObjectUtils
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
name|ObjectContext
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
name|cpa
operator|.
name|CPAContextCase
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
name|cpa
operator|.
name|defaults
operator|.
name|client
operator|.
name|DefaultsTable3
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
name|cpa
operator|.
name|defaults
operator|.
name|client
operator|.
name|DefaultsTable4
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
name|RefreshQuery
import|;
end_import

begin_class
specifier|public
class|class
name|RuntimeLoaderDelegateClientDefaultsLoadingTest
extends|extends
name|CPAContextCase
block|{
comment|/** 	 * Ensures that one-way mapping can be used from the client. 	 */
specifier|public
name|void
name|testUpdateImplicitClientToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"defaults_table4"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"defaults_table3"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"defaults_table3"
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
literal|"defaults_table3"
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
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"defaults_table4"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"defaults_table3_id"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|1
block|}
argument_list|)
expr_stmt|;
name|ObjectContext
name|clientContext
init|=
name|getClientContext
argument_list|()
decl_stmt|;
name|DefaultsTable4
name|o
init|=
operator|(
name|DefaultsTable4
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|clientContext
argument_list|,
name|DefaultsTable4
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"id"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|DefaultsTable3
name|o1
init|=
operator|(
name|DefaultsTable3
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|clientContext
argument_list|,
name|DefaultsTable3
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"id"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|DefaultsTable3
name|o2
init|=
operator|(
name|DefaultsTable3
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|clientContext
argument_list|,
name|DefaultsTable3
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"id"
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o1
operator|.
name|getDefaultTable4s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|o2
operator|.
name|getDefaultTable4s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|o2
operator|.
name|addToDefaultTable4s
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o2
operator|.
name|getDefaultTable4s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|clientContext
operator|.
name|modifiedObjects
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|clientContext
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o2
operator|.
name|getDefaultTable4s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// there is a bug in RefreshQuery that fails to invalidate to-many on
comment|// the client - so working around it be creating a new context; still
comment|// running the query though to refresh the server
name|clientContext
operator|.
name|performQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|()
argument_list|)
expr_stmt|;
name|clientContext
operator|=
name|getClientContext
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|o1
operator|=
operator|(
name|DefaultsTable3
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|clientContext
argument_list|,
name|DefaultsTable3
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"id"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|o2
operator|=
operator|(
name|DefaultsTable3
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|clientContext
argument_list|,
name|DefaultsTable3
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"id"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o2
operator|.
name|getDefaultTable4s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|o1
operator|.
name|getDefaultTable4s
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

