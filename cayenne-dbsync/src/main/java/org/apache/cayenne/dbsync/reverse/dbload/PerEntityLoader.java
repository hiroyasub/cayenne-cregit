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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|PerEntityLoader
extends|extends
name|AbstractLoader
block|{
name|PerEntityLoader
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|DbLoaderConfiguration
name|config
parameter_list|,
name|DbLoaderDelegate
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|,
name|config
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|load
parameter_list|(
name|DatabaseMetaData
name|metaData
parameter_list|,
name|DbLoadDataStore
name|map
parameter_list|)
throws|throws
name|SQLException
block|{
for|for
control|(
name|DbEntity
name|dbEntity
range|:
name|map
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|shouldLoad
argument_list|(
name|dbEntity
argument_list|)
condition|)
block|{
continue|continue;
block|}
try|try
init|(
name|ResultSet
name|rs
init|=
name|getResultSet
argument_list|(
name|dbEntity
argument_list|,
name|metaData
argument_list|)
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|processResultSet
argument_list|(
name|dbEntity
argument_list|,
name|map
argument_list|,
name|rs
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|catchException
argument_list|(
name|dbEntity
argument_list|,
name|ex
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
block|}
name|boolean
name|shouldLoad
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|catchException
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|SQLException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|abstract
name|ResultSet
name|getResultSet
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DatabaseMetaData
name|metaData
parameter_list|)
throws|throws
name|SQLException
function_decl|;
specifier|abstract
name|void
name|processResultSet
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DbLoadDataStore
name|map
parameter_list|,
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
function_decl|;
block|}
end_class

end_unit

