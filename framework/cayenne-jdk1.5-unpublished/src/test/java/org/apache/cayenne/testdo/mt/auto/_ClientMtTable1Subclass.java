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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable1
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "MtTable1Subclass" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientMtTable1Subclass
extends|extends
name|ClientMtTable1
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SUBCLASS_ATTRIBUTE1_PROPERTY
init|=
literal|"subclassAttribute1"
decl_stmt|;
specifier|protected
name|String
name|subclassAttribute1
decl_stmt|;
specifier|public
name|String
name|getSubclassAttribute1
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
literal|"subclassAttribute1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|subclassAttribute1
return|;
block|}
specifier|public
name|void
name|setSubclassAttribute1
parameter_list|(
name|String
name|subclassAttribute1
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
literal|"subclassAttribute1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|Object
name|oldValue
init|=
name|this
operator|.
name|subclassAttribute1
decl_stmt|;
name|this
operator|.
name|subclassAttribute1
operator|=
name|subclassAttribute1
expr_stmt|;
comment|// notify objectContext about simple property change
if|if
condition|(
name|objectContext
operator|!=
literal|null
condition|)
block|{
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"subclassAttribute1"
argument_list|,
name|oldValue
argument_list|,
name|subclassAttribute1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

