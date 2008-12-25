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
name|access
operator|.
name|select
package|;
end_package

begin_comment
comment|/**  * A private interface that allows the builder to inject offset to a SelectDescriptor  * segment.  *   * @since 3.0  */
end_comment

begin_interface
interface|interface
name|SelectSegment
extends|extends
name|SelectDescriptor
argument_list|<
name|Object
argument_list|>
block|{
name|void
name|setColumnOffset
parameter_list|(
name|int
name|offset
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

