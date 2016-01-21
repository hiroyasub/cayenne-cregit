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
name|map
operator|.
name|naming
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_enum
specifier|public
enum|enum
name|NameCheckers
implements|implements
name|NameChecker
block|{
name|dataChannelDescriptor
argument_list|(
literal|"project"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|,
name|dataMap
argument_list|(
literal|"datamap"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
block|,
name|reverseEngineering
argument_list|(
literal|"reverseEngineering"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
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
for|for
control|(
name|DataMap
name|dataMap
range|:
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|namingContext
operator|)
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
if|if
condition|(
name|dataMap
operator|!=
literal|null
operator|&&
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|!=
literal|null
operator|&&
name|dataMap
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
name|dataMap
operator|.
name|getReverseEngineering
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
block|,
name|objEntity
argument_list|(
literal|"ObjEntity"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
block|,
name|embeddable
argument_list|(
literal|"Embeddable"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
name|getEmbeddable
argument_list|(
name|map
operator|.
name|getNameWithDefaultPackage
argument_list|(
name|name
argument_list|)
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
block|,
name|embeddableAttribute
argument_list|(
literal|"untitledAttr"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
block|,
name|dbEntity
argument_list|(
literal|"db_entity"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
block|,
name|procedureParameter
argument_list|(
literal|"UntitledProcedureParameter"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
block|,
name|procedure
argument_list|(
literal|"procedure"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
block|,
name|query
argument_list|(
literal|"query"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
name|getQueryDescriptor
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
block|,
name|objAttribute
argument_list|(
literal|"untitledAttr"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|objRelationship
operator|.
name|isNameInUse
argument_list|(
name|namingContext
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
block|,
name|dbAttribute
argument_list|(
literal|"untitledAttr"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
operator|||
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
block|}
block|,
name|dataNodeDescriptor
argument_list|(
literal|"datanode"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
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
for|for
control|(
name|DataNodeDescriptor
name|dataNodeDescriptor
range|:
name|domain
operator|.
name|getNodeDescriptors
argument_list|()
control|)
block|{
if|if
condition|(
name|dataNodeDescriptor
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
block|,
name|objRelationship
argument_list|(
literal|"untitledRel"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|ObjEntity
name|ent
init|=
operator|(
name|ObjEntity
operator|)
name|namingContext
decl_stmt|;
return|return
name|dbAttribute
operator|.
name|isNameInUse
argument_list|(
name|namingContext
argument_list|,
name|name
argument_list|)
operator|||
name|ent
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|contains
argument_list|(
literal|"get"
operator|+
name|StringUtils
operator|.
name|capitalize
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
block|}
block|,
name|dbRelationship
argument_list|(
literal|"untitledRel"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|dbAttribute
operator|.
name|isNameInUse
argument_list|(
name|namingContext
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
block|,
name|objCallbackMethod
argument_list|(
literal|"ObjCallbackMethod"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isNameInUse
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|ObjEntity
name|ent
init|=
operator|(
name|ObjEntity
operator|)
name|namingContext
decl_stmt|;
return|return
name|name
operator|.
name|startsWith
argument_list|(
literal|"get"
argument_list|)
operator|&&
name|dbAttribute
operator|.
name|isNameInUse
argument_list|(
name|namingContext
argument_list|,
name|StringUtils
operator|.
name|uncapitalize
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|)
operator|||
name|ent
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|contains
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|;
specifier|public
specifier|final
name|String
name|baseName
decl_stmt|;
name|NameCheckers
parameter_list|(
name|String
name|baseName
parameter_list|)
block|{
name|this
operator|.
name|baseName
operator|=
name|baseName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|baseName
parameter_list|()
block|{
return|return
name|baseName
return|;
block|}
block|}
end_enum

end_unit

