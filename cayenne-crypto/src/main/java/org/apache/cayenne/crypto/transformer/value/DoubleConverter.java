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

begin_comment
comment|/**  * Converts between double and byte[]  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DoubleConverter
implements|implements
name|BytesConverter
argument_list|<
name|Double
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|Double
argument_list|>
name|INSTANCE
init|=
operator|new
name|DoubleConverter
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BYTES
init|=
literal|8
decl_stmt|;
specifier|static
name|double
name|getDouble
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
if|if
condition|(
name|bytes
operator|.
name|length
operator|>
name|BYTES
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"byte[] is too large for a single double value: "
operator|+
name|bytes
operator|.
name|length
argument_list|)
throw|;
block|}
return|return
name|Double
operator|.
name|longBitsToDouble
argument_list|(
name|LongConverter
operator|.
name|getLong
argument_list|(
name|bytes
argument_list|)
argument_list|)
return|;
block|}
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|Double
name|d
parameter_list|)
block|{
return|return
name|LongConverter
operator|.
name|getBytes
argument_list|(
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|d
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Double
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|getDouble
argument_list|(
name|bytes
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
name|Double
name|value
parameter_list|)
block|{
return|return
name|getBytes
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

