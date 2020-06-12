begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|generic
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
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
name|ObjAttribute
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|ComparisionStrategyFactory
block|{
name|BiFunction
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Boolean
argument_list|>
name|getStrategy
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

