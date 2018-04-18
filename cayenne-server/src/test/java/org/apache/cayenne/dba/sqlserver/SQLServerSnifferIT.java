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
name|sqlserver
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
name|configuration
operator|.
name|server
operator|.
name|PkGeneratorFactoryProvider
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
name|DbAdapter
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
name|di
operator|.
name|AdhocObjectFactory
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
name|di
operator|.
name|Inject
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
name|unit
operator|.
name|SQLServerUnitDbAdapter
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
name|unit
operator|.
name|UnitDbAdapter
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCaseDataSourceFactory
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|SQLServerSnifferIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PkGeneratorFactoryProvider
name|pkGeneratorProvider
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testCreateAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLServerSniffer
name|sniffer
init|=
operator|new
name|SQLServerSniffer
argument_list|(
name|objectFactory
argument_list|,
name|pkGeneratorProvider
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
literal|null
decl_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
init|)
block|{
name|adapter
operator|=
name|sniffer
operator|.
name|createAdapter
argument_list|(
name|c
operator|.
name|getMetaData
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|accessStackAdapter
operator|instanceof
name|SQLServerUnitDbAdapter
condition|)
block|{
name|assertNotNull
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertNull
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

