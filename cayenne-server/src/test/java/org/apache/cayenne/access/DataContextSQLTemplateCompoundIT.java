begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
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
name|Cayenne
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
name|di
operator|.
name|Inject
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
name|CapsStrategy
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
name|SQLTemplate
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|testdo
operator|.
name|compound
operator|.
name|CompoundFkTestEntity
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
name|testdo
operator|.
name|compound
operator|.
name|CompoundPkTestEntity
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|COMPOUND_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextSQLTemplateCompoundIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tCompoundPkTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tCompoundFkTest
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_FK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|.
name|setColumns
argument_list|(
literal|"KEY1"
argument_list|,
literal|"KEY2"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOUND_FK_TEST"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|setColumns
argument_list|(
literal|"PKEY"
argument_list|,
literal|"F_KEY1"
argument_list|,
literal|"F_KEY2"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createTwoCompoundPKsAndCompoundFKsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tCompoundPkTest
operator|.
name|insert
argument_list|(
literal|"a1"
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|tCompoundPkTest
operator|.
name|insert
argument_list|(
literal|"b1"
argument_list|,
literal|"b2"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|insert
argument_list|(
literal|6
argument_list|,
literal|"a1"
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|tCompoundFkTest
operator|.
name|insert
argument_list|(
literal|7
argument_list|,
literal|"b1"
argument_list|,
literal|"b2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBindObjectEqualCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoCompoundPKsAndCompoundFKsDataSet
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pk
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|CompoundPkTestEntity
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|String
name|template
init|=
literal|"SELECT * FROM COMPOUND_FK_TEST t0"
operator|+
literal|" WHERE #bindObjectEqual($a [ 't0.F_KEY1', 't0.F_KEY2' ] [ 'KEY1', 'KEY2' ] ) ORDER BY PKEY"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|query
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|CompoundFkTestEntity
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CompoundFkTestEntity
name|p
init|=
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBindObjectNotEqualCompound
parameter_list|()
throws|throws
name|Exception
block|{
name|createTwoCompoundPKsAndCompoundFKsDataSet
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|pk
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY1_PK_COLUMN
argument_list|,
literal|"a1"
argument_list|)
expr_stmt|;
name|pk
operator|.
name|put
argument_list|(
name|CompoundPkTestEntity
operator|.
name|KEY2_PK_COLUMN
argument_list|,
literal|"a2"
argument_list|)
expr_stmt|;
name|CompoundPkTestEntity
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|pk
argument_list|)
decl_stmt|;
name|String
name|template
init|=
literal|"SELECT * FROM COMPOUND_FK_TEST t0"
operator|+
literal|" WHERE #bindObjectNotEqual($a [ 't0.F_KEY1', 't0.F_KEY2' ] [ 'KEY1', 'KEY2' ] ) ORDER BY PKEY"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|query
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|CompoundFkTestEntity
argument_list|>
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CompoundFkTestEntity
name|p
init|=
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|Cayenne
operator|.
name|intPKForObject
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

