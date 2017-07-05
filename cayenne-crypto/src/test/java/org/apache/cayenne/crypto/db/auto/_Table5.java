begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|db
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

begin_comment
comment|/**  * Class _Table5 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Table5
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
name|CRYPTO_INT1
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"cryptoInt1"
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
name|CRYPTO_INT3
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"cryptoInt3"
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
name|CRYPTO_INT4
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"cryptoInt4"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|int
name|cryptoInt1
decl_stmt|;
specifier|protected
name|int
name|cryptoInt3
decl_stmt|;
specifier|protected
name|int
name|cryptoInt4
decl_stmt|;
specifier|public
name|void
name|setCryptoInt1
parameter_list|(
name|int
name|cryptoInt1
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"cryptoInt1"
argument_list|,
name|this
operator|.
name|cryptoInt1
argument_list|,
name|cryptoInt1
argument_list|)
expr_stmt|;
name|this
operator|.
name|cryptoInt1
operator|=
name|cryptoInt1
expr_stmt|;
block|}
specifier|public
name|int
name|getCryptoInt1
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"cryptoInt1"
argument_list|)
expr_stmt|;
return|return
name|cryptoInt1
return|;
block|}
specifier|public
name|void
name|setCryptoInt3
parameter_list|(
name|int
name|cryptoInt3
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"cryptoInt3"
argument_list|,
name|this
operator|.
name|cryptoInt3
argument_list|,
name|cryptoInt3
argument_list|)
expr_stmt|;
name|this
operator|.
name|cryptoInt3
operator|=
name|cryptoInt3
expr_stmt|;
block|}
specifier|public
name|int
name|getCryptoInt3
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"cryptoInt3"
argument_list|)
expr_stmt|;
return|return
name|cryptoInt3
return|;
block|}
specifier|public
name|void
name|setCryptoInt4
parameter_list|(
name|int
name|cryptoInt4
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"cryptoInt4"
argument_list|,
name|this
operator|.
name|cryptoInt4
argument_list|,
name|cryptoInt4
argument_list|)
expr_stmt|;
name|this
operator|.
name|cryptoInt4
operator|=
name|cryptoInt4
expr_stmt|;
block|}
specifier|public
name|int
name|getCryptoInt4
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"cryptoInt4"
argument_list|)
expr_stmt|;
return|return
name|cryptoInt4
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
literal|"cryptoInt1"
case|:
return|return
name|this
operator|.
name|cryptoInt1
return|;
case|case
literal|"cryptoInt3"
case|:
return|return
name|this
operator|.
name|cryptoInt3
return|;
case|case
literal|"cryptoInt4"
case|:
return|return
name|this
operator|.
name|cryptoInt4
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
literal|"cryptoInt1"
case|:
name|this
operator|.
name|cryptoInt1
operator|=
name|val
operator|==
literal|null
condition|?
literal|0
else|:
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"cryptoInt3"
case|:
name|this
operator|.
name|cryptoInt3
operator|=
name|val
operator|==
literal|null
condition|?
literal|0
else|:
operator|(
name|Integer
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"cryptoInt4"
case|:
name|this
operator|.
name|cryptoInt4
operator|=
name|val
operator|==
literal|null
condition|?
literal|0
else|:
operator|(
name|Integer
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
name|writeInt
argument_list|(
name|cryptoInt1
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|cryptoInt3
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeInt
argument_list|(
name|cryptoInt4
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
name|cryptoInt1
operator|=
name|in
operator|.
name|readInt
argument_list|()
expr_stmt|;
name|cryptoInt3
operator|=
name|in
operator|.
name|readInt
argument_list|()
expr_stmt|;
name|cryptoInt4
operator|=
name|in
operator|.
name|readInt
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

