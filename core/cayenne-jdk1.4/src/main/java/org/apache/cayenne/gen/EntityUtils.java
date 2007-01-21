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
name|Iterator
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
name|Relationship
import|;
end_import

begin_comment
comment|/**  * Attributes and Methods for working with ObjEntities.  *   * @since 1.2  * @author Mike Kienenberger  */
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
name|super
argument_list|()
expr_stmt|;
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
block|}
comment|/**      * Returns class name (without a package) of the sub class associated with this      * generator.      */
specifier|public
name|String
name|getSubClassName
parameter_list|()
block|{
return|return
name|subClassName
return|;
block|}
comment|/**      * Returns the super class (without a package) of the data object class associated      * with this generator      */
specifier|public
name|String
name|getSuperClassName
parameter_list|()
block|{
return|return
name|superClassName
return|;
block|}
comment|/**      * Returns the base class (without a package) of the data object class associated with      * this generator. Class name must not include a package.      */
specifier|public
name|String
name|getBaseClassName
parameter_list|()
block|{
return|return
name|baseClassName
return|;
block|}
comment|/**      * Returns Java package name of the class associated with this generator.      */
specifier|public
name|String
name|getSubPackageName
parameter_list|()
block|{
return|return
name|subPackageName
return|;
block|}
comment|/**      * Returns<code>superPackageName</code> property that defines a superclass's      * package name.      */
specifier|public
name|String
name|getSuperPackageName
parameter_list|()
block|{
return|return
name|superPackageName
return|;
block|}
comment|/**      * Returns<code>basePackageName</code> property that defines a baseclass's      * (superclass superclass) package name.      */
specifier|public
name|String
name|getBasePackageName
parameter_list|()
block|{
return|return
name|basePackageName
return|;
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
comment|/**      * Returns true if current ObjEntity contains at least one toMany relationship.      */
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
name|Iterator
name|it
init|=
name|anObjEntity
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Relationship
name|r
init|=
operator|(
name|Relationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
comment|/**      * Returns true if current ObjEntity contains at least one toMany relationship, ignoring      * those declared in superentities.      *       * @since 1.2      */
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
comment|/**      * Returns true if an ObjEntity contains at least one toMany relationship, ignoring      * those declared in superentities.      *       * @since 1.2      */
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
name|Iterator
name|it
init|=
name|anObjEntity
operator|.
name|getDeclaredRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Relationship
name|r
init|=
operator|(
name|Relationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
comment|/**      * Returns true if current ObjEntity contains at least one toOne relationship.      */
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
name|Iterator
name|it
init|=
name|anObjEntity
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Relationship
name|r
init|=
operator|(
name|Relationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|false
operator|==
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
comment|/**      * Returns true if current ObjEntity contains at least one toOne relationship, ignoring      * those declared in superentities.      */
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
comment|/**      * Returns true if an ObjEntity contains at least one toOne relationship, ignoring      * those declared in superentities.      */
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
name|Iterator
name|it
init|=
name|anObjEntity
operator|.
name|getDeclaredRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Relationship
name|r
init|=
operator|(
name|Relationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
block|}
end_class

end_unit

