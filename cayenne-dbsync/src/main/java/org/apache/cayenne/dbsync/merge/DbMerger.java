begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|Objects
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
comment|/**  * Wraps an algorithm to traverse a {@link DataMap} and create a group of {@link MergerToken}s that can be used to  * synchronize data store and Cayenne model.  */
end_comment

begin_class
specifier|public
class|class
name|DbMerger
block|{
specifier|private
name|MergerTokenFactory
name|tokenFactory
decl_stmt|;
specifier|private
name|ValueForNullProvider
name|valueForNull
decl_stmt|;
specifier|private
name|boolean
name|skipRelationshipsTokens
decl_stmt|;
specifier|private
name|boolean
name|skipPKTokens
decl_stmt|;
specifier|private
name|FiltersConfig
name|filters
decl_stmt|;
specifier|private
name|DbMerger
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|MergerTokenFactory
name|tokenFactory
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|(
name|tokenFactory
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DbMerger
name|build
parameter_list|(
name|MergerTokenFactory
name|tokenFactory
parameter_list|)
block|{
return|return
name|builder
argument_list|(
name|tokenFactory
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Return true if the two unordered {@link Collection}s of {@link DbJoin}s      * are equal. Entity and Attribute names are compared case insensitively.      *<p>      * TODO complexity n^2; sort both collection and go through them to compare      * = 2*n*log(n) + n      */
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
comment|/**      * Create MergerTokens that represent the difference between two {@link DataMap} objects.      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|DataMap
name|dbImport
parameter_list|)
block|{
name|dbImport
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
name|dataMap
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
name|filter
argument_list|(
name|dataMap
argument_list|,
name|filters
argument_list|)
argument_list|,
name|dbImport
operator|.
name|getDbEntities
argument_list|()
argument_list|)
decl_stmt|;
comment|// sort
name|Collections
operator|.
name|sort
argument_list|(
name|tokens
argument_list|,
operator|new
name|TokenComparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|filter
parameter_list|(
name|DataMap
name|existing
parameter_list|,
name|FiltersConfig
name|filtersConfig
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|existingFiltered
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|existing
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
name|TableFilter
name|tableFilter
init|=
name|filtersConfig
operator|.
name|tableFilter
argument_list|(
name|entity
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tableFilter
operator|!=
literal|null
operator|&&
name|tableFilter
operator|.
name|isIncludeTable
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|existingFiltered
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|existingFiltered
return|;
block|}
specifier|protected
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
name|entities
parameter_list|,
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|dbImportedEntities
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
argument_list|<>
argument_list|(
name|dbImportedEntities
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
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|dbEntity
range|:
name|entities
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
comment|// look for table
name|DbEntity
name|detectedEntity
init|=
name|findDbEntity
argument_list|(
name|dbImportedEntities
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
name|tokenFactory
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
name|tokenFactory
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
if|if
condition|(
operator|!
name|skipRelationshipsTokens
condition|)
block|{
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
block|}
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
if|if
condition|(
operator|!
name|skipPKTokens
condition|)
block|{
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
block|}
comment|// drop table
comment|// TODO: support drop table. currently, too many tables are marked for
comment|// drop
for|for
control|(
name|DbEntity
name|e
range|:
name|dbEntitiesToDrop
control|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|tokenFactory
operator|.
name|createDropTableToDb
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|e
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|DbEntity
name|detectedEntity
init|=
name|findDbEntity
argument_list|(
name|entities
argument_list|,
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|detectedEntity
operator|!=
literal|null
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|tokenFactory
operator|.
name|createDropRelationshipToDb
argument_list|(
name|detectedEntity
argument_list|,
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
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
name|tokenFactory
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
name|tokenFactory
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
name|tokenFactory
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
name|tokenFactory
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
name|tokenFactory
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
name|tokenFactory
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
name|tokenFactory
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
name|tokenFactory
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
name|setTargetEntityName
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
comment|// Add all relationships. Tokens will decide whether or not to execute
name|MergerToken
name|token
init|=
name|tokenFactory
operator|.
name|createDropRelationshipToDb
argument_list|(
name|dbEntity
argument_list|,
name|detected
argument_list|)
decl_stmt|;
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
comment|// Add all relationships. Tokens will decide whether or not to execute
name|AddRelationshipToDb
name|token
init|=
operator|(
name|AddRelationshipToDb
operator|)
name|tokenFactory
operator|.
name|createAddRelationshipToDb
argument_list|(
name|dbEntity
argument_list|,
name|rel
argument_list|)
decl_stmt|;
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
name|detectedEntity
operator|instanceof
name|DetectedDbEntity
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
name|tokenFactory
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
comment|/**      * case insensitive search for a {@link DbEntity} in a {@link DataMap} by      * name      */
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
comment|/**      * case insensitive search for a {@link DbAttribute} in a {@link DbEntity}      * by name      */
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
comment|/**      * search for a {@link DbRelationship} like rel in the given      * {@link DbEntity}      */
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
specifier|public
specifier|static
class|class
name|Builder
block|{
specifier|private
name|DbMerger
name|merger
decl_stmt|;
specifier|private
name|Builder
parameter_list|(
name|MergerTokenFactory
name|tokenFactory
parameter_list|)
block|{
name|this
operator|.
name|merger
operator|=
operator|new
name|DbMerger
argument_list|()
expr_stmt|;
name|this
operator|.
name|merger
operator|.
name|tokenFactory
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|tokenFactory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbMerger
name|build
parameter_list|()
block|{
if|if
condition|(
name|merger
operator|.
name|valueForNull
operator|==
literal|null
condition|)
block|{
name|merger
operator|.
name|valueForNull
operator|=
operator|new
name|EmptyValueForNullProvider
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|merger
operator|.
name|filters
operator|==
literal|null
condition|)
block|{
comment|// default: match all tables, no stored procedures
name|merger
operator|.
name|filters
operator|=
name|FiltersConfig
operator|.
name|create
argument_list|(
literal|null
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
expr_stmt|;
block|}
return|return
name|merger
return|;
block|}
specifier|public
name|Builder
name|valueForNullProvider
parameter_list|(
name|ValueForNullProvider
name|provider
parameter_list|)
block|{
name|merger
operator|.
name|valueForNull
operator|=
name|provider
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|skipRelationshipsTokens
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|merger
operator|.
name|skipRelationshipsTokens
operator|=
name|flag
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|skipPKTokens
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|merger
operator|.
name|skipPKTokens
operator|=
name|flag
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|filters
parameter_list|(
name|FiltersConfig
name|filters
parameter_list|)
block|{
name|merger
operator|.
name|filters
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|filters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_class

end_unit

