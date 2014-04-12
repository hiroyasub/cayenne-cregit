begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
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
name|event
operator|.
name|CayenneEvent
import|;
end_import

begin_comment
comment|/**  * Triggered while project is saved.  */
end_comment

begin_class
specifier|public
class|class
name|ProjectOnSaveEvent
extends|extends
name|CayenneEvent
block|{
specifier|public
name|ProjectOnSaveEvent
parameter_list|(
name|Object
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

