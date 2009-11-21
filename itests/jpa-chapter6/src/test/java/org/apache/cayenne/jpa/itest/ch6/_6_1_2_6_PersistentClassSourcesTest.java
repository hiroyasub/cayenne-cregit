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
name|ch6
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
name|ch6
operator|.
name|entity
operator|.
name|UndeclaredEntity1
import|;
end_import

begin_class
specifier|public
class|class
name|_6_1_2_6_PersistentClassSourcesTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testLoadImplicitFromUnitRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"UndeclaredEntity1"
argument_list|)
expr_stmt|;
name|UndeclaredEntity1
name|e
init|=
operator|new
name|UndeclaredEntity1
argument_list|()
decl_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|persist
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|getEntityManager
argument_list|()
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getDbHelper
argument_list|()
operator|.
name|getRowCount
argument_list|(
literal|"UndeclaredEntity1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

