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
name|relationship
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
name|Map
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
name|relationship
operator|.
name|IdMapToManyTarget
import|;
end_import

begin_comment
comment|/**  * Class _IdMapToMany was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_IdMapToMany
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TARGETS_PROPERTY
init|=
literal|"targets"
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
name|addToTargets
parameter_list|(
name|IdMapToManyTarget
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|TARGETS_PROPERTY
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromTargets
parameter_list|(
name|IdMapToManyTarget
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|TARGETS_PROPERTY
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
name|Map
argument_list|<
name|Object
argument_list|,
name|IdMapToManyTarget
argument_list|>
name|getTargets
parameter_list|()
block|{
return|return
operator|(
name|Map
argument_list|<
name|Object
argument_list|,
name|IdMapToManyTarget
argument_list|>
operator|)
name|readProperty
argument_list|(
name|TARGETS_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit

