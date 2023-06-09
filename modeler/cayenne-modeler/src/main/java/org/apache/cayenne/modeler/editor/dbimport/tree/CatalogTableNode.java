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
name|modeler
operator|.
name|editor
operator|.
name|dbimport
operator|.
name|tree
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|FilterContainer
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
import|;
end_import

begin_class
class|class
name|CatalogTableNode
extends|extends
name|TableNode
argument_list|<
name|CatalogNode
argument_list|>
block|{
name|CatalogTableNode
parameter_list|(
name|String
name|name
parameter_list|,
name|CatalogNode
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|List
argument_list|<
name|FilterContainer
argument_list|>
name|getContainers
parameter_list|(
name|ReverseEngineering
name|config
parameter_list|)
block|{
name|List
argument_list|<
name|FilterContainer
argument_list|>
name|containers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|containers
operator|.
name|add
argument_list|(
name|getParent
argument_list|()
operator|.
name|getCatalog
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|containers
operator|.
name|add
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|containers
return|;
block|}
block|}
end_class

end_unit

