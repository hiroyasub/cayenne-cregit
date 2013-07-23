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
name|BigInteger
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
comment|/**  * Class _BigIntegerEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_BigIntegerEntity
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
name|BIG_INTEGER_FIELD_PROPERTY
init|=
literal|"bigIntegerField"
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
name|BigInteger
argument_list|>
name|BIG_INTEGER_FIELD
init|=
operator|new
name|Property
argument_list|<
name|BigInteger
argument_list|>
argument_list|(
literal|"bigIntegerField"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setBigIntegerField
parameter_list|(
name|BigInteger
name|bigIntegerField
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"bigIntegerField"
argument_list|,
name|bigIntegerField
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BigInteger
name|getBigIntegerField
parameter_list|()
block|{
return|return
operator|(
name|BigInteger
operator|)
name|readProperty
argument_list|(
literal|"bigIntegerField"
argument_list|)
return|;
block|}
block|}
end_class

end_unit
