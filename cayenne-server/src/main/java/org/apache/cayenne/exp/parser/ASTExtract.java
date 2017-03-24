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
operator|.
name|parser
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
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|Expression
import|;
end_import

begin_class
specifier|public
class|class
name|ASTExtract
extends|extends
name|ASTFunctionCall
block|{
comment|/**      * Available components of date/time.      * Names must be in sync with tokens used in dateTimeExtractingFunction() rule in ExpressionParser.jjt      */
specifier|public
enum|enum
name|DateTimePart
block|{
name|YEAR
block|,
name|MONTH
block|,
name|WEEK
block|,
comment|// day options, day is synonym for dayOfMonth
name|DAY_OF_YEAR
block|,
name|DAY
block|,
name|DAY_OF_MONTH
block|,
name|DAY_OF_WEEK
block|,
name|HOUR
block|,
name|MINUTE
block|,
name|SECOND
block|}
comment|/**      * Map from camelCase name to enum elements.      * @see ASTFunctionCall#nameToCamelCase(String)      */
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DateTimePart
argument_list|>
name|NAME_TO_PART
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
for|for
control|(
name|DateTimePart
name|part
range|:
name|DateTimePart
operator|.
name|values
argument_list|()
control|)
block|{
name|NAME_TO_PART
operator|.
name|put
argument_list|(
name|nameToCamelCase
argument_list|(
name|part
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|part
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * camelCase name, found in ExpressionParser.jjt tokens      */
specifier|private
name|String
name|partName
decl_stmt|;
specifier|private
name|DateTimePart
name|part
decl_stmt|;
name|ASTExtract
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
literal|"EXTRACT"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTExtract
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTEXTRACT
argument_list|,
literal|"EXTRACT"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFunctionName
parameter_list|()
block|{
return|return
name|part
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendFunctionNameAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|partName
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method is used by {@link ExpressionParser}      * @param partToken {@link Token#image} from {@link ExpressionParser}      */
name|void
name|setPartToken
parameter_list|(
name|String
name|partToken
parameter_list|)
block|{
name|part
operator|=
name|NAME_TO_PART
operator|.
name|get
argument_list|(
name|partToken
argument_list|)
expr_stmt|;
if|if
condition|(
name|part
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown timestamp part: %s"
argument_list|,
name|partToken
argument_list|)
throw|;
block|}
name|this
operator|.
name|partName
operator|=
name|partToken
expr_stmt|;
block|}
comment|/**      * This method is used by FunctionExpressionFactory      * @param part date/time part to extract      */
specifier|public
name|void
name|setPart
parameter_list|(
name|DateTimePart
name|part
parameter_list|)
block|{
name|this
operator|.
name|part
operator|=
name|part
expr_stmt|;
name|this
operator|.
name|partName
operator|=
name|nameToCamelCase
argument_list|(
name|part
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DateTimePart
name|getPart
parameter_list|()
block|{
return|return
name|part
return|;
block|}
specifier|public
name|String
name|getPartCamelCaseName
parameter_list|()
block|{
return|return
name|partName
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
name|ASTExtract
name|copy
init|=
operator|new
name|ASTExtract
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|copy
operator|.
name|partName
operator|=
name|partName
expr_stmt|;
name|copy
operator|.
name|part
operator|=
name|part
expr_stmt|;
return|return
name|copy
return|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getRequiredChildrenCount
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|evaluateSubNode
parameter_list|(
name|Object
name|o
parameter_list|,
name|Object
index|[]
name|evaluatedChildren
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

