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
name|util
package|;
end_package

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
name|Map
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
name|access
operator|.
name|DataDomain
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
name|Query
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * Factory class that generates various Cayenne objects with default names that are unique  * in their corresponding context. Supports creation of the following objects:  *<ul>  *<li>DataMap</li>  *<li>ObjEntity</li>  *<li>ObjAttribute</li>  *<li>ObjRelationship</li>  *<li>DbEntity</li>  *<li>DbAttribute</li>  *<li>DbRelationship</li>  *<li>DataNodeDescriptor</li>  *<li>DataDomain</li>  *<li>Query</li>  *<li>Procedure</li>  *<li>ProcedureParameter</li>  *</ul>  * This is a helper class used mostly by GUI and database reengineering classes.  *   * @since 3.1 moved from project package  */
end_comment

begin_comment
comment|// TODO andrus 03/10/2010: should we make that a pluggable DI strategy?
end_comment

begin_comment
comment|// TODO andrus 03/01/2011: move to Modeler?
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|NamedObjectFactory
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|NamedObjectFactory
argument_list|>
name|factories
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|NamedObjectFactory
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|factories
operator|.
name|put
argument_list|(
name|DataMap
operator|.
name|class
argument_list|,
operator|new
name|DataMapFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|ObjEntity
operator|.
name|class
argument_list|,
operator|new
name|ObjEntityFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|,
operator|new
name|DbEntityFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|ObjAttribute
operator|.
name|class
argument_list|,
operator|new
name|ObjAttributeFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|DbAttribute
operator|.
name|class
argument_list|,
operator|new
name|DbAttributeFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|DataNodeDescriptor
operator|.
name|class
argument_list|,
operator|new
name|DataNodeDescriptorFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|DataChannelDescriptor
operator|.
name|class
argument_list|,
operator|new
name|DataChannelDescriptorFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|DbRelationship
operator|.
name|class
argument_list|,
operator|new
name|DbRelationshipFactory
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|ObjRelationship
operator|.
name|class
argument_list|,
operator|new
name|ObjRelationshipFactory
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|Procedure
operator|.
name|class
argument_list|,
operator|new
name|ProcedureFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|Query
operator|.
name|class
argument_list|,
operator|new
name|SelectQueryFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|ProcedureParameter
operator|.
name|class
argument_list|,
operator|new
name|ProcedureParameterFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|Embeddable
operator|.
name|class
argument_list|,
operator|new
name|EmbeddableFactory
argument_list|()
argument_list|)
expr_stmt|;
name|factories
operator|.
name|put
argument_list|(
name|EmbeddableAttribute
operator|.
name|class
argument_list|,
operator|new
name|EmbeddableAttributeFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|createName
parameter_list|(
name|Class
name|objectClass
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|(
name|factories
operator|.
name|get
argument_list|(
name|objectClass
argument_list|)
operator|)
operator|.
name|makeName
argument_list|(
name|namingContext
argument_list|)
return|;
block|}
comment|/**      * @since 1.0.5      */
specifier|public
specifier|static
name|String
name|createName
parameter_list|(
name|Class
name|objectClass
parameter_list|,
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
return|return
operator|(
name|factories
operator|.
name|get
argument_list|(
name|objectClass
argument_list|)
operator|)
operator|.
name|makeName
argument_list|(
name|namingContext
argument_list|,
name|nameBase
argument_list|)
return|;
block|}
comment|/**      * Creates an object using an appropriate factory class. If no factory is found for      * the object, NullPointerException is thrown.      *<p>      *<i>Note that newly created object is not added to the parent. This behavior can be      * changed later.</i>      *</p>      */
specifier|public
specifier|static
name|Object
name|createObject
parameter_list|(
name|Class
name|objectClass
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|(
name|factories
operator|.
name|get
argument_list|(
name|objectClass
argument_list|)
operator|)
operator|.
name|makeObject
argument_list|(
name|namingContext
argument_list|)
return|;
block|}
comment|/**      * @since 1.0.5      */
specifier|public
specifier|static
name|Object
name|createObject
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|DataMap
argument_list|>
name|objectClass
parameter_list|,
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
return|return
operator|(
name|factories
operator|.
name|get
argument_list|(
name|objectClass
argument_list|)
operator|)
operator|.
name|makeObject
argument_list|(
name|namingContext
argument_list|,
name|nameBase
argument_list|)
return|;
block|}
comment|/**      * Creates a relationship using an appropriate factory class. If no factory is found      * for the object, NullPointerException is thrown.      *<p>      *<i>Note that newly created object is not added to the parent. This behavior can be      * changed later.</i>      *</p>      */
specifier|public
specifier|static
name|Relationship
name|createRelationship
parameter_list|(
name|Entity
name|srcEnt
parameter_list|,
name|Entity
name|targetEnt
parameter_list|,
name|boolean
name|toMany
parameter_list|)
block|{
name|NamedObjectFactory
name|factory
init|=
operator|(
name|srcEnt
operator|instanceof
name|ObjEntity
operator|)
condition|?
operator|new
name|ObjRelationshipFactory
argument_list|(
name|targetEnt
argument_list|,
name|toMany
argument_list|)
else|:
operator|new
name|DbRelationshipFactory
argument_list|(
name|targetEnt
argument_list|,
name|toMany
argument_list|)
decl_stmt|;
return|return
operator|(
name|Relationship
operator|)
name|factory
operator|.
name|makeObject
argument_list|(
name|srcEnt
argument_list|)
return|;
block|}
comment|/**      * Creates a unique name for the new object and constructs this object.      */
specifier|protected
specifier|synchronized
name|String
name|makeName
parameter_list|(
name|Object
name|namingContext
parameter_list|)
block|{
return|return
name|makeName
argument_list|(
name|namingContext
argument_list|,
name|nameBase
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @since 1.0.5      */
specifier|protected
specifier|synchronized
name|String
name|makeName
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
name|int
name|c
init|=
literal|1
decl_stmt|;
name|String
name|name
init|=
name|nameBase
decl_stmt|;
while|while
condition|(
name|isNameInUse
argument_list|(
name|name
argument_list|,
name|namingContext
argument_list|)
condition|)
block|{
name|name
operator|=
name|nameBase
operator|+
name|c
operator|++
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
comment|/**      * Creates a unique name for the new object and constructs this object.      */
specifier|protected
name|Object
name|makeObject
parameter_list|(
name|Object
name|namingContext
parameter_list|)
block|{
return|return
name|makeObject
argument_list|(
name|namingContext
argument_list|,
name|nameBase
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @since 1.0.5      */
specifier|protected
name|Object
name|makeObject
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|makeName
argument_list|(
name|namingContext
argument_list|,
name|nameBase
argument_list|)
argument_list|,
name|namingContext
argument_list|)
return|;
block|}
comment|/** Returns a base default name, like "UntitledEntity", etc. */
specifier|protected
specifier|abstract
name|String
name|nameBase
parameter_list|()
function_decl|;
comment|/** Internal factory method. Invoked after the name is figured out. */
specifier|protected
specifier|abstract
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
function_decl|;
comment|/**      * Checks if the name is already taken by another sibling in the same context.      */
specifier|protected
specifier|abstract
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
function_decl|;
specifier|static
class|class
name|DataChannelDescriptorFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledDomain"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataChannelDescriptor
name|dataChDes
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|dataChDes
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|dataChDes
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|static
class|class
name|DataMapFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledMap"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|DataMap
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
comment|// null context is a situation when DataMap is a
comment|// top level object of the project
if|if
condition|(
name|namingContext
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|namingContext
operator|instanceof
name|DataDomain
condition|)
block|{
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|namingContext
decl_stmt|;
return|return
name|domain
operator|.
name|getDataMap
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
if|if
condition|(
name|namingContext
operator|instanceof
name|DataChannelDescriptor
condition|)
block|{
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|namingContext
decl_stmt|;
return|return
name|domain
operator|.
name|getDataMap
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|static
class|class
name|ObjEntityFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledObjEntity"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|ObjEntity
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
return|return
name|map
operator|.
name|getObjEntity
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|EmbeddableFactory
extends|extends
name|NamedObjectFactory
block|{
specifier|private
name|String
name|nameBase
decl_stmt|;
specifier|public
name|String
name|getNameBase
parameter_list|()
block|{
return|return
name|nameBase
return|;
block|}
specifier|public
name|void
name|setNameBase
parameter_list|(
name|String
name|nameBase
parameter_list|)
block|{
name|this
operator|.
name|nameBase
operator|=
name|nameBase
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
if|if
condition|(
name|getNameBase
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setNameBase
argument_list|(
literal|"UntitledEmbeddable"
argument_list|)
expr_stmt|;
block|}
return|return
name|getNameBase
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|Embeddable
argument_list|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|+
literal|"."
operator|+
name|name
argument_list|)
return|;
block|}
return|return
operator|new
name|Embeddable
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|map
operator|.
name|getEmbeddable
argument_list|(
operator|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|+
literal|"."
operator|+
name|name
operator|)
argument_list|)
operator|!=
literal|null
return|;
block|}
return|return
name|map
operator|.
name|getEmbeddable
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|EmbeddableAttributeFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"untitledAttr"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|EmbeddableAttribute
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|Embeddable
name|emb
init|=
operator|(
name|Embeddable
operator|)
name|namingContext
decl_stmt|;
return|return
name|emb
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|DbEntityFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledDbEntity"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|DbEntity
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
return|return
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|ProcedureParameterFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledProcedureParameter"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|ProcedureParameter
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
comment|// it doesn't matter if we create a parameter with
comment|// a duplicate name.. parameters are positional anyway..
comment|// still try to use unique names for visual consistency
name|Procedure
name|procedure
init|=
operator|(
name|Procedure
operator|)
name|namingContext
decl_stmt|;
for|for
control|(
specifier|final
name|ProcedureParameter
name|parameter
range|:
name|procedure
operator|.
name|getCallParameters
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|parameter
operator|.
name|getName
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
block|}
specifier|static
class|class
name|ProcedureFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledProcedure"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|Procedure
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
return|return
name|map
operator|.
name|getProcedure
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|SelectQueryFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledQuery"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|()
decl_stmt|;
name|query
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
return|return
name|map
operator|.
name|getQuery
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|ObjAttributeFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"untitledAttr"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|ObjAttribute
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
operator|(
name|ObjEntity
operator|)
name|namingContext
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|Entity
name|ent
init|=
operator|(
name|Entity
operator|)
name|namingContext
decl_stmt|;
return|return
name|ent
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
specifier|static
class|class
name|DbAttributeFactory
extends|extends
name|ObjAttributeFactory
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|DbAttribute
argument_list|(
name|name
argument_list|,
name|TypesMapping
operator|.
name|NOT_DEFINED
argument_list|,
operator|(
name|DbEntity
operator|)
name|namingContext
argument_list|)
return|;
block|}
block|}
specifier|static
class|class
name|DataNodeDescriptorFactory
extends|extends
name|NamedObjectFactory
block|{
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
return|return
literal|"UntitledDataNode"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|DataNodeDescriptor
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|namingContext
decl_stmt|;
name|Iterator
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodeIt
init|=
name|domain
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|nodeIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|nodeIt
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
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
block|}
specifier|static
class|class
name|ObjRelationshipFactory
extends|extends
name|NamedObjectFactory
block|{
specifier|protected
name|Entity
name|target
decl_stmt|;
specifier|protected
name|boolean
name|toMany
decl_stmt|;
specifier|public
name|ObjRelationshipFactory
parameter_list|(
name|Entity
name|target
parameter_list|,
name|boolean
name|toMany
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|toMany
operator|=
name|toMany
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|ObjRelationship
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNameInUse
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
name|Entity
name|ent
init|=
operator|(
name|Entity
operator|)
name|namingContext
decl_stmt|;
return|return
name|ent
operator|.
name|getRelationship
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**          * Returns generated name for the ObjRelationships. For to-one case and entity          * name "xxxx" it generates name "toXxxx". For to-many case and entity name "Xxxx"          * it generates name "xxxxArray".          */
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return
literal|"untitledRel"
return|;
block|}
name|String
name|name
init|=
name|target
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
operator|(
name|toMany
operator|)
condition|?
name|Character
operator|.
name|toLowerCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|+
literal|"Array"
else|:
literal|"to"
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
specifier|static
class|class
name|DbRelationshipFactory
extends|extends
name|ObjRelationshipFactory
block|{
specifier|public
name|DbRelationshipFactory
parameter_list|(
name|Entity
name|target
parameter_list|,
name|boolean
name|toMany
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|,
name|toMany
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|namingContext
parameter_list|)
block|{
return|return
operator|new
name|DbRelationship
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**          * Returns generated name for the DbRelationships. For to-one case it generates          * name "TO_XXXX". For to-many case it generates name "XXXX_ARRAY".          */
annotation|@
name|Override
specifier|protected
name|String
name|nameBase
parameter_list|()
block|{
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return
literal|"untitledRel"
return|;
block|}
name|String
name|name
init|=
name|target
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
operator|(
name|toMany
operator|)
condition|?
name|name
operator|+
literal|"_ARRAY"
else|:
literal|"TO_"
operator|+
name|name
return|;
block|}
block|}
block|}
end_class

end_unit

