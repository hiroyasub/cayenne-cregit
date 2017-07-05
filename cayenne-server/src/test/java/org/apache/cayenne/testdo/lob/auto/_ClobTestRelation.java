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
name|lob
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
name|Property
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
name|lob
operator|.
name|ClobTestEntity
import|;
end_import

begin_comment
comment|/**  * Class _ClobTestRelation was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClobTestRelation
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
name|String
name|ID_PK_COLUMN
init|=
literal|"ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|ID
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"id"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|VALUE
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"value"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|ClobTestEntity
argument_list|>
name|CLOB_ID
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"clobId"
argument_list|,
name|ClobTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Integer
name|id
decl_stmt|;
specifier|protected
name|Integer
name|value
decl_stmt|;
specifier|protected
name|Object
name|clobId
decl_stmt|;
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"id"
argument_list|,
name|this
operator|.
name|id
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"value"
argument_list|,
name|this
operator|.
name|value
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|Integer
name|getValue
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setClobId
parameter_list|(
name|ClobTestEntity
name|clobId
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"clobId"
argument_list|,
name|clobId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClobTestEntity
name|getClobId
parameter_list|()
block|{
return|return
operator|(
name|ClobTestEntity
operator|)
name|readProperty
argument_list|(
literal|"clobId"
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
literal|"id"
case|:
return|return
name|this
operator|.
name|id
return|;
case|case
literal|"value"
case|:
return|return
name|this
operator|.
name|value
return|;
case|case
literal|"clobId"
case|:
return|return
name|this
operator|.
name|clobId
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
literal|"id"
case|:
name|this
operator|.
name|id
operator|=
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"value"
case|:
name|this
operator|.
name|value
operator|=
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"clobId"
case|:
name|this
operator|.
name|clobId
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
name|id
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|clobId
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
name|id
operator|=
operator|(
name|Integer
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|value
operator|=
operator|(
name|Integer
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|clobId
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

