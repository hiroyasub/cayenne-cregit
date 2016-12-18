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
name|inheritance_vertical
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
name|inheritance_vertical
operator|.
name|IvAbstract
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
name|inheritance_vertical
operator|.
name|IvConcrete
import|;
end_import

begin_comment
comment|/**  * Class _IvConcrete was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_IvConcrete
extends|extends
name|IvAbstract
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
name|String
argument_list|>
name|NAME
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|List
argument_list|<
name|IvConcrete
argument_list|>
argument_list|>
name|CHILDREN
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"children"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|IvConcrete
argument_list|>
name|PARENT
init|=
operator|new
name|Property
argument_list|<>
argument_list|(
literal|"parent"
argument_list|)
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
literal|"name"
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
literal|"name"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToChildren
parameter_list|(
name|IvConcrete
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
name|IvConcrete
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
name|IvConcrete
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|IvConcrete
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
name|setParent
parameter_list|(
name|IvConcrete
name|parent
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"parent"
argument_list|,
name|parent
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvConcrete
name|getParent
parameter_list|()
block|{
return|return
operator|(
name|IvConcrete
operator|)
name|readProperty
argument_list|(
literal|"parent"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

