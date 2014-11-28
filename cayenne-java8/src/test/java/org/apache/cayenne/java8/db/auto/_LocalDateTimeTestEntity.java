begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|java8
operator|.
name|db
operator|.
name|auto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
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
comment|/**  * Class _LocalDateTimeTestEntity was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_LocalDateTimeTestEntity
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
name|TIMESTAMP_PROPERTY
init|=
literal|"timestamp"
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
name|LocalDateTime
argument_list|>
name|TIMESTAMP
init|=
operator|new
name|Property
argument_list|<
name|LocalDateTime
argument_list|>
argument_list|(
literal|"timestamp"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|LocalDateTime
name|timestamp
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"timestamp"
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
block|}
specifier|public
name|LocalDateTime
name|getTimestamp
parameter_list|()
block|{
return|return
operator|(
name|LocalDateTime
operator|)
name|readProperty
argument_list|(
literal|"timestamp"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

