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
operator|.
name|context
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
name|dba
operator|.
name|TypesMapping
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
name|filter
operator|.
name|NameFilter
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
name|Entity
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
name|ObjAttribute
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
name|map
operator|.
name|Relationship
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
name|DeleteRuleUpdater
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
name|EntityMergeListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Implements methods for entity merging.  */
end_comment

begin_class
specifier|public
class|class
name|EntityMergeSupport
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EntityMergeSupport
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|CLASS_TO_PRIMITIVE
decl_stmt|;
static|static
block|{
name|CLASS_TO_PRIMITIVE
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"byte"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"long"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"double"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"boolean"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"float"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"short"
argument_list|)
expr_stmt|;
name|CLASS_TO_PRIMITIVE
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"int"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|ObjectNameGenerator
name|nameGenerator
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|EntityMergeListener
argument_list|>
name|listeners
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|removingMeaningfulFKs
decl_stmt|;
specifier|private
specifier|final
name|NameFilter
name|meaningfulPKsFilter
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|usingPrimitives
decl_stmt|;
specifier|public
name|EntityMergeSupport
parameter_list|(
name|ObjectNameGenerator
name|nameGenerator
parameter_list|,
name|NameFilter
name|meaningfulPKsFilter
parameter_list|,
name|boolean
name|removingMeaningfulFKs
parameter_list|,
name|boolean
name|usingPrimitives
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|nameGenerator
operator|=
name|nameGenerator
expr_stmt|;
name|this
operator|.
name|removingMeaningfulFKs
operator|=
name|removingMeaningfulFKs
expr_stmt|;
name|this
operator|.
name|meaningfulPKsFilter
operator|=
name|meaningfulPKsFilter
expr_stmt|;
name|this
operator|.
name|usingPrimitives
operator|=
name|usingPrimitives
expr_stmt|;
comment|// will ensure that all created ObjRelationships would have
comment|// default delete rule
name|addEntityMergeListener
argument_list|(
name|DeleteRuleUpdater
operator|.
name|getEntityMergeListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRemovingMeaningfulFKs
parameter_list|()
block|{
return|return
name|removingMeaningfulFKs
return|;
block|}
comment|/**      * Updates each one of the collection of ObjEntities, adding attributes and      * relationships based on the current state of its DbEntity.      *      * @return true if any ObjEntity has changed as a result of synchronization.      */
specifier|public
name|boolean
name|synchronizeWithDbEntities
parameter_list|(
name|Iterable
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ObjEntity
name|nextEntity
range|:
name|objEntities
control|)
block|{
if|if
condition|(
name|synchronizeWithDbEntity
argument_list|(
name|nextEntity
argument_list|)
condition|)
block|{
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
comment|/**      * Updates ObjEntity attributes and relationships based on the current state      * of its DbEntity.      *      * @return true if the ObjEntity has changed as a result of synchronization.      */
specifier|public
name|boolean
name|synchronizeWithDbEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbEntity
name|dbEntity
init|=
name|entity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|removingMeaningfulFKs
condition|)
block|{
name|changed
operator|=
name|getRidOfAttributesThatAreNowSrcAttributesForRelationships
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|changed
operator||=
name|addMissingAttributes
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|changed
operator||=
name|addMissingRelationships
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
name|changed
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|boolean
name|synchronizeOnDbAttributeAdded
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|incomingRels
init|=
name|getIncomingRelationships
argument_list|(
name|dbAttribute
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|shouldAddToObjEntity
argument_list|(
name|entity
argument_list|,
name|dbAttribute
argument_list|,
name|incomingRels
argument_list|)
condition|)
block|{
name|addMissingAttribute
argument_list|(
name|entity
argument_list|,
name|dbAttribute
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|boolean
name|synchronizeOnDbRelationshipAdded
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbRelationship
name|dbRelationship
parameter_list|)
block|{
if|if
condition|(
name|shouldAddToObjEntity
argument_list|(
name|entity
argument_list|,
name|dbRelationship
argument_list|)
condition|)
block|{
name|addMissingRelationship
argument_list|(
name|entity
argument_list|,
name|dbRelationship
argument_list|)
expr_stmt|;
if|if
condition|(
name|removingMeaningfulFKs
condition|)
block|{
name|getRidOfAttributesThatAreNowSrcAttributesForRelationships
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|addMissingRelationships
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationshipsToAdd
init|=
name|getRelationshipsToAdd
argument_list|(
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationshipsToAdd
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|DbRelationship
name|dr
range|:
name|relationshipsToAdd
control|)
block|{
name|addMissingRelationship
argument_list|(
name|entity
argument_list|,
name|dr
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|createObjRelationship
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbRelationship
name|dr
parameter_list|,
name|String
name|targetEntityName
parameter_list|)
block|{
name|ObjRelationship
name|or
init|=
operator|new
name|ObjRelationship
argument_list|()
decl_stmt|;
name|or
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|or
argument_list|,
name|entity
argument_list|)
operator|.
name|baseName
argument_list|(
name|nameGenerator
operator|.
name|relationshipName
argument_list|(
name|dr
argument_list|)
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|or
operator|.
name|addDbRelationship
argument_list|(
name|dr
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ObjEntity
argument_list|>
name|objEntities
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getSubclassesForObjEntity
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|boolean
name|hasFlattingAttributes
init|=
literal|false
decl_stmt|;
name|boolean
name|needGeneratedEntity
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|objEntities
operator|.
name|containsKey
argument_list|(
name|targetEntityName
argument_list|)
condition|)
block|{
name|needGeneratedEntity
operator|=
literal|false
expr_stmt|;
block|}
for|for
control|(
name|ObjEntity
name|subObjEntity
range|:
name|objEntities
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|ObjAttribute
name|objAttribute
range|:
name|subObjEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|String
name|path
init|=
name|objAttribute
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|or
operator|.
name|getDbRelationshipPath
argument_list|()
argument_list|)
condition|)
block|{
name|hasFlattingAttributes
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|hasFlattingAttributes
condition|)
block|{
if|if
condition|(
name|needGeneratedEntity
condition|)
block|{
name|or
operator|.
name|setTargetEntityName
argument_list|(
name|targetEntityName
argument_list|)
expr_stmt|;
name|or
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|entity
operator|.
name|addRelationship
argument_list|(
name|or
argument_list|)
expr_stmt|;
name|fireRelationshipAdded
argument_list|(
name|or
argument_list|)
expr_stmt|;
block|}
return|return
name|needGeneratedEntity
return|;
block|}
specifier|private
name|boolean
name|addMissingAttributes
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbAttribute
name|da
range|:
name|getAttributesToAdd
argument_list|(
name|entity
argument_list|)
control|)
block|{
name|addMissingAttribute
argument_list|(
name|entity
argument_list|,
name|da
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|changed
return|;
block|}
specifier|private
name|void
name|addMissingRelationship
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbRelationship
name|dbRelationship
parameter_list|)
block|{
comment|// getting DataMap from DbRelationship's source entity. This is the only object in our arguments that
comment|// is guaranteed to be a part of the map....
name|DataMap
name|dataMap
init|=
name|dbRelationship
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|DbEntity
name|targetEntity
init|=
name|dbRelationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|mappedObjEntities
init|=
name|dataMap
operator|.
name|getMappedEntities
argument_list|(
name|targetEntity
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappedObjEntities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|targetEntity
operator|==
literal|null
condition|)
block|{
name|targetEntity
operator|=
operator|new
name|DbEntity
argument_list|(
name|dbRelationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dbRelationship
operator|.
name|getTargetEntityName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|boolean
name|needGeneratedEntity
init|=
name|createObjRelationship
argument_list|(
name|entity
argument_list|,
name|dbRelationship
argument_list|,
name|nameGenerator
operator|.
name|objEntityName
argument_list|(
name|targetEntity
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|needGeneratedEntity
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't find ObjEntity for "
operator|+
name|dbRelationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Db Relationship ("
operator|+
name|dbRelationship
operator|+
literal|") will have GUESSED Obj Relationship reflection. "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
for|for
control|(
name|Entity
name|mappedTarget
range|:
name|mappedObjEntities
control|)
block|{
name|createObjRelationship
argument_list|(
name|entity
argument_list|,
name|dbRelationship
argument_list|,
name|mappedTarget
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addMissingAttribute
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbAttribute
name|da
parameter_list|)
block|{
name|ObjAttribute
name|oa
init|=
operator|new
name|ObjAttribute
argument_list|()
decl_stmt|;
name|oa
operator|.
name|setName
argument_list|(
name|NameBuilder
operator|.
name|builder
argument_list|(
name|oa
argument_list|,
name|entity
argument_list|)
operator|.
name|baseName
argument_list|(
name|nameGenerator
operator|.
name|objAttributeName
argument_list|(
name|da
argument_list|)
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|oa
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|da
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|usingPrimitives
condition|)
block|{
name|String
name|primitive
init|=
name|CLASS_TO_PRIMITIVE
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|primitive
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|primitive
expr_stmt|;
block|}
block|}
name|oa
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|oa
operator|.
name|setDbAttributePath
argument_list|(
name|da
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|oa
argument_list|)
expr_stmt|;
name|fireAttributeAdded
argument_list|(
name|oa
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|getRidOfAttributesThatAreNowSrcAttributesForRelationships
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|DbAttribute
name|da
range|:
name|getMeaningfulFKs
argument_list|(
name|entity
argument_list|)
control|)
block|{
name|ObjAttribute
name|oa
init|=
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|da
argument_list|)
decl_stmt|;
while|while
condition|(
name|oa
operator|!=
literal|null
condition|)
block|{
name|String
name|attrName
init|=
name|oa
operator|.
name|getName
argument_list|()
decl_stmt|;
name|entity
operator|.
name|removeAttribute
argument_list|(
name|attrName
argument_list|)
expr_stmt|;
name|changed
operator|=
literal|true
expr_stmt|;
name|oa
operator|=
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|da
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
comment|/**      * Returns a list of DbAttributes that are mapped to foreign keys.      *      * @since 1.2      */
specifier|public
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|getMeaningfulFKs
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|fks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|property
range|:
name|objEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|DbAttribute
name|column
init|=
name|property
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
comment|// check if adding it makes sense at all
if|if
condition|(
name|column
operator|!=
literal|null
operator|&&
name|column
operator|.
name|isForeignKey
argument_list|()
condition|)
block|{
name|fks
operator|.
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fks
return|;
block|}
comment|/**      * Returns a list of attributes that exist in the DbEntity, but are missing      * from the ObjEntity.      */
specifier|protected
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|getAttributesToAdd
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|DbEntity
name|dbEntity
init|=
name|objEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|missing
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|incomingRels
init|=
name|getIncomingRelationships
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
for|for
control|(
name|DbAttribute
name|dba
range|:
name|dbEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|shouldAddToObjEntity
argument_list|(
name|objEntity
argument_list|,
name|dba
argument_list|,
name|incomingRels
argument_list|)
condition|)
block|{
name|missing
operator|.
name|add
argument_list|(
name|dba
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|missing
return|;
block|}
specifier|protected
name|boolean
name|shouldAddToObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|incomingRels
parameter_list|)
block|{
if|if
condition|(
name|dbAttribute
operator|.
name|getName
argument_list|()
operator|==
literal|null
operator|||
name|entity
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|dbAttribute
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|addMeaningfulPK
init|=
name|meaningfulPKsFilter
operator|.
name|isIncluded
argument_list|(
name|entity
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
operator|!
name|addMeaningfulPK
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// check FK's
name|boolean
name|isFK
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|rit
init|=
name|dbAttribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|isFK
operator|&&
name|rit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|rel
init|=
name|rit
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
name|join
operator|.
name|getSource
argument_list|()
operator|==
name|dbAttribute
condition|)
block|{
name|isFK
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|addMeaningfulPK
condition|)
block|{
if|if
condition|(
operator|!
name|dbAttribute
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|isFK
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isFK
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// check incoming relationships
name|rit
operator|=
name|incomingRels
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|isFK
operator|&&
name|rit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|rel
init|=
name|rit
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
name|join
operator|.
name|getTarget
argument_list|()
operator|==
name|dbAttribute
condition|)
block|{
name|isFK
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|addMeaningfulPK
condition|)
block|{
if|if
condition|(
operator|!
name|dbAttribute
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|isFK
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isFK
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
name|boolean
name|shouldAddToObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|DbRelationship
name|dbRelationship
parameter_list|)
block|{
if|if
condition|(
name|dbRelationship
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Relationship
name|relationship
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|ObjRelationship
name|objRelationship
init|=
operator|(
name|ObjRelationship
operator|)
name|relationship
decl_stmt|;
if|if
condition|(
name|objRelationshipHasDbRelationship
argument_list|(
name|objRelationship
argument_list|,
name|dbRelationship
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
comment|/**      * @return true if objRelationship includes given dbRelationship      */
specifier|private
name|boolean
name|objRelationshipHasDbRelationship
parameter_list|(
name|ObjRelationship
name|objRelationship
parameter_list|,
name|DbRelationship
name|dbRelationship
parameter_list|)
block|{
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|objRelationship
operator|.
name|getDbRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|relationship
operator|.
name|getSourceEntityName
argument_list|()
operator|.
name|equals
argument_list|(
name|dbRelationship
operator|.
name|getSourceEntityName
argument_list|()
argument_list|)
operator|&&
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
operator|.
name|equals
argument_list|(
name|dbRelationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
operator|&&
name|isSameAttributes
argument_list|(
name|relationship
operator|.
name|getSourceAttributes
argument_list|()
argument_list|,
name|dbRelationship
operator|.
name|getSourceAttributes
argument_list|()
argument_list|)
operator|&&
name|isSameAttributes
argument_list|(
name|relationship
operator|.
name|getTargetAttributes
argument_list|()
argument_list|,
name|dbRelationship
operator|.
name|getTargetAttributes
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
comment|/**      * @param collection1 first collection to compare      * @param collection2 second collection to compare      * @return true if collections have same size and attributes in them have same names      */
specifier|private
name|boolean
name|isSameAttributes
parameter_list|(
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|collection1
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|collection2
parameter_list|)
block|{
if|if
condition|(
name|collection1
operator|.
name|size
argument_list|()
operator|!=
name|collection2
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|collection1
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|iterator1
init|=
name|collection1
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|iterator2
init|=
name|collection2
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|collection1
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|attr1
init|=
name|iterator1
operator|.
name|next
argument_list|()
decl_stmt|;
name|DbAttribute
name|attr2
init|=
name|iterator2
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|attr1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|attr2
operator|.
name|getName
argument_list|()
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
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|getIncomingRelationships
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|incoming
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|nextEntity
range|:
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|nextEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
comment|// TODO: PERFORMANCE 'getTargetEntity' is generally slow, called
comment|// in this iterator it is showing (e.g. in YourKit profiles)..
comment|// perhaps use cheaper 'getTargetEntityName()' or even better -
comment|// pre-cache all relationships by target entity to avoid O(n)
comment|// search ?
comment|// (need to profile to prove the difference)
if|if
condition|(
name|entity
operator|==
name|relationship
operator|.
name|getTargetEntity
argument_list|()
condition|)
block|{
name|incoming
operator|.
name|add
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|incoming
return|;
block|}
specifier|protected
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getRelationshipsToAdd
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|missing
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbRelationship
name|dbRel
range|:
name|objEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|shouldAddToObjEntity
argument_list|(
name|objEntity
argument_list|,
name|dbRel
argument_list|)
condition|)
block|{
name|missing
operator|.
name|add
argument_list|(
name|dbRel
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|missing
return|;
block|}
comment|/**      * Registers new EntityMergeListener      */
specifier|public
name|void
name|addEntityMergeListener
parameter_list|(
name|EntityMergeListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unregisters an EntityMergeListener      */
specifier|public
name|void
name|removeEntityMergeListener
parameter_list|(
name|EntityMergeListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Notifies all listeners that an ObjAttribute was added      */
specifier|protected
name|void
name|fireAttributeAdded
parameter_list|(
name|ObjAttribute
name|attr
parameter_list|)
block|{
for|for
control|(
name|EntityMergeListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|objAttributeAdded
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Notifies all listeners that an ObjRelationship was added      */
specifier|protected
name|void
name|fireRelationshipAdded
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
for|for
control|(
name|EntityMergeListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|objRelationshipAdded
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

