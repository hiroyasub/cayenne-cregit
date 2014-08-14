begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|naming
package|;
end_package

begin_comment
comment|/**  *  * @since 3.1 moved from project package  */
end_comment

begin_interface
specifier|public
interface|interface
name|NameChecker
block|{
comment|/**      * Returns a base default name, like "UntitledEntity", etc.      *      * */
name|String
name|baseName
parameter_list|()
function_decl|;
comment|/**      * Checks if the name is already taken by another sibling in the same      * context.      *      */
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

