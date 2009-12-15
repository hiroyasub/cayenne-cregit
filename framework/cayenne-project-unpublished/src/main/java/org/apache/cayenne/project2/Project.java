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
name|project2
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
name|configuration
operator|.
name|ConfigurationNode
import|;
end_import

begin_comment
comment|/**  * A model of a Cayenne mapping project. A project consists of descriptors for  * DataChannel, DataNodes and DataMaps and associated filesystem files they are loaded  * from and saved to.  *   * @since 3.1  */
end_comment

begin_comment
comment|// do we even need a project wrapper around ConfigurationNode, as currently it does
end_comment

begin_comment
comment|// nothing?? Maybe in the future make it store configuration Resources for the project
end_comment

begin_comment
comment|// nodes to avoid attaching them to descriptors?
end_comment

begin_class
specifier|public
class|class
name|Project
block|{
specifier|protected
name|ConfigurationNode
name|rootNode
decl_stmt|;
specifier|public
name|Project
parameter_list|(
name|ConfigurationNode
name|rootNode
parameter_list|)
block|{
name|this
operator|.
name|rootNode
operator|=
name|rootNode
expr_stmt|;
block|}
specifier|public
name|ConfigurationNode
name|getRootNode
parameter_list|()
block|{
return|return
name|rootNode
return|;
block|}
block|}
end_class

end_unit

