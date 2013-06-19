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
name|project
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|DeleteRule
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
name|map
operator|.
name|ObjRelationship
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
name|util
operator|.
name|Util
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_class
class|class
name|ObjRelationshipValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"Unnamed ObjRelationship"
argument_list|)
expr_stmt|;
block|}
comment|// check if there are attributes having the same name
if|else if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship '%s' has the same name as one of ObjAttributes"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NameValidationHelper
name|helper
init|=
name|NameValidationHelper
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|invalidChars
init|=
name|helper
operator|.
name|invalidCharsInObjPathComponent
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalidChars
operator|!=
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship name '%s' contains invalid characters: %s"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|,
name|invalidChars
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|helper
operator|.
name|invalidDataObjectProperty
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship name '%s' is a reserved word"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship '%s' has no target entity"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// check for missing DbRelationship mappings
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRels
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbRels
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship '%s' has no DbRelationship mapping"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DbEntity
name|expectedSrc
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbEntity
name|expectedTarget
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|dbRels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getSourceEntity
argument_list|()
operator|!=
name|expectedSrc
operator|||
operator|(
name|dbRels
operator|.
name|get
argument_list|(
name|dbRels
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|)
operator|.
name|getTargetEntity
argument_list|()
operator|!=
name|expectedTarget
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship '%s' has incomplete DbRelationship mapping"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Disallow a Nullify delete rule where the relationship is toMany and the
comment|// foreign key attributes are mandatory.
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
operator|&&
operator|!
name|relationship
operator|.
name|isFlattened
argument_list|()
operator|&&
operator|(
name|relationship
operator|.
name|getDeleteRule
argument_list|()
operator|==
name|DeleteRule
operator|.
name|NULLIFY
operator|)
condition|)
block|{
name|ObjRelationship
name|inverse
init|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|inverse
operator|!=
literal|null
condition|)
block|{
name|DbRelationship
name|firstRel
init|=
name|inverse
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|DbJoin
argument_list|>
name|attributePairIterator
init|=
name|firstRel
operator|.
name|getJoins
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// by default, the relation will be check for mandatory.
name|boolean
name|check
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|attributePairIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbJoin
name|pair
init|=
name|attributePairIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pair
operator|.
name|getSource
argument_list|()
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
comment|// a field of the fk can be nullable, cancel the check.
name|check
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|check
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjRelationship '%s' has a Nullify delete rule and a mandatory reverse relationship"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// check for relationships with same source and target entities
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjRelationship
name|rel
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|relationship
operator|.
name|getDbRelationshipPath
argument_list|()
operator|!=
literal|null
operator|&&
name|relationship
operator|.
name|getDbRelationshipPath
argument_list|()
operator|.
name|equals
argument_list|(
name|rel
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|relationship
operator|!=
name|rel
operator|&&
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|==
name|rel
operator|.
name|getTargetEntity
argument_list|()
operator|&&
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|==
name|rel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjectRelationship '%s' duplicates relationship '%s'"
argument_list|,
name|toString
argument_list|(
name|relationship
argument_list|)
argument_list|,
name|toString
argument_list|(
name|rel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// check for invalid relationships in inherited entities
if|if
condition|(
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ObjRelationship
name|revRel
init|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|!=
name|revRel
operator|.
name|getTargetEntity
argument_list|()
operator|||
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|!=
name|revRel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|revRel
argument_list|,
literal|"Usage of super entity's relationships '%s' as reversed relationships for sub entity is discouraged"
argument_list|,
name|toString
argument_list|(
name|revRel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|checkForDuplicates
argument_list|(
name|relationship
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
comment|/**      * Per CAY-1813, make sure two (or more) ObjRelationships do not map to the      * same database path.      */
specifier|private
name|void
name|checkForDuplicates
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|!=
literal|null
operator|&&
name|relationship
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|dbRelationshipPath
init|=
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
operator|+
literal|"."
operator|+
name|relationship
operator|.
name|getDbRelationshipPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbRelationshipPath
operator|!=
literal|null
condition|)
block|{
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjRelationship
name|comparisonRelationship
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|relationship
operator|!=
name|comparisonRelationship
condition|)
block|{
name|String
name|comparisonDbRelationshipPath
init|=
name|comparisonRelationship
operator|.
name|getTargetEntityName
argument_list|()
operator|+
literal|"."
operator|+
name|comparisonRelationship
operator|.
name|getDbRelationshipPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbRelationshipPath
operator|.
name|equals
argument_list|(
name|comparisonDbRelationshipPath
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|relationship
argument_list|,
literal|"ObjEntity '%s' contains a duplicate ObjRelationship mapping ('%s' -> '%s')"
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|dbRelationshipPath
argument_list|)
expr_stmt|;
return|return;
comment|// Duplicate found, stop.
block|}
block|}
block|}
block|}
block|}
block|}
specifier|private
name|String
name|toString
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|"[null source entity]."
operator|+
name|relationship
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|relationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|relationship
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

