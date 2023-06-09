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
name|mt
operator|.
name|auto
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
name|exp
operator|.
name|property
operator|.
name|PropertyFactory
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
name|property
operator|.
name|StringProperty
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
name|mt
operator|.
name|ClientMtTable1
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "MtTable1Subclass2" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientMtTable1Subclass2
extends|extends
name|ClientMtTable1
block|{
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|SUBCLASS2ATTRIBUTE1
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"subclass2Attribute1"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|subclass2Attribute1
decl_stmt|;
specifier|public
name|String
name|getSubclass2Attribute1
parameter_list|()
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"subclass2Attribute1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|subclass2Attribute1
return|;
block|}
specifier|public
name|void
name|setSubclass2Attribute1
parameter_list|(
name|String
name|subclass2Attribute1
parameter_list|)
block|{
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|prepareForAccess
argument_list|(
name|this
argument_list|,
literal|"subclass2Attribute1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"subclass2Attribute1"
argument_list|,
name|this
operator|.
name|subclass2Attribute1
argument_list|,
name|subclass2Attribute1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|subclass2Attribute1
operator|=
name|subclass2Attribute1
expr_stmt|;
block|}
block|}
end_class

end_unit

