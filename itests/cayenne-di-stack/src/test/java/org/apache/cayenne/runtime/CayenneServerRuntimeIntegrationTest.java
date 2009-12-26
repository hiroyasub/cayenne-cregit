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
name|runtime
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
name|Cayenne
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
name|ObjectContext
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
name|DataDomain
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
name|itest
operator|.
name|ItestDBUtils
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
name|itest
operator|.
name|di_stack
operator|.
name|Table1
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
specifier|public
class|class
name|CayenneServerRuntimeIntegrationTest
extends|extends
name|CayenneServerRuntimeCase
block|{
annotation|@
name|Override
specifier|protected
name|RuntimeName
name|getRuntimeName
parameter_list|()
block|{
return|return
name|RuntimeName
operator|.
name|DEFAULT
return|;
block|}
specifier|public
name|void
name|testGetDomain_singleton
parameter_list|()
block|{
name|DataDomain
name|domain1
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|domain1
argument_list|)
expr_stmt|;
name|DataDomain
name|domain2
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|domain2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|domain1
argument_list|,
name|domain2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNewContext_notSingleton
parameter_list|()
block|{
name|ObjectContext
name|context1
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context1
argument_list|)
expr_stmt|;
name|ObjectContext
name|context2
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|context2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|context1
argument_list|,
name|context2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNewContext_separateObjects
parameter_list|()
throws|throws
name|Exception
block|{
name|ItestDBUtils
name|dbUtils
init|=
name|getDbUtils
argument_list|()
decl_stmt|;
name|dbUtils
operator|.
name|deleteAll
argument_list|(
literal|"TABLE1"
argument_list|)
expr_stmt|;
name|dbUtils
operator|.
name|insert
argument_list|(
literal|"TABLE1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"ID"
block|,
literal|"NAME"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"Abc"
block|}
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectContext
name|context1
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|context2
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Table1
name|o1
init|=
operator|(
name|Table1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context1
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|Table1
name|o2
init|=
operator|(
name|Table1
operator|)
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context2
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Abc"
argument_list|,
name|o1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|o2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

