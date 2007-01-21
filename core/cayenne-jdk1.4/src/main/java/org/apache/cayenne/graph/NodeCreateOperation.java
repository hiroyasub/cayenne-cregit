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
name|graph
package|;
end_package

begin_comment
comment|/**  * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|NodeCreateOperation
extends|extends
name|NodeDiff
block|{
specifier|public
name|NodeCreateOperation
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|super
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NodeCreateOperation
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|int
name|diffId
parameter_list|)
block|{
name|super
argument_list|(
name|nodeId
argument_list|,
name|diffId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
name|tracker
operator|.
name|nodeCreated
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
name|tracker
operator|.
name|nodeRemoved
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

