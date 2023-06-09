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
name|map
operator|.
name|ProcedureParameter
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureMerger
extends|extends
name|AbstractMerger
argument_list|<
name|DataMap
argument_list|,
name|Procedure
argument_list|>
block|{
specifier|private
specifier|final
name|FiltersConfig
name|filtersConfig
decl_stmt|;
specifier|private
name|DataMap
name|originalDataMap
decl_stmt|;
specifier|private
name|DataMap
name|importedDataMap
decl_stmt|;
name|ProcedureMerger
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
name|Procedure
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
return|return
operator|new
name|MergerDictionaryDiff
operator|.
name|Builder
argument_list|<
name|Procedure
argument_list|>
argument_list|()
operator|.
name|originalDictionary
argument_list|(
operator|new
name|ProcedureDictionary
argument_list|(
name|original
argument_list|,
name|filtersConfig
argument_list|)
argument_list|)
operator|.
name|importedDictionary
argument_list|(
operator|new
name|ProcedureDictionary
argument_list|(
name|imported
argument_list|,
name|filtersConfig
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingOriginal
parameter_list|(
name|Procedure
name|imported
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createDropProcedureToDb
argument_list|(
name|imported
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingImported
parameter_list|(
name|Procedure
name|original
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createAddProcedureToDb
argument_list|(
name|original
argument_list|)
argument_list|)
return|;
block|}
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
name|Procedure
argument_list|>
name|same
parameter_list|)
block|{
name|Procedure
name|original
init|=
name|same
operator|.
name|getOriginal
argument_list|()
decl_stmt|;
name|Procedure
name|imported
init|=
name|same
operator|.
name|getImported
argument_list|()
decl_stmt|;
if|if
condition|(
name|needToCreateTokens
argument_list|(
name|original
argument_list|,
name|imported
argument_list|)
condition|)
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
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createAddProcedureToDb
argument_list|(
name|original
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createDropProcedureToDb
argument_list|(
name|imported
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|needToCreateTokens
parameter_list|(
name|Procedure
name|original
parameter_list|,
name|Procedure
name|imported
parameter_list|)
block|{
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|originalParams
init|=
name|original
operator|.
name|getCallParameters
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|importedParams
init|=
name|imported
operator|.
name|getCallParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|originalParams
operator|.
name|size
argument_list|()
operator|!=
name|importedParams
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|ProcedureParameter
name|oP
range|:
name|originalParams
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ProcedureParameter
name|iP
range|:
name|importedParams
control|)
block|{
if|if
condition|(
name|oP
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|iP
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|oP
operator|.
name|getType
argument_list|()
operator|==
name|iP
operator|.
name|getType
argument_list|()
operator|&&
name|oP
operator|.
name|getPrecision
argument_list|()
operator|==
name|iP
operator|.
name|getPrecision
argument_list|()
operator|&&
name|oP
operator|.
name|getMaxLength
argument_list|()
operator|==
name|iP
operator|.
name|getMaxLength
argument_list|()
operator|&&
name|oP
operator|.
name|getDirection
argument_list|()
operator|==
name|iP
operator|.
name|getDirection
argument_list|()
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
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
block|}
end_class

end_unit

