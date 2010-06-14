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
name|unit
operator|.
name|di
operator|.
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|dbsync
operator|.
name|SkipSchemaUpdateStrategy
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
name|configuration
operator|.
name|server
operator|.
name|DataDomainProvider
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
name|map
operator|.
name|DataMap
import|;
end_import

begin_class
class|class
name|ServerCaseDataDomainProvider
extends|extends
name|DataDomainProvider
block|{
annotation|@
name|Inject
specifier|protected
name|DataSource
name|dataSource
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|DataDomain
name|createDataDomain
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|domain
init|=
name|super
operator|.
name|createDataDomain
argument_list|()
decl_stmt|;
comment|// add nodes dynamically
comment|// TODO: andrus, 06/14/2010 should probably map them in XML to avoid this mess...
for|for
control|(
name|DataMap
name|dataMap
range|:
name|domain
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|addDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|node
operator|.
name|setSchemaUpdateStrategy
argument_list|(
operator|new
name|SkipSchemaUpdateStrategy
argument_list|()
argument_list|)
expr_stmt|;
comment|// customizations from SimpleAccessStackAdapter that are not yet ported...
comment|// those can be done better now
comment|// node
comment|// .getAdapter()
comment|// .getExtendedTypes()
comment|// .registerType(new StringET1ExtendedType());
comment|//
comment|// // tweak mapping with a delegate
comment|// for (Procedure proc : map.getProcedures()) {
comment|// getAdapter(node).tweakProcedure(proc);
comment|// }
comment|// use shared data source in all cases but the multi-node...
comment|// if (MultiNodeCase.NODE1.equals(node.getName())
comment|// || MultiNodeCase.NODE2.equals(node.getName())) {
comment|// node.setDataSource(resources.createDataSource());
comment|// }
comment|// else {
comment|// node.setDataSource(resources.getDataSource());
comment|// }
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
name|domain
return|;
block|}
block|}
end_class

end_unit

