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
name|merge
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
name|CayenneRuntimeException
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
name|DbLoader
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
name|DefaultDbLoaderDelegate
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
name|Attribute
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
name|DbJoin
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
name|DetectedDbEntity
import|;
end_import

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
name|SQLException
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Traverse a {@link DataNode} and a {@link DataMap} and create a group of  * {@link MergerToken}s to alter the {@link DataNode} data store to match the  * {@link DataMap}.  *   */
end_comment

begin_class
specifier|public
class|class
name|DbMerger
block|{
specifier|private
specifier|final
name|MergerFactory
name|factory
decl_stmt|;
specifier|private
specifier|final
name|ValueForNullProvider
name|valueForNull
decl_stmt|;
specifier|private
specifier|final
name|String
name|schema
decl_stmt|;
specifier|public
name|DbMerger
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
name|this
argument_list|(
name|factory
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbMerger
parameter_list|(
name|MergerFactory
name|factory
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|this
argument_list|(
name|factory
argument_list|,
literal|null
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbMerger
parameter_list|(
name|MergerFactory
name|factory
parameter_list|,
name|ValueForNullProvider
name|valueForNull
parameter_list|,
name|String
name|schema
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|valueForNull
operator|=
name|valueForNull
operator|==
literal|null
condition|?
operator|new
name|EmptyValueForNullProvider
argument_list|()
else|:
name|valueForNull
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
comment|/**      * A method that return true if the given table name should be included. The default      * implementation include all tables.      */
specifier|public
name|boolean
name|includeTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Create and return a {@link List} of {@link MergerToken}s to alter the given      * {@link DataNode} to match the given {@link DataMap}      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|DataNode
name|dataNode
parameter_list|,
name|DataMap
name|existing
parameter_list|)
block|{
return|return
name|createMergeTokens
argument_list|(
name|dataNode
operator|.
name|getDataSource
argument_list|()
argument_list|,
name|dataNode
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|existing
argument_list|)
return|;
block|}
comment|/**      * Create and return a {@link List} of {@link MergerToken}s to alter the given      * {@link DataNode} to match the given {@link DataMap}      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|DbLoader
name|dbLoader
parameter_list|,
name|DataMap
name|existing
parameter_list|)
block|{
return|return
name|createMergeTokens
argument_list|(
name|existing
argument_list|,
name|loadDataMapFromDb
argument_list|(
name|dbLoader
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Create and return a {@link List} of {@link MergerToken}s to alter the given      * {@link DataNode} to match the given {@link DataMap}      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|DataSource
name|dataSource
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|DataMap
name|existingDataMap
parameter_list|)
block|{
return|return
name|createMergeTokens
argument_list|(
name|existingDataMap
argument_list|,
name|loadDataMapFromDb
argument_list|(
name|dataSource
argument_list|,
name|adapter
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Create and return a {@link List} of {@link MergerToken}s to alter the given      * {@link DataNode} to match the given {@link DataMap}      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|DataMap
name|existing
parameter_list|,
name|DataMap
name|loadedFomDb
parameter_list|)
block|{
name|loadedFomDb
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
name|existing
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|(
name|existing
operator|.
name|getDbEntities
argument_list|()
argument_list|,
name|loadedFomDb
operator|.
name|getDbEntities
argument_list|()
argument_list|)
decl_stmt|;
comment|// sort. use a custom Comparator since only toDb tokens are comparable by now
name|Collections
operator|.
name|sort
argument_list|(
name|tokens
argument_list|,
operator|new
name|Comparator
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|MergerToken
name|o1
parameter_list|,
name|MergerToken
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|instanceof
name|AbstractToDbToken
operator|&&
name|o2
operator|instanceof
name|AbstractToDbToken
condition|)
block|{
name|AbstractToDbToken
name|d1
init|=
operator|(
name|AbstractToDbToken
operator|)
name|o1
decl_stmt|;
name|AbstractToDbToken
name|d2
init|=
operator|(
name|AbstractToDbToken
operator|)
name|o2
decl_stmt|;
return|return
name|d1
operator|.
name|compareTo
argument_list|(
name|d2
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
specifier|private
name|DataMap
name|loadDataMapFromDb
parameter_list|(
name|DataSource
name|dataSource
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|Connection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|conn
operator|=
name|dataSource
operator|.
name|getConnection
argument_list|()
expr_stmt|;
specifier|final
name|DbMerger
name|merger
init|=
name|this
decl_stmt|;
comment|// TODO pass naming strategy
name|DbLoader
name|dbLoader
init|=
operator|new
name|DbLoader
argument_list|(
name|conn
argument_list|,
name|adapter
argument_list|,
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|includeTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
return|return
name|merger
operator|.
name|includeTableName
argument_list|(
name|tableName
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|loadDataMapFromDb
argument_list|(
name|dbLoader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't load dataMap from db."
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// Do nothing.
block|}
block|}
block|}
block|}
specifier|private
name|DataMap
name|loadDataMapFromDb
parameter_list|(
name|DbLoader
name|dbLoader
parameter_list|)
block|{
name|DataMap
name|detectedDataMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
try|try
block|{
name|dbLoader
operator|.
name|load
argument_list|(
name|detectedDataMap
argument_list|,
literal|null
argument_list|,
name|schema
argument_list|,
literal|null
argument_list|,
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// TODO log
block|}
return|return
name|detectedDataMap
return|;
block|}
comment|/**      *      *      * @param existing      * @param loadedFromDb      * @return      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|existing
parameter_list|,
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|loadedFromDb
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|dbEntitiesToDrop
init|=
operator|new
name|LinkedList
argument_list|<
name|DbEntity
argument_list|>
argument_list|(
name|loadedFromDb
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|dbEntity
range|:
name|existing
control|)
block|{
name|String
name|tableName
init|=
name|dbEntity
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|includeTableName
argument_list|(
name|tableName
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// look for table
name|DbEntity
name|detectedEntity
init|=
name|findDbEntity
argument_list|(
name|loadedFromDb
argument_list|,
name|tableName
argument_list|)
decl_stmt|;
if|if
condition|(
name|detectedEntity
operator|==
literal|null
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createCreateTableToDb
argument_list|(
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: does this work properly with createReverse?
for|for
control|(
name|DbRelationship
name|rel
range|:
name|dbEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createAddRelationshipToDb
argument_list|(
name|dbEntity
argument_list|,
name|rel
argument_list|)
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
name|dbEntitiesToDrop
operator|.
name|remove
argument_list|(
name|detectedEntity
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|addAll
argument_list|(
name|checkRelationshipsToDrop
argument_list|(
name|dbEntity
argument_list|,
name|detectedEntity
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|addAll
argument_list|(
name|checkRelationshipsToAdd
argument_list|(
name|dbEntity
argument_list|,
name|detectedEntity
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|addAll
argument_list|(
name|checkRows
argument_list|(
name|dbEntity
argument_list|,
name|detectedEntity
argument_list|)
argument_list|)
expr_stmt|;
name|MergerToken
name|token
init|=
name|checkPrimaryKeyChange
argument_list|(
name|dbEntity
argument_list|,
name|detectedEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
comment|// drop table
comment|// TODO: support drop table. currently, too many tables are marked for drop
for|for
control|(
name|DbEntity
name|e
range|:
name|dbEntitiesToDrop
control|)
block|{
if|if
condition|(
operator|!
name|includeTableName
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createDropTableToDb
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|checkRows
parameter_list|(
name|DbEntity
name|existing
parameter_list|,
name|DbEntity
name|loadedFromDb
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
decl_stmt|;
comment|// columns to drop
for|for
control|(
name|DbAttribute
name|detected
range|:
name|loadedFromDb
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|findDbAttribute
argument_list|(
name|existing
argument_list|,
name|detected
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createDropColumnToDb
argument_list|(
name|existing
argument_list|,
name|detected
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// columns to add or modify
for|for
control|(
name|DbAttribute
name|attr
range|:
name|existing
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|String
name|columnName
init|=
name|attr
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
name|DbAttribute
name|detected
init|=
name|findDbAttribute
argument_list|(
name|loadedFromDb
argument_list|,
name|columnName
argument_list|)
decl_stmt|;
if|if
condition|(
name|detected
operator|==
literal|null
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createAddColumnToDb
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|attr
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
if|if
condition|(
name|valueForNull
operator|.
name|hasValueFor
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createSetValueForNullToDb
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|,
name|valueForNull
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createSetNotNullToDb
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
comment|// check for not null
if|if
condition|(
name|attr
operator|.
name|isMandatory
argument_list|()
operator|!=
name|detected
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
if|if
condition|(
name|attr
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
if|if
condition|(
name|valueForNull
operator|.
name|hasValueFor
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createSetValueForNullToDb
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|,
name|valueForNull
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createSetNotNullToDb
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createSetAllowNullToDb
argument_list|(
name|existing
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: check more types than char/varchar
comment|// TODO: psql report VARCHAR for text column, not clob
switch|switch
condition|(
name|detected
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|Types
operator|.
name|VARCHAR
case|:
case|case
name|Types
operator|.
name|CHAR
case|:
if|if
condition|(
name|attr
operator|.
name|getMaxLength
argument_list|()
operator|!=
name|detected
operator|.
name|getMaxLength
argument_list|()
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|factory
operator|.
name|createSetColumnTypeToDb
argument_list|(
name|existing
argument_list|,
name|detected
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
return|return
name|tokens
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|checkRelationshipsToDrop
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DbEntity
name|detectedEntity
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
decl_stmt|;
comment|// relationships to drop
for|for
control|(
name|DbRelationship
name|detected
range|:
name|detectedEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|findDbRelationship
argument_list|(
name|dbEntity
argument_list|,
name|detected
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// alter detected relationship to match entity and attribute names.
comment|// (case sensitively)
name|DbEntity
name|targetEntity
init|=
name|findDbEntity
argument_list|(
name|dbEntity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getDbEntities
argument_list|()
argument_list|,
name|detected
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetEntity
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|detected
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|detected
operator|.
name|setTargetEntity
argument_list|(
name|targetEntity
argument_list|)
expr_stmt|;
comment|// manipulate the joins to match the DbAttributes in the model
for|for
control|(
name|DbJoin
name|join
range|:
name|detected
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|DbAttribute
name|sattr
init|=
name|findDbAttribute
argument_list|(
name|dbEntity
argument_list|,
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sattr
operator|!=
literal|null
condition|)
block|{
name|join
operator|.
name|setSourceName
argument_list|(
name|sattr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DbAttribute
name|tattr
init|=
name|findDbAttribute
argument_list|(
name|targetEntity
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tattr
operator|!=
literal|null
condition|)
block|{
name|join
operator|.
name|setTargetName
argument_list|(
name|tattr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|MergerToken
name|token
init|=
name|factory
operator|.
name|createDropRelationshipToDb
argument_list|(
name|dbEntity
argument_list|,
name|detected
argument_list|)
decl_stmt|;
if|if
condition|(
name|detected
operator|.
name|isToMany
argument_list|()
condition|)
block|{
comment|// default toModel as we can not do drop a toMany in the db. only
comment|// toOne are represented using foreign key
name|token
operator|=
name|token
operator|.
name|createReverse
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tokens
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|checkRelationshipsToAdd
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DbEntity
name|detectedEntity
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbRelationship
name|rel
range|:
name|dbEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|includeTableName
argument_list|(
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|findDbRelationship
argument_list|(
name|detectedEntity
argument_list|,
name|rel
argument_list|)
operator|==
literal|null
condition|)
block|{
name|AddRelationshipToDb
name|token
init|=
operator|(
name|AddRelationshipToDb
operator|)
name|factory
operator|.
name|createAddRelationshipToDb
argument_list|(
name|dbEntity
argument_list|,
name|rel
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|shouldGenerateFkConstraint
argument_list|()
condition|)
block|{
comment|// TODO I guess we should add relationship always; in order to have ability
comment|// TODO generate reverse relationship. If it doesn't have anything to execute it will be passed
comment|// TODO through execution without any affect on db
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|tokens
return|;
block|}
specifier|private
name|MergerToken
name|checkPrimaryKeyChange
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|DbEntity
name|detectedEntity
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyOriginal
init|=
name|detectedEntity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyNew
init|=
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|String
name|primaryKeyName
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|detectedEntity
operator|instanceof
name|DetectedDbEntity
operator|)
condition|)
block|{
name|primaryKeyName
operator|=
operator|(
operator|(
name|DetectedDbEntity
operator|)
name|detectedEntity
operator|)
operator|.
name|getPrimaryKeyName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|upperCaseEntityNames
argument_list|(
name|primaryKeyOriginal
argument_list|)
operator|.
name|equals
argument_list|(
name|upperCaseEntityNames
argument_list|(
name|primaryKeyNew
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|factory
operator|.
name|createSetPrimaryKeyToDb
argument_list|(
name|dbEntity
argument_list|,
name|primaryKeyOriginal
argument_list|,
name|primaryKeyNew
argument_list|,
name|primaryKeyName
argument_list|)
return|;
block|}
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|upperCaseEntityNames
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Attribute
argument_list|>
name|attrs
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Attribute
name|attr
range|:
name|attrs
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
comment|/**      * case insensitive search for a {@link DbEntity} in a {@link DataMap} by name      */
specifier|private
name|DbEntity
name|findDbEntity
parameter_list|(
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|,
name|String
name|caseInsensitiveName
parameter_list|)
block|{
comment|// TODO: create a Map with upper case keys?
for|for
control|(
name|DbEntity
name|e
range|:
name|dbEntities
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|caseInsensitiveName
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * case insensitive search for a {@link DbAttribute} in a {@link DbEntity} by name      */
specifier|private
name|DbAttribute
name|findDbAttribute
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|String
name|caseInsensitiveName
parameter_list|)
block|{
for|for
control|(
name|DbAttribute
name|a
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|caseInsensitiveName
argument_list|)
condition|)
block|{
return|return
name|a
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * search for a {@link DbRelationship} like rel in the given {@link DbEntity}      */
specifier|private
name|DbRelationship
name|findDbRelationship
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
for|for
control|(
name|DbRelationship
name|candidate
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|equalDbJoinCollections
argument_list|(
name|candidate
operator|.
name|getJoins
argument_list|()
argument_list|,
name|rel
operator|.
name|getJoins
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|candidate
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Return true if the two unordered {@link Collection}s of {@link DbJoin}s are      * equal. Entity and Attribute names are compared case insensitively.      *      * TODO complexity n^2; sort both collection and go through them to compare = 2*n*log(n) + n      */
specifier|private
specifier|static
name|boolean
name|equalDbJoinCollections
parameter_list|(
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|j1s
parameter_list|,
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|j2s
parameter_list|)
block|{
if|if
condition|(
name|j1s
operator|.
name|size
argument_list|()
operator|!=
name|j2s
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|DbJoin
name|j1
range|:
name|j1s
control|)
block|{
if|if
condition|(
operator|!
name|havePair
argument_list|(
name|j2s
argument_list|,
name|j1
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
name|boolean
name|havePair
parameter_list|(
name|Collection
argument_list|<
name|DbJoin
argument_list|>
name|j2s
parameter_list|,
name|DbJoin
name|j1
parameter_list|)
block|{
for|for
control|(
name|DbJoin
name|j2
range|:
name|j2s
control|)
block|{
if|if
condition|(
operator|!
name|isNull
argument_list|(
name|j1
operator|.
name|getSource
argument_list|()
argument_list|)
operator|&&
operator|!
name|isNull
argument_list|(
name|j1
operator|.
name|getTarget
argument_list|()
argument_list|)
operator|&&
operator|!
name|isNull
argument_list|(
name|j2
operator|.
name|getSource
argument_list|()
argument_list|)
operator|&&
operator|!
name|isNull
argument_list|(
name|j2
operator|.
name|getTarget
argument_list|()
argument_list|)
operator|&&
name|j1
operator|.
name|getSource
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|j2
operator|.
name|getSource
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|j1
operator|.
name|getTarget
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|j2
operator|.
name|getTarget
argument_list|()
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|j1
operator|.
name|getSourceName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|j2
operator|.
name|getSourceName
argument_list|()
argument_list|)
operator|&&
name|j1
operator|.
name|getTargetName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|j2
operator|.
name|getTargetName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isNull
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
return|return
name|attribute
operator|==
literal|null
operator|||
name|attribute
operator|.
name|getEntity
argument_list|()
operator|==
literal|null
return|;
block|}
block|}
end_class

end_unit

