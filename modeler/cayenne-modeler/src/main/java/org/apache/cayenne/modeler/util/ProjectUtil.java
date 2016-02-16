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
name|modeler
operator|.
name|util
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|Embeddable
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
name|EmbeddableAttribute
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
name|EntityResolver
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
name|MappingNamespace
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
name|query
operator|.
name|QueryDescriptor
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

begin_comment
comment|/**  * Provides utility methods to perform various manipulations with project objects.  */
end_comment

begin_class
specifier|public
class|class
name|ProjectUtil
block|{
specifier|public
specifier|static
name|void
name|setProcedureParameterName
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|parameter
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// If name hasn't changed, just return
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldName
argument_list|,
name|newName
argument_list|)
condition|)
block|{
return|return;
block|}
name|Procedure
name|procedure
init|=
name|parameter
operator|.
name|getProcedure
argument_list|()
decl_stmt|;
name|procedure
operator|.
name|removeCallParameter
argument_list|(
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|procedure
operator|.
name|addCallParameter
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setDataMapName
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|map
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// must fully relink renamed map
name|List
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<
name|DataNodeDescriptor
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DataNodeDescriptor
name|node
range|:
name|domain
operator|.
name|getNodeDescriptors
argument_list|()
control|)
if|if
condition|(
name|node
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|contains
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
name|nodes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|map
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
for|for
control|(
name|DataNodeDescriptor
name|node
range|:
name|nodes
control|)
block|{
name|node
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|remove
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|node
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
name|map
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setDataNodeName
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|,
name|DataNodeDescriptor
name|node
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|node
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setProcedureName
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|Procedure
name|procedure
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|procedure
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// If name hasn't changed, just return
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldName
argument_list|,
name|newName
argument_list|)
condition|)
block|{
return|return;
block|}
name|procedure
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeProcedure
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|map
operator|.
name|addProcedure
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
comment|// important - clear parent namespace:
name|MappingNamespace
name|ns
init|=
name|map
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns
operator|instanceof
name|EntityResolver
condition|)
block|{
operator|(
operator|(
name|EntityResolver
operator|)
name|ns
operator|)
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setQueryName
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|query
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// If name hasn't changed, just return
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldName
argument_list|,
name|newName
argument_list|)
condition|)
block|{
return|return;
block|}
name|query
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|query
operator|.
name|setDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeQueryDescriptor
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|map
operator|.
name|addQueryDescriptor
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// important - clear parent namespace:
name|MappingNamespace
name|ns
init|=
name|map
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns
operator|instanceof
name|EntityResolver
condition|)
block|{
operator|(
operator|(
name|EntityResolver
operator|)
name|ns
operator|)
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setObjEntityName
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|ObjEntity
name|entity
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|entity
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// If name hasn't changed, just return
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldName
argument_list|,
name|newName
argument_list|)
condition|)
block|{
return|return;
block|}
name|entity
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|oldName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// important - clear parent namespace:
name|MappingNamespace
name|ns
init|=
name|map
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns
operator|instanceof
name|EntityResolver
condition|)
block|{
operator|(
operator|(
name|EntityResolver
operator|)
name|ns
operator|)
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Renames a DbEntity and changes the name of all references.      */
specifier|public
specifier|static
name|void
name|setDbEntityName
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|entity
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// If name hasn't changed, just return
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|oldName
argument_list|,
name|newName
argument_list|)
condition|)
block|{
return|return;
block|}
name|entity
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
name|entity
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|removeDbEntity
argument_list|(
name|oldName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
comment|// important - clear parent namespace:
name|MappingNamespace
name|ns
init|=
name|map
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns
operator|instanceof
name|EntityResolver
condition|)
block|{
operator|(
operator|(
name|EntityResolver
operator|)
name|ns
operator|)
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Changes the name of the attribute and all references to this attribute.      */
specifier|public
specifier|static
name|void
name|setAttributeName
parameter_list|(
name|Attribute
name|attribute
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|attribute
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|Entity
name|entity
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|removeAttribute
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Changes the name of the embeddable attribute and all references to this embeddable attribute.      */
specifier|public
specifier|static
name|void
name|setEmbeddableAttributeName
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|String
name|oldName
init|=
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|attribute
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|Embeddable
name|embeddable
init|=
name|attribute
operator|.
name|getEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddable
operator|!=
literal|null
condition|)
block|{
name|embeddable
operator|.
name|removeAttribute
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
name|embeddable
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Changes the name of the attribute in all places in DataMap. */
specifier|public
specifier|static
name|void
name|setRelationshipName
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|Relationship
name|rel
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
name|rel
operator|==
literal|null
operator|||
name|rel
operator|!=
name|entity
operator|.
name|getRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|entity
operator|.
name|removeRelationship
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|rel
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Cleans any mappings of ObjEntities, ObjAttributes, ObjRelationship to the      * corresponding Db* objects that not longer exist.      */
specifier|public
specifier|static
name|void
name|cleanObjMappings
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
for|for
control|(
name|ObjEntity
name|entity
range|:
name|map
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|DbEntity
name|dbEnt
init|=
name|entity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
comment|// the whole entity mapping is invalid
if|if
condition|(
name|dbEnt
operator|!=
literal|null
operator|&&
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEnt
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
name|dbEnt
condition|)
block|{
name|clearDbMapping
argument_list|(
name|entity
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// check individual attributes
for|for
control|(
name|ObjAttribute
name|att
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
comment|// If flattenet atribute
name|String
name|dbAttributePath
init|=
name|att
operator|.
name|getDbAttributePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttributePath
operator|!=
literal|null
operator|&&
name|dbAttributePath
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|String
index|[]
name|pathSplit
init|=
name|dbAttributePath
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
comment|// If flattened attribute
if|if
condition|(
name|pathSplit
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|boolean
name|isTruePath
init|=
name|isDbAttributePathCorrect
argument_list|(
name|dbEnt
argument_list|,
name|dbAttributePath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isTruePath
condition|)
block|{
name|att
operator|.
name|setDbAttributePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|DbAttribute
name|dbAtt
init|=
name|att
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAtt
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dbEnt
operator|.
name|getAttribute
argument_list|(
name|dbAtt
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
name|dbAtt
condition|)
block|{
name|att
operator|.
name|setDbAttributePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// check individual relationships
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
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelList
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
name|rel
operator|.
name|getDbRelationships
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbRelationship
name|dbRel
range|:
name|dbRelList
control|)
block|{
name|Entity
name|srcEnt
init|=
name|dbRel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcEnt
operator|==
literal|null
operator|||
name|map
operator|.
name|getDbEntity
argument_list|(
name|srcEnt
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
name|srcEnt
operator|||
name|srcEnt
operator|.
name|getRelationship
argument_list|(
name|dbRel
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
name|dbRel
condition|)
block|{
name|rel
operator|.
name|removeDbRelationship
argument_list|(
name|dbRel
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * check if path is correct. path is correct when he consist from<code>DbRelationship</code>      * objects, each<code>DbRelationship</code> object have  following<code>DbRelationship</code>      * object as a target, last component is<code>DbAttribute</code>      *      * @param currentEnt current db entity      * @param dbAttributePath path to check      * @return if path is correct return true      */
specifier|public
specifier|static
name|boolean
name|isDbAttributePathCorrect
parameter_list|(
name|DbEntity
name|currentEnt
parameter_list|,
name|String
name|dbAttributePath
parameter_list|)
block|{
if|if
condition|(
name|currentEnt
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
index|[]
name|pathSplit
init|=
name|dbAttributePath
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
name|int
name|size
init|=
name|pathSplit
operator|.
name|length
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|size
condition|;
name|j
operator|++
control|)
block|{
name|DbRelationship
name|relationship
init|=
name|currentEnt
operator|.
name|getRelationship
argument_list|(
name|pathSplit
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationship
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|currentEnt
operator|=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
return|return
name|currentEnt
operator|.
name|getAttribute
argument_list|(
name|pathSplit
index|[
operator|(
name|size
operator|)
index|]
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**      * Clears all the mapping between this obj entity and its current db entity. Clears      * mapping between entities, attributes and relationships.      */
specifier|public
specifier|static
name|void
name|clearDbMapping
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|DbEntity
name|db_entity
init|=
name|entity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|db_entity
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ObjAttribute
name|objAttr
range|:
name|entity
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|DbAttribute
name|dbAttr
init|=
name|objAttr
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|dbAttr
condition|)
block|{
name|objAttr
operator|.
name|setDbAttributePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ObjRelationship
name|obj_rel
range|:
name|entity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
name|obj_rel
operator|.
name|clearDbRelationships
argument_list|()
expr_stmt|;
block|}
name|entity
operator|.
name|setDbEntity
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if one of relationship joins uses a given attribute as a source.      */
specifier|public
specifier|static
name|boolean
name|containsSourceAttribute
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|!=
name|relationship
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|DbJoin
name|join
range|:
name|relationship
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
name|attribute
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
comment|/**      * Returns true if one of relationship joins uses a given attribute as a target.      */
specifier|public
specifier|static
name|boolean
name|containsTargetAttribute
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|!=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|DbJoin
name|join
range|:
name|relationship
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
name|attribute
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
comment|/**      * Returns a collection of DbRelationships that use this attribute as a source.      */
specifier|public
specifier|static
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|getRelationshipsUsingAttributeAsSource
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|Entity
name|parent
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|parentRelationships
init|=
operator|(
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
operator|)
name|parent
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
name|parentRelationships
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// Iterator it = parentRelationships.iterator();
comment|// while (it.hasNext()) {
comment|// DbRelationship relationship = (DbRelationship) it.next();
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|parentRelationships
control|)
block|{
if|if
condition|(
name|ProjectUtil
operator|.
name|containsSourceAttribute
argument_list|(
name|relationship
argument_list|,
name|attribute
argument_list|)
condition|)
block|{
name|relationships
operator|.
name|add
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|relationships
return|;
block|}
comment|/**      * Returns a collection of DbRelationships that use this attribute as a source.      */
specifier|public
specifier|static
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|getRelationshipsUsingAttributeAsTarget
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|Entity
name|parent
init|=
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|DataMap
name|map
init|=
name|parent
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
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
name|Entity
name|entity
range|:
name|map
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|entity
operator|==
name|parent
condition|)
block|{
continue|continue;
block|}
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|entityRelationships
init|=
operator|(
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
operator|)
name|entity
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
for|for
control|(
name|DbRelationship
name|relationship
range|:
name|entityRelationships
control|)
block|{
if|if
condition|(
name|ProjectUtil
operator|.
name|containsTargetAttribute
argument_list|(
name|relationship
argument_list|,
name|attribute
argument_list|)
condition|)
block|{
name|relationships
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
name|relationships
return|;
block|}
block|}
end_class

end_unit

