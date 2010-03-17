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
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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

begin_comment
comment|/**  * Display event for several selected objects  */
end_comment

begin_class
specifier|public
class|class
name|MultipleObjectsDisplayEvent
extends|extends
name|EventObject
block|{
specifier|private
name|ConfigurationNode
index|[]
name|nodes
decl_stmt|;
specifier|public
name|MultipleObjectsDisplayEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ConfigurationNode
index|[]
name|objects
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|this
operator|.
name|nodes
operator|=
operator|(
name|ConfigurationNode
index|[]
operator|)
name|objects
expr_stmt|;
block|}
comment|/**      * @return all paths of this event      */
specifier|public
name|ConfigurationNode
index|[]
name|getNodes
parameter_list|()
block|{
return|return
name|nodes
return|;
block|}
block|}
end_class

end_unit

