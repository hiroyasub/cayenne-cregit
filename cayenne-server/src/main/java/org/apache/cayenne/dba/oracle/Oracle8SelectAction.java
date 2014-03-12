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
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|DataNode
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|Oracle8SelectAction
extends|extends
name|OracleSelectAction
block|{
parameter_list|<
name|T
parameter_list|>
name|Oracle8SelectAction
parameter_list|(
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|query
parameter_list|,
name|DataNode
name|dataNode
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|SelectTranslator
name|createTranslator
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|SelectTranslator
name|translator
init|=
operator|new
name|Oracle8SelectTranslator
argument_list|()
decl_stmt|;
name|translator
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setAdapter
argument_list|(
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setEntityResolver
argument_list|(
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setJdbcEventLogger
argument_list|(
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
block|}
end_class

end_unit

