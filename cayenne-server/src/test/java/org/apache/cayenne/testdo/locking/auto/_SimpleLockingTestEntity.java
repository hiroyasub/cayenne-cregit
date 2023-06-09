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
name|locking
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

begin_comment
comment|/**  * Class _SimpleLockingTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_SimpleLockingTestEntity
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
name|LOCKING_TEST_ID_PK_PROPERTY
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|ExpressionFactory
operator|.
name|dbPathExp
argument_list|(
literal|"LOCKING_TEST_ID"
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
name|LOCKING_TEST_ID_PK_COLUMN
init|=
literal|"LOCKING_TEST_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|DESCRIPTION
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"description"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|NumericProperty
argument_list|<
name|Integer
argument_list|>
name|INT_COLUMN_NOTNULL
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
literal|"intColumnNotnull"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|NumericProperty
argument_list|<
name|Integer
argument_list|>
name|INT_COLUMN_NULL
init|=
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
literal|"intColumnNull"
argument_list|,
name|Integer
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
specifier|protected
name|String
name|description
decl_stmt|;
specifier|protected
name|int
name|intColumnNotnull
decl_stmt|;
specifier|protected
name|Integer
name|intColumnNull
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"description"
argument_list|,
name|this
operator|.
name|description
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|description
return|;
block|}
specifier|public
name|void
name|setIntColumnNotnull
parameter_list|(
name|int
name|intColumnNotnull
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"intColumnNotnull"
argument_list|,
name|this
operator|.
name|intColumnNotnull
argument_list|,
name|intColumnNotnull
argument_list|)
expr_stmt|;
name|this
operator|.
name|intColumnNotnull
operator|=
name|intColumnNotnull
expr_stmt|;
block|}
specifier|public
name|int
name|getIntColumnNotnull
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"intColumnNotnull"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|intColumnNotnull
return|;
block|}
specifier|public
name|void
name|setIntColumnNull
parameter_list|(
name|int
name|intColumnNull
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"intColumnNull"
argument_list|,
name|this
operator|.
name|intColumnNull
argument_list|,
name|intColumnNull
argument_list|)
expr_stmt|;
name|this
operator|.
name|intColumnNull
operator|=
name|intColumnNull
expr_stmt|;
block|}
specifier|public
name|int
name|getIntColumnNull
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"intColumnNull"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|intColumnNull
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|this
operator|.
name|intColumnNull
return|;
block|}
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
literal|"description"
case|:
return|return
name|this
operator|.
name|description
return|;
case|case
literal|"intColumnNotnull"
case|:
return|return
name|this
operator|.
name|intColumnNotnull
return|;
case|case
literal|"intColumnNull"
case|:
return|return
name|this
operator|.
name|intColumnNull
return|;
case|case
literal|"name"
case|:
return|return
name|this
operator|.
name|name
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
literal|"description"
case|:
name|this
operator|.
name|description
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"intColumnNotnull"
case|:
name|this
operator|.
name|intColumnNotnull
operator|=
name|val
operator|==
literal|null
condition|?
literal|0
else|:
operator|(
name|int
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"intColumnNull"
case|:
name|this
operator|.
name|intColumnNull
operator|=
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
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
name|description
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|this
operator|.
name|intColumnNotnull
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|intColumnNull
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
name|description
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
name|intColumnNotnull
operator|=
name|in
operator|.
name|readInt
argument_list|()
expr_stmt|;
name|this
operator|.
name|intColumnNull
operator|=
operator|(
name|Integer
operator|)
name|in
operator|.
name|readObject
argument_list|()
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
block|}
block|}
end_class

end_unit

