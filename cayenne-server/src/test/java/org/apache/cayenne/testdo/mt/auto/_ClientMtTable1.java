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
name|exp
operator|.
name|property
operator|.
name|ListProperty
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
name|ClientMtTable2
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
name|util
operator|.
name|PersistentObjectList
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
name|StringProperty
argument_list|<
name|String
argument_list|>
name|GLOBAL_ATTRIBUTE1
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"globalAttribute1"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|SERVER_ATTRIBUTE1
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"serverAttribute1"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|ClientMtTable2
argument_list|>
name|TABLE2ARRAY
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"table2Array"
argument_list|,
name|ClientMtTable2
operator|.
name|class
argument_list|)
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
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
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
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"globalAttribute1"
argument_list|,
name|this
operator|.
name|globalAttribute1
argument_list|,
name|globalAttribute1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|globalAttribute1
operator|=
name|globalAttribute1
expr_stmt|;
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
name|objectContext
operator|.
name|propertyChanged
argument_list|(
name|this
argument_list|,
literal|"serverAttribute1"
argument_list|,
name|this
operator|.
name|serverAttribute1
argument_list|,
name|serverAttribute1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|serverAttribute1
operator|=
name|serverAttribute1
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ClientMtTable2
argument_list|>
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
if|else if
condition|(
name|this
operator|.
name|table2Array
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|table2Array
operator|=
operator|new
name|PersistentObjectList
argument_list|<>
argument_list|(
name|this
argument_list|,
literal|"table2Array"
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
if|else if
condition|(
name|this
operator|.
name|table2Array
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|table2Array
operator|=
operator|new
name|PersistentObjectList
argument_list|<>
argument_list|(
name|this
argument_list|,
literal|"table2Array"
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
if|else if
condition|(
name|this
operator|.
name|table2Array
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|table2Array
operator|=
operator|new
name|PersistentObjectList
argument_list|<>
argument_list|(
name|this
argument_list|,
literal|"table2Array"
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

