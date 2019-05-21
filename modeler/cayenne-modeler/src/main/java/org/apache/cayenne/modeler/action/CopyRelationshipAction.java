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
name|modeler
operator|.
name|action
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|ConfigurationNode
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
name|modeler
operator|.
name|Application
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
name|modeler
operator|.
name|ProjectController
import|;
end_import

begin_comment
comment|/**  * Action for copying relationship(s)  */
end_comment

begin_class
specifier|public
class|class
name|CopyRelationshipAction
extends|extends
name|CopyAction
implements|implements
name|MultipleObjectsAction
block|{
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME
init|=
literal|"Copy Relationship"
decl_stmt|;
comment|/**      * Name of action if multiple attrs are selected      */
specifier|private
specifier|final
specifier|static
name|String
name|ACTION_NAME_MULTIPLE
init|=
literal|"Copy Relationships"
decl_stmt|;
specifier|public
specifier|static
name|String
name|getActionName
parameter_list|()
block|{
return|return
name|ACTION_NAME
return|;
block|}
specifier|public
name|String
name|getActionName
parameter_list|(
name|boolean
name|multiple
parameter_list|)
block|{
return|return
name|multiple
condition|?
name|ACTION_NAME_MULTIPLE
else|:
name|ACTION_NAME
return|;
block|}
specifier|public
name|CopyRelationshipAction
parameter_list|(
name|Application
name|application
parameter_list|)
block|{
name|super
argument_list|(
name|ACTION_NAME
argument_list|,
name|application
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if last object in the path contains a removable      * attribute.      */
annotation|@
name|Override
specifier|public
name|boolean
name|enableForPath
parameter_list|(
name|ConfigurationNode
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|object
operator|instanceof
name|Relationship
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|copy
parameter_list|(
name|ProjectController
name|mediator
parameter_list|)
block|{
name|Object
index|[]
name|rels
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentObjRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|rels
operator|==
literal|null
operator|||
name|rels
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|rels
operator|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDbRelationships
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|rels
operator|!=
literal|null
operator|&&
name|rels
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|rels
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

