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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|BigIntegerConverterTest
block|{
specifier|private
name|BigInteger
name|positiveInt
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
name|BigInteger
name|negativeInt
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MIN_VALUE
argument_list|)
operator|.
name|subtract
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|Long
operator|.
name|MIN_VALUE
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testConverter
parameter_list|()
block|{
name|BigIntegerConverter
name|converter
init|=
operator|new
name|BigIntegerConverter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|positiveInt
argument_list|,
name|converter
operator|.
name|fromBytes
argument_list|(
name|converter
operator|.
name|toBytes
argument_list|(
name|positiveInt
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConverter_Negative
parameter_list|()
block|{
name|BigIntegerConverter
name|converter
init|=
operator|new
name|BigIntegerConverter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|negativeInt
argument_list|,
name|converter
operator|.
name|fromBytes
argument_list|(
name|converter
operator|.
name|toBytes
argument_list|(
name|negativeInt
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConverter_Zero
parameter_list|()
block|{
name|BigInteger
name|originalValue
init|=
name|BigInteger
operator|.
name|ZERO
decl_stmt|;
name|BigIntegerConverter
name|converter
init|=
operator|new
name|BigIntegerConverter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|originalValue
argument_list|,
name|converter
operator|.
name|fromBytes
argument_list|(
name|converter
operator|.
name|toBytes
argument_list|(
name|originalValue
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

