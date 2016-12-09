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
name|map
operator|.
name|DataMap
import|;
end_import

begin_class
specifier|abstract
class|class
name|AbstractMerger
parameter_list|<
name|T
parameter_list|,
name|M
parameter_list|>
implements|implements
name|Merger
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|MergerDictionaryDiff
argument_list|<
name|M
argument_list|>
name|diff
decl_stmt|;
specifier|private
name|MergerTokenFactory
name|tokenFactory
decl_stmt|;
name|DataMap
name|originalDataMap
decl_stmt|;
name|DataMap
name|importedDataMap
decl_stmt|;
name|AbstractMerger
parameter_list|(
name|MergerTokenFactory
name|tokenFactory
parameter_list|,
name|DataMap
name|original
parameter_list|,
name|DataMap
name|imported
parameter_list|)
block|{
name|this
operator|.
name|tokenFactory
operator|=
name|tokenFactory
expr_stmt|;
name|this
operator|.
name|originalDataMap
operator|=
name|original
expr_stmt|;
name|this
operator|.
name|importedDataMap
operator|=
name|imported
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MergerToken
argument_list|>
name|createMergeTokens
parameter_list|(
name|T
name|original
parameter_list|,
name|T
name|imported
parameter_list|)
block|{
name|diff
operator|=
name|createDiff
argument_list|(
name|original
argument_list|,
name|imported
argument_list|)
expr_stmt|;
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
for|for
control|(
name|MergerDiffPair
argument_list|<
name|M
argument_list|>
name|pair
range|:
name|diff
operator|.
name|getMissing
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|tokensForMissing
init|=
name|createTokensForMissing
argument_list|(
name|pair
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokensForMissing
operator|!=
literal|null
condition|)
block|{
name|tokens
operator|.
name|addAll
argument_list|(
name|tokensForMissing
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|MergerDiffPair
argument_list|<
name|M
argument_list|>
name|pair
range|:
name|diff
operator|.
name|getSame
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|tokensForSame
init|=
name|createTokensForSame
argument_list|(
name|pair
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokensForSame
operator|!=
literal|null
condition|)
block|{
name|tokens
operator|.
name|addAll
argument_list|(
name|tokensForSame
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tokens
return|;
block|}
name|MergerDictionaryDiff
argument_list|<
name|M
argument_list|>
name|getDiff
parameter_list|()
block|{
return|return
name|diff
return|;
block|}
name|MergerTokenFactory
name|getTokenFactory
parameter_list|()
block|{
return|return
name|tokenFactory
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissing
parameter_list|(
name|MergerDiffPair
argument_list|<
name|M
argument_list|>
name|missing
parameter_list|)
block|{
if|if
condition|(
name|missing
operator|.
name|getOriginal
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|createTokensForMissingOriginal
argument_list|(
name|missing
operator|.
name|getImported
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createTokensForMissingImported
argument_list|(
name|missing
operator|.
name|getOriginal
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|abstract
name|MergerDictionaryDiff
argument_list|<
name|M
argument_list|>
name|createDiff
parameter_list|(
name|T
name|original
parameter_list|,
name|T
name|imported
parameter_list|)
function_decl|;
specifier|abstract
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingOriginal
parameter_list|(
name|M
name|imported
parameter_list|)
function_decl|;
specifier|abstract
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingImported
parameter_list|(
name|M
name|original
parameter_list|)
function_decl|;
specifier|abstract
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForSame
parameter_list|(
name|MergerDiffPair
argument_list|<
name|M
argument_list|>
name|same
parameter_list|)
function_decl|;
block|}
end_class

end_unit

