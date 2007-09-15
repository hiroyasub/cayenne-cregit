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
name|PersistentObject
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
name|ClientMtTable2
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "MtTable1" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientMtTable1
extends|extends
name|PersistentObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GLOBAL_ATTRIBUTE1_PROPERTY
init|=
literal|"globalAttribute1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_ATTRIBUTE1_PROPERTY
init|=
literal|"serverAttribute1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE2ARRAY_PROPERTY
init|=
literal|"table2Array"
decl_stmt|;
specifier|protected
name|String
name|globalAttribute1
decl_stmt|;
specifier|protected
name|String
name|serverAttribute1
decl_stmt|;
specifier|protected
name|java
operator|.
name|util
operator|.
name|List
name|table2Array
decl_stmt|;
specifier|public
name|String
name|getGlobalAttribute1
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
literal|"globalAttribute1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|globalAttribute1
return|;
block|}
specifier|public
name|void
name|setGlobalAttribute1
parameter_list|(
name|String
name|globalAttribute1
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
literal|"globalAttribute1"
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
name|globalAttribute1
decl_stmt|;
name|this
operator|.
name|globalAttribute1
operator|=
name|globalAttribute1
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
literal|"globalAttribute1"
argument_list|,
name|oldValue
argument_list|,
name|globalAttribute1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getServerAttribute1
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
literal|"serverAttribute1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|serverAttribute1
return|;
block|}
specifier|public
name|void
name|setServerAttribute1
parameter_list|(
name|String
name|serverAttribute1
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
literal|"serverAttribute1"
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
name|serverAttribute1
decl_stmt|;
name|this
operator|.
name|serverAttribute1
operator|=
name|serverAttribute1
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
literal|"serverAttribute1"
argument_list|,
name|oldValue
argument_list|,
name|serverAttribute1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|java
operator|.
name|util
operator|.
name|List
name|getTable2Array
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
literal|"table2Array"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|table2Array
return|;
block|}
specifier|public
name|void
name|addToTable2Array
parameter_list|(
name|ClientMtTable2
name|object
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
literal|"table2Array"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|table2Array
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTable2Array
parameter_list|(
name|ClientMtTable2
name|object
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
literal|"table2Array"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|table2Array
operator|.
name|remove
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

