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
name|unit
package|;
end_package

begin_comment
comment|/**  * Super class of test cases that use DataMap with   * one-way relationships.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|OneWayMappingCase
extends|extends
name|CayenneCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ONEWAY_ACCESS_STACK
init|=
literal|"OneWayStack"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|ONEWAY_ACCESS_STACK
argument_list|)
return|;
block|}
block|}
end_class

end_unit

