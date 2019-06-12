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
name|QueryDescriptor
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ClientClassGenerationAction
extends|extends
name|ClassGenerationAction
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEMPLATES_DIR_NAME
init|=
literal|"templates/v4_1/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUBCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"client-subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUPERCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"client-superclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SINGLE_CLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"client-singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DMAP_SUBCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"client-datamap-subclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DMAP_SUPERCLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"client-datamap-superclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATAMAP_SINGLE_CLASS_TEMPLATE
init|=
name|TEMPLATES_DIR_NAME
operator|+
literal|"client-datamap-singleclass.vm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SUPERCLASS_PREFIX
init|=
literal|"_Client"
decl_stmt|;
specifier|public
name|ClientClassGenerationAction
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|defaultTemplateName
parameter_list|(
name|TemplateType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|ENTITY_SUBCLASS
case|:
return|return
name|SUBCLASS_TEMPLATE
return|;
case|case
name|ENTITY_SUPERCLASS
case|:
return|return
name|SUPERCLASS_TEMPLATE
return|;
case|case
name|ENTITY_SINGLE_CLASS
case|:
return|return
name|SINGLE_CLASS_TEMPLATE
return|;
case|case
name|EMBEDDABLE_SUBCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUBCLASS_TEMPLATE
return|;
case|case
name|EMBEDDABLE_SUPERCLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SUPERCLASS_TEMPLATE
return|;
case|case
name|EMBEDDABLE_SINGLE_CLASS
case|:
return|return
name|ClassGenerationAction
operator|.
name|EMBEDDABLE_SINGLE_CLASS_TEMPLATE
return|;
case|case
name|DATAMAP_SUPERCLASS
case|:
return|return
name|DMAP_SUPERCLASS_TEMPLATE
return|;
case|case
name|DATAMAP_SUBCLASS
case|:
return|return
name|DMAP_SUBCLASS_TEMPLATE
return|;
case|case
name|DATAMAP_SINGLE_CLASS
case|:
return|return
name|DATAMAP_SINGLE_CLASS_TEMPLATE
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported template type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
comment|/**      * @since 4.0 throws exception      */
annotation|@
name|Override
specifier|public
name|void
name|addEntities
parameter_list|(
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|entities
parameter_list|)
block|{
if|if
condition|(
operator|!
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
operator|.
name|isClientSupported
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't create client classes. Check client supported option on DataMap configuration."
argument_list|)
throw|;
block|}
if|if
condition|(
name|entities
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ObjEntity
name|entity
range|:
name|entities
control|)
block|{
if|if
condition|(
operator|!
name|entity
operator|.
name|isServerOnly
argument_list|()
condition|)
block|{
name|cgenConfiguration
operator|.
name|addArtifact
argument_list|(
operator|new
name|ClientEntityArtifact
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addQueries
parameter_list|(
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|queries
parameter_list|)
block|{
if|if
condition|(
name|cgenConfiguration
operator|.
name|getArtifactsGenerationMode
argument_list|()
operator|.
name|equals
argument_list|(
name|ArtifactsGenerationMode
operator|.
name|ALL
operator|.
name|getLabel
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|queries
operator|!=
literal|null
condition|)
block|{
name|cgenConfiguration
operator|.
name|addArtifact
argument_list|(
operator|new
name|ClientDataMapArtifact
argument_list|(
name|cgenConfiguration
operator|.
name|getDataMap
argument_list|()
argument_list|,
name|queries
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setCgenConfiguration
parameter_list|(
name|CgenConfiguration
name|cgenConfiguration
parameter_list|)
block|{
name|super
operator|.
name|setCgenConfiguration
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQueryTemplate
argument_list|(
name|DMAP_SUBCLASS_TEMPLATE
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setQuerySuperTemplate
argument_list|(
name|DMAP_SUPERCLASS_TEMPLATE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

