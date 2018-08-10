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
name|LocalDate
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|CayenneCryptoException
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|LocalDateTimeConverter
implements|implements
name|BytesConverter
argument_list|<
name|LocalDateTime
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|LocalDateTime
argument_list|>
name|INSTANCE
init|=
operator|new
name|LocalDateTimeConverter
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
name|LocalDateTimeConverter
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
name|LocalDateTime
name|fromBytes
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
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected number of bytes: "
operator|+
name|bytes
operator|.
name|length
argument_list|)
throw|;
block|}
comment|// long values converted to varying length byte arrays, so first byte is length
name|int
name|dateLength
init|=
name|bytes
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|dateLength
operator|<=
literal|0
operator|||
name|dateLength
operator|>=
name|bytes
operator|.
name|length
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Corrupted data for LocalDateTime: wrong encoded length"
argument_list|)
throw|;
block|}
name|int
name|timeLength
init|=
name|bytes
operator|.
name|length
operator|-
literal|1
operator|-
name|dateLength
decl_stmt|;
name|byte
index|[]
name|date
init|=
operator|new
name|byte
index|[
name|dateLength
index|]
decl_stmt|;
name|byte
index|[]
name|time
init|=
operator|new
name|byte
index|[
name|timeLength
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bytes
argument_list|,
literal|1
argument_list|,
name|date
argument_list|,
literal|0
argument_list|,
name|dateLength
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|bytes
argument_list|,
name|dateLength
operator|+
literal|1
argument_list|,
name|time
argument_list|,
literal|0
argument_list|,
name|timeLength
argument_list|)
expr_stmt|;
name|LocalDate
name|localDate
init|=
name|LocalDate
operator|.
name|ofEpochDay
argument_list|(
name|longConverter
operator|.
name|fromBytes
argument_list|(
name|date
argument_list|)
argument_list|)
decl_stmt|;
name|LocalTime
name|localTime
init|=
name|LocalTime
operator|.
name|ofNanoOfDay
argument_list|(
name|longConverter
operator|.
name|fromBytes
argument_list|(
name|time
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|LocalDateTime
operator|.
name|of
argument_list|(
name|localDate
argument_list|,
name|localTime
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
name|LocalDateTime
name|value
parameter_list|)
block|{
name|byte
index|[]
name|date
init|=
name|longConverter
operator|.
name|toBytes
argument_list|(
name|value
operator|.
name|toLocalDate
argument_list|()
operator|.
name|toEpochDay
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|time
init|=
name|longConverter
operator|.
name|toBytes
argument_list|(
name|value
operator|.
name|toLocalTime
argument_list|()
operator|.
name|toNanoOfDay
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|datetime
init|=
operator|new
name|byte
index|[
name|date
operator|.
name|length
operator|+
name|time
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
comment|// store date part length as first byte
name|datetime
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
name|date
operator|.
name|length
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|date
argument_list|,
literal|0
argument_list|,
name|datetime
argument_list|,
literal|1
argument_list|,
name|date
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|time
argument_list|,
literal|0
argument_list|,
name|datetime
argument_list|,
name|date
operator|.
name|length
operator|+
literal|1
argument_list|,
name|time
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|datetime
return|;
block|}
block|}
end_class

end_unit

