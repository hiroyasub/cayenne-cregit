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
name|hybrid
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
name|HybridDataObject
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
name|hybrid
operator|.
name|HybridEntity1
import|;
end_import

begin_comment
comment|/**  * Class _HybridEntity2 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_HybridEntity2
extends|extends
name|HybridDataObject
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
name|INT_FIELD
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"intField"
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
name|String
argument_list|>
name|STR_FIELD
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"strField"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|HybridEntity1
argument_list|>
name|HYBRID_ENTITY1
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"hybridEntity1"
argument_list|,
name|HybridEntity1
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|int
name|intField
decl_stmt|;
specifier|protected
name|String
name|strField
decl_stmt|;
specifier|protected
name|Object
name|hybridEntity1
decl_stmt|;
specifier|public
name|void
name|setIntField
parameter_list|(
name|int
name|intField
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"intField"
argument_list|,
name|this
operator|.
name|intField
argument_list|,
name|intField
argument_list|)
expr_stmt|;
name|this
operator|.
name|intField
operator|=
name|intField
expr_stmt|;
block|}
specifier|public
name|int
name|getIntField
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"intField"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|intField
return|;
block|}
specifier|public
name|void
name|setStrField
parameter_list|(
name|String
name|strField
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"strField"
argument_list|,
name|this
operator|.
name|strField
argument_list|,
name|strField
argument_list|)
expr_stmt|;
name|this
operator|.
name|strField
operator|=
name|strField
expr_stmt|;
block|}
specifier|public
name|String
name|getStrField
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"strField"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|strField
return|;
block|}
specifier|public
name|void
name|setHybridEntity1
parameter_list|(
name|HybridEntity1
name|hybridEntity1
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"hybridEntity1"
argument_list|,
name|hybridEntity1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HybridEntity1
name|getHybridEntity1
parameter_list|()
block|{
return|return
operator|(
name|HybridEntity1
operator|)
name|readProperty
argument_list|(
literal|"hybridEntity1"
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
literal|"intField"
case|:
return|return
name|this
operator|.
name|intField
return|;
case|case
literal|"strField"
case|:
return|return
name|this
operator|.
name|strField
return|;
case|case
literal|"hybridEntity1"
case|:
return|return
name|this
operator|.
name|hybridEntity1
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
literal|"intField"
case|:
name|this
operator|.
name|intField
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
literal|"strField"
case|:
name|this
operator|.
name|strField
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"hybridEntity1"
case|:
name|this
operator|.
name|hybridEntity1
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
name|writeInt
argument_list|(
name|this
operator|.
name|intField
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|strField
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|hybridEntity1
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
name|intField
operator|=
name|in
operator|.
name|readInt
argument_list|()
expr_stmt|;
name|this
operator|.
name|strField
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
name|hybridEntity1
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

