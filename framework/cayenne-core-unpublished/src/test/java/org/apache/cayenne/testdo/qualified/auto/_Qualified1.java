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
name|qualified
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
name|qualified
operator|.
name|Qualified2
import|;
end_import

begin_comment
comment|/**  * Class _Qualified1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_Qualified1
extends|extends
name|CayenneDataObject
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DELETED_PROPERTY
init|=
literal|"deleted"
decl_stmt|;
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
name|QUALIFIED2S_PROPERTY
init|=
literal|"qualified2s"
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
name|setDeleted
parameter_list|(
name|Boolean
name|deleted
parameter_list|)
block|{
name|writeProperty
argument_list|(
name|DELETED_PROPERTY
argument_list|,
name|deleted
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getDeleted
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|readProperty
argument_list|(
name|DELETED_PROPERTY
argument_list|)
return|;
block|}
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
name|addToQualified2s
parameter_list|(
name|Qualified2
name|obj
parameter_list|)
block|{
name|addToManyTarget
argument_list|(
name|QUALIFIED2S_PROPERTY
argument_list|,
name|obj
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFromQualified2s
parameter_list|(
name|Qualified2
name|obj
parameter_list|)
block|{
name|removeToManyTarget
argument_list|(
name|QUALIFIED2S_PROPERTY
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
name|Qualified2
argument_list|>
name|getQualified2s
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|Qualified2
argument_list|>
operator|)
name|readProperty
argument_list|(
name|QUALIFIED2S_PROPERTY
argument_list|)
return|;
block|}
block|}
end_class

end_unit
