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
name|inheritance
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
name|inheritance
operator|.
name|DirectToSubEntity
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
name|inheritance
operator|.
name|RelatedEntity
import|;
end_import

begin_comment
comment|/**  * Class _BaseEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_BaseEntity
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
name|BASE_ENTITY_ID_PK_COLUMN
init|=
literal|"BASE_ENTITY_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|ENTITY_TYPE
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"entityType"
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
name|DirectToSubEntity
argument_list|>
name|TO_DIRECT_TO_SUB_ENTITY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"toDirectToSubEntity"
argument_list|,
name|DirectToSubEntity
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|RelatedEntity
argument_list|>
name|TO_RELATED_ENTITY
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"toRelatedEntity"
argument_list|,
name|RelatedEntity
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|entityType
decl_stmt|;
specifier|protected
name|Object
name|toDirectToSubEntity
decl_stmt|;
specifier|protected
name|Object
name|toRelatedEntity
decl_stmt|;
specifier|public
name|void
name|setEntityType
parameter_list|(
name|String
name|entityType
parameter_list|)
block|{
name|beforePropertyWrite
argument_list|(
literal|"entityType"
argument_list|,
name|this
operator|.
name|entityType
argument_list|,
name|entityType
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityType
operator|=
name|entityType
expr_stmt|;
block|}
specifier|public
name|String
name|getEntityType
parameter_list|()
block|{
name|beforePropertyRead
argument_list|(
literal|"entityType"
argument_list|)
expr_stmt|;
return|return
name|entityType
return|;
block|}
specifier|public
name|void
name|setToDirectToSubEntity
parameter_list|(
name|DirectToSubEntity
name|toDirectToSubEntity
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toDirectToSubEntity"
argument_list|,
name|toDirectToSubEntity
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DirectToSubEntity
name|getToDirectToSubEntity
parameter_list|()
block|{
return|return
operator|(
name|DirectToSubEntity
operator|)
name|readProperty
argument_list|(
literal|"toDirectToSubEntity"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToRelatedEntity
parameter_list|(
name|RelatedEntity
name|toRelatedEntity
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toRelatedEntity"
argument_list|,
name|toRelatedEntity
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RelatedEntity
name|getToRelatedEntity
parameter_list|()
block|{
return|return
operator|(
name|RelatedEntity
operator|)
name|readProperty
argument_list|(
literal|"toRelatedEntity"
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
literal|"entityType"
case|:
return|return
name|this
operator|.
name|entityType
return|;
case|case
literal|"toDirectToSubEntity"
case|:
return|return
name|this
operator|.
name|toDirectToSubEntity
return|;
case|case
literal|"toRelatedEntity"
case|:
return|return
name|this
operator|.
name|toRelatedEntity
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
literal|"entityType"
case|:
name|this
operator|.
name|entityType
operator|=
operator|(
name|String
operator|)
name|val
expr_stmt|;
break|break;
case|case
literal|"toDirectToSubEntity"
case|:
name|this
operator|.
name|toDirectToSubEntity
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toRelatedEntity"
case|:
name|this
operator|.
name|toRelatedEntity
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
name|entityType
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|toDirectToSubEntity
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|toRelatedEntity
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
name|entityType
operator|=
operator|(
name|String
operator|)
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|toDirectToSubEntity
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|toRelatedEntity
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

