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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|MockMappingNamespace
implements|implements
name|MappingNamespace
block|{
specifier|private
name|Map
name|dbEntities
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|objEntities
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|queries
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|procedures
init|=
operator|new
name|HashMap
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
name|addQuery
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|queries
operator|.
name|put
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|,
name|query
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
operator|(
name|DbEntity
operator|)
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
operator|(
name|ObjEntity
operator|)
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
operator|(
name|Procedure
operator|)
name|procedures
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Query
name|getQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|Query
operator|)
name|queries
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Collection
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
name|getQueries
parameter_list|()
block|{
return|return
name|queries
operator|.
name|values
argument_list|()
return|;
block|}
block|}
end_class

end_unit

