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
name|map
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
name|Persistent
import|;
end_import

begin_comment
comment|/**  * A proxy for lazy on-demand initialization of the mapping cache.  */
end_comment

begin_class
specifier|abstract
class|class
name|ProxiedMappingNamespace
implements|implements
name|MappingNamespace
block|{
specifier|private
specifier|volatile
name|MappingNamespace
name|delegate
decl_stmt|;
name|MappingNamespace
name|getDelegate
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
name|delegate
operator|=
name|createDelegate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|delegate
return|;
block|}
specifier|protected
specifier|abstract
name|MappingNamespace
name|createDelegate
parameter_list|()
function_decl|;
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getEmbeddable
argument_list|(
name|className
argument_list|)
return|;
block|}
specifier|public
name|SQLResult
name|getResult
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getResult
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|EntityInheritanceTree
name|getInheritanceTree
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getInheritanceTree
argument_list|(
name|entityName
argument_list|)
return|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getProcedure
argument_list|(
name|procedureName
argument_list|)
return|;
block|}
specifier|public
name|QueryDescriptor
name|getQueryDescriptor
parameter_list|(
name|String
name|queryName
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getQueryDescriptor
argument_list|(
name|queryName
argument_list|)
return|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|entityClass
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|object
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getDbEntities
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getDbEntities
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|getObjEntities
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getObjEntities
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Procedure
argument_list|>
name|getProcedures
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getProcedures
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|getQueryDescriptors
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getQueryDescriptors
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Embeddable
argument_list|>
name|getEmbeddables
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getEmbeddables
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SQLResult
argument_list|>
name|getResults
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
operator|.
name|getResults
argument_list|()
return|;
block|}
block|}
end_class

end_unit

