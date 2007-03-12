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
name|access
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

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
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Time
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|context
operator|.
name|InternalContextAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|MethodInvocationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|ParseErrorException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|exception
operator|.
name|ResourceNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|directive
operator|.
name|Directive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|parser
operator|.
name|node
operator|.
name|Node
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A custom Velocity directive to describe a ResultSet column. There are the following  * possible invocation formats inside the template:  *   *<pre>  *      #result(column_name) - e.g. #result('ARTIST_ID')  *      #result(column_name java_type) - e.g. #result('ARTIST_ID' 'String')  *      #result(column_name java_type column_alias) - e.g. #result('ARTIST_ID' 'String' 'ID')  *      #result(column_name java_type column_alias data_row_key) - e.g. #result('ARTIST_ID' 'String' 'ID' 'toArtist.ID')  *</pre>  *   *<p>  * 'data_row_key' is needed if SQL 'column_alias' is not appropriate as a DataRow key on  * the Cayenne side. One common case when this happens is when a DataRow retrieved from a  * query is mapped using joint prefetch keys. In this case DataRow must use DB_PATH  * expressions for joint column keys, and their format is incompatible with most databases  * alias format.  *</p>  *<p>  * Most common Java types used in JDBC can be specified without a package. This includes  * all numeric types, primitives, String, SQL dates, BigDecimal and BigInteger.  *</p>  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ResultDirective
extends|extends
name|Directive
block|{
specifier|private
specifier|static
specifier|final
name|Map
name|typesGuess
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
comment|// init default types
comment|// primitives
name|typesGuess
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// numeric
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Long"
argument_list|,
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Double"
argument_list|,
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Byte"
argument_list|,
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Boolean"
argument_list|,
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Float"
argument_list|,
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Short"
argument_list|,
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Integer"
argument_list|,
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// other
name|typesGuess
operator|.
name|put
argument_list|(
literal|"String"
argument_list|,
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Date"
argument_list|,
name|Date
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Time"
argument_list|,
name|Time
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"Timestamp"
argument_list|,
name|Timestamp
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"BigDecimal"
argument_list|,
name|BigDecimal
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typesGuess
operator|.
name|put
argument_list|(
literal|"BigInteger"
argument_list|,
name|BigInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"result"
return|;
block|}
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|LINE
return|;
block|}
specifier|public
name|boolean
name|render
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|Node
name|node
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceNotFoundException
throws|,
name|ParseErrorException
throws|,
name|MethodInvocationException
block|{
name|String
name|column
init|=
name|getChildAsString
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseErrorException
argument_list|(
literal|"Column name expected at line "
operator|+
name|node
operator|.
name|getLine
argument_list|()
operator|+
literal|", column "
operator|+
name|node
operator|.
name|getColumn
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|alias
init|=
name|getChildAsString
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|String
name|dataRowKey
init|=
name|getChildAsString
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|3
argument_list|)
decl_stmt|;
comment|// determine what we want to name this column in a resulting DataRow...
name|String
name|label
init|=
operator|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|dataRowKey
argument_list|)
operator|)
condition|?
name|dataRowKey
else|:
operator|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|alias
argument_list|)
operator|)
condition|?
name|alias
else|:
literal|null
decl_stmt|;
name|ColumnDescriptor
name|columnDescriptor
init|=
operator|new
name|ColumnDescriptor
argument_list|()
decl_stmt|;
name|columnDescriptor
operator|.
name|setName
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|columnDescriptor
operator|.
name|setLabel
argument_list|(
name|label
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|getChildAsString
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|columnDescriptor
operator|.
name|setJavaClass
argument_list|(
name|guessType
argument_list|(
name|type
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
name|column
argument_list|)
expr_stmt|;
comment|// append column alias if needed.
comment|// Note that if table aliases are used, this logic will result in SQL like
comment|// "t0.ARTIST_NAME AS ARTIST_NAME". Doing extra regex matching to handle this
comment|// won't probably buy us much.
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|alias
argument_list|)
operator|&&
operator|!
name|alias
operator|.
name|equals
argument_list|(
name|column
argument_list|)
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
literal|" AS "
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
name|bindResult
argument_list|(
name|context
argument_list|,
name|columnDescriptor
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|protected
name|Object
name|getChild
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Node
name|node
parameter_list|,
name|int
name|i
parameter_list|)
throws|throws
name|MethodInvocationException
block|{
return|return
operator|(
name|i
operator|>=
literal|0
operator|&&
name|i
operator|<
name|node
operator|.
name|jjtGetNumChildren
argument_list|()
operator|)
condition|?
name|node
operator|.
name|jjtGetChild
argument_list|(
name|i
argument_list|)
operator|.
name|value
argument_list|(
name|context
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Returns a directive argument at a given index converted to String.      *       * @since 1.2      */
specifier|protected
name|String
name|getChildAsString
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Node
name|node
parameter_list|,
name|int
name|i
parameter_list|)
throws|throws
name|MethodInvocationException
block|{
name|Object
name|value
init|=
name|getChild
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
name|i
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Converts "short" type notation to the fully qualified class name. Right now      * supports all major standard SQL types, including primitives. All other types are      * expected to be fully qualified, and are not converted.      */
specifier|protected
name|String
name|guessType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|String
name|guessed
init|=
operator|(
name|String
operator|)
name|typesGuess
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|guessed
operator|!=
literal|null
condition|?
name|guessed
else|:
name|type
return|;
block|}
comment|/**      * Adds value to the list of result columns in the context.      */
specifier|protected
name|void
name|bindResult
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|ColumnDescriptor
name|columnDescriptor
parameter_list|)
block|{
name|Collection
name|resultColumns
init|=
operator|(
name|Collection
operator|)
name|context
operator|.
name|getInternalUserContext
argument_list|()
operator|.
name|get
argument_list|(
name|SQLTemplateProcessor
operator|.
name|RESULT_COLUMNS_LIST_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultColumns
operator|!=
literal|null
condition|)
block|{
name|resultColumns
operator|.
name|add
argument_list|(
name|columnDescriptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

