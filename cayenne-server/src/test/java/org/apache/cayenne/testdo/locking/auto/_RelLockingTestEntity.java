begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|locking
operator|.
name|auto
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
name|CayenneDataObject
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
name|exp
operator|.
name|Property
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
name|locking
operator|.
name|LockingHelper
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
name|locking
operator|.
name|SimpleLockingTestEntity
import|;
end_import

begin_comment
comment|/**  * Class _RelLockingTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_RelLockingTestEntity
extends|extends
name|CayenneDataObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REL_LOCKING_TEST_ID_PK_COLUMN
init|=
literal|"REL_LOCKING_TEST_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|NAME
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|LockingHelper
argument_list|>
argument_list|>
name|LOCKING_HELPERS
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"lockingHelpers"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|SimpleLockingTestEntity
argument_list|>
name|TO_SIMPLE_LOCKING_TEST
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"toSimpleLockingTest"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToLockingHelpers
parameter_list|(
name|LockingHelper
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"lockingHelpers"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromLockingHelpers
parameter_list|(
name|LockingHelper
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"lockingHelpers"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|LockingHelper
argument_list|>
name|getLockingHelpers
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|LockingHelper
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"lockingHelpers"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToSimpleLockingTest
parameter_list|(
name|SimpleLockingTestEntity
name|toSimpleLockingTest
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toSimpleLockingTest"
argument_list|,
name|toSimpleLockingTest
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SimpleLockingTestEntity
name|getToSimpleLockingTest
parameter_list|()
block|{
return|return
operator|(
name|SimpleLockingTestEntity
operator|)
name|readProperty
argument_list|(
literal|"toSimpleLockingTest"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

