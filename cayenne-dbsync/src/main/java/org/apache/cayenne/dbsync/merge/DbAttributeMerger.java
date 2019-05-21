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
name|DetectedDbEntity
import|;
end_import

begin_class
class|class
name|DbAttributeMerger
extends|extends
name|AbstractMerger
argument_list|<
name|DbEntity
argument_list|,
name|DbAttribute
argument_list|>
block|{
specifier|private
specifier|final
name|ValueForNullProvider
name|valueForNull
decl_stmt|;
name|DbAttributeMerger
parameter_list|(
name|MergerTokenFactory
name|tokenFactory
parameter_list|,
name|ValueForNullProvider
name|valueForNull
parameter_list|)
block|{
name|super
argument_list|(
name|tokenFactory
argument_list|)
expr_stmt|;
name|this
operator|.
name|valueForNull
operator|=
name|valueForNull
expr_stmt|;
block|}
annotation|@
name|Override
name|MergerDictionaryDiff
argument_list|<
name|DbAttribute
argument_list|>
name|createDiff
parameter_list|(
name|DbEntity
name|original
parameter_list|,
name|DbEntity
name|imported
parameter_list|)
block|{
return|return
operator|new
name|MergerDictionaryDiff
operator|.
name|Builder
argument_list|<
name|DbAttribute
argument_list|>
argument_list|()
operator|.
name|originalDictionary
argument_list|(
operator|new
name|DbAttributeDictionary
argument_list|(
name|original
argument_list|)
argument_list|)
operator|.
name|importedDictionary
argument_list|(
operator|new
name|DbAttributeDictionary
argument_list|(
name|imported
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Add column to db      * @param original attribute found in model but missing in db      */
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingImported
parameter_list|(
name|DbAttribute
name|original
parameter_list|)
block|{
name|DbEntity
name|originalDbEntity
init|=
name|original
operator|.
name|getEntity
argument_list|()
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
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createAddColumnToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|original
argument_list|)
argument_list|)
expr_stmt|;
comment|// Create not null check
if|if
condition|(
name|original
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
name|originalDbEntity
argument_list|,
name|original
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createSetValueForNullToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|original
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
name|getTokenFactory
argument_list|()
operator|.
name|createSetNotNullToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|original
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|original
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|originalDbEntity
operator|instanceof
name|DetectedDbEntity
operator|&&
literal|"VIEW"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|DetectedDbEntity
operator|)
name|originalDbEntity
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
return|return
name|tokens
return|;
block|}
comment|/**      * Drop column in db      * @param imported attribute found in db but missing in model      */
annotation|@
name|Override
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|createTokensForMissingOriginal
parameter_list|(
name|DbAttribute
name|imported
parameter_list|)
block|{
name|DbEntity
name|originalDbEntity
init|=
name|getOriginalDictionary
argument_list|()
operator|.
name|getByName
argument_list|(
name|imported
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createDropColumnToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|imported
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Compare same attributes in model and db      */
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
name|DbAttribute
argument_list|>
name|same
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// isMandatory flag
name|checkMandatory
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
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
comment|// check type (including max length, scale and precision)
name|checkType
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
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
comment|// not implemented yet
comment|// isGenerated flag
name|checkIsGenerated
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
argument_list|,
name|tokens
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
specifier|private
name|void
name|checkMandatory
parameter_list|(
name|DbAttribute
name|original
parameter_list|,
name|DbAttribute
name|imported
parameter_list|,
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
if|if
condition|(
name|original
operator|.
name|isMandatory
argument_list|()
operator|==
name|imported
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
return|return;
block|}
name|DbEntity
name|originalDbEntity
init|=
name|original
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|original
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
name|originalDbEntity
argument_list|,
name|original
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createSetValueForNullToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|original
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
name|getTokenFactory
argument_list|()
operator|.
name|createSetNotNullToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|original
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
name|getTokenFactory
argument_list|()
operator|.
name|createSetAllowNullToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|original
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check whether attributes have same type, max length, scale and precision      * @param original attribute in model      * @param imported attribute from db      * @return true if attributes not same      */
specifier|private
name|boolean
name|needUpdateType
parameter_list|(
name|DbAttribute
name|original
parameter_list|,
name|DbAttribute
name|imported
parameter_list|)
block|{
if|if
condition|(
name|original
operator|.
name|getType
argument_list|()
operator|!=
name|imported
operator|.
name|getType
argument_list|()
condition|)
block|{
comment|// Decimal and NUMERIC types are effectively equal so skip their interchange
if|if
condition|(
operator|(
name|original
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|DECIMAL
operator|||
name|original
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|NUMERIC
operator|)
operator|&&
operator|(
name|imported
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|DECIMAL
operator|||
name|imported
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|NUMERIC
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
if|if
condition|(
name|original
operator|.
name|getMaxLength
argument_list|()
operator|!=
name|imported
operator|.
name|getMaxLength
argument_list|()
condition|)
block|{
name|int
index|[]
name|typesWithMaxLength
init|=
block|{
name|Types
operator|.
name|NCHAR
block|,
name|Types
operator|.
name|NVARCHAR
block|,
name|Types
operator|.
name|CHAR
block|,
name|Types
operator|.
name|VARCHAR
block|}
decl_stmt|;
for|for
control|(
name|int
name|type
range|:
name|typesWithMaxLength
control|)
block|{
if|if
condition|(
name|original
operator|.
name|getType
argument_list|()
operator|==
name|type
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
if|if
condition|(
name|needUpdateScale
argument_list|(
name|original
argument_list|,
name|imported
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|original
operator|.
name|getAttributePrecision
argument_list|()
operator|!=
name|imported
operator|.
name|getAttributePrecision
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|needUpdateScale
parameter_list|(
name|DbAttribute
name|original
parameter_list|,
name|DbAttribute
name|imported
parameter_list|)
block|{
if|if
condition|(
name|original
operator|.
name|getScale
argument_list|()
operator|==
name|imported
operator|.
name|getScale
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// -1 and 0 are actually equal values for scale
if|if
condition|(
operator|(
name|original
operator|.
name|getScale
argument_list|()
operator|==
operator|-
literal|1
operator|||
name|original
operator|.
name|getScale
argument_list|()
operator|==
literal|0
operator|)
operator|&&
operator|(
name|imported
operator|.
name|getScale
argument_list|()
operator|==
operator|-
literal|1
operator|||
name|imported
operator|.
name|getScale
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|checkType
parameter_list|(
name|DbAttribute
name|original
parameter_list|,
name|DbAttribute
name|imported
parameter_list|,
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
if|if
condition|(
operator|!
name|needUpdateType
argument_list|(
name|original
argument_list|,
name|imported
argument_list|)
condition|)
block|{
return|return;
block|}
name|DbEntity
name|originalDbEntity
init|=
name|original
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createSetColumnTypeToDb
argument_list|(
name|originalDbEntity
argument_list|,
name|imported
argument_list|,
name|original
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkIsGenerated
parameter_list|(
name|DbAttribute
name|original
parameter_list|,
name|DbAttribute
name|imported
parameter_list|,
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
if|if
condition|(
name|original
operator|.
name|isGenerated
argument_list|()
operator|==
name|imported
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
return|return;
block|}
name|tokens
operator|.
name|add
argument_list|(
name|getTokenFactory
argument_list|()
operator|.
name|createSetGeneratedFlagToDb
argument_list|(
name|original
operator|.
name|getEntity
argument_list|()
argument_list|,
name|original
argument_list|,
name|original
operator|.
name|isGenerated
argument_list|()
argument_list|)
argument_list|)
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

