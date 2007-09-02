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

begin_class
specifier|public
class|class
name|_6_1_2_6_PersistentClassSourcesTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testLoadUndeclared
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
comment|//		UndeclaredEntity1 e = new UndeclaredEntity1();
comment|//		getEntityManager().persist(e);
comment|//		getEntityManager().getTransaction().commit();
comment|//
comment|//		assertEquals(1, getDbHelper().getRowCount("UndeclaredEntity1"));
block|}
block|}
end_class

end_unit

