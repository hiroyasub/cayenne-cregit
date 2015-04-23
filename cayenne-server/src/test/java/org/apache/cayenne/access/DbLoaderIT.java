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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|loader
operator|.
name|DbLoaderConfiguration
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
name|loader
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
name|access
operator|.
name|loader
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
name|access
operator|.
name|loader
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|dba
operator|.
name|TypesMapping
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
name|DbRelationship
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
name|ObjAttribute
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
name|ObjEntity
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
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|tuple
operator|.
name|Pair
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertNull
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
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DbLoaderIT
extends|extends
name|ServerCase
block|{
specifier|public
specifier|static
specifier|final
name|DbLoaderConfiguration
name|CONFIG
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DbAdapter
name|adapter
decl_stmt|;
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
specifier|private
name|DbLoader
name|loader
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|=
operator|new
name|DbLoader
argument_list|(
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|,
name|adapter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|.
name|getConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTableTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|?
argument_list|>
name|tableTypes
init|=
name|loader
operator|.
name|getTableTypes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tableTypes
argument_list|)
expr_stmt|;
name|String
name|tableLabel
init|=
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
if|if
condition|(
name|tableLabel
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Missing type for table '"
operator|+
name|tableLabel
operator|+
literal|"' - "
operator|+
name|tableTypes
argument_list|,
name|tableTypes
operator|.
name|contains
argument_list|(
name|tableLabel
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|viewLabel
init|=
name|adapter
operator|.
name|tableTypeForView
argument_list|()
decl_stmt|;
if|if
condition|(
name|viewLabel
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Missing type for view '"
operator|+
name|viewLabel
operator|+
literal|"' - "
operator|+
name|tableTypes
argument_list|,
name|tableTypes
operator|.
name|contains
argument_list|(
name|viewLabel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTables
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tableLabel
init|=
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
name|tables
init|=
name|loader
operator|.
name|getTables
argument_list|(
operator|new
name|DbLoaderConfiguration
argument_list|()
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tables
argument_list|)
expr_stmt|;
name|boolean
name|foundArtist
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
name|table
range|:
name|tables
control|)
block|{
if|if
condition|(
literal|"ARTIST"
operator|.
name|equalsIgnoreCase
argument_list|(
name|table
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|foundArtist
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"'ARTIST' is missing from the table list: "
operator|+
name|tables
argument_list|,
name|foundArtist
argument_list|)
expr_stmt|;
block|}
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
name|Map
argument_list|<
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
argument_list|>
name|tables
init|=
name|loader
operator|.
name|getTables
argument_list|(
name|config
argument_list|,
operator|new
name|String
index|[]
block|{
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tables
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tables
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
name|Map
argument_list|<
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
argument_list|>
name|tables
init|=
name|loader
operator|.
name|getTables
argument_list|(
name|config
argument_list|,
operator|new
name|String
index|[]
block|{
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tables
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tables
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
name|testLoadWithMeaningfulPK
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
index|[]
name|tableLabel
init|=
block|{
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
block|}
decl_stmt|;
name|loader
operator|.
name|setCreatingMeaningfulPK
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
argument_list|>
name|testLoader
init|=
name|loader
operator|.
name|getTables
argument_list|(
name|CONFIG
argument_list|,
name|tableLabel
argument_list|)
decl_stmt|;
if|if
condition|(
name|testLoader
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|testLoader
operator|=
name|loader
operator|.
name|getTables
argument_list|(
name|CONFIG
argument_list|,
name|tableLabel
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
init|=
name|loader
operator|.
name|loadDbEntities
argument_list|(
name|map
argument_list|,
name|CONFIG
argument_list|,
name|testLoader
argument_list|)
decl_stmt|;
name|loader
operator|.
name|loadObjEntities
argument_list|(
name|map
argument_list|,
name|CONFIG
argument_list|,
name|entities
argument_list|)
expr_stmt|;
name|ObjEntity
name|artist
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|ObjAttribute
name|id
init|=
name|artist
operator|.
name|getAttribute
argument_list|(
literal|"artistId"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * DataMap loading is in one big test method, since breaking it in      * individual tests would require multiple reads of metatdata which is      * extremely slow on some RDBMS (Sybase).      */
annotation|@
name|Test
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|supportsUnique
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsUniqueConstraints
argument_list|()
decl_stmt|;
name|boolean
name|supportsLobs
init|=
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
decl_stmt|;
name|boolean
name|supportsFK
init|=
name|accessStackAdapter
operator|.
name|supportsFKConstraints
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|setDefaultPackage
argument_list|(
literal|"foo.x"
argument_list|)
expr_stmt|;
name|String
name|tableLabel
init|=
name|adapter
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
comment|// *** TESTING THIS ***
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
init|=
name|loader
operator|.
name|loadDbEntities
argument_list|(
name|map
argument_list|,
name|CONFIG
argument_list|,
name|loader
operator|.
name|getTables
argument_list|(
name|CONFIG
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertDbEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportsLobs
condition|)
block|{
name|assertLobDbEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|// *** TESTING THIS ***
name|Map
argument_list|<
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
argument_list|>
name|tables
init|=
operator|new
name|HashMap
argument_list|<
name|Pair
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
name|value
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Pair
argument_list|<
name|DbEntity
argument_list|,
name|PatternFilter
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|e
range|:
name|entities
control|)
block|{
name|value
operator|.
name|put
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
name|Pair
operator|.
name|of
argument_list|(
name|e
argument_list|,
name|PatternFilter
operator|.
name|INCLUDE_EVERYTHING
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|tables
operator|.
name|put
argument_list|(
name|Pair
operator|.
name|of
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadDbRelationships
argument_list|(
name|CONFIG
argument_list|,
name|tables
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportsFK
condition|)
block|{
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|rels
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"ARTIST"
argument_list|)
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|rels
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// test one-to-one
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"PAINTING"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
comment|// find relationship to PAINTING_INFO
name|DbRelationship
name|oneToOne
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DbRelationship
name|rel
range|:
name|rels
control|)
block|{
if|if
condition|(
literal|"PAINTING_INFO"
operator|.
name|equalsIgnoreCase
argument_list|(
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
condition|)
block|{
name|oneToOne
operator|=
name|rel
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"No relationship to PAINTING_INFO"
argument_list|,
name|oneToOne
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Relationship to PAINTING_INFO must be to-one"
argument_list|,
name|oneToOne
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Relationship to PAINTING_INFO must be to-one"
argument_list|,
name|oneToOne
operator|.
name|isToDependentPK
argument_list|()
argument_list|)
expr_stmt|;
comment|// test UNIQUE only if FK is supported...
if|if
condition|(
name|supportsUnique
condition|)
block|{
name|assertUniqueConstraintsInRelationships
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *** TESTING THIS ***
name|loader
operator|.
name|setCreatingMeaningfulPK
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|loader
operator|.
name|loadObjEntities
argument_list|(
name|map
argument_list|,
name|CONFIG
argument_list|,
name|entities
argument_list|)
expr_stmt|;
name|assertObjEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
comment|// now when the map is loaded, test
comment|// various things
comment|// selectively check how different types were processed
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsColumnTypeReengineering
argument_list|()
condition|)
block|{
name|checkTypes
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertUniqueConstraintsInRelationships
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
comment|// unfortunately JDBC metadata doesn't provide info for UNIQUE
comment|// constraints....
comment|// cant reengineer them...
comment|// find rel to TO_ONEFK1
comment|/*          * Iterator it = getDbEntity(map,          * "TO_ONEFK2").getRelationships().iterator(); DbRelationship rel =          * (DbRelationship) it.next(); assertEquals("TO_ONEFK1",          * rel.getTargetEntityName());          * assertFalse("UNIQUE constraint was ignored...", rel.isToMany());          */
block|}
specifier|private
name|void
name|assertDbEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|dae
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null 'ARTIST' entity, other DbEntities: "
operator|+
name|map
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
name|DbAttribute
name|a
init|=
name|getDbAttribute
argument_list|(
name|dae
argument_list|,
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a
operator|.
name|isGenerated
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
name|map
argument_list|,
literal|"GENERATED_COLUMN_TEST"
argument_list|)
decl_stmt|;
name|DbAttribute
name|id
init|=
name|bag
operator|.
name|getAttribute
argument_list|(
literal|"GENERATED_COLUMN"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|id
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|id
operator|.
name|isGenerated
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertObjEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|boolean
name|supportsLobs
init|=
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
decl_stmt|;
name|boolean
name|supportsFK
init|=
name|accessStackAdapter
operator|.
name|supportsFKConstraints
argument_list|()
decl_stmt|;
name|ObjEntity
name|ae
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ae
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
name|ae
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert primary key is not an attribute
name|assertNull
argument_list|(
name|ae
operator|.
name|getAttribute
argument_list|(
literal|"artistId"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportsLobs
condition|)
block|{
name|assertLobObjEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|supportsFK
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|rels1
init|=
name|ae
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rels1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rels1
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"foo.x.Artist"
argument_list|,
name|ae
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertLobDbEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|blobEnt
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"BLOB_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobEnt
argument_list|)
expr_stmt|;
name|DbAttribute
name|blobAttr
init|=
name|getDbAttribute
argument_list|(
name|blobEnt
argument_list|,
literal|"BLOB_COL"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobAttr
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|,
name|blobAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|BLOB
operator|==
name|blobAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|LONGVARBINARY
operator|==
name|blobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|clobEnt
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"CLOB_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobEnt
argument_list|)
expr_stmt|;
name|DbAttribute
name|clobAttr
init|=
name|getDbAttribute
argument_list|(
name|clobEnt
argument_list|,
literal|"CLOB_COL"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobAttr
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|,
name|clobAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|CLOB
operator|==
name|clobAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|LONGVARCHAR
operator|==
name|clobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|/*         DbEntity nclobEnt = getDbEntity(map, "NCLOB_TEST");         assertNotNull(nclobEnt);         DbAttribute nclobAttr = getDbAttribute(nclobEnt, "NCLOB_COL");         assertNotNull(nclobAttr);         assertTrue(msgForTypeMismatch(Types.NCLOB, nclobAttr), Types.NCLOB == nclobAttr.getType()                 || Types.LONGVARCHAR == nclobAttr.getType()); */
block|}
specifier|private
name|void
name|assertLobObjEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|ObjEntity
name|blobEnt
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"BlobTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobEnt
argument_list|)
expr_stmt|;
comment|// BLOBs should be mapped as byte[]
name|ObjAttribute
name|blobAttr
init|=
name|blobEnt
operator|.
name|getAttribute
argument_list|(
literal|"blobCol"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"BlobTest.blobCol failed to doLoad"
argument_list|,
name|blobAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|blobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|clobEnt
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"ClobTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobEnt
argument_list|)
expr_stmt|;
comment|// CLOBs should be mapped as Strings by default
name|ObjAttribute
name|clobAttr
init|=
name|clobEnt
operator|.
name|getAttribute
argument_list|(
literal|"clobCol"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|clobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|nclobEnt
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"NclobTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nclobEnt
argument_list|)
expr_stmt|;
comment|// CLOBs should be mapped as Strings by default
name|ObjAttribute
name|nclobAttr
init|=
name|nclobEnt
operator|.
name|getAttribute
argument_list|(
literal|"nclobCol"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nclobAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|nclobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbEntity
name|getDbEntity
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbEntity
name|de
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// sometimes table names get converted to lowercase
if|if
condition|(
name|de
operator|==
literal|null
condition|)
block|{
name|de
operator|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|de
return|;
block|}
specifier|private
name|DbAttribute
name|getDbAttribute
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbAttribute
name|da
init|=
name|ent
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// sometimes table names get converted to lowercase
if|if
condition|(
name|da
operator|==
literal|null
condition|)
block|{
name|da
operator|=
name|ent
operator|.
name|getAttribute
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|da
return|;
block|}
specifier|private
name|DataMap
name|originalMap
parameter_list|()
block|{
return|return
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**      * Selectively check how different types were processed.      */
specifier|public
name|void
name|checkTypes
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|dbe
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|DbEntity
name|floatTest
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"FLOAT_TEST"
argument_list|)
decl_stmt|;
name|DbEntity
name|smallintTest
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"SMALLINT_TEST"
argument_list|)
decl_stmt|;
name|DbAttribute
name|integerAttr
init|=
name|getDbAttribute
argument_list|(
name|dbe
argument_list|,
literal|"PAINTING_ID"
argument_list|)
decl_stmt|;
name|DbAttribute
name|decimalAttr
init|=
name|getDbAttribute
argument_list|(
name|dbe
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|varcharAttr
init|=
name|getDbAttribute
argument_list|(
name|dbe
argument_list|,
literal|"PAINTING_TITLE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|floatAttr
init|=
name|getDbAttribute
argument_list|(
name|floatTest
argument_list|,
literal|"FLOAT_COL"
argument_list|)
decl_stmt|;
name|DbAttribute
name|smallintAttr
init|=
name|getDbAttribute
argument_list|(
name|smallintTest
argument_list|,
literal|"SMALLINT_COL"
argument_list|)
decl_stmt|;
comment|// check decimal
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|DECIMAL
argument_list|,
name|decimalAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|DECIMAL
operator|==
name|decimalAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|NUMERIC
operator|==
name|decimalAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|decimalAttr
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
comment|// check varchar
name|assertEquals
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|varcharAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|varcharAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|255
argument_list|,
name|varcharAttr
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
comment|// check integer
name|assertEquals
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|integerAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|integerAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// check float
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|FLOAT
argument_list|,
name|floatAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|FLOAT
operator|==
name|floatAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|DOUBLE
operator|==
name|floatAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|REAL
operator|==
name|floatAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// check smallint
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|SMALLINT
argument_list|,
name|smallintAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|SMALLINT
operator|==
name|smallintAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|INTEGER
operator|==
name|smallintAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|checkAllDBEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
for|for
control|(
name|DbEntity
name|origEnt
range|:
name|originalMap
argument_list|()
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
name|DbEntity
name|newEnt
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|origEnt
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbAttribute
name|origAttr
range|:
name|origEnt
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|DbAttribute
name|newAttr
init|=
name|newEnt
operator|.
name|getAttribute
argument_list|(
name|origAttr
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No matching DbAttribute for '"
operator|+
name|origAttr
operator|.
name|getName
argument_list|()
argument_list|,
name|newAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|origAttr
argument_list|,
name|newAttr
argument_list|)
argument_list|,
name|origAttr
operator|.
name|getType
argument_list|()
argument_list|,
name|newAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// length and precision doesn't have to be the same
comment|// it must be greater or equal
name|assertTrue
argument_list|(
name|origAttr
operator|.
name|getMaxLength
argument_list|()
operator|<=
name|newAttr
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|origAttr
operator|.
name|getScale
argument_list|()
operator|<=
name|newAttr
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|String
name|msgForTypeMismatch
parameter_list|(
name|DbAttribute
name|origAttr
parameter_list|,
name|DbAttribute
name|newAttr
parameter_list|)
block|{
return|return
name|msgForTypeMismatch
argument_list|(
name|origAttr
operator|.
name|getType
argument_list|()
argument_list|,
name|newAttr
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|msgForTypeMismatch
parameter_list|(
name|int
name|origType
parameter_list|,
name|DbAttribute
name|newAttr
parameter_list|)
block|{
name|String
name|nt
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|newAttr
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|ot
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|origType
argument_list|)
decl_stmt|;
return|return
name|attrMismatch
argument_list|(
name|newAttr
operator|.
name|getName
argument_list|()
argument_list|,
literal|"expected type:<"
operator|+
name|ot
operator|+
literal|">, but was<"
operator|+
name|nt
operator|+
literal|">"
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|attrMismatch
parameter_list|(
name|String
name|attrName
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
return|return
literal|"[Error loading attribute '"
operator|+
name|attrName
operator|+
literal|"': "
operator|+
name|msg
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

