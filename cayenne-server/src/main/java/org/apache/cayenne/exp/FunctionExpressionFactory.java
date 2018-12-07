begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
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
name|exp
operator|.
name|parser
operator|.
name|ASTAbs
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
name|parser
operator|.
name|ASTAvg
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
name|parser
operator|.
name|ASTConcat
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
name|parser
operator|.
name|ASTCount
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
name|parser
operator|.
name|ASTCurrentDate
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
name|parser
operator|.
name|ASTCurrentTime
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
name|parser
operator|.
name|ASTCurrentTimestamp
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
name|parser
operator|.
name|ASTDistinct
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
name|parser
operator|.
name|ASTExtract
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
name|parser
operator|.
name|ASTLength
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
name|parser
operator|.
name|ASTLocate
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
name|parser
operator|.
name|ASTLower
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
name|parser
operator|.
name|ASTMax
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
name|parser
operator|.
name|ASTMin
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
name|parser
operator|.
name|ASTMod
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
name|parser
operator|.
name|ASTObjPath
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
name|parser
operator|.
name|ASTScalar
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
name|parser
operator|.
name|ASTSqrt
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
name|parser
operator|.
name|ASTSubstring
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
name|parser
operator|.
name|ASTSum
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
name|parser
operator|.
name|ASTTrim
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
name|parser
operator|.
name|ASTUpper
import|;
end_import

begin_comment
comment|/**  * Collection of factory methods to create function call expressions.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|FunctionExpressionFactory
block|{
comment|/**      * Call SUBSTRING(string, offset, length) function      *      * @param exp expression that must evaluate to string      * @param offset start offset of substring      * @param length length of substring      * @return SUBSTRING() call expression      */
specifier|public
specifier|static
name|Expression
name|substringExp
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|substringExp
argument_list|(
name|exp
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
name|offset
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
name|length
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Call SUBSTRING(string, offset, length) function      *      * @param path Object path value      * @param offset start offset of substring      * @param length length of substring      * @return SUBSTRING() call expression      */
specifier|public
specifier|static
name|Expression
name|substringExp
parameter_list|(
name|String
name|path
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|substringExp
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
name|offset
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
name|length
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Call SUBSTRING(string, offset, length) function      *      * @param exp expression that must evaluate to string      * @param offset start offset of substring must evaluate to int      * @param length length of substring must evaluate to int      * @return SUBSTRING() call expression      */
specifier|public
specifier|static
name|Expression
name|substringExp
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|Expression
name|offset
parameter_list|,
name|Expression
name|length
parameter_list|)
block|{
return|return
operator|new
name|ASTSubstring
argument_list|(
name|exp
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
comment|/**      * @param exp string expression to trim      * @return TRIM() call expression      */
specifier|public
specifier|static
name|Expression
name|trimExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTTrim
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @return TRIM() call expression      */
specifier|public
specifier|static
name|Expression
name|trimExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|ASTTrim
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param exp string expression      * @return LOWER() call expression      */
specifier|public
specifier|static
name|Expression
name|lowerExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTLower
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @return LOWER() call expression      */
specifier|public
specifier|static
name|Expression
name|lowerExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|ASTLower
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param exp string expression      * @return UPPER() call expression      */
specifier|public
specifier|static
name|Expression
name|upperExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTUpper
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @return UPPER() call expression      */
specifier|public
specifier|static
name|Expression
name|upperExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|ASTUpper
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param exp string expression      * @return LENGTH() call expression      */
specifier|public
specifier|static
name|Expression
name|lengthExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTLength
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @return LENGTH() call expression      */
specifier|public
specifier|static
name|Expression
name|lengthExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|ASTLength
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Call LOCATE(substring, string) function that return position      * of substring in string or 0 if it is not found.      *      * @param substring object path value      * @param exp string expression      * @return LOCATE() call expression      */
specifier|public
specifier|static
name|Expression
name|locateExp
parameter_list|(
name|String
name|substring
parameter_list|,
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|locateExp
argument_list|(
operator|new
name|ASTScalar
argument_list|(
name|substring
argument_list|)
argument_list|,
name|exp
argument_list|)
return|;
block|}
comment|/**      * Call LOCATE(substring, string) function that return position      * of substring in string or 0 if it is not found.      *      * @param substring object path value      * @param path object path      * @return LOCATE() call expression      */
specifier|public
specifier|static
name|Expression
name|locateExp
parameter_list|(
name|String
name|substring
parameter_list|,
name|String
name|path
parameter_list|)
block|{
return|return
name|locateExp
argument_list|(
operator|new
name|ASTScalar
argument_list|(
name|substring
argument_list|)
argument_list|,
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Call LOCATE(substring, string) function that return position      * of substring in string or 0 if it is not found.      *      * @param substring string expression      * @param exp string expression      * @return LOCATE() call expression      */
specifier|public
specifier|static
name|Expression
name|locateExp
parameter_list|(
name|Expression
name|substring
parameter_list|,
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTLocate
argument_list|(
name|substring
argument_list|,
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param exp numeric expression      * @return ABS() call expression      */
specifier|public
specifier|static
name|Expression
name|absExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTAbs
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @return ABS() call expression      */
specifier|public
specifier|static
name|Expression
name|absExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|ASTAbs
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param exp numeric expression      * @return SQRT() call expression      */
specifier|public
specifier|static
name|Expression
name|sqrtExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTSqrt
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @return SQRT() call expression      */
specifier|public
specifier|static
name|Expression
name|sqrtExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|ASTSqrt
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param exp numeric expression      * @param number divisor      * @return MOD() call expression      */
specifier|public
specifier|static
name|Expression
name|modExp
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|Number
name|number
parameter_list|)
block|{
return|return
name|modExp
argument_list|(
name|exp
argument_list|,
operator|new
name|ASTScalar
argument_list|(
name|number
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param path object path value      * @param number divisor      * @return MOD() call expression      */
specifier|public
specifier|static
name|Expression
name|modExp
parameter_list|(
name|String
name|path
parameter_list|,
name|Number
name|number
parameter_list|)
block|{
return|return
name|modExp
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
name|path
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
name|number
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param exp object path value      * @param number numeric expression      * @return MOD() call expression      */
specifier|public
specifier|static
name|Expression
name|modExp
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|Expression
name|number
parameter_list|)
block|{
return|return
operator|new
name|ASTMod
argument_list|(
name|exp
argument_list|,
name|number
argument_list|)
return|;
block|}
comment|/**      *<p>      * Factory method for expression to call CONCAT(string1, string2, ...) function      *</p>      *<p>      * Can be used like:<pre>      *  Expression concat = concatExp(SomeClass.POPERTY_1.getPath(), SomeClass.PROPERTY_2.getPath());      *</pre>      *</p>      *<p>      * SQL generation note:      *<ul>      *<li> if DB supports CONCAT function with vararg then it will be used      *<li> if DB supports CONCAT function with two args but also supports concat operator, then operator (eg ||) will be used      *<li> if DB supports only CONCAT function with two args then it will be used what can lead to SQL exception if      * used with more than two arguments      *</ul>      *</p>      *<p>Currently only known DB with limited concatenation functionality is Openbase.</p>      *      * @param expressions array of expressions      * @return CONCAT() call expression      */
specifier|public
specifier|static
name|Expression
name|concatExp
parameter_list|(
name|Expression
modifier|...
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|ASTConcat
argument_list|()
return|;
block|}
return|return
operator|new
name|ASTConcat
argument_list|(
name|expressions
argument_list|)
return|;
block|}
comment|/**      *<p>      * Factory method for expression to call CONCAT(string1, string2, ...) function      *</p>      *<p>      * Can be used like:<pre>      *  Expression concat = concatExp("property1", "property2");      *</pre>      *</p>      *<p>      * SQL generation note:      *<ul>      *<li> if DB supports CONCAT function with vararg then it will be used      *<li> if DB supports CONCAT function with two args but also supports concat operator, then operator (eg ||) will be used      *<li> if DB supports only CONCAT function with two args then it will be used what can lead to SQL exception if      * used with more than two arguments      *</ul>      *</p>      *<p>Currently only Openbase DB has limited concatenation functionality.</p>      *      * @param paths array of paths      * @return CONCAT() call expression      */
specifier|public
specifier|static
name|Expression
name|concatExp
parameter_list|(
name|String
modifier|...
name|paths
parameter_list|)
block|{
if|if
condition|(
name|paths
operator|==
literal|null
operator|||
name|paths
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|ASTConcat
argument_list|()
return|;
block|}
name|Expression
index|[]
name|expressions
init|=
operator|new
name|Expression
index|[
name|paths
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|paths
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|expressions
index|[
name|i
index|]
operator|=
operator|new
name|ASTObjPath
argument_list|(
name|paths
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ASTConcat
argument_list|(
name|expressions
argument_list|)
return|;
block|}
comment|/**      * @return Expression COUNT(&ast;)      */
specifier|public
specifier|static
name|Expression
name|countExp
parameter_list|()
block|{
return|return
operator|new
name|ASTCount
argument_list|()
return|;
block|}
comment|/**      * @return Expression COUNT(exp)      */
specifier|public
specifier|static
name|Expression
name|countExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTCount
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @return Expression COUNT(DISTINCT(exp))      */
specifier|public
specifier|static
name|Expression
name|countDistinctExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTCount
argument_list|(
operator|new
name|ASTDistinct
argument_list|(
name|exp
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @return Expression MIN(exp)      */
specifier|public
specifier|static
name|Expression
name|minExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTMin
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @return Expression MAX(exp)      */
specifier|public
specifier|static
name|Expression
name|maxExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTMax
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @return Expression AVG(exp)      */
specifier|public
specifier|static
name|Expression
name|avgExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTAvg
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @return SUM(exp) expression      */
specifier|public
specifier|static
name|Expression
name|sumExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
operator|new
name|ASTSum
argument_list|(
name|exp
argument_list|)
return|;
block|}
comment|/**      * @return CURRENT_DATE expression      */
specifier|public
specifier|static
name|Expression
name|currentDate
parameter_list|()
block|{
return|return
operator|new
name|ASTCurrentDate
argument_list|()
return|;
block|}
comment|/**      * @return CURRENT_TIME expression      */
specifier|public
specifier|static
name|Expression
name|currentTime
parameter_list|()
block|{
return|return
operator|new
name|ASTCurrentTime
argument_list|()
return|;
block|}
comment|/**      * @return CURRENT_TIMESTAMP expression      */
specifier|public
specifier|static
name|Expression
name|currentTimestamp
parameter_list|()
block|{
return|return
operator|new
name|ASTCurrentTimestamp
argument_list|()
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return year(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|yearExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|YEAR
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return year(path) function expression      */
specifier|public
specifier|static
name|Expression
name|yearExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|YEAR
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return month(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|monthExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|MONTH
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return month(path) function expression      */
specifier|public
specifier|static
name|Expression
name|monthExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|MONTH
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return week(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|weekExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|WEEK
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return week(path) function expression      */
specifier|public
specifier|static
name|Expression
name|weekExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|WEEK
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return dayOfYear(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|dayOfYearExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|DAY_OF_YEAR
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return dayOfYear(path) function expression      */
specifier|public
specifier|static
name|Expression
name|dayOfYearExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|DAY_OF_YEAR
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return dayOfMonth(exp) function expression, synonym for day()      */
specifier|public
specifier|static
name|Expression
name|dayOfMonthExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|DAY_OF_MONTH
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return dayOfMonth(path) function expression, synonym for day()      */
specifier|public
specifier|static
name|Expression
name|dayOfMonthExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|DAY_OF_MONTH
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return dayOfWeek(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|dayOfWeekExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|DAY_OF_WEEK
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return dayOfWeek(path) function expression      */
specifier|public
specifier|static
name|Expression
name|dayOfWeekExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|DAY_OF_WEEK
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return hour(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|hourExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|HOUR
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return hour(path) function expression      */
specifier|public
specifier|static
name|Expression
name|hourExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|HOUR
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return minute(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|minuteExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|MINUTE
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return minute(path) function expression      */
specifier|public
specifier|static
name|Expression
name|minuteExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|MINUTE
argument_list|)
return|;
block|}
comment|/**      * @param exp date/timestamp expression      * @return second(exp) function expression      */
specifier|public
specifier|static
name|Expression
name|secondExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|exp
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|SECOND
argument_list|)
return|;
block|}
comment|/**      * @param path String path      * @return second(path) function expression      */
specifier|public
specifier|static
name|Expression
name|secondExp
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|path
argument_list|,
name|ASTExtract
operator|.
name|DateTimePart
operator|.
name|SECOND
argument_list|)
return|;
block|}
specifier|static
name|Expression
name|extractExp
parameter_list|(
name|String
name|path
parameter_list|,
name|ASTExtract
operator|.
name|DateTimePart
name|part
parameter_list|)
block|{
return|return
name|extractExp
argument_list|(
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
name|path
argument_list|)
argument_list|,
name|part
argument_list|)
return|;
block|}
specifier|static
name|Expression
name|extractExp
parameter_list|(
name|Expression
name|exp
parameter_list|,
name|ASTExtract
operator|.
name|DateTimePart
name|part
parameter_list|)
block|{
name|ASTExtract
name|extract
init|=
operator|new
name|ASTExtract
argument_list|(
name|exp
argument_list|)
decl_stmt|;
name|extract
operator|.
name|setPart
argument_list|(
name|part
argument_list|)
expr_stmt|;
return|return
name|extract
return|;
block|}
block|}
end_class

end_unit

