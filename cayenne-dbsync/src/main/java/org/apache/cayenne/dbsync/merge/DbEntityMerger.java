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
name|merge
package|;
end_package

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
name|Set
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
name|merge
operator|.
name|token
operator|.
name|MergerToken
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
name|dbsync
operator|.
name|model
operator|.
name|DetectedDbEntity
import|;
end_import

begin_class
class|class
name|DbEntityMerger
extends|extends
name|AbstractMerger
argument_list|<
name|DataMap
argument_list|,
name|DbEntity
argument_list|>
block|{
specifier|private
specifier|final
name|FiltersConfig
name|filtersConfig
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|skipPKTokens
decl_stmt|;
specifier|private
name|DataMap
name|originalDataMap
decl_stmt|;
specifier|private
name|DataMap
name|importedDataMap
decl_stmt|;
name|DbEntityMerger
parameter_list|(
name|MergerTokenFactory
name|tokenFactory
parameter_list|,
name|DataMap
name|original
parameter_list|,
name|DataMap
name|imported
parameter_list|,
name|FiltersConfig
name|filtersConfig
parameter_list|,
name|boolean
name|skipPKTokens
parameter_list|)
block|{
name|super
argument_list|(
name|tokenFactory
argument_list|)
expr_stmt|;
name|this
operator|.
name|filtersConfig
operator|=
name|filtersConfig
expr_stmt|;
name|this
operator|.
name|skipPKTokens
operator|=
name|skipPKTokens
expr_stmt|;
name|originalDataMap
operator|=
name|original
expr_stmt|;
name|importedDataMap
operator|=
name|imported
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|()
block|{
return|return
name|createMergeTokens
argument_list|(
name|originalDataMap
argument_list|,
name|importedDataMap
argument_list|)
return|;
block|}
annotation|@
name|Override
name|MergerDictionaryDiff
argument_list|<
name|DbEntity
argument_list|>
name|createDiff
parameter_list|(
name|DataMap
name|original
parameter_list|,
name|DataMap
name|imported
parameter_list|)
block|{
name|DbEntityDictionary
name|dictionary
init|=
operator|new
name|DbEntityDictionary
argument_list|(
name|original
argument_list|,
name|filtersConfig
argument_list|)
decl_stmt|;
name|MergerDictionaryDiff
argument_list|<
name|DbEntity
argument_list|>
name|diff
init|=
operator|new
name|MergerDictionaryDiff
operator|.
name|Builder
argument_list|<
name|DbEntity
argument_list|>
argument_list|()
operator|.
name|originalDictionary
argument_list|(
name|dictionary
argument_list|)
operator|.
name|importedDictionary
argument_list|(
operator|new
name|DbEntityDictionary
argument_list|(
name|imported
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|setOriginalDictionary
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
return|return
name|diff
return|;
block|}
comment|/**      * Generate Drop Table in DB token      * @param imported DbEntity not found in model but found in DB      */
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingOriginal
parameter_list|(
name|DbEntity
name|imported
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createDropTableToDb
argument_list|(
name|imported
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Generate Create Table in model token      * @param original DbEntity found in model but not found in DB      */
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingImported
parameter_list|(
name|DbEntity
name|original
parameter_list|)
block|{
name|Collection
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
comment|// add entity
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createCreateTableToDb
argument_list|(
name|original
argument_list|)
argument_list|)
expr_stmt|;
comment|// add it's relationships
for|for
control|(
name|DbRelationship
name|rel
range|:
name|original
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createAddRelationshipToDb
argument_list|(
name|original
argument_list|,
name|rel
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|tokens
return|;
block|}
comment|/**      * Compare same entities. For now we check primary keys.      * @param same found DbEntities in model and db      */
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForSame
parameter_list|(
name|MergerDiffPair
argument_list|<
name|DbEntity
argument_list|>
name|same
parameter_list|)
block|{
if|if
condition|(
name|skipPKTokens
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|checkPrimaryKeyChange
argument_list|(
name|same
operator|.
name|getOriginal
argument_list|()
argument_list|,
name|same
operator|.
name|getImported
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|checkPrimaryKeyChange
parameter_list|(
name|DbEntity
name|original
parameter_list|,
name|DbEntity
name|imported
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyOriginal
init|=
name|imported
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
name|original
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
name|imported
operator|instanceof
name|DetectedDbEntity
condition|)
block|{
if|if
condition|(
literal|"VIEW"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|DetectedDbEntity
operator|)
name|imported
operator|)
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
comment|// Views doesn't has PKs in a database, but if the user selects some PKs in a model, we put these keys.
return|return
literal|null
return|;
block|}
name|primaryKeyName
operator|=
operator|(
operator|(
name|DetectedDbEntity
operator|)
name|imported
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
name|Collections
operator|.
name|singleton
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createSetPrimaryKeyToDb
argument_list|(
name|original
argument_list|,
name|primaryKeyOriginal
argument_list|,
name|primaryKeyNew
argument_list|,
name|primaryKeyName
argument_list|)
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
name|attributes
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
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Attribute
name|attr
range|:
name|attributes
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
block|}
end_class

end_unit

