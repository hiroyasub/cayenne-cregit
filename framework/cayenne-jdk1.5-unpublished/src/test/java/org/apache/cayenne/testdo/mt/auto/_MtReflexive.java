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
name|CayenneDataObject
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
name|MtReflexive
import|;
end_import

begin_comment
comment|/**  * Class _MtReflexive was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_MtReflexive
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PROPERTY
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHILDREN_PROPERTY
init|=
literal|"children"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TO_PARENT_PROPERTY
init|=
literal|"toParent"
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
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|writeProperty
argument_list|(
name|NAME_PROPERTY
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
name|NAME_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToChildren
parameter_list|(
name|MtReflexive
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|CHILDREN_PROPERTY
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
name|MtReflexive
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|CHILDREN_PROPERTY
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
name|MtReflexive
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|MtReflexive
argument_list|>
operator|)
name|readProperty
argument_list|(
name|CHILDREN_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setToParent
parameter_list|(
name|MtReflexive
name|toParent
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
name|TO_PARENT_PROPERTY
argument_list|,
name|toParent
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MtReflexive
name|getToParent
parameter_list|()
block|{
return|return
operator|(
name|MtReflexive
operator|)
name|readProperty
argument_list|(
name|TO_PARENT_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit

