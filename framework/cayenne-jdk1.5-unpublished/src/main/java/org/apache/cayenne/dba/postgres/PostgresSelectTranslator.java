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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|trans
operator|.
name|SelectTranslator
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
name|query
operator|.
name|QueryMetadata
import|;
end_import

begin_comment
comment|/**  * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PostgresSelectTranslator
extends|extends
name|SelectTranslator
block|{
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|super
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
name|QueryMetadata
name|metadata
init|=
name|getQuery
argument_list|()
operator|.
name|getMetaData
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
comment|// limit results
name|int
name|offset
init|=
name|metadata
operator|.
name|getFetchStartIndex
argument_list|()
decl_stmt|;
name|int
name|limit
init|=
name|metadata
operator|.
name|getFetchLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|offset
operator|>
literal|0
operator|||
name|limit
operator|>
literal|0
condition|)
block|{
name|sql
operator|+=
literal|" LIMIT "
expr_stmt|;
if|if
condition|(
name|limit
operator|==
literal|0
condition|)
block|{
name|limit
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
name|sql
operator|+=
name|limit
operator|+
literal|" OFFSET "
operator|+
name|offset
expr_stmt|;
block|}
return|return
name|sql
return|;
block|}
block|}
end_class

end_unit

