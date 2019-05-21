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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|FiltersConfig
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
name|filters
operator|.
name|PatternFilter
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
name|filters
operator|.
name|TableFilter
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
name|junit
operator|.
name|Test
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
name|assertEquals
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
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|EntityLoaderIT
extends|extends
name|BaseLoaderIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetTablesWithWrongCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|DbLoaderConfiguration
name|config
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFiltersConfig
argument_list|(
name|FiltersConfig
operator|.
name|create
argument_list|(
literal|"WRONG"
argument_list|,
literal|null
argument_list|,
name|TableFilter
operator|.
name|everything
argument_list|()
argument_list|,
name|PatternFilter
operator|.
name|INCLUDE_NOTHING
argument_list|)
argument_list|)
expr_stmt|;
name|EntityLoader
name|loader
init|=
operator|new
name|EntityLoader
argument_list|(
name|adapter
argument_list|,
name|config
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|loader
operator|.
name|load
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
comment|// SQL Server will throw exception here.
name|assertTrue
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"WRONG"
argument_list|)
argument_list|)
expr_stmt|;
comment|// just check that message is about "WRONG" catalog
block|}
name|assertTrue
argument_list|(
literal|"Store is not empty"
argument_list|,
name|store
operator|.
name|getDbEntities
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTablesWithWrongSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|DbLoaderConfiguration
name|config
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFiltersConfig
argument_list|(
name|FiltersConfig
operator|.
name|create
argument_list|(
literal|null
argument_list|,
literal|"WRONG"
argument_list|,
name|TableFilter
operator|.
name|everything
argument_list|()
argument_list|,
name|PatternFilter
operator|.
name|INCLUDE_NOTHING
argument_list|)
argument_list|)
expr_stmt|;
name|EntityLoader
name|loader
init|=
operator|new
name|EntityLoader
argument_list|(
name|adapter
argument_list|,
name|config
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Store is not empty"
argument_list|,
name|store
operator|.
name|getDbEntities
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityLoader
name|loader
init|=
operator|new
name|EntityLoader
argument_list|(
name|adapter
argument_list|,
name|EMPTY_CONFIG
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
name|connection
operator|.
name|getMetaData
argument_list|()
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Store not empty"
argument_list|,
name|store
operator|.
name|getDbEntities
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertDbEntities
argument_list|()
expr_stmt|;
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
condition|)
block|{
name|assertLobDbEntities
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertDbEntities
parameter_list|()
block|{
name|DbEntity
name|dae
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null 'ARTIST' entity, other DbEntities: "
operator|+
name|store
operator|.
name|getDbEntityMap
argument_list|()
argument_list|,
name|dae
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST"
argument_list|,
name|dae
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|adapter
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
name|DbEntity
name|bag
init|=
name|getDbEntity
argument_list|(
literal|"GENERATED_COLUMN_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null 'GENERATED_COLUMN_TEST' entity, other DbEntities: "
operator|+
name|store
operator|.
name|getDbEntityMap
argument_list|()
argument_list|,
name|bag
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertLobDbEntities
parameter_list|()
block|{
name|DbEntity
name|blobEnt
init|=
name|getDbEntity
argument_list|(
literal|"BLOB_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobEnt
argument_list|)
expr_stmt|;
name|DbEntity
name|clobEnt
init|=
name|getDbEntity
argument_list|(
literal|"CLOB_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobEnt
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

