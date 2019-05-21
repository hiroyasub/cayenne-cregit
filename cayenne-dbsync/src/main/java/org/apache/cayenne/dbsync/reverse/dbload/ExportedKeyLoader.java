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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

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

begin_class
class|class
name|ExportedKeyLoader
extends|extends
name|PerEntityLoader
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ExportedKeyLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|ExportedKeyLoader
parameter_list|(
name|DbLoaderConfiguration
name|config
parameter_list|,
name|DbLoaderDelegate
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|config
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|boolean
name|shouldLoad
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|dbRelationship
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Error getting relationships for '"
operator|+
name|entity
operator|.
name|getCatalog
argument_list|()
operator|+
literal|"."
operator|+
name|entity
operator|.
name|getSchema
argument_list|()
operator|+
literal|"', ignoring. "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
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
block|{
return|return
name|metaData
operator|.
name|getExportedKeys
argument_list|(
name|dbEntity
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|dbEntity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
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
block|{
name|ExportedKey
name|key
init|=
operator|new
name|ExportedKey
argument_list|(
name|rs
argument_list|)
decl_stmt|;
name|DbEntity
name|pkEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|key
operator|.
name|getPk
argument_list|()
operator|.
name|getTable
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|key
operator|.
name|getPk
argument_list|()
operator|.
name|validateEntity
argument_list|(
name|pkEntity
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Skip relation: '"
operator|+
name|key
operator|+
literal|"' because table '"
operator|+
name|key
operator|.
name|getPk
argument_list|()
operator|.
name|getTable
argument_list|()
operator|+
literal|"' is not found or in different catalog/schema"
argument_list|)
expr_stmt|;
return|return;
block|}
name|DbEntity
name|fkEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|key
operator|.
name|getFk
argument_list|()
operator|.
name|getTable
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|key
operator|.
name|getFk
argument_list|()
operator|.
name|validateEntity
argument_list|(
name|fkEntity
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Skip relation: '"
operator|+
name|key
operator|+
literal|"' because table '"
operator|+
name|key
operator|.
name|getFk
argument_list|()
operator|.
name|getTable
argument_list|()
operator|+
literal|"' is not found or in different catalog/schema"
argument_list|)
expr_stmt|;
return|return;
block|}
name|map
operator|.
name|addExportedKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

