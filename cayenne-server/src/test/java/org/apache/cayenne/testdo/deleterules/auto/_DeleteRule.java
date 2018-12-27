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
name|deleterules
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

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
name|BaseDataObject
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
name|ExpressionFactory
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
name|NumericProperty
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
name|deleterules
operator|.
name|DeleteCascade
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
name|deleterules
operator|.
name|DeleteDeny
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
name|deleterules
operator|.
name|DeleteNullify
import|;
end_import

begin_comment
comment|/**  * Class _DeleteRule was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_DeleteRule
extends|extends
name|BaseDataObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|NumericProperty
argument_list|<
name|Integer
argument_list|>
name|DELETE_RULE_ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"DELETE_RULE_ID"
argument_list|)
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_RULE_ID_PK_COLUMN
init|=
literal|"DELETE_RULE_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|NAME
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"name"
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
name|DeleteCascade
argument_list|>
name|FROM_CASCADE
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"fromCascade"
argument_list|,
name|DeleteCascade
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|DeleteDeny
argument_list|>
name|FROM_DENY
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"fromDeny"
argument_list|,
name|DeleteDeny
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ListProperty
argument_list|<
name|DeleteNullify
argument_list|>
name|FROM_NULLIFY
init|=
name|PropertyFactory
operator|.
name|createList
argument_list|(
literal|"fromNullify"
argument_list|,
name|DeleteNullify
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Object
name|fromCascade
decl_stmt|;
specifier|protected
name|Object
name|fromDeny
decl_stmt|;
specifier|protected
name|Object
name|fromNullify
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"name"
argument_list|,
name|this
operator|.
name|name
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|name
return|;
block|}
specifier|public
name|void
name|addToFromCascade
parameter_list|(
name|DeleteCascade
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"fromCascade"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFromCascade
parameter_list|(
name|DeleteCascade
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"fromCascade"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|DeleteCascade
argument_list|>
name|getFromCascade
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|DeleteCascade
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"fromCascade"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToFromDeny
parameter_list|(
name|DeleteDeny
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"fromDeny"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFromDeny
parameter_list|(
name|DeleteDeny
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"fromDeny"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|DeleteDeny
argument_list|>
name|getFromDeny
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|DeleteDeny
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"fromDeny"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToFromNullify
parameter_list|(
name|DeleteNullify
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"fromNullify"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromFromNullify
parameter_list|(
name|DeleteNullify
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"fromNullify"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|DeleteNullify
argument_list|>
name|getFromNullify
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|DeleteNullify
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"fromNullify"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readPropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|)
block|{
if|if
condition|(
name|propName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
switch|switch
condition|(
name|propName
condition|)
block|{
case|case
literal|"name"
case|:
return|return
name|this
operator|.
name|name
return|;
case|case
literal|"fromCascade"
case|:
return|return
name|this
operator|.
name|fromCascade
return|;
case|case
literal|"fromDeny"
case|:
return|return
name|this
operator|.
name|fromDeny
return|;
case|case
literal|"fromNullify"
case|:
return|return
name|this
operator|.
name|fromNullify
return|;
default|default:
return|return
name|super
operator|.
name|readPropertyDirectly
argument_list|(
name|propName
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writePropertyDirectly
parameter_list|(
name|String
name|propName
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
if|if
condition|(
name|propName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
switch|switch
condition|(
name|propName
condition|)
block|{
case|case
literal|"name"
case|:
name|this
operator|.
name|name
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"fromCascade"
case|:
name|this
operator|.
name|fromCascade
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"fromDeny"
case|:
name|this
operator|.
name|fromDeny
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"fromNullify"
case|:
name|this
operator|.
name|fromNullify
operator|=
name|val
expr_stmt|;
break|break;
default|default:
name|super
operator|.
name|writePropertyDirectly
argument_list|(
name|propName
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeSerialized
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|readSerialized
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeState
parameter_list|(
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|writeState
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|name
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|fromCascade
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|fromDeny
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|fromNullify
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|readState
parameter_list|(
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|super
operator|.
name|readState
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromCascade
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromDeny
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromNullify
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

