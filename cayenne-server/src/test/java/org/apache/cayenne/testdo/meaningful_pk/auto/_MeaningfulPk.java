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
name|meaningful_pk
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
name|property
operator|.
name|EntityProperty
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
name|meaningful_pk
operator|.
name|MeaningfulPkDep2
import|;
end_import

begin_comment
comment|/**  * Class _MeaningfulPk was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MeaningfulPk
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
name|PK_PK_COLUMN
init|=
literal|"PK"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|StringProperty
argument_list|<
name|String
argument_list|>
name|PK
init|=
name|PropertyFactory
operator|.
name|createString
argument_list|(
literal|"pk"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EntityProperty
argument_list|<
name|MeaningfulPkDep2
argument_list|>
name|MEANINGFUL_PK_DEP2S
init|=
name|PropertyFactory
operator|.
name|createEntity
argument_list|(
literal|"meaningfulPkDep2s"
argument_list|,
name|MeaningfulPkDep2
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|pk
decl_stmt|;
specifier|protected
name|Object
name|meaningfulPkDep2s
decl_stmt|;
specifier|public
name|void
name|setPk
parameter_list|(
name|String
name|pk
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"pk"
argument_list|,
name|this
operator|.
name|pk
argument_list|,
name|pk
argument_list|)
expr_stmt|;
name|this
operator|.
name|pk
operator|=
name|pk
expr_stmt|;
block|}
specifier|public
name|String
name|getPk
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"pk"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|pk
return|;
block|}
specifier|public
name|void
name|setMeaningfulPkDep2s
parameter_list|(
name|MeaningfulPkDep2
name|meaningfulPkDep2s
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"meaningfulPkDep2s"
argument_list|,
name|meaningfulPkDep2s
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MeaningfulPkDep2
name|getMeaningfulPkDep2s
parameter_list|()
block|{
return|return
operator|(
name|MeaningfulPkDep2
operator|)
name|readProperty
argument_list|(
literal|"meaningfulPkDep2s"
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
literal|"pk"
case|:
return|return
name|this
operator|.
name|pk
return|;
case|case
literal|"meaningfulPkDep2s"
case|:
return|return
name|this
operator|.
name|meaningfulPkDep2s
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
literal|"pk"
case|:
name|this
operator|.
name|pk
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"meaningfulPkDep2s"
case|:
name|this
operator|.
name|meaningfulPkDep2s
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
name|pk
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|meaningfulPkDep2s
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
name|pk
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
name|meaningfulPkDep2s
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

