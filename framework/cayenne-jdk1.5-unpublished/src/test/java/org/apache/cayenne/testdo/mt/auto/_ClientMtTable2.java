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
name|ValueHolder
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
name|ClientMtTable3
import|;
end_import

begin_comment
comment|/**  * A generated persistent class mapped as "MtTable2" Cayenne entity. It is a good idea to  * avoid changing this class manually, since it will be overwritten next time code is  * regenerated. If you need to make any customizations, put them in a subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClientMtTable2
extends|extends
name|PersistentObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|GLOBAL_ATTRIBUTE_PROPERTY
init|=
literal|"globalAttribute"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE1_PROPERTY
init|=
literal|"table1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE3_PROPERTY
init|=
literal|"table3"
decl_stmt|;
specifier|protected
name|String
name|globalAttribute
decl_stmt|;
specifier|protected
name|ValueHolder
name|table1
decl_stmt|;
specifier|protected
name|ValueHolder
name|table3
decl_stmt|;
specifier|public
name|String
name|getGlobalAttribute
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
literal|"globalAttribute"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|globalAttribute
return|;
block|}
specifier|public
name|void
name|setGlobalAttribute
parameter_list|(
name|String
name|globalAttribute
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
literal|"globalAttribute"
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
name|globalAttribute
decl_stmt|;
name|this
operator|.
name|globalAttribute
operator|=
name|globalAttribute
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
literal|"globalAttribute"
argument_list|,
name|oldValue
argument_list|,
name|globalAttribute
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ClientMtTable1
name|getTable1
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
literal|"table1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ClientMtTable1
operator|)
name|table1
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setTable1
parameter_list|(
name|ClientMtTable1
name|table1
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
literal|"table1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|table1
operator|.
name|setValue
argument_list|(
name|table1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClientMtTable3
name|getTable3
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
literal|"table3"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ClientMtTable3
operator|)
name|table3
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setTable3
parameter_list|(
name|ClientMtTable3
name|table3
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
literal|"table3"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|table3
operator|.
name|setValue
argument_list|(
name|table3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

