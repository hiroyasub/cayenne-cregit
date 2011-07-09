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
name|hsqldb
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
name|jdbc
operator|.
name|ProcedureAction
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
name|ProcedureTranslator
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
name|JdbcActionBuilder
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
name|JdbcAdapter
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
name|EntityResolver
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
name|ProcedureQuery
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
name|SQLAction
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

begin_class
class|class
name|HSQLActionBuilder
extends|extends
name|JdbcActionBuilder
block|{
name|HSQLActionBuilder
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|SQLAction
name|objectSelectAction
parameter_list|(
name|SelectQuery
name|query
parameter_list|)
block|{
return|return
operator|new
name|HSQLSelectAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SQLAction
name|procedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|)
block|{
name|ProcedureAction
name|procedureAction
init|=
operator|new
name|ProcedureAction
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|ProcedureTranslator
name|createTranslator
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|ProcedureTranslator
name|transl
init|=
operator|new
name|HSQLDBProcedureTranslator
argument_list|()
decl_stmt|;
name|transl
operator|.
name|setAdapter
argument_list|(
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|transl
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|transl
operator|.
name|setEntityResolver
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|transl
operator|.
name|setConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|transl
operator|.
name|setJdbcEventLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
return|return
name|transl
return|;
block|}
block|}
decl_stmt|;
return|return
name|procedureAction
return|;
block|}
block|}
end_class

end_unit

