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
name|project2
operator|.
name|validate
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
name|project
operator|.
name|validator
operator|.
name|MappingNamesHelper
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

begin_class
class|class
name|ObjRelationshipValidator
block|{
name|void
name|validate
parameter_list|(
name|Object
name|object
parameter_list|,
name|ConfigurationValidationVisitor
name|validator
parameter_list|)
block|{
name|ObjRelationship
name|rel
init|=
operator|(
name|ObjRelationship
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Unnamed ObjRelationship."
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|// check if there are attributes having the same name
if|else if
condition|(
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" has the same name as one of ObjAttributes"
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MappingNamesHelper
name|helper
init|=
name|MappingNamesHelper
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
name|rel
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
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" name contains invalid characters: "
operator|+
name|invalidChars
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|helper
operator|.
name|invalidDataObjectProperty
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" name is invalid."
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|rel
operator|.
name|getTargetEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" has no target entity."
argument_list|,
name|object
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
name|rel
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbRels
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" has no DbRelationship mapping."
argument_list|,
name|object
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
name|rel
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
name|rel
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
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" has incomplete DbRelationship mapping."
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Disallow a Nullify delete rule where the relationship is toMany and the
comment|// foreign key attributes are mandatory.
if|if
condition|(
name|rel
operator|.
name|isToMany
argument_list|()
operator|&&
operator|!
name|rel
operator|.
name|isFlattened
argument_list|()
operator|&&
operator|(
name|rel
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
name|rel
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
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"ObjRelationship "
operator|+
name|objRelationshipIdentifier
argument_list|(
name|rel
argument_list|)
operator|+
literal|" has a Nullify delete rule and a mandatory reverse relationship "
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|String
name|objRelationshipIdentifier
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|rel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
return|return
literal|"<[null source entity]."
operator|+
name|rel
operator|.
name|getName
argument_list|()
operator|+
literal|">"
return|;
block|}
return|return
literal|"<"
operator|+
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|rel
operator|.
name|getName
argument_list|()
operator|+
literal|">"
return|;
block|}
block|}
end_class

end_unit

