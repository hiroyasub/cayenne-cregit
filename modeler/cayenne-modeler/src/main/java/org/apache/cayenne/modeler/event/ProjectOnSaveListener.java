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
name|java
operator|.
name|util
operator|.
name|EventListener
import|;
end_import

begin_comment
comment|/**   * Interface for classes that are interested in ProjectOnSave events.   */
end_comment

begin_interface
specifier|public
interface|interface
name|ProjectOnSaveListener
extends|extends
name|EventListener
block|{
comment|/** Changes made before saving project	 */
specifier|public
name|void
name|beforeSaveChanges
parameter_list|(
name|ProjectOnSaveEvent
name|e
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

