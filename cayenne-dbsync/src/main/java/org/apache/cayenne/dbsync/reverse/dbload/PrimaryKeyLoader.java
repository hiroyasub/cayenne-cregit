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
name|map
operator|.
name|DbAttribute
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
name|DetectedDbEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
class|class
name|PrimaryKeyLoader
extends|extends
name|PerEntityLoader
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DbLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|PrimaryKeyLoader
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
name|getPrimaryKeys
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
name|String
name|columnName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"COLUMN_NAME"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|dbEntity
operator|.
name|getAttribute
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
condition|)
block|{
comment|// why an attribute might be null is not quiet clear
comment|// but there is a bug report 731406 indicating that it is
comment|// possible so just print the warning, and ignore
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't locate attribute for primary key: "
operator|+
name|columnName
argument_list|)
expr_stmt|;
return|return;
block|}
name|attribute
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DetectedDbEntity
operator|)
name|dbEntity
operator|)
operator|.
name|setPrimaryKeyName
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|"PK_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

