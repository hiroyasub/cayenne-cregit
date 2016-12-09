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
name|merge
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
name|Collections
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
name|EmptyValueForNullProvider
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
name|ValueForNullProvider
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
name|merge
operator|.
name|token
operator|.
name|TokenComparator
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

begin_comment
comment|/**  * Synchronization data base store and Cayenne model.  */
end_comment

begin_class
specifier|public
class|class
name|DataMapMerger
implements|implements
name|Merger
argument_list|<
name|DataMap
argument_list|>
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
name|DataMapMerger
parameter_list|()
block|{
block|}
comment|/**      * Create List of MergerToken that represent the difference between two {@link DataMap} objects.      */
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|DataMap
name|original
parameter_list|,
name|DataMap
name|importedFromDb
parameter_list|)
block|{
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|DbEntityMerger
name|dbEntityMerger
init|=
name|mergeDbEntities
argument_list|(
name|tokens
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|)
decl_stmt|;
name|mergeAttributes
argument_list|(
name|tokens
argument_list|,
name|dbEntityMerger
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|)
expr_stmt|;
name|mergeRelationships
argument_list|(
name|tokens
argument_list|,
name|dbEntityMerger
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|)
expr_stmt|;
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
name|DbEntityMerger
name|mergeDbEntities
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|,
name|DataMap
name|original
parameter_list|,
name|DataMap
name|importedFromDb
parameter_list|)
block|{
name|DbEntityMerger
name|dbEntityMerger
init|=
operator|new
name|DbEntityMerger
argument_list|(
name|tokenFactory
argument_list|,
name|skipPKTokens
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|addAll
argument_list|(
name|dbEntityMerger
operator|.
name|createMergeTokens
argument_list|(
name|original
argument_list|,
name|importedFromDb
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dbEntityMerger
return|;
block|}
specifier|private
name|void
name|mergeAttributes
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|,
name|DbEntityMerger
name|dbEntityMerger
parameter_list|,
name|DataMap
name|original
parameter_list|,
name|DataMap
name|importedFromDb
parameter_list|)
block|{
name|ChainMerger
argument_list|<
name|DbEntity
argument_list|,
name|DbAttribute
argument_list|>
name|dbAttributeMerger
init|=
operator|new
name|ChainMerger
argument_list|<>
argument_list|(
name|tokenFactory
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|,
operator|new
name|DbAttributeMerger
argument_list|(
name|tokenFactory
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|,
name|valueForNull
argument_list|)
argument_list|,
name|dbEntityMerger
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|addAll
argument_list|(
name|dbAttributeMerger
operator|.
name|createMergeTokens
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|mergeRelationships
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|,
name|DbEntityMerger
name|dbEntityMerger
parameter_list|,
name|DataMap
name|original
parameter_list|,
name|DataMap
name|importedFromDb
parameter_list|)
block|{
name|ChainMerger
argument_list|<
name|DbEntity
argument_list|,
name|DbRelationship
argument_list|>
name|dbRelationshipMerger
init|=
operator|new
name|ChainMerger
argument_list|<>
argument_list|(
name|tokenFactory
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|,
operator|new
name|DbRelationshipMerger
argument_list|(
name|tokenFactory
argument_list|,
name|original
argument_list|,
name|importedFromDb
argument_list|,
name|skipRelationshipsTokens
argument_list|)
argument_list|,
name|dbEntityMerger
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|addAll
argument_list|(
name|dbRelationshipMerger
operator|.
name|createMergeTokens
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
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
name|DataMapMerger
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
specifier|public
specifier|static
class|class
name|Builder
block|{
specifier|private
name|DataMapMerger
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
name|DataMapMerger
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
name|DataMapMerger
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

