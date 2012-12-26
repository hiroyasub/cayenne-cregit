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
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|exp
operator|.
name|Property
import|;
end_import

begin_comment
comment|/**  * Class _BigDecimalEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_BigDecimalEntity
extends|extends
name|CayenneDataObject
block|{
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BIG_DECIMAL_FIELD_PROPERTY
init|=
literal|"bigDecimalField"
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
name|BigDecimal
argument_list|>
name|BIG_DECIMAL_FIELD
init|=
operator|new
name|Property
argument_list|<
name|BigDecimal
argument_list|>
argument_list|(
literal|"bigDecimalField"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setBigDecimalField
parameter_list|(
name|BigDecimal
name|bigDecimalField
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"bigDecimalField"
argument_list|,
name|bigDecimalField
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BigDecimal
name|getBigDecimalField
parameter_list|()
block|{
return|return
operator|(
name|BigDecimal
operator|)
name|readProperty
argument_list|(
literal|"bigDecimalField"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

