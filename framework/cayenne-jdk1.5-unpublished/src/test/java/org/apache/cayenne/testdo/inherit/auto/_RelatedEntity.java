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
name|inherit
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
name|inherit
operator|.
name|BaseEntity
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
name|inherit
operator|.
name|SubEntity
import|;
end_import

begin_comment
comment|/**  * Class _RelatedEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_RelatedEntity
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BASE_ENTITIES_PROPERTY
init|=
literal|"baseEntities"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUB_ENTITIES_PROPERTY
init|=
literal|"subEntities"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATED_ENTITY_ID_PK_COLUMN
init|=
literal|"RELATED_ENTITY_ID"
decl_stmt|;
specifier|public
name|void
name|addToBaseEntities
parameter_list|(
name|BaseEntity
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"baseEntities"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromBaseEntities
parameter_list|(
name|BaseEntity
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"baseEntities"
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
name|BaseEntity
argument_list|>
name|getBaseEntities
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|BaseEntity
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"baseEntities"
argument_list|)
return|;
block|}
specifier|public
name|void
name|addToSubEntities
parameter_list|(
name|SubEntity
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
literal|"subEntities"
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromSubEntities
parameter_list|(
name|SubEntity
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
literal|"subEntities"
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
name|SubEntity
argument_list|>
name|getSubEntities
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|SubEntity
argument_list|>
operator|)
name|readProperty
argument_list|(
literal|"subEntities"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

