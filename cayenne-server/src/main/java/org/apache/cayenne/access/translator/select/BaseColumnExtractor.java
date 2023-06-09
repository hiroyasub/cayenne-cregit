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
name|access
operator|.
name|translator
operator|.
name|select
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
name|map
operator|.
name|DbAttribute
import|;
end_import

begin_import
import|import static
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
name|SQLBuilder
operator|.
name|table
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|abstract
class|class
name|BaseColumnExtractor
implements|implements
name|ColumnExtractor
block|{
specifier|protected
specifier|final
name|TranslatorContext
name|context
decl_stmt|;
name|BaseColumnExtractor
parameter_list|(
name|TranslatorContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|protected
name|void
name|addDbAttribute
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|labelPrefix
parameter_list|,
name|DbAttribute
name|dba
parameter_list|)
block|{
name|String
name|alias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForPath
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
name|String
name|dataRowKey
init|=
name|labelPrefix
operator|!=
literal|null
condition|?
name|labelPrefix
operator|+
literal|'.'
operator|+
name|dba
operator|.
name|getName
argument_list|()
else|:
name|dba
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Node
name|columnNode
init|=
name|table
argument_list|(
name|alias
argument_list|)
operator|.
name|column
argument_list|(
name|dba
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|context
operator|.
name|addResultNode
argument_list|(
name|columnNode
argument_list|,
name|dataRowKey
argument_list|)
operator|.
name|setDbAttribute
argument_list|(
name|dba
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

