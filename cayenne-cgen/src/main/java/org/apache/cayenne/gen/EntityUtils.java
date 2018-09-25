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
name|gen
package|;
end_package

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
name|CayenneRuntimeException
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
name|ObjectId
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
name|Relationship
import|;
end_import

begin_comment
comment|/**  * Attributes and Methods for working with ObjEntities.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|EntityUtils
block|{
comment|// template substitution values
specifier|protected
name|String
name|subClassName
decl_stmt|;
specifier|protected
name|String
name|superClassName
decl_stmt|;
specifier|protected
name|String
name|baseClassName
decl_stmt|;
specifier|protected
name|String
name|subPackageName
decl_stmt|;
specifier|protected
name|String
name|superPackageName
decl_stmt|;
specifier|protected
name|String
name|basePackageName
decl_stmt|;
specifier|protected
name|DataMap
name|primaryDataMap
decl_stmt|;
specifier|protected
name|ObjEntity
name|objEntity
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|callbackNames
decl_stmt|;
specifier|public
name|EntityUtils
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|String
name|fqnBaseClass
parameter_list|,
name|String
name|fqnSuperClass
parameter_list|,
name|String
name|fqnSubClass
parameter_list|)
block|{
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|this
operator|.
name|baseClassName
operator|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnBaseClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|basePackageName
operator|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|fqnBaseClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|superClassName
operator|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnSuperClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|superPackageName
operator|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|fqnSuperClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|subClassName
operator|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|fqnSubClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|subPackageName
operator|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|fqnSubClass
argument_list|)
expr_stmt|;
name|this
operator|.
name|primaryDataMap
operator|=
name|dataMap
expr_stmt|;
name|this
operator|.
name|objEntity
operator|=
name|objEntity
expr_stmt|;
name|this
operator|.
name|callbackNames
operator|=
name|objEntity
operator|.
name|getCallbackMethods
argument_list|()
expr_stmt|;
block|}
name|EntityUtils
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|String
name|baseClassName
parameter_list|,
name|String
name|basePackageName
parameter_list|,
name|String
name|superClassName
parameter_list|,
name|String
name|superPackageName
parameter_list|,
name|String
name|subClassName
parameter_list|,
name|String
name|subPackageName
parameter_list|)
block|{
name|this
operator|.
name|baseClassName
operator|=
name|baseClassName
expr_stmt|;
name|this
operator|.
name|basePackageName
operator|=
name|basePackageName
expr_stmt|;
name|this
operator|.
name|superClassName
operator|=
name|superClassName
expr_stmt|;
name|this
operator|.
name|superPackageName
operator|=
name|superPackageName
expr_stmt|;
name|this
operator|.
name|subClassName
operator|=
name|subClassName
expr_stmt|;
name|this
operator|.
name|subPackageName
operator|=
name|subPackageName
expr_stmt|;
name|this
operator|.
name|primaryDataMap
operator|=
name|dataMap
expr_stmt|;
name|this
operator|.
name|objEntity
operator|=
name|objEntity
expr_stmt|;
name|this
operator|.
name|callbackNames
operator|=
name|objEntity
operator|.
name|getCallbackMethods
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return Returns the primary DataMap.      * @since 1.2      */
specifier|public
name|DataMap
name|getPrimaryDataMap
parameter_list|()
block|{
return|return
name|primaryDataMap
return|;
block|}
comment|/**      * Returns the EntityResolver for this set of DataMaps.      *       * @since 1.2      */
specifier|public
name|MappingNamespace
name|getEntityResolver
parameter_list|()
block|{
return|return
name|primaryDataMap
operator|.
name|getNamespace
argument_list|()
return|;
block|}
comment|/**      * Returns true if current ObjEntity is defined as abstract.      */
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
name|isAbstract
argument_list|(
name|objEntity
argument_list|)
return|;
block|}
comment|/**      * Returns true if current ObjEntity is defined as abstract.      */
specifier|public
name|boolean
name|isAbstract
parameter_list|(
name|ObjEntity
name|anObjEntity
parameter_list|)
block|{
return|return
name|anObjEntity
operator|!=
literal|null
operator|&&
name|anObjEntity
operator|.
name|isAbstract
argument_list|()
return|;
block|}
comment|/**      * Returns true if current ObjEntity contains at least one toMany      * relationship.      */
specifier|public
name|boolean
name|hasToManyRelationships
parameter_list|()
block|{
return|return
name|hasToManyRelationships
argument_list|(
name|objEntity
argument_list|)
return|;
block|}
comment|/**      * Returns true if an ObjEntity contains at least one toMany relationship.      */
specifier|public
name|boolean
name|hasToManyRelationships
parameter_list|(
name|ObjEntity
name|anObjEntity
parameter_list|)
block|{
if|if
condition|(
name|anObjEntity
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
name|r
range|:
name|anObjEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|r
operator|.
name|isToMany
argument_list|()
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
comment|/**      * Returns true if current ObjEntity contains at least one toMany      * relationship, ignoring those declared in superentities.      *       * @since 1.2      */
specifier|public
name|boolean
name|hasToManyDeclaredRelationships
parameter_list|()
block|{
return|return
name|hasToManyDeclaredRelationships
argument_list|(
name|objEntity
argument_list|)
return|;
block|}
comment|/**      * Returns true if an ObjEntity contains at least one toMany relationship,      * ignoring those declared in superentities.      *       * @since 1.2      */
specifier|public
name|boolean
name|hasToManyDeclaredRelationships
parameter_list|(
name|ObjEntity
name|anObjEntity
parameter_list|)
block|{
if|if
condition|(
name|anObjEntity
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
name|r
range|:
name|anObjEntity
operator|.
name|getDeclaredRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|r
operator|.
name|isToMany
argument_list|()
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
comment|/**      * Returns true if current ObjEntity contains at least one toOne      * relationship.      */
specifier|public
name|boolean
name|hasToOneRelationships
parameter_list|()
block|{
return|return
name|hasToOneRelationships
argument_list|(
name|objEntity
argument_list|)
return|;
block|}
comment|/**      * Returns true if an ObjEntity contains at least one toOne relationship.      */
specifier|public
name|boolean
name|hasToOneRelationships
parameter_list|(
name|ObjEntity
name|anObjEntity
parameter_list|)
block|{
if|if
condition|(
name|anObjEntity
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
name|r
range|:
name|anObjEntity
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|r
operator|.
name|isToMany
argument_list|()
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
comment|/**      * Returns true if current ObjEntity contains at least one toOne      * relationship, ignoring those declared in superentities.      */
specifier|public
name|boolean
name|hasToOneDeclaredRelationships
parameter_list|()
block|{
return|return
name|hasToOneDeclaredRelationships
argument_list|(
name|objEntity
argument_list|)
return|;
block|}
comment|/**      * Returns true if an ObjEntity contains at least one toOne relationship,      * ignoring those declared in superentities.      */
specifier|public
name|boolean
name|hasToOneDeclaredRelationships
parameter_list|(
name|ObjEntity
name|anObjEntity
parameter_list|)
block|{
if|if
condition|(
name|anObjEntity
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
name|r
range|:
name|anObjEntity
operator|.
name|getDeclaredRelationships
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|r
operator|.
name|isToMany
argument_list|()
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
comment|/**      * Returns the map key type for a collection relationship of type      * java.util.Map.      *       * @param relationship      *            The relationship to look up type information for.      * @return The type of the attribute keyed on.      */
specifier|public
name|String
name|getMapKeyType
parameter_list|(
specifier|final
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|ObjEntity
name|targetEntity
init|=
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
comment|// If the map key is null, then we're doing look-ups by actual object
comment|// key.
if|if
condition|(
name|relationship
operator|.
name|getMapKey
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// If it's a multi-column key, then the return type is always
comment|// ObjectId.
name|DbEntity
name|dbEntity
init|=
name|targetEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|dbEntity
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|)
condition|)
block|{
return|return
name|ObjectId
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
comment|// If it's a single column key or no key exists at all, then we
comment|// really don't
comment|// know what the key type is,
comment|// so default to Object.
return|return
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
comment|// If the map key is a non-default attribute, then fetch the attribute
comment|// and return
comment|// its type.
name|ObjAttribute
name|attribute
init|=
name|targetEntity
operator|.
name|getAttribute
argument_list|(
name|relationship
operator|.
name|getMapKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid map key '%s', no matching attribute found"
argument_list|,
name|relationship
operator|.
name|getMapKey
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|attribute
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**      * @since 4.1      * Checks is the db attribute declared for some object attribute.      * @param id - db attribute      */
specifier|public
name|boolean
name|declaresDbAttribute
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|objEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|a
lambda|->
name|id
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @return the list of all callback names registered for the entity.      * @since 3.0      */
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getCallbackNames
parameter_list|()
block|{
return|return
name|callbackNames
return|;
block|}
block|}
end_class

end_unit

