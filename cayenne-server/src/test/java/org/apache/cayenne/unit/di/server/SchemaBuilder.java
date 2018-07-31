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
name|DbGenerator
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
name|access
operator|.
name|jdbc
operator|.
name|reader
operator|.
name|DefaultRowReaderFactory
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
name|translator
operator|.
name|batch
operator|.
name|DefaultBatchTranslatorFactory
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
name|translator
operator|.
name|select
operator|.
name|DefaultSelectTranslatorFactory
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
name|ashwood
operator|.
name|AshwoodEntitySorter
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
name|cache
operator|.
name|MapQueryCache
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
name|DataMapLoader
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
name|event
operator|.
name|DefaultEventManager
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
name|log
operator|.
name|JdbcEventLogger
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
name|Procedure
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
name|resource
operator|.
name|URLResource
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
name|testdo
operator|.
name|extended_type
operator|.
name|StringET1ExtendedType
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
name|net
operator|.
name|URL
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
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * Default implementation of the AccessStack that has a single DataNode per DataMap.  */
end_comment

begin_class
specifier|public
class|class
name|SchemaBuilder
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SchemaBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SKIP_SCHEMA_KEY
init|=
literal|"cayenneTestSkipSchemaCreation"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|MAPS_REQUIRING_SCHEMA_SETUP
init|=
block|{
literal|"testmap.map.xml"
block|,
literal|"compound.map.xml"
block|,
literal|"misc-types.map.xml"
block|,
literal|"things.map.xml"
block|,
literal|"numeric-types.map.xml"
block|,
literal|"binary-pk.map.xml"
block|,
literal|"no-pk.map.xml"
block|,
literal|"lob.map.xml"
block|,
literal|"date-time.map.xml"
block|,
literal|"enum.map.xml"
block|,
literal|"extended-type.map.xml"
block|,
literal|"generated.map.xml"
block|,
literal|"mixed-persistence-strategy.map.xml"
block|,
literal|"people.map.xml"
block|,
literal|"primitive.map.xml"
block|,
literal|"inheritance.map.xml"
block|,
literal|"locking.map.xml"
block|,
literal|"soft-delete.map.xml"
block|,
literal|"empty.map.xml"
block|,
literal|"relationships.map.xml"
block|,
literal|"relationships-activity.map.xml"
block|,
literal|"relationships-delete-rules.map.xml"
block|,
literal|"relationships-collection-to-many.map.xml"
block|,
literal|"relationships-child-master.map.xml"
block|,
literal|"relationships-clob.map.xml"
block|,
literal|"relationships-flattened.map.xml"
block|,
literal|"relationships-set-to-many.map.xml"
block|,
literal|"relationships-to-many-fk.map.xml"
block|,
literal|"relationships-to-one-fk.map.xml"
block|,
literal|"return-types.map.xml"
block|,
literal|"uuid.map.xml"
block|,
literal|"multi-tier.map.xml"
block|,
literal|"reflexive.map.xml"
block|,
literal|"delete-rules.map.xml"
block|,
literal|"lifecycle-callbacks-order.map.xml"
block|,
literal|"lifecycles.map.xml"
block|,
literal|"map-to-many.map.xml"
block|,
literal|"toone.map.xml"
block|,
literal|"meaningful-pk.map.xml"
block|,
literal|"table-primitives.map.xml"
block|,
literal|"generic.map.xml"
block|,
literal|"map-db1.map.xml"
block|,
literal|"map-db2.map.xml"
block|,
literal|"embeddable.map.xml"
block|,
literal|"qualified.map.xml"
block|,
literal|"quoted-identifiers.map.xml"
block|,
literal|"inheritance-single-table1.map.xml"
block|,
literal|"inheritance-vertical.map.xml"
block|,
literal|"oneway-rels.map.xml"
block|,
literal|"unsupported-distinct-types.map.xml"
block|,
literal|"array-type.map.xml"
block|,
literal|"cay-2032.map.xml"
block|,
literal|"weighted-sort.map.xml"
block|,
literal|"hybrid-data-object.map.xml"
block|,
literal|"java8.map.xml"
block|,
literal|"inheritance-with-enum.map.xml"
block|}
decl_stmt|;
comment|// hardcoded dependent entities that should be excluded
comment|// if LOBs are not supported
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EXTRA_EXCLUDED_FOR_NO_LOB
init|=
operator|new
name|String
index|[]
block|{
literal|"CLOB_DETAIL"
block|}
decl_stmt|;
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
specifier|private
name|UnitDbAdapter
name|unitDbAdapter
decl_stmt|;
specifier|private
name|DbAdapter
name|dbAdapter
decl_stmt|;
specifier|private
name|DataDomain
name|domain
decl_stmt|;
specifier|private
name|JdbcEventLogger
name|jdbcEventLogger
decl_stmt|;
annotation|@
name|Inject
name|DataMapLoader
name|loader
decl_stmt|;
specifier|public
name|SchemaBuilder
parameter_list|(
annotation|@
name|Inject
name|ServerCaseDataSourceFactory
name|dataSourceFactory
parameter_list|,
annotation|@
name|Inject
name|UnitDbAdapter
name|unitDbAdapter
parameter_list|,
annotation|@
name|Inject
name|DbAdapter
name|dbAdapter
parameter_list|,
annotation|@
name|Inject
name|JdbcEventLogger
name|jdbcEventLogger
parameter_list|)
block|{
name|this
operator|.
name|dataSourceFactory
operator|=
name|dataSourceFactory
expr_stmt|;
name|this
operator|.
name|unitDbAdapter
operator|=
name|unitDbAdapter
expr_stmt|;
name|this
operator|.
name|dbAdapter
operator|=
name|dbAdapter
expr_stmt|;
name|this
operator|.
name|jdbcEventLogger
operator|=
name|jdbcEventLogger
expr_stmt|;
block|}
comment|/** 	 * Completely rebuilds test schema. 	 */
comment|// TODO - this method changes the internal state of the object ... refactor
specifier|public
name|void
name|rebuildSchema
parameter_list|()
block|{
comment|// generate schema combining all DataMaps that require schema support.
comment|// Schema generation is done like that instead of per DataMap on demand
comment|// to avoid conflicts when dropping and generating PK objects.
name|DataMap
index|[]
name|maps
init|=
operator|new
name|DataMap
index|[
name|MAPS_REQUIRING_SCHEMA_SETUP
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|maps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|URL
name|mapURL
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|MAPS_REQUIRING_SCHEMA_SETUP
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|maps
index|[
name|i
index|]
operator|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URLResource
argument_list|(
name|mapURL
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|domain
operator|=
operator|new
name|DataDomain
argument_list|(
literal|"temp"
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setEventManager
argument_list|(
operator|new
name|DefaultEventManager
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setEntitySorter
argument_list|(
operator|new
name|AshwoodEntitySorter
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|setQueryCache
argument_list|(
operator|new
name|MapQueryCache
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
for|for
control|(
name|DataMap
name|map
range|:
name|maps
control|)
block|{
name|initNode
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|SKIP_SCHEMA_KEY
argument_list|)
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"skipping schema generation... "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dropSchema
argument_list|()
expr_stmt|;
name|dropPKSupport
argument_list|()
expr_stmt|;
name|createSchema
argument_list|()
expr_stmt|;
name|createPKSupport
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error rebuilding schema"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|initNode
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|setJdbcEventLogger
argument_list|(
name|jdbcEventLogger
argument_list|)
expr_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|dbAdapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|setDataSource
argument_list|(
name|dataSourceFactory
operator|.
name|getSharedDataSource
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup test extended types
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|registerType
argument_list|(
operator|new
name|StringET1ExtendedType
argument_list|()
argument_list|)
expr_stmt|;
comment|// tweak mapping with a delegate
for|for
control|(
name|Procedure
name|proc
range|:
name|map
operator|.
name|getProcedures
argument_list|()
control|)
block|{
name|unitDbAdapter
operator|.
name|tweakProcedure
argument_list|(
name|proc
argument_list|)
expr_stmt|;
block|}
name|filterDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|node
operator|.
name|addDataMap
argument_list|(
name|map
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
name|node
operator|.
name|setRowReaderFactory
argument_list|(
operator|new
name|DefaultRowReaderFactory
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setBatchTranslatorFactory
argument_list|(
operator|new
name|DefaultBatchTranslatorFactory
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|setSelectTranslatorFactory
argument_list|(
operator|new
name|DefaultSelectTranslatorFactory
argument_list|()
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Remote binary pk {@link DbEntity} for {@link DbAdapter} not supporting 	 * that and so on. 	 */
specifier|protected
name|void
name|filterDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|boolean
name|supportsBinaryPK
init|=
name|unitDbAdapter
operator|.
name|supportsBinaryPK
argument_list|()
decl_stmt|;
if|if
condition|(
name|supportsBinaryPK
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entitiesToRemove
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|ent
range|:
name|map
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
for|for
control|(
name|DbAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
comment|// check for BIN PK or FK to BIN Pk
if|if
condition|(
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BINARY
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|VARBINARY
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|LONGVARBINARY
condition|)
block|{
if|if
condition|(
name|attr
operator|.
name|isPrimaryKey
argument_list|()
operator|||
name|attr
operator|.
name|isForeignKey
argument_list|()
condition|)
block|{
name|entitiesToRemove
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
for|for
control|(
name|DbEntity
name|e
range|:
name|entitiesToRemove
control|)
block|{
name|map
operator|.
name|removeDbEntity
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Drops all test tables. */
specifier|private
name|void
name|dropSchema
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|dropSchema
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Creates all test tables in the database. 	 */
specifier|private
name|void
name|createSchema
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|createSchema
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dropPKSupport
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|dropPKSupport
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Creates primary key support for all node DbEntities. Will use its 	 * facilities provided by DbAdapter to generate any necessary database 	 * objects and data for primary key support. 	 */
specifier|public
name|void
name|createPKSupport
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|createPKSupport
argument_list|(
name|node
argument_list|,
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Helper method that orders DbEntities to satisfy referential constraints 	 * and returns an ordered list. 	 */
specifier|private
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntitiesInInsertOrder
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|TreeMap
argument_list|<
name|String
argument_list|,
name|DbEntity
argument_list|>
name|dbEntityMap
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|map
operator|.
name|getDbEntityMap
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dbEntityMap
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|dbEntitiesFilter
argument_list|(
name|entities
argument_list|)
expr_stmt|;
name|domain
operator|.
name|getEntitySorter
argument_list|()
operator|.
name|sortDbEntities
argument_list|(
name|entities
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|entities
return|;
block|}
specifier|protected
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntitiesInDeleteOrder
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|domain
operator|.
name|getDataMap
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DbEntity
argument_list|>
name|dbEntityMap
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|map
operator|.
name|getDbEntityMap
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dbEntityMap
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|dbEntitiesFilter
argument_list|(
name|entities
argument_list|)
expr_stmt|;
name|domain
operator|.
name|getEntitySorter
argument_list|()
operator|.
name|sortDbEntities
argument_list|(
name|entities
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|entities
return|;
block|}
comment|// This seems actually unused for some time now (from 2014 to 2018), and caused no trouble
specifier|private
name|void
name|dbEntitiesFilter
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|entities
parameter_list|)
block|{
comment|// filter various unsupported tests...
comment|// LOBs
name|boolean
name|excludeLOB
init|=
operator|!
name|unitDbAdapter
operator|.
name|supportsLobs
argument_list|()
decl_stmt|;
name|boolean
name|excludeBinPK
init|=
operator|!
name|unitDbAdapter
operator|.
name|supportsBinaryPK
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludeLOB
operator|||
name|excludeBinPK
condition|)
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|filtered
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|ent
range|:
name|entities
control|)
block|{
comment|// check for LOB attributes
if|if
condition|(
name|excludeLOB
condition|)
block|{
if|if
condition|(
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|EXTRA_EXCLUDED_FOR_NO_LOB
argument_list|,
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
operator|>=
literal|0
condition|)
block|{
continue|continue;
block|}
name|boolean
name|hasLob
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BLOB
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|hasLob
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|hasLob
condition|)
block|{
continue|continue;
block|}
block|}
comment|// check for BIN PK
if|if
condition|(
name|excludeBinPK
condition|)
block|{
name|boolean
name|skip
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|attr
range|:
name|ent
operator|.
name|getAttributes
argument_list|()
control|)
block|{
comment|// check for BIN PK or FK to BIN Pk
if|if
condition|(
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BINARY
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|VARBINARY
operator|||
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|LONGVARBINARY
condition|)
block|{
if|if
condition|(
name|attr
operator|.
name|isPrimaryKey
argument_list|()
operator|||
name|attr
operator|.
name|isForeignKey
argument_list|()
condition|)
block|{
name|skip
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|skip
condition|)
block|{
continue|continue;
block|}
block|}
name|filtered
operator|.
name|add
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
name|entities
operator|=
name|filtered
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dropSchema
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|list
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|map
argument_list|)
decl_stmt|;
try|try
init|(
name|Connection
name|conn
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
name|DatabaseMetaData
name|md
init|=
name|conn
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|allTables
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|ResultSet
name|tables
init|=
name|md
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"%"
argument_list|,
literal|null
argument_list|)
init|)
block|{
while|while
condition|(
name|tables
operator|.
name|next
argument_list|()
condition|)
block|{
comment|// 'toUpperCase' is needed since most databases are case insensitive,
comment|// and some will convert names to lower case (e.g. PostgreSQL)
name|String
name|name
init|=
name|tables
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|allTables
operator|.
name|add
argument_list|(
name|name
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|unitDbAdapter
operator|.
name|willDropTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|,
name|allTables
argument_list|)
expr_stmt|;
comment|// drop all tables in the map
try|try
init|(
name|Statement
name|stmt
init|=
name|conn
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|ListIterator
argument_list|<
name|DbEntity
argument_list|>
name|it
init|=
name|list
operator|.
name|listIterator
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|DbEntity
name|ent
init|=
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|allTables
operator|.
name|contains
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|String
name|dropSql
range|:
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|dropTableStatements
argument_list|(
name|ent
argument_list|)
control|)
block|{
try|try
block|{
name|logger
operator|.
name|info
argument_list|(
name|dropSql
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
name|dropSql
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqe
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Can't drop table "
operator|+
name|ent
operator|.
name|getName
argument_list|()
operator|+
literal|", ignoring..."
argument_list|,
name|sqe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|unitDbAdapter
operator|.
name|droppedTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dropPKSupport
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|filteredEntities
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|dropAutoPk
argument_list|(
name|node
argument_list|,
name|filteredEntities
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createPKSupport
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|DbEntity
argument_list|>
name|filteredEntities
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|createAutoPk
argument_list|(
name|node
argument_list|,
name|filteredEntities
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createSchema
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|Connection
name|conn
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
name|unitDbAdapter
operator|.
name|willCreateTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
try|try
init|(
name|Statement
name|stmt
init|=
name|conn
operator|.
name|createStatement
argument_list|()
init|)
block|{
for|for
control|(
name|String
name|query
range|:
name|tableCreateQueries
argument_list|(
name|node
argument_list|,
name|map
argument_list|)
control|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
block|}
name|unitDbAdapter
operator|.
name|createdTables
argument_list|(
name|conn
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Returns iterator of preprocessed table create queries. 	 */
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|tableCreateQueries
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|DbAdapter
name|adapter
init|=
name|node
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|DbGenerator
name|gen
init|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|map
argument_list|,
literal|null
argument_list|,
name|domain
argument_list|,
name|jdbcEventLogger
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|orderedEnts
init|=
name|dbEntitiesInInsertOrder
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|queries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// table definitions
for|for
control|(
name|DbEntity
name|ent
range|:
name|orderedEnts
control|)
block|{
name|queries
operator|.
name|add
argument_list|(
name|adapter
operator|.
name|createTable
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// FK constraints
for|for
control|(
name|DbEntity
name|ent
range|:
name|orderedEnts
control|)
block|{
if|if
condition|(
operator|!
name|unitDbAdapter
operator|.
name|supportsFKConstraints
argument_list|(
name|ent
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|qs
init|=
name|gen
operator|.
name|createConstraintsQueries
argument_list|(
name|ent
argument_list|)
decl_stmt|;
name|queries
operator|.
name|addAll
argument_list|(
name|qs
argument_list|)
expr_stmt|;
block|}
return|return
name|queries
return|;
block|}
block|}
end_class

end_unit

