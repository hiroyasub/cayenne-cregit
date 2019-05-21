begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|hsqldb
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ColumnNode
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|FunctionNode
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|OpExpressionNode
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
name|access
operator|.
name|translator
operator|.
name|select
operator|.
name|BaseSQLTreeProcessor
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|TrimmingColumnNode
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|HSQLTreeProcessor
extends|extends
name|BaseSQLTreeProcessor
block|{
annotation|@
name|Override
specifier|protected
name|void
name|onColumnNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|ColumnNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
operator|new
name|TrimmingColumnNode
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onFunctionNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|FunctionNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|Node
name|replacement
init|=
name|getReplacementForFunction
argument_list|(
name|child
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacement
operator|!=
literal|null
condition|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Node
name|getReplacementForFunction
parameter_list|(
name|FunctionNode
name|child
parameter_list|)
block|{
switch|switch
condition|(
name|child
operator|.
name|getFunctionName
argument_list|()
condition|)
block|{
case|case
literal|"DAY_OF_MONTH"
case|:
case|case
literal|"DAY_OF_WEEK"
case|:
case|case
literal|"DAY_OF_YEAR"
case|:
comment|// hsqldb variants are without '_'
return|return
operator|new
name|FunctionNode
argument_list|(
name|child
operator|.
name|getFunctionName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"_"
argument_list|,
literal|""
argument_list|)
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
case|case
literal|"CURRENT_DATE"
case|:
case|case
literal|"CURRENT_TIMESTAMP"
case|:
return|return
operator|new
name|FunctionNode
argument_list|(
name|child
operator|.
name|getFunctionName
argument_list|()
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
case|case
literal|"CURRENT_TIME"
case|:
comment|// from documentation:
comment|// CURRENT_TIME returns a value of TIME WITH TIME ZONE type.
comment|// LOCALTIME returns a value of TIME type.
comment|// CURTIME() is a synonym for LOCALTIME.
comment|// use LOCALTIME to better align with other DBs
return|return
operator|new
name|FunctionNode
argument_list|(
literal|"LOCALTIME"
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
case|case
literal|"CONCAT"
case|:
return|return
operator|new
name|OpExpressionNode
argument_list|(
literal|"||"
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

