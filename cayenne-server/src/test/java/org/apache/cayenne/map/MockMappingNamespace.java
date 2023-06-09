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
name|Persistent
import|;
end_import

begin_class
specifier|public
class|class
name|MockMappingNamespace
implements|implements
name|MappingNamespace
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DbEntity
argument_list|>
name|dbEntities
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ObjEntity
argument_list|>
name|objEntities
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|QueryDescriptor
argument_list|>
name|queryDescriptors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Procedure
argument_list|>
name|procedures
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
literal|null
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
literal|null
return|;
block|}
specifier|public
name|void
name|addDbEntity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|dbEntities
operator|.
name|put
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addObjEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|objEntities
operator|.
name|put
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addQueryDescriptor
parameter_list|(
name|QueryDescriptor
name|queryDescriptor
parameter_list|)
block|{
name|queryDescriptors
operator|.
name|put
argument_list|(
name|queryDescriptor
operator|.
name|getName
argument_list|()
argument_list|,
name|queryDescriptor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addProcedure
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
name|procedures
operator|.
name|put
argument_list|(
name|procedure
operator|.
name|getName
argument_list|()
argument_list|,
name|procedure
argument_list|)
expr_stmt|;
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
name|dbEntities
operator|.
name|get
argument_list|(
name|name
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
name|objEntities
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|procedures
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|QueryDescriptor
name|getQueryDescriptor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|queryDescriptors
operator|.
name|get
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
name|dbEntities
operator|.
name|values
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
name|objEntities
operator|.
name|values
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
name|procedures
operator|.
name|values
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
name|queryDescriptors
operator|.
name|values
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
return|return
literal|null
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
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

