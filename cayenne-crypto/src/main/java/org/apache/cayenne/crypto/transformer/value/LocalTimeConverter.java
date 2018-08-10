begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|transformer
operator|.
name|value
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|LocalTimeConverter
implements|implements
name|BytesConverter
argument_list|<
name|LocalTime
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|LocalTime
argument_list|>
name|INSTANCE
init|=
operator|new
name|LocalTimeConverter
argument_list|(
name|LongConverter
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
specifier|private
name|BytesConverter
argument_list|<
name|Long
argument_list|>
name|longConverter
decl_stmt|;
specifier|public
name|LocalTimeConverter
parameter_list|(
name|BytesConverter
argument_list|<
name|Long
argument_list|>
name|longConverter
parameter_list|)
block|{
name|this
operator|.
name|longConverter
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|longConverter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|LocalTime
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|LocalTime
operator|.
name|ofNanoOfDay
argument_list|(
name|longConverter
operator|.
name|fromBytes
argument_list|(
name|bytes
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|toBytes
parameter_list|(
name|LocalTime
name|value
parameter_list|)
block|{
return|return
name|longConverter
operator|.
name|toBytes
argument_list|(
name|value
operator|.
name|toNanoOfDay
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

