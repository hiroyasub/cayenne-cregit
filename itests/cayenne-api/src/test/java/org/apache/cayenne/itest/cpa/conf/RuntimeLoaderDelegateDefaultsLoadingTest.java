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
name|DefaultsTable2
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
name|map
operator|.
name|DbEntity
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
name|map
operator|.
name|ObjEntity
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
name|RuntimeLoaderDelegateDefaultsLoadingTest
extends|extends
name|CPAContextCase
block|{
specifier|public
name|void
name|testLoadedReverseDb
parameter_list|()
block|{
name|DbEntity
name|table1
init|=
name|getContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"defaults_table1"
argument_list|)
decl_stmt|;
name|DbEntity
name|table2
init|=
name|getContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"defaults_table2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|table1
operator|.
name|getAnyRelationship
argument_list|(
name|table2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|table1
operator|.
name|getAnyRelationship
argument_list|(
name|table2
argument_list|)
operator|.
name|isRuntime
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|table2
operator|.
name|getAnyRelationship
argument_list|(
name|table1
argument_list|)
operator|.
name|isRuntime
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLoadedReverseObj
parameter_list|()
block|{
name|ObjEntity
name|class1
init|=
name|getContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"DefaultsTable1"
argument_list|)
decl_stmt|;
name|ObjEntity
name|class2
init|=
name|getContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"DefaultsTable2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|class1
operator|.
name|getAnyRelationship
argument_list|(
name|class2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|class1
operator|.
name|getAnyRelationship
argument_list|(
name|class2
argument_list|)
operator|.
name|isRuntime
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|class2
operator|.
name|getAnyRelationship
argument_list|(
name|class1
argument_list|)
operator|.
name|isRuntime
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"defaults_table2"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"defaults_table1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"defaults_table1"
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
literal|"defaults_table2"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"defaults_table1_id"
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
name|DefaultsTable2
name|o
init|=
operator|(
name|DefaultsTable2
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|DefaultsTable2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o
operator|.
name|getToTable1
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|o
operator|.
name|getToTable1
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateImplicitToOne
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
name|getContext
argument_list|()
argument_list|,
name|DefaultsTable4
operator|.
name|class
argument_list|,
literal|1
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
name|getContext
argument_list|()
argument_list|,
name|DefaultsTable3
operator|.
name|class
argument_list|,
literal|1
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
name|getContext
argument_list|()
argument_list|,
name|DefaultsTable3
operator|.
name|class
argument_list|,
literal|2
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
name|getContext
argument_list|()
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
name|getContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|RefreshQuery
argument_list|()
argument_list|)
expr_stmt|;
comment|// note that the old to-many is only refreshed after invalidation with
comment|// RefreshQuery... should this be treated as a bug?
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
block|}
block|}
end_class

end_unit

