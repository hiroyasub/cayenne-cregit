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
name|relationships
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
name|relationships
operator|.
name|ReflexiveAndToOne
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
name|relationships
operator|.
name|RelationshipHelper
import|;
end_import

begin_comment
comment|/**  * Class _ReflexiveAndToOne was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ReflexiveAndToOne
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
name|REFLEXIVE_AND_TO_ONE_ID_PK_COLUMN
init|=
literal|"REFLEXIVE_AND_TO_ONE_ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|NAME
init|=
name|Property
operator|.
name|create
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
name|Property
argument_list|<
name|List
argument_list|<
name|ReflexiveAndToOne
argument_list|>
argument_list|>
name|CHILDREN
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"children"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|RelationshipHelper
argument_list|>
name|TO_HELPER
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"toHelper"
argument_list|,
name|RelationshipHelper
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|ReflexiveAndToOne
argument_list|>
name|TO_PARENT
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"toParent"
argument_list|,
name|ReflexiveAndToOne
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
name|children
decl_stmt|;
specifier|protected
name|Object
name|toHelper
decl_stmt|;
specifier|protected
name|Object
name|toParent
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
name|addToChildren
parameter_list|(
name|ReflexiveAndToOne
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"children"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromChildren
parameter_list|(
name|ReflexiveAndToOne
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"children"
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
name|ReflexiveAndToOne
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|ReflexiveAndToOne
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"children"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToHelper
parameter_list|(
name|RelationshipHelper
name|toHelper
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toHelper"
argument_list|,
name|toHelper
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RelationshipHelper
name|getToHelper
parameter_list|()
block|{
return|return
operator|(
name|RelationshipHelper
operator|)
name|readProperty
argument_list|(
literal|"toHelper"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToParent
parameter_list|(
name|ReflexiveAndToOne
name|toParent
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"toParent"
argument_list|,
name|toParent
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReflexiveAndToOne
name|getToParent
parameter_list|()
block|{
return|return
operator|(
name|ReflexiveAndToOne
operator|)
name|readProperty
argument_list|(
literal|"toParent"
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
literal|"children"
case|:
return|return
name|this
operator|.
name|children
return|;
case|case
literal|"toHelper"
case|:
return|return
name|this
operator|.
name|toHelper
return|;
case|case
literal|"toParent"
case|:
return|return
name|this
operator|.
name|toParent
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
literal|"children"
case|:
name|this
operator|.
name|children
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toHelper"
case|:
name|this
operator|.
name|toHelper
operator|=
name|val
expr_stmt|;
break|break;
case|case
literal|"toParent"
case|:
name|this
operator|.
name|toParent
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
name|children
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toHelper
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|this
operator|.
name|toParent
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
name|children
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toHelper
operator|=
name|in
operator|.
name|readObject
argument_list|()
expr_stmt|;
name|this
operator|.
name|toParent
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

