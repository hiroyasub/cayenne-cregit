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
name|Map
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

begin_class
class|class
name|MergerDictionaryDiff
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|same
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|missing
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|MergerDictionaryDiff
parameter_list|()
block|{
block|}
specifier|public
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|getSame
parameter_list|()
block|{
return|return
name|same
return|;
block|}
specifier|public
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|getMissing
parameter_list|()
block|{
return|return
name|missing
return|;
block|}
specifier|public
name|void
name|addAll
parameter_list|(
name|MergerDictionaryDiff
argument_list|<
name|T
argument_list|>
name|other
parameter_list|)
block|{
name|same
operator|.
name|addAll
argument_list|(
name|other
operator|.
name|getSame
argument_list|()
argument_list|)
expr_stmt|;
name|missing
operator|.
name|addAll
argument_list|(
name|other
operator|.
name|getMissing
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|Builder
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|MergerDictionaryDiff
argument_list|<
name|T
argument_list|>
name|diff
decl_stmt|;
specifier|private
name|MergerDictionary
argument_list|<
name|T
argument_list|>
name|originalDictionary
decl_stmt|;
specifier|private
name|MergerDictionary
argument_list|<
name|T
argument_list|>
name|importedDictionary
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|sameNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Builder
parameter_list|()
block|{
name|diff
operator|=
operator|new
name|MergerDictionaryDiff
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|Builder
argument_list|<
name|T
argument_list|>
name|originalDictionary
parameter_list|(
name|MergerDictionary
argument_list|<
name|T
argument_list|>
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|originalDictionary
operator|=
name|dictionary
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
argument_list|<
name|T
argument_list|>
name|importedDictionary
parameter_list|(
name|MergerDictionary
argument_list|<
name|T
argument_list|>
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|importedDictionary
operator|=
name|dictionary
expr_stmt|;
return|return
name|this
return|;
block|}
name|MergerDictionaryDiff
argument_list|<
name|T
argument_list|>
name|build
parameter_list|()
block|{
if|if
condition|(
name|originalDictionary
operator|==
literal|null
operator|||
name|importedDictionary
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Dictionaries not set"
argument_list|)
throw|;
block|}
name|originalDictionary
operator|.
name|init
argument_list|()
expr_stmt|;
name|importedDictionary
operator|.
name|init
argument_list|()
expr_stmt|;
name|diff
operator|.
name|same
operator|=
name|buildSame
argument_list|()
expr_stmt|;
name|diff
operator|.
name|missing
operator|=
name|buildMissing
argument_list|()
expr_stmt|;
return|return
name|diff
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|buildSame
parameter_list|()
block|{
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|sameEntities
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|entry
range|:
name|originalDictionary
operator|.
name|getDictionary
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|T
name|original
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|T
name|imported
init|=
name|importedDictionary
operator|.
name|getByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|imported
operator|!=
literal|null
condition|)
block|{
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
name|pair
init|=
operator|new
name|MergerDiffPair
argument_list|<>
argument_list|(
name|original
argument_list|,
name|imported
argument_list|)
decl_stmt|;
name|sameEntities
operator|.
name|add
argument_list|(
name|pair
argument_list|)
expr_stmt|;
name|sameNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sameEntities
return|;
block|}
specifier|private
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|buildMissing
parameter_list|()
block|{
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|missingEntities
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|addMissingFromDictionary
argument_list|(
name|missingEntities
argument_list|,
name|originalDictionary
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|addMissingFromDictionary
argument_list|(
name|missingEntities
argument_list|,
name|importedDictionary
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|missingEntities
return|;
block|}
specifier|private
name|void
name|addMissingFromDictionary
parameter_list|(
name|List
argument_list|<
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
argument_list|>
name|missingEntities
parameter_list|,
name|MergerDictionary
argument_list|<
name|T
argument_list|>
name|dictionary
parameter_list|,
name|boolean
name|isOriginal
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|entry
range|:
name|dictionary
operator|.
name|getDictionary
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|sameNames
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|MergerDiffPair
argument_list|<
name|T
argument_list|>
name|pair
init|=
operator|new
name|MergerDiffPair
argument_list|<>
argument_list|(
name|isOriginal
condition|?
name|entry
operator|.
name|getValue
argument_list|()
else|:
literal|null
argument_list|,
name|isOriginal
condition|?
literal|null
else|:
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|missingEntities
operator|.
name|add
argument_list|(
name|pair
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

