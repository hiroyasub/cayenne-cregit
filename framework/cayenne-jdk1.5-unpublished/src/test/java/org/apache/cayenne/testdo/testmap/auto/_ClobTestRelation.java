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
name|testmap
operator|.
name|auto
package|;
end_package

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
name|testmap
operator|.
name|ClobTestEntity
import|;
end_import

begin_comment
comment|/**  * Class _ClobTestRelation was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ClobTestRelation
extends|extends
name|CayenneDataObject
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|ID_PROPERTY
init|=
literal|"id"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_PROPERTY
init|=
literal|"value"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CLOB_ID_PROPERTY
init|=
literal|"clobId"
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
name|ID
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|VALUE
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|ClobTestEntity
argument_list|>
name|CLOB_ID
init|=
operator|new
name|Property
argument_list|<
name|ClobTestEntity
argument_list|>
argument_list|(
literal|"clobId"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"id"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getValue
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"value"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClobId
parameter_list|(
name|ClobTestEntity
name|clobId
parameter_list|)
block|{
name|setToOneTarget
argument_list|(
literal|"clobId"
argument_list|,
name|clobId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ClobTestEntity
name|getClobId
parameter_list|()
block|{
return|return
operator|(
name|ClobTestEntity
operator|)
name|readProperty
argument_list|(
literal|"clobId"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

