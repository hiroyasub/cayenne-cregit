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
name|reverse
operator|.
name|dbimport
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
name|naming
operator|.
name|NameBuilder
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
name|naming
operator|.
name|ObjectNameGenerator
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * An ObjEntity that may be removed as a result of flattenning relationships.  */
end_comment

begin_class
class|class
name|ManyToManyCandidateEntity
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ManyToManyCandidateEntity
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ObjEntity
name|joinEntity
decl_stmt|;
specifier|private
specifier|final
name|DbRelationship
name|dbRel1
decl_stmt|;
specifier|private
specifier|final
name|DbRelationship
name|dbRel2
decl_stmt|;
specifier|private
specifier|final
name|ObjEntity
name|entity1
decl_stmt|;
specifier|private
specifier|final
name|ObjEntity
name|entity2
decl_stmt|;
specifier|private
specifier|final
name|DbRelationship
name|reverseRelationship1
decl_stmt|;
specifier|private
specifier|final
name|DbRelationship
name|reverseRelationship2
decl_stmt|;
specifier|private
name|ManyToManyCandidateEntity
parameter_list|(
name|ObjEntity
name|entityValue
parameter_list|,
name|List
argument_list|<
name|ObjRelationship
argument_list|>
name|relationships
parameter_list|)
block|{
name|joinEntity
operator|=
name|entityValue
expr_stmt|;
name|ObjRelationship
name|rel1
init|=
name|relationships
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ObjRelationship
name|rel2
init|=
name|relationships
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|dbRel1
operator|=
name|rel1
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|dbRel2
operator|=
name|rel2
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|reverseRelationship1
operator|=
name|dbRel1
operator|.
name|getReverseRelationship
argument_list|()
expr_stmt|;
name|reverseRelationship2
operator|=
name|dbRel2
operator|.
name|getReverseRelationship
argument_list|()
expr_stmt|;
name|entity1
operator|=
name|rel1
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
name|entity2
operator|=
name|rel2
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
comment|/**      * Method check - if current entity represent many to many temporary table      *      * @return true if current entity is represent many to many table; otherwise returns false      */
specifier|public
specifier|static
name|ManyToManyCandidateEntity
name|build
parameter_list|(
name|ObjEntity
name|joinEntity
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|ObjRelationship
argument_list|>
name|relationships
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|joinEntity
operator|.
name|getRelationships
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationships
operator|.
name|size
argument_list|()
operator|!=
literal|2
operator|||
operator|(
name|relationships
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|||
name|relationships
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ManyToManyCandidateEntity
name|candidateEntity
init|=
operator|new
name|ManyToManyCandidateEntity
argument_list|(
name|joinEntity
argument_list|,
name|relationships
argument_list|)
decl_stmt|;
if|if
condition|(
name|candidateEntity
operator|.
name|isManyToMany
argument_list|()
condition|)
block|{
return|return
name|candidateEntity
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|isManyToMany
parameter_list|()
block|{
name|boolean
name|isNotHaveAttributes
init|=
name|joinEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
decl_stmt|;
return|return
name|isNotHaveAttributes
operator|&&
name|reverseRelationship1
operator|!=
literal|null
operator|&&
name|reverseRelationship1
operator|.
name|isToDependentPK
argument_list|()
operator|&&
name|reverseRelationship2
operator|!=
literal|null
operator|&&
name|reverseRelationship2
operator|.
name|isToDependentPK
argument_list|()
operator|&&
name|entity1
operator|!=
literal|null
operator|&&
name|entity2
operator|!=
literal|null
return|;
block|}
specifier|private
name|void
name|addFlattenedRelationship
parameter_list|(
name|ObjectNameGenerator
name|nameGenerator
parameter_list|,
name|ObjEntity
name|srcEntity
parameter_list|,
name|ObjEntity
name|dstEntity
parameter_list|,
name|DbRelationship
name|rel1
parameter_list|,
name|DbRelationship
name|rel2
parameter_list|)
block|{
if|if
condition|(
name|rel1
operator|.
name|getSourceAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|rel2
operator|.
name|getTargetAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Wrong call ManyToManyCandidateEntity.addFlattenedRelationship(... , "
operator|+
name|srcEntity
operator|.
name|getName
argument_list|()
operator|+
literal|", "
operator|+
name|dstEntity
operator|.
name|getName
argument_list|()
operator|+
literal|", ...)"
argument_list|)
expr_stmt|;
return|return;
block|}
name|ObjRelationship
name|newRelationship
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|newRelationship
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|newRelationship
argument_list|,
name|srcEntity
argument_list|)
operator|.
name|baseName
argument_list|(
name|nameGenerator
operator|.
name|relationshipName
argument_list|(
name|rel1
argument_list|,
name|rel2
argument_list|)
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|newRelationship
operator|.
name|setSourceEntity
argument_list|(
name|srcEntity
argument_list|)
expr_stmt|;
name|newRelationship
operator|.
name|setTargetEntityName
argument_list|(
name|dstEntity
argument_list|)
expr_stmt|;
name|newRelationship
operator|.
name|addDbRelationship
argument_list|(
name|rel1
argument_list|)
expr_stmt|;
name|newRelationship
operator|.
name|addDbRelationship
argument_list|(
name|rel2
argument_list|)
expr_stmt|;
name|srcEntity
operator|.
name|addRelationship
argument_list|(
name|newRelationship
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method make direct relationships between 2 entities and remove relationships to      * many to many entity      *      * @param nameGenerator      */
specifier|public
name|void
name|optimizeRelationships
parameter_list|(
name|ObjectNameGenerator
name|nameGenerator
parameter_list|)
block|{
name|entity1
operator|.
name|removeRelationship
argument_list|(
name|reverseRelationship1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|entity2
operator|.
name|removeRelationship
argument_list|(
name|reverseRelationship2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addFlattenedRelationship
argument_list|(
name|nameGenerator
argument_list|,
name|entity1
argument_list|,
name|entity2
argument_list|,
name|reverseRelationship1
argument_list|,
name|dbRel2
argument_list|)
expr_stmt|;
name|addFlattenedRelationship
argument_list|(
name|nameGenerator
argument_list|,
name|entity2
argument_list|,
name|entity1
argument_list|,
name|reverseRelationship2
argument_list|,
name|dbRel1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

