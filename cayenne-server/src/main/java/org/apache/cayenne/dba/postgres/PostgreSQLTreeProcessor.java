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
name|dba
operator|.
name|postgres
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|LikeNode
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
name|LimitOffsetNode
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
name|TrimmingColumnNode
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
name|dba
operator|.
name|postgres
operator|.
name|sqltree
operator|.
name|PositionFunctionNode
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
name|dba
operator|.
name|postgres
operator|.
name|sqltree
operator|.
name|PostgresExtractFunctionNode
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
name|dba
operator|.
name|postgres
operator|.
name|sqltree
operator|.
name|PostgresLikeNode
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
name|dba
operator|.
name|postgres
operator|.
name|sqltree
operator|.
name|PostgresLimitOffsetNode
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|PostgreSQLTreeProcessor
extends|extends
name|BaseSQLTreeProcessor
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|EXTRACT_FUNCTION_NAMES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"DAY_OF_MONTH"
argument_list|,
literal|"DAY"
argument_list|,
literal|"MONTH"
argument_list|,
literal|"HOUR"
argument_list|,
literal|"WEEK"
argument_list|,
literal|"YEAR"
argument_list|,
literal|"DAY_OF_WEEK"
argument_list|,
literal|"DAY_OF_YEAR"
argument_list|,
literal|"MINUTE"
argument_list|,
literal|"SECOND"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|onLimitOffsetNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|LimitOffsetNode
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
name|PostgresLimitOffsetNode
argument_list|(
name|child
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
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
name|onLikeNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|LikeNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|child
operator|.
name|isIgnoreCase
argument_list|()
condition|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
operator|new
name|PostgresLikeNode
argument_list|(
name|child
operator|.
name|isNot
argument_list|()
argument_list|,
name|child
operator|.
name|getEscape
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|null
decl_stmt|;
name|String
name|functionName
init|=
name|child
operator|.
name|getFunctionName
argument_list|()
decl_stmt|;
if|if
condition|(
name|EXTRACT_FUNCTION_NAMES
operator|.
name|contains
argument_list|(
name|functionName
argument_list|)
condition|)
block|{
name|replacement
operator|=
operator|new
name|PostgresExtractFunctionNode
argument_list|(
name|functionName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"CURRENT_DATE"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
operator|||
literal|"CURRENT_TIME"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
operator|||
literal|"CURRENT_TIMESTAMP"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
condition|)
block|{
name|replacement
operator|=
operator|new
name|FunctionNode
argument_list|(
name|functionName
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"LOCATE"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
condition|)
block|{
name|replacement
operator|=
operator|new
name|PositionFunctionNode
argument_list|(
name|child
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

