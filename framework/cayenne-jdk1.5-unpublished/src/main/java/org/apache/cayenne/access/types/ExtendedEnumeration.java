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
operator|.
name|types
package|;
end_package

begin_interface
specifier|public
interface|interface
name|ExtendedEnumeration
block|{
comment|/**      * Return the value to be stored in the database for this enumeration.  In      * actuality, this should be an Integer or a String.      */
specifier|public
name|Object
name|getDatabaseValue
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

