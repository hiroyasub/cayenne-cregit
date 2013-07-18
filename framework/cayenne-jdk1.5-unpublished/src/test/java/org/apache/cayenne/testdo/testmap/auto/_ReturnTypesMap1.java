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
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * Class _ReturnTypesMap1 was generated by Cayenne.  * It is probably a good idea to avoid changing this class manually,  * since it may be overwritten next time code is regenerated.  * If you need to make any customizations, please use subclass.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|_ReturnTypesMap1
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
name|BIGINT_COLUMN_PROPERTY
init|=
literal|"bigintColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BIT_COLUMN_PROPERTY
init|=
literal|"bitColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|BOOLEAN_COLUMN_PROPERTY
init|=
literal|"booleanColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|CHAR_COLUMN_PROPERTY
init|=
literal|"charColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DATE_COLUMN_PROPERTY
init|=
literal|"dateColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DECIMAL_COLUMN_PROPERTY
init|=
literal|"decimalColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DOUBLE_COLUMN_PROPERTY
init|=
literal|"doubleColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|FLOAT_COLUMN_PROPERTY
init|=
literal|"floatColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|INTEGER_COLUMN_PROPERTY
init|=
literal|"integerColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|LONGVARCHAR_COLUMN_PROPERTY
init|=
literal|"longvarcharColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|NUMERIC_COLUMN_PROPERTY
init|=
literal|"numericColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|REAL_COLUMN_PROPERTY
init|=
literal|"realColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|SMALLINT_COLUMN_PROPERTY
init|=
literal|"smallintColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|TIME_COLUMN_PROPERTY
init|=
literal|"timeColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_COLUMN_PROPERTY
init|=
literal|"timestampColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|TINYINT_COLUMN_PROPERTY
init|=
literal|"tinyintColumn"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|VARCHAR_COLUMN_PROPERTY
init|=
literal|"varcharColumn"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AAAID_PK_COLUMN
init|=
literal|"AAAID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Long
argument_list|>
name|BIGINT_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Long
argument_list|>
argument_list|(
literal|"bigintColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Boolean
argument_list|>
name|BIT_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Boolean
argument_list|>
argument_list|(
literal|"bitColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Boolean
argument_list|>
name|BOOLEAN_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Boolean
argument_list|>
argument_list|(
literal|"booleanColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|CHAR_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"charColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|DATE_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Date
argument_list|>
argument_list|(
literal|"dateColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|BigDecimal
argument_list|>
name|DECIMAL_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|BigDecimal
argument_list|>
argument_list|(
literal|"decimalColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Double
argument_list|>
name|DOUBLE_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Double
argument_list|>
argument_list|(
literal|"doubleColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Float
argument_list|>
name|FLOAT_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Float
argument_list|>
argument_list|(
literal|"floatColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Integer
argument_list|>
name|INTEGER_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|"integerColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|LONGVARCHAR_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"longvarcharColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|BigDecimal
argument_list|>
name|NUMERIC_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|BigDecimal
argument_list|>
argument_list|(
literal|"numericColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Float
argument_list|>
name|REAL_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Float
argument_list|>
argument_list|(
literal|"realColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Short
argument_list|>
name|SMALLINT_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Short
argument_list|>
argument_list|(
literal|"smallintColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|TIME_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Date
argument_list|>
argument_list|(
literal|"timeColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Date
argument_list|>
name|TIMESTAMP_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Date
argument_list|>
argument_list|(
literal|"timestampColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|Byte
argument_list|>
name|TINYINT_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|Byte
argument_list|>
argument_list|(
literal|"tinyintColumn"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Property
argument_list|<
name|String
argument_list|>
name|VARCHAR_COLUMN
init|=
operator|new
name|Property
argument_list|<
name|String
argument_list|>
argument_list|(
literal|"varcharColumn"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setBigintColumn
parameter_list|(
name|Long
name|bigintColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"bigintColumn"
argument_list|,
name|bigintColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Long
name|getBigintColumn
parameter_list|()
block|{
return|return
operator|(
name|Long
operator|)
name|readProperty
argument_list|(
literal|"bigintColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setBitColumn
parameter_list|(
name|Boolean
name|bitColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"bitColumn"
argument_list|,
name|bitColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getBitColumn
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|readProperty
argument_list|(
literal|"bitColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setBooleanColumn
parameter_list|(
name|Boolean
name|booleanColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"booleanColumn"
argument_list|,
name|booleanColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Boolean
name|getBooleanColumn
parameter_list|()
block|{
return|return
operator|(
name|Boolean
operator|)
name|readProperty
argument_list|(
literal|"booleanColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setCharColumn
parameter_list|(
name|String
name|charColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"charColumn"
argument_list|,
name|charColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getCharColumn
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"charColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDateColumn
parameter_list|(
name|Date
name|dateColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"dateColumn"
argument_list|,
name|dateColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getDateColumn
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"dateColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDecimalColumn
parameter_list|(
name|BigDecimal
name|decimalColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"decimalColumn"
argument_list|,
name|decimalColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BigDecimal
name|getDecimalColumn
parameter_list|()
block|{
return|return
operator|(
name|BigDecimal
operator|)
name|readProperty
argument_list|(
literal|"decimalColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDoubleColumn
parameter_list|(
name|Double
name|doubleColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"doubleColumn"
argument_list|,
name|doubleColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Double
name|getDoubleColumn
parameter_list|()
block|{
return|return
operator|(
name|Double
operator|)
name|readProperty
argument_list|(
literal|"doubleColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setFloatColumn
parameter_list|(
name|Float
name|floatColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"floatColumn"
argument_list|,
name|floatColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Float
name|getFloatColumn
parameter_list|()
block|{
return|return
operator|(
name|Float
operator|)
name|readProperty
argument_list|(
literal|"floatColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setIntegerColumn
parameter_list|(
name|Integer
name|integerColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"integerColumn"
argument_list|,
name|integerColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getIntegerColumn
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|readProperty
argument_list|(
literal|"integerColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setLongvarcharColumn
parameter_list|(
name|String
name|longvarcharColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"longvarcharColumn"
argument_list|,
name|longvarcharColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLongvarcharColumn
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"longvarcharColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setNumericColumn
parameter_list|(
name|BigDecimal
name|numericColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"numericColumn"
argument_list|,
name|numericColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BigDecimal
name|getNumericColumn
parameter_list|()
block|{
return|return
operator|(
name|BigDecimal
operator|)
name|readProperty
argument_list|(
literal|"numericColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setRealColumn
parameter_list|(
name|Float
name|realColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"realColumn"
argument_list|,
name|realColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Float
name|getRealColumn
parameter_list|()
block|{
return|return
operator|(
name|Float
operator|)
name|readProperty
argument_list|(
literal|"realColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSmallintColumn
parameter_list|(
name|Short
name|smallintColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"smallintColumn"
argument_list|,
name|smallintColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Short
name|getSmallintColumn
parameter_list|()
block|{
return|return
operator|(
name|Short
operator|)
name|readProperty
argument_list|(
literal|"smallintColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTimeColumn
parameter_list|(
name|Date
name|timeColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"timeColumn"
argument_list|,
name|timeColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getTimeColumn
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"timeColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTimestampColumn
parameter_list|(
name|Date
name|timestampColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"timestampColumn"
argument_list|,
name|timestampColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Date
name|getTimestampColumn
parameter_list|()
block|{
return|return
operator|(
name|Date
operator|)
name|readProperty
argument_list|(
literal|"timestampColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTinyintColumn
parameter_list|(
name|Byte
name|tinyintColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"tinyintColumn"
argument_list|,
name|tinyintColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Byte
name|getTinyintColumn
parameter_list|()
block|{
return|return
operator|(
name|Byte
operator|)
name|readProperty
argument_list|(
literal|"tinyintColumn"
argument_list|)
return|;
block|}
specifier|public
name|void
name|setVarcharColumn
parameter_list|(
name|String
name|varcharColumn
parameter_list|)
block|{
name|writeProperty
argument_list|(
literal|"varcharColumn"
argument_list|,
name|varcharColumn
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVarcharColumn
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|readProperty
argument_list|(
literal|"varcharColumn"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

